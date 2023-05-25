/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package car.rental.system;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import EditDelete.cell.TableActionCellEditor;
import EditDelete.cell.TableActionCellRender;
import EditDelete.cell.TableActionEvent;

/**
 *
 * @author Akila
 */
public class AdminPage extends javax.swing.JFrame {

    
    private int tableSelectedIndex = 0;

    /**
     *
     */
    private String clickedIndexID = "";

    /**
     *holding current user selected page panel
     */
    private String currentPageHolder = "dashboardPage";

    /**
     *declaration of employee object
     */
    private EmployeeData employee1 = null;

    /**
     *declaration of vehical owner object
     */
    private VehicalOwner owner = null;

    /**
     *declaration of statistical data class object
     */
    private StatisticalData stat1 = null;

    /**
     *declaration of car class object
     */
    private Car car1 = null;

    /**
     *declaration of discount class object
     */
    private DiscountAdder discountData = null;

    /**
     *Default constructor
     */
    public AdminPage() {
        initComponents();
        employee1 = new EmployeeData(); //initialize employee object
        owner = new VehicalOwner();   //initialize owner object
        car1 = new Car();   //initialize car object
        discountData = new DiscountAdder();  //initialize discount object
        stat1 = new StatisticalData();   //initialize statistical class object
        showStatisticalData();    //get all statistical data

        owner.loadOwnerData(vehicalOwnerAddingtable);    //load owner table data
        employee1.loadEmployeeData("all", null, employeeTable, null);    //load employee tabledata 
        car1.renderCarTable(CarTable);   //load car table data
        
        
        
        
        
        //tables edit delete button function
        TableActionEvent event = new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                System.out.println("Edit row : " + row);
                if (CarTable.isEditing()) {
                    editSelectedCarData(row);   //open seledted car details in new panel
                } else if (employeeTable.isEditing()) {
                    System.out.println("Car table edit check   " + CarTable.isEditing());
                    editSelectedEmployeeData(row);   //open selected employee details in new panel
                } else if (discountTable.isEditing()) {
                    System.out.println("edit dis available");
                    editSelectedDiscountData(row);   //open selected discount data in new panel
                } else if (vehicalOwnerAddingtable.isEditing()) {
                    System.out.println("edit owner available");
                    editSelectedOwnerData(row);    //open selected owner data in new panel
                }

            }

