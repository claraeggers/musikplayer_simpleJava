package musikplayer3001;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

//Rendering fuer RadioButtons in GUI
//Quelle: https://www.tutorialspoint.com/how-can-we-add-insert-a-jradiobutton-to-a-jtable-cell-in-java
public class RadioButtonRenderer implements TableCellRenderer {

	@Override
	public Component getTableCellRendererComponent(JTable tabelle, Object wert, boolean isSelected, boolean hasFocus,
			int reihe, int spalte) {
		if (wert == null)
			return null;
		return (Component)wert;
	}

}