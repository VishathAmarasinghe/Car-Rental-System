/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package car.rental.system;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

/**
 *
 * @author Akila
 */
public class CustomerRegistrationForm extends javax.swing.JFrame {
    
    Connection con=null;
    Statement st=null;
    ResultSet rs=null;
    PreparedStatement ps=null;
    
    String employeeLastIndex="";
    String EmployeeUpadingIndex="";
    String customerLastIndex="";
    private CustomerData customer2=null;
    private EmployeeData employee2=null;
    private People people1=null;


    
    public CustomerRegistrationForm(String tableType) {
        initComponents();
        removeTextfromError();
        customer2=new CustomerData();
        employee2=new EmployeeData();
        

        
        
    }
    
    

    //constructor with result set of required customer details
    public CustomerRegistrationForm(ResultSet details,ResultSet phoneDetails,String tableType){
        initComponents();
        removeTextfromError();
        getLastIndexEmployeeTable(tableType);
        SetUpdateComponents(details, phoneDetails,tableType);
        if (tableType.equalsIgnoreCase("customer")) {
            customer2=new CustomerData();
        }else{
            employee2=new EmployeeData();
        }
  
    }
    
    public CustomerRegistrationForm(String clickedIDno,String tableType,String employeeType){
        initComponents();
        removeTextfromError();
//        getLastIndexEmployeeTable(tableType);
        
         if (tableType.equalsIgnoreCase("customer")) {
           customer2=new CustomerData();
        }else{
           employee2=new EmployeeData();
        }
        if (clickedIDno!=null) {
            SetUpdateComponents(clickedIDno,tableType);
        }
        
        System.out.println("checking loading");
  
    }
    
    
    
    //re loading database data to corresponding customer form
    private void SetUpdateComponents(ResultSet detaiSet,ResultSet phoneDetails,String tableType){
        try{
            
            while (detaiSet.next()) {                
                String role=detaiSet.getString("role");
                if (tableType.equalsIgnoreCase("customer")) {
                    EmployeeUpadingIndex=detaiSet.getString("customerID");
                }else{
                    EmployeeUpadingIndex=detaiSet.getString("EmpID");
                }
                
                
                
                System.out.println("EMPID UPdating "+EmployeeUpadingIndex );
                
                RegFirstName.setText(detaiSet.getString("FirstName"));
                RegLastName.setText(detaiSet.getString("lastName"));
                if (role.equalsIgnoreCase("Driver")) {
                    NIC_licence.setText("Licence");
                    RegNIC.setText(detaiSet.getString("LicenceNo"));
                    RegTitle.setText("Update Driver");
                    addbtnRegister.setText("Update Driver");
                    
                }else if (role.equalsIgnoreCase("Admin")) {
                    RegNIC.setText(detaiSet.getString("NIC"));
                    RegTitle.setText("Update Admin");
                    addbtnRegister.setText("Update Admin");
                    
                }if (role.equalsIgnoreCase("Cashier")) {
                    RegNIC.setText(detaiSet.getString("NIC"));
                    RegTitle.setText("Update Cashier");
                    addbtnRegister.setText("Update Cashier");
                    
                }if (role.equalsIgnoreCase("VOwner")) {
                    RegNIC.setText(detaiSet.getString("NIC"));
                    RegTitle.setText("Update car Owner");
                    addbtnRegister.setText("Update Owner");
                    
                }if (role.equalsIgnoreCase("customer")) {
                    RegNIC.setText(detaiSet.getString("NIC"));
                    RegTitle.setText("Update Customer");
                    addbtnRegister.setText("Update Customer");
                }
                
               
                
                RegEmail.setText(detaiSet.getString("Email"));
                regCity.setText(detaiSet.getString("city"));
                RegAddress1.setText(detaiSet.getString("Address1"));
                RegAddress2.setText(detaiSet.getString("Address2"));
                
            }
            String phoneNUmberArray[]=new String[2];
            int numcount=0;
            while (phoneDetails.next()) {                
                phoneNUmberArray[numcount]=phoneDetails.getString("phoneNo");
                numcount++;
            }
            RegPhoneNo1.setText(phoneNUmberArray[0]);
            regPhoneNo2.setText(phoneNUmberArray[1]);
            
        }catch(Exception e){
            System.out.println("Update Loading Error");
        }
    }
    
    
    
