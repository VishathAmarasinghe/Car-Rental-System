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

/**
 *
 * @author Akila
 */
public class GenerateKeys {
    Connection con=null;
    Statement st=null;
    ResultSet rs=null;
    PreparedStatement ps=null;
    
    private String getLastIndexEmployeeTable(String peopleType){
        String employeeLastIndex="";
        try {
            con=DatabaseConnection.StablishDatabaseConnection();
            String slectMax="";
            if (peopleType.equalsIgnoreCase("customer")) {
                slectMax="Select max(CustomerID) as maxID from customer";
            }else if (peopleType.equalsIgnoreCase("employee")){
                slectMax="Select max(EmpID) as maxID from employee";
            }else if (peopleType.equalsIgnoreCase("reserve")){
                slectMax="Select max(ReservationID) as maxID from reservation";
            }else if (peopleType.equalsIgnoreCase("bill")){
                slectMax="Select max(BillNo) as maxID from bill";
            }else if (peopleType.equalsIgnoreCase("driver")){
                slectMax="Select max(DriverID) as maxID from driver";
            }
            
            ps = con.prepareStatement(slectMax);
            rs=ps.executeQuery();
            while (rs.next()) {                
                employeeLastIndex=rs.getString("maxID");
                if (employeeLastIndex==null) {
                    if (peopleType.equalsIgnoreCase("reserve")) {
                        employeeLastIndex="R000";
                    }else if (peopleType.equalsIgnoreCase("bill")) {
                        employeeLastIndex="B000";
                    }else if (peopleType.equalsIgnoreCase("driver")) {
                        employeeLastIndex="D000";
                    }
                }
                System.out.println("Max ID "+employeeLastIndex);
            }
            
            
        } catch (Exception e) {
            System.out.println("Error from 49 "+e);
        }finally{
            DatabaseConnection.removeConnection(rs, st, ps, con);
            return employeeLastIndex;
        }
        
    }
    
    private String genaratedID(String empID){
        System.out.println("Generation "+empID);
        int currentValue=Integer.parseInt(empID.substring(1));
        int newValue=currentValue+1;
        String newGenaratedID=String.format("%03d", newValue);
        if (empID.charAt(0) == 'A') {
            System.out.println("new one    " + "A" + newGenaratedID);
            return "A" + newGenaratedID;
        } else if (empID.charAt(0) == 'C') {
            System.out.println("new one    " + "C" + newGenaratedID);
            return "C" + newGenaratedID;
        } else if (empID.charAt(0) == 'R') {
            return "R" + newGenaratedID;
        } else if (empID.charAt(0) == 'B') {
            return "B" + newGenaratedID;
        } else if (empID.charAt(0) == 'D') {
            return "D" + newGenaratedID;
        } else {
            return "X" + newGenaratedID;
        }
    }
    
    
    public String getGeneratedNewID(String keyType){
        return genaratedID(getLastIndexEmployeeTable(keyType));
    }
    
}
