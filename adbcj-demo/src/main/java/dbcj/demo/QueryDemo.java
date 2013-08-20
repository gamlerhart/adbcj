package dbcj.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created with IntelliJ IDEA.
 * User: fooling
 * Date: 13-8-16
 * Time: 上午11:02
 * To change this template use File | Settings | File Templates.
 */
public class QueryDemo {
    public static void main(String[] args){
        Connection con=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
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
            con= DriverManager.getConnection(url, name, password);
        } catch (Exception e){
            e.printStackTrace();
        }

        try{
            ps=con.prepareStatement("select * from user_info limit 234234,4");

        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            rs=ps.executeQuery();
            while (rs.next()){

                int id=rs.getInt(0);
                int n=rs.getInt(1);
                int passwd=rs.getInt(2);
                System.out.println("id:"+id+",name:"+n+",password:"+passwd);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
