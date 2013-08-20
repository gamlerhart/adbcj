package org.adbcj.dbcj;

import org.adbcj.ConnectionManagerProvider;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.sql.DriverManager;

/**
 * Created with IntelliJ IDEA.
 * User: fooling
 * Date: 13-8-16
 * Time: 下午1:23
 * To change this template use File | Settings | File Templates.
 */


@Test
public class ConnectionTest {
    @Parameters({"url","user","password"})
    @Test
    public void assumeIsAdbcjProtocol(String url) throws Exception{
        if (!url.startsWith("adbcj:")){
            Assert.fail();
        }

    }
    @Parameters({"url","user","password"})
    @Test
    public void connectAndNormalSelect(String url,String user,String password){
        String sql="select 1";


        try {
            new Driver();
            java.sql.Connection con =DriverManager.getConnection(url,user,password);
            java.sql.PreparedStatement pstmt=con.prepareStatement(sql);
            java.sql.ResultSet resultSet =pstmt.executeQuery();
            resultSet.next();
            Assert.assertEquals(resultSet.getInt(0),1);

        } catch (Exception e){
            e.printStackTrace();
            Assert.fail();
        }

    }
}
