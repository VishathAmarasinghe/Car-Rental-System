/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package car.rental.system;

import car.rental.system.EventHandleID.EventHandle;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import com.raven.datechooser.EventDateChooser;
import com.raven.datechooser.SelectedAction;
import com.raven.datechooser.SelectedDate;
import javax.swing.JOptionPane;
/**
 *
 * @author Akila
 */
public class CustomSearch1 extends javax.swing.JFrame {

    /**
     * Creates new form CustomSearch1
     */
    
    
    private EventHandle event;
    private ResultSet rs;
    private Connection con;
    private PreparedStatement ps;
    String selectedCarNumer="";

    public EventHandle getEvent() {
        return event;
    }

    public void setEvent(EventHandle event) {
        this.event = event;
    }
    public CustomSearch1() {
        initComponents();
        startDateChooser.setTextRefernce(startDate);
        endDateChooser.setTextRefernce(endDate);
        startDateChooser.addEventDateChooser(new EventDateChooser() {
            @Override
            public void dateSelected(SelectedAction action, SelectedDate date) {
                System.out.println(date.getDay() + "-" + date.getMonth() + "-" + date.getYear());
                if (action.getAction() == SelectedAction.DAY_SELECTED) {
                    startDateChooser.hidePopup();
                }
            }
        });
        endDateChooser.addEventDateChooser(new EventDateChooser() {
            @Override
            public void dateSelected(SelectedAction action, SelectedDate date) {
                System.out.println(date.getDay() + "-" + date.getMonth() + "-" + date.getYear());
                if (action.getAction() == SelectedAction.DAY_SELECTED) {
                    endDateChooser.hidePopup();
                }
            }
        });
    }
    