    //set components to update
    private void SetUpdateComponents(String clickedIndex,String tableType){

       String role=null;
        if (tableType.equalsIgnoreCase("customer")) {
            customer2.searchCustomer(clickedIndex,null);
            RegFirstName.setText(customer2.getFirstName());
            RegLastName.setText(customer2.getLastName());
            role=customer2.getRole();
            EmployeeUpadingIndex=customer2.getID();
            
            
            RegNIC.setText(customer2.getNIC());
            RegTitle.setText("Update Customer");
            addbtnRegister.setText("Update Customer");
            
            
            RegEmail.setText(customer2.getEmail());
            regCity.setText(customer2.getCity());
            RegAddress1.setText(customer2.getAddress1());
            RegAddress2.setText(customer2.getAddress2());
            RegPhoneNo1.setText(customer2.getPhoneNo1());
            regPhoneNo2.setText(customer2.getPhoneNo2());

        }else{
            employee2.searchEmployee(clickedIndex,null);
            RegFirstName.setText(employee2.getFirstName());
            RegLastName.setText(employee2.getLastName());
            role=employee2.getRole();
            EmployeeUpadingIndex=employee2.getID();
            
            
            
            
            if (role.equalsIgnoreCase("Driver")) {
                    NIC_licence.setText("Licence");
//                    RegNIC.setText(employee2.getLicenceNo());
                    RegTitle.setText("Update Driver");
                    addbtnRegister.setText("Update Driver");
                    
            }else if (role.equalsIgnoreCase("Admin")) {
                    RegNIC.setText(employee2.getNIC());
                    RegTitle.setText("Update Admin");
                    addbtnRegister.setText("Update Admin");
                    
            }if (role.equalsIgnoreCase("Cashier")) {
                    RegNIC.setText(employee2.getNIC());
                    RegTitle.setText("Update Cashier");
                    addbtnRegister.setText("Update Cashier");
                    
            }if (role.equalsIgnoreCase("VOwner")) {
                    RegNIC.setText(employee2.getNIC());
                    RegTitle.setText("Update car Owner");
                    addbtnRegister.setText("Update Owner");
                    
            }
            RegEmail.setText(employee2.getEmail());
            regCity.setText(employee2.getCity());
            RegAddress1.setText(employee2.getAddress1());
            RegAddress2.setText(employee2.getAddress2());
            RegPhoneNo1.setText(employee2.getPhoneNo1());
            regPhoneNo2.setText(employee2.getPhoneNo2());
        }
         
        System.out.println("EMPID UPdating "+EmployeeUpadingIndex );
  
    }
    

    
    
    
    
    public void SetRegistrationFrameTitle(String RegisterTitle,String AddbtnTitle){
        RegTitle.setText(RegisterTitle);
        addbtnRegister.setText(AddbtnTitle);
        if (RegisterTitle.equalsIgnoreCase("Driver Registration")) {
            NIC_licence.setText("Licence");
        }
    }
    
