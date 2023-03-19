/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package successBtn;


import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;



/**
 *
 * @author Akila
 */
public class TableActionbtnCellRender extends DefaultTableCellRenderer{
    
    private boolean visibalValue;

    public TableActionbtnCellRender(boolean visibility) {
        visibalValue=visibility;
    }
    
    
    
    
    
    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object o, boolean isSeleted, boolean bln1, int row, int column) {
        Component com = super.getTableCellRendererComponent(jtable, o, isSeleted, bln1, row, column);
        btnContainer btnCont=new btnContainer(visibalValue);
        if (isSeleted==false && row%2==0) {
            btnCont.setBackground(Color.WHITE);
        }else{
            
            btnCont.setBackground(com.getBackground());
        }

        if (isSeleted==true) {
            btnCont.chageTextColour();
        }
        return btnCont;
    }
    
}
