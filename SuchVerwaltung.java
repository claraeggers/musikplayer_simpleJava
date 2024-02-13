package musikplayer3001;

import java.util.HashSet;
import java.util.Set;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public abstract class SuchVerwaltung {
	private static JTable neueTabelle;
	private static DefaultTableModel neuesModel;
	private static final Set<Integer> neueReihen = new HashSet<>();
	private static Playlist suchPlaylist = new Playlist("Suchliste");
	
	//Durchsucht übergebene Tabelle nach Texttreffern, erstellt dann neue Tabelle nur mit Treffern
	public static JTable sucheTabelle (JTable tabelle, String text) {
		DefaultTableModel model = (DefaultTableModel) tabelle.getModel();
		int reihenAnzahl = model.getRowCount();
		int spaltenAnzahl = model.getColumnCount();
		
		String[] top = new String[model.getColumnCount()];
		for (int c = 0; c < model.getColumnCount(); c++) {
			top[c] = model.getColumnName(c);
		}
		neuesModel = new DefaultTableModel (
				new Object[][] {},
				top);
		neueReihen.clear();
		
		for (int reihe = 0; reihe < reihenAnzahl; reihe++) {
			for (int spalte = 0; spalte < spaltenAnzahl-1; spalte++) {
				Object zelle = model.getValueAt(reihe, spalte);
				if (zelle != null && zelle.toString().toLowerCase().contains(text.toLowerCase()) && !neueReihen.contains(reihe)) {
					addReiheInTabelle(tabelle,reihe);
					neueReihen.add(reihe);
				}
			}
		}
		return neueTabelle;
	}
	//Funktion die Reihen in Tabelle hinzufuegt
	private static void addReiheInTabelle(JTable original, int reihe) {
		Object[] reihenDaten = new Object[original.getModel().getColumnCount()];
		for (int spalte = 0; spalte < original.getModel().getColumnCount(); spalte++) {
			reihenDaten[spalte] = original.getValueAt(reihe, spalte);
		}
		neuesModel.addRow(reihenDaten);
		suchPlaylist.addSong((Musikstueck) original.getValueAt(reihe, original.getColumnCount()-1));
		neueTabelle = new JTable(neuesModel);
	}
	
	public static JTable getSucheTabelle() {
		return neueTabelle;
	}
	
	public static DefaultTableModel getSucheModel() {
		return neuesModel;
	}
	
	public static Playlist getSuchPlaylist() {
		return suchPlaylist;
	}

}