    private void getLastIndexEmployeeTable(String peopleType){
        try {
            con=DatabaseConnection.StablishDatabaseConnection();
            String slectMax="";
            if (peopleType.equalsIgnoreCase("customer")) {
                slectMax="Select max(CustomerID) as maxID from customer";
            }else{
                slectMax="Select max(EmpID) as maxID from employee";
            }
            
            ps = con.prepareStatement(slectMax);
            rs=ps.executeQuery();
            while (rs.next()) {                
                employeeLastIndex=rs.getString("maxID");
                System.out.println("Max ID "+employeeLastIndex);
            }
            
        } catch (Exception e) {
            System.out.println("Error from 49 "+e);
        }finally{
            DatabaseConnection.removeConnection(rs, st, ps, con);
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
    
    
    
    private String randomNumberGenarator(String initailName){
       int value=(int)((Math.random())*(5000-2000+1)+2000);
       String randomPassword=initailName.substring(0, 2)+"Rent@"+String.valueOf(value);
       return randomPassword;
    }
    
    private void addData(String dataType,String peopleType){
        
        
        removeTextfromError();
        Validations v1=new Validations();
        String fnameError=v1.nameChecker(RegFirstName.getText());
        String lnameError=v1.nameChecker(RegLastName.getText());
        String emilError=v1.emailChecker(RegEmail.getText());
        String addres1Error=v1.addressChecker(RegAddress1.getText());
        String address2Error=v1.addressChecker(RegAddress2.getText());
        String cityError=v1.addressChecker(regCity.getText());
        String phoneNoError1=v1.phoneNumberValidate(RegPhoneNo1.getText());
        String phoneNoError2=v1.phoneNumberValidate(regPhoneNo2.getText());
        String nicError=v1.NICvalidate(RegNIC.getText());
        
        
        if (RegPhoneNo1.getText().equalsIgnoreCase(regPhoneNo2.getText())) {
            phoneNoError1="Same number";
        }
        
        
        if (fnameError.equalsIgnoreCase("") && lnameError.equalsIgnoreCase("") &&
                emilError.equalsIgnoreCase("") && addres1Error.equalsIgnoreCase("") && address2Error.equalsIgnoreCase("") &&
                cityError.equalsIgnoreCase("") && phoneNoError1.equalsIgnoreCase("") && phoneNoError2.equalsIgnoreCase("") &&
                nicError.equalsIgnoreCase("") && !RegPhoneNo1.getText().equalsIgnoreCase(regPhoneNo2.getText())) {
            
            
            
                    if (dataType.equalsIgnoreCase("customer")) {
                        customer2.SetAllData(new String[]{"","Customer",
                            RegFirstName.getText(),RegLastName.getText(),RegEmail.getText(),RegAddress1.getText(),RegAddress2.getText(),regCity.getText(),RegNIC.getText(),RegPhoneNo1.getText(),
                            regPhoneNo2.getText()
                        });
            
                    if ( customer2.InsertCustomerData()) {
                            JOptionPane.showMessageDialog(null, "Data Added Successfully","Data Manipulation",JOptionPane.INFORMATION_MESSAGE);
                
                        }
                        CarRentalSystem.closeWindows(this);
            
           
        }else{
            if (peopleType.equalsIgnoreCase("Driver")) {
                employee2.SetAllData(new String[]{"","Driver",
                RegFirstName.getText(),RegLastName.getText(),RegEmail.getText(),RegAddress1.getText(),RegAddress2.getText(),regCity.getText(),RegNIC.getText(),RegPhoneNo1.getText(),
                regPhoneNo2.getText()});
                
                boolean actionResult[]=employee2.InsertEmployeeData(peopleType);
                if (actionResult[0]) {
                JOptionPane.showMessageDialog(null, "Data Added Successfully","Data Manipulation",JOptionPane.INFORMATION_MESSAGE);
                    if (actionResult[1]) {
                        JOptionPane.showMessageDialog(null, "Email Send To the New Employee","Registration",JOptionPane.INFORMATION_MESSAGE);
                    }
                    CarRentalSystem.closeWindows(this);
            }
                
            }else{
               employee2.SetAllData(new String[]{"",peopleType,
                RegFirstName.getText(),RegLastName.getText(),RegEmail.getText(),RegAddress1.getText(),RegAddress2.getText(),regCity.getText(),RegNIC.getText(),RegPhoneNo1.getText(),
                regPhoneNo2.getText()});
                
                boolean actionResult[]=employee2.InsertEmployeeData(peopleType);
                if (actionResult[0]) {
                JOptionPane.showMessageDialog(null, "Data Added Successfully","Data Manipulation",JOptionPane.INFORMATION_MESSAGE);
                    if (actionResult[1]) {
                        JOptionPane.showMessageDialog(null, "Email Send To the New Employee","Registration",JOptionPane.INFORMATION_MESSAGE);
                    }
            }
            CarRentalSystem.closeWindows(this);
        }
 
    }
  
        }else{
            
            fNameValidator.setText(fnameError);
            lnameValidator.setText(lnameError);
            mailValidator.setText(emilError);
            addressValidator.setText(addres1Error);
            address2Validator.setText(address2Error);
            cityValidator.setText(cityError);
            phoneNo1Validator.setText(phoneNoError1);
            phoneNo2Validator.setText(phoneNoError2);
            NICvalidator.setText(nicError);
            
        }
 
        
    }
    
    
    
    private void UpdateData(String dataType,String peopleType){
        
        
        
        removeTextfromError();
        Validations v1=new Validations();
        String fnameError=v1.nameChecker(RegFirstName.getText());
        String lnameError=v1.nameChecker(RegLastName.getText());
        String emilError=v1.emailChecker(RegEmail.getText());
        String addres1Error=v1.addressChecker(RegAddress1.getText());
        String address2Error=v1.addressChecker(RegAddress2.getText());
        String cityError=v1.addressChecker(regCity.getText());
        String phoneNoError1=v1.phoneNumberValidate(RegPhoneNo1.getText());
        String phoneNoError2=v1.phoneNumberValidate(regPhoneNo2.getText());
        String nicError=v1.NICvalidate(RegNIC.getText());
        
        
        if (RegPhoneNo1.getText().equalsIgnoreCase(regPhoneNo2.getText())) {
            phoneNoError1="Same Number";
            phoneNoError2="Same Number";
        }
        
        
        if (fnameError.equalsIgnoreCase("") && lnameError.equalsIgnoreCase("") &&
                emilError.equalsIgnoreCase("") && addres1Error.equalsIgnoreCase("") && address2Error.equalsIgnoreCase("") &&
                cityError.equalsIgnoreCase("") && phoneNoError1.equalsIgnoreCase("") && phoneNoError2.equalsIgnoreCase("") &&
                nicError.equalsIgnoreCase("") && !RegPhoneNo1.getText().equalsIgnoreCase(regPhoneNo2.getText())) {
        
        
        
        
        
        
        if (dataType.equalsIgnoreCase("customer")) {
            customer2.SetAllData(new String[]{"","Customer",
                RegFirstName.getText(),RegLastName.getText(),RegEmail.getText(),RegAddress1.getText(),RegAddress2.getText(),regCity.getText(),RegNIC.getText(),RegPhoneNo1.getText(),
                regPhoneNo2.getText()
            });
            
            if ( customer2.updateCustomer(EmployeeUpadingIndex)) {
                JOptionPane.showMessageDialog(null, "Data Updated Successfully","Data Manipulation",JOptionPane.INFORMATION_MESSAGE);
                
            }
            CarRentalSystem.closeWindows(this);
            
           
        }else{
            if (peopleType.equalsIgnoreCase("Driver")) {
                employee2.SetAllData(new String[]{"","Driver",
                RegFirstName.getText(),RegLastName.getText(),RegEmail.getText(),RegAddress1.getText(),RegAddress2.getText(),regCity.getText(),null,RegNIC.getText(),RegPhoneNo1.getText(),
                regPhoneNo2.getText()});
                
                if ( employee2.updateEmployeeData(EmployeeUpadingIndex)) {
                JOptionPane.showMessageDialog(null, "Data Updated Successfully","Data Manipulation",JOptionPane.INFORMATION_MESSAGE);
                
            }
                
            }else{
               employee2.SetAllData(new String[]{"",peopleType,
                RegFirstName.getText(),RegLastName.getText(),RegEmail.getText(),RegAddress1.getText(),RegAddress2.getText(),regCity.getText(),RegNIC.getText(),RegPhoneNo1.getText(),
                regPhoneNo2.getText()});
                
                if ( employee2.updateEmployeeData(EmployeeUpadingIndex)) {
                JOptionPane.showMessageDialog(null, "Data Updated Successfully","Data Manipulation",JOptionPane.INFORMATION_MESSAGE);
            }
            }
            CarRentalSystem.closeWindows(this);
        }
        
        }else{
            fNameValidator.setText(fnameError);
            lnameValidator.setText(lnameError);
            mailValidator.setText(emilError);
            addressValidator.setText(addres1Error);
            address2Validator.setText(address2Error);
            cityValidator.setText(cityError);
            phoneNo1Validator.setText(phoneNoError1);
            phoneNo2Validator.setText(phoneNoError2);
            NICvalidator.setText(nicError);
        }
 
    }
    
    
    
    
    
    private void addDataToTheTables(String dataType,String[] typeFormat){
        try {
            
  
            con=DatabaseConnection.StablishDatabaseConnection();
            st=con.createStatement();
            String firstName=RegFirstName.getText();
            String LastName=RegLastName.getText();
            String email=RegEmail.getText();
            String address1=RegAddress1.getText();
            String address2=RegAddress2.getText();
            String city=regCity.getText();
            int phoneno1=Integer.parseInt(RegPhoneNo1.getText());
            int phoneno2=Integer.parseInt(regPhoneNo2.getText());
            String NIC_or_Licence=RegNIC.getText();
            String GeneratedIDEmp="";
            if (typeFormat[0].equalsIgnoreCase("added")) {
                GeneratedIDEmp=genaratedID(employeeLastIndex);
            }
            
            
            String query="";
            
          
            
            if (dataType.equalsIgnoreCase("Owner")) {
                System.out.println("INside Else");
                if (typeFormat[0].equalsIgnoreCase("Added")) {
                    query="insert into employee values(\""+GeneratedIDEmp+"\",\"VOwner\",\""+firstName+"\",\""+LastName+"\",\""+email+"\",\""+address1+"\",\""+address2+"\",\""+city+"\",\""+NIC_or_Licence+"\",null,null,null)";
                }else{
                    
                    query="update employee set firstName=\""+firstName+"\",lastName=\""+LastName+"\",Email=\""+email+"\",Address1=\""+address1+"\",Address2=\""+address2+"\","
                            + "city=\""+city+"\",NIC=\""+NIC_or_Licence+"\" where empID=\""+typeFormat[1]+"\"";
//
                }
                
            
            }else if(dataType.equalsIgnoreCase("Driver")){
                if (typeFormat[0].equalsIgnoreCase("added")) {
                    query="insert into employee values(\""+GeneratedIDEmp+"\",\"Driver\",\""+firstName+"\",\""+LastName+"\",\""+email+"\",\""+address1+"\",\""+address2+"\",\""+city+"\",null,\""+NIC_or_Licence+"\",null,null)";
            
                }else{
                    query="update employee set firstName=\""+firstName+"\",lastName=\""+LastName+"\",Email=\""+email+"\",Address1=\""+address1+"\",Address2=\""+address2+"\","
                            + "city=\""+city+"\",LicenceNo=\""+NIC_or_Licence+"\" where empID=\""+typeFormat[1]+"\"";
                }
                
            }else if(dataType.equalsIgnoreCase("customer")){
                if (typeFormat[0].equalsIgnoreCase("added")) {
                    query="insert into customer values(\""+GeneratedIDEmp+"\",\"Customer\",\""+firstName+"\",\""+LastName+"\",\""+email+"\",\""+address1+"\",\""+address2+"\",\""+city+"\",\""+NIC_or_Licence+"\")";
            
                }else{
                    System.out.println("update Custimer icon");
                    query="update customer set FirstName=\""+firstName+"\",LastName=\""+LastName+"\",Email=\""+email+"\",Address1=\""+address1+"\",Address2=\""+address2+"\","
                            + "city=\""+city+"\" where CustomerID=\""+typeFormat[1]+"\"";
                }
                
            }
            else{
                
                if (typeFormat[0].equalsIgnoreCase("added")) {
                    query="insert into employee values(\""+GeneratedIDEmp+"\",\""+dataType+"\",\""+firstName+"\",\""+LastName+"\",\""+email+"\",\""+address1+"\",\""+address2+"\",\""+city+"\","
                        + "\""+NIC_or_Licence+"\",null,\""+firstName+"123\",\""+randomNumberGenarator(firstName)+"\")";
            
                }else{
                    query="update employee set firstName=\""+firstName+"\",lastName=\""+LastName+"\",Email=\""+email+"\",Address1=\""+address1+"\",Address2=\""+address2+"\","
                            + "city=\""+city+"\",NIC=\""+NIC_or_Licence+"\" where empID=\""+typeFormat[1]+"\"";
                }
                
            }
            
            System.out.println("Quary "+query);
            st.executeUpdate(query);
            if (dataType.equalsIgnoreCase("customer")) {
                if (typeFormat[0].equalsIgnoreCase("added")) {
                    String phoneNoQuery1="insert into customerphone values(\""+GeneratedIDEmp+"\","+phoneno1+")";
                    st.executeUpdate(phoneNoQuery1);
                    String phoneNoQuery2="insert into customerphone values(\""+GeneratedIDEmp+"\","+phoneno2+")";
                    st.executeUpdate(phoneNoQuery2);
                }else{
                    String updatePhoneNo="update customerphone set phoneNo="+phoneno1+" where customerID=\""+typeFormat[1]+"\"";
                    st.executeUpdate(updatePhoneNo);
            }
            }else{
                if (typeFormat[0].equalsIgnoreCase("added")) {
                    String phoneNoQuery1="insert into employeephone values(\""+GeneratedIDEmp+"\","+phoneno1+")";
                    st.executeUpdate(phoneNoQuery1);
                    String phoneNoQuery2="insert into employeephone values(\""+GeneratedIDEmp+"\","+phoneno2+")";
                    st.executeUpdate(phoneNoQuery2);
                }else{
                    String updatePhoneNo="update employeephone set phoneNo="+phoneno1+" where empID=\""+typeFormat[1]+"\"";
                    st.executeUpdate(updatePhoneNo);
            }
            }
            
            
            
            
                            
            
            JOptionPane.showMessageDialog(null, "Data \""+typeFormat[0]+"\" Successfully","Data Manipulation",JOptionPane.INFORMATION_MESSAGE);
          
            CarRentalSystem.closeWindows(this);
                 
            
        } catch (Exception e) {
            System.out.println("Error from 218 "+e);
        }finally{
            DatabaseConnection.removeConnection(rs, st, ps, con);
        }
    }
    
    private void removeTextfromError(){
        fNameValidator.setText("");
        lnameValidator.setText("");
        mailValidator.setText("");
        addressValidator.setText("");
        address2Validator.setText("");
        cityValidator.setText("");
        phoneNo1Validator.setText("");
        phoneNo2Validator.setText("");
        NICvalidator.setText("");
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        RegLastName = new javax.swing.JTextField();
        RegTitle = new javax.swing.JLabel();
        RegEmail = new javax.swing.JTextField();
        RegFirstName = new javax.swing.JTextField();
        RegAddress2 = new javax.swing.JTextField();
        RegAddress1 = new javax.swing.JTextField();
        RegPhoneNo1 = new javax.swing.JTextField();
        regCity = new javax.swing.JTextField();
        regPhoneNo2 = new javax.swing.JTextField();
        RegNIC = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        NIC_licence = new javax.swing.JLabel();
        addbtnRegister = new javax.swing.JButton();
        CustomerAddingCancelation = new javax.swing.JButton();
        NICvalidator = new javax.swing.JLabel();
        phoneNo2Validator = new javax.swing.JLabel();
        phoneNo1Validator = new javax.swing.JLabel();
        cityValidator = new javax.swing.JLabel();
        address2Validator = new javax.swing.JLabel();
        addressValidator = new javax.swing.JLabel();
        mailValidator = new javax.swing.JLabel();
        fNameValidator = new javax.swing.JLabel();
        lnameValidator = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(28, 78, 128));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 72, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 670, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 670));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        RegLastName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RegLastNameActionPerformed(evt);
            }
        });

        RegTitle.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        RegTitle.setForeground(new java.awt.Color(0, 0, 0));
        RegTitle.setText("Customer Registration");

        RegEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RegEmailActionPerformed(evt);
            }
        });

        RegFirstName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RegFirstNameActionPerformed(evt);
            }
        });

        RegAddress2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RegAddress2ActionPerformed(evt);
            }
        });

        RegAddress1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RegAddress1ActionPerformed(evt);
            }
        });

        regPhoneNo2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                regPhoneNo2ActionPerformed(evt);
            }
        });

        RegNIC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RegNICActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Last Name");

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("First Name");

        jLabel4.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Email");

        jLabel5.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Address1");

        jLabel6.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Address2");

        jLabel7.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("City");

        jLabel8.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("Emergency Phone No");

        jLabel9.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Phone No1");

        NIC_licence.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        NIC_licence.setForeground(new java.awt.Color(0, 0, 0));
        NIC_licence.setText("NIC No");

        addbtnRegister.setText("Add Customer");
        addbtnRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addbtnRegisterActionPerformed(evt);
            }
        });

        CustomerAddingCancelation.setText("Cancel");
        CustomerAddingCancelation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CustomerAddingCancelationActionPerformed(evt);
            }
        });

        NICvalidator.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        NICvalidator.setForeground(new java.awt.Color(255, 0, 0));
        NICvalidator.setText("abc");

        phoneNo2Validator.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        phoneNo2Validator.setForeground(new java.awt.Color(255, 0, 0));
        phoneNo2Validator.setText("abc");

        phoneNo1Validator.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        phoneNo1Validator.setForeground(new java.awt.Color(255, 0, 0));
        phoneNo1Validator.setText("abc");

        cityValidator.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        cityValidator.setForeground(new java.awt.Color(255, 0, 0));
        cityValidator.setText("abc");

        address2Validator.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        address2Validator.setForeground(new java.awt.Color(255, 0, 0));
        address2Validator.setText("abc");

        addressValidator.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        addressValidator.setForeground(new java.awt.Color(255, 0, 0));
        addressValidator.setText("abc");

        mailValidator.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        mailValidator.setForeground(new java.awt.Color(255, 0, 0));
        mailValidator.setText("abc");

        fNameValidator.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        fNameValidator.setForeground(new java.awt.Color(255, 0, 0));
        fNameValidator.setText("abc");

        lnameValidator.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        lnameValidator.setForeground(new java.awt.Color(255, 0, 0));
        lnameValidator.setText("abc");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(RegTitle)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(RegAddress2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(RegPhoneNo1, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9)
                                    .addComponent(phoneNo1Validator, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(regPhoneNo2, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8)
                                    .addComponent(phoneNo2Validator, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(RegAddress1)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(RegNIC, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(300, 300, 300))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(NICvalidator, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(NIC_licence, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(addressValidator, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(RegFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel3))
                                            .addGap(63, 63, 63)
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel2)
                                                .addComponent(RegLastName, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(lnameValidator, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addGap(0, 0, Short.MAX_VALUE)
                                            .addComponent(addbtnRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(CustomerAddingCancelation, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(RegEmail)
                                        .addComponent(jLabel4))
                                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(fNameValidator, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(mailValidator, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(address2Validator, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cityValidator, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(66, 66, 66))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(regCity, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(RegTitle)
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RegFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(RegLastName, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fNameValidator)
                    .addComponent(lnameValidator))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addGap(2, 2, 2)
                .addComponent(RegEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(mailValidator)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(RegAddress1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(addressValidator)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addGap(2, 2, 2)
                .addComponent(RegAddress2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(address2Validator)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addGap(2, 2, 2)
                .addComponent(regCity, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cityValidator)
                .addGap(4, 4, 4)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(regPhoneNo2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(RegPhoneNo1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(phoneNo2Validator)
                    .addComponent(phoneNo1Validator))
                .addGap(3, 3, 3)
                .addComponent(NIC_licence)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(RegNIC, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(NICvalidator)
                .addGap(12, 12, 12)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addbtnRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CustomerAddingCancelation, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(122, 122, 122))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 0, 660, 670));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void RegLastNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RegLastNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_RegLastNameActionPerformed

    private void RegAddress2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RegAddress2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_RegAddress2ActionPerformed

    private void RegAddress1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RegAddress1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_RegAddress1ActionPerformed

    private void RegFirstNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RegFirstNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_RegFirstNameActionPerformed

    private void regPhoneNo2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_regPhoneNo2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_regPhoneNo2ActionPerformed

    private void RegNICActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RegNICActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_RegNICActionPerformed

    private void addbtnRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addbtnRegisterActionPerformed
        // TODO add your handling code here:
        String[]  updateType={"Added",EmployeeUpadingIndex};
        System.out.println("Employee Updating index "+EmployeeUpadingIndex);
        if (addbtnRegister.getText().equalsIgnoreCase("Add Owner")) {
//            addDataToTheTables("Owner",updateType);
              addData("employee", "VOwner");
        }else if(addbtnRegister.getText().equalsIgnoreCase("Add Driver")){
//            addDataToTheTables("Driver",updateType);
              addData("employee", "Driver");
        }else if(addbtnRegister.getText().equalsIgnoreCase("Add Cashier")){
//            addDataToTheTables("Cashier",updateType);
              addData("employee", "Cashier");
        }else if(addbtnRegister.getText().equalsIgnoreCase("Add Admin")){
//            addDataToTheTables("Admin",updateType);
              addData("employee", "Admin");
        }else if(addbtnRegister.getText().equalsIgnoreCase("Add Customer")){
//            addDataToTheTables("customer",updateType);
              addData("customer", null);
        }
        
        
        System.out.println("update panel came");
        updateType[0]="Updated";
        if (addbtnRegister.getText().equalsIgnoreCase("Update Driver")) {
//            addDataToTheTables("Driver",updateType);
              UpdateData("employee", "Driver");
        }else if(addbtnRegister.getText().equalsIgnoreCase("Update Admin")){
//            addDataToTheTables("Admin",updateType);
              UpdateData("employee", "Admin");
        }else if(addbtnRegister.getText().equalsIgnoreCase("Update Cashier")){
//            addDataToTheTables("Cashier",updateType);
            UpdateData("employee", "Cashier");
        }else if(addbtnRegister.getText().equalsIgnoreCase("Update Owner")){
//            addDataToTheTables("Owner",updateType);
            UpdateData("employee", "VOwner");
        }else if(addbtnRegister.getText().equalsIgnoreCase("Update Customer")){
//            addDataToTheTables("customer",updateType);
              UpdateData("customer", "customer");
        }
        
//        changeverificationStatus();
          Bill.verificationStatus=true;
//          Bill abc=new Bill();
         
        
    }//GEN-LAST:event_addbtnRegisterActionPerformed

    private void CustomerAddingCancelationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CustomerAddingCancelationActionPerformed
        CarRentalSystem.closeWindows(this);
    }//GEN-LAST:event_CustomerAddingCancelationActionPerformed

    private void RegEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RegEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_RegEmailActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CustomerRegistrationForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CustomerRegistrationForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CustomerRegistrationForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CustomerRegistrationForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CustomerRegistrationForm("All").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CustomerAddingCancelation;
    private javax.swing.JLabel NIC_licence;
    private javax.swing.JLabel NICvalidator;
    private javax.swing.JTextField RegAddress1;
    private javax.swing.JTextField RegAddress2;
    private javax.swing.JTextField RegEmail;
    private javax.swing.JTextField RegFirstName;
    private javax.swing.JTextField RegLastName;
    private javax.swing.JTextField RegNIC;
    private javax.swing.JTextField RegPhoneNo1;
    private javax.swing.JLabel RegTitle;
    private javax.swing.JButton addbtnRegister;
    private javax.swing.JLabel address2Validator;
    private javax.swing.JLabel addressValidator;
    private javax.swing.JLabel cityValidator;
    private javax.swing.JLabel fNameValidator;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lnameValidator;
    private javax.swing.JLabel mailValidator;
    private javax.swing.JLabel phoneNo1Validator;
    private javax.swing.JLabel phoneNo2Validator;
    private javax.swing.JTextField regCity;
    private javax.swing.JTextField regPhoneNo2;
    // End of variables declaration//GEN-END:variables
}
