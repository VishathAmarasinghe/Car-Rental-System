/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package car.rental.system;

import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Akila
 */
public class Bill extends javax.swing.JFrame {

    /**
     * Creates new form Bill
     */
    
     public static boolean verificationStatus=false;
     private int carPrice =0;
    
     public Bill(){
        initComponents();
        moreDetailsBtn.setVisible(false);
        verificationStatusShower.setVisible(false);
        verifyCustomerBtn.setVisible(false);
        driverDetailsBill.setVisible(false);
        driverSelectorBill.setVisible(false);
        verificationStatusShowerVehical.setVisible(false);
        
     };
    
   
    public Bill(ResultSet reservationResult ) {
        initComponents();
        moreDetailsBtn.setVisible(false);
        verificationStatusShower.setVisible(false);
        verifyCustomerBtn.setVisible(false);
        driverDetailsBill.setVisible(false);
        driverSelectorBill.setVisible(false);
        loadreservationData(reservationResult);
        vehicalDriverDetailsGetter();
        verificationStatusShowerVehical.setVisible(false);
        paymentCalculation();
//        verificationbtn.addAncestorListener(listener);
        
    }
    
    private void loadreservationData(ResultSet reservationResult){
        try {
            reservationResult.next();
            pickUpDateBill.setText(reservationResult.getString("PickedUpDate"));
            dropOffDatebill.setText(reservationResult.getString("DropOffDate"));
            vehicalNoBill.setText(reservationResult.getString("VehicalNumber"));
            driverStatusBill.setText(reservationResult.getString("DriverStatus"));
            if (!driverStatusBill.getText().equalsIgnoreCase("no")) {
                driverDetailsBill.setVisible(true);
                driverSelectorBill.setVisible(true);
            }
            billCustomerSearchField.setText(reservationResult.getString("DummyName"));
            searchReservationcustomer();
        } catch (Exception ex) {
            System.out.println("Load Reservation Error"+ex);
        }
        
    }
    
    private void paymentCalculation(){
        
        double finalTotalPayment=0;
        
        int PickUpday=Integer.parseInt(pickUpDateBill.getText().substring(8));
        int PickUpmonth=Integer.parseInt(pickUpDateBill.getText().substring(5,7));
        
        int DropOffday=Integer.parseInt(dropOffDatebill.getText().substring(8));
        int DropOffmonth=Integer.parseInt(dropOffDatebill.getText().substring(5,7));
        
        int number_of_Days=0;
        
        if (DropOffday-PickUpday==0) {
            number_of_Days=1+(DropOffmonth-PickUpmonth);
        }else{
            number_of_Days=(DropOffday-PickUpday)+(DropOffmonth-PickUpmonth);
        }
        
        
        double total_car_Rent=number_of_Days*carPrice;
        
        
        perDayCarCharge.setText(String.valueOf(carPrice));
        numberOfDays.setText(String.valueOf(number_of_Days));
        totalCarRent.setText(String.valueOf(total_car_Rent));
        
        
        if (!driverStatusBill.getText().equalsIgnoreCase("no")) {
            double driverCharge=2500*number_of_Days;
            finalTotalPayment=driverCharge+total_car_Rent;
            
            driverChargePerDay.setText("2500");
            
        }else{
            finalTotalPayment=total_car_Rent;
            driverChargePerDay.setText("0");
        }
        
        TotalChargebill.setText(String.valueOf(finalTotalPayment));
        
        double advance_Payment=finalTotalPayment*0.4;
        
        advancePayment.setText(String.valueOf(advance_Payment));
        
        double pending_Payment=finalTotalPayment-advance_Payment;
        
        pendingPayments.setText(String.valueOf(pending_Payment));
        
        
        
        
        
        
    }
    
    
    private void checkResultAccuracy(){
        if (!CustomerIDshower.getText().substring(0,2).equalsIgnoreCase("No")) {
            moreDetailsBtn.setVisible(true);
            verifyCustomerBtn.setVisible(true);
        }else{
            moreDetailsBtn.setVisible(false);
            verifyCustomerBtn.setVisible(false);
        }
    }
    
