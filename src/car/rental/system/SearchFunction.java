/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package car.rental.system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Akila
 */
public class SearchFunction {
    
    private Connection con;
    
   public String[] getSearchedCustomerName(String searchData){
       
       System.out.println("searched Name "+searchData);
        
        String[] result={null,null,null,null,"",""};
        
        try {
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/carrentalsystem", "root", "akila123");
            String[] searchfeilds={"CustomerID","firstName","LastName","Email","Address1","City","NIC","PhoneNo"};
            for (int i = 0; i < searchfeilds.length; i++) {
                if (i==7) {
                    ResultSet phoneResult1=checkSearCustomer("customerphone", "phoneNo", searchData);
                    if (phoneResult1!=null) {
                    
                    
                    result[0]=phoneResult1.getString("CustomerID");
                    result[3]=phoneResult1.getString("PhoneNo");
                    ResultSet customerDetails=checkSearCustomer("customer", "CustomerID", result[0]);
                    result[1]=customerDetails.getString("FirstName");
                    result[2]=customerDetails.getString("lastName");
                    
                   
                    break;  
                                    }
                }else{
                    ResultSet result1=checkSearCustomer("customer", searchfeilds[i], searchData);
                
                if (result1!=null) {
                    
                    result[0]=result1.getString("CustomerID");
                    result[1]=result1.getString("FirstName");
                    result[2]=result1.getString("lastName");
                    ResultSet phoneResult=checkSearCustomer("customerphone", "CustomerID", result[0]);
                    result[3]=phoneResult.getString("PhoneNo");
                    int resultPlace=4;
                    while (phoneResult.next()) {                        
                        result[resultPlace]=phoneResult.getString("PhoneNo");
                        resultPlace++;
                        if (resultPlace==5) {
                            break;
                        }
                    }
                    break;  
                }
                }
                
                
            }

            con.close();

        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }
    
    private ResultSet checkSearCustomer(String tableName,String searchField,String searchData){
        try{
            String singleData="select * from "+tableName+" where "+searchField+"=\""+searchData+"\"";
            PreparedStatement ps = con.prepareStatement(singleData);
            ResultSet rs=ps.executeQuery();
            
            if (rs.next()) {
                return rs;
            }else{
                rs=null;
                return rs;
            }
        }catch(Exception e){
                System.out.println("checkCustomer Search "+e);
                return null;
        }
    }
    
    
    
    public static void main(String[] args) {
        SearchFunction sf1=new SearchFunction();
        String[] result3=sf1.getSearchedCustomerName("C001");
        System.out.println(result3[0]);
        System.out.println(result3[1]);
        System.out.println(result3[2]);
        System.out.println(result3[3]);
        System.out.println(result3[4]);
    }
}
