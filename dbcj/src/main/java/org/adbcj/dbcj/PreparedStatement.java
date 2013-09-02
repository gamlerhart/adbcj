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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author foooling@gmail.com
 */
public class PreparedStatement extends StatementImpl implements java.sql.PreparedStatement{




    protected DbFuture<PreparedUpdate> updateFuture=null;
    protected DbFuture<PreparedQuery> queryFuture=null;
    protected ArrayList<Object> params=new ArrayList<Object>();
    protected final String sql;

    @Override
    public void close(){
        //FIXME : may have problem using get() when query is big
        try{
            if(updateFuture!=null){
                updateFuture.cancel(true);
                updateFuture.get().close();
            }
            if (queryFuture!=null){
                queryFuture.cancel(true);
                queryFuture.get().close();
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            updateFuture=null;
            queryFuture=null;
            params=null;
        }

    }



    public PreparedStatement(Connection con,String sql) throws UnknownError{
        super(con);
        this.sql=sql;

        /*queryFuture=realConnection.prepareQuery(sql);

        updateFuture=realConnection.prepareUpdate(sql);*/


    }



    public Object[] getParamArray(){
        return params.toArray();
    }



    @Override
    public ResultSet executeQuery() throws SQLException {
        org.adbcj.ResultSet ars;
        try{
            if (queryFuture==null){
                queryFuture=realConnection.prepareQuery(sql);
            }
            PreparedQuery preparedQuery=queryFuture.get();
            if (params.size()==0)
                ars = preparedQuery.execute().get();
            else
                ars=preparedQuery.execute(getParamArray()).get();
            return new ResultSetImpl(ars);
        }catch (Exception e){
            throw new SQLException("Cannot execute this query SQL:"+sql);
        }
    }

    @Override
    public int executeUpdate() throws SQLException {
        org.adbcj.Result ar=null;
        try{
            if (updateFuture==null){
                updateFuture=realConnection.prepareUpdate(sql);
            }
            if (params.size()==0)
                ar=updateFuture.get().execute().get();
            else
                ar=updateFuture.get().execute(getParamArray()).get();
            return (int)ar.getAffectedRows();
        }catch (Exception e){
            throw new SQLException("Cannot execute this update SQL:"+sql);
        }
    }

    //Set value into an ArrayList
    protected void setValue(int parameterIndex,Object item){
        if (parameterIndex>params.size()){
            for(int i=params.size();i<parameterIndex;i++){
                params.add(i,null);
            }
        }
        params.set(parameterIndex-1,item);
    }

    @Override
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        setValue(parameterIndex,x);
    }

    @Override
    public void setByte(int parameterIndex, byte x) throws SQLException {
        setValue(parameterIndex,x);
    }

    @Override
    public void setShort(int parameterIndex, short x) throws SQLException {
        setValue(parameterIndex,x);
    }

    @Override
    public void setInt(int parameterIndex, int x) throws SQLException {
        setValue(parameterIndex,x);
    }

    @Override
    public void setLong(int parameterIndex, long x) throws SQLException {
        setValue(parameterIndex,x);
    }

    @Override
    public void setFloat(int parameterIndex, float x) throws SQLException {
        setValue(parameterIndex,x);
    }

    @Override
    public void setDouble(int parameterIndex, double x) throws SQLException {
        setValue(parameterIndex,x);
    }

    @Override
    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        setValue(parameterIndex,x);
    }

    @Override
    public void setString(int parameterIndex, String x) throws SQLException {
        setValue(parameterIndex,x);
    }

    @Override
    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        setValue(parameterIndex,x);
    }

    @Override
    public void setDate(int parameterIndex, Date x) throws SQLException {
        setValue(parameterIndex,x);
    }

    @Override
    public void setTime(int parameterIndex, Time x) throws SQLException {
        setValue(parameterIndex,x);
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        setValue(parameterIndex,x);
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
        setValue(parameterIndex,x);
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
