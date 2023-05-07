/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package car.rental.system;

import java.awt.BorderLayout;
import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author Akila
 */
public class StatisticalData {

    
    Connection con=null;
    Statement st=null;
    ResultSet rs=null;
    PreparedStatement ps=null;
    
    /**
     *get all statistical data for dashboard
     * @return result array 
     */
    public String[] statisticalDataSender(){
        String[] resultArray={"0","0","0"};
        try {
            con=DatabaseConnection.StablishDatabaseConnection();
                
            String slectMax = "select count(empID) from employee;";
            ps = con.prepareStatement(slectMax);
            rs = ps.executeQuery();
            rs.next();
            String totalEmp=rs.getString("count(empID)");
            resultArray[0]=totalEmp;
            
            
            slectMax = " select count(CustomerID) from Customer;";
            ps = con.prepareStatement(slectMax);
            rs = ps.executeQuery();
            rs.next();
            String totalcustomers=rs.getString("count(CustomerID)");
            resultArray[1]=totalcustomers;
            
            
            
            slectMax = " select count(carNumber) from cars;";
            ps = con.prepareStatement(slectMax);
            rs = ps.executeQuery();
            rs.next();
            String totalcars=rs.getString("count(carNumber)");
            resultArray[2]=totalcars;
                
            
        } catch (Exception e) {
            System.out.println("Statiscal Error "+e);
        }
        
        finally{
            DatabaseConnection.removeConnection(rs, st, ps, con);
            return resultArray;
        }
    }
    
    /**
     *get reservation details for the bar chart 
     *
     * @param panel
     */
    public void barchart(JPanel panel){
        LocalDate currentDate=LocalDate.now();
        System.out.println(currentDate);
        String CurrentDateString=String.valueOf(currentDate);
        int month=Integer.parseInt(CurrentDateString.substring(5,7));
        System.out.println("asdasd "+month);
        
        try {
            con=DatabaseConnection.StablishDatabaseConnection();
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for (int i = 1; i <= 30; i++) {
                String newDate = CurrentDateString.substring(0, 8)+i;
                String showingDate=currentDate.getMonth()+"/"+i;
                String slectMax = " select count(ReservationID) from reservation where \""+newDate+"\" between pickedupdate and dropOffDate;";
                ps = con.prepareStatement(slectMax);
                rs = ps.executeQuery();
                rs.next();
                String countvalue=rs.getString("count(ReservationID)");
                System.out.println(newDate+"  "+countvalue);
                dataset.setValue(Integer.parseInt(countvalue), "", String.valueOf(i));
            }

            JFreeChart chart = ChartFactory.createBarChart("Number Of Reservations in "+currentDate.getMonth(), "Date", "Reservation Count", dataset, PlotOrientation.VERTICAL, false, false, false);
            CategoryPlot catplot = chart.getCategoryPlot();
            catplot.setRangeGridlinePaint(Color.WHITE);
            chart.setBackgroundPaint(Color.WHITE);
            catplot.setBackgroundPaint(Color.WHITE);

            ((BarRenderer)catplot.getRenderer()).setBarPainter(new StandardBarPainter());   //plot bar chart 
            BarRenderer r=(BarRenderer)chart.getCategoryPlot().getRenderer();
            r.setSeriesPaint(0, new Color(128, 28, 78));
            
            ChartPanel chartpanel = new ChartPanel(chart);
            panel.removeAll();
            panel.add(chartpanel, BorderLayout.CENTER);
            panel.validate();


        }
        
            
            
         catch (Exception e) {
            System.out.println("Error from 49 "+e);
        }finally{
            DatabaseConnection.removeConnection(rs, st, ps, con);
        }
    }
    
    /**
     *
     * @return
     */
    public String[] statisticalDataSenderCasher(){
        String[] resultArray={"0","0","0"};
        try {
            
            con=DatabaseConnection.StablishDatabaseConnection();
                
            String slectMax = " select count(reservationID) from reservation where reservationStatus=\"Pending\";";
            ps = con.prepareStatement(slectMax);
            ResultSet rs = ps.executeQuery();
            rs.next();
            String pendingCount=rs.getString("count(reservationID)");
            resultArray[0]=pendingCount;
            
            
            slectMax = " select count(CustomerID) from Customer;";
            ps = con.prepareStatement(slectMax);
            rs = ps.executeQuery();
            rs.next();
            String totalcustomers=rs.getString("count(CustomerID)");
            resultArray[1]=totalcustomers;
            
            
            
            slectMax = "select count(reservationID) from reservation where reservationStatus=\"Proceeded\";";
            ps = con.prepareStatement(slectMax);
            rs = ps.executeQuery();
            rs.next();
            String proceededCount=rs.getString("count(reservationID)");
            resultArray[2]=proceededCount;
                
            
        } catch (Exception e) {
            System.out.println("Statiscal Error in cashier "+e);
        }
        
        finally{
            DatabaseConnection.removeConnection(rs, st, ps, con);
            return resultArray;
        }
    }
    
    /**
     *
     * @param panel
     */
    public void barchartCashier(JPanel panel){
        LocalDate currentDate=LocalDate.now();
        System.out.println(currentDate);
        String CurrentDateString=String.valueOf(currentDate);
        int month=Integer.parseInt(CurrentDateString.substring(5,7));
        System.out.println("asdasd "+month);
        
        try {
            con=DatabaseConnection.StablishDatabaseConnection();
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for (int i = 1; i <= 30; i++) {
                String newDate = CurrentDateString.substring(0, 8)+i;
                String showingDate=currentDate.getMonth()+"/"+i;
                String slectMax = " select count(ReservationID) from reservation where dropOffDate=\""+newDate+"\";";
                PreparedStatement ps = con.prepareStatement(slectMax);
                ResultSet rs = ps.executeQuery();
                rs.next();
                String countvalue=rs.getString("count(ReservationID)");
                System.out.println(newDate+"  "+countvalue);
                dataset.setValue(Integer.parseInt(countvalue), "", String.valueOf(i));
            }

            JFreeChart chart = ChartFactory.createBarChart("Number Of Reservations due in "+currentDate.getMonth(), "Date", "Reservation Count", dataset, PlotOrientation.VERTICAL, false, false, false);
            CategoryPlot catplot = chart.getCategoryPlot();
            catplot.setRangeGridlinePaint(Color.WHITE);
            chart.setBackgroundPaint(Color.WHITE);
            catplot.setBackgroundPaint(Color.WHITE);

            ((BarRenderer)catplot.getRenderer()).setBarPainter(new StandardBarPainter());
            BarRenderer r=(BarRenderer)chart.getCategoryPlot().getRenderer();
            r.setSeriesPaint(0, new Color(128, 28, 78));
            
            ChartPanel chartpanel = new ChartPanel(chart);
            panel.removeAll();
            panel.add(chartpanel, BorderLayout.CENTER);
            panel.validate();


        }
        
            
            
         catch (Exception e) {
            System.out.println("Error from 49 "+e);
        }
        finally{
            DatabaseConnection.removeConnection(rs, st, ps, con);
        }
    }
    
    
}
