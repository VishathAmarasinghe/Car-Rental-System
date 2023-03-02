/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package car.rental.system;

import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Akila
 */
public class CarRentalSystem {
    
    
    public  static void closeWindows(Window source){
        WindowEvent closeWindow=new WindowEvent(source, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(closeWindow);
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        SplashScreen splash=new SplashScreen();
        splash.setVisible(true);
        
        for (int i = 0; i <= 100; i++) {
            try {
                Thread.sleep(50);
                splash.presentageValue.setText(String.valueOf(i)+"%");
                splash.progressBarshower.setValue(i);
                if (i>50) {
                    splash.backgroundSplash.setVisible(false);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(CarRentalSystem.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        splash.dispose();
        CustomSearch1 s1=new CustomSearch1();
        s1.setVisible(true);
    }
    
}
