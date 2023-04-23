/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package car.rental.system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Akila
 */
public class People {
    
    private String ID;
    private String role;
    private String FirstName;
    private String LastName;
    private String Email;
    private String Address1;
    private String Address2;
    private String City;
    private String NIC;
    
    private String phoneNo1;
    private String phoneNo2;
    
    
    private Connection con = null;
    private PreparedStatement ps = null;
    
    
    
    
    
    
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String FirstName) {
        this.FirstName = FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String LastName) {
        this.LastName = LastName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getAddress1() {
        return Address1;
    }

    public void setAddress1(String Address1) {
        this.Address1 = Address1;
    }

    public String getAddress2() {
        return Address2;
    }

    public void setAddress2(String Address2) {
        this.Address2 = Address2;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String City) {
        this.City = City;
    }

    public String getNIC() {
        return NIC;
    }

    public void setNIC(String NIC) {
        this.NIC = NIC;
    }

    

    public String getPhoneNo1() {
        return phoneNo1;
    }

    public void setPhoneNo1(String phoneNo1) {
        this.phoneNo1 = phoneNo1;
    }

    public String getPhoneNo2() {
        return phoneNo2;
    }

    public void setPhoneNo2(String phoneNo2) {
        this.phoneNo2 = phoneNo2;
    }
    
    
    
    
    public String getLastIndexEmployeeTable(String searchID,String searchTableName) {
        String returnIndex = "";
        if (searchTableName.equalsIgnoreCase("employee")) {
            returnIndex = "A000";
        } else if (searchTableName.equalsIgnoreCase("customer")) {
            returnIndex = "C000";
        } else if (searchTableName.equalsIgnoreCase("vehicalowner")) {
            returnIndex = "V000";
        }

        try {
            Connection con = DatabaseConnection.StablishDatabaseConnection();
            String slectMax = slectMax = "Select max(" + searchID + ") as maxID from " + searchTableName;
            PreparedStatement ps = con.prepareStatement(slectMax);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                returnIndex = rs.getString("maxID");
                System.out.println("Max ID " + returnIndex);
            }
            if (returnIndex == null) {
                if (searchTableName.equalsIgnoreCase("employee")) {
                    returnIndex = "A000";
                } else if (searchTableName.equalsIgnoreCase("customer")) {
                    returnIndex = "C000";
                } else if (searchTableName.equalsIgnoreCase("vehicalowner")) {
                    returnIndex = "V000";
                }
            }
            rs.close();
            ps.close();
            con.close();
            return returnIndex;
        } catch (Exception e) {
            System.out.println("Error from 49 " + e);
            return returnIndex;
        }
    }

    public String genaratedID(String empID) {
        System.out.println("Generation " + empID);
        int currentValue = Integer.parseInt(empID.substring(1));
        int newValue = currentValue + 1;
        String newGenaratedID = String.format("%03d", newValue);
        if (empID.charAt(0) == 'A') {
            System.out.println("new one    " + "A" + newGenaratedID);
            return "A" + newGenaratedID;
        }else if (empID.charAt(0) == 'V') {
            System.out.println("new one    " + "V" + newGenaratedID);
            return "V" + newGenaratedID;
        }
        else {
            System.out.println("new one    " + "C" + newGenaratedID);
            return "C" + newGenaratedID;
        }

    }
    
    
    
    
     
    
    
}
