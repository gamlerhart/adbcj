package org.adbcj.dbcj;

import junit.framework.Assert;
import org.adbcj.DbFuture;
import org.adbcj.PreparedQuery;
import org.testng.annotations.Test;

/**
 * Created with IntelliJ IDEA.
 * User: fooling
 * Date: 13-8-16
 * Time: 下午3:36
 * To change this template use File | Settings | File Templates.
 */
public class UtilTest {
    @Test
    public void testPreparedStatementParameter(){
        Object[] targetArray={null,567,null,"good",(short)0,3.456};
        String mockSql="select * from fake where name=? and article_id=? and time=? and title=? and reply=? and rate=?";
        PreparedStatement pstmt=new PreparedStatement(new ConnectionImpl(null){
            @Override
            public DbFuture<PreparedQuery> prepareQuery(String sql) {
                return null;
            }
        },mockSql);

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
