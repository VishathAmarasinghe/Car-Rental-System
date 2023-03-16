/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package car.rental.system;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Akila
 */
public class CarAddingPanel extends javax.swing.JFrame {
    
    
    public int CarAddingIndicator=1;
    String selectedImagePath="";
    String previousCarNumber="";

    /**
     * Creates new form CarAddingPanel
     */
    public CarAddingPanel(ResultSet CarResult) {
        
        initComponents();
        loadOwnerDetails();
        updateCarBtn.setText("Update Car");
        updateSettingCar(CarResult);
        
        
        
        
        
        
//        if (btnType==true) {
//            updateCarBtn.setText("Add Car");
//        }else{
//            updateCarBtn.setText("Update Car");
//        }
    }
    
    public CarAddingPanel(){
        initComponents();
        loadOwnerDetails();
    }
    
    private void loadOwnerDetails(){
        try {
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/carrentalsystem", "root", "akila123");
            String s="select EmpID, firstname,lastname from employee where role=\"VOwner\"";
       
            PreparedStatement ps = con.prepareStatement(s);
            ResultSet rs=ps.executeQuery();
            while (rs.next()) {          
                String empID=rs.getString("EmpID");
                String Fname=rs.getString("FirstName");
                String Lname=rs.getString("lastName");
                ownerComboBox.addItem(empID+" "+Fname+" "+Lname);
                

            }
            rs.close();
            ps.close();
            con.close();
            ownerComboBox.setSelectedItem(null);
 
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    
    private void addNewCar(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/carrentalsystem", "root", "akila123");
            String querys="insert into cars(CarNumber,VehicalType,CarType,CarModel,SeatNo,ACType,FuelType,CarImage,OwnerID) values(?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps=con.prepareStatement(querys);
            InputStream iss=new FileInputStream(new File(selectedImagePath));
            ps.setString(1, carNumberPlate.getText());
            ps.setString(2, VehicalTypeSelecter.getSelectedItem().toString());
            ps.setBlob(8,iss );
            ps.setString(3, CarTypeSelector.getText());
            ps.setString(4, CarModel.getText());
            ps.setString(5, seatCountSelector.getText());
            ps.setString(6, ACTypeSelecter.getSelectedItem().toString());
            ps.setString(7, FuelTypeSelector.getSelectedItem().toString());
            ps.setString(9, ownerComboBox.getSelectedItem().toString().substring(0,4));
            
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Car Added Successfully","Data Manipulation",JOptionPane.INFORMATION_MESSAGE);
            CarAddingIndicator=CarAddingIndicator+1;
            ps.close();
            con.close();
            CarRentalSystem.closeWindows(this);
        } catch (Exception e) {
            System.out.println("DB Error : "+e);
        }
    }
    
    private void updateSettingCar(ResultSet CarResult ){
        try {
            while (CarResult.next()) {        
                previousCarNumber=CarResult.getString("CarNumber");
//                carNumberPlate.setText(CarResult.getString("CarNumber"));
                carNumberPlate.setEditable(false);
                
                CarTypeSelector.setText(CarResult.getString("CarType"));
                CarModel.setText(CarResult.getString("CarModel"));
                seatCountSelector.setText(CarResult.getString("SeatNo"));
                if (CarResult.getString("ACType").equalsIgnoreCase("AC")) {
                    ACTypeSelecter.setSelectedIndex(0);
                }else{
                    ACTypeSelecter.setSelectedIndex(1);
                }
                
                if (CarResult.getString("FuelType").equalsIgnoreCase("Petrol")) {
                    FuelTypeSelector.setSelectedIndex(0);
                }else{
                    FuelTypeSelector.setSelectedIndex(1);
                }
                
                VehicalTypeSelecter.setSelectedItem(new String(CarResult.getString("VehicalType")));
                for (int i = 0; i < ownerComboBox.getItemCount(); i++) {
                    if (CarResult.getString("OwnerID").equalsIgnoreCase(ownerComboBox.getItemAt(i).substring(0,4))) {
                        ownerComboBox.setSelectedIndex(i);
                    }
                }
                byte[] img=CarResult.getBytes("CarImage");
                ImageIcon image=new ImageIcon(img);
                Image im=image.getImage();
                Image myimage=im.getScaledInstance(imageShower.getWidth(), imageShower.getHeight(), Image.SCALE_SMOOTH);
                ImageIcon Scaledimage=new ImageIcon(myimage);
                imageShower.setIcon(Scaledimage);
                
            }
        } catch (Exception e) {
            System.out.println("CarRendering Error "+e);
        }
    }
    private void updateExistingCar(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/carrentalsystem", "root", "akila123");
            Statement st=con.createStatement();
            String query="update cars set VehicalType= \""+VehicalTypeSelecter.getSelectedItem().toString()
                    +"\", CarType= \""+CarTypeSelector.getText()+"\",CarModel= \""+CarModel.getText()+"\",SeatNo= \""+seatCountSelector.getText()+"\","
                    + "ACType= \""+ACTypeSelecter.getSelectedItem().toString()+"\", FuelType= \""+FuelTypeSelector.getSelectedItem().toString()+"\","
                    + "OwnerID= \""+ownerComboBox.getSelectedItem().toString().substring(0,4)+"\" where CarNumber= \""+previousCarNumber+"\"";
            st.executeUpdate(query);
            if (selectedImagePath!="") {
                try{
                    String updateImageQuary="update cars set CarImage= (?) where carNumber=\""+previousCarNumber+"\"";
                    PreparedStatement ps=con.prepareStatement(updateImageQuary);
                    InputStream iss=new FileInputStream(new File(selectedImagePath));
                    ps.setBlob(1, iss);
                    ps.executeUpdate();
                }catch(Exception e){
                    System.out.println("Car IMage Updating Error");
                }

            }
            JOptionPane.showMessageDialog(null, "Car Updated Successfully","Data Manipulation",JOptionPane.INFORMATION_MESSAGE);
            CarRentalSystem.closeWindows(this);
            
            
            
        } catch (Exception ex) {
            System.out.println("Update Existing Car "+ex);
        }
       
            
    }
    
    
    
    
        private void resetTextFields(){
            
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
        jLabel1 = new javax.swing.JLabel();
        seatCountSelector = new javax.swing.JTextField();
        carNumberPlate = new javax.swing.JTextField();
        CarTypeSelector = new javax.swing.JTextField();
        ownerComboBox = new javax.swing.JComboBox<>();
        VehicalTypeSelecter = new javax.swing.JComboBox<>();
        ACTypeSelecter = new javax.swing.JComboBox<>();
        CarModel = new javax.swing.JTextField();
        FuelTypeSelector = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        OwnerSelector = new javax.swing.JLabel();
        imageShower = new javax.swing.JLabel();
        updateCarBtn = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(28, 78, 128));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 58, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 630, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 630));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Car Registration");

        seatCountSelector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatCountSelectorActionPerformed(evt);
            }
        });

        CarTypeSelector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CarTypeSelectorActionPerformed(evt);
            }
        });

        VehicalTypeSelecter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Car", "Van" }));

        ACTypeSelecter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "AC", "Non AC" }));
        ACTypeSelecter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ACTypeSelecterActionPerformed(evt);
            }
        });

        FuelTypeSelector.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Petrol", "Diesel" }));

        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Car Number");

        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Vehical Type");

        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Car Model");

        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Car Type");

        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Seat No");

        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Fuel Type");

        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("AC Type");

        OwnerSelector.setForeground(new java.awt.Color(0, 0, 0));
        OwnerSelector.setText("Car Owner");

        imageShower.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        updateCarBtn.setText("Add Car");
        updateCarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateCarBtnActionPerformed(evt);
            }
        });

        jButton2.setText("Cancel");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Add Image");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(84, 84, 84)
                                .addComponent(jLabel1)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(carNumberPlate, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(CarTypeSelector, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE)
                                .addComponent(seatCountSelector)
                                .addComponent(ownerComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(ACTypeSelecter, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(CarModel)
                                .addComponent(VehicalTypeSelecter, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(FuelTypeSelector, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(OwnerSelector, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 152, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(imageShower, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE))
                        .addGap(118, 118, 118))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(updateCarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(4, 4, 4)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(carNumberPlate, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)
                        .addGap(2, 2, 2)
                        .addComponent(VehicalTypeSelecter, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addGap(2, 2, 2)
                        .addComponent(CarTypeSelector, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addGap(2, 2, 2)
                        .addComponent(CarModel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(imageShower, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addGap(2, 2, 2)
                .addComponent(seatCountSelector, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8)
                .addGap(2, 2, 2)
                .addComponent(ACTypeSelecter, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addGap(2, 2, 2)
                .addComponent(FuelTypeSelector, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(OwnerSelector)
                .addGap(2, 2, 2)
                .addComponent(ownerComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(updateCarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 0, 780, 630));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void CarTypeSelectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CarTypeSelectorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CarTypeSelectorActionPerformed

    private void ACTypeSelecterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ACTypeSelecterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ACTypeSelecterActionPerformed

    private void seatCountSelectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_seatCountSelectorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_seatCountSelectorActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        CarRentalSystem.closeWindows(this);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        JFileChooser browser=new JFileChooser();
        FileNameExtensionFilter filerExtention=new FileNameExtensionFilter("Images","png","jpg","jpeg");
        browser.addChoosableFileFilter(filerExtention);
        int showopenDialog=browser.showOpenDialog(null);
        
        if (showopenDialog==JFileChooser.APPROVE_OPTION) {
            File selectedImage=browser.getSelectedFile();
            selectedImagePath=selectedImage.getAbsolutePath();
            JOptionPane.showMessageDialog(null, selectedImagePath);
            
            ImageIcon image=new ImageIcon(selectedImagePath);
            Image im=image.getImage();
            Image myimage=im.getScaledInstance(imageShower.getWidth(), imageShower.getHeight(), Image.SCALE_SMOOTH);
            ImageIcon newimage=new ImageIcon(myimage);
            
            imageShower.setIcon(newimage);
            
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void updateCarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateCarBtnActionPerformed
        // TODO add your handling code here:
        if (updateCarBtn.getText().equalsIgnoreCase("Update Car")) {
            updateExistingCar();
        }else{
            addNewCar();
        }
    
    }//GEN-LAST:event_updateCarBtnActionPerformed

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
            java.util.logging.Logger.getLogger(CarAddingPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CarAddingPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CarAddingPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CarAddingPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CarAddingPanel().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> ACTypeSelecter;
    private javax.swing.JTextField CarModel;
    private javax.swing.JTextField CarTypeSelector;
    private javax.swing.JComboBox<String> FuelTypeSelector;
    private javax.swing.JLabel OwnerSelector;
    private javax.swing.JComboBox<String> VehicalTypeSelecter;
    private javax.swing.JTextField carNumberPlate;
    private javax.swing.JLabel imageShower;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JComboBox<String> ownerComboBox;
    private javax.swing.JTextField seatCountSelector;
    private javax.swing.JButton updateCarBtn;
    // End of variables declaration//GEN-END:variables
}
