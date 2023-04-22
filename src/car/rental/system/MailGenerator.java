/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package car.rental.system;



import java.io.File;
import java.util.Date;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JOptionPane;
/**
 *
 * @author Akila
 */
public class MailGenerator {
    
    
    
    
    
    
  
    public boolean sendMailsAuthentications(String SenderMail,String employeeUserName,String employeePassword){
        
        String subject_title="ABC Car Rental System (Employee Authentication)";
        String sendContent="Hello User, \nWelcome to ABC Car Rental Service as an employee\nYou can work with our system using below user credentials\n"
                + "UserName: "+employeeUserName+"\nPassword: "+employeePassword+"\n\n\nThank You!\nBest Regards\nSystemAdmin\nABC Car Rental pvt(Ltd)";
        try {
            Properties properties=new Properties();
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
            properties.put("mail.smtp.port", "587");
            Session session=Session.getDefaultInstance(properties, new Authenticator(){
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("projectcarrent@gmail.com","tmqiirntuafgilem");
                }
                
            });
            Message message=new MimeMessage(session);
            message.setSubject(subject_title);
            message.setContent(sendContent,"text/plain");
            message.setFrom(new InternetAddress("projectcarrent@gmail.com"));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(SenderMail));
            message.setSentDate(new Date());
            
            Transport.send(message);
            return true;
        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null,e.getMessage());
               System.out.println(e);
               return false;
               
        }
    }
    
    
    public void sendCustomerBill(String SendersEmailAddress,File selectedFile){
        try {
            Properties properties=new Properties();
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
            properties.put("mail.smtp.port", "587");
            Session session=Session.getDefaultInstance(properties, new Authenticator(){
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("projectcarrent@gmail.com","tmqiirntuafgilem");
                }
                
            });
            Message message=new MimeMessage(session);
            message.setSubject("Car Rent Payment Bill");
//            message.setContent(content.getText(),"text/plain");
            message.setFrom(new InternetAddress("projectcarrent@gmail.com"));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(SendersEmailAddress));
            message.setSentDate(new Date());
            
            Multipart multipart=new MimeMultipart();
            
            BodyPart bodypart=new MimeBodyPart();
            String content="Hello Customer!!\nThank You for joining with us, Your payment Bill is attached here\n";
            bodypart.setText(content);
            multipart.addBodyPart(bodypart);
            
            
                DataSource datasource=new FileDataSource(selectedFile);
                MimeBodyPart mimeBodyPart=new MimeBodyPart();
                mimeBodyPart.setDataHandler(new DataHandler(datasource));
                mimeBodyPart.setFileName(selectedFile.getName());
                multipart.addBodyPart(mimeBodyPart);
            
            
            message.setContent(multipart);
            Transport.send(message);
            JOptionPane.showMessageDialog(null,"Bill Sended to the Customer via Mail");
        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null,e.getMessage());
               System.out.println(e);
        }
    }
}
