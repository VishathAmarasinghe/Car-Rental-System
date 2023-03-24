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
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Akila
 */
public class CustomerData extends People{
    
    
//    private String role;
//    private String FirstName;
//    private String LastName;
//    private String Email;
//    private String Address1;
//    private String Address2;
//    private String City;
//    private String NIC;
//    private String phoneNo1;
//    private String phoneNo2;
    
    
    
    
    
    
    

    
    
    
    
    public void loadCustomerData(String loadType,String CustomerSearchID ,JTable customerTable){
        DefaultTableModel customerTableLoad=null;
        if (customerTable!=null) {
            customerTableLoad=(DefaultTableModel)customerTable.getModel();
            customerTableLoad.getDataVector().removeAllElements();
            customerTableLoad.fireTableDataChanged();
        }
        
        
        try {
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/carrentalsystem", "root", "akila123");
            String s="";
            
            if (loadType.equalsIgnoreCase("all")) {
               s ="select * from customer";
            }else{
               s="select * from customer where customerID= \""+CustomerSearchID+"\""; 
            }
            
            PreparedStatement ps = con.prepareStatement(s);
            ResultSet rs=ps.executeQuery();
            while (rs.next()) {                
                String empID=rs.getString("customerID");
                System.out.println("Substring check "+empID.substring(0,4));
                String sd="select phoneNo from customerphone where customerID=\""+empID+"\"";
                PreparedStatement psd = con.prepareStatement(sd);
                ResultSet numberResult=psd.executeQuery();
                String[] numberArray=new String[2];
                int count=0;
                while (numberResult.next()) {                    
                    numberArray[count]=numberResult.getString("phoneNo");
                    count++;
                }
                psd.close();
                numberResult.close();
                String role=rs.getString("role");
                String Fname=rs.getString("FirstName");
                String Lname=rs.getString("lastName");
                String NICno=rs.getString("NIC");
                String email=rs.getString("Email");
                String city=rs.getString("city");
                String address1=rs.getString("Address1");
                String address2=rs.getString("Address2");
               
                String[] ownerData={empID,role,Fname,Lname,email,address1,address2,city,NICno,numberArray[0],numberArray[1]};
                SetAllData(ownerData);
                
                String[] ownerData2={empID,Fname,Lname,role,NICno,email,city,numberArray[0],numberArray[1]};
                
                if (customerTable!=null) {
                    customerTableLoad.addRow(ownerData2);
                }

            }
            rs.close();
            ps.close();
            con.close();
 
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public boolean InsertCustomerData(){
        
        try {
            getAllData();
            setID(genaratedID(getLastIndexEmployeeTable()));
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/carrentalsystem", "root", "akila123");
            Statement st=con.createStatement();
           
            String query="insert into customer values(\""+getID()+"\",\"Customer\",\""+getFirstName()+"\",\""+getLastName()+"\",\""+getEmail()+"\",\""+getAddress1()+"\",\""+getAddress2()+"\",\""+getCity()+"\",\""+getNIC()+"\",null)";

            System.out.println("Quary "+query);
            st.executeUpdate(query);

            System.out.println("Executed Succeffull");
            String phoneNoQuery1="insert into customerphone values(\""+getID()+"\","+getPhoneNo1()+")";
            st.executeUpdate(phoneNoQuery1);
            String phoneNoQuery2="insert into customerphone values(\""+getID()+"\","+getPhoneNo2()+")";
            st.executeUpdate(phoneNoQuery2);
            
            
            return true;
            
        } catch (Exception e) {
            System.out.println("Error from 218 "+e);
            return false;
        }
    }
    
    
    
    public boolean updateCustomer(String customerID){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/carrentalsystem", "root", "akila123");
            Statement st=con.createStatement();
            
            
            
            String query="update customer set FirstName=\""+getFirstName()+"\",LastName=\""+getLastName()+"\",Email=\""+getEmail()+"\",Address1=\""+getAddress1()+"\",Address2=\""+getAddress2()+"\","
                            + "city=\""+getCity()+"\" where CustomerID=\""+customerID+"\"";

            System.out.println("Quary "+query);
            st.executeUpdate(query);

            String updatePhoneNo="update customerphone set phoneNo="+getPhoneNo1()+" where customerID=\""+customerID+"\"";
            st.executeUpdate(updatePhoneNo);
            
            return true;
            
        }catch (Exception e) {
            
            System.out.println("Error from 218 "+e);
            return false;
        }
    }
    
    public void searchCustomer(String searchData){
         
 
       System.out.println("searched Name "+searchData);
        
        String[] result={null,null,null,null,"",""};
        
        try {
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/carrentalsystem", "root", "akila123");
            String[] searchfeilds={"CustomerID","firstName","LastName","Email","Address1","City","NIC","PhoneNo"};
            for (int i = 0; i < searchfeilds.length; i++) {
                if (i==7) {
                    ResultSet phoneResult1=checkSearCustomer(con,"customerphone", "phoneNo", searchData);
                    if (phoneResult1!=null) {
                           loadCustomerData("id", phoneResult1.getString("CustomerID"),null);

                    break;  
                                    }
                }else{
                    ResultSet result1=checkSearCustomer(con,"customer", searchfeilds[i], searchData);
                
                    if (result1!=null) {
                      
                        loadCustomerData("id", result1.getString("CustomerID"),null);
                        break;  
                    }
                }
               }

            con.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    
    private ResultSet checkSearCustomer(Connection con,String tableName,String searchField,String searchData){
        try{
            String singleData="select * from "+tableName+" where "+searchField+"=\""+searchData+"\"";
            PreparedStatement ps = con.prepareStatement(singleData);
            ResultSet rs=ps.executeQuery();
            
            if (rs.next()) {
                return rs;
            }else{
                rs=null;
                return rs;
            }
        }catch(Exception e){
                System.out.println("checkCustomer Search "+e);
                return null;
        }
    }

    
    
    
    
    public void SetAllData(String[] CustomerDataArray){
        setNIC(CustomerDataArray[8]);
        setID(CustomerDataArray[0]);
        setRole(CustomerDataArray[1]);
        setFirstName(CustomerDataArray[2]);
        setLastName(CustomerDataArray[3]);
        setEmail(CustomerDataArray[4]);
        setAddress1(CustomerDataArray[5]);
        setAddress2(CustomerDataArray[6]);
        setCity(CustomerDataArray[7]);
        setPhoneNo1(CustomerDataArray[9]);
        setPhoneNo2(CustomerDataArray[10]);
        
    }
    
    private void getAllData(){
        System.out.println("ID "+getID());
        System.out.println("role "+getRole());
        System.out.println("firstName "+getFirstName());
        System.out.println("LastName "+getLastName());
        System.out.println("Email "+getEmail());
        System.out.println("Address1 "+getAddress1());
        System.out.println("Address2  "+getAddress2());
        System.out.println("City "+getCity());
        System.out.println("NIC "+getNIC());
        
        
    }
    
    
    private String getLastIndexEmployeeTable(){
        String returnIndex="C000";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/carrentalsystem", "root", "akila123");
            String slectMax=slectMax="Select max(CustomerID) as maxID from Customer";
            PreparedStatement ps = con.prepareStatement(slectMax);
            ResultSet rs=ps.executeQuery();
            while (rs.next()) {                
               returnIndex=rs.getString("maxID");
                System.out.println("Max ID "+returnIndex);
            }
            rs.close();
            ps.close();
            con.close();
            return returnIndex;
        } catch (Exception e) {
            System.out.println("Error from 49 "+e);
            return returnIndex;
        }
    }
    
    private String genaratedID(String empID){
        System.out.println("Generation "+empID);
        int currentValue=Integer.parseInt(empID.substring(1));
        int newValue=currentValue+1;
        String newGenaratedID=String.format("%03d", newValue);
        if (empID.charAt(0)=='A') {
            System.out.println("new one    "+"A"+newGenaratedID);
            return "A"+newGenaratedID;
        }else{
            System.out.println("new one    "+"C"+newGenaratedID);
            return "C"+newGenaratedID;
        }
        
      
    }
    
    
    
    
    
    
    
    public static void main(String[] args) {
        CustomerData c1=new CustomerData();
        c1.loadCustomerData("all", "",null);
        c1.getAllData();
    }
    
    
    
    
    
    
    
    
    
    
    
}
