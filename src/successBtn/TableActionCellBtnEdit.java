/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package successBtn;

import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;

import EditDelete.cell.TableActionEvent;

/**
 *
 * @author Akila
 */
public class TableActionCellBtnEdit extends DefaultCellEditor{
    
    private TableBtnActionEvent event;
    private boolean visibalValue;

    public TableActionCellBtnEdit(TableBtnActionEvent event,boolean visibility) {
        super(new JCheckBox());
        this.event=event;
        visibalValue=visibility;
    }
    
    
    

    @Override
    public Component getTableCellEditorComponent(JTable jtable, Object o, boolean bln, int row, int column) {
        btnContainer btncont=new btnContainer(visibalValue);
        btncont.initEvents(event, row);
        btncont.setBackground(jtable.getSelectionBackground());
        return btncont;
    }
    
}
