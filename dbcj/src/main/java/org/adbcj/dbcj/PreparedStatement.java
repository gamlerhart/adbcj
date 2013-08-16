package org.adbcj.dbcj;

import org.adbcj.DbFuture;
import org.adbcj.PreparedQuery;
import org.adbcj.PreparedUpdate;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.sql.ResultSet;
import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: fooling
 * Date: 13-8-15
 * Time: 下午3:32
 * To change this template use File | Settings | File Templates.
 */
public class PreparedStatement extends StatementImpl implements java.sql.PreparedStatement{
    private static enum Type {
        UPDATE,
        QUERY
    }
    private final Connection connection;
    private final Type type;
    private final DbFuture<PreparedUpdate> updateFuture;
    private final DbFuture<PreparedQuery> queryFuture;
    private Object[] params=null;






    public PreparedStatement(Connection con,String sql) throws UnknownError{
        connection=con;
        type=getQueryType(sql);

        if(type==Type.QUERY){
            queryFuture=connection.prepareQuery(sql);
            updateFuture=null;
        }else {
            queryFuture=null;
            updateFuture=connection.prepareUpdate(sql);
        }
    }



    public Type getQueryType(String sql) throws UnsupportedOperationException {

        switch (Character.toLowerCase(sql.charAt(0))){
            case 's':
                return Type.QUERY;
            case 'u':
            case 'i':
            case 'd':
                return Type.UPDATE;
            default:
                throw new UnsupportedOperationException("Not supported query");

        }
    }


    @Override
    public ResultSet executeQuery() throws SQLException {
        if (type != Type.QUERY){
            throw new SQLException("Not supported query");
        }
        try{
            PreparedQuery preparedQuery=queryFuture.get();
            org.adbcj.ResultSet ars=preparedQuery.execute(params).get();
            //TODO: change org.adbcj.ResultSet into java.sql.ResultSet
            return null;
        }catch (Exception e){
            throw new SQLException("Unknown situation");
        }
    }

    @Override
    public int executeUpdate() throws SQLException {
        if (type!=Type.UPDATE)
            throw new SQLException("Not supported update");
        try{
            org.adbcj.Result ar=updateFuture.get().execute().get();
            return (int)ar.getAffectedRows();
        }catch (Exception e){
            throw new SQLException("Unknown situation");
        }
    }

    @Override
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setByte(int parameterIndex, byte x) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setShort(int parameterIndex, short x) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setInt(int parameterIndex, int x) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setLong(int parameterIndex, long x) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setFloat(int parameterIndex, float x) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setDouble(int parameterIndex, double x) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setString(int parameterIndex, String x) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setDate(int parameterIndex, Date x) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setTime(int parameterIndex, Time x) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void clearParameters() throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setObject(int parameterIndex, Object x) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean execute() throws SQLException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addBatch() throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setRef(int parameterIndex, Ref x) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setBlob(int parameterIndex, Blob x) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setClob(int parameterIndex, Clob x) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setArray(int parameterIndex, Array x) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setURL(int parameterIndex, URL x) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setNString(int parameterIndex, String value) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setNClob(int parameterIndex, NClob value) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setClob(int parameterIndex, Reader reader) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}