    private void vehicalDriverDetailsGetter(){
        try {
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/carrentalsystem", "root", "akila123");
            String singleData="select * from cars where CarNumber=\""+vehicalNoBill.getText()+"\"";
            PreparedStatement ps = con.prepareStatement(singleData);
            ResultSet rs=ps.executeQuery();
            
            rs.next();
            vehicalNameBill.setText(rs.getString("CarModel")+" "+rs.getString("CarType"));
            carPrice=Integer.parseInt(rs.getString("Price"));
            
            String availableDrivers="select * from employee where role=\""+"Driver"+"\"";
            PreparedStatement psnumbers = con.prepareStatement(availableDrivers);
            ResultSet rs2=psnumbers.executeQuery();
            
            while (rs2.next()) {                
                String empID=rs2.getString("EmpID");
                String Fname=rs2.getString("FirstName");
                String Lname=rs2.getString("lastName");
                driverSelectorBill.addItem(empID+" "+Fname+" "+Lname);
            }
            
            CustomerRegistrationForm form1=new CustomerRegistrationForm(rs, rs2,"customer");
            form1.setVisible(true);
            
            con.close();

        } catch (Exception e) {
            System.out.println("Vehical Driver details Getter "+e);
        }
    }
    
    
    private void searchReservationcustomer(){
        SearchFunction searchCustom=new SearchFunction();
        CustomerIDshower.setForeground(Color.BLACK);
        CustomerNameShower.setForeground(Color.BLACK);
        mobileNoShower.setForeground(Color.BLACK);
        String[] searchResult=searchCustom.getSearchedCustomerName(billCustomerSearchField.getText());
        if (searchResult[0]!=null) {
            CustomerIDshower.setText(searchResult[0]);
            CustomerNameShower.setText(searchResult[1]+" "+searchResult[2]);
            mobileNoShower.setText(searchResult[3]+"  "+searchResult[4]+"  "+searchResult[5]);
        }else{
            CustomerIDshower.setForeground(Color.red);
            CustomerNameShower.setForeground(Color.red);
            mobileNoShower.setForeground(Color.red);
//            CustomerIDshower.setFont(new Font("Segoe UI", , 12));
           CustomerIDshower.setText("No Related Customer ID Found");
            CustomerNameShower.setText("No Related Customer Name Found");
            mobileNoShower.setText("No Related Customer phone No Found");
        }
        checkResultAccuracy();
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
        jPanel3 = new javax.swing.JPanel();
        billCustomerSearchField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        BillCustomerSearchBtn = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        CustomerIDshower = new javax.swing.JLabel();
        CustomerNameShower = new javax.swing.JLabel();
        mobileNoShower = new javax.swing.JLabel();
        moreDetailsBtn = new javax.swing.JButton();
        verifyCustomerBtn = new javax.swing.JButton();
        verificationStatusShower = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        driverDetailsBill = new javax.swing.JLabel();
        driverStatusBill = new javax.swing.JLabel();
        driverSelectorBill = new javax.swing.JComboBox<>();
        vehicalNoBill = new javax.swing.JLabel();
        vehicalNameBill = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        pickUpDateBill = new javax.swing.JLabel();
        dropOffDatebill = new javax.swing.JLabel();
        verifyDriverVehicalBtn2 = new javax.swing.JButton();
        verificationStatusShowerVehical1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        perDayCarCharge = new javax.swing.JLabel();
        numberOfDays = new javax.swing.JLabel();
        totalCarRent = new javax.swing.JLabel();
        driverChargePerDay = new javax.swing.JLabel();
        TotalChargebill = new javax.swing.JLabel();
        pendingPayments = new javax.swing.JLabel();
        verifyDriverVehicalBtn1 = new javax.swing.JButton();
        verificationStatusShowerVehical = new javax.swing.JLabel();
        advancePayment = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(28, 78, 128));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 413, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        billCustomerSearchField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        billCustomerSearchField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                billCustomerSearchFieldActionPerformed(evt);
            }
        });
        billCustomerSearchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                billCustomerSearchFieldKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                billCustomerSearchFieldKeyTyped(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Customer Details");

        BillCustomerSearchBtn.setText("Search");
        BillCustomerSearchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BillCustomerSearchBtnActionPerformed(evt);
            }
        });

        jButton2.setText("Add New Customer");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Customer No : ");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Name              :");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Mobile No      :");

        CustomerIDshower.setForeground(new java.awt.Color(0, 0, 0));
        CustomerIDshower.setText("asdasd");

        CustomerNameShower.setForeground(new java.awt.Color(0, 0, 0));
        CustomerNameShower.setText("asd");

        mobileNoShower.setForeground(new java.awt.Color(0, 0, 0));
        mobileNoShower.setText("sd");

        moreDetailsBtn.setText("More Customer Details");
        moreDetailsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moreDetailsBtnActionPerformed(evt);
            }
        });

        verifyCustomerBtn.setText("Verify Customer");
        verifyCustomerBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                verifyCustomerBtnActionPerformed(evt);
            }
        });

        verificationStatusShower.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        verificationStatusShower.setForeground(new java.awt.Color(0, 0, 0));
        verificationStatusShower.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/correct2.png"))); // NOI18N
        verificationStatusShower.setText("Customer Verified");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(34, 34, 34)
                                .addComponent(billCustomerSearchField, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(CustomerIDshower, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(mobileNoShower, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(CustomerNameShower, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                                .addComponent(verificationStatusShower)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(BillCustomerSearchBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(moreDetailsBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(verifyCustomerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(33, 33, 33))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(billCustomerSearchField, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BillCustomerSearchBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(CustomerIDshower, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(CustomerNameShower, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(21, 21, 21)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(mobileNoShower, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(moreDetailsBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(19, 19, 19)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(verifyCustomerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(verificationStatusShower, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Vehical & Driver Details");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Vehical No    :");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Vehical Name   :");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("Driver Status:  ");

        driverDetailsBill.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        driverDetailsBill.setForeground(new java.awt.Color(0, 0, 0));
        driverDetailsBill.setText("Driver Details:  ");

        driverStatusBill.setText("jLabel10");

        vehicalNoBill.setText("jLabel10");

        vehicalNameBill.setText("jLabel10");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setText("PickUp Date   :");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setText("DropOff Date :");

        pickUpDateBill.setText("jLabel10");

        dropOffDatebill.setText("jLabel10");

        verifyDriverVehicalBtn2.setText("Verify Vehical & Driver");
        verifyDriverVehicalBtn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                verifyDriverVehicalBtn2ActionPerformed(evt);
            }
        });

        verificationStatusShowerVehical1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        verificationStatusShowerVehical1.setForeground(new java.awt.Color(0, 0, 0));
        verificationStatusShowerVehical1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/correct2.png"))); // NOI18N
        verificationStatusShowerVehical1.setText("Customer Verified");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(vehicalNoBill))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(vehicalNameBill))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(pickUpDateBill)))
                        .addGap(120, 120, 120)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addGap(18, 18, 18)
                                        .addComponent(driverStatusBill))
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(driverDetailsBill)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(driverSelectorBill, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addGap(18, 18, 18)
                                .addComponent(dropOffDatebill)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(verifyDriverVehicalBtn2, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30))))
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                    .addContainerGap(600, Short.MAX_VALUE)
                    .addComponent(verificationStatusShowerVehical1)
                    .addGap(49, 49, 49)))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel6)
                                    .addComponent(vehicalNoBill))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7)
                                    .addComponent(vehicalNameBill)))
                            .addComponent(verifyDriverVehicalBtn2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(pickUpDateBill)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(driverStatusBill))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(driverDetailsBill)
                            .addComponent(driverSelectorBill, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(dropOffDatebill))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                    .addContainerGap(104, Short.MAX_VALUE)
                    .addComponent(verificationStatusShowerVehical1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Customer Details");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 0, 0));
        jLabel12.setText("Car Charge Per Day: ");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 0, 0));
        jLabel13.setText("No of Days:");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 0, 0));
        jLabel14.setText("Total Car Rent");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 0, 0));
        jLabel15.setText("Driver Charge Per Day: ");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 0, 0));
        jLabel16.setText("Total Charge");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 0, 0));
        jLabel17.setText("Advance Payment(40% of Total Charge): ");

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 0, 0));
        jLabel18.setText("Pending Payment");

        perDayCarCharge.setText("jLabel19");

        numberOfDays.setText("jLabel19");

        totalCarRent.setText("jLabel19");

        driverChargePerDay.setText("jLabel19");

        TotalChargebill.setText("jLabel19");

        pendingPayments.setText("jLabel19");

        verifyDriverVehicalBtn1.setText("Verify Paymant");
        verifyDriverVehicalBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                verifyDriverVehicalBtn1ActionPerformed(evt);
            }
        });

        verificationStatusShowerVehical.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        verificationStatusShowerVehical.setForeground(new java.awt.Color(0, 0, 0));
        verificationStatusShowerVehical.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/correct2.png"))); // NOI18N
        verificationStatusShowerVehical.setText("Payment Verified");

        advancePayment.setText("jTextField1");
        advancePayment.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                advancePaymentKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14)
                            .addComponent(jLabel15)
                            .addComponent(jLabel16))
                        .addGap(122, 122, 122)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(driverChargePerDay, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(totalCarRent, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(numberOfDays, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(perDayCarCharge, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TotalChargebill, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17)
                            .addComponent(jLabel18))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pendingPayments, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(advancePayment, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(verificationStatusShowerVehical)
                            .addComponent(verifyDriverVehicalBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(27, 27, 27))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(perDayCarCharge))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(numberOfDays))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(totalCarRent))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(driverChargePerDay))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(TotalChargebill))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(verifyDriverVehicalBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(advancePayment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(verificationStatusShowerVehical, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel18)
                        .addComponent(pendingPayments)))
                .addGap(32, 32, 32))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(64, 64, 64))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, 750));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void billCustomerSearchFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_billCustomerSearchFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_billCustomerSearchFieldActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        CustomerRegistrationForm c1=new CustomerRegistrationForm("customer");
        c1.setVisible(true);
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void BillCustomerSearchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BillCustomerSearchBtnActionPerformed
        // TODO add your handling code here:
        searchReservationcustomer();
        
    }//GEN-LAST:event_BillCustomerSearchBtnActionPerformed

    private void billCustomerSearchFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_billCustomerSearchFieldKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_billCustomerSearchFieldKeyTyped

    private void billCustomerSearchFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_billCustomerSearchFieldKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_billCustomerSearchFieldKeyPressed

    private void moreDetailsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moreDetailsBtnActionPerformed
        // TODO add your handling code here:
        try {
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/carrentalsystem", "root", "akila123");
            String singleData="select * from customer where customerID=\""+CustomerIDshower.getText()+"\"";
            PreparedStatement ps = con.prepareStatement(singleData);
            ResultSet rs=ps.executeQuery();
            
            String singleDataPhoneNumbers="select * from customerphone where customerID=\""+CustomerIDshower.getText()+"\"";
            PreparedStatement psnumbers = con.prepareStatement(singleDataPhoneNumbers);
            ResultSet rs2=psnumbers.executeQuery();
            
            CustomerRegistrationForm form1=new CustomerRegistrationForm(rs, rs2,"customer");
            form1.setVisible(true);
            
            con.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_moreDetailsBtnActionPerformed

    private void verifyCustomerBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_verifyCustomerBtnActionPerformed
        // TODO add your handling code here:
        int resultofCUstomerValidation= JOptionPane.showConfirmDialog(null,"Please Confirm that you double check the details with NIC","Deleting Confirmation",
                       JOptionPane.YES_NO_OPTION);
            if (resultofCUstomerValidation==0) {
                  verificationStatusShower.setVisible(true);
                  verifyCustomerBtn.setVisible(false);
                  System.out.println(verifyCustomerBtn.getAlignmentX()+"   "+verifyCustomerBtn.getAlignmentY());
                  billCustomerSearchField.setEditable(false);
                  BillCustomerSearchBtn.setEnabled(false);
            }
                
    }//GEN-LAST:event_verifyCustomerBtnActionPerformed

    private void verifyDriverVehicalBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_verifyDriverVehicalBtn1ActionPerformed
        // TODO add your handling code here:
        
        if (!driverStatusBill.getText().equalsIgnoreCase("no")) {
            
        }
        int resultofDriverVehicalValidation= JOptionPane.showConfirmDialog(null,"Please Confirm that you double check the details with NIC","Deleting Confirmation",
                       JOptionPane.YES_NO_OPTION);
            if (resultofDriverVehicalValidation==0) {
                
                  verifyDriverVehicalBtn1.setVisible(false);
                  verificationStatusShowerVehical.setVisible(true);
            }
    }//GEN-LAST:event_verifyDriverVehicalBtn1ActionPerformed

    private void verifyDriverVehicalBtn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_verifyDriverVehicalBtn2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_verifyDriverVehicalBtn2ActionPerformed

    private void advancePaymentKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_advancePaymentKeyTyped
        // TODO add your handling code here:
        
        double chagingValue=Double.valueOf(TotalChargebill.getText())-Double.valueOf(advancePayment.getText());
        pendingPayments.setText(String.valueOf(chagingValue));
    }//GEN-LAST:event_advancePaymentKeyTyped

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
            java.util.logging.Logger.getLogger(Bill.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Bill.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Bill.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Bill.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Bill().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BillCustomerSearchBtn;
    private javax.swing.JLabel CustomerIDshower;
    private javax.swing.JLabel CustomerNameShower;
    private javax.swing.JLabel TotalChargebill;
    private javax.swing.JTextField advancePayment;
    private javax.swing.JTextField billCustomerSearchField;
    private javax.swing.JLabel driverChargePerDay;
    private javax.swing.JLabel driverDetailsBill;
    private javax.swing.JComboBox<String> driverSelectorBill;
    private javax.swing.JLabel driverStatusBill;
    private javax.swing.JLabel dropOffDatebill;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
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
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JLabel mobileNoShower;
    private javax.swing.JButton moreDetailsBtn;
    private javax.swing.JLabel numberOfDays;
    private javax.swing.JLabel pendingPayments;
    private javax.swing.JLabel perDayCarCharge;
    private javax.swing.JLabel pickUpDateBill;
    private javax.swing.JLabel totalCarRent;
    private javax.swing.JLabel vehicalNameBill;
    private javax.swing.JLabel vehicalNoBill;
    private javax.swing.JLabel verificationStatusShower;
    private javax.swing.JLabel verificationStatusShowerVehical;
    private javax.swing.JLabel verificationStatusShowerVehical1;
    private javax.swing.JButton verifyCustomerBtn;
    private javax.swing.JButton verifyDriverVehicalBtn1;
    private javax.swing.JButton verifyDriverVehicalBtn2;
    // End of variables declaration//GEN-END:variables
}
