/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package car.rental.system;

import java.sql.Connection;
import java.sql.DriverManager;
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
    
    public static void main(String[] args) {
        StablishDatabaseConnection();
    }
    
}
