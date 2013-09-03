package org.adbcj.dbcj;

import junit.framework.Assert;
import org.adbcj.*;
import org.adbcj.Connection;
import org.adbcj.ResultSet;
import org.testng.annotations.Test;

/**
 * @author foooling@gmail.com
 */
public class UtilTest {
    @Test
    public void testPreparedStatementParameter(){
        Object[] targetArray={null,567,null,"good",(short)0,3.456};
        String mockSql="select * from fake where name=? and article_id=? and time=? and title=? and reply=? and rate=?";
        PreparedStatement pstmt=new PreparedStatement(new ConnectionImpl(
                new Connection() {
                    @Override
                    public ConnectionManager getConnectionManager() {
                        return null;  //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public void beginTransaction() {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public DbFuture<Void> commit() {
                        return null;  //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public DbFuture<Void> rollback() {
                        return null;  //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public boolean isInTransaction() {
                        return false;  //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public DbFuture<ResultSet> executeQuery(String sql) {
                        return null;  //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public <T> DbFuture<T> executeQuery(String sql, ResultHandler<T> eventHandler, T accumulator) {
                        return null;  //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public DbFuture<Result> executeUpdate(String sql) {
                        return null;  //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public DbFuture<PreparedQuery> prepareQuery(String sql) {
                        return null;  //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public DbFuture<PreparedUpdate> prepareUpdate(String sql) {
                        return null;  //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public DbFuture<Void> close() throws DbException {
                        return null;  //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public DbFuture<Void> close(CloseMode closeMode) throws DbException {
                        return null;  //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public boolean isClosed() throws DbException {
                        return false;  //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public boolean isOpen() throws DbException {
                        return false;  //To change body of implemented methods use File | Settings | File Templates.
                    }
                }
        ),mockSql);

        try{
            pstmt.setInt(2, 567);
            pstmt.setShort(5,(short)0);
            pstmt.setString(4,"good");
            pstmt.setDouble(6,3.456);
        } catch (Exception e){
            Assert.fail();
        }
        for(int i=0;i<pstmt.getParamArray().length;i++){
            org.testng.Assert.assertEquals(pstmt.getParamArray()[i],targetArray[i]);
        }




    }

}
