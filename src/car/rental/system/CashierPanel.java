/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package car.rental.system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import raven.cell.TableActionCellEditor;
import raven.cell.TableActionCellRender;
import raven.cell.TableActionEvent;
import successBtn.TableActionCellBtnEdit;
import successBtn.TableActionbtnCellRender;
import successBtn.TableBtnActionEvent;

/**
 *
 * @author Akila
 */
public class CashierPanel extends javax.swing.JFrame {

    /**
     * Creates new form AdminPage
     */
    
    private String clickedIndexID="";
    private String currentPageHolder="dashboardPage";
    public CashierPanel() {
        initComponents();
        loadOwnerData("All");
        loadPendingData();
        TableActionEvent event = new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                System.out.println("Edit row : " + row);
                if (customerTable.isEditing()) {
                    editSelectedCustomerData(row);
                }else if(PendingReservationsTable.isEditing()){
//                    System.out.println("Car table edit check   "+CarTable.isEditing());
//                    editSelectedCustomerData(row);
                }else if(ProceededReservationTable.isEditing()){
                    System.out.println("edit dis available");
//                    editSelectedDiscountData(row);
                }
            }

            @Override
            public void onDelete(int row) {
                if (customerTable.isEditing()) {
                    customerTable.getCellEditor().stopCellEditing();
                System.out.println("Delete row : " + row);
                }
                
               int resultofDelete= JOptionPane.showConfirmDialog(null,"Are you sure that you want to delete record","Deleting Confirmation",
                       JOptionPane.YES_NO_OPTION);
                if (resultofDelete==0) {
                    deleteSelectedRaw(row);
                }
            }

        };
        TableBtnActionEvent event1=new TableBtnActionEvent() {
            @Override
            public void onBtnEdit(int row) {
                System.out.println("Proceed CLidked");
            }

            @Override
            public void onBtnDelete(int row) {
                if (PendingReservationsTable.isEditing()) {
                    PendingReservationsTable.getCellEditor().stopCellEditing();
                }
            }
        };
        
        PendingReservationsTable.getColumnModel().getColumn(6).setCellRenderer(new TableActionbtnCellRender());
        PendingReservationsTable.getColumnModel().getColumn(6).setCellEditor(new TableActionCellBtnEdit(event1));
        
       ProceededReservationTable.getColumnModel().getColumn(3).setCellRenderer(new TableActionCellRender());
       ProceededReservationTable.getColumnModel().getColumn(3).setCellEditor(new TableActionCellEditor(event));
        
      
        
        customerTable.getColumnModel().getColumn(7).setCellRenderer(new TableActionCellRender());
        customerTable.getColumnModel().getColumn(7).setCellEditor(new TableActionCellEditor(event));
    }
    
    
    
    
    
    
    private void loadOwnerData(String searchType){
        try {
            DefaultTableModel customerTableLoad=(DefaultTableModel)customerTable.getModel();
            customerTableLoad.getDataVector().removeAllElements();
            customerTableLoad.fireTableDataChanged();
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/carrentalsystem", "root", "akila123");
            String s="";
            if (searchType.equalsIgnoreCase("all")) {
                s="select CustomerID, role, firstname,lastname,NIC,email,city from customer";
            }else{
                s="select customerID,role, firstname,lastname,NIC,email,city from customer where role=\""+searchType+"\"";
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
               
                String[] ownerData={empID,Fname,Lname,role,NICno,email,city,numberArray[0],numberArray[1]};
                
                customerTableLoad.addRow(ownerData);

            }
            rs.close();
            ps.close();
            con.close();
 
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    
    private void loadPendingData(){
        try {
            DefaultTableModel pendingTableLoad=(DefaultTableModel)PendingReservationsTable.getModel();
            pendingTableLoad.getDataVector().removeAllElements();
            pendingTableLoad.fireTableDataChanged();
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/carrentalsystem", "root", "akila123");
            String s="select * from reservation where ReservationStatus=\"pending\" ";
           
            
            PreparedStatement ps = con.prepareStatement(s);
            ResultSet rs=ps.executeQuery();
            while (rs.next()) {                
                String reserveID=rs.getString("ReservationID");
                
                String pickedUpdate=rs.getString("PickedUpDate");
                String dropOffDate=rs.getString("DropOffDate");
                String VehicalNumer=rs.getString("VehicalNumber");
                String dummyCustName=rs.getString("DummyName");
                String driverRequest=rs.getString("DriverStatus");
                
               
                String[] ownerData={reserveID,dropOffDate,pickedUpdate,VehicalNumer,driverRequest,dummyCustName};
                
                pendingTableLoad.addRow(ownerData);

            }
            rs.close();
            ps.close();
            con.close();
 
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    private void editSelectedCustomerData(int raw){
        DefaultTableModel ownerTableclicked=(DefaultTableModel)customerTable.getModel();
        clickedIndexID=(String) ownerTableclicked.getValueAt(raw, 0);
        try {
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/carrentalsystem", "root", "akila123");
            String singleData="select * from customer where customerID=\""+clickedIndexID+"\"";
            PreparedStatement ps = con.prepareStatement(singleData);
            ResultSet rs=ps.executeQuery();
            
            String singleDataPhoneNumbers="select * from customerphone where customerID=\""+clickedIndexID+"\"";
            PreparedStatement psnumbers = con.prepareStatement(singleDataPhoneNumbers);
            ResultSet rs2=psnumbers.executeQuery();
            
            CustomerRegistrationForm form1=new CustomerRegistrationForm(rs, rs2,"customer");
            form1.setVisible(true);
            
            con.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    
    
    
    private void deleteSelectedRaw(int rawNo){
        try {

        DefaultTableModel customerTableModel=(DefaultTableModel) customerTable.getModel();
        DefaultTableModel proceedReservationTableClicked=(DefaultTableModel)ProceededReservationTable.getModel();
        DefaultTableModel PendingReservationTableClicked=(DefaultTableModel)PendingReservationsTable.getModel();
        
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/carrentalsystem", "root", "akila123");
        
        String deletePhoneNoRaw="";
        Statement st=con.createStatement();
        System.out.println("Clicked indez  "+clickedIndexID);
            
        if (currentPageHolder.equalsIgnoreCase("customerPage")) {
                    clickedIndexID=(String) customerTableModel.getValueAt(rawNo, 0);
                    deletePhoneNoRaw="delete from customerphone where customerId=\""+clickedIndexID+"\"";
                    st.executeUpdate(deletePhoneNoRaw);
                    String deleteRaw="delete from customer where customerId=\""+clickedIndexID+"\"";
                    st.executeUpdate(deleteRaw);
                    customerTableModel.removeRow(rawNo);
            
        }else if(currentPageHolder.equalsIgnoreCase("ProceedReservationPage")){
                    clickedIndexID=(String) proceedReservationTableClicked.getValueAt(rawNo, 0);
                    
                    proceedReservationTableClicked.removeRow(rawNo);
        }
        else if(currentPageHolder.equalsIgnoreCase("PendingReservationPage")){
                    clickedIndexID=(String) PendingReservationTableClicked.getValueAt(rawNo, 0);
//                    deletePhoneNoRaw="delete from discount where discountID=\""+clickedIndexID+"\"";
//                    st.executeUpdate(deletePhoneNoRaw);
//                    PendingReservationTableClicked.removeRow(rawNo);
        }
         
        con.close();
        } catch (Exception e) {
            System.out.println("Error from casherPanel 154  "+e);
        }
        
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        SidepanelAdminboard = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        DashboardCashier = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        customerAddingPanel = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        customerTable = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        PendingReservationsPanel = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        PendingReservationsTable = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        ProceedReservationPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        ProceededReservationTable = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        CashierDashboard = new javax.swing.JButton();
        addCustomerBtn = new javax.swing.JButton();
        proceedReservationBtn = new javax.swing.JButton();
        pendingreservationsBtn = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        SidepanelAdminboard.setBackground(new java.awt.Color(28, 78, 128));

        javax.swing.GroupLayout SidepanelAdminboardLayout = new javax.swing.GroupLayout(SidepanelAdminboard);
        SidepanelAdminboard.setLayout(SidepanelAdminboardLayout);
        SidepanelAdminboardLayout.setHorizontalGroup(
            SidepanelAdminboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1500, Short.MAX_VALUE)
        );
        SidepanelAdminboardLayout.setVerticalGroup(
            SidepanelAdminboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );

        getContentPane().add(SidepanelAdminboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1500, 60));

        jTabbedPane1.setBackground(new java.awt.Color(153, 153, 255));

        DashboardCashier.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Dashboard");

        jPanel2.setBackground(new java.awt.Color(20, 90, 45));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Total Number Of Customers");

        jPanel3.setBackground(new java.awt.Color(28, 128, 64));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("50");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addGap(21, 21, 21))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(37, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(33, 33, 33))
        );

        jPanel4.setBackground(new java.awt.Color(20, 90, 45));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Pending Reservations");

        jPanel5.setBackground(new java.awt.Color(28, 128, 64));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("50");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addGap(19, 19, 19))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(37, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(33, 33, 33))
        );

        jPanel6.setBackground(new java.awt.Color(20, 90, 45));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Proceeded Reservations");

        jPanel7.setBackground(new java.awt.Color(28, 128, 64));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("50");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(25, Short.MAX_VALUE)
                .addComponent(jLabel12)
                .addGap(20, 20, 20))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(33, 33, 33))
        );

        javax.swing.GroupLayout DashboardCashierLayout = new javax.swing.GroupLayout(DashboardCashier);
        DashboardCashier.setLayout(DashboardCashierLayout);
        DashboardCashierLayout.setHorizontalGroup(
            DashboardCashierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DashboardCashierLayout.createSequentialGroup()
                .addContainerGap(544, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addContainerGap(544, Short.MAX_VALUE))
            .addGroup(DashboardCashierLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        DashboardCashierLayout.setVerticalGroup(
            DashboardCashierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DashboardCashierLayout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(DashboardCashierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(429, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("tab6", DashboardCashier);

        customerAddingPanel.setBackground(new java.awt.Color(241, 241, 241));

        jButton1.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jButton1.setText("Add Customer");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        customerTable.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        customerTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "CustomerID", "Role", "First Name", "LastName", "NIC", "Email", "City", "Settings"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        customerTable.setRowHeight(30);
        customerTable.setSelectionBackground(new java.awt.Color(28, 78, 128));
        jScrollPane4.setViewportView(customerTable);
        if (customerTable.getColumnModel().getColumnCount() > 0) {
            customerTable.getColumnModel().getColumn(5).setResizable(false);
            customerTable.getColumnModel().getColumn(5).setPreferredWidth(150);
            customerTable.getColumnModel().getColumn(7).setResizable(false);
        }

        jLabel4.setForeground(new java.awt.Color(0, 204, 204));
        jLabel4.setText("Customer panel");

        jButton2.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jButton2.setText("Search");

        javax.swing.GroupLayout customerAddingPanelLayout = new javax.swing.GroupLayout(customerAddingPanel);
        customerAddingPanel.setLayout(customerAddingPanelLayout);
        customerAddingPanelLayout.setHorizontalGroup(
            customerAddingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(customerAddingPanelLayout.createSequentialGroup()
                .addContainerGap(55, Short.MAX_VALUE)
                .addGroup(customerAddingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(customerAddingPanelLayout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(90, 90, 90)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 1100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(55, Short.MAX_VALUE))
        );
        customerAddingPanelLayout.setVerticalGroup(
            customerAddingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(customerAddingPanelLayout.createSequentialGroup()
                .addContainerGap(60, Short.MAX_VALUE)
                .addGroup(customerAddingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 512, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46))
        );

        jTabbedPane1.addTab("Driver", customerAddingPanel);

        PendingReservationsPanel.setBackground(new java.awt.Color(241, 241, 241));

        PendingReservationsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ReservationID", "PickedUp Date", "Drop Off Date", "Vehical Number", "Driver Request", "Customer Name", "Settings"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        PendingReservationsTable.setRowHeight(30);
        PendingReservationsTable.setSelectionBackground(new java.awt.Color(28, 78, 128));
        jScrollPane5.setViewportView(PendingReservationsTable);
        if (PendingReservationsTable.getColumnModel().getColumnCount() > 0) {
            PendingReservationsTable.getColumnModel().getColumn(6).setResizable(false);
            PendingReservationsTable.getColumnModel().getColumn(6).setPreferredWidth(120);
        }

        jLabel5.setForeground(new java.awt.Color(0, 204, 204));
        jLabel5.setText("PendingReservations");

        javax.swing.GroupLayout PendingReservationsPanelLayout = new javax.swing.GroupLayout(PendingReservationsPanel);
        PendingReservationsPanel.setLayout(PendingReservationsPanelLayout);
        PendingReservationsPanelLayout.setHorizontalGroup(
            PendingReservationsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PendingReservationsPanelLayout.createSequentialGroup()
                .addContainerGap(51, Short.MAX_VALUE)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 1100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(59, Short.MAX_VALUE))
            .addGroup(PendingReservationsPanelLayout.createSequentialGroup()
                .addGap(253, 253, 253)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PendingReservationsPanelLayout.setVerticalGroup(
            PendingReservationsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PendingReservationsPanelLayout.createSequentialGroup()
                .addContainerGap(73, Short.MAX_VALUE)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 512, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );

        jTabbedPane1.addTab("admin", PendingReservationsPanel);

        ProceedReservationPanel.setBackground(new java.awt.Color(241, 241, 241));

        ProceededReservationTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        ProceededReservationTable.setRowHeight(30);
        ProceededReservationTable.setSelectionBackground(new java.awt.Color(28, 78, 128));
        jScrollPane3.setViewportView(ProceededReservationTable);

        jLabel3.setForeground(new java.awt.Color(0, 204, 204));
        jLabel3.setText("Proceeded Reservations");

        javax.swing.GroupLayout ProceedReservationPanelLayout = new javax.swing.GroupLayout(ProceedReservationPanel);
        ProceedReservationPanel.setLayout(ProceedReservationPanelLayout);
        ProceedReservationPanelLayout.setHorizontalGroup(
            ProceedReservationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ProceedReservationPanelLayout.createSequentialGroup()
                .addContainerGap(55, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(55, Short.MAX_VALUE))
            .addGroup(ProceedReservationPanelLayout.createSequentialGroup()
                .addGap(253, 253, 253)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        ProceedReservationPanelLayout.setVerticalGroup(
            ProceedReservationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ProceedReservationPanelLayout.createSequentialGroup()
                .addContainerGap(73, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 512, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );

        jTabbedPane1.addTab("cashier", ProceedReservationPanel);

        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 0, 1210, 710));

        jPanel1.setBackground(new java.awt.Color(28, 78, 128));

        CashierDashboard.setBackground(new java.awt.Color(28, 78, 128));
        CashierDashboard.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        CashierDashboard.setForeground(new java.awt.Color(255, 255, 255));
        CashierDashboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/cars3.png"))); // NOI18N
        CashierDashboard.setText("Dashboard");
        CashierDashboard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CashierDashboardActionPerformed(evt);
            }
        });

        addCustomerBtn.setBackground(new java.awt.Color(28, 78, 128));
        addCustomerBtn.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        addCustomerBtn.setForeground(new java.awt.Color(255, 255, 255));
        addCustomerBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/cars3.png"))); // NOI18N
        addCustomerBtn.setText("Add Customer");
        addCustomerBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCustomerBtnActionPerformed(evt);
            }
        });

        proceedReservationBtn.setBackground(new java.awt.Color(28, 78, 128));
        proceedReservationBtn.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        proceedReservationBtn.setForeground(new java.awt.Color(255, 255, 255));
        proceedReservationBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/cars3.png"))); // NOI18N
        proceedReservationBtn.setText("Proceeded Reservations");
        proceedReservationBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                proceedReservationBtnActionPerformed(evt);
            }
        });

        pendingreservationsBtn.setBackground(new java.awt.Color(28, 78, 128));
        pendingreservationsBtn.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        pendingreservationsBtn.setForeground(new java.awt.Color(255, 255, 255));
        pendingreservationsBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/cars3.png"))); // NOI18N
        pendingreservationsBtn.setText("Pending Reservations");
        pendingreservationsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pendingreservationsBtnActionPerformed(evt);
            }
        });

        jButton6.setBackground(new java.awt.Color(28, 78, 128));
        jButton6.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/cars3.png"))); // NOI18N
        jButton6.setText("Logout");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(addCustomerBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(proceedReservationBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(CashierDashboard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pendingreservationsBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addComponent(CashierDashboard, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(addCustomerBtn)
                .addGap(36, 36, 36)
                .addComponent(pendingreservationsBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(proceedReservationBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 340, Short.MAX_VALUE)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 290, -1));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void CashierDashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CashierDashboardActionPerformed
        // TODO add your handling code here:
        currentPageHolder="dashboardPage";
        jTabbedPane1.setSelectedIndex(0);
        
    }//GEN-LAST:event_CashierDashboardActionPerformed

    private void addCustomerBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCustomerBtnActionPerformed
        // TODO add your handling code here:
        currentPageHolder="customerPage";
        loadOwnerData("All");
        jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_addCustomerBtnActionPerformed

    private void proceedReservationBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_proceedReservationBtnActionPerformed
        // TODO add your handling code here:
        currentPageHolder="ProceedReservationPage";
        jTabbedPane1.setSelectedIndex(3);
    }//GEN-LAST:event_proceedReservationBtnActionPerformed

    private void pendingreservationsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pendingreservationsBtnActionPerformed
        jTabbedPane1.setSelectedIndex(2);
        currentPageHolder="PendingReservationPage";
        // TODO add your handling code here:
    }//GEN-LAST:event_pendingreservationsBtnActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        CustomerRegistrationForm customer1=new CustomerRegistrationForm("customer");
        customer1.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(CashierPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CashierPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CashierPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CashierPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CashierPanel().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CashierDashboard;
    private javax.swing.JPanel DashboardCashier;
    private javax.swing.JPanel PendingReservationsPanel;
    private javax.swing.JTable PendingReservationsTable;
    private javax.swing.JPanel ProceedReservationPanel;
    private javax.swing.JTable ProceededReservationTable;
    private javax.swing.JPanel SidepanelAdminboard;
    private javax.swing.JButton addCustomerBtn;
    private javax.swing.JPanel customerAddingPanel;
    private javax.swing.JTable customerTable;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
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
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JButton pendingreservationsBtn;
    private javax.swing.JButton proceedReservationBtn;
    // End of variables declaration//GEN-END:variables
}
