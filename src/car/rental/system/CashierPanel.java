/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package car.rental.system;

import javax.swing.table.DefaultTableModel;
import raven.cell.TableActionCellEditor;
import raven.cell.TableActionCellRender;
import raven.cell.TableActionEvent;

/**
 *
 * @author Akila
 */
public class CashierPanel extends javax.swing.JFrame {

    /**
     * Creates new form AdminPage
     */
    public CashierPanel() {
        initComponents();
        TableActionEvent event = new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                System.out.println("Edit row : " + row);
            }

            @Override
            public void onDelete(int row) {
                if (vehicalOwnerTable.isEditing()) {
                    vehicalOwnerTable.getCellEditor().stopCellEditing();
                }
                DefaultTableModel model = (DefaultTableModel) vehicalOwnerTable.getModel();
                model.removeRow(row);
            }

        };
        vehicalOwnerTable.getColumnModel().getColumn(3).setCellRenderer(new TableActionCellRender());
        vehicalOwnerTable.getColumnModel().getColumn(3).setCellEditor(new TableActionCellEditor(event));
        
        AdminTable.getColumnModel().getColumn(3).setCellRenderer(new TableActionCellRender());
        AdminTable.getColumnModel().getColumn(3).setCellEditor(new TableActionCellEditor(event));
        
       cashierTable.getColumnModel().getColumn(3).setCellRenderer(new TableActionCellRender());
       cashierTable.getColumnModel().getColumn(3).setCellEditor(new TableActionCellEditor(event));
        
        discountTable.getColumnModel().getColumn(3).setCellRenderer(new TableActionCellRender());
        discountTable.getColumnModel().getColumn(3).setCellEditor(new TableActionCellEditor(event));
        
        DriverTable.getColumnModel().getColumn(3).setCellRenderer(new TableActionCellRender());
        DriverTable.getColumnModel().getColumn(3).setCellEditor(new TableActionCellEditor(event));
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
        AdminPanel = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        AdminTable = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        DriverPanel = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        DriverTable = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        cashierPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        cashierTable = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        discountPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        discountTable = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        vehicalOwnerpanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        vehicalOwnerTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        CashierDashboard = new javax.swing.JButton();
        PendingReservations = new javax.swing.JButton();
        cashierbtn = new javax.swing.JButton();
        discountbtn = new javax.swing.JButton();
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

        AdminPanel.setBackground(new java.awt.Color(241, 241, 241));

        AdminTable.setModel(new javax.swing.table.DefaultTableModel(
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
        AdminTable.setRowHeight(30);
        AdminTable.setSelectionBackground(new java.awt.Color(153, 255, 153));
        jScrollPane5.setViewportView(AdminTable);

        jLabel5.setForeground(new java.awt.Color(0, 204, 204));
        jLabel5.setText("Cashier Panel");

        javax.swing.GroupLayout AdminPanelLayout = new javax.swing.GroupLayout(AdminPanel);
        AdminPanel.setLayout(AdminPanelLayout);
        AdminPanelLayout.setHorizontalGroup(
            AdminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, AdminPanelLayout.createSequentialGroup()
                .addContainerGap(51, Short.MAX_VALUE)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 1100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(59, Short.MAX_VALUE))
            .addGroup(AdminPanelLayout.createSequentialGroup()
                .addGap(253, 253, 253)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        AdminPanelLayout.setVerticalGroup(
            AdminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AdminPanelLayout.createSequentialGroup()
                .addContainerGap(73, Short.MAX_VALUE)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 512, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );

        jTabbedPane1.addTab("admin", AdminPanel);

        DriverPanel.setBackground(new java.awt.Color(241, 241, 241));

        jButton1.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jButton1.setText("Add Customer");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        DriverTable.setModel(new javax.swing.table.DefaultTableModel(
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
        DriverTable.setRowHeight(30);
        DriverTable.setSelectionBackground(new java.awt.Color(153, 255, 153));
        jScrollPane4.setViewportView(DriverTable);

        jLabel4.setForeground(new java.awt.Color(0, 204, 204));
        jLabel4.setText("Customer panel");

        jButton2.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jButton2.setText("Search");

        javax.swing.GroupLayout DriverPanelLayout = new javax.swing.GroupLayout(DriverPanel);
        DriverPanel.setLayout(DriverPanelLayout);
        DriverPanelLayout.setHorizontalGroup(
            DriverPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DriverPanelLayout.createSequentialGroup()
                .addContainerGap(55, Short.MAX_VALUE)
                .addGroup(DriverPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(DriverPanelLayout.createSequentialGroup()
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
        DriverPanelLayout.setVerticalGroup(
            DriverPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DriverPanelLayout.createSequentialGroup()
                .addContainerGap(60, Short.MAX_VALUE)
                .addGroup(DriverPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 512, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46))
        );

        jTabbedPane1.addTab("Driver", DriverPanel);

        cashierPanel.setBackground(new java.awt.Color(241, 241, 241));

        cashierTable.setModel(new javax.swing.table.DefaultTableModel(
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
        cashierTable.setRowHeight(30);
        cashierTable.setSelectionBackground(new java.awt.Color(153, 255, 153));
        jScrollPane3.setViewportView(cashierTable);

        jLabel3.setForeground(new java.awt.Color(0, 204, 204));
        jLabel3.setText("Cashier panel");

        javax.swing.GroupLayout cashierPanelLayout = new javax.swing.GroupLayout(cashierPanel);
        cashierPanel.setLayout(cashierPanelLayout);
        cashierPanelLayout.setHorizontalGroup(
            cashierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cashierPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(cashierPanelLayout.createSequentialGroup()
                .addGap(253, 253, 253)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        cashierPanelLayout.setVerticalGroup(
            cashierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cashierPanelLayout.createSequentialGroup()
                .addContainerGap(73, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 512, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );

        jTabbedPane1.addTab("cashier", cashierPanel);

        discountPanel.setBackground(new java.awt.Color(241, 241, 241));

        discountTable.setModel(new javax.swing.table.DefaultTableModel(
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
        discountTable.setRowHeight(30);
        discountTable.setSelectionBackground(new java.awt.Color(153, 255, 153));
        jScrollPane2.setViewportView(discountTable);

        jLabel2.setForeground(new java.awt.Color(0, 204, 204));
        jLabel2.setText("Discount panel");

        javax.swing.GroupLayout discountPanelLayout = new javax.swing.GroupLayout(discountPanel);
        discountPanel.setLayout(discountPanelLayout);
        discountPanelLayout.setHorizontalGroup(
            discountPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, discountPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(discountPanelLayout.createSequentialGroup()
                .addGap(253, 253, 253)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        discountPanelLayout.setVerticalGroup(
            discountPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(discountPanelLayout.createSequentialGroup()
                .addContainerGap(73, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 512, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );

        jTabbedPane1.addTab("discount", discountPanel);

        vehicalOwnerpanel.setBackground(new java.awt.Color(241, 241, 241));

        vehicalOwnerTable.setModel(new javax.swing.table.DefaultTableModel(
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
        vehicalOwnerTable.setRowHeight(30);
        vehicalOwnerTable.setSelectionBackground(new java.awt.Color(153, 255, 153));
        jScrollPane1.setViewportView(vehicalOwnerTable);

        jLabel1.setForeground(new java.awt.Color(0, 204, 204));
        jLabel1.setText("Vehical owner panel");

        javax.swing.GroupLayout vehicalOwnerpanelLayout = new javax.swing.GroupLayout(vehicalOwnerpanel);
        vehicalOwnerpanel.setLayout(vehicalOwnerpanelLayout);
        vehicalOwnerpanelLayout.setHorizontalGroup(
            vehicalOwnerpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(vehicalOwnerpanelLayout.createSequentialGroup()
                .addGap(253, 253, 253)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, vehicalOwnerpanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        vehicalOwnerpanelLayout.setVerticalGroup(
            vehicalOwnerpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(vehicalOwnerpanelLayout.createSequentialGroup()
                .addContainerGap(73, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 512, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48))
        );

        jTabbedPane1.addTab("vehicalowner", vehicalOwnerpanel);

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

        PendingReservations.setBackground(new java.awt.Color(28, 78, 128));
        PendingReservations.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        PendingReservations.setForeground(new java.awt.Color(255, 255, 255));
        PendingReservations.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/cars3.png"))); // NOI18N
        PendingReservations.setText("Add Customer");
        PendingReservations.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PendingReservationsActionPerformed(evt);
            }
        });

        cashierbtn.setBackground(new java.awt.Color(28, 78, 128));
        cashierbtn.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        cashierbtn.setForeground(new java.awt.Color(255, 255, 255));
        cashierbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/cars3.png"))); // NOI18N
        cashierbtn.setText("Proceeded Reservations");
        cashierbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cashierbtnActionPerformed(evt);
            }
        });

        discountbtn.setBackground(new java.awt.Color(28, 78, 128));
        discountbtn.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        discountbtn.setForeground(new java.awt.Color(255, 255, 255));
        discountbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/cars3.png"))); // NOI18N
        discountbtn.setText("Pending Reservations");
        discountbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                discountbtnActionPerformed(evt);
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
                    .addComponent(PendingReservations, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cashierbtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(CashierDashboard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(discountbtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addComponent(CashierDashboard, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(PendingReservations)
                .addGap(36, 36, 36)
                .addComponent(discountbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(cashierbtn)
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
        jTabbedPane1.setSelectedIndex(0);
        
    }//GEN-LAST:event_CashierDashboardActionPerformed

    private void PendingReservationsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PendingReservationsActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_PendingReservationsActionPerformed

    private void cashierbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cashierbtnActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(2);
    }//GEN-LAST:event_cashierbtnActionPerformed

    private void discountbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_discountbtnActionPerformed
        jTabbedPane1.setSelectedIndex(3);
        // TODO add your handling code here:
    }//GEN-LAST:event_discountbtnActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        CustomerRegistrationForm customer1=new CustomerRegistrationForm();
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
    private javax.swing.JPanel AdminPanel;
    private javax.swing.JTable AdminTable;
    private javax.swing.JButton CashierDashboard;
    private javax.swing.JPanel DriverPanel;
    private javax.swing.JTable DriverTable;
    private javax.swing.JButton PendingReservations;
    private javax.swing.JPanel SidepanelAdminboard;
    private javax.swing.JPanel cashierPanel;
    private javax.swing.JTable cashierTable;
    private javax.swing.JButton cashierbtn;
    private javax.swing.JPanel discountPanel;
    private javax.swing.JTable discountTable;
    private javax.swing.JButton discountbtn;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTable vehicalOwnerTable;
    private javax.swing.JPanel vehicalOwnerpanel;
    // End of variables declaration//GEN-END:variables
}
