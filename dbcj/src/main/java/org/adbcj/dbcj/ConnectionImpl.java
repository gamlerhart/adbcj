package org.adbcj.dbcj;

import org.adbcj.ConnectionManagerProvider;
import org.adbcj.DbFuture;
import org.adbcj.PreparedQuery;
import org.adbcj.PreparedUpdate;

import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * @author foooling@gmail.com
 */
public class ConnectionImpl implements Connection {


    protected final org.adbcj.Connection realConnection;
    public ConnectionImpl(org.adbcj.Connection rc){
        realConnection=rc;

    }


    @Override
    public org.adbcj.Connection getRealConnection(){
        if (realConnection.isClosed()){
            return null;
        }
        return realConnection;
    }






    @Override
    public Statement createStatement() throws SQLException {
        return new StatementImpl(this);
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return new org.adbcj.dbcj.PreparedStatement(this,sql);
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void commit() throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void rollback() throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void close() throws SQLException {
        try{

            realConnection.close().get();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean isClosed() throws SQLException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getCatalog() throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
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
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getHoldability() throws SQLException {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Clob createClob() throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Blob createBlob() throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public NClob createNClob() throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setSchema(String schema) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getSchema() throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
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
