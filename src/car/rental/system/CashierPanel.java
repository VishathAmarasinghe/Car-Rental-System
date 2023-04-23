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
import EditDelete.cell.TableActionCellEditor;
import EditDelete.cell.TableActionCellRender;
import EditDelete.cell.TableActionEvent;
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
    //get the clicked Index
    private String clickedIndexID = "";

    /**
     * holding current page name that cashier is using
     */
    private String currentPageHolder = "dashboardPage";

    /**
     * declare customer object
     */
    private CustomerData customer1 = null;

    /**
     * holder to set accessed cashier details
     */
    private String AccessedCashierID = null;

    /**
     * declare Statistical data class object
     */
    private StatisticalData stat1 = null;

    /**
     * constructor
     *
     * @param cashierID
     */
    public CashierPanel(String cashierID) {
        initComponents();
        AccessedCashierID = cashierID;     //assgin the selected cashier ID
        customer1 = new CustomerData();   //initialize customer object
        stat1 = new StatisticalData();      //initialize statistical object
        customer1.loadCustomerData("all", null, customerTable);   //load all customer details

        loadPendingData("pending");     //load all pending reservation data
        loadPendingData("proceeded");     //load all proceeded reservation data
        loadBillData();                         //load all bill data
        showStatisticalData();                  //show statistical data in the dashboad 

        //create object of table action event
        TableActionEvent event = new TableActionEvent() {
            @Override
            public void onEdit(int row) {    //check table is in edit mode
                System.out.println("Edit row : " + row);
                if (customerTable.isEditing()) {
                    editSelectedCustomerData(row);     //edit selected customer data
                } else if (PendingReservationsTable.isEditing()) {

                } else if (ProceededReservationTable.isEditing()) {
                    System.out.println("edit dis available");

                }
            }

            @Override
            public void onDelete(int row) {
                if (customerTable.isEditing()) {
                    customerTable.getCellEditor().stopCellEditing();    //stop table editing
                    System.out.println("Delete row : " + row);
                }

                int resultofDelete = JOptionPane.showConfirmDialog(null, "Are you sure that you want to delete record", "Deleting Confirmation",
                        JOptionPane.YES_NO_OPTION);
                if (resultofDelete == 0) {
                    deleteSelectedRaw(row);    //delete selected raw corresponding to the table
                }
            }

        };
        TableBtnActionEvent event1 = new TableBtnActionEvent() {    //create object of table button(Success)
            @Override
            public void onBtnEdit(int row) {
                System.out.println("Proceed CLidked " + row);

                if (PendingReservationsTable.isEditing()) {
                    editSelectedPendingTable(row, "pending");     //edit pending table

                } else if (ProceededReservationTable.isEditing()) {
                    editSelectedPendingTable(row, "proceeding");   //edit proceeding table
                }
            }

            @Override
            public void onBtnDelete(int row) {
                if (PendingReservationsTable.isEditing()) {
                    PendingReservationsTable.getCellEditor().stopCellEditing();   //stop cell editing
                }
                if (ProceededReservationTable.isEditing()) {
                    ProceededReservationTable.getCellEditor().stopCellEditing();
                }
                int resultofDelete = JOptionPane.showConfirmDialog(null, "Are you sure that you want to delete record", "Deleting Confirmation",
                        JOptionPane.YES_NO_OPTION);
                if (resultofDelete == 0) {
                    deleteSelectedRaw(row);   //delete corresponding column
                }
            }
        };

        
        
        // add success , discard button to the pending reservation table
        PendingReservationsTable.getColumnModel().getColumn(6).setCellRenderer(new TableActionbtnCellRender(true));
        PendingReservationsTable.getColumnModel().getColumn(6).setCellEditor(new TableActionCellBtnEdit(event1, true));

        
         // add success , discard button to the proceed reservation table
        ProceededReservationTable.getColumnModel().getColumn(6).setCellRenderer(new TableActionbtnCellRender(false));
        ProceededReservationTable.getColumnModel().getColumn(6).setCellEditor(new TableActionCellBtnEdit(event1, false));

        
        //add edit delete button to the customer table
        customerTable.getColumnModel().getColumn(7).setCellRenderer(new TableActionCellRender());
        customerTable.getColumnModel().getColumn(7).setCellEditor(new TableActionCellEditor(event));
    }

    /**
     *show all statistical data inside the dashboard
     */
    private void showStatisticalData() {
        stat1.barchartCashier(cashierBarchartpanel);
        String[] statResut = stat1.statisticalDataSenderCasher();
        customerNoStat.setText(statResut[1]);
        pendingreservationstat.setText(statResut[0]);
        proceededReservationStat.setText(statResut[2]);

    }
    
    private void stopCellEditing(){
        if (PendingReservationsTable.isEditing()) {
            PendingReservationsTable.getCellEditor().stopCellEditing();   //stop cell editing
        }
        if (ProceededReservationTable.isEditing()) {
            ProceededReservationTable.getCellEditor().stopCellEditing();   //stop cell editing
        }
        if (customerTable.isEditing()) {
            customerTable.getCellEditor().stopCellEditing();   //stop cell editing
        }
        
    }

    /**
     *edit selected pending table
     * @param raw
     * @param tableType
     */
    private void editSelectedPendingTable(int raw, String tableType) {

        try {
            if (tableType.equalsIgnoreCase("pending")) {
                DefaultTableModel pendingTableClicked = (DefaultTableModel) PendingReservationsTable.getModel();   //get selected  row
                clickedIndexID = (String) pendingTableClicked.getValueAt(raw, 0);
            } else {
                DefaultTableModel proceedTableGetClicked = (DefaultTableModel) ProceededReservationTable.getModel();
                clickedIndexID = (String) proceedTableGetClicked.getValueAt(raw, 0);
            }

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/carrentalsystem", "root", "akila123");
            String singleData = "select * from reservation where reservationID=\"" + clickedIndexID + "\"";
            PreparedStatement ps = con.prepareStatement(singleData);
            ResultSet rs = ps.executeQuery();

            System.out.println("executed here");
            Bill billrender = new Bill(rs, tableType, AccessedCashierID);  //create bill object 
            billrender.setVisible(true);

            con.close();

        } catch (Exception e) {
            System.out.println("edit selected Pnael " + e);
        }
    }

    /**
     *load all pending data
     * @param tableType
     */
    private void loadPendingData(String tableType) {
        try {

            DefaultTableModel proceedTableLoad = null;
            DefaultTableModel pendingTableLoad = null;
            DefaultTableModel doneProceedTableLoad = null;

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/carrentalsystem", "root", "akila123");
            String s = "";
            if (tableType.equalsIgnoreCase("proceeded")) {
                s = "select * from reservation where ReservationStatus=\"proceeded\" order by DropOffDate ";
                proceedTableLoad = (DefaultTableModel) ProceededReservationTable.getModel();
                proceedTableLoad.getDataVector().removeAllElements();    //remove all data from table
                proceedTableLoad.fireTableDataChanged();    
            } else if (tableType.equalsIgnoreCase("pending")) {
                s = "select * from reservation where ReservationStatus=\"pending\" ";
                pendingTableLoad = (DefaultTableModel) PendingReservationsTable.getModel();
                pendingTableLoad.getDataVector().removeAllElements();
                pendingTableLoad.fireTableDataChanged();
            } else {
                s = "select * from reservation where ReservationStatus=\"Done\" ";
                doneProceedTableLoad = (DefaultTableModel) finishedReservationTable.getModel();
                doneProceedTableLoad.getDataVector().removeAllElements();
                doneProceedTableLoad.fireTableDataChanged();
            }

            PreparedStatement ps = con.prepareStatement(s);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String reserveID = rs.getString("ReservationID");   //get reservation data from database

                String pickedUpdate = rs.getString("PickedUpDate");
                String dropOffDate = rs.getString("DropOffDate");
                String VehicalNumer = rs.getString("VehicalNumber");
                String dummyCustName = rs.getString("DummyName");
                String driverRequest = rs.getString("DriverStatus");
                String billNo = rs.getString("BillNo");
                String cashierID = rs.getString("CustomerID");

                if (tableType.equalsIgnoreCase("proceeded")) {
                    String[] proceededData = {reserveID, pickedUpdate, dropOffDate, VehicalNumer, cashierID, billNo};
                    proceedTableLoad.addRow(proceededData);  //add retreived data to the corresponding table
                } else if (tableType.equalsIgnoreCase("pending")) {
                    String[] ownerData = {reserveID, pickedUpdate, dropOffDate, VehicalNumer, driverRequest, dummyCustName};
                    pendingTableLoad.addRow(ownerData);  //add retreived data to the corresponding table
                } else {
                    String[] finisedData = {reserveID, pickedUpdate, dropOffDate, VehicalNumer, cashierID, billNo};
                    doneProceedTableLoad.addRow(finisedData);  //add retreived data to the corresponding table
                }
            }
            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     *load bill data
     */
    private void loadBillData() {
        try {

            DefaultTableModel billdataTable = null;
            billdataTable = (DefaultTableModel) billTable.getModel();
            billdataTable.getDataVector().removeAllElements();
            billdataTable.fireTableDataChanged();

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/carrentalsystem", "root", "akila123");
            String s = "select * from Bill";

            PreparedStatement ps = con.prepareStatement(s);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String billID = rs.getString("BillNo");    //get bill data from database

                String BillDate = rs.getString("BillDate");
                String totalAmount = rs.getString("TotalAmount");
                String pendingPayment = rs.getString("pendingPayment");

                String[] finisedData = {billID, BillDate, totalAmount, pendingPayment};
                billdataTable.addRow(finisedData);

            }
            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            System.out.println("Bill load details table " + e);
        }
    }

    /**
     *edit selected customer details
     * @param raw
     */
    private void editSelectedCustomerData(int raw) {
        DefaultTableModel ownerTableclicked = (DefaultTableModel) customerTable.getModel();
        clickedIndexID = (String) ownerTableclicked.getValueAt(raw, 0);
        try {

            System.out.println("editbtnComes");
            CustomerRegistrationForm form1 = new CustomerRegistrationForm(clickedIndexID, "customer", null); //open customer detailspage
            form1.setVisible(true);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     *delete selected raw from table
     * @param rawNo
     */
    private void deleteSelectedRaw(int rawNo) {
        try {

            DefaultTableModel customerTableModel = (DefaultTableModel) customerTable.getModel();
            DefaultTableModel proceedReservationTableClicked = (DefaultTableModel) ProceededReservationTable.getModel();
            DefaultTableModel PendingReservationTableClicked = (DefaultTableModel) PendingReservationsTable.getModel();

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/carrentalsystem", "root", "akila123");

            String deletePhoneNoRaw = "";
            Statement st = con.createStatement();
            System.out.println("Clicked indez  " + clickedIndexID);

            if (currentPageHolder.equalsIgnoreCase("customerPage")) {
                clickedIndexID = (String) customerTableModel.getValueAt(rawNo, 0);
                deletePhoneNoRaw = "delete from customerphone where customerId=\"" + clickedIndexID + "\"";
                st.executeUpdate(deletePhoneNoRaw);
                String deleteRaw = "delete from customer where customerId=\"" + clickedIndexID + "\"";
                st.executeUpdate(deleteRaw);
                customerTableModel.removeRow(rawNo);

            } else if (currentPageHolder.equalsIgnoreCase("ProceedReservationPage")) {
                clickedIndexID = (String) proceedReservationTableClicked.getValueAt(rawNo, 0);
                deletePhoneNoRaw = "delete from reservation where ReservationID=\"" + clickedIndexID + "\"";
                st.executeUpdate(deletePhoneNoRaw);
                proceedReservationTableClicked.removeRow(rawNo);
            } else if (currentPageHolder.equalsIgnoreCase("PendingReservationPage")) {
                clickedIndexID = (String) PendingReservationTableClicked.getValueAt(rawNo, 0);
                deletePhoneNoRaw = "delete from reservation where ReservationID=\"" + clickedIndexID + "\"";
                st.executeUpdate(deletePhoneNoRaw);
                PendingReservationTableClicked.removeRow(rawNo);
            }

            con.close();
        } catch (Exception e) {
            System.out.println("Error from casherPanel 154  " + e);
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
        customerNoStat = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        pendingreservationstat = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        proceededReservationStat = new javax.swing.JLabel();
        cashierBarchartpanel = new javax.swing.JPanel();
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
        finishedReservationPanel = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        finishedReservationTable = new javax.swing.JTable();
        jLabel14 = new javax.swing.JLabel();
        billPanel = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        billTable = new javax.swing.JTable();
        jLabel13 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        CashierDashboard = new javax.swing.JButton();
        addCustomerBtn = new javax.swing.JButton();
        proceedReservationBtn = new javax.swing.JButton();
        pendingreservationsBtn = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        succeededReservations = new javax.swing.JButton();
        billdetailsbtn = new javax.swing.JButton();

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

        customerNoStat.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        customerNoStat.setForeground(new java.awt.Color(255, 255, 255));
        customerNoStat.setText("50");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addComponent(customerNoStat)
                .addGap(21, 21, 21))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addComponent(customerNoStat, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        pendingreservationstat.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        pendingreservationstat.setForeground(new java.awt.Color(255, 255, 255));
        pendingreservationstat.setText("50");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addComponent(pendingreservationstat)
                .addGap(19, 19, 19))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addComponent(pendingreservationstat, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        proceededReservationStat.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        proceededReservationStat.setForeground(new java.awt.Color(255, 255, 255));
        proceededReservationStat.setText("50");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(25, Short.MAX_VALUE)
                .addComponent(proceededReservationStat)
                .addGap(20, 20, 20))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addComponent(proceededReservationStat, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        cashierBarchartpanel.setLayout(new java.awt.BorderLayout());

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
            .addGroup(DashboardCashierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DashboardCashierLayout.createSequentialGroup()
                    .addContainerGap(35, Short.MAX_VALUE)
                    .addComponent(cashierBarchartpanel, javax.swing.GroupLayout.PREFERRED_SIZE, 1128, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(47, Short.MAX_VALUE)))
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
            .addGroup(DashboardCashierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DashboardCashierLayout.createSequentialGroup()
                    .addContainerGap(287, Short.MAX_VALUE)
                    .addComponent(cashierBarchartpanel, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(15, Short.MAX_VALUE)))
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

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(82, 82, 82)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 1100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(55, Short.MAX_VALUE))
        );
        customerAddingPanelLayout.setVerticalGroup(
            customerAddingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(customerAddingPanelLayout.createSequentialGroup()
                .addContainerGap(56, Short.MAX_VALUE)
                .addGroup(customerAddingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                false, false, false, false, false, false, true
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

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Pending Reservations");

        javax.swing.GroupLayout PendingReservationsPanelLayout = new javax.swing.GroupLayout(PendingReservationsPanel);
        PendingReservationsPanel.setLayout(PendingReservationsPanelLayout);
        PendingReservationsPanelLayout.setHorizontalGroup(
            PendingReservationsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PendingReservationsPanelLayout.createSequentialGroup()
                .addContainerGap(51, Short.MAX_VALUE)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 1100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(59, Short.MAX_VALUE))
            .addGroup(PendingReservationsPanelLayout.createSequentialGroup()
                .addGap(506, 506, 506)
                .addComponent(jLabel5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PendingReservationsPanelLayout.setVerticalGroup(
            PendingReservationsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PendingReservationsPanelLayout.createSequentialGroup()
                .addContainerGap(63, Short.MAX_VALUE)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 512, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );

        jTabbedPane1.addTab("admin", PendingReservationsPanel);

        ProceedReservationPanel.setBackground(new java.awt.Color(241, 241, 241));

        ProceededReservationTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ReservationID", "PickedUp Date", "DropOff Date", "VehicalID", "CustomerNo", "BillNo", "Settings"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        ProceededReservationTable.setRowHeight(30);
        ProceededReservationTable.setSelectionBackground(new java.awt.Color(28, 78, 128));
        jScrollPane3.setViewportView(ProceededReservationTable);
        if (ProceededReservationTable.getColumnModel().getColumnCount() > 0) {
            ProceededReservationTable.getColumnModel().getColumn(4).setHeaderValue("CustomerNo");
            ProceededReservationTable.getColumnModel().getColumn(5).setHeaderValue("BillNo");
            ProceededReservationTable.getColumnModel().getColumn(6).setHeaderValue("Settings");
        }

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
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
                .addGap(497, 497, 497)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        ProceedReservationPanelLayout.setVerticalGroup(
            ProceedReservationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ProceedReservationPanelLayout.createSequentialGroup()
                .addContainerGap(64, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 512, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );

        jTabbedPane1.addTab("cashier", ProceedReservationPanel);

        finishedReservationPanel.setBackground(new java.awt.Color(241, 241, 241));

        finishedReservationTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ReservationID", "PickedUp Date", "DropOff Date", "VehicalID", "CustomerNo", "BillNo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        finishedReservationTable.setRowHeight(30);
        finishedReservationTable.setSelectionBackground(new java.awt.Color(28, 78, 128));
        jScrollPane7.setViewportView(finishedReservationTable);
        if (finishedReservationTable.getColumnModel().getColumnCount() > 0) {
            finishedReservationTable.getColumnModel().getColumn(4).setHeaderValue("CustomerNo");
            finishedReservationTable.getColumnModel().getColumn(5).setHeaderValue("BillNo");
        }

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 0, 0));
        jLabel14.setText("Finished Reservations");

        javax.swing.GroupLayout finishedReservationPanelLayout = new javax.swing.GroupLayout(finishedReservationPanel);
        finishedReservationPanel.setLayout(finishedReservationPanelLayout);
        finishedReservationPanelLayout.setHorizontalGroup(
            finishedReservationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, finishedReservationPanelLayout.createSequentialGroup()
                .addContainerGap(55, Short.MAX_VALUE)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 1100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(55, Short.MAX_VALUE))
            .addGroup(finishedReservationPanelLayout.createSequentialGroup()
                .addGap(497, 497, 497)
                .addComponent(jLabel14)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        finishedReservationPanelLayout.setVerticalGroup(
            finishedReservationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(finishedReservationPanelLayout.createSequentialGroup()
                .addContainerGap(65, Short.MAX_VALUE)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 512, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );

        jTabbedPane1.addTab("cashier", finishedReservationPanel);

        billPanel.setBackground(new java.awt.Color(241, 241, 241));

        billTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Bill ID", "Bill Date", "Total Amount", "Pending Payment"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        billTable.setRowHeight(30);
        billTable.setSelectionBackground(new java.awt.Color(28, 78, 128));
        billTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                billTableMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(billTable);

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 0, 0));
        jLabel13.setText("Bills");

        javax.swing.GroupLayout billPanelLayout = new javax.swing.GroupLayout(billPanel);
        billPanel.setLayout(billPanelLayout);
        billPanelLayout.setHorizontalGroup(
            billPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, billPanelLayout.createSequentialGroup()
                .addContainerGap(55, Short.MAX_VALUE)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 1100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(55, Short.MAX_VALUE))
            .addGroup(billPanelLayout.createSequentialGroup()
                .addGap(575, 575, 575)
                .addComponent(jLabel13)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        billPanelLayout.setVerticalGroup(
            billPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(billPanelLayout.createSequentialGroup()
                .addContainerGap(66, Short.MAX_VALUE)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 512, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );

        jTabbedPane1.addTab("cashier", billPanel);

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

        succeededReservations.setBackground(new java.awt.Color(28, 78, 128));
        succeededReservations.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        succeededReservations.setForeground(new java.awt.Color(255, 255, 255));
        succeededReservations.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/cars3.png"))); // NOI18N
        succeededReservations.setText("Finished Reservations");
        succeededReservations.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                succeededReservationsActionPerformed(evt);
            }
        });

        billdetailsbtn.setBackground(new java.awt.Color(28, 78, 128));
        billdetailsbtn.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        billdetailsbtn.setForeground(new java.awt.Color(255, 255, 255));
        billdetailsbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/cars3.png"))); // NOI18N
        billdetailsbtn.setText("Bills");
        billdetailsbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                billdetailsbtnActionPerformed(evt);
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
                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(succeededReservations, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(billdetailsbtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addGap(37, 37, 37)
                .addComponent(proceedReservationBtn)
                .addGap(42, 42, 42)
                .addComponent(succeededReservations)
                .addGap(29, 29, 29)
                .addComponent(billdetailsbtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 194, Short.MAX_VALUE)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 290, -1));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void CashierDashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CashierDashboardActionPerformed
        // TODO add your handling code here:
        currentPageHolder = "dashboardPage";
        jTabbedPane1.setSelectedIndex(0);
        showStatisticalData();

    }//GEN-LAST:event_CashierDashboardActionPerformed

    private void addCustomerBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCustomerBtnActionPerformed
        // TODO add your handling code here:
        currentPageHolder = "customerPage";
        customer1.loadCustomerData("All", null, customerTable);
        jTabbedPane1.setSelectedIndex(1);
        stopCellEditing();
    }//GEN-LAST:event_addCustomerBtnActionPerformed

    private void proceedReservationBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_proceedReservationBtnActionPerformed
        // TODO add your handling code here:
        stopCellEditing();
        currentPageHolder = "ProceedReservationPage";
        jTabbedPane1.setSelectedIndex(3);
        loadPendingData("proceeded");

    }//GEN-LAST:event_proceedReservationBtnActionPerformed

    private void pendingreservationsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pendingreservationsBtnActionPerformed
        stopCellEditing();
        jTabbedPane1.setSelectedIndex(2);
        currentPageHolder = "PendingReservationPage";
        loadPendingData("pending");
        loadPendingData("proceeded");

        // TODO add your handling code here:
    }//GEN-LAST:event_pendingreservationsBtnActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        CarRentalSystem.closeWindows(this);
        CustomSearch1 carsearch=new CustomSearch1();
        carsearch.setVisible(true);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        CustomerRegistrationForm customer1 = new CustomerRegistrationForm(null, "customer", "");
        customer1.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void succeededReservationsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_succeededReservationsActionPerformed
        // TODO add your handling code here:
        stopCellEditing();
        currentPageHolder = "finishedReservationPage";
        jTabbedPane1.setSelectedIndex(4);
        loadPendingData("Done");
    }//GEN-LAST:event_succeededReservationsActionPerformed

    private void billdetailsbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_billdetailsbtnActionPerformed
        // TODO add your handling code here:
        currentPageHolder = "BillTablePage";
        jTabbedPane1.setSelectedIndex(5);
        loadBillData();
