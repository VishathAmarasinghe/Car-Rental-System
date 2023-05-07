/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package car.rental.system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author Akila
 */
public class DatabaseConnection {
    
    
    private static final String database="jdbc:mysql://localhost:3306/carrentalsystem";
    private static final String userName="root";
    private static final String Password="akila123";
    
    
    
    
    public static Connection StablishDatabaseConnection(){
       Connection con=null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = (Connection) DriverManager.getConnection(database,userName , Password);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Database Connection Error!","Database Connection",JOptionPane.ERROR_MESSAGE);
            System.out.println("DataBaseConnection Error "+e);
        }
        finally{
            return con;
        }
    
    }
    
    
    public static void removeConnection(ResultSet rs,Statement st,PreparedStatement ps,Connection con){
        try {
            if (rs!=null) {
                rs.close();
                rs=null;
            }
            if (st!=null) {
                st.close();
                st=null;
            }
            if (ps!=null) {
                ps.close();
                ps=null;
            }
            if (con!=null) {
                con.close();
                con=null;
            }
            
        } catch (Exception e) {
            System.out.println("Connection Dispatching Error "+e);
        }
        
        
    }
    
    
    public static void main(String[] args) {
        StablishDatabaseConnection();
    }
    
}
