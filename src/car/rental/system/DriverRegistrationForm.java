/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package car.rental.system;

import javax.swing.JOptionPane;

/**
 *
 * @author Akila
 */
public class DriverRegistrationForm extends javax.swing.JFrame {

    Driver d1=null;
    public DriverRegistrationForm() {
        initComponents();
        d1=new Driver();
        setAllErrorShowers(new String[]{"","","","","","","","","","",""});
    }
    
    public DriverRegistrationForm(String clickedIndex) {
        initComponents();
        d1=new Driver();
        setAllErrorShowers(new String[]{"","","","","","","","","","",""});
        renderComponentsToUpdate(clickedIndex);
        addbtnDriver1.setText("Update Driver");
    }
    
    
    private void setAllErrorShowers(String[] arr){
        driverFnameErrorShower.setText(arr[0]);
        driverLNameErrorShower.setText(arr[1]);
        driverEmailErrorShower.setText(arr[2]);
        driverAddress1ErrorShower.setText(arr[3]);
        driverAddressError2Shower.setText(arr[4]);
        driverCityErrorShower.setText(arr[5]);
        DriverPhoneNo1ErrorShower.setText(arr[6]);
        DriverPhoneNo2ErrorShower.setText(arr[7]);
        DriverNICErrorShower.setText(arr[8]);
        DriverLicenceNoErrorShower.setText(arr[9]);
        DriverPaymentErrorShower.setText(arr[10]);
    }
    
    
    private void renderComponentsToUpdate(String clickedIndex){
        d1.loadEmployeeData("Driver", clickedIndex, null, "");
        d1.loadDriverAdditionalDetails();
        
        
        driverFirstName.setText(d1.getFirstName());
        DriverLastName.setText(d1.getLastName());
        DriverEmail.setText(d1.getEmail());
        DriverAddress1.setText(d1.getAddress1());
        DriverAddress2.setText(d1.getAddress2());
        DriverCity.setText(d1.getCity());
        DriverphoneNo1.setText(d1.getPhoneNo1());
        DriverphoneNo2.setText(d1.getPhoneNo2());
        driverNIC.setText(d1.getNIC());
        DriverLicence.setText(d1.getLicenceNo());
        DriverPayment.setText(String.valueOf(d1.getHireCharge()));
        
    }
    
    
    
    
    private boolean checkUserInputValidations(){
        setAllErrorShowers(new String[]{"","","","","","","","","","",""});
        Validations v1=new Validations();
        String fnameError=v1.nameChecker(driverFirstName.getText());
        String lnameError=v1.nameChecker(DriverLastName.getText());
        String emilError=v1.emailChecker(DriverEmail.getText());
        String addres1Error=v1.addressChecker(DriverAddress1.getText());
        String address2Error=v1.addressChecker(DriverAddress2.getText());
        String cityError=v1.addressChecker(DriverCity.getText());
        String phoneNoError1=v1.phoneNumberValidate(DriverphoneNo1.getText());
        String phoneNoError2=v1.phoneNumberValidate(DriverphoneNo2.getText());
        String nicError=v1.NICvalidate(driverNIC.getText());
        String hireSalError=v1.priceValidator(DriverPayment.getText());
        
        if (DriverphoneNo1.getText().equalsIgnoreCase(DriverphoneNo2.getText())) {
            phoneNoError1="Same number";
        }
        
        boolean result=fnameError.equalsIgnoreCase("") && lnameError.equalsIgnoreCase("") &&
                emilError.equalsIgnoreCase("") && addres1Error.equalsIgnoreCase("") && address2Error.equalsIgnoreCase("") &&
                cityError.equalsIgnoreCase("") && phoneNoError1.equalsIgnoreCase("") && phoneNoError2.equalsIgnoreCase("") &&
                nicError.equalsIgnoreCase("") && !DriverphoneNo1.getText().equalsIgnoreCase(DriverphoneNo2.getText()) && hireSalError.equalsIgnoreCase("");
        
        if (result) {
            return true;
        }else{
            setAllErrorShowers(new String[]{fnameError,lnameError,emilError,addres1Error,address2Error,cityError,phoneNoError1,phoneNoError2,nicError,"",hireSalError});
            return false;
        }
    }
    
    
    public void updateData(){
        d1.setFirstName(driverFirstName.getText());
        d1.setLastName(DriverLastName.getText());
        d1.setEmail(DriverEmail.getText());
        d1.setAddress1(DriverAddress1.getText());
        d1.setAddress2(DriverAddress2.getText());
        d1.setCity(DriverCity.getText());
        d1.setPhoneNo1(DriverphoneNo1.getText());
        d1.setPhoneNo2(DriverphoneNo2.getText());
        d1.setNIC(driverNIC.getText());
        d1.setLicenceNo(DriverLicence.getText());
        d1.setHireCharge(Double.valueOf(DriverPayment.getText()));
        
        boolean result=d1.updateEmployeeData(d1.getID());
        d1.updateDriverAdditionalComponents();
        if (result) {
            JOptionPane.showMessageDialog(null, "Data Updated Successfully!", "DBMS", JOptionPane.INFORMATION_MESSAGE);
        }
        CarRentalSystem.closeWindows(this);

        
        
        
        
        
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
        DriverLastName = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        DriverEmail = new javax.swing.JTextField();
        driverFirstName = new javax.swing.JTextField();
        DriverAddress2 = new javax.swing.JTextField();
        DriverAddress1 = new javax.swing.JTextField();
        DriverphoneNo1 = new javax.swing.JTextField();
        DriverCity = new javax.swing.JTextField();
        DriverphoneNo2 = new javax.swing.JTextField();
        DriverLicence = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        addbtnDriver1 = new javax.swing.JButton();
        driverAddingCancelation = new javax.swing.JButton();
        DriverPayment = new javax.swing.JTextField();
        driverNIC = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        driverFnameErrorShower = new javax.swing.JLabel();
        driverLNameErrorShower = new javax.swing.JLabel();
        driverEmailErrorShower = new javax.swing.JLabel();
        driverAddress1ErrorShower = new javax.swing.JLabel();
        driverAddressError2Shower = new javax.swing.JLabel();
        driverCityErrorShower = new javax.swing.JLabel();
        DriverLicenceNoErrorShower = new javax.swing.JLabel();
        DriverNICErrorShower = new javax.swing.JLabel();
        DriverPhoneNo1ErrorShower = new javax.swing.JLabel();
        DriverPhoneNo2ErrorShower = new javax.swing.JLabel();
        DriverPaymentErrorShower = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();

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
            .addGap(0, 790, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 790));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        DriverLastName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DriverLastNameActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Driver Registration");

