package musikplayer3001;

import java.awt.Component;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JTable;

//Erstellt RadioButtons fuer die GUI
//Quelle: https://www.tutorialspoint.com/how-can-we-add-insert-a-jradiobutton-to-a-jtable-cell-in-java
public class RadioButtonEditor extends DefaultCellEditor implements ItemListener {
	private JRadioButton button;
	public RadioButtonEditor(JCheckBox checkBox) {
		super(checkBox);
	}
	public Component getTableCellEditorComponent(JTable tabelle, Object wert, boolean ausgewaehlt, int reihe, int spalte) {
		if (wert==null) return null;
			button = (JRadioButton)wert;
		    button.addItemListener(this);
		    return button;
		}
	public Object getCellEditorValue() {
		button.removeItemListener(this);
		return button;
	}
	@Override
	public void itemStateChanged(ItemEvent e) {
		super.fireEditingStopped();
	}
}