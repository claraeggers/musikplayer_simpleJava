package musikplayer3001;

import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public abstract class LoescheVerwaltung {
	private static JTable loescheTabelle;
	private static DefaultTableModel loescheModel;
	private static ArrayList knoepfe;
	
	//erstellt Tabelle mit Auswahlknöpfen für GUI
	public static void erstelleTabelle (JTable tabelle) {
		int reihen = tabelle.getRowCount();
		int spalten = tabelle.getColumnCount();
		Object[][] songdaten = new Object[reihen][spalten + 1];

		for (int reihe = 0; reihe < reihen; reihe++) {
			for (int spalte = 0; spalte < spalten; spalte++) {
				songdaten[reihe][spalte] = tabelle.getValueAt(reihe, spalte);
			}
			songdaten[reihe][spalten] = new JRadioButton();

		}

		String[] spaltenname = new String[spalten + 1];
		for (int spalte = 0; spalte < spalten; spalte++) {
			spaltenname[spalte] = tabelle.getColumnName(spalte);
		}
		spaltenname[spalten] = "Löschen?";

		loescheModel = new DefaultTableModel(songdaten, spaltenname);
		knoepfe = new ArrayList<>();
		for (int reihe = 0; reihe < reihen; reihe++) {
			knoepfe.add((JRadioButton) loescheModel.getValueAt(reihe, spalten));
		}
		loescheTabelle = new JTable(loescheModel);
		loescheTabelle.getColumn("Löschen?").setCellRenderer(new RadioButtonRenderer());
		loescheTabelle.getColumn("Löschen?").setCellEditor(new RadioButtonEditor(new JCheckBox()));
	}
	
	//Funktion zum Löschen von ausgewählten Reihen
	public static void loescheReihen(JTable tabelle) {
		int spaltenZahl = tabelle.getModel().getColumnCount();
		int reihenZahl = tabelle.getModel().getRowCount();
		for (int reihe = 0; reihe < reihenZahl; reihe++) {
			JRadioButton rb = (JRadioButton) tabelle.getValueAt(reihe, spaltenZahl-1);
			if (rb != null && rb.isSelected() && tabelle.getValueAt(reihe, spaltenZahl-2) instanceof Musikstueck) {
				Verwaltung.loescheElement(tabelle.getValueAt(reihe, spaltenZahl-2));
				Einlesen.loescheSong((String)tabelle.getValueAt(reihe, 0), (String)tabelle.getValueAt(reihe, 1), "Musik/CSV Dateien/Musik.csv");
			} else if (rb != null && rb.isSelected() && tabelle.getValueAt(reihe, spaltenZahl-2) instanceof Playlist) {
				Verwaltung.loescheElement(tabelle.getValueAt(reihe, spaltenZahl-2));
				Einlesen.loeschePlaylist((Playlist)tabelle.getValueAt(reihe, spaltenZahl-2), "Musik/CSV Dateien/Playlists.csv");
			}
		}
	}
	
	public static JTable getDeleteTable() {
		return loescheTabelle;
	}
}