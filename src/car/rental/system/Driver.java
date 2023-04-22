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
public class Driver extends EmployeeData{
    
     private String licenceNo;
     private double hireCharge;

    public String getLicenceNo() {
        return licenceNo;
    }

    public void setLicenceNo(String licenceNo) {
        this.licenceNo = licenceNo;
    }

    public double getHireCharge() {
        return hireCharge;
    }

    public void setHireCharge(double hireCharge) {
        this.hireCharge = hireCharge;
    }
    
    
    public void setAllDriverAdditionalData(String[] arr){
        setLicenceNo(arr[0]);
        setHireCharge(Double.valueOf(arr[1]));
    }
    
    
    public void addDriverAdditionalData(){
        try {
            
            Connection con = DatabaseConnection.StablishDatabaseConnection();
            String querys = "insert into driver(DriverID,rentCharge,LicenceNo,EmpID) values(?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(querys);
            
            GenerateKeys key1=new GenerateKeys();;
            ps.setString(1, key1.getGeneratedNewID("driver"));
            ps.setDouble(2,getHireCharge() );
            ps.setString(3, getLicenceNo());
            ps.setString(4, getID());
            

            ps.executeUpdate();

            ps.close();
            con.close();

        } catch (Exception e) {
            System.out.println("DB Error Driver Additional : " + e);
            JOptionPane.showMessageDialog(null, "Driver Adding Error Found", "Data Manipulation", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void updateDriverAdditionalComponents(){
        try {
            Connection con = DatabaseConnection.StablishDatabaseConnection();
            Statement st = con.createStatement();

            String query = "update driver set rentcharge=\"" + getHireCharge() + "\",LicenceNo=\"" + getLicenceNo() + "\" where empID=\"" + getID() + "\"";

            System.out.println("Quary " + query);
            st.executeUpdate(query);

            


        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Data Updating Driver Addi Error!", "DBMS", JOptionPane.ERROR_MESSAGE);
            System.out.println("Error from 218 " + e);

        } finally {
            try {

            } catch (Exception e) {
                System.out.println("connection Closing Error " + e);
            }
        }
    }
    
    
    
    public void deleteDriverAdditionalData(String employeeID){
        try {
            Connection con=DatabaseConnection.StablishDatabaseConnection();
            Statement st=con.createStatement();
            String deleteRaw = "delete from driver where empId=\"" + employeeID + "\"";
            st.executeUpdate(deleteRaw);
            con.close();
            JOptionPane.showMessageDialog(null, "Item Removed Successfully!", "DBMS", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            System.out.println("Data Deletion Error Found Driver Additional");
        }
    }
    
    public void loadDriverAdditionalDetails(){
        try {

            Connection con = DatabaseConnection.StablishDatabaseConnection();
            if (con != null) {
                String s = "select * from driver where empID= \"" + getID() + "\"";
                

                PreparedStatement ps = con.prepareStatement(s);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    
                    setHireCharge(rs.getDouble("RentCharge"));
                    setLicenceNo(rs.getString("LicenceNo"));

                    
                }
            }

        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Data Loading driver Additional Error!", "DBMS", JOptionPane.ERROR_MESSAGE);

        } finally {
            try {
               
            } catch (Exception e) {
                System.out.println("connection Closing Error");
            }

        }
    }
     
     
}
