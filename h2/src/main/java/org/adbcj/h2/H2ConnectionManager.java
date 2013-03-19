package org.adbcj.h2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.adbcj.*;
import org.adbcj.h2.decoding.Decoder;
import org.adbcj.h2.packets.ClientHandshake;
import org.adbcj.support.AbstractConnectionManager;
import org.adbcj.support.DefaultDbFuture;
import org.adbcj.support.LoginCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author roman.stoffel@gamlor.info
 */
public class H2ConnectionManager extends AbstractConnectionManager {
    private final static Logger logger = LoggerFactory.getLogger(H2ConnectionManager.class);

    private final Bootstrap bootstrap;
    private static final String ENCODER = H2ConnectionManager.class.getName() + ".encoder";
    private static final String DECODER = H2ConnectionManager.class.getName() + ".decoder";
    private final String url;
    private final LoginCredentials credentials;
    private final Map<String, String> keys;
    private final Set<H2Connection> connections = new HashSet<H2Connection>();

    public H2ConnectionManager(String url,String host,
                               int port,
                               LoginCredentials credentials,
                               Map<String, String> properties,
                               Map<String,String> keys) {
        super(properties);
        this.url = url;
        this.credentials = credentials;
        this.keys = keys;

        bootstrap = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .remoteAddress(new InetSocketAddress(host, port))
                .handler(new ChannelInitializer(){

                    @Override
                    public void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(ENCODER, new Encoder());
                        ch.pipeline().addLast("handler", new Handler());
                    }
                });
    }

    @Override
    public DbFuture<Connection> connect() {
        if (isClosed()) {
            throw new DbSessionClosedException("Connection manager closed");
        }
        logger.debug("Starting connection");

        final ChannelFuture channelFuture = bootstrap.connect();

        final DefaultDbFuture<Connection> connectFuture = new DefaultDbFuture<Connection>(stackTracingOptions());

        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                logger.debug("Connect completed");

                Channel channel = future.channel();

                if (!future.isSuccess()) {
                    channel.close();
                    if(future.cause()!=null){
                        connectFuture.setException(future.cause());
                    }
                    return;
                }

                H2Connection connection = new H2Connection(maxQueueLength(),H2ConnectionManager.this,channel);
                channel.pipeline().addFirst(DECODER, new Decoder(connectFuture,connection));
                channel.write(
                        new ClientHandshake(credentials.getDatabase(),url,
                                credentials.getUserName(),
                                credentials.getPassword(),keys));


                if(future.cause()!=null){
                    connectFuture.setException(future.cause());
                }
                synchronized (connections){
                    connections.add(connection);
                }

            }
        });

        return connectFuture;
    }

    public void removeConnection(H2Connection connection) {
        synchronized (connections){
            connections.remove(connection);
        }
    }

    @Override
    public DbFuture<Void> doClose(CloseMode mode) throws DbException {
        ArrayList<H2Connection> connectionsCopy;
        synchronized (connections){
            connectionsCopy = new ArrayList<H2Connection>(connections);
        }
        final AtomicInteger toCloseCount = new AtomicInteger(connectionsCopy.size());
        final DefaultDbFuture closeFuture = new DefaultDbFuture<Void>(stackTracingOptions());
        for (H2Connection connection : connectionsCopy) {
            connection.close(mode).addListener(new DbListener<Void>() {
                @Override
                public void onCompletion(DbFuture<Void> future) {
                    final int toCloseConnnectionCount = toCloseCount.decrementAndGet();
                    if (toCloseConnnectionCount <= 0) {
                        shutdownBootstrapper(closeFuture);
                    }
                }
            });
        }
        if(connectionsCopy.isEmpty()){
            shutdownBootstrapper(closeFuture);
        }
        return closeFuture;
    }

    private void shutdownBootstrapper(final DefaultDbFuture closeFuture) {
        new Thread("Closing H2 ConnectionManager") {
            @Override
            public void run() {
                bootstrap.shutdown();
                closeFuture.setResult(null);
            }
        }.start();
    }
}
