/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package car.rental.system;

//import com.sun.jdi.connect.spi.Connection;
import java.sql.*;

/**
 *
 * @author Akila
 */
public class dbtesting {
    
    public static void main(String[] args) {
        try {
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/apple", "root", "akila123");
            Statement st=con.createStatement();
            String s="insert into details values(1,\"vvs\",\"lls\")";
            st.executeUpdate(s);
            
            con.close();
            
            
                        
            
            
            
        } catch (Exception e) {
            System.out.println(e);
        }

        randomNumberGenarator("ada");
        
    }
    
    private static String randomNumberGenarator(String initailName){
       int value=(int)((Math.random())*(5000-2000+1)+2000);
        System.out.println(value);
        return "";
    }
    
}
