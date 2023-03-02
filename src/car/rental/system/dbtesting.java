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
            Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/employee", "root", "");
            String s="Select * from salgrade";
            PreparedStatement ps = con.prepareStatement(s);
            ResultSet rs=ps.executeQuery();
            while (rs.next()) {                
                String name=rs.getString("grade");
                System.out.println(name);

            }
                        
            
            
            
        } catch (Exception e) {
            System.out.println(e);
        }
        
    }
    
}
