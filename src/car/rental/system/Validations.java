/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package car.rental.system;

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
            Pattern p=Pattern.compile("^[A-Z0-9._%+-]+@[a-z0-9.-]+//.[a-z]$",Pattern.CASE_INSENSITIVE);
            Matcher m=p.matcher(mail);
            if (!m.find()) {
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
    
    
    
    
    
    
    
    public static void main(String[] args) {
        Validations v1=new Validations();
        System.out.println(v1.NICvalidate("20013360070"));
    }
    
}
