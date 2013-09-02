package dbcj.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author foooling@gmail.com
 */
public class MainDemo {

    static {
        try{
            Class.forName("org.adbcj.dbcj.Driver");
            //Uncomment would still work
            //Class.forName("com.mysql.jdbc.Driver");
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private static void initTable(Connection connection) throws Exception{
        PreparedStatement pstmt=connection.prepareStatement("CREATE TABLE IF NOT EXISTS user_info(\n" +
                "  id int(11) NOT NULL AUTO_INCREMENT,\n" +
                "  name varchar(255) NOT NULL,\n" +
                "  passwd varchar(255) NOT NULL,\n" +
                "  PRIMARY KEY (id)\n" +
                ") ENGINE = INNODB;");
        pstmt.executeUpdate();
        pstmt.close();
    }
    private static void insertValues(Connection connection) throws Exception{

        PreparedStatement preparedStatement=connection.prepareStatement("insert into `user_info`(`name`,`passwd`) values(?,?)");
        preparedStatement.setString(1,"jack");
        preparedStatement.setString(2,"pass1");
        preparedStatement.executeUpdate();
        preparedStatement.setString(1,"alice");
        preparedStatement.executeUpdate();
        preparedStatement.setString(1,"bob");
        preparedStatement.setString(2,"pass2");
        preparedStatement.executeUpdate();
    }
    private static void checkResults(Connection connection) throws Exception{
        ResultSet resultSet=null;
        PreparedStatement preparedStatement=connection.prepareStatement("select * from `user_info` order by id desc");
        try {
            resultSet=preparedStatement.executeQuery();
        } catch (Exception e){
            throw new Exception("Cannot get result set");
        }
        while(resultSet.next()){
            int userid=resultSet.getInt(1);
            String username= resultSet.getString(2);
            String password= resultSet.getString(3);
            System.out.println(userid+" "+username+" "+password);
        }
    }

    private static void clearTable(Connection connection) throws Exception{
        PreparedStatement preparedStatement=connection.prepareStatement("drop table `user_info`");
        preparedStatement.executeUpdate();
    }

    public static void main(String[] args){
        String url="jdbc:mysql://localhost/adbcjtck";
        String user="adbcjtck";
        String password="adbcjtck";
        Connection conn=null;
        try{
            conn= DriverManager.getConnection(url,user,password);

            initTable(conn);
            insertValues(conn);
            checkResults(conn);
            clearTable(conn);
            //conn.close();

        } catch (Exception e){
            e.printStackTrace();
        }




    }
}