            @Override
            public void onDelete(int row) {
                System.out.println("Delete row : " + row);
                if (employeeTable.isEditing()) {
                    employeeTable.getCellEditor().stopCellEditing();   //stop cell editing if it is in the cell editing mode
                }
                stopCellEditing();

                int resultofDelete = JOptionPane.showConfirmDialog(null, "Are you sure that you want to delete record", "Deleting Confirmation",
                        JOptionPane.YES_NO_OPTION);
                if (resultofDelete == 0) {
                    deleteSelectedRaw(row);   //delete selected raw corresponding to the table
                }

            }

        };

        //adding edit and delete btn to the employee table last column
        employeeTable.getColumnModel().getColumn(9).setCellRenderer(new TableActionCellRender());
        employeeTable.getColumnModel().getColumn(9).setCellEditor(new TableActionCellEditor(event));

        //adding edit and delete btn to the discount table last column
        discountTable.getColumnModel().getColumn(4).setCellRenderer(new TableActionCellRender());
        discountTable.getColumnModel().getColumn(4).setCellEditor(new TableActionCellEditor(event));

        //adding edit and delete btn to the vehical Owner table last column
        vehicalOwnerAddingtable.getColumnModel().getColumn(7).setCellRenderer(new TableActionCellRender());
        vehicalOwnerAddingtable.getColumnModel().getColumn(7).setCellEditor(new TableActionCellEditor(event));

        //adding edit and delete btn to the car table last column
        CarTable.getColumnModel().getColumn(8).setCellRenderer(new TableActionCellRender());
        CarTable.getColumnModel().getColumn(6).setCellRenderer(new tableImageCellRender());
        CarTable.getColumnModel().getColumn(8).setCellEditor(new TableActionCellEditor(event));
    }

    /**
     * display statistical data inside the dashboard
     */
    private void showStatisticalData() {
        stat1.barchart(barchartPanel);
        String[] statResut = stat1.statisticalDataSender();
        customerStat.setText(statResut[1]);
        vehicalStat.setText(statResut[2]);
        employeeStat.setText(statResut[0]);

    }

    /**
     * Delete selected raw from selected table
     *
     * @param rawNo clicked raw number
     */
    private void deleteSelectedRaw(int rawNo) {
        try {

            DefaultTableModel carModel = (DefaultTableModel) CarTable.getModel();
            DefaultTableModel ownerTableclicked = (DefaultTableModel) employeeTable.getModel();
            DefaultTableModel discountTableclicked = (DefaultTableModel) discountTable.getModel();

            System.out.println("Clicked indez  " + clickedIndexID);

            if (currentPageHolder.equalsIgnoreCase("CarPage")) {
                clickedIndexID = (String) carModel.getValueAt(rawNo, 0);
                car1.deleteItem(clickedIndexID);  //delete selected car
                carModel.removeRow(rawNo);

            } else if (currentPageHolder.equalsIgnoreCase("employeePage")) {
                clickedIndexID = (String) ownerTableclicked.getValueAt(rawNo, 0);
                String clickedRole = (String) ownerTableclicked.getValueAt(rawNo, 3);
                if (clickedRole.equalsIgnoreCase("Driver")) {
                    Driver d1 = new Driver();
                    d1.deleteDriverAdditionalData(clickedIndexID);
                    employee1.deleteEmployee(clickedIndexID);   //delete selected employee
                } else {
                    employee1.deleteEmployee(clickedIndexID);  //delete selected driver
                }

                ownerTableclicked.removeRow(rawNo);
            } else if (currentPageHolder.equalsIgnoreCase("discountPage")) {
                clickedIndexID = (String) discountTable.getValueAt(rawNo, 0);
                discountData.deleteSelectedDiscount(clickedIndexID);   //delete selected discount
                discountTableclicked.removeRow(rawNo);
            } else if (currentPageHolder.equalsIgnoreCase("vehicalOwnerPage")) {
                clickedIndexID = (String) discountTable.getValueAt(rawNo, 0);
                owner.deleteSelectedOwner(clickedIndexID);    //delete selected owner
                discountTableclicked.removeRow(rawNo);
            }

        } catch (Exception e) {
            System.out.println("Error from adminpanel deleting  " + e);
        }

    }

    /**
     * functionality for edit data in employee table
     *
     * @param raw
     */
    private void editSelectedEmployeeData(int raw) {
        DefaultTableModel ownerTableclicked = (DefaultTableModel) employeeTable.getModel();
        clickedIndexID = (String) ownerTableclicked.getValueAt(raw, 0);
        String clickedRole = (String) ownerTableclicked.getValueAt(raw, 3);
        try {

            if (clickedRole.equalsIgnoreCase("Driver")) {
                DriverRegistrationForm driverform = new DriverRegistrationForm(clickedIndexID);   //load clicked driver details in new form
                driverform.setVisible(true);

            } else {
                
                //open clicked customer detail in new form
                CustomerRegistrationForm form1 = new CustomerRegistrationForm(clickedIndexID, "employee", null);
                form1.setVisible(true);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * functionality for edit data in vehicalowner table
     *
     * @param raw
     */
    private void editSelectedOwnerData(int raw) {
        DefaultTableModel ownerTableclicked = (DefaultTableModel) vehicalOwnerAddingtable.getModel();
        clickedIndexID = (String) ownerTableclicked.getValueAt(raw, 0);
        try {

            VOwnerRegistrationFrom form1 = new VOwnerRegistrationFrom(clickedIndexID);   //open new panel to update selected owner
            form1.setVisible(true);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * functionality for edit data in car table
     *
     * @param raw
     */
    private void editSelectedCarData(int raw) {
        DefaultTableModel CarTableclicked = (DefaultTableModel) CarTable.getModel();
        clickedIndexID = (String) CarTableclicked.getValueAt(raw, 0);   //get clicked raw number
        try {

            CarAddingPanel carAdd1 = new CarAddingPanel(clickedIndexID);   //open car panel to update 
            carAdd1.setVisible(true);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * functionality for edit data in discount table
     *
     * @param raw
     */
    private void editSelectedDiscountData(int raw) {
        DefaultTableModel discountclicked = (DefaultTableModel) discountTable.getModel();
        clickedIndexID = (String) discountclicked.getValueAt(raw, 0);
        try {

            DiscountAdder disc1 = new DiscountAdder(clickedIndexID);   //open discount panel to update discount data
            disc1.setVisible(true);

        } catch (Exception e) {
            System.out.println("AAAAAAAAAAA   " + e);
        }
    }

    /**
     * load all data in discount table in database
     */
    private void renderDiscountTable() {
        try {
            discountData.renderDiscountTable(discountTable);    //load discount data to the 

        } catch (Exception e) {
            System.out.println("Discount Table Render " + e);
        }
    }
    
    
    private void stopCellEditing() {
        if (employeeTable.isEditing()) {
            employeeTable.getCellEditor().stopCellEditing();   //stop cell editing if it is in the cell editing mode
        }
        if (discountTable.isEditing()) {
            discountTable.getCellEditor().stopCellEditing();   //stop cell editing if it is in the cell editing mode
        }
        if (vehicalOwnerAddingtable.isEditing()) {
            vehicalOwnerAddingtable.getCellEditor().stopCellEditing();   //stop cell editing if it is in the cell editing mode
        }
        if (CarTable.isEditing()) {
            CarTable.getCellEditor().stopCellEditing();   //stop cell editing if it is in the cell editing mode
        }
        
    }


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        SidepanelAdminboard = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        DashboardPanel = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        customerStat = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        vehicalStat = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        employeeStat = new javax.swing.JLabel();
        barchartPanel = new javax.swing.JPanel();
        employeeDetailPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        employeeTable = new javax.swing.JTable();
        employeeSearchTextGetter = new javax.swing.JTextField();
        employeeSearchBtn = new javax.swing.JButton();
        employeeTypeSelector = new javax.swing.JComboBox<>();
        selectQueryEmp = new javax.swing.JComboBox<>();
        CarAddingPanel = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        CarTable = new javax.swing.JTable();
        jLabel13 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        CarSearchBar = new javax.swing.JTextField();
        searchCarIcon = new javax.swing.JButton();
        discountPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        discountTable = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        vehicalOwnerPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        vehicalOwnerAddingtable = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        vehicalOwnerAddingbtn = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        employeeDetailsbtn = new javax.swing.JButton();
        discountbtn = new javax.swing.JButton();
        carDetailsbtn = new javax.swing.JButton();
        dashboardbtn = new javax.swing.JButton();
        VehicalOweneState = new javax.swing.JButton();
        logout = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
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

        DashboardPanel.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Dashboard");

        jPanel2.setBackground(new java.awt.Color(20, 90, 45));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Total Number Of Customers");

        jPanel3.setBackground(new java.awt.Color(28, 128, 64));

        customerStat.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        customerStat.setForeground(new java.awt.Color(255, 255, 255));
        customerStat.setText("50");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addComponent(customerStat)
                .addGap(21, 21, 21))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addComponent(customerStat, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        jLabel9.setText("No of Vehicals");

        jPanel5.setBackground(new java.awt.Color(28, 128, 64));

        vehicalStat.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        vehicalStat.setForeground(new java.awt.Color(255, 255, 255));
        vehicalStat.setText("50");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addComponent(vehicalStat)
                .addGap(19, 19, 19))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addComponent(vehicalStat, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        jLabel11.setText("Total Number of Employees");

        jPanel7.setBackground(new java.awt.Color(28, 128, 64));

        employeeStat.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        employeeStat.setForeground(new java.awt.Color(255, 255, 255));
        employeeStat.setText("50");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(25, Short.MAX_VALUE)
                .addComponent(employeeStat)
                .addGap(20, 20, 20))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addComponent(employeeStat, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
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

        barchartPanel.setBackground(new java.awt.Color(255, 255, 255));
        barchartPanel.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout DashboardPanelLayout = new javax.swing.GroupLayout(DashboardPanel);
        DashboardPanel.setLayout(DashboardPanelLayout);
        DashboardPanelLayout.setHorizontalGroup(
            DashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DashboardPanelLayout.createSequentialGroup()
                .addContainerGap(554, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addContainerGap(554, Short.MAX_VALUE))
            .addGroup(DashboardPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(DashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DashboardPanelLayout.createSequentialGroup()
                    .addContainerGap(44, Short.MAX_VALUE)
                    .addComponent(barchartPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 1127, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(59, Short.MAX_VALUE)))
        );
        DashboardPanelLayout.setVerticalGroup(
            DashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DashboardPanelLayout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addComponent(jLabel6)
                .addGap(39, 39, 39)
                .addGroup(DashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(422, Short.MAX_VALUE))
            .addGroup(DashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DashboardPanelLayout.createSequentialGroup()
                    .addContainerGap(296, Short.MAX_VALUE)
                    .addComponent(barchartPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(19, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("tab6", DashboardPanel);

        employeeDetailPanel.setBackground(new java.awt.Color(241, 241, 241));

        employeeTable.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        employeeTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "First Name", "Last Name", "Role", "Email", "NIC", "City", "Phone No 1", "Phone No 2", "Settings"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        employeeTable.setRowHeight(35);
        employeeTable.setSelectionBackground(new java.awt.Color(28, 78, 128));
        employeeTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                employeeTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(employeeTable);
        if (employeeTable.getColumnModel().getColumnCount() > 0) {
            employeeTable.getColumnModel().getColumn(0).setResizable(false);
            employeeTable.getColumnModel().getColumn(0).setPreferredWidth(15);
            employeeTable.getColumnModel().getColumn(4).setResizable(false);
            employeeTable.getColumnModel().getColumn(4).setPreferredWidth(100);
            employeeTable.getColumnModel().getColumn(5).setResizable(false);
            employeeTable.getColumnModel().getColumn(7).setResizable(false);
            employeeTable.getColumnModel().getColumn(7).setPreferredWidth(50);
            employeeTable.getColumnModel().getColumn(8).setResizable(false);
            employeeTable.getColumnModel().getColumn(8).setPreferredWidth(50);
        }

        employeeSearchTextGetter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employeeSearchTextGetterActionPerformed(evt);
            }
        });
        employeeSearchTextGetter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                employeeSearchTextGetterKeyTyped(evt);
            }
        });

        employeeSearchBtn.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        employeeSearchBtn.setText("Search");
        employeeSearchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employeeSearchBtnActionPerformed(evt);
            }
        });

        employeeTypeSelector.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        employeeTypeSelector.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Add Admin", "Add Cashier", "Add Driver" }));
        employeeTypeSelector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employeeTypeSelectorActionPerformed(evt);
            }
        });

        selectQueryEmp.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        selectQueryEmp.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Show All Employees", "Show All Admins", "Show All Cashiers", "Show All Drivers" }));
        selectQueryEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectQueryEmpActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout employeeDetailPanelLayout = new javax.swing.GroupLayout(employeeDetailPanel);
        employeeDetailPanel.setLayout(employeeDetailPanelLayout);
        employeeDetailPanelLayout.setHorizontalGroup(
            employeeDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(employeeDetailPanelLayout.createSequentialGroup()
                .addContainerGap(65, Short.MAX_VALUE)
                .addGroup(employeeDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(employeeDetailPanelLayout.createSequentialGroup()
                        .addComponent(employeeTypeSelector, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(employeeSearchTextGetter, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(employeeSearchBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(selectQueryEmp, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(65, Short.MAX_VALUE))
        );
        employeeDetailPanelLayout.setVerticalGroup(
            employeeDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(employeeDetailPanelLayout.createSequentialGroup()
                .addContainerGap(51, Short.MAX_VALUE)
                .addGroup(employeeDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(employeeSearchBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(employeeSearchTextGetter, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(employeeTypeSelector, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(selectQueryEmp, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 512, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58))
        );

        jTabbedPane1.addTab("vehicalowner", employeeDetailPanel);

        CarAddingPanel.setBackground(new java.awt.Color(241, 241, 241));

        CarTable.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        CarTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Car Number", "Car Model", "Car Type", "Seat No", "AC Type", "Fuel Type", "Image", "Price Per Day", "Settings"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        CarTable.setRowHeight(30);
        CarTable.setSelectionBackground(new java.awt.Color(28, 78, 128));
        jScrollPane6.setViewportView(CarTable);

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 0, 0));
        jLabel13.setText("Car Panel");

        jButton3.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jButton3.setText("Add Car");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        searchCarIcon.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        searchCarIcon.setText("Search");
        searchCarIcon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchCarIconActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout CarAddingPanelLayout = new javax.swing.GroupLayout(CarAddingPanel);
        CarAddingPanel.setLayout(CarAddingPanelLayout);
        CarAddingPanelLayout.setHorizontalGroup(
            CarAddingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CarAddingPanelLayout.createSequentialGroup()
                .addContainerGap(65, Short.MAX_VALUE)
                .addGroup(CarAddingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CarAddingPanelLayout.createSequentialGroup()
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(504, 504, 504)
                        .addComponent(CarSearchBar, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(searchCarIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 1100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(65, Short.MAX_VALUE))
            .addGroup(CarAddingPanelLayout.createSequentialGroup()
                .addGap(545, 545, 545)
                .addComponent(jLabel13)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        CarAddingPanelLayout.setVerticalGroup(
            CarAddingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CarAddingPanelLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CarAddingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchCarIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CarSearchBar, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 512, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );

        jTabbedPane1.addTab("discount", CarAddingPanel);

        discountPanel.setBackground(new java.awt.Color(241, 241, 241));

        discountTable.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        discountTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Discount ID", "Discount Name", "Precentage", "Description", "Settings"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        discountTable.setRowHeight(30);
        discountTable.setSelectionBackground(new java.awt.Color(28, 78, 128));
        jScrollPane2.setViewportView(discountTable);
        if (discountTable.getColumnModel().getColumnCount() > 0) {
            discountTable.getColumnModel().getColumn(3).setResizable(false);
            discountTable.getColumnModel().getColumn(3).setPreferredWidth(200);
        }

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Discount Panel");

        jButton1.setText("Add Discount");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout discountPanelLayout = new javax.swing.GroupLayout(discountPanel);
        discountPanel.setLayout(discountPanelLayout);
        discountPanelLayout.setHorizontalGroup(
            discountPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(discountPanelLayout.createSequentialGroup()
                .addContainerGap(65, Short.MAX_VALUE)
                .addGroup(discountPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(discountPanelLayout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(252, 252, 252)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(65, Short.MAX_VALUE))
        );
        discountPanelLayout.setVerticalGroup(
            discountPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(discountPanelLayout.createSequentialGroup()
                .addGroup(discountPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(discountPanelLayout.createSequentialGroup()
                        .addContainerGap(61, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28))
                    .addGroup(discountPanelLayout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 512, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );

        jTabbedPane1.addTab("discount", discountPanel);

        vehicalOwnerPanel.setBackground(new java.awt.Color(241, 241, 241));

        vehicalOwnerAddingtable.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        vehicalOwnerAddingtable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "VehicalOwnerID", "FirstName", "LastName", "Email", "City", "NIC  No", "Phone No", "Settings"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        vehicalOwnerAddingtable.setRowHeight(30);
        vehicalOwnerAddingtable.setSelectionBackground(new java.awt.Color(28, 78, 128));
        jScrollPane3.setViewportView(vehicalOwnerAddingtable);
        if (vehicalOwnerAddingtable.getColumnModel().getColumnCount() > 0) {
            vehicalOwnerAddingtable.getColumnModel().getColumn(3).setResizable(false);
            vehicalOwnerAddingtable.getColumnModel().getColumn(3).setPreferredWidth(200);
        }

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Vehical Owner Panel");

        vehicalOwnerAddingbtn.setText("Add Owner");
        vehicalOwnerAddingbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vehicalOwnerAddingbtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout vehicalOwnerPanelLayout = new javax.swing.GroupLayout(vehicalOwnerPanel);
        vehicalOwnerPanel.setLayout(vehicalOwnerPanelLayout);
        vehicalOwnerPanelLayout.setHorizontalGroup(
            vehicalOwnerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(vehicalOwnerPanelLayout.createSequentialGroup()
                .addContainerGap(65, Short.MAX_VALUE)
                .addGroup(vehicalOwnerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(vehicalOwnerPanelLayout.createSequentialGroup()
                        .addComponent(vehicalOwnerAddingbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(213, 213, 213)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(65, Short.MAX_VALUE))
        );
        vehicalOwnerPanelLayout.setVerticalGroup(
            vehicalOwnerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(vehicalOwnerPanelLayout.createSequentialGroup()
                .addGroup(vehicalOwnerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(vehicalOwnerPanelLayout.createSequentialGroup()
                        .addContainerGap(61, Short.MAX_VALUE)
                        .addComponent(vehicalOwnerAddingbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28))
                    .addGroup(vehicalOwnerPanelLayout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 512, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );

        jTabbedPane1.addTab("discount", vehicalOwnerPanel);

        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 0, 1230, 710));

        jPanel1.setBackground(new java.awt.Color(28, 78, 128));

        employeeDetailsbtn.setBackground(new java.awt.Color(28, 78, 128));
        employeeDetailsbtn.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        employeeDetailsbtn.setForeground(new java.awt.Color(255, 255, 255));
        employeeDetailsbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/cars3.png"))); // NOI18N
        employeeDetailsbtn.setText("Employee");
        employeeDetailsbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employeeDetailsbtnActionPerformed(evt);
            }
        });

        discountbtn.setBackground(new java.awt.Color(28, 78, 128));
        discountbtn.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        discountbtn.setForeground(new java.awt.Color(255, 255, 255));
        discountbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/cars3.png"))); // NOI18N
        discountbtn.setText("Discount");
        discountbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                discountbtnActionPerformed(evt);
            }
        });

        carDetailsbtn.setBackground(new java.awt.Color(28, 78, 128));
        carDetailsbtn.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        carDetailsbtn.setForeground(new java.awt.Color(255, 255, 255));
        carDetailsbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/cars3.png"))); // NOI18N
        carDetailsbtn.setText("Cars");
        carDetailsbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carDetailsbtnActionPerformed(evt);
            }
        });

        dashboardbtn.setBackground(new java.awt.Color(28, 78, 128));
        dashboardbtn.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        dashboardbtn.setForeground(new java.awt.Color(255, 255, 255));
        dashboardbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/cars3.png"))); // NOI18N
        dashboardbtn.setText("Dashboard");
        dashboardbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dashboardbtnActionPerformed(evt);
            }
        });

        VehicalOweneState.setBackground(new java.awt.Color(28, 78, 128));
        VehicalOweneState.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        VehicalOweneState.setForeground(new java.awt.Color(255, 255, 255));
        VehicalOweneState.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/cars3.png"))); // NOI18N
        VehicalOweneState.setText("Vehical Owner");
        VehicalOweneState.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                VehicalOweneStateActionPerformed(evt);
            }
        });

        logout.setBackground(new java.awt.Color(28, 78, 128));
        logout.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        logout.setForeground(new java.awt.Color(255, 255, 255));
        logout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/cars3.png"))); // NOI18N
        logout.setText("Log Out");
        logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(logout, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(VehicalOweneState, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dashboardbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(discountbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(carDetailsbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(employeeDetailsbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(54, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addComponent(dashboardbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(employeeDetailsbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(carDetailsbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(discountbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(VehicalOweneState, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 255, Short.MAX_VALUE)
                .addComponent(logout, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 270, -1));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
/**
     * employee button to navigate to employee table panel
     *
     * @param evt
     */
    private void employeeDetailsbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employeeDetailsbtnActionPerformed
        // TODO add your handling code here:
        stopCellEditing();
        currentPageHolder = "employeePage";
//        loadOwnerData("all");
        employee1.loadEmployeeData("all", null, employeeTable, null);
        jTabbedPane1.setSelectedIndex(1);
        

    }//GEN-LAST:event_employeeDetailsbtnActionPerformed

    private void discountbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_discountbtnActionPerformed
        stopCellEditing();
        jTabbedPane1.setSelectedIndex(3);
        renderDiscountTable();
        currentPageHolder = "discountPage";
        
        // TODO add your handling code here:
    }//GEN-LAST:event_discountbtnActionPerformed

    private void carDetailsbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carDetailsbtnActionPerformed
        // TODO add your handling code here:
        stopCellEditing();
        currentPageHolder = "CarPage";
        car1.renderCarTable(CarTable);

        jTabbedPane1.setSelectedIndex(2);
        
    }//GEN-LAST:event_carDetailsbtnActionPerformed

    private void dashboardbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dashboardbtnActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(0);
        showStatisticalData();
    }//GEN-LAST:event_dashboardbtnActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        CarAddingPanel carUpdate1 = new CarAddingPanel();  //open car adding panel to insert new car
        carUpdate1.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void employeeTypeSelectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employeeTypeSelectorActionPerformed
        // TODO add your handling code here:
        System.out.println(employeeTypeSelector.getSelectedIndex());
        if (employeeTypeSelector.getSelectedIndex() == 3) {
            System.out.println("Vowner Selected");
            CustomerRegistrationForm c1 = new CustomerRegistrationForm(null, "employee", "VOwner");
            c1.SetRegistrationFrameTitle("Car Owner Registration", "Add Owner");

            c1.setVisible(true);
        } else if (employeeTypeSelector.getSelectedIndex() == 2) {

            DriverRegistrationForm d1 = new DriverRegistrationForm();
            d1.setVisible(true);
        } else if (employeeTypeSelector.getSelectedIndex() == 1) {
            CustomerRegistrationForm c1 = new CustomerRegistrationForm(null, "employee", "Cashier");
            c1.SetRegistrationFrameTitle("Cashier Registration", "Add Cashier");
            c1.setVisible(true);
        } else if (employeeTypeSelector.getSelectedIndex() == 0) {
            CustomerRegistrationForm c1 = new CustomerRegistrationForm(null, "employee", "Admin");
            c1.SetRegistrationFrameTitle("Admin Registration", "Add Admin");
            c1.setVisible(true);
        }
    }//GEN-LAST:event_employeeTypeSelectorActionPerformed

    
    
    /**
     * load all data corresponding to the selected employee type
     * @param evt 
     */
    private void selectQueryEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectQueryEmpActionPerformed
        // TODO add your handling code here:
        
        if (employeeTable.isEditing()) {
            employeeTable.getCellEditor().stopCellEditing();   //stop cell editing if it is in the cell editing mode
        }

        if (selectQueryEmp.getSelectedIndex() == 0) {
            employee1.loadEmployeeData("all", null, employeeTable, null);  
        } else if (selectQueryEmp.getSelectedIndex() == 1) {

            employee1.loadEmployeeData("all", null, employeeTable, "Admin");   //load admin data
        } else if (selectQueryEmp.getSelectedIndex() == 2) {

            employee1.loadEmployeeData("all", null, employeeTable, "Cashier");   //load cashier data
        } else if (selectQueryEmp.getSelectedIndex() == 3) {

            employee1.loadEmployeeData("all", null, employeeTable, "Driver");   //load driver data
        } else if (selectQueryEmp.getSelectedIndex() == 4) {

            employee1.loadEmployeeData("all", null, employeeTable, "VOwner");   //load vehical owner data
        }
    }//GEN-LAST:event_selectQueryEmpActionPerformed

    private void employeeTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_employeeTableMouseClicked
        // TODO add your handling code here:


    }//GEN-LAST:event_employeeTableMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        DiscountAdder di = new DiscountAdder();
        di.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void employeeSearchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employeeSearchBtnActionPerformed

        employee1.searchEmployee(employeeSearchTextGetter.getText(), employeeTable);
    }//GEN-LAST:event_employeeSearchBtnActionPerformed

    private void employeeSearchTextGetterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employeeSearchTextGetterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_employeeSearchTextGetterActionPerformed

    private void employeeSearchTextGetterKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_employeeSearchTextGetterKeyTyped

        employee1.searchEmployee(employeeSearchTextGetter.getText(), employeeTable);
    }//GEN-LAST:event_employeeSearchTextGetterKeyTyped

    private void VehicalOweneStateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_VehicalOweneStateActionPerformed
        stopCellEditing();
        jTabbedPane1.setSelectedIndex(4);
        currentPageHolder = "vehicalOwnerPage";
        owner.loadOwnerData(vehicalOwnerAddingtable);

    }//GEN-LAST:event_VehicalOweneStateActionPerformed

    private void vehicalOwnerAddingbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vehicalOwnerAddingbtnActionPerformed

        VOwnerRegistrationFrom owner1 = new VOwnerRegistrationFrom();   //open the vehical owner details adding form 
        owner1.setVisible(true);
    }//GEN-LAST:event_vehicalOwnerAddingbtnActionPerformed

    private void logoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutActionPerformed

        CarRentalSystem.closeWindows(this);
        CustomSearch1 search = new CustomSearch1();   //open the car search customer page
        search.setVisible(true);
    }//GEN-LAST:event_logoutActionPerformed

    private void searchCarIconActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchCarIconActionPerformed
        // TODO add your handling code here:
        if (CarTable.isEditing()) {
            CarTable.getCellEditor().stopCellEditing();   //stop cell editing if it is in the cell editing mode
        }
        car1.searchCarNew(CarSearchBar.getText(), CarTable);
        
    }//GEN-LAST:event_searchCarIconActionPerformed

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
            java.util.logging.Logger.getLogger(AdminPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdminPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdminPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AdminPage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel CarAddingPanel;
    private javax.swing.JTextField CarSearchBar;
    private javax.swing.JTable CarTable;
    private javax.swing.JPanel DashboardPanel;
    private javax.swing.JPanel SidepanelAdminboard;
    private javax.swing.JButton VehicalOweneState;
    private javax.swing.JPanel barchartPanel;
    private javax.swing.JButton carDetailsbtn;
    private javax.swing.JLabel customerStat;
    private javax.swing.JButton dashboardbtn;
    private javax.swing.JPanel discountPanel;
    private javax.swing.JTable discountTable;
    private javax.swing.JButton discountbtn;
    private javax.swing.JPanel employeeDetailPanel;
    private javax.swing.JButton employeeDetailsbtn;
    private javax.swing.JButton employeeSearchBtn;
    private javax.swing.JTextField employeeSearchTextGetter;
    private javax.swing.JLabel employeeStat;
    private javax.swing.JTable employeeTable;
    private javax.swing.JComboBox<String> employeeTypeSelector;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton logout;
    private javax.swing.JButton searchCarIcon;
    private javax.swing.JComboBox<String> selectQueryEmp;
    private javax.swing.JButton vehicalOwnerAddingbtn;
    private javax.swing.JTable vehicalOwnerAddingtable;
    private javax.swing.JPanel vehicalOwnerPanel;
    private javax.swing.JLabel vehicalStat;
    // End of variables declaration//GEN-END:variables
}
