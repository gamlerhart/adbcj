package org.adbcj.connectionpool;

import org.adbcj.*;
import org.adbcj.support.DefaultDbFuture;
import org.adbcj.support.DefaultDbSessionFuture;
import org.adbcj.support.FutureUtils;
import org.adbcj.support.OneArgFunction;

import java.util.*;

/**
 * @author roman.stoffel@gamlor.info
 */
public final class PooledConnection implements Connection, PooledResource {
    private final Connection nativeConnection;
    private final PooledConnectionManager pooledConnectionManager;
    private volatile DefaultDbFuture<Void> closingFuture;
    private final Map<DbFuture,DefaultDbFuture> runningOperations = new HashMap<DbFuture, DefaultDbFuture>();
    private final Set<AbstractPooledPreparedStatement> openStatements = new HashSet<AbstractPooledPreparedStatement>();
    private final Object collectionsLock = new Object();
    private final DbListener operationsListener = new DbListener() {
        @Override
        public void onCompletion(DbFuture future) {
            synchronized (collectionsLock) {
                runningOperations.remove(future);
                if (isClosed()) {
                    mayFinallyCloseConnection();
                }
            }
        }
    };

    public PooledConnection(Connection nativConnection, PooledConnectionManager pooledConnectionManager) {
        this.nativeConnection = nativConnection;
        this.pooledConnectionManager = pooledConnectionManager;
    }

    @Override
    public ConnectionManager getConnectionManager() {
        return pooledConnectionManager;
    }

    @Override
    public void beginTransaction() {
        checkClosed();
        nativeConnection.beginTransaction();
    }

    @Override
    public DbSessionFuture<Void> commit() {
        checkClosed();
        return monitor(nativeConnection.commit());
    }

    @Override
    public DbSessionFuture<Void> rollback() {
        checkClosed();
        return monitor(nativeConnection.rollback());
    }

    @Override
    public boolean isInTransaction() {
        checkClosed();
        return nativeConnection.isInTransaction();
    }

    @Override
    public DbSessionFuture<ResultSet> executeQuery(String sql) {
        checkClosed();
        return monitor(nativeConnection.executeQuery(sql));
    }

    @Override
    public <T> DbSessionFuture<T> executeQuery(String sql, ResultHandler<T> eventHandler, T accumulator) {
        checkClosed();
        return monitor(nativeConnection.executeQuery(sql, eventHandler, accumulator));
    }

    @Override
    public DbSessionFuture<Result> executeUpdate(String sql) {
        checkClosed();
        return monitor(nativeConnection.executeUpdate(sql));
    }

    @Override
    public DbSessionFuture<PreparedQuery> prepareQuery(String sql) {
        checkClosed();
        return monitor(nativeConnection.prepareQuery(sql), new OneArgFunction<PreparedQuery, PreparedQuery>() {
            @Override
            public PreparedQuery apply(PreparedQuery arg) {
                final PooledPreparedQuery pooledPreparedQuery = new PooledPreparedQuery(arg, PooledConnection.this);
                addStatement(pooledPreparedQuery);
                return pooledPreparedQuery;
            }
        });
    }

    @Override
    public DbSessionFuture<PreparedUpdate> prepareUpdate(String sql) {
        checkClosed();
        return monitor(nativeConnection.prepareUpdate(sql), new OneArgFunction<PreparedUpdate, PreparedUpdate>() {
            @Override
            public PreparedUpdate apply(PreparedUpdate arg) {
                final PooledPreparedUpdate pooledPreparedUpdate = new PooledPreparedUpdate(arg, PooledConnection.this);
                addStatement(pooledPreparedUpdate);
                return pooledPreparedUpdate;
            }
        });
    }

    @Override
    public DbFuture<Void> close() throws DbException {
        return close(CloseMode.CLOSE_GRACEFULLY);
    }

    @Override
    public DbFuture<Void> close(CloseMode closeMode) throws DbException {
        synchronized (collectionsLock) {
            if (isClosed()) {
                return closingFuture;
            }
            closingFuture = new DefaultDbFuture<Void>();
            if (closeMode == CloseMode.CANCEL_PENDING_OPERATIONS) {
                ArrayList<Map.Entry<DbFuture,DefaultDbFuture>> iterationCopy
                        = new ArrayList<Map.Entry<DbFuture,DefaultDbFuture>>(runningOperations.entrySet());
                for (Map.Entry<DbFuture,DefaultDbFuture> runningOperation : iterationCopy) {
                    runningOperation.getValue().trySetException(new DbSessionClosedException());
                    runningOperation.getKey().cancel(true);
                }
            }
            mayFinallyCloseConnection();
        }
        return closingFuture;
    }

    @Override
    public boolean isClosed() throws DbException {
        return closingFuture != null;
    }

    @Override
    public boolean isOpen() throws DbException {
        return nativeConnection.isOpen();
    }

    private void addStatement(AbstractPooledPreparedStatement statement) {
        synchronized (collectionsLock){
            openStatements.add(statement);
        }
    }

    private void mayFinallyCloseConnection() {
        assert Thread.holdsLock(collectionsLock);
        if(runningOperations.isEmpty() && !openStatements.isEmpty()){
            ArrayList<AbstractPooledPreparedStatement> stmts = new ArrayList<AbstractPooledPreparedStatement>(openStatements);
            for (AbstractPooledPreparedStatement openStatement : stmts) {
                openStatements.remove(openStatement);
                openStatement.close();
            }
        } else if(runningOperations.isEmpty() && openStatements.isEmpty()){
            finallyClose();
        }
    }

    <T> DbFuture<T> monitor(DbFuture<T> futureToMonitor) {
        return monitor(futureToMonitor,OneArgFunction.ID_FUNCTION);
    }
    <TArgument,TResult> DbFuture<TResult> monitor(DbFuture<TArgument> futureToMonitor,
                                                  OneArgFunction<TArgument,TResult> transform) {
        final DefaultDbFuture<TResult> newFuture = FutureUtils.map(futureToMonitor, transform);
        addMonitoring(futureToMonitor, newFuture);
        return newFuture;
    }

    <T> DbSessionFuture<T> monitor(DbSessionFuture<T> futureToMonitor) {
        return monitor(futureToMonitor,OneArgFunction.ID_FUNCTION);
    }

    <TArgument,TResult>DbSessionFuture<TResult> monitor(DbSessionFuture<TArgument> futureToMonitor,
                                   OneArgFunction<TArgument,TResult> transform) {
        final DefaultDbSessionFuture<TResult> newFuture = FutureUtils.map(futureToMonitor, transform);
        addMonitoring(futureToMonitor, newFuture);
        return newFuture;
    }

    private <TArgument, TResult> void addMonitoring(DbFuture<TArgument> futureToMonitor, DefaultDbFuture<TResult> newFuture) {
        synchronized (collectionsLock){
            runningOperations.put(futureToMonitor, newFuture);
            futureToMonitor.addListener(operationsListener);
        }
    }


    Connection getNativeConnection() {
        return nativeConnection;
    }

    void checkClosed() {
        if (isClosed()) {
            throw new DbSessionClosedException("This connection is already closed");
        }
    }

    private void finallyClose() {
        if (closingFuture.trySetResult(null)) {
            pooledConnectionManager.returnConnection(this);
        }
    }

    void removeResource(AbstractPooledPreparedStatement dbListener) {
        synchronized (collectionsLock){
            openStatements.remove(dbListener);
        }
    }
}
