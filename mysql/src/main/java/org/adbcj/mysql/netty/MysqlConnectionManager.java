package org.adbcj.mysql.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.adbcj.*;
import org.adbcj.mysql.codec.ClientRequest;
import org.adbcj.mysql.codec.MySqlClientDecoder;
import org.adbcj.mysql.codec.MySqlClientEncoder;
import org.adbcj.mysql.codec.MySqlConnection;
import org.adbcj.mysql.codec.decoding.Connecting;
import org.adbcj.mysql.codec.decoding.DecoderState;
import org.adbcj.mysql.codec.packets.ServerPacket;
import org.adbcj.support.AbstractConnectionManager;
import org.adbcj.support.DefaultDbFuture;
import org.adbcj.support.LoginCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MysqlConnectionManager extends AbstractConnectionManager {

	private static final Logger logger = LoggerFactory.getLogger(MysqlConnectionManager.class);


	private static final String ENCODER = MysqlConnectionManager.class.getName() + ".encoder";
	private static final String DECODER = MysqlConnectionManager.class.getName() + ".decoder";
    private final LoginCredentials credentials;

	private final Bootstrap bootstrap;
    private final Set<MySqlConnection> connections = new HashSet<MySqlConnection>();
    private final AtomicInteger idCounter = new AtomicInteger();

    public MysqlConnectionManager(String host,
                                  int port,
                                  String username,
                                  String password,
                                  String schema,
                                  Map<String,String> properties) {
        super(properties);
        credentials = new LoginCredentials(username, password, schema);

		bootstrap = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE,true)
                .option(ChannelOption.AUTO_READ,false)
                .remoteAddress(new InetSocketAddress(host,port))
                .handler(new ChannelInitializer() {

                    @Override
                    public void initChannel(Channel ch) throws Exception {
                        ch.config().setAutoRead(false);
                        ch.pipeline().addLast(ENCODER, new Encoder());

                    }
                });
	}



    protected DbFuture<Void> doClose(CloseMode closeMode) throws DbException {
        final DefaultDbFuture closeFuture;
        ArrayList<MySqlConnection> connectionsCopy;
        synchronized (connections) {
            closeFuture = new DefaultDbFuture<Void>(stackTracingOptions());
            connectionsCopy = new ArrayList<MySqlConnection>(connections);
        }
        final AtomicInteger toCloseCount = new AtomicInteger(connectionsCopy.size());
        for (MySqlConnection connection : connectionsCopy) {
            connection.close(closeMode).addListener(new DbListener<Void>() {
                @Override
                public void onCompletion(DbFuture<Void> future) {
                    if(0==toCloseCount.decrementAndGet()){
                        shutdownBootraper(closeFuture);
                    }
                }
            });
        }
        if(connectionsCopy.isEmpty()){
            shutdownBootraper(closeFuture);
        }
        return closeFuture;
    }

    private void shutdownBootraper(final DefaultDbFuture closeFuture) {
        new Thread("Closing MySQL ConnectionManager"){
            @Override
            public void run() {
                bootstrap.group().shutdownGracefully().addListener(new GenericFutureListener() {
                    @Override
                    public void operationComplete(Future future) throws Exception {
                        if(future.isSuccess()){
                            closeFuture.setResult(null);

                        }else{
                            closeFuture.setException(future.cause());
                        }
                    }
                });
            }
        }.start();
    }

    public DbFuture<Connection> connect() {
        if (isClosed()) {
            throw new DbException("Connection manager closed");
        }
        logger.debug("Starting connection");

        final ChannelFuture channelFuture = bootstrap.connect();

        final DefaultDbFuture<Connection> connectFuture = new DefaultDbFuture<Connection>(stackTracingOptions());

        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                logger.debug("Physical connect completed");

                Channel channel = future.channel();

                if (!future.isSuccess()) {
                    channel.close();
                    if(future.cause()!=null){
                        connectFuture.setException(future.cause());
                    }
                    return;
                }

                MySqlConnection connection = new MySqlConnection(maxQueueLength(), MysqlConnectionManager.this, channel);
                channel.pipeline().addLast(DECODER, new Decoder(
                        new Connecting(connectFuture, connection, credentials),connection));
                channel.pipeline().addLast("end-handler", new Handler(connection));

                if (future.cause() != null) {
                    connectFuture.setException(future.cause());
                }


                channel.config().setAutoRead(true);
                channel.read();
            }
        });

        return connectFuture;                          }



    public int nextId() {
        return idCounter.incrementAndGet();
    }

    public void addConnection(MySqlConnection connection) {
        synchronized (connections) {
            connections.add(connection);
        }
    }

    public boolean removeConnection(MySqlConnection connection) {
        synchronized (connections) {
            return connections.remove(connection);
        }
    }

}

class Decoder extends ByteToMessageDecoder {
    private final static Logger log = LoggerFactory.getLogger(Decoder.class);
	private final MySqlClientDecoder decoder;
    private final MySqlConnection connection;

    public Decoder(DecoderState state,MySqlConnection connection) {
        decoder = new MySqlClientDecoder(state);
        this.connection = connection;
    }

    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf buffer,List<Object> out) throws Exception {
        InputStream in = new ByteBufInputStream(buffer);
        try {
            Object obj= decoder.decode(in,ctx.channel(), false);
            if(log.isDebugEnabled()&&null!=obj){
                log.debug("Decoded message: {}",obj);
            }
            if(obj!=null){
                out.add(obj);
            }
        } finally {
            in.close();
        }
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        connection.tryCompleteClose();
    }
}


class                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             Handler extends SimpleChannelInboundHandler<ServerPacket> {
    private final MySqlConnection connection;
    private static final Logger logger = LoggerFactory.getLogger(Handler.class);

    Handler(MySqlConnection connection) {
        this.connection = connection;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ServerPacket item) throws Exception {
        // The binary decoder is a state machine,
        // which decodes according to the expected state.
        // and directly returns to the connection
        // Not elegant, but works
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        throw new Error("Not implemented yet: TODO");  //TODO: Implement
    }


}

@ChannelHandler.Sharable
class Encoder extends MessageToByteEncoder<ClientRequest> {
    private final static Logger log = LoggerFactory.getLogger(Encoder.class);

	private final MySqlClientEncoder encoder = new MySqlClientEncoder();

    @Override
    public void encode(ChannelHandlerContext ctx, ClientRequest msg, ByteBuf buffer) throws Exception {
        if(log.isDebugEnabled()){
            log.debug("Sending request: {}", msg);
        }

        ByteBufOutputStream out = new ByteBufOutputStream(buffer);
        encoder.encode(msg, out);
        out.close();
    }
}

