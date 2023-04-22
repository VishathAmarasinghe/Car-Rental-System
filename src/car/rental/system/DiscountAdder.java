/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package car.rental.system;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Akila
 */
public class DiscountAdder extends javax.swing.JFrame {

    /**
     * Creates new form DiscountAdder
     */
    
    private int updatingDiscountID=0;
    public DiscountAdder() {
        initComponents();
        setAllDiscountErrorShowers(new String[]{"","",""});
    }
    
    public DiscountAdder(String SelectedIndexID) {
        
        
        try {
            initComponents();
            setAllDiscountErrorShowers(new String[]{"","",""});
            discountPressAdder.setText("Update");
            settingUpComponentsToUpdate(SelectedIndexID);

        } catch (Exception e) {
            System.out.println("Reloading Discounts " + e);
        }

    }
    
    private void settingUpComponentsToUpdate(String SelectedIndexID){
        try {
            
            
            Connection con = DatabaseConnection.StablishDatabaseConnection();
            String singleData="select * from discount where discountID=\""+SelectedIndexID+"\"";
            PreparedStatement ps = con.prepareStatement(singleData);
            ResultSet discountResult=ps.executeQuery();
            while (discountResult.next()) {   
            
            updatingDiscountID=Integer.parseInt(discountResult.getString("discountID"));
            discountNameAdder1.setText(discountResult.getString("discountName"));
            discountPrecentageAdder.setText(discountResult.getString("precentage"));
            discountDisc.setText(discountResult.getString("Description"));
            
        }
         
            con.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
    private void updateDiscount(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/carrentalsystem", "root", "akila123");
            Statement st=con.createStatement();
            String query="update discount set discountName= \""+discountNameAdder1.getText()+"\",precentage= \""+discountPrecentageAdder.getText()+"\","
                    + "Description= \""+discountDisc.getText()+"\" where discountID="+updatingDiscountID;
            st.executeUpdate(query);
            JOptionPane.showMessageDialog(null, "Discount Updated Successfully","Data Manipulation",JOptionPane.INFORMATION_MESSAGE);
            CarRentalSystem.closeWindows(this);
        }catch(Exception e){
            System.out.println("Update Discount "+e);
        }
    }

    
    
    private void addDiscount(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/carrentalsystem", "root", "akila123");
            String querys="insert into discount(discountName,precentage,Description) values(?,?,?)";
            PreparedStatement ps=con.prepareStatement(querys);
            ps.setString(1, discountNameAdder1.getText());
            ps.setString(2, discountPrecentageAdder.getText());
            ps.setString(3, discountDisc.getText());
            
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Discount Added Successfully","Data Manipulation",JOptionPane.INFORMATION_MESSAGE);
            CarRentalSystem.closeWindows(this);
            
        }catch (Exception e){
            System.out.println("Discount Adder "+e);
        }
        
    }
    
