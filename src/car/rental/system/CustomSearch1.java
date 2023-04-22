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
import java.awt.Color;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private boolean[] dataParameters={true,false};
    String selectedCarNumer="";

    public EventHandle getEvent() {
        return event;
    }

    public void setEvent(EventHandle event) {
        this.event = event;
    }
    public CustomSearch1() {
        initComponents();
        Validations datevalidate=new Validations();
        startDateChooser.setTextRefernce(startDate);
        endDateChooser.setTextRefernce(endDate);
        startDateChooser.addEventDateChooser(new EventDateChooser() {
            @Override
            public void dateSelected(SelectedAction action, SelectedDate date) {
                System.out.println(date.getDay() + "-" + date.getMonth() + "-" + date.getYear());
                startDateError.setVisible(false);
                if (action.getAction() == SelectedAction.DAY_SELECTED) {
                    startDateChooser.hidePopup();
                    try {
                        if (datevalidate.DateValidation(startDate.getText())) {
                            startDateError.setText("Invalid Date");
                            startDateError.setVisible(true);
                        }else{
                            dataParameters[0]=true;
                        }
                    } catch (ParseException ex) {
                        Logger.getLogger(CustomSearch1.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        endDateChooser.addEventDateChooser(new EventDateChooser() {
            @Override
            public void dateSelected(SelectedAction action, SelectedDate date) {
                System.out.println(date.getDay() + "-" + date.getMonth() + "-" + date.getYear());
                endDateError1.setVisible(false);
                if (action.getAction() == SelectedAction.DAY_SELECTED) {
                    endDateChooser.hidePopup();
                    try {
                        if (datevalidate.DateValidation(endDate.getText())) {
                            endDateError1.setText("Invalid Date");
                            endDateError1.setVisible(true);
                        }else{
                            dataParameters[1]=true;
                        }
                    } catch (ParseException ex) {
                        Logger.getLogger(CustomSearch1.class.getName()).log(Level.SEVERE, null, ex);
                    }
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
            System.out.println("DB Error 117 : "+e);
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
            
            String carPrices=result.getString("Price");
            carPrice.setText("Rs: "+carPrices);
        
            byte[] img=result.getBytes("CarImage");
            ImageIcon image=new ImageIcon(img);
            Image im=image.getImage();
            Image myimage=im.getScaledInstance(CarImageRequestCard.getWidth(), CarImageRequestCard.getHeight(), Image.SCALE_SMOOTH);
            ImageIcon Scaledimage=new ImageIcon(myimage);
            CarImageRequestCard.setIcon(Scaledimage);
            CarName.setText(data.car_model+" "+data.car_type);
            SeatCount2.setText(data.seat_no);
            AcTypeRequestCard.setText(data.ac_Type);
            FuelTypeRequestCard.setText(data.fuel_type);
            
            individucalComPickupDate.setText(startDate.getText());
            individuvalComDropOffDate.setText(endDate.getText());
            
            System.out.println("datessss "+startDate.getText());
            
            long daycount=dayMonthCalculator(startDate.getText(), endDate.getText());
            
            
            if (daycount==0) {
                dayCountBooked.setText(String.valueOf(1));
                carPriceTotal.setText("Rs: "+String.valueOf(1*Double.valueOf(carPrices)));
            }else{
                dayCountBooked.setText(String.valueOf(daycount+2));
                carPriceTotal.setText("Rs: "+String.valueOf((daycount+2)*Double.valueOf(carPrices)));
            }
            
            
            
            withOutDriverRadio.setSelected(true);
            
        }catch (Exception e){
            System.out.println("DB Error req card : "+e);
        }
    }
    
    private long dayMonthCalculator(String startDate,String endDate){
        LocalDate datebefore=LocalDate.of(Integer.parseInt(startDate.substring(6)), Integer.parseInt(startDate.substring(3,5)), Integer.parseInt(startDate.substring(0,2)));
        LocalDate dateAfter=LocalDate.of(Integer.parseInt(endDate.substring(6)), Integer.parseInt(endDate.substring(3,5)), Integer.parseInt(endDate.substring(0,2)));
        Period period=Period.between(datebefore, dateAfter);
        long dayCount=Math.abs(period.getDays());
        return dayCount;
    }
    
    
    public void loadCarCards(){
        
        
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
            
            Car car1=new Car();
            ArrayList<Car> carResult=car1.getAvailableCarDetails(pickupDate, droupOutDate,vehicalTypeSearchBox.getSelectedItem().toString());
            for (int i = 0; i < carResult.size(); i++) {
                addItem(new ItemModel(carResult.get(i).getCarNumber(),carResult.get(i).getCarType(),carResult.get(i).getCarModel(),String.valueOf(carResult.get(i).getSeatNo()),
                        carResult.get(i).getAcType(),carResult.get(i).getFuelType(),carResult.get(i).getCarImage()));
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
        startDateError = new javax.swing.JLabel();
        endDateError1 = new javax.swing.JLabel();
        SearchBarImage = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        cardContainer = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        BackToSearchPanel = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        BackToAvailableVehicals = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        CarImageRequestCard = new javax.swing.JLabel();
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
        carPrice = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        dayCountBooked = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        dummyValue = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        carPriceTotal = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jButton2.setBackground(new java.awt.Color(28, 78, 128));
        jButton2.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
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
                .addContainerGap(1098, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        getContentPane().add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1290, 50));

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Segoe UI Variable", 1, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Create Your own travel comfort!");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 110, 540, 50));

        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Vehical Type");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 260, -1, -1));

        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Start Date");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 260, -1, -1));

        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("End Date");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 260, -1, -1));

        jButton3.setText("Search");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 280, 150, 40));

        endDate.setForeground(new java.awt.Color(0, 0, 0));
        endDate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                endDateMouseClicked(evt);
            }
        });
        jPanel1.add(endDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 280, 220, 40));

        startDate.setForeground(new java.awt.Color(0, 0, 0));
        startDate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                startDateMouseClicked(evt);
            }
        });
        jPanel1.add(startDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 280, 220, 40));

        vehicalTypeSearchBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Car", "Van" }));
        jPanel1.add(vehicalTypeSearchBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 280, 130, 40));

        startDateError.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        startDateError.setForeground(new java.awt.Color(204, 0, 0));
        jPanel1.add(startDateError, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 330, 220, 20));

        endDateError1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        endDateError1.setForeground(new java.awt.Color(204, 0, 0));
        jPanel1.add(endDateError1, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 330, 220, 20));

        SearchBarImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/searchform.png"))); // NOI18N
        SearchBarImage.setAutoscrolls(true);
        SearchBarImage.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel1.add(SearchBarImage, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -30, 1300, 690));

        jTabbedPane1.addTab("tab1", jPanel1);

        jPanel2.setBackground(new java.awt.Color(28, 78, 128));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Available Vehicals");

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        cardContainer.setBackground(new java.awt.Color(28, 78, 128));
        cardContainer.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(28, 78, 128)));
        cardContainer.setForeground(new java.awt.Color(28, 78, 128));

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

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                jLabel4AncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

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
                .addContainerGap(577, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1201, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BackToSearchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 556, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        jPanel7.setBackground(new java.awt.Color(28, 128, 78));

        CarImageRequestCard.setBackground(new java.awt.Color(51, 255, 255));
        CarImageRequestCard.setForeground(new java.awt.Color(255, 255, 255));
        CarImageRequestCard.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

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

        carPrice.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        carPrice.setForeground(new java.awt.Color(0, 0, 0));
        carPrice.setText("Rs: 12500");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 0, 0));
        jLabel16.setText("No of Days Suppose to Book");

        dayCountBooked.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        dayCountBooked.setForeground(new java.awt.Color(0, 0, 0));
        dayCountBooked.setText("3");

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 0, 0));
        jLabel18.setText("Total Approximate Charge");

        dummyValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dummyValueActionPerformed(evt);
            }
        });

        jLabel19.setForeground(new java.awt.Color(0, 0, 0));
        jLabel19.setText("Enter Your Name Here");

        carPriceTotal.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        carPriceTotal.setForeground(new java.awt.Color(0, 0, 0));
        carPriceTotal.setText("Rs: 20000");

        jLabel3.setText("Conditions Apply: Driver Charge may be Vary");

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/freee.png"))); // NOI18N

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/seatttt.png"))); // NOI18N

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/ac22.png"))); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(CarName, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(withDriverRadio)
                                    .addComponent(jLabel14)
                                    .addComponent(jLabel16)
                                    .addComponent(jLabel18))
                                .addGap(38, 38, 38)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(carPrice)
                                    .addComponent(dayCountBooked, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(carPriceTotal))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(124, 124, 124)
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(AcTypeRequestCard)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(FuelTypeRequestCard)
                                .addGap(44, 44, 44)))
                        .addContainerGap())
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(withOutDriverRadio))
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(individucalComPickupDate, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(individuvalComDropOffDate, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(33, 33, 33))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
                                    .addComponent(dummyValue, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel12)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(SeatCount2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27))))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(CarName, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(SeatCount2)
                        .addComponent(AcTypeRequestCard))
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(FuelTypeRequestCard))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(individucalComPickupDate)
                    .addComponent(individuvalComDropOffDate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(withDriverRadio)
                    .addComponent(withOutDriverRadio))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(carPrice))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(dayCountBooked))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(carPriceTotal))
                .addGap(18, 18, 18)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(dummyValue, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(72, 72, 72))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(CarImageRequestCard, javax.swing.GroupLayout.PREFERRED_SIZE, 657, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 372, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addComponent(CarImageRequestCard, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(BackToAvailableVehicals, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(48, 48, 48))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(BackToAvailableVehicals, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(77, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("tab3", jPanel3);

        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1290, 700));

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
        if (dataParameters[0] && dataParameters[1]) {
            loadCarCards();
            jTabbedPane1.setSelectedIndex(1);
            jPanel6.setBackground(new Color(28, 78, 128));
        }else{
            JOptionPane.showMessageDialog(null, "Incorrect Input Data!", "DBMS", JOptionPane.ERROR_MESSAGE);
        }
        
        

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

    private void jLabel4AncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jLabel4AncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel4AncestorAdded

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
    private javax.swing.JLabel carPrice;
    private javax.swing.JLabel carPriceTotal;
    private javax.swing.JPanel cardContainer;
    private javax.swing.JLabel dayCountBooked;
    private javax.swing.ButtonGroup driverbtngroup;
    private javax.swing.JTextField dummyValue;
    private javax.swing.JTextField endDate;
    private com.raven.datechooser.DateChooser endDateChooser;
    private javax.swing.JLabel endDateError1;
    private javax.swing.JLabel individucalComPickupDate;
    private javax.swing.JLabel individuvalComDropOffDate;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
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
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField startDate;
    private com.raven.datechooser.DateChooser startDateChooser;
    private javax.swing.JLabel startDateError;
    private javax.swing.JComboBox<String> vehicalTypeSearchBox;
    private javax.swing.JRadioButton withDriverRadio;
    private javax.swing.JRadioButton withOutDriverRadio;
    // End of variables declaration//GEN-END:variables
}
