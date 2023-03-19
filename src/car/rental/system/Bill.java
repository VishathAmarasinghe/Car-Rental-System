/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package car.rental.system;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Akila
 */
public class Bill extends javax.swing.JFrame {

    /**
     * Creates new form Bill
     */
    public static boolean verificationStatus = false;
    private int carPrice = 0;
    double driverCharge = 0;
    private String reservationNo="";
    private boolean verification_customer=false;
    private boolean verification_DriverVehical=false;
     private boolean verification_finalPayment=false;
     private String previousBillID="";
     private String correspondingTable="";

    public Bill() {
        initComponents();
        moreDetailsBtn.setVisible(false);
        verificationStatusShower.setVisible(false);
        verifyCustomerBtn.setVisible(false);
        driverDetailsBill.setVisible(false);
        driverSelectorBill.setVisible(false);
        verificationStatusShowerVehicalbtn.setVisible(false);
        verificationStatusShowerVehical.setVisible(false);
        BillImageCreation();

    }

    ;
    
   
    public Bill(ResultSet reservationResult,String TableType) {
        initComponents();
        correspondingTable=TableType;
        moreDetailsBtn.setVisible(false);
        verificationStatusShower.setVisible(false);
        verifyCustomerBtn.setVisible(false);
        driverDetailsBill.setVisible(false);
        driverSelectorBill.setVisible(false);
        loadreservationData(reservationResult,TableType);
        vehicalDriverDetailsGetter();
        verificationStatusShowerVehicalbtn.setVisible(false);
        verificationStatusShowerVehical.setVisible(false);
        paymentCalculation(TableType);
//        verificationbtn.addAncestorListener(listener);

    }

    private void loadreservationData(ResultSet reservationResult,String TableType) {
        try {
            reservationResult.next();
            reservationNo=reservationResult.getString("ReservationID");
            pickUpDateBill.setText(reservationResult.getString("PickedUpDate"));
            dropOffDatebill.setText(reservationResult.getString("DropOffDate"));
            vehicalNoBill.setText(reservationResult.getString("VehicalNumber"));
            driverStatusBill.setText(reservationResult.getString("DriverStatus"));
            if (!driverStatusBill.getText().equalsIgnoreCase("no")) {
                driverDetailsBill.setVisible(true);
                driverSelectorBill.setVisible(true);
            }
            if (TableType.equalsIgnoreCase("pending")) {
                billCustomerSearchField.setText(reservationResult.getString("DummyName"));
            }else if (TableType.equalsIgnoreCase("proceeding")) {
                billCustomerSearchField.setText(reservationResult.getString("CustomerID"));
                previousBillID=reservationResult.getString("BillNo");
            }
            
            searchReservationcustomer();
        } catch (Exception ex) {
            System.out.println("Load Reservation Error" + ex);
        }

    }