//        loadPendingData("Done");
    }//GEN-LAST:event_billdetailsbtnActionPerformed

    private void billTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_billTableMouseClicked
        // TODO add your handling code here:
        int selectedBillRaw = billTable.getSelectedRow();
        DefaultTableModel billmodelTable = (DefaultTableModel) billTable.getModel();
        clickedIndexID = (String) billmodelTable.getValueAt(selectedBillRaw, 0);
        BillShower show1 = new BillShower(clickedIndexID);
        show1.setVisible(true);


    }//GEN-LAST:event_billTableMouseClicked

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
                new CashierPanel("A002").setVisible(true);
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
    private javax.swing.JPanel billPanel;
    private javax.swing.JTable billTable;
    private javax.swing.JButton billdetailsbtn;
    private javax.swing.JPanel cashierBarchartpanel;
    private javax.swing.JPanel customerAddingPanel;
    private javax.swing.JLabel customerNoStat;
    private javax.swing.JTable customerTable;
    private javax.swing.JPanel finishedReservationPanel;
    private javax.swing.JTable finishedReservationTable;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
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
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JButton pendingreservationsBtn;
    private javax.swing.JLabel pendingreservationstat;
    private javax.swing.JButton proceedReservationBtn;
    private javax.swing.JLabel proceededReservationStat;
    private javax.swing.JButton succeededReservations;
    // End of variables declaration//GEN-END:variables
}