        driverFirstName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                driverFirstNameActionPerformed(evt);
            }
        });

        DriverAddress2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DriverAddress2ActionPerformed(evt);
            }
        });

        DriverAddress1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DriverAddress1ActionPerformed(evt);
            }
        });

        DriverphoneNo2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DriverphoneNo2ActionPerformed(evt);
            }
        });

        DriverLicence.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DriverLicenceActionPerformed(evt);
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
        jLabel8.setText("Phone No2");

        jLabel9.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Phone No1");

        jLabel10.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setText("NIC No");

        addbtnDriver1.setText("Add Driver");
        addbtnDriver1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addbtnDriver1ActionPerformed(evt);
            }
        });

        driverAddingCancelation.setText("Cancel");
        driverAddingCancelation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                driverAddingCancelationActionPerformed(evt);
            }
        });

        DriverPayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DriverPaymentActionPerformed(evt);
            }
        });

        driverNIC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                driverNICActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setText("Charge Per Rent");

        jLabel12.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 0, 0));
        jLabel12.setText("Licence No");

        driverFnameErrorShower.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        driverFnameErrorShower.setForeground(new java.awt.Color(255, 0, 0));
        driverFnameErrorShower.setText("jLabel13");

        driverLNameErrorShower.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        driverLNameErrorShower.setForeground(new java.awt.Color(255, 0, 0));
        driverLNameErrorShower.setText("jLabel13");

        driverEmailErrorShower.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        driverEmailErrorShower.setForeground(new java.awt.Color(255, 0, 0));
        driverEmailErrorShower.setText("jLabel13");

        driverAddress1ErrorShower.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        driverAddress1ErrorShower.setForeground(new java.awt.Color(255, 0, 0));
        driverAddress1ErrorShower.setText("jLabel13");

        driverAddressError2Shower.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        driverAddressError2Shower.setForeground(new java.awt.Color(255, 0, 0));
        driverAddressError2Shower.setText("jLabel13");

        driverCityErrorShower.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        driverCityErrorShower.setForeground(new java.awt.Color(255, 0, 0));
        driverCityErrorShower.setText("jLabel13");

        DriverLicenceNoErrorShower.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        DriverLicenceNoErrorShower.setForeground(new java.awt.Color(255, 0, 0));
        DriverLicenceNoErrorShower.setText("jLabel13");

        DriverNICErrorShower.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        DriverNICErrorShower.setForeground(new java.awt.Color(255, 0, 0));
        DriverNICErrorShower.setText("jLabel13");

        DriverPhoneNo1ErrorShower.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        DriverPhoneNo1ErrorShower.setForeground(new java.awt.Color(255, 0, 0));
        DriverPhoneNo1ErrorShower.setText("jLabel13");

        DriverPhoneNo2ErrorShower.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        DriverPhoneNo2ErrorShower.setForeground(new java.awt.Color(255, 0, 0));
        DriverPhoneNo2ErrorShower.setText("jLabel13");

        DriverPaymentErrorShower.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        DriverPaymentErrorShower.setForeground(new java.awt.Color(255, 0, 0));
        DriverPaymentErrorShower.setText("jLabel13");

        jLabel13.setForeground(new java.awt.Color(0, 0, 0));
        jLabel13.setText("Required to fill all details");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(245, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap(245, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(DriverPhoneNo1ErrorShower, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(driverCityErrorShower, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addComponent(jLabel13)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(addbtnDriver1, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(37, 37, 37)
                                            .addComponent(driverAddingCancelation, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(driverNIC, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(DriverNICErrorShower, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(DriverPayment, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(DriverPaymentErrorShower, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGap(64, 64, 64)
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(DriverLicenceNoErrorShower, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(DriverLicence, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel12)))))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(DriverCity, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6)
                                    .addComponent(driverFnameErrorShower, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(driverAddressError2Shower, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(driverAddress1ErrorShower, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(driverEmailErrorShower, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(DriverAddress2, javax.swing.GroupLayout.PREFERRED_SIZE, 543, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(DriverphoneNo1, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9))
                                .addGap(64, 64, 64)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(DriverphoneNo2, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8)
                                    .addComponent(DriverPhoneNo2ErrorShower, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(DriverAddress1, javax.swing.GroupLayout.PREFERRED_SIZE, 543, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(DriverEmail, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel4)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(driverFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel3))
                                            .addGap(63, 63, 63)
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel2)
                                                .addComponent(DriverLastName, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(driverLNameErrorShower, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(11, 11, 11)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(driverFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(DriverLastName, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(driverFnameErrorShower)
                    .addComponent(driverLNameErrorShower))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addGap(2, 2, 2)
                .addComponent(DriverEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(driverEmailErrorShower)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(DriverAddress1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(driverAddress1ErrorShower)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addGap(2, 2, 2)
                .addComponent(DriverAddress2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(driverAddressError2Shower)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addGap(2, 2, 2)
                .addComponent(DriverCity, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(driverCityErrorShower)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(DriverphoneNo1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(DriverphoneNo2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(DriverPhoneNo1ErrorShower)
                    .addComponent(DriverPhoneNo2ErrorShower))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(DriverLicence, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(driverNIC, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(DriverNICErrorShower)
                    .addComponent(DriverLicenceNoErrorShower))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(DriverPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(DriverPaymentErrorShower)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addbtnDriver1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(driverAddingCancelation, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addGap(18, 18, 18))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 0, 660, 790));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void DriverLastNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DriverLastNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_DriverLastNameActionPerformed

    private void DriverAddress2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DriverAddress2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_DriverAddress2ActionPerformed

    private void DriverAddress1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DriverAddress1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_DriverAddress1ActionPerformed

    private void driverFirstNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_driverFirstNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_driverFirstNameActionPerformed

    private void DriverphoneNo2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DriverphoneNo2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_DriverphoneNo2ActionPerformed

    private void DriverLicenceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DriverLicenceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_DriverLicenceActionPerformed

    private void addbtnDriver1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addbtnDriver1ActionPerformed
        // TODO add your handling code here:
        if (addbtnDriver1.getText().equalsIgnoreCase("Update Driver")) {
            updateData();
        }else{
            if (checkUserInputValidations()) {
            String[] details={"","Driver",driverFirstName.getText(),DriverLastName.getText(),DriverEmail.getText(),DriverAddress1.getText(),
            DriverAddress2.getText(),DriverCity.getText(),driverNIC.getText(),DriverphoneNo1.getText(),DriverphoneNo2.getText()};
            d1.SetAllData(details);
            d1.setAllDriverAdditionalData(new String[]{DriverLicence.getText(),DriverPayment.getText()});
            d1.InsertEmployeeData("");
            d1.addDriverAdditionalData();
//            if (actionResult[0]) {
//                JOptionPane.showMessageDialog(null, "Data Added Successfully", "Data Manipulation", JOptionPane.INFORMATION_MESSAGE);
//                if (actionResult[1]) {
//                    JOptionPane.showMessageDialog(null, "Email Send To the New Employee", "Registration", JOptionPane.INFORMATION_MESSAGE);
//                }
//            }
            CarRentalSystem.closeWindows(this);
        }
        }
        
        
        
    }//GEN-LAST:event_addbtnDriver1ActionPerformed

    private void driverAddingCancelationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_driverAddingCancelationActionPerformed
        CarRentalSystem.closeWindows(this);
    }//GEN-LAST:event_driverAddingCancelationActionPerformed

    private void DriverPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DriverPaymentActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_DriverPaymentActionPerformed

    private void driverNICActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_driverNICActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_driverNICActionPerformed

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
            java.util.logging.Logger.getLogger(DriverRegistrationForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DriverRegistrationForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DriverRegistrationForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DriverRegistrationForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DriverRegistrationForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField DriverAddress1;
    private javax.swing.JTextField DriverAddress2;
    private javax.swing.JTextField DriverCity;
    private javax.swing.JTextField DriverEmail;
    private javax.swing.JTextField DriverLastName;
    private javax.swing.JTextField DriverLicence;
    private javax.swing.JLabel DriverLicenceNoErrorShower;
    private javax.swing.JLabel DriverNICErrorShower;
    private javax.swing.JTextField DriverPayment;
    private javax.swing.JLabel DriverPaymentErrorShower;
    private javax.swing.JLabel DriverPhoneNo1ErrorShower;
    private javax.swing.JLabel DriverPhoneNo2ErrorShower;
    private javax.swing.JTextField DriverphoneNo1;
    private javax.swing.JTextField DriverphoneNo2;
    private javax.swing.JButton addbtnDriver1;
    private javax.swing.JButton driverAddingCancelation;
    private javax.swing.JLabel driverAddress1ErrorShower;
    private javax.swing.JLabel driverAddressError2Shower;
    private javax.swing.JLabel driverCityErrorShower;
    private javax.swing.JLabel driverEmailErrorShower;
    private javax.swing.JTextField driverFirstName;
    private javax.swing.JLabel driverFnameErrorShower;
    private javax.swing.JLabel driverLNameErrorShower;
    private javax.swing.JTextField driverNIC;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
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
    // End of variables declaration//GEN-END:variables
}
