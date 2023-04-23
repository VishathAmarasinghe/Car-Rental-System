/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package car.rental.system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Akila
 */
public class EmployeeData extends People {

   
    private String useName;
    private String Password;
    private Connection con = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private Statement st = null;

    public EmployeeData() {

        setUseName(null);
        setPassword(null);
      
        setNIC(null);
    }

    

    public String getUseName() {
        return useName;
    }

    public void setUseName(String useName) {
        if (useName != null) {
            this.useName = useName + "123";
        } else {
            this.useName = useName;
        }

    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        if (Password != null) {
            this.Password = randomNumberGenarator(getFirstName());

        } else {
            this.Password = Password;
        }

//        this.Password = Password;
    }

    /**
     *
     * @param loadType
     * @param employeeSearchID
     * @param employeeTable
     * @param loadType
     */
    public void loadEmployeeData(String loadType, String employeeSearchID, JTable employeeTable, String loadRoleType) {
        DefaultTableModel employeeTableLoad = null;
        if (employeeTable != null) {
            employeeTableLoad = (DefaultTableModel) employeeTable.getModel();
            employeeTableLoad.getDataVector().removeAllElements();
            employeeTableLoad.fireTableDataChanged();
        }

        try {

            con = DatabaseConnection.StablishDatabaseConnection();
            if (con != null) {
                String s = "";

                if (loadType.equalsIgnoreCase("all")) {
                    if (loadRoleType != null) {
                        s = "select * from employee where role=\"" + loadRoleType + "\"";
                    } else {
                        s = "select * from employee";
                    }

                } else {
                    s = "select * from employee where empID= \"" + employeeSearchID + "\"";
                }

                ps = con.prepareStatement(s);
                rs = ps.executeQuery();
                while (rs.next()) {
                    String empID = rs.getString("empID");
                    System.out.println("Substring check " + empID.substring(0, 4));
                   
                    String[] numberArray = new String[10];
                   
                    numberArray[0] = rs.getString("phoneNo1");
                    numberArray[1] = rs.getString("phoneNo2");
                    String role = rs.getString("role");
                    String Fname = rs.getString("FirstName");
                    String Lname = rs.getString("lastName");
                    String NICno = rs.getString("NIC");
                    String email = rs.getString("Email");
                    String city = rs.getString("city");
                    String address1 = rs.getString("Address1");
                    String address2 = rs.getString("Address2");
                    

                    String[] ownerData = {empID, role, Fname, Lname, email, address1, address2, city, NICno, numberArray[0], numberArray[1]};
                    SetAllData(ownerData);

                    String[] ownerData2 = {empID, Fname, Lname, role, email, NICno, city, numberArray[0], numberArray[1]};

                    if (employeeTable != null) {
                        employeeTableLoad.addRow(ownerData2);
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

    public boolean[] InsertEmployeeData(String roleType) {
        boolean resultArray[] = {false, false};
        try {
//            if (getRole().equalsIgnoreCase("admin") || getRole().equalsIgnoreCase("cashier")) {
                setPassword(getFirstName());
                setUseName(getFirstName());
                System.out.println("password insider");
//            }
            setID(genaratedID(getLastIndexEmployeeTable("empid","employee")));
            con = DatabaseConnection.StablishDatabaseConnection();
            st = con.createStatement();

            String query = "insert into employee values(\"" + getID() + "\",\"" + getRole() + "\",\"" + getFirstName() + "\",\"" + getLastName() + "\",\"" + getEmail() + "\",\"" + getAddress1() + "\",\"" + getAddress2() + "\",\"" + getCity() + "\","
                    + "\"" + getNIC() + "\",\""+getPhoneNo1()+"\",\""+getPhoneNo2()+"\",\"" + getUseName() + "\",\"" + getPassword() + "\")";

            System.out.println("Quary " + query);
            st.executeUpdate(query);

            

            resultArray[0] = true;

            if (resultArray[0] == true) {
                MailGenerator mail1 = new MailGenerator();
                boolean result2 = mail1.sendMailsAuthentications(getEmail(), getUseName(), getPassword());
               
                resultArray[1] = result2;
            } else {
                resultArray[1] = false;
            }

            return resultArray;

        } catch (Exception e) {
            System.out.println("Error from 218 " + e);
            JOptionPane.showMessageDialog(null, "Data Inserting employee Error!", "DBMS", JOptionPane.ERROR_MESSAGE);
            return resultArray;
        } finally {

            try {

                st.close();
                con.close();
            } catch (Exception e) {
                System.out.println("connection Closing Error " + e);
            }

        }
    }

    public boolean updateEmployeeData(String customerID) {
        try {
            con = DatabaseConnection.StablishDatabaseConnection();
            Statement st = con.createStatement();
            
            System.out.println("phone aaaaaaaaa "+getPhoneNo1()+"   "+getPhoneNo2());

            String query = "update employee set FirstName=\"" + getFirstName() + "\",LastName=\"" + getLastName() + "\",Email=\"" + getEmail() + "\",Address1=\"" + getAddress1() + "\",Address2=\"" + getAddress2() + "\","
                    + "city=\"" + getCity() + "\", NIC=\""+getNIC()+"\",phoneNo1=\""+getPhoneNo1()+"\" ,phoneNo2=\""+getPhoneNo2()+"\" where empID=\"" + customerID + "\"";

            System.out.println("Quary " + query);
            st.executeUpdate(query);

            

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

    public void searchEmployee(String searchData, JTable resultTable) {

        System.out.println("searched Name " + searchData);

        String[] result = {null, null, null, null, "", ""};

        try {

            con=DatabaseConnection.StablishDatabaseConnection();
            String[] searchfeilds = {"EmpID", "firstName", "LastName", "Email", "Address1", "City", "NIC", "PhoneNo1","PhoneNo2"};
            for (int i = 0; i < searchfeilds.length; i++) {
                
                    ResultSet result1 = checkSearCustomer(con, "employee", searchfeilds[i], searchData);

                    if (result1 != null) {

                        loadEmployeeData("id", result1.getString("empID"), resultTable, null);
                        break;
                    }
                
            }

            con.close();

        } catch (Exception e) {
            System.out.println(e);
        }finally{
            try {
//                st.close();
                con.close();
            } catch (Exception e) {
                System.out.println("connection Closing Error " + e);
            }
        }
    }

    private ResultSet checkSearCustomer(Connection con, String tableName, String searchField, String searchData) {
        try {
            String singleData = "select * from " + tableName + " where " + searchField + "=\"" + searchData + "\"";
            PreparedStatement ps = con.prepareStatement(singleData);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs;
            } else {
                rs = null;
                return rs;
            }
        } catch (Exception e) {
            System.out.println("checkCustomer Search " + e);
            return null;
        }
    }

    public void SetAllData(String[] employeeData) {

        setID(employeeData[0]);
        setRole(employeeData[1]);
        setFirstName(employeeData[2]);
        setLastName(employeeData[3]);
        setEmail(employeeData[4]);
        setAddress1(employeeData[5]);
        setAddress2(employeeData[6]);
        setCity(employeeData[7]);
        setNIC(employeeData[8]);
        setPhoneNo1(employeeData[9]);
        setPhoneNo2(employeeData[10]);
        

    }

    private void getAllData() {
        System.out.println("ID " + getID());
        System.out.println("role " + getRole());
        System.out.println("firstName " + getFirstName());
        System.out.println("LastName " + getLastName());
        System.out.println("Email " + getEmail());
        System.out.println("Address1 " + getAddress1());
        System.out.println("Address2  " + getAddress2());
        System.out.println("City " + getCity());
        System.out.println("NIC " + getNIC());

    }

    private String randomNumberGenarator(String initailName) {
        int value = (int) ((Math.random()) * (5000 - 2000 + 1) + 2000);
        String randomPassword = initailName.substring(0, 2) + "Rent@" + String.valueOf(value);
        return randomPassword;
    }

    

    public String[] checkPassword(String UserNameGetter, String state, char[] passwordGetter) {
        boolean accessGetter = false;
        String[] accessArray={"false",""};
        try {
            con=DatabaseConnection.StablishDatabaseConnection();
            String slectMax = "Select EmpID, password from employee where username=\"" + UserNameGetter + "\" and role=\"" + state + "\"";

            PreparedStatement ps = con.prepareStatement(slectMax);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String password = rs.getString("password");
                
                char[] passwordArray = passwordGetter;
                String passwordString = new String(passwordArray);
                System.out.println("Max ID " + password);
                if (password.equalsIgnoreCase(passwordString)) {
                    accessGetter = true;
                    String empID=rs.getString("EmpID");
                    accessArray[0]="true";
                    accessArray[1]=empID;
                    break;
                }
            }
            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            System.out.println("Error from password checker " + e);
            JOptionPane.showMessageDialog(null, "Password Generating Error!", "DBMS", JOptionPane.ERROR_MESSAGE);
            
        }finally{
             try {
//                st.close();
                con.close();
            } catch (Exception e) {
                System.out.println("connection Closing Error " + e);
            }
        }
        return accessArray;
    }
    
    public void deleteEmployee(String clickedIndexID){
        
        try {
            con=DatabaseConnection.StablishDatabaseConnection();
            Statement st = con.createStatement();
            String deleteRaw = "delete from employee where empId=\"" + clickedIndexID + "\"";
            st.executeUpdate(deleteRaw);
            con.close();
        } catch (Exception e) {
            System.out.println("Data Deletion Error Found "+e);
        }
                    
    }
    
    
    public void loadDriverCarReservation(JTable drivercarTable,String DriverID,String reservationStatusType){
        DefaultTableModel employeeTableLoad = null;
        if (drivercarTable != null) {
            employeeTableLoad = (DefaultTableModel) drivercarTable.getModel();
            employeeTableLoad.getDataVector().removeAllElements();
            employeeTableLoad.fireTableDataChanged();
        }

        try {

            con = DatabaseConnection.StablishDatabaseConnection();
            if (con != null) {
                String s = "select reservation.reservationID, reservation.PickedUpDate,reservation.DropOffDate,Customer.firstname,reservation.VehicalNumber from reservation "+ 
                "inner join Customer on reservation.CustomerID=Customer.CustomerID where DriverID=\""+DriverID+"\" and reservationStatus=\""+reservationStatusType+"\"";



                ps = con.prepareStatement(s);
                rs = ps.executeQuery();
                while (rs.next()) {
                    
                    
                    
                    
                    String reservationID = rs.getString("reservationID");
                    String pickUpdate = rs.getString("PickedUpDate");
                    String dropDate = rs.getString("DropOffDate");
                    String customerName = rs.getString("firstname");
                    String vehicalNo = rs.getString("VehicalNumber");
                    

                    

                    String[] rentDetails = {DriverID,reservationID, customerName, vehicalNo, pickUpdate, dropDate};

                    if (drivercarTable != null) {
                        System.out.println("aaaaaaaaaaaaaa");
                        employeeTableLoad.addRow(rentDetails);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Data Loading Error in driver reservations!", "DBMS", JOptionPane.ERROR_MESSAGE);

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

}