    public void deleteSelectedDiscount(String clickedIndexID){
        try {
            Connection con = DatabaseConnection.StablishDatabaseConnection();
            Statement st = con.createStatement();
            String deletePhoneNoRaw = "delete from discount where discountID=\"" + clickedIndexID + "\"";
            st.executeUpdate(deletePhoneNoRaw);
        } catch (Exception e) {
            System.out.println("Data deleting Error");
        }
    }
    
    
    public void renderDiscountTable(JTable discountTable){
        try {
            DefaultTableModel discountTableRender = (DefaultTableModel) discountTable.getModel();
            discountTableRender.getDataVector().removeAllElements();
            discountTableRender.fireTableDataChanged();

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/carrentalsystem", "root", "akila123");
            String s = "select * from discount";
            PreparedStatement ps = con.prepareStatement(s);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String discountID = rs.getString("discountID");
                String discountName = rs.getString("discountName");
                String discountPrecentage = rs.getString("precentage");
                String discountDiscription = rs.getString("Description");

                discountTableRender.addRow(new String[]{discountID, discountName, discountPrecentage, discountDiscription});

            }
            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            System.out.println("Discount Table Render " + e);
        }
    }
    
    
    private void setAllDiscountErrorShowers(String[] arr){
        discountNameErrorShower.setText(arr[0]);
        discountPrecentageErrShower.setText(arr[1]);
        discountDiscErrorShower.setText(arr[2]);
    }
    
    
    private boolean checkDiscountValidations(){
        boolean validateResult = true;
        Validations v1 = new Validations();
        setAllDiscountErrorShowers(new String[]{"", "", ""});
        String discountNameError = v1.discountNameDescriptionValidator(discountNameAdder1.getText());
        String discountDescError = v1.discountNameDescriptionValidator(discountDisc.getText());
        String dispresentageError = v1.discountprecentageValidator(discountPrecentageAdder.getText());
        boolean result = discountNameError.equalsIgnoreCase("") && dispresentageError.equalsIgnoreCase("") && discountDescError.equalsIgnoreCase("");
        if (result) {
            return validateResult;
        } else {
            validateResult = false;
            setAllDiscountErrorShowers(new String[]{discountNameError, dispresentageError, discountDescError});

        }

        return validateResult;
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
        discountPrecentageAdder = new javax.swing.JTextField();
        discountNameAdder1 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        discountDisc = new javax.swing.JTextArea();
        discountPressAdder = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        discountNameErrorShower = new javax.swing.JLabel();
        discountPrecentageErrShower = new javax.swing.JLabel();
        discountDiscErrorShower = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(28, 78, 128));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 48, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 360, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 360));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Discount Form");

        discountNameAdder1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                discountNameAdder1ActionPerformed(evt);
            }
        });

        discountDisc.setColumns(20);
        discountDisc.setRows(5);
        jScrollPane1.setViewportView(discountDisc);

        discountPressAdder.setText("Add");
        discountPressAdder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                discountPressAdderActionPerformed(evt);
            }
        });

        jButton2.setText("Cancel");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Discount Name");

        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Discount Precentage");

        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Description");

        discountNameErrorShower.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        discountNameErrorShower.setForeground(new java.awt.Color(255, 0, 51));
        discountNameErrorShower.setText("jLabel5");

        discountPrecentageErrShower.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        discountPrecentageErrShower.setForeground(new java.awt.Color(255, 0, 51));
        discountPrecentageErrShower.setText("jLabel5");

        discountDiscErrorShower.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        discountDiscErrorShower.setForeground(new java.awt.Color(255, 0, 51));
        discountDiscErrorShower.setText("jLabel5");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(198, 198, 198)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(discountPressAdder, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(discountNameAdder1)
                                    .addComponent(discountNameErrorShower, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 141, Short.MAX_VALUE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(discountPrecentageAdder, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(discountPrecentageErrShower, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(discountDiscErrorShower, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(38, 38, 38))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(13, 13, 13)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(discountNameAdder1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(discountPrecentageAdder, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(discountNameErrorShower)
                    .addComponent(discountPrecentageErrShower))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(discountDiscErrorShower)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(discountPressAdder, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 0, 530, 360));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void discountNameAdder1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_discountNameAdder1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_discountNameAdder1ActionPerformed

    private void discountPressAdderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_discountPressAdderActionPerformed
        // TODO add your handling code here:
        if (discountPressAdder.getText().equalsIgnoreCase("Update")) {
            if (checkDiscountValidations()) {
                updateDiscount();
            }

        } else {
            if (checkDiscountValidations()) {
                addDiscount();
            }

        }
        
    }//GEN-LAST:event_discountPressAdderActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        CarRentalSystem.closeWindows(this);
    }//GEN-LAST:event_jButton2ActionPerformed

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
            java.util.logging.Logger.getLogger(DiscountAdder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DiscountAdder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DiscountAdder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DiscountAdder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DiscountAdder().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea discountDisc;
    private javax.swing.JLabel discountDiscErrorShower;
    private javax.swing.JTextField discountNameAdder1;
    private javax.swing.JLabel discountNameErrorShower;
    private javax.swing.JTextField discountPrecentageAdder;
    private javax.swing.JLabel discountPrecentageErrShower;
    private javax.swing.JButton discountPressAdder;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
