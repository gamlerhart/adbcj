package org.adbcj.dbcj;

import java.sql.*;
import java.sql.Connection;
import java.sql.ResultSet;

/**
 * @author foooling@gmail.com
 */
public class StatementImpl implements Statement {


    protected final org.adbcj.dbcj.Connection connection;
    protected final org.adbcj.Connection realConnection;

    public StatementImpl(org.adbcj.dbcj.Connection conn){
        connection=conn;
        realConnection=connection.getRealConnection();
    }




    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        try {
            org.adbcj.ResultSet ars =realConnection.executeQuery(sql).get();
            return new ResultSetImpl(ars);

        } catch (Exception e){
            throw new SQLException("Failed to execute query: "+sql);
        }
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        try {

            org.adbcj.Result ar =realConnection.executeUpdate(sql).get();
            return (int)ar.getAffectedRows();

        } catch (Exception e){
            throw new SQLException("Failed to execute query: "+sql);
        }
    }

    @Override
    public void close() throws SQLException {
        //TODO: close behavior
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getMaxFieldSize() throws SQLException {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setMaxFieldSize(int max) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getMaxRows() throws SQLException {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setMaxRows(int max) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setEscapeProcessing(boolean enable) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getQueryTimeout() throws SQLException {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setQueryTimeout(int seconds) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void cancel() throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void clearWarnings() throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setCursorName(String name) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean execute(String sql) throws SQLException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getUpdateCount() throws SQLException {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getFetchDirection() throws SQLException {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getFetchSize() throws SQLException {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getResultSetType() throws SQLException {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addBatch(String sql) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void clearBatch() throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int[] executeBatch() throws SQLException {
        return new int[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Connection getConnection() throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean getMoreResults(int current) throws SQLException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isClosed() throws SQLException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setPoolable(boolean poolable) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isPoolable() throws SQLException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void closeOnCompletion() throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
