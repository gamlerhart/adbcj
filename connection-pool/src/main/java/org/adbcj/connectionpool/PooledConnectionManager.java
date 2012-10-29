package org.adbcj.connectionpool;

import org.adbcj.*;
import org.adbcj.support.AbstractConnectionManager;
import org.adbcj.support.DefaultDbFuture;
import org.adbcj.support.FutureUtils;
import org.adbcj.support.OneArgFunction;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author roman.stoffel@gamlor.info
 */
public class PooledConnectionManager extends AbstractConnectionManager implements PooledResource {
    private final ConnectionManager connectionManager;
    private final ConcurrentLinkedQueue<Connection> poolOfConnections = new ConcurrentLinkedQueue<Connection>();
    private final ConcurrentHashMap<PooledConnection,Boolean> aliveConnections = new ConcurrentHashMap<PooledConnection,Boolean>();
    private volatile boolean closed;
    public PooledConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public DbFuture<Connection> connect() {
        if(closed){
            throw new DbException("Connection manager is closed. Cannot open a new connection");
        }
        return (DbFuture) FutureUtils.map(findOrGetNewConnection(), new OneArgFunction<Connection, PooledConnection>() {
            @Override
            public PooledConnection apply(Connection arg) {
                final PooledConnection pooledConnection = new PooledConnection(arg, PooledConnectionManager.this);
                aliveConnections.put(pooledConnection, true);
                return pooledConnection;
            }
        });
    }

    private DbFuture<Connection> findOrGetNewConnection() {
        Connection connection = poolOfConnections.poll();
        if(null!=connection){
            return DefaultDbFuture.completed(connection);
        }
        return connectionManager.connect();
    }

    @Override
    public DbFuture<Void> close(CloseMode mode) throws DbException {
        closed = true;
        for (Connection pooledConnection : aliveConnections.keySet()) {
            pooledConnection.close(mode);
        }
        return connectionManager.close(mode);
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

    public DbFuture<Void> returnConnection(PooledConnection pooledConnection) {
        final DefaultDbFuture<Void> transactionReturned = new DefaultDbFuture<Void>();
        aliveConnections.remove(pooledConnection);
        final Connection nativeTx = pooledConnection.getNativeConnection();
        if(!nativeTx.isClosed() && nativeTx.isInTransaction()){
            nativeTx.rollback().addListener(new DbListener<Void>() {
                @Override
                public void onCompletion(DbFuture<Void> future) {
                    poolOfConnections.offer(nativeTx);
                    transactionReturned.setResult(null);
                }
            });
        } else {
            poolOfConnections.offer(nativeTx);
            transactionReturned.setResult(null);
        }
        return transactionReturned;
    }
}
