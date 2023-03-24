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
public class EmployeeData extends People{
    
    
    private String licenceNo;
    private String useName;
    private String Password;

    public EmployeeData() {
        
        setUseName(null);
        setPassword(null);
        setLicenceNo(null);
        setNIC(null);
    }
    
    
    
    
    
    

   public String getLicenceNo() {
        return licenceNo;
    }

    public void setLicenceNo(String licenceNo) {
        this.licenceNo = licenceNo;
    }

    

    
    public String getUseName() {
        return useName;
    }

    public void setUseName(String useName) {
        if (useName!=null) {
            this.useName = useName+"123";
        }else{
            this.useName=useName;
        }
        
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        if (Password!=null) {
            this.Password=randomNumberGenarator(getFirstName());
            
        }else{
            this.Password=Password;
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
    public void loadEmployeeData(String loadType,String employeeSearchID ,JTable employeeTable,String loadRoleType){
        DefaultTableModel employeeTableLoad=null;
        if (employeeTable!=null) {
            employeeTableLoad=(DefaultTableModel)employeeTable.getModel();
            employeeTableLoad.getDataVector().removeAllElements();
            employeeTableLoad.fireTableDataChanged();
        }
        
        
        try {
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/carrentalsystem", "root", "akila123");
            String s="";
            
            if (loadType.equalsIgnoreCase("all")) {
                if (loadRoleType!=null) {
                    s ="select * from employee where role=\""+loadRoleType+"\"";
                }else{
                    s ="select * from employee";
                }
               
            }else{
               s="select * from employee where empID= \""+employeeSearchID+"\""; 
            }
            
            PreparedStatement ps = con.prepareStatement(s);
            ResultSet rs=ps.executeQuery();
            while (rs.next()) {                
                String empID=rs.getString("empID");
                System.out.println("Substring check "+empID.substring(0,4));
                String sd="select phoneNo from employeephone where empID=\""+empID+"\"";
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
                String licenceNo=rs.getString("LicenceNo");
               
                String[] ownerData={empID,role,Fname,Lname,email,address1,address2,city,NICno,licenceNo,numberArray[0],numberArray[1]};
                SetAllData(ownerData);
                
                String[] ownerData2={empID,Fname,Lname,role,email,NICno,city,numberArray[0],numberArray[1]};
                
                if (employeeTable!=null) {
                    employeeTableLoad.addRow(ownerData2);
                }

            }
            rs.close();
            ps.close();
            con.close();
 
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public boolean[] InsertEmployeeData(String roleType){
        boolean resultArray[]={false,false};
        try {
            if (getRole().equalsIgnoreCase("admin")|| getRole().equalsIgnoreCase("cashier")) {
                setPassword(getFirstName());
                setUseName(getFirstName());
                System.out.println("password insider");
            }
            setID(genaratedID(getLastIndexEmployeeTable()));
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/carrentalsystem", "root", "akila123");
            Statement st=con.createStatement();
           
           String query="insert into employee values(\""+getID()+"\",\""+getRole()+"\",\""+getFirstName()+"\",\""+getLastName()+"\",\""+getEmail()+"\",\""+getAddress1()+"\",\""+getAddress2()+"\",\""+getCity()+"\","
                        + "\""+getNIC()+"\",\""+getLicenceNo()+"\",\""+getUseName()+"\",\""+getPassword()+"\")";

            System.out.println("Quary "+query);
            st.executeUpdate(query);

            System.out.println("initial Query Succedd");
            String phoneNoQuery1="insert into employeephone values(\""+getID()+"\","+getPhoneNo1()+")";
            st.executeUpdate(phoneNoQuery1);
            String phoneNoQuery2="insert into employeephone values(\""+getID()+"\","+getPhoneNo2()+")";
            st.executeUpdate(phoneNoQuery2);
            
            resultArray[0]=true;
            
            if ((getRole().equalsIgnoreCase("Admin")||getRole().equalsIgnoreCase("Cashier")) && resultArray[0]==true) {
                MailGenerator mail1=new MailGenerator();
                boolean result2=mail1.sendMailsAuthentications(getEmail(), getUseName(), getPassword());
                resultArray[1]=result2;
            }else{
                resultArray[1]=false;
            }
            
            
            return resultArray;
            
        } catch (Exception e) {
            System.out.println("Error from 218 "+e);
            return resultArray;
        }
    }
    
    
    
    public boolean updateEmployeeData(String customerID){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/carrentalsystem", "root", "akila123");
            Statement st=con.createStatement();
            
            
            
            String query="update employee set FirstName=\""+getFirstName()+"\",LastName=\""+getLastName()+"\",Email=\""+getEmail()+"\",Address1=\""+getAddress1()+"\",Address2=\""+getAddress2()+"\","
                            + "city=\""+getCity()+"\" where empID=\""+customerID+"\"";

            System.out.println("Quary "+query);
            st.executeUpdate(query);

            String updatePhoneNo="update employeephone set phoneNo="+getPhoneNo1()+" where empID=\""+customerID+"\"";
            st.executeUpdate(updatePhoneNo);
            
            return true;
            
        }catch (Exception e) {
            
            System.out.println("Error from 218 "+e);
            return false;
        }
    }
    
    public void searchEmployee(String searchData,JTable resultTable){
         
 
       System.out.println("searched Name "+searchData);
        
        String[] result={null,null,null,null,"",""};
        
        try {
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/carrentalsystem", "root", "akila123");
            String[] searchfeilds={"EmpID","firstName","LastName","Email","Address1","City","NIC","PhoneNo"};
            for (int i = 0; i < searchfeilds.length; i++) {
                if (i==7) {
                    ResultSet phoneResult1=checkSearCustomer(con,"employeephone", "phoneNo", searchData);
                    if (phoneResult1!=null) {
                           loadEmployeeData("id", phoneResult1.getString("empID"),resultTable,null);

                    break;  
                                    }
                }else{
                    ResultSet result1=checkSearCustomer(con,"employee", searchfeilds[i], searchData);
                
                    if (result1!=null) {
                      
                        loadEmployeeData("id", result1.getString("empID"),resultTable,null);
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

    
    
    
    
    public void SetAllData(String[] employeeData){
        
        
        
        
        setID(employeeData[0]);
        setRole(employeeData[1]);
        setNIC(employeeData[8]);
        setFirstName(employeeData[2]);
        setLastName(employeeData[3]);
        setEmail(employeeData[4]);
        setAddress1(employeeData[5]);
        setAddress2(employeeData[6]);
        setCity(employeeData[7]);
        setPhoneNo1(employeeData[10]);
        setPhoneNo2(employeeData[11]);
        setLicenceNo(employeeData[9]);
        
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
    
    
     private String randomNumberGenarator(String initailName){
       int value=(int)((Math.random())*(5000-2000+1)+2000);
       String randomPassword=initailName.substring(0, 2)+"Rent@"+String.valueOf(value);
       return randomPassword;
    }
    
    
    
    private String getLastIndexEmployeeTable(){
        String returnIndex="A000";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/carrentalsystem", "root", "akila123");
            String slectMax=slectMax="Select max(EmpID) as maxID from employee";
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
    
    
    
    
}
