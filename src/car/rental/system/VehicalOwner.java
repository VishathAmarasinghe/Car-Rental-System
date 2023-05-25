/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package car.rental.system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Akila
 */
public class VehicalOwner extends People {

    private ImageIcon nicImage;
    private Connection con = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private Statement st = null;

    public ImageIcon getNicImage() {
        return nicImage;
    }

    public void setNicImage(ImageIcon nicImage) {
        this.nicImage = nicImage;
    }

    public boolean InsertVOwnerData() {
        boolean result = true;
        try {

            setID(genaratedID(getLastIndexEmployeeTable("VOwnerID", "VehicalOwner")));
            con = DatabaseConnection.StablishDatabaseConnection();

            String querys = "insert into vehicalOwner(VOwnerID,FName,LName,Email,City,NIC,phoneNo) values(?,?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(querys);
            ps.setString(1, getID());
            ps.setString(2, getFirstName());

            ps.setString(3, getLastName());
            ps.setString(4, getEmail());
            ps.setString(5, getCity());
            ps.setString(6, getNIC());
            ps.setString(7, getPhoneNo1());

            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Inserted Successfully!", "DBMS", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            System.out.println("Error from 218 " + e);
            JOptionPane.showMessageDialog(null, "Data Inserting Error!", "DBMS", JOptionPane.ERROR_MESSAGE);
            result = false;

        } finally {

            try {

//                st.close();
                con.close();
            } catch (Exception e) {
                System.out.println("connection Closing Error " + e);
            }
            return result;

        }
    }

    public boolean updateOwnerData() {

        try {
            con = DatabaseConnection.StablishDatabaseConnection();
            Statement st = con.createStatement();

            String query = "update vehicalowner set FName=\"" + getFirstName() + "\",LName=\"" + getLastName() + "\",Email=\"" + getEmail() + "\", city=\"" + getCity() + "\" , NIC=\"" + getNIC() + "\" ,  phoneNo=\"" + getPhoneNo1() + "\" where VOwnerID=\"" + getID() + "\"";

            System.out.println("Quary " + query);
            st.executeUpdate(query);

            JOptionPane.showMessageDialog(null, "Data Updated Successfully!", "DBMS", JOptionPane.INFORMATION_MESSAGE);
            return true;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Data Updating Error!", "DBMS", JOptionPane.ERROR_MESSAGE);
            System.out.println("Error from 218 " + e);
            return false;
        } finally {
            try {
//                st.close();
                con.close();
            } catch (Exception e) {
                System.out.println("connection Closing Error " + e);
            }
        }
    }

    public void loadOwnerData(JTable employeeTable) {
        DefaultTableModel employeeTableLoad = null;
        if (employeeTable != null) {
            employeeTableLoad = (DefaultTableModel) employeeTable.getModel();
            employeeTableLoad.getDataVector().removeAllElements();
            employeeTableLoad.fireTableDataChanged();
        }

        try {

            con = DatabaseConnection.StablishDatabaseConnection();
            if (con != null) {
                String s = "select * from vehicalowner";

                ps = con.prepareStatement(s);
                rs = ps.executeQuery();
                while (rs.next()) {
                    String empID = rs.getString("VOwnerID");
                    System.out.println("Substring check " + empID.substring(0, 4));

                    String Fname = rs.getString("FName");
                    String Lname = rs.getString("LName");
                    String NICno = rs.getString("NIC");
                    String email = rs.getString("Email");
                    String city = rs.getString("city");
                    String phoneNo = rs.getString("phoneNo");

                    String[] ownerData = {empID, Fname, Lname, email, city, NICno, phoneNo};
                    SetAllData(ownerData);

                    if (employeeTable != null) {
                        employeeTableLoad.addRow(ownerData);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Data Loading Error!", "DBMS", JOptionPane.ERROR_MESSAGE);

        } finally {
            try {
                rs.close();
                ps.close();
                con.close();
            } catch (Exception e) {
                System.out.println("connection Closing Error");
            }

        }
    }

    public void getSingleOwnerData(String selectedIndex) {
        try {

            con = DatabaseConnection.StablishDatabaseConnection();
            setID(selectedIndex);
            if (con != null) {
                String s = "select * from vehicalowner where VOwnerID=\"" + selectedIndex + "\"";

                ps = con.prepareStatement(s);
                rs = ps.executeQuery();
                while (rs.next()) {
                    String empID = rs.getString("VOwnerID");
                    System.out.println("Substring check " + empID.substring(0, 4));

                    String Fname = rs.getString("FName");
                    String Lname = rs.getString("LName");
                    String NICno = rs.getString("NIC");
                    String email = rs.getString("Email");
                    String city = rs.getString("city");
                    String phoneNo = rs.getString("phoneNo");

                    String[] ownerData = {empID, Fname, Lname, email, city, NICno, phoneNo};
                    SetAllData(ownerData);

                }
            }

        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Data Loading Error!", "DBMS", JOptionPane.ERROR_MESSAGE);

        } finally {
            try {
                rs.close();
                ps.close();
                con.close();
            } catch (Exception e) {
                System.out.println("connection Closing Error");
            }

        }
    }

    public void SetAllData(String[] employeeData) {

        setID(employeeData[0]);
        setFirstName(employeeData[1]);
        setLastName(employeeData[2]);
        setEmail(employeeData[3]);
        setCity(employeeData[4]);
        setNIC(employeeData[5]);
        setPhoneNo1(employeeData[6]);

    }
    
    
    public void deleteSelectedOwner(String clickedIndexID){
        try {
            Connection con=DatabaseConnection.StablishDatabaseConnection();
            Statement st=con.createStatement();
            String deletePhoneNoRaw="delete from vehicalowner where VOwnerID=\""+clickedIndexID+"\"";
            st.executeUpdate(deletePhoneNoRaw);
        } catch (Exception e) {
             JOptionPane.showMessageDialog(null, "Cannot Remove Vehical Owner, Owner is Already assigned to a car and Car is already assign to a task", "DBMS", JOptionPane.ERROR_MESSAGE);
            System.out.println("Data deleting Error");
        }
    }

}
