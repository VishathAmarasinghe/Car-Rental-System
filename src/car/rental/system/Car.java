/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package car.rental.system;

import java.awt.Image;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Akila
 */
public class Car {
    Connection con=null;
    Statement st=null;
    ResultSet rs=null;
    PreparedStatement ps=null;

    private String CarNumber;

    private String vehicalType;

    private String carType;

    private String carModel;

    private int seatNo;

    private String AcType;

    private String fuelType;

    private ImageIcon carImage;

    private String StringOwnerId;

    private String price;

    private InputStream CarImageRawFile;

    /**
     * get car number
     *
     * @return
     */
    public String getCarNumber() {
        return CarNumber;
    }

    /**
     * get car image raw file(Image file)
     *
     * @return
     */
    public InputStream getCarImageRawFile() {
        return CarImageRawFile;
    }

    /**
     * set car image raw file
     *
     * @param CarImageRawFile
     */
    public void setCarImageRawFile(InputStream CarImageRawFile) {
        this.CarImageRawFile = CarImageRawFile;
    }

    public void setCarNumber(String CarNumber) {
        this.CarNumber = CarNumber;
    }

    public String getVehicalType() {
        return vehicalType;
    }

    public void setVehicalType(String vehicalType) {
        this.vehicalType = vehicalType;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public int getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(int seatNo) {
        this.seatNo = seatNo;
    }

    public String getAcType() {
        return AcType;
    }

    public void setAcType(String AcType) {
        this.AcType = AcType;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public ImageIcon getCarImage() {
        return carImage;
    }

    public void setCarImage(ImageIcon carImage) {
        this.carImage = carImage;
    }

    public String getStringOwnerId() {
        return StringOwnerId;
    }

    public void setStringOwnerId(String StringOwnerId) {
        this.StringOwnerId = StringOwnerId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    /**
     * get selected car details
     *
     * @param selectedCarID
     */
    public void getSelectedCarDetails(String selectedCarID) {
        try {
            con=DatabaseConnection.StablishDatabaseConnection();
            String query = "Select * from cars where CarNumber=\"" + selectedCarID + "\"";
            ps = con.prepareStatement(query);
            ResultSet CarResult = ps.executeQuery();

            while (CarResult.next()) {
                setCarNumber(CarResult.getString("CarNumber"));
//                carNumberPlate.setText(CarResult.getString("CarNumber"));
//                carNumberPlate.setEditable(false);

                setCarType(CarResult.getString("CarType"));
                setCarModel(CarResult.getString("CarModel"));   //set car details to the parameters
                setSeatNo(CarResult.getInt("SeatNo"));
                setAcType(CarResult.getString("ACType"));

                setFuelType(CarResult.getString("FuelType"));

                setVehicalType(CarResult.getString("VehicalType"));

                setStringOwnerId(CarResult.getString("OwnerID"));

                setPrice(CarResult.getString("Price"));
                byte[] img = CarResult.getBytes("CarImage");
                ImageIcon image = new ImageIcon(img);
                setCarImage(image);

            }

        } catch (Exception e) {
            System.out.println("DB Error : " + e);
        }
        finally{
            DatabaseConnection.removeConnection(rs, st, ps, con);
        }
    }

    /**
     * get available cars which are in between the selected dates
     *
     * @param startDate
     * @param endDate
     * @param vehicalTypeSelector
     * @return
     */
    public ArrayList<Car> getAvailableCarDetails(String startDate, String endDate, String vehicalTypeSelector) {
        ArrayList<Car> carArray = new ArrayList<>();
        try {
            con=DatabaseConnection.StablishDatabaseConnection();
            String s = "select * from cars where carNumber not in (select VehicalNumber from reservation where pickedUpdate between \"" + startDate + "\" "
                    + "and \"" + endDate + "\" and dropOffdate between \"" + startDate + "\"and \"" + endDate + "\") and vehicalType= \"" + vehicalTypeSelector + "\"";
            ps = con.prepareStatement(s);

            rs = ps.executeQuery();
            while (rs.next()) {

                Car c1 = new Car();   //create car card

                byte[] img = rs.getBytes("CarImage");

                String car_keyValue = rs.getString("CarNumber");
                String car_type = rs.getString("CarType");
                String car_model = rs.getString("CarModel");
                String seat_no = rs.getString("SeatNo");
                String ac_Type = rs.getString("ACType");
                String fuel_type = rs.getString("FuelType");
                System.out.println(car_keyValue);      //get selected car details
                System.out.println("car type " + car_type);
                System.out.println("car model " + car_model);
                System.out.println("seat " + seat_no);
                System.out.println("ac type  " + ac_Type);
                System.out.println("fuel type " + fuel_type);

                c1.CarNumber = car_keyValue;
                c1.carType = car_type;
                c1.carModel = car_model;
                c1.seatNo = Integer.parseInt(seat_no);
                c1.AcType = ac_Type;
                c1.fuelType = fuel_type;

                ImageIcon image = new ImageIcon(img);
                Image im = image.getImage();
                Image myimage = im.getScaledInstance(236, 172, Image.SCALE_SMOOTH);
                ImageIcon newimage = new ImageIcon(myimage);

                c1.carImage = newimage;
                carArray.add(c1);    //add car details to the array

            }

        } catch (Exception e) {
            System.out.println("DB Error car range  Selecting : " + e);
        }finally{
            DatabaseConnection.removeConnection(rs, st, ps, con);
            return carArray;
        }

        
    }

    /**
     * add new car to the database
     */
    public void addNewCar() {
        try {
            con=DatabaseConnection.StablishDatabaseConnection();
            String querys = "insert into cars(CarNumber,VehicalType,CarType,CarModel,SeatNo,ACType,FuelType,CarImage,OwnerID,Price) values(?,?,?,?,?,?,?,?,?,?)";
            ps = con.prepareStatement(querys);
//            InputStream iss=new FileInputStream(new File(selectedImagePath));
            ps.setString(1, getCarNumber());
            ps.setString(2, getVehicalType());
            ps.setBlob(8, getCarImageRawFile());
            ps.setString(3, getCarType());
            ps.setString(4, getCarModel());
            ps.setInt(5, getSeatNo());
            ps.setString(6, getAcType());
            ps.setString(7, getFuelType());
            ps.setString(9, getStringOwnerId());
            ps.setInt(10, Integer.parseInt(getPrice()));

            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Car Added Successfully", "Data Manipulation", JOptionPane.INFORMATION_MESSAGE);

            

        } catch (Exception e) {
            System.out.println("DB Error : " + e);
            JOptionPane.showMessageDialog(null, "Car Adding Error Found", "Data Manipulation", JOptionPane.ERROR_MESSAGE);
        }finally{
            DatabaseConnection.removeConnection(rs, st, ps, con);
        }
    }

    /**
     * update selected car details
     */
    public void updateSelectedCarClass() {
        try {
            con=DatabaseConnection.StablishDatabaseConnection();
            st = con.createStatement();
            String query = "update cars set VehicalType= \"" + getVehicalType()
                    + "\", CarType= \"" + getCarType() + "\",CarModel= \"" + getCarModel() + "\",SeatNo= \"" + getSeatNo() + "\","
                    + "ACType= \"" + getAcType() + "\", FuelType= \"" + getFuelType() + "\","
                    + "OwnerID= \"" + getStringOwnerId() + "\", price=" + getPrice() + " where CarNumber= \"" + getCarNumber() + "\"";
            st.executeUpdate(query);
            if (getCarImageRawFile() != null) {
                try {
                    String updateImageQuary = "update cars set CarImage= (?) where carNumber=\"" + getCarNumber() + "\"";
                    PreparedStatement ps = con.prepareStatement(updateImageQuary);

                    ps.setBlob(1, getCarImageRawFile());
                    ps.executeUpdate();
                } catch (Exception e) {
                    System.out.println("Car IMage Updating Error");
                }

            }
            JOptionPane.showMessageDialog(null, "Car Updated Successfully", "Data Manipulation", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            System.out.println("Update Existing Car " + ex);
            JOptionPane.showMessageDialog(null, "Car Updating Error", "Data Manipulation", JOptionPane.ERROR_MESSAGE);
        }finally{
            DatabaseConnection.removeConnection(rs, st, ps, con);
        }
    }

    /**
     * load all car details
     *
     * @param cartable
     */
    public void renderCarTable(JTable cartable) {
        try {
            DefaultTableModel CartableLoad = (DefaultTableModel) cartable.getModel();
            CartableLoad.getDataVector().removeAllElements();
            CartableLoad.fireTableDataChanged();

            con=DatabaseConnection.StablishDatabaseConnection();
            String s = "select * from cars";
            ps = con.prepareStatement(s);
            rs = ps.executeQuery();

            while (rs.next()) {
                String car_keyValue = rs.getString("CarNumber");
                String car_type = rs.getString("CarType");
                String car_model = rs.getString("CarModel");
                String seat_no = rs.getString("SeatNo");
                String ac_Type = rs.getString("ACType");
                String fuel_type = rs.getString("FuelType");
                String carprice = rs.getString("price");
                byte[] img = rs.getBytes("CarImage");

                ImageIcon image = new ImageIcon(img);
                Image im = image.getImage();
                Image myimage = im.getScaledInstance(120, 100, Image.SCALE_SMOOTH);
                ImageIcon newimage = new ImageIcon(myimage);

                JLabel imageLable = new JLabel();
                imageLable.setIcon(newimage);

                CartableLoad.addRow(new Object[]{car_keyValue, car_model, car_type, seat_no, ac_Type, fuel_type, imageLable, carprice});  //add car details to the table

            }
            

        } catch (Exception e) {
            System.out.println(e);
        }finally{
            DatabaseConnection.removeConnection(rs, st, ps, con);
        }
    }

    /**
     * delete selected car items
     *
     * @param clickedIndexID
     */
    public void deleteItem(String clickedIndexID) {
        try {
            con=DatabaseConnection.StablishDatabaseConnection();
            st = con.createStatement();
            String deletePhoneNoRaw = "delete from cars where CarNumber=\"" + clickedIndexID + "\"";
            st.executeUpdate(deletePhoneNoRaw);
        } catch (Exception e) {

        }
        finally{
            DatabaseConnection.removeConnection(rs, st, ps, con);
        }
    }
    
    public void searchCarNew(String searchData, JTable resultTable) {

        System.out.println("searched Name " + searchData);
        

        try {
            
            DefaultTableModel CartableLoad = (DefaultTableModel) resultTable.getModel();
            CartableLoad.getDataVector().removeAllElements();
            CartableLoad.fireTableDataChanged();

            con = DatabaseConnection.StablishDatabaseConnection();
            String[] searchfeilds = {"carNumber", "VehicalType", "CarType", "CarModel", "SeatNo", "ACType", "FuelType", "OwnerID", "Price"};
            for (int i = 0; i < searchfeilds.length; i++) {

                rs = searchCar(con, "cars", searchfeilds[i], searchData,i);

                if (rs != null) {

//                    loadEmployeeData("id", result1.getString("empID"), resultTable, null);
                    while (rs.next()) {
                        String car_keyValue = rs.getString("CarNumber");
                        String car_type = rs.getString("CarType");
                        String car_model = rs.getString("CarModel");
                        String seat_no = rs.getString("SeatNo");
                        String ac_Type = rs.getString("ACType");
                        String fuel_type = rs.getString("FuelType");
                        String carprice = rs.getString("price");
                        byte[] img = rs.getBytes("CarImage");

                        ImageIcon image = new ImageIcon(img);
                        Image im = image.getImage();
                        Image myimage = im.getScaledInstance(120, 100, Image.SCALE_SMOOTH);
                        ImageIcon newimage = new ImageIcon(myimage);

                        JLabel imageLable = new JLabel();
                        imageLable.setIcon(newimage);

                        CartableLoad.addRow(new Object[]{car_keyValue, car_model, car_type, seat_no, ac_Type, fuel_type, imageLable, carprice});  //add car details to the table

                    }
//                    break;
                }

            }

            

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DatabaseConnection.removeConnection(null, st, ps, con);
        }
    }

    
    
    
    //search car by type, ac type, model etc.
    private ResultSet searchCar(Connection con, String tableName, String searchField, String searchData,int num) {
        try {
            String singleData;
            if (num==2  || num==3) {
                String editors="\"%"+String.valueOf(searchData)+"%\"";
                singleData = "select * from " + tableName + " where " + searchField + " like "+editors;
                System.out.println(singleData);
            }else{
               singleData = "select * from " + tableName + " where " + searchField + "=\"" + searchData + "\"";
            }
             
            PreparedStatement ps = con.prepareStatement(singleData);
            ResultSet rs = ps.executeQuery();

            
            return rs;
            
        } catch (Exception e) {
            System.out.println("checkCustomer Search " + e);
            return null;
        }
        finally{
            DatabaseConnection.removeConnection(null, st, ps, null);
        }
    }

    /**
     * set all details to the parameters
     *
     * @param cardetailArray
     * @param iss
     */
    public void setAllCarDetails(String[] cardetailArray, InputStream iss) {
        setCarNumber(cardetailArray[0]);
        setVehicalType(cardetailArray[1]);
        setCarType(cardetailArray[2]);
        setCarModel(cardetailArray[3]);
        setSeatNo(Integer.parseInt(cardetailArray[4]));
        setAcType(cardetailArray[5]);
        setFuelType(cardetailArray[6]);
        setCarImageRawFile(iss);
        setStringOwnerId(cardetailArray[7]);
        setPrice(cardetailArray[8]);

    }

}