    private void paymentCalculation(String TableType) {

        double finalTotalPayment = 0;

        int PickUpday = Integer.parseInt(pickUpDateBill.getText().substring(8));
        int PickUpmonth = Integer.parseInt(pickUpDateBill.getText().substring(5, 7));

        int DropOffday = Integer.parseInt(dropOffDatebill.getText().substring(8));
        int DropOffmonth = Integer.parseInt(dropOffDatebill.getText().substring(5, 7));

        int number_of_Days = 0;

        if (DropOffday - PickUpday == 0) {
            number_of_Days = 1 + (DropOffmonth - PickUpmonth);
        } else {
            number_of_Days = (DropOffday - PickUpday) + (DropOffmonth - PickUpmonth);
        }

        double total_car_Rent = number_of_Days * carPrice;

        perDayCarCharge.setText(String.valueOf(carPrice));
        numberOfDays.setText(String.valueOf(number_of_Days));
        totalCarRent.setText(String.valueOf(total_car_Rent));

        if (!driverStatusBill.getText().equalsIgnoreCase("no")) {
            driverCharge = 2500 * number_of_Days;
            finalTotalPayment = driverCharge + total_car_Rent;

            driverChargePerDay.setText("2500");

        } else {
            finalTotalPayment = total_car_Rent;
            driverChargePerDay.setText("0");
        }

        TotalChargebill.setText(String.valueOf(finalTotalPayment));

        double advance_Payment = finalTotalPayment * 0.4;

        advancePayment.setText(String.valueOf(advance_Payment));

        double pending_Payment = finalTotalPayment - advance_Payment;

        pendingPayments.setText(String.valueOf(pending_Payment));
        
        if (TableType.equalsIgnoreCase("proceeding")) {
            
            carchargeLabel.setVisible(false);
            perDayCarCharge.setVisible(false);
            nofoDaysLabel.setVisible(false);
            numberOfDays.setVisible(false);
            totalCarRentLabel.setVisible(false);
            totalCarRent.setVisible(false);
            prepaidLabel.setVisible(false);
            advancePayment.setVisible(false);
            
            try{
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/carrentalsystem", "root", "akila123");
                String singleData = "select * from Bill where BillNo=\"" + previousBillID + "\"";
                PreparedStatement ps = con.prepareStatement(singleData);
                ResultSet rs = ps.executeQuery();
                rs.next();
                double totalAmountProceeded=Double.parseDouble(rs.getString("TotalAmount"));
                double PaidAmountProceeded=Double.parseDouble(rs.getString("AdvancePaiedAmount"));
                
                driverChargeLabel.setText("Total Charge");
                driverChargePerDay.setText(String.valueOf(totalAmountProceeded));
                AdvancePaidCharge.setText("Paid value");
                TotalChargebill.setText(String.valueOf(PaidAmountProceeded));
                pendingPayments.setText(String.valueOf(totalAmountProceeded-PaidAmountProceeded));
                
            }catch(Exception e){
                System.out.println("Proceeded Table Handle error "+e);
            }
        }

    }

    private void checkResultAccuracy() {
        if (!CustomerIDshower.getText().substring(0, 2).equalsIgnoreCase("No")) {
            moreDetailsBtn.setVisible(true);
            verifyCustomerBtn.setVisible(true);
        } else {
            moreDetailsBtn.setVisible(false);
            verifyCustomerBtn.setVisible(false);
        }
    }

