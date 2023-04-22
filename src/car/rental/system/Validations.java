/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package car.rental.system;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Akila
 */
public class Validations {
    
    private int fNameLastNameSize=25;
    private int emailSize=50;
    private int addressSize=40;
    
    
    
    
   private String fnameError;
   private String lnameError;
   private String emilError;
   private String addres1Error;
   private String address2Error;
   private String cityError;
   private String phoneNoError1;
   private String phoneNoError2;
   private String nicError;

    public String getFnameError() {
        return fnameError;
    }

    public void setFnameError(String fnameError) {
        this.fnameError = fnameError;
    }

    public String getLnameError() {
        return lnameError;
    }

    public void setLnameError(String lnameError) {
        this.lnameError = lnameError;
    }

    public String getEmilError() {
        return emilError;
    }

    public void setEmilError(String emilError) {
        this.emilError = emilError;
    }

    public String getAddres1Error() {
        return addres1Error;
    }

    public void setAddres1Error(String addres1Error) {
        this.addres1Error = addres1Error;
    }

    public String getAddress2Error() {
        return address2Error;
    }

    public void setAddress2Error(String address2Error) {
        this.address2Error = address2Error;
    }

    public String getCityError() {
        return cityError;
    }

    public void setCityError(String cityError) {
        this.cityError = cityError;
    }

    public String getPhoneNoError1() {
        return phoneNoError1;
    }

    public void setPhoneNoError1(String phoneNoError1) {
        this.phoneNoError1 = phoneNoError1;
    }

    public String getPhoneNoError2() {
        return phoneNoError2;
    }

    public void setPhoneNoError2(String phoneNoError2) {
        this.phoneNoError2 = phoneNoError2;
    }

    public String getNicError() {
        return nicError;
    }

    public void setNicError(String nicError) {
        this.nicError = nicError;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    public String nameChecker(String name){
        String validateError="";
        if (name.length()>fNameLastNameSize) {
            validateError="name is too long (max<25 characters)";
        }else{
            Pattern p=Pattern.compile("[^a-z ]",Pattern.CASE_INSENSITIVE);
            Matcher m=p.matcher(name);
            if (m.find()) {
                validateError="Special Characters Found($%#*&./ and numbers)";
            }
        }
        return validateError;
    }
    
    
    public String emailChecker(String mail){
        String validateError="";
        if (mail.length()>emailSize) {
            validateError="email is too long (max<50 characters)";
        }else{
            Pattern p=Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",Pattern.CASE_INSENSITIVE);
            Matcher m=p.matcher(mail);
//            System.out.println(m.find());
            if (m.find()==false) {
                validateError="Invalid Email address";
            }
        }
        return validateError;
    }
    
    
    public String addressChecker(String addressChecker){
        String validateError="";
        if (addressChecker.length()>addressSize) {
            validateError="address is too long (max<50 characters)";
        }else{
            Pattern p=Pattern.compile("[`~!#$%@^&*?[:;{}|]+=]",Pattern.CASE_INSENSITIVE);
            Matcher m=p.matcher(addressChecker);
            if (m.find()) {
                validateError="Invalid address special Characters Contains";
            }
        }
        return validateError;
    }
    
    
    public String phoneNumberValidate(String phoneNoValidator){
        String validateError="";
        if (phoneNoValidator.length()!=10) {
            validateError="not a phone No";
        }else{
            Pattern p=Pattern.compile("[`[A-Z]~!#$%@^&*?[:;{}|]+=]",Pattern.CASE_INSENSITIVE);
            Matcher m=p.matcher(phoneNoValidator);
            if (m.find()) {
                validateError="Invalid phone No";
            }
        }
        return validateError;
    }
    
    public String NICvalidate(String NICvalidate){
        String validateError="";
        if (NICvalidate.length()!=12) {
            validateError="not a NIC No";
        }else{
            Pattern p=Pattern.compile("^[0-9]{12}|[0-9]{11}[x|v]",Pattern.CASE_INSENSITIVE);
            Matcher m=p.matcher(NICvalidate);
            if (!m.find()) {
                validateError="Invalid NIC No";
            }
        }
        return validateError;
    }
    
    
    public boolean DateValidation(String startDate ) throws ParseException{
        boolean dateValidator=false;
        System.out.println("incoming date "+startDate);
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
//        Date d1 = fmt.parse(selectedYear+selectedMonth+selectedDay);
        Date date1=new SimpleDateFormat("dd-MM-yyyy").parse(startDate);
        
        LocalDateTime myDateObj = LocalDateTime.now();

        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate=myDateObj.format(myFormatObj);
        System.out.println(formattedDate);
//        System.out.println(selectedDay+"-"+selectedMonth+"-"+selectedYear);
        
        if (date1.compareTo(new Date())<0) {
            System.out.println("less una");
            if (formattedDate.equals(startDate)) {
                dateValidator=false;
            }else{
                dateValidator=true;
            }
            System.out.println("current state "+dateValidator);
            
        }
        
        return dateValidator;
        
    }
    
    
    public String carNumberValidate(String carNumber){
        String validateError="";
        if (carNumber.length()==7 || carNumber.length()==6) {
           Pattern p=Pattern.compile("^([A-z]{1,2})[0-9]{4}$|^([A-z]{1,3})[0-9]{4}$",Pattern.CASE_INSENSITIVE);
            Matcher m=p.matcher(carNumber);
            if (!m.find()) {
                validateError="Invalid car Number";
            }
            
        }else{
            validateError="Not in the correct Format";
        }
        return validateError;
    }
    
    public String carTypeAndModelValidator(String modelOrType){
        String validateError="";
        if (modelOrType.length()<=1) {
            validateError="invalid input";
        }
        return validateError;
    }
    
    
    public String SeatNoValidator(String seatNo){
        String validateError="";
        try {
            int seatno=Integer.parseInt(seatNo);
            if (seatno>20) {
                validateError="invalid Count";
            }
        } catch (Exception e) {
            validateError="Not a Number";
        }finally{
            return validateError;
        }
    }
    
    
    
    public String priceValidator(String price){
        String validateError="";
        try {
            double priceN=Double.valueOf(price);
            if (priceN<=0) {
                validateError="invalid price";
            }
        } catch (Exception e) {
            validateError="Not a Number";
        }finally{
            return validateError;
        }
    }
    
    
    public String discountNameDescriptionValidator(String validateString){
        String validateError="";
        if (validateString.length()<=2) {
            validateError="need more Characters";
        }
        return validateError;
    }
    
    public String discountprecentageValidator(String validateString){
        String validateError="";
        try {
            double priceN=Double.valueOf(validateString);
            if (priceN<=0) {
                validateError="invalid price";
            }
        } catch (Exception e) {
            validateError="Not a Number";
        }finally{
            return validateError;
        }
    }
    
    
   
    
    
    
    
    
    
    
    public static void main(String[] args) throws ParseException {
        Validations v1=new Validations();
//        System.out.println(v1.DateValidation(""));
        System.out.println(v1.carNumberValidate("A@A1212"));
    }
    
}