    public void addItem(ItemModel data){
        Card card1=new Card();
        card1.setData(data);
        card1.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    System.out.println(data.car_keyValue);
                    RequestCardEditor(data);
                    changeStateOfJTabbedPane();
                }
            }
        });
        cardContainer.add(card1);
        cardContainer.repaint();
        cardContainer.revalidate();
    }
    
    public ResultSet databaseExecuation(String s){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/carrentalsystem", "root", "akila123");
            ps = con.prepareStatement(s);
            rs=ps.executeQuery();
            
        } catch (Exception e) {
            System.out.println("DB Error : "+e);
        }
        return rs;
    }
    
    
    public void RequestCardEditor(ItemModel data){
        try{
            String query="Select * from cars where CarNumber=\""+data.car_keyValue+"\"";
            selectedCarNumer=data.car_keyValue;
            ResultSet result =databaseExecuation(query);
            result.next();
            String foundType=result.getString(1);
            System.out.println("found Type "+foundType);
        
            byte[] img=result.getBytes("CarImage");
            ImageIcon image=new ImageIcon(img);
            Image im=image.getImage();
            Image myimage=im.getScaledInstance(609, 361, Image.SCALE_SMOOTH);
            ImageIcon Scaledimage=new ImageIcon(myimage);
            CarImageRequestCard.setIcon(Scaledimage);
            CarName.setText(data.car_model+" "+data.car_type);
            SeatCount2.setText(data.seat_no);
            AcTypeRequestCard.setText(data.ac_Type);
            FuelTypeRequestCard.setText(data.fuel_type);
            
            individucalComPickupDate.setText(startDate.getText());
            individuvalComDropOffDate.setText(endDate.getText());
            
            withOutDriverRadio.setSelected(true);
            
        }catch (Exception e){
            System.out.println("DB Error req card : "+e);
        }
    }
    
    
    public void test(){
        
        
        cardContainer.removeAll();
        cardContainer.revalidate();
        cardContainer.repaint();
                
              System.out.println("added");
        try {
            
            String pickupDate=dateChanger(startDate.getText());
            String droupOutDate=dateChanger(endDate.getText());
            
            
            System.out.println("start Date "+pickupDate);
            System.out.println("end date "+droupOutDate);
            System.out.println("type vehical "+vehicalTypeSearchBox.getSelectedItem().toString());
            
            
            
            cardContainer.setLayout(new  WrapLayout());
            String s=   "select * from cars where carNumber not in (select VehicalNumber from reservation where pickedUpdate between \""+pickupDate+"\" "
                    + "and \""+droupOutDate+"\" and dropOffdate between \""+pickupDate+"\"and \""+droupOutDate+"\") and vehicalType= \""+vehicalTypeSearchBox.getSelectedItem().toString()+"\"";
            ResultSet rs=databaseExecuation(s);
            while(rs.next()) {    
//                Card c3=new Card();
                
                byte[] img=rs.getBytes("CarImage");

                
                String car_keyValue=rs.getString("CarNumber");
                String car_type=rs.getString("CarType");
                String car_model=rs.getString("CarModel");
                String seat_no=rs.getString("SeatNo");
                String ac_Type=rs.getString("ACType");
                String fuel_type=rs.getString("FuelType");
                System.out.println(car_keyValue);
                System.out.println("car type "+car_type);
                System.out.println("car model "+car_model);
                System.out.println("seat "+seat_no);
                System.out.println("ac type  "+ac_Type);
                System.out.println("fuel type "+fuel_type);
            
                
                ImageIcon image=new ImageIcon(img);
                Image im=image.getImage();
                Image myimage=im.getScaledInstance(236, 172, Image.SCALE_SMOOTH);
                ImageIcon newimage=new ImageIcon(myimage);
                
                addItem(new ItemModel(car_keyValue,car_type,car_model,seat_no,ac_Type,fuel_type,newimage));
                
            }
            
        } catch (Exception e) {
            System.out.println("text runner "+e);
        }        
    }
    
    private String dateChanger(String date){
        String day=date.substring(0,2);
        String month=date.substring(3,5);
        String year=date.substring(6);
        return year+"-"+month+"-"+day;
    }
    
    public void changeStateOfJTabbedPane(){
        jTabbedPane1.setSelectedIndex(2);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        endDateChooser = new com.raven.datechooser.DateChooser();
        startDateChooser = new com.raven.datechooser.DateChooser();
        driverbtngroup = new javax.swing.ButtonGroup();
        jPanel6 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        endDate = new javax.swing.JTextField();
        startDate = new javax.swing.JTextField();
        vehicalTypeSearchBox = new javax.swing.JComboBox<>();
        SearchBarImage = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        cardContainer = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        BackToSearchPanel = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        BackToAvailableVehicals = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        CarName = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        individuvalComDropOffDate = new javax.swing.JLabel();
        individucalComPickupDate = new javax.swing.JLabel();
        withOutDriverRadio = new javax.swing.JRadioButton();
        withDriverRadio = new javax.swing.JRadioButton();
        jButton1 = new javax.swing.JButton();
        FuelTypeRequestCard = new javax.swing.JLabel();
        SeatCount2 = new javax.swing.JLabel();
        AcTypeRequestCard = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        dummyValue = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        CarImageRequestCard = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jButton2.setBackground(new java.awt.Color(217, 241, 255));
        jButton2.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(0, 0, 0));
        jButton2.setText("LOGIN");
        jButton2.setBorder(null);
        jButton2.setBorderPainted(false);
        jButton2.setFocusPainted(false);
        jButton2.setFocusable(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(958, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        getContentPane().add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1130, 50));

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Segoe UI Variable", 1, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Create Your own travel comfort!");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 110, 540, 50));

        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Vehical Type");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 210, -1, -1));

        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setText("Start Date");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 210, -1, -1));

        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("End Date");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 210, -1, -1));

        jButton3.setText("Search");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 230, 150, 40));

        endDate.setForeground(new java.awt.Color(0, 0, 0));
        endDate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                endDateMouseClicked(evt);
            }
        });
        jPanel1.add(endDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 230, 220, 40));

        startDate.setForeground(new java.awt.Color(0, 0, 0));
        startDate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                startDateMouseClicked(evt);
            }
        });
        jPanel1.add(startDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 230, 220, 40));

        vehicalTypeSearchBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Car", "Van" }));
        jPanel1.add(vehicalTypeSearchBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 230, 130, 40));

        SearchBarImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/searchform.png"))); // NOI18N
        jPanel1.add(SearchBarImage, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jTabbedPane1.addTab("tab1", jPanel1);

        jPanel2.setBackground(new java.awt.Color(28, 78, 128));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Available Vehicals");

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("jLabel4");

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        cardContainer.setBackground(new java.awt.Color(255, 255, 255));
        cardContainer.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1031, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        cardContainer.add(jPanel5);

        jScrollPane1.setViewportView(cardContainer);

        BackToSearchPanel.setBackground(new java.awt.Color(28, 78, 128));
        BackToSearchPanel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        BackToSearchPanel.setForeground(new java.awt.Color(255, 255, 255));
        BackToSearchPanel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/backbtn.png"))); // NOI18N
        BackToSearchPanel.setText("Back");
        BackToSearchPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BackToSearchPanelMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(BackToSearchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(413, 413, 413)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(50, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1034, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                    .addContainerGap(52, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 1029, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(49, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BackToSearchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 488, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                    .addContainerGap(91, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(460, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("tab2", jPanel2);

        jPanel3.setBackground(new java.awt.Color(28, 78, 128));
        jPanel3.setPreferredSize(new java.awt.Dimension(1130, 559));

        BackToAvailableVehicals.setBackground(new java.awt.Color(28, 78, 128));
        BackToAvailableVehicals.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        BackToAvailableVehicals.setForeground(new java.awt.Color(255, 255, 255));
        BackToAvailableVehicals.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/backbtn.png"))); // NOI18N
        BackToAvailableVehicals.setText("Back");
        BackToAvailableVehicals.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BackToAvailableVehicalsMouseClicked(evt);
            }
        });

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setForeground(new java.awt.Color(0, 0, 0));

        CarName.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        CarName.setForeground(new java.awt.Color(0, 0, 0));
        CarName.setText("Toyota Premio 2017");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Pickup Date");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Drop Off Date");

        individuvalComDropOffDate.setForeground(new java.awt.Color(0, 0, 0));
        individuvalComDropOffDate.setText("25th October 2023");

        individucalComPickupDate.setForeground(new java.awt.Color(0, 0, 0));
        individucalComPickupDate.setText("25th October 2023");

        driverbtngroup.add(withOutDriverRadio);
        withOutDriverRadio.setForeground(new java.awt.Color(0, 0, 0));
        withOutDriverRadio.setText("Without Driver");

        driverbtngroup.add(withDriverRadio);
        withDriverRadio.setForeground(new java.awt.Color(0, 0, 0));
        withDriverRadio.setText("With Driver");

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 0, 0));
        jButton1.setText("Send Booking Request");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        FuelTypeRequestCard.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        FuelTypeRequestCard.setForeground(new java.awt.Color(0, 0, 0));
        FuelTypeRequestCard.setText("Petrol");

        SeatCount2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        SeatCount2.setForeground(new java.awt.Color(0, 0, 0));
        SeatCount2.setText("4");

        AcTypeRequestCard.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        AcTypeRequestCard.setForeground(new java.awt.Color(0, 0, 0));
        AcTypeRequestCard.setText("AC");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 0, 0));
        jLabel14.setText("Per Day Charge ");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 0, 0));
        jLabel15.setText("Rs: 12500");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 0, 0));
        jLabel16.setText("No of Days Suppose to Book");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 0, 0));
        jLabel17.setText("3");

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 0, 0));
        jLabel18.setText("Total Approximate Charge");

        dummyValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dummyValueActionPerformed(evt);
            }
        });

        jLabel19.setForeground(new java.awt.Color(0, 0, 0));
        jLabel19.setText("Enter Your Mobile No Here");

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(0, 0, 0));
        jLabel20.setText("Rs: 5000");

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(0, 0, 0));
        jLabel21.setText("Driver Payment");

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(0, 0, 0));
        jLabel22.setText("Rs: 20000");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18)
                            .addComponent(jLabel14)
                            .addComponent(jLabel19))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addComponent(withDriverRadio)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(withOutDriverRadio))
                                .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(dummyValue, javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                    .addGap(2, 2, 2)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel21)
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                            .addGap(2, 2, 2)
                                            .addComponent(individucalComPickupDate, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jLabel16))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addComponent(individuvalComDropOffDate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGap(0, 10, Short.MAX_VALUE)
                                        .addComponent(CarName, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGap(49, 49, 49)
                                        .addComponent(SeatCount2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(AcTypeRequestCard)
                                        .addGap(86, 86, 86)
                                        .addComponent(FuelTypeRequestCard)))
                                .addGap(18, 18, 18)))
                        .addContainerGap(45, Short.MAX_VALUE))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(CarName, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(AcTypeRequestCard)
                        .addComponent(SeatCount2))
                    .addComponent(FuelTypeRequestCard))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(individucalComPickupDate)
                    .addComponent(individuvalComDropOffDate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(withOutDriverRadio)
                    .addComponent(withDriverRadio))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jLabel16))
                .addGap(32, 32, 32)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(jLabel21))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jLabel22))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dummyValue, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
        );

        CarImageRequestCard.setBackground(new java.awt.Color(51, 255, 255));
        CarImageRequestCard.setForeground(new java.awt.Color(255, 255, 255));
        CarImageRequestCard.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(BackToAvailableVehicals, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CarImageRequestCard, javax.swing.GroupLayout.PREFERRED_SIZE, 609, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(BackToAvailableVehicals, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(53, 53, 53)
                        .addComponent(CarImageRequestCard, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(16, 16, 16))
        );

        jTabbedPane1.addTab("tab3", jPanel3);

        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1130, 590));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void dummyValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dummyValueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_dummyValueActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        
        CarRentalSystem.closeWindows(this);
        LoginForm l1=new LoginForm();
        l1.setVisible(true);

    }//GEN-LAST:event_jButton2ActionPerformed

    private void BackToSearchPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BackToSearchPanelMouseClicked
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(0);
    }//GEN-LAST:event_BackToSearchPanelMouseClicked

    private void BackToAvailableVehicalsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BackToAvailableVehicalsMouseClicked
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_BackToAvailableVehicalsMouseClicked

    private void startDateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_startDateMouseClicked
        // TODO add your handling code here:
        startDateChooser.showPopup();
    }//GEN-LAST:event_startDateMouseClicked

    private void endDateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_endDateMouseClicked
        // TODO add your handling code here:
        endDateChooser.showPopup();
    }//GEN-LAST:event_endDateMouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        test();
        jTabbedPane1.setSelectedIndex(1);
        

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        
        withDriverRadio.setActionCommand("yes");
        withOutDriverRadio.setActionCommand("no");
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con3 = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/carrentalsystem", "root", "akila123");
            String querys="insert into reservation(ReservationID,ReservationStatus,pickedUpDate,dropOffDate,VehicalNumber,DummyName,DriverStatus) values(?,?,?,?,?,?,?)";
            PreparedStatement ps3=con3.prepareStatement(querys);
            
            GenerateKeys g1=new GenerateKeys();
            
            ps3.setString(1,g1.getGeneratedNewID("reserve"));
            ps3.setString(2, "pending");
            ps3.setString(3, dateChanger(startDate.getText()));
            ps3.setString(4, dateChanger(endDate.getText()));
            ps3.setString(5, selectedCarNumer);
            ps3.setString(6, dummyValue.getText());
            ps3.setString(7, driverbtngroup.getSelection().getActionCommand());
            
            
            ps3.executeUpdate();
            JOptionPane.showMessageDialog(null, "Request Added Successfully","Data Manipulation",JOptionPane.INFORMATION_MESSAGE);
            
            ps3.close();
            con3.close();
            jTabbedPane1.setSelectedIndex(0);
        } catch (Exception e) {
            System.out.println("DB Error : "+e);
        }
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
            java.util.logging.Logger.getLogger(CustomSearch1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CustomSearch1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CustomSearch1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CustomSearch1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CustomSearch1().setVisible(true);
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel AcTypeRequestCard;
    private javax.swing.JLabel BackToAvailableVehicals;
    private javax.swing.JLabel BackToSearchPanel;
    private javax.swing.JLabel CarImageRequestCard;
    private javax.swing.JLabel CarName;
    private javax.swing.JLabel FuelTypeRequestCard;
    private javax.swing.JLabel SearchBarImage;
    private javax.swing.JLabel SeatCount2;
    private javax.swing.JPanel cardContainer;
    private javax.swing.ButtonGroup driverbtngroup;
    private javax.swing.JTextField dummyValue;
    private javax.swing.JTextField endDate;
    private com.raven.datechooser.DateChooser endDateChooser;
    private javax.swing.JLabel individucalComPickupDate;
    private javax.swing.JLabel individuvalComDropOffDate;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField startDate;
    private com.raven.datechooser.DateChooser startDateChooser;
    private javax.swing.JComboBox<String> vehicalTypeSearchBox;
    private javax.swing.JRadioButton withDriverRadio;
    private javax.swing.JRadioButton withOutDriverRadio;
    // End of variables declaration//GEN-END:variables
}