    private void vehicalDriverDetailsGetter() {
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/carrentalsystem", "root", "akila123");
            String singleData = "select * from cars where CarNumber=\"" + vehicalNoBill.getText() + "\"";
            PreparedStatement ps = con.prepareStatement(singleData);
            ResultSet rs = ps.executeQuery();

            rs.next();
            vehicalNameBill.setText(rs.getString("CarModel") + " " + rs.getString("CarType"));
            carPrice = Integer.parseInt(rs.getString("Price"));

            String availableDrivers = "select * from employee where role=\"" + "Driver" + "\"";
            PreparedStatement psnumbers = con.prepareStatement(availableDrivers);
            ResultSet rs2 = psnumbers.executeQuery();

            while (rs2.next()) {
                String empID = rs2.getString("EmpID");
                String Fname = rs2.getString("FirstName");
                String Lname = rs2.getString("lastName");
                driverSelectorBill.addItem(empID + " " + Fname + " " + Lname);
            }

//            CustomerRegistrationForm form1 = new CustomerRegistrationForm(rs, rs2, "customer");
//            form1.setVisible(true);

            con.close();

        } catch (Exception e) {
            System.out.println("Vehical Driver details Getter " + e);
        }
    }

    private void searchReservationcustomer() {
        SearchFunction searchCustom = new SearchFunction();
        CustomerIDshower.setForeground(Color.BLACK);
        CustomerNameShower.setForeground(Color.BLACK);
        mobileNoShower.setForeground(Color.BLACK);
        String[] searchResult = searchCustom.getSearchedCustomerName(billCustomerSearchField.getText());
        if (searchResult[0] != null) {
            CustomerIDshower.setText(searchResult[0]);
            CustomerNameShower.setText(searchResult[1] + " " + searchResult[2]);
            mobileNoShower.setText(searchResult[3] + "  " + searchResult[4] + "  " + searchResult[5]);
        } else {
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

    private InputStream BillImageCreation() {
        int billwidgth = printBill.getWidth();
        int billheight = printBill.getHeight();
        BufferedImage bufferedImage = new BufferedImage(billwidgth, billheight, BufferedImage.TYPE_INT_RGB);
        Graphics2D gf = bufferedImage.createGraphics();
        printBill.paint(gf);
        
        InputStream inputSt;
        try{
                String cwd=Path.of("").toAbsolutePath().toString()+"\\Bills\\"+BillNo.getText()+".png";
                File outputFIle=new File(cwd);
                outputFIle.getParentFile().mkdirs();
                
		ImageIO.write(bufferedImage, "png", outputFIle);
                inputSt=new FileInputStream(cwd);
                gf.dispose();
                 return inputSt;
        } catch (Exception e) {
		System.out.println("File Container Error "+e);
                return null;
	}
    }

    private void customerDetailsBillChecker() {
        customerNoPrintBill.setText(CustomerIDshower.getText());
        CustomerNamePrintBill.setText(CustomerNameShower.getText());
        PhoneNoCustomerBill.setText(mobileNoShower.getText().substring(0, 11));
        LocalDateTime ldt = LocalDateTime.now();
        String formattedDateStr = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)
                .format(ldt);
        System.out.println("Formatted Date in String format: " + formattedDateStr);
        DateAdder.setText(formattedDateStr);

    }

    private void carDetailsDriverDetailsChecker() {
        carNoPrintBill.setText(vehicalNoBill.getText());
        DefaultTableModel CarDriverTableBill = (DefaultTableModel) billTable.getModel();
        String[] rowData = {vehicalNameBill.getText(), perDayCarCharge.getText(), pickUpDateBill.getText(), dropOffDatebill.getText(), totalCarRent.getText()};
        CarDriverTableBill.addRow(rowData);

        if (!driverStatusBill.getText().equalsIgnoreCase("No")) {
            String[] driverRawData = {"Driver(" + driverSelectorBill.getSelectedItem().toString() + ")", driverChargePerDay.getText(), pickUpDateBill.getText(), dropOffDatebill.getText(), String.valueOf(driverCharge)};
            CarDriverTableBill.addRow(driverRawData);
        }

    }
    
    
    
    private void UpdateBillTable(String tableType){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/carrentalsystem", "root", "akila123");
            String querys="insert into bill(BillNo,BillDate,TotalAmount,AdvancePaiedAmount,pendingPayment,pendingReturnDate,Bill1Image) values(?,?,?,?,?,?,?)";
            PreparedStatement ps=con.prepareStatement(querys);
            GenerateKeys billkey=new GenerateKeys();
            
            String billNoNew=billkey.getGeneratedNewID("bill");
            BillNo.setText(billNoNew);
//            InputStream iss=new FileInputStream(new File(selectedImagePath));/
            ps.setString(1, billNoNew);
            ps.setString(2, DateAdder.getText());
            ps.setBlob(7,  BillImageCreation());
            
            if (tableType.equalsIgnoreCase("proceeding")) {
                ps.setString(3, driverChargePerDay.getText());
                ps.setString(4, driverChargePerDay.getText());
                ps.setString(5, "0");
                ps.setString(6, dropOffDatebill.getText());
            }else{
                ps.setString(3, SubTotalBill1.getText());
                ps.setString(4, advancePaymentBill.getText());
                ps.setString(5, pendingPaymentBillPrint.getText());
                ps.setString(6, dropOffDatebill.getText());
            }
            
            ps.executeUpdate();
            
            String DriverID="";
            if (!driverStatusBill.getText().equalsIgnoreCase("No")) {
               DriverID= driverSelectorBill.getSelectedItem().toString().substring(0,4);
            }else{
                DriverID=null;
            }
            
            Statement st=con.createStatement();
            String updateReservation="";
            if (tableType.equalsIgnoreCase("proceeding")){
                updateReservation="update reservation set reservationStatus=\""+"Done"+"\", DriverID=\""+DriverID+"\",BillNo= \""+billNoNew+"\", customerID=\""+CustomerIDshower.getText()+"\" where ReservationID=\""+reservationNo+"\"";
          
            }else{
                updateReservation="update reservation set reservationStatus=\""+"Proceeded"+"\", DriverID=\""+DriverID+"\",BillNo= \""+billNoNew+"\", customerID=\""+CustomerIDshower.getText()+"\" where ReservationID=\""+reservationNo+"\"";
          
            }
            st.executeUpdate(updateReservation);
            JOptionPane.showMessageDialog(null, "Bill Record Added Successfully","Data Manipulation",JOptionPane.INFORMATION_MESSAGE);

            ps.close();
            con.close();
//            CarRentalSystem.closeWindows(this);
        } catch (Exception e) {
            System.out.println("DB bill  Error : "+e);
        }
    }

    private void finalBillcalculationSetter() {
        SubTotalBill1.setText(TotalChargebill.getText());
        pendingPaymentBillPrint.setText(TotalChargebill.getText());
        advancePaymentBill.setText(advancePayment.getText());
        pendingPaymentBillPrint.setText(pendingPayments.getText());
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
        printBill = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        billTable = new javax.swing.JTable();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        PhoneNoCustomerBill = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        BillNo = new javax.swing.JLabel();
        carNoPrintBill = new javax.swing.JLabel();
        customerNoPrintBill = new javax.swing.JLabel();
        CustomerNamePrintBill = new javax.swing.JLabel();
        DateAdder = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        pendingPaymentBillPrint = new javax.swing.JLabel();
        SubTotalBill1 = new javax.swing.JLabel();
        advancePaymentBill = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        SubTotalBill3 = new javax.swing.JLabel();
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
        verifyDriverVehical = new javax.swing.JButton();
        verificationStatusShowerVehicalbtn = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        carchargeLabel = new javax.swing.JLabel();
        nofoDaysLabel = new javax.swing.JLabel();
        totalCarRentLabel = new javax.swing.JLabel();
        driverChargeLabel = new javax.swing.JLabel();
        AdvancePaidCharge = new javax.swing.JLabel();
        prepaidLabel = new javax.swing.JLabel();
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
        jButton1 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(28, 78, 128));

        printBill.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("ABC Car Rental Pvt(Ltd)");
        jPanel2.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 30, -1, -1));

        jPanel6.setBackground(new java.awt.Color(28, 78, 128));

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Invoice");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(189, 189, 189)
                .addComponent(jLabel20)
                .addContainerGap(189, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(69, Short.MAX_VALUE)
                .addComponent(jLabel20)
                .addGap(26, 26, 26))
        );

        jPanel2.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 440, 120));

        jPanel7.setBackground(new java.awt.Color(28, 78, 128));

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("THANK YOU FOR YOUR BUSINESS");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(133, 133, 133)
                .addComponent(jLabel21)
                .addContainerGap(141, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel2.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 640, 440, 20));

        billTable.setBackground(new java.awt.Color(255, 255, 255));
        billTable.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        billTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Description", "Unit Price", "Start Date", "End Date", "Amount (Rs)"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(billTable);
        if (billTable.getColumnModel().getColumnCount() > 0) {
            billTable.getColumnModel().getColumn(0).setPreferredWidth(150);
            billTable.getColumnModel().getColumn(1).setResizable(false);
            billTable.getColumnModel().getColumn(1).setPreferredWidth(50);
            billTable.getColumnModel().getColumn(4).setResizable(false);
        }

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 340, 400, 150));

        jLabel22.setBackground(new java.awt.Color(204, 204, 204));
        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(0, 0, 0));
        jLabel22.setText("Details");
        jPanel2.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 140, 140, 20));

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(0, 0, 0));
        jLabel23.setText("Car No");
        jPanel2.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 200, 40, 20));

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(0, 0, 0));
        jLabel24.setText("Date");
        jPanel2.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 160, 40, 20));

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(0, 0, 0));
        jLabel25.setText("Invoice No:");
        jPanel2.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 180, 70, 20));

        jLabel26.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(0, 0, 0));
        jLabel26.setText("Email: ");
        jPanel2.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, 40, 20));

        jLabel27.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(0, 0, 0));
        jLabel27.setText("Phone No:");
        jPanel2.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, 60, 20));

        jLabel28.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(0, 0, 0));
        jLabel28.setText("Company :");
        jPanel2.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, 70, 20));

        jLabel29.setBackground(new java.awt.Color(204, 204, 204));
        jLabel29.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(0, 0, 0));
        jLabel29.setText("From");
        jPanel2.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 140, 20));

        jLabel30.setBackground(new java.awt.Color(204, 204, 204));
        jLabel30.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(0, 0, 0));
        jLabel30.setText("Bill To");
        jPanel2.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 230, 140, 20));

        jLabel31.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(0, 0, 0));
        jLabel31.setText("Customer No:");
        jPanel2.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 250, 80, 20));

        jLabel32.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(0, 0, 0));
        jLabel32.setText("Name");
        jPanel2.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 270, 60, 20));

        jLabel33.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(0, 0, 0));
        jLabel33.setText("Phone No");
        jPanel2.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 290, 60, 20));

        PhoneNoCustomerBill.setText("jLabel34");
        jPanel2.add(PhoneNoCustomerBill, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 290, 110, 20));

        jLabel35.setForeground(new java.awt.Color(0, 0, 0));
        jLabel35.setText("abcrentals@gmail.com");
        jPanel2.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 290, 130, 20));

        BillNo.setText("jLabel34");
        jPanel2.add(BillNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 180, 110, 20));

        carNoPrintBill.setText("CarNoPrintbill");
        jPanel2.add(carNoPrintBill, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 200, 110, 20));

        customerNoPrintBill.setText("jLabel34");
        jPanel2.add(customerNoPrintBill, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 250, 110, 20));

        CustomerNamePrintBill.setText("jLabel34");
        jPanel2.add(CustomerNamePrintBill, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 270, 110, 20));

        DateAdder.setText("jLabel34");
        jPanel2.add(DateAdder, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 160, 110, 20));

        jLabel41.setForeground(new java.awt.Color(0, 0, 0));
        jLabel41.setText("ABC Car Rental Pvt(Ltd)");
        jPanel2.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 250, 130, 20));

        jLabel42.setForeground(new java.awt.Color(0, 0, 0));
        jLabel42.setText("+94778586666");
        jPanel2.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 270, 110, 20));

        jLabel34.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(0, 0, 0));
        jLabel34.setText("Pending Payment");
        jPanel2.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 550, -1, 20));

        jLabel36.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(0, 0, 0));
        jLabel36.setText("Sub Total");
        jPanel2.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 490, -1, 20));

        jLabel38.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(0, 0, 0));
        jLabel38.setText("Advance Payment");
        jPanel2.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 530, 130, 20));

        jLabel39.setText("jLabel39");
        jPanel2.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pendingPaymentBillPrint.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        pendingPaymentBillPrint.setForeground(new java.awt.Color(0, 0, 0));
        pendingPaymentBillPrint.setText("jLabel40");
        jPanel2.add(pendingPaymentBillPrint, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 550, -1, 20));

        SubTotalBill1.setForeground(new java.awt.Color(0, 0, 0));
        SubTotalBill1.setText("jLabel40");
        jPanel2.add(SubTotalBill1, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 490, -1, 20));

        advancePaymentBill.setForeground(new java.awt.Color(0, 0, 0));
        advancePaymentBill.setText("jLabel40");
        jPanel2.add(advancePaymentBill, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 530, -1, 20));

        jLabel40.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(0, 0, 0));
        jLabel40.setText("Discount");
        jPanel2.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 510, 60, 20));

        SubTotalBill3.setForeground(new java.awt.Color(0, 0, 0));
        SubTotalBill3.setText("jLabel40");
        jPanel2.add(SubTotalBill3, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 510, -1, 20));

        javax.swing.GroupLayout printBillLayout = new javax.swing.GroupLayout(printBill);
        printBill.setLayout(printBillLayout);
        printBillLayout.setHorizontalGroup(
            printBillLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(printBillLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        printBillLayout.setVerticalGroup(
            printBillLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, printBillLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 665, Short.MAX_VALUE)
                .addContainerGap())
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
                        .addContainerGap())
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(34, 34, 34)
                                .addComponent(billCustomerSearchField, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                                        .addComponent(CustomerNameShower, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 144, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(BillCustomerSearchBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(moreDetailsBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(verifyCustomerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(33, 33, 33))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(verificationStatusShower)
                                .addGap(47, 47, 47))))))
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
                            .addComponent(mobileNoShower, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(moreDetailsBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(19, 19, 19)
                        .addComponent(verifyCustomerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(verificationStatusShower, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
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

        verifyDriverVehical.setText("Verify Vehical & Driver");
        verifyDriverVehical.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                verifyDriverVehicalActionPerformed(evt);
            }
        });

        verificationStatusShowerVehicalbtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        verificationStatusShowerVehicalbtn.setForeground(new java.awt.Color(0, 0, 0));
        verificationStatusShowerVehicalbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/correct2.png"))); // NOI18N
        verificationStatusShowerVehicalbtn.setText("Customer Verified");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addContainerGap())
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
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addComponent(verifyDriverVehical, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addComponent(verificationStatusShowerVehicalbtn)
                                .addGap(46, 46, 46))))))
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
                            .addComponent(verifyDriverVehical, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel10)
                                    .addComponent(pickUpDateBill))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(verificationStatusShowerVehicalbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))))
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
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Payment Details");

        carchargeLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        carchargeLabel.setForeground(new java.awt.Color(0, 0, 0));
        carchargeLabel.setText("Car Charge Per Day: ");

        nofoDaysLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        nofoDaysLabel.setForeground(new java.awt.Color(0, 0, 0));
        nofoDaysLabel.setText("No of Days:");

        totalCarRentLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        totalCarRentLabel.setForeground(new java.awt.Color(0, 0, 0));
        totalCarRentLabel.setText("Total Car Rent");

        driverChargeLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        driverChargeLabel.setForeground(new java.awt.Color(0, 0, 0));
        driverChargeLabel.setText("Driver Charge Per Day: ");

        AdvancePaidCharge.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        AdvancePaidCharge.setForeground(new java.awt.Color(0, 0, 0));
        AdvancePaidCharge.setText("Total Charge");

        prepaidLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        prepaidLabel.setForeground(new java.awt.Color(0, 0, 0));
        prepaidLabel.setText("Advance Payment(40% of Total Charge): ");

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
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(carchargeLabel)
                                    .addComponent(nofoDaysLabel)
                                    .addComponent(totalCarRentLabel)
                                    .addComponent(driverChargeLabel)
                                    .addComponent(AdvancePaidCharge))
                                .addGap(122, 122, 122)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(driverChargePerDay, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(totalCarRent, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(numberOfDays, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(perDayCarCharge, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(TotalChargebill, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(27, 27, 27))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(prepaidLabel)
                            .addComponent(jLabel18))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pendingPayments, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(advancePayment, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addComponent(verificationStatusShowerVehical)
                                .addGap(37, 37, 37))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addComponent(verifyDriverVehicalBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(25, 25, 25))))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(carchargeLabel)
                    .addComponent(perDayCarCharge))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nofoDaysLabel)
                    .addComponent(numberOfDays))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(totalCarRentLabel)
                    .addComponent(totalCarRent))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(driverChargeLabel)
                    .addComponent(driverChargePerDay))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(AdvancePaidCharge)
                    .addComponent(TotalChargebill))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(prepaidLabel)
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

        jButton1.setText("Print");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(153, 255, 153));
        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton4.setForeground(new java.awt.Color(0, 0, 0));
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/correct2.png"))); // NOI18N
        jButton4.setText("Proceed ");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(255, 102, 102));
        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton5.setForeground(new java.awt.Color(0, 0, 0));
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/discardBtnImage.png"))); // NOI18N
        jButton5.setText("Discard");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(printBill, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(164, 164, 164))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(printBill, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1400, 780));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void billCustomerSearchFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_billCustomerSearchFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_billCustomerSearchFieldActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        CustomerRegistrationForm c1 = new CustomerRegistrationForm("customer");
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
            String singleData = "select * from customer where customerID=\"" + CustomerIDshower.getText() + "\"";
            PreparedStatement ps = con.prepareStatement(singleData);
            ResultSet rs = ps.executeQuery();

            String singleDataPhoneNumbers = "select * from customerphone where customerID=\"" + CustomerIDshower.getText() + "\"";
            PreparedStatement psnumbers = con.prepareStatement(singleDataPhoneNumbers);
            ResultSet rs2 = psnumbers.executeQuery();

            CustomerRegistrationForm form1 = new CustomerRegistrationForm(rs, rs2, "customer");
            form1.setVisible(true);

            con.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_moreDetailsBtnActionPerformed

    private void verifyCustomerBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_verifyCustomerBtnActionPerformed
        // TODO add your handling code here:
        int resultofCUstomerValidation = JOptionPane.showConfirmDialog(null, "Please Confirm that you double check the details with NIC", "Deleting Confirmation",
                JOptionPane.YES_NO_OPTION);
        if (resultofCUstomerValidation == 0) {
            verificationStatusShower.setVisible(true);
            verifyCustomerBtn.setVisible(false);
            System.out.println(verifyCustomerBtn.getAlignmentX() + "   " + verifyCustomerBtn.getAlignmentY());
            billCustomerSearchField.setEditable(false);
            BillCustomerSearchBtn.setEnabled(false);

            verificationStatusShowerVehical.setVisible(true);
            verification_customer=true;
            customerDetailsBillChecker();
        }

    }//GEN-LAST:event_verifyCustomerBtnActionPerformed

    private void verifyDriverVehicalBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_verifyDriverVehicalBtn1ActionPerformed
        // TODO add your handling code here:

        if (!driverStatusBill.getText().equalsIgnoreCase("no")) {

        }
        int resultofDriverVehicalValidation = JOptionPane.showConfirmDialog(null, "Please Confirm that you double check the details with NIC", "Deleting Confirmation",
                JOptionPane.YES_NO_OPTION);
        if (resultofDriverVehicalValidation == 0) {

            verifyDriverVehicalBtn1.setVisible(false);
            verificationStatusShowerVehical.setVisible(true);
            verification_finalPayment=true;
            finalBillcalculationSetter();
        }
    }//GEN-LAST:event_verifyDriverVehicalBtn1ActionPerformed

    private void verifyDriverVehicalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_verifyDriverVehicalActionPerformed
        // TODO add your handling code here:
        verificationStatusShowerVehicalbtn.setVisible(true);
        verificationStatusShowerVehical.setVisible(false);
        verifyDriverVehical.setVisible(false);
        carDetailsDriverDetailsChecker();
        verification_DriverVehical=true;
    }//GEN-LAST:event_verifyDriverVehicalActionPerformed

    private void advancePaymentKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_advancePaymentKeyTyped
        // TODO add your handling code here:

        double chagingValue = Double.valueOf(TotalChargebill.getText()) - Double.valueOf(advancePayment.getText());
        pendingPayments.setText(String.valueOf(chagingValue));
    }//GEN-LAST:event_advancePaymentKeyTyped

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("Print Data");

        job.setPrintable(new Printable() {
            public int print(Graphics pg, PageFormat pf, int pageNum) {
                pf.setOrientation(PageFormat.PORTRAIT);
                if (pageNum > 0) {
                    return Printable.NO_SUCH_PAGE;
                }

                Graphics2D g2 = (Graphics2D) pg;
                g2.translate(pf.getImageableX(), pf.getImageableY());
                g2.scale(1.355, 1.18);

                printBill.print(g2);

                return Printable.PAGE_EXISTS;

            }
        });
        boolean ok = job.printDialog();
        if (ok) {
            try {

                job.print();
            } catch (PrinterException ex) {
                ex.printStackTrace();
            }
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        CarRentalSystem.closeWindows(this);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        if (verification_customer && verification_DriverVehical && verification_finalPayment) {
            UpdateBillTable(correspondingTable);
           
        }else{
            JOptionPane.showMessageDialog(null, "please Verify all details","Bill Error",JOptionPane.ERROR_MESSAGE);
        }
        
        
    }//GEN-LAST:event_jButton4ActionPerformed

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
    private javax.swing.JLabel AdvancePaidCharge;
    private javax.swing.JButton BillCustomerSearchBtn;
    private javax.swing.JLabel BillNo;
    private javax.swing.JLabel CustomerIDshower;
    private javax.swing.JLabel CustomerNamePrintBill;
    private javax.swing.JLabel CustomerNameShower;
    private javax.swing.JLabel DateAdder;
    private javax.swing.JLabel PhoneNoCustomerBill;
    private javax.swing.JLabel SubTotalBill1;
    private javax.swing.JLabel SubTotalBill3;
    private javax.swing.JLabel TotalChargebill;
    private javax.swing.JTextField advancePayment;
    private javax.swing.JLabel advancePaymentBill;
    private javax.swing.JTextField billCustomerSearchField;
    private javax.swing.JTable billTable;
    private javax.swing.JLabel carNoPrintBill;
    private javax.swing.JLabel carchargeLabel;
    private javax.swing.JLabel customerNoPrintBill;
    private javax.swing.JLabel driverChargeLabel;
    private javax.swing.JLabel driverChargePerDay;
    private javax.swing.JLabel driverDetailsBill;
    private javax.swing.JComboBox<String> driverSelectorBill;
    private javax.swing.JLabel driverStatusBill;
    private javax.swing.JLabel dropOffDatebill;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
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
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel mobileNoShower;
    private javax.swing.JButton moreDetailsBtn;
    private javax.swing.JLabel nofoDaysLabel;
    private javax.swing.JLabel numberOfDays;
    private javax.swing.JLabel pendingPaymentBillPrint;
    private javax.swing.JLabel pendingPayments;
    private javax.swing.JLabel perDayCarCharge;
    private javax.swing.JLabel pickUpDateBill;
    private javax.swing.JLabel prepaidLabel;
    private javax.swing.JPanel printBill;
    private javax.swing.JLabel totalCarRent;
    private javax.swing.JLabel totalCarRentLabel;
    private javax.swing.JLabel vehicalNameBill;
    private javax.swing.JLabel vehicalNoBill;
    private javax.swing.JLabel verificationStatusShower;
    private javax.swing.JLabel verificationStatusShowerVehical;
    private javax.swing.JLabel verificationStatusShowerVehicalbtn;
    private javax.swing.JButton verifyCustomerBtn;
    private javax.swing.JButton verifyDriverVehical;
    private javax.swing.JButton verifyDriverVehicalBtn1;
    // End of variables declaration//GEN-END:variables
}
