package org.adbcj.tck.test;

import org.adbcj.Connection;
import org.adbcj.PreparedQuery;
import org.adbcj.ResultSet;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author roman.stoffel@gamlor.info
 * @since 10.05.12
 */
public class UnicodeTest extends AbstractWithConnectionManagerTest{

    @Test
    public void canReadDifferentTexts() throws Exception{
        Connection connection = connectionManager.connect().get();
        ResultSet resultSet = connection.executeQuery("SELECT textData FROM textcontent ORDER by lang").get();
        Assert.assertEquals(5,resultSet.size());

        Assert.assertEquals(resultSet.get(0).get("textData").getString(),"Die äüö sind toll");
        Assert.assertEquals(resultSet.get(1).get("textData").getString(),"English is a nice language");
        Assert.assertEquals(resultSet.get(2).get("textData").getString(),"ウィキペディア（英: Wikipedia）");
        Assert.assertEquals(resultSet.get(3).get("textData").getString(),"난 한국어 너무 좋아해요");
        Assert.assertEquals(resultSet.get(4).get("textData").getString(),"维基百科（英语：Wikipedia）");
    }
    @Test
    public void canReadWriteDelete() throws Exception{
        Connection connection = connectionManager.connect().get();
        connection.executeUpdate("INSERT INTO textcontent (lang, textData) VALUES ('fa','ویکی‌پدیا (به انگلیسی: Wikipedia)')").get();
        ResultSet resultSet = connection.executeQuery("SELECT textData FROM textcontent WHERE lang LIKE 'fa'").get();


        Assert.assertEquals(resultSet.get(0).get("textData").getString(),"ویکی‌پدیا (به انگلیسی: Wikipedia)");


        connection.executeUpdate("DELETE FROM textcontent WHERE lang LIKE 'fa'").get();


        ResultSet checkDeleted = connection.executeQuery("SELECT textData FROM textcontent WHERE lang LIKE 'fa'").get();
        Assert.assertEquals(checkDeleted.size(),0);
    }
    @Test
    public void worksWithPreparedStatements() throws Exception{
        Connection connection = connectionManager.connect().get();
        PreparedQuery statement = connection.prepareQuery("SELECT textData FROM textcontent WHERE textData LIKE ?").get();
        ResultSet resultSet = statement.execute("%한국어%").get();


        Assert.assertEquals(resultSet.get(0).get("textData").getString(),"난 한국어 너무 좋아해요");

        statement.close();
    }
}
