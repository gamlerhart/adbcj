package dbcj.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
/**
 * Created with IntelliJ IDEA.
 * User: fooling
 * Date: 13-8-15
 * Time: 下午5:14
 * To change this template use File | Settings | File Templates.
 */
public class UpdateDemo {
    public static void main(String[] args){
        Connection con=null;
        PreparedStatement ps=null;
        int count=0;
        try{
            Class.forName("org.adbcj.dbcj.Driver");
        }catch (Exception e){
            e.printStackTrace();
        }
        String url="jdbc:mysql://localhost/adbcjtck";
        String name="adbcjtck";
        String password="adbcjtck";
        try{
            con= DriverManager.getConnection(url,name,password);
        } catch (Exception e){
            e.printStackTrace();
        }

        try{
            ps=con.prepareStatement("insert into user_info values(?,3,?)");

        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            ps.setInt(1,544);
            ps.setInt(2,55453);
            count=ps.executeUpdate();
        } catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(count);
    }
}
