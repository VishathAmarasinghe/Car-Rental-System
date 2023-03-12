/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package car.rental.system;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 *
 * @author Akila
 */
public class ItemModel {
    String car_type;
    String car_model;
    String seat_no;
    String ac_Type;
    String fuel_type;
    ImageIcon carimage;
    String car_keyValue;

    public String getCar_keyValue() {
        return car_keyValue;
    }

    public void setCar_keyValue(String car_keyValue) {
        this.car_keyValue = car_keyValue;
    }

    public String getCar_type() {
        return car_type;
    }

    public void setCar_type(String car_type) {
        this.car_type = car_type;
    }

    public String getCar_model() {
        return car_model;
    }

    public void setCar_model(String car_model) {
        this.car_model = car_model;
    }

    public String getSeat_no() {
        return seat_no;
    }

    public void setSeat_no(String seat_no) {
        this.seat_no = seat_no;
    }

    public String getAc_Type() {
        return ac_Type;
    }

    public void setAc_Type(String ac_Type) {
        this.ac_Type = ac_Type;
    }

    public String getFuel_type() {
        return fuel_type;
    }

    public void setFuel_type(String fuel_type) {
        this.fuel_type = fuel_type;
    }

    public ImageIcon getCarimage() {
        return carimage;
    }

    public void setCarimage(ImageIcon carimage) {
        this.carimage = carimage;
    }

    public ItemModel(String car_keyValue ,String car_type, String car_model, String seat_no, String ac_Type, String fuel_type, ImageIcon carimage) {
        this.car_type = car_type;
        this.car_model = car_model;
        this.seat_no = seat_no;
        this.ac_Type = ac_Type;
        this.fuel_type = fuel_type;
        this.carimage = carimage;
        this.car_keyValue=car_keyValue;
    }

   

    
    
    
    
}
