/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package car.rental.system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Akila
 */
public class GenerateKeys {
    
    private String getLastIndexEmployeeTable(String peopleType){
        String employeeLastIndex="";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/carrentalsystem", "root", "akila123");
            String slectMax="";
            if (peopleType.equalsIgnoreCase("customer")) {
                slectMax="Select max(CustomerID) as maxID from customer";
            }else if (peopleType.equalsIgnoreCase("employee")){
                slectMax="Select max(EmpID) as maxID from employee";
            }else if (peopleType.equalsIgnoreCase("reserve")){
                slectMax="Select max(ReservationID) as maxID from reservation";
            }
            
            PreparedStatement ps = con.prepareStatement(slectMax);
            ResultSet rs=ps.executeQuery();
            while (rs.next()) {                
                employeeLastIndex=rs.getString("maxID");
                System.out.println("Max ID "+employeeLastIndex);
            }
            
            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            System.out.println("Error from 49 "+e);
        }
        return employeeLastIndex;
    }
    
    private String genaratedID(String empID){
        System.out.println("Generation "+empID);
        int currentValue=Integer.parseInt(empID.substring(1));
        int newValue=currentValue+1;
        String newGenaratedID=String.format("%03d", newValue);
        if (empID.charAt(0)=='A') {
            System.out.println("new one    "+"A"+newGenaratedID);
            return "A"+newGenaratedID;
        }else if (empID.charAt(0)=='C'){
            System.out.println("new one    "+"C"+newGenaratedID);
            return "C"+newGenaratedID;
        }else if (empID.charAt(0)=='R'){
            return "R"+newGenaratedID;
        }else{
           return "D"+newGenaratedID;
        }
    }
    
    
    public String getGeneratedNewID(String keyType){
        return genaratedID(getLastIndexEmployeeTable(keyType));
    }
    
}
