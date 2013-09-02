package dbcj.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author foooling@gmail.com
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

                int id=rs.getInt(1);
                int n=rs.getInt(2);
                int passwd=rs.getInt(3);
                System.out.println("id:"+id+",name:"+n+",password:"+passwd);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
