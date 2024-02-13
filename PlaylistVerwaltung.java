package musikplayer3001;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public abstract class PlaylistVerwaltung {
	private static JTable tabelleUmbenennen;
	private static DefaultTableModel modellUmbenennen;
	private static ButtonGroup buttons;
	
	// Funktion zur manuellen Erstellung von PLaylists nach in GUI ausgewaehlten Kriterien
	public static void createPlaylist(String typ, String spezifika) {
		Playlist playlist = new Playlist("Playlist "+typ+" "+spezifika);
		
		switch (typ) {
		case "Interpret":
			playlist.playlistNachInterpret(spezifika);
			break;
		case "Genre":
			playlist.playlistNachGenre(spezifika);
			break;
		case "Album":
			playlist.playlistNachAlbum(spezifika);
			break; 
		default:
			break;
		}
		Verwaltung.addPlaylist(playlist);
		Einlesen.hinzufuegenPlaylist(playlist, "Musik/CSV Dateien/Playlists.csv");
	}
	
	//Erstellt Tabelle fuer GUI
	public static void createTable (JTable tabelle) {
		int reihen = tabelle.getRowCount();
		int spalten = tabelle.getColumnCount();
		Object[][] daten = new Object[reihen][spalten+1];

		for (int reihe = 0; reihe < reihen; reihe++) {
			for (int spalte = 0; spalte < spalten; spalte++) {
				daten[reihe][spalte] = tabelle.getValueAt(reihe, spalte);
			}
			daten[reihe][spalten] = new JRadioButton();
		}
		
		String[] spaltenNamen = new String[spalten+1];
		for (int spalte = 0; spalte < spalten; spalte++) {
			spaltenNamen[spalte] = tabelle.getColumnName(spalte);
		}
		spaltenNamen[spalten] = "Umbenennen?";
		
		modellUmbenennen = new DefaultTableModel(daten,spaltenNamen);
		buttons = new ButtonGroup();
		for (int reihe = 0; reihe < reihen; reihe++) {
			buttons.add((JRadioButton)modellUmbenennen.getValueAt(reihe, spalten));
		}
		tabelleUmbenennen = new JTable(modellUmbenennen);
		tabelleUmbenennen.getColumn("Umbenennen?").setCellRenderer(new RadioButtonRenderer());
		tabelleUmbenennen.getColumn("Umbenennen?").setCellEditor(new RadioButtonEditor(new JCheckBox()));
	}
	//FUnktion zum Umbenennen in Tabelle und in Datenbank
	public static void renamePlaylist(JTable tabelle) {
		int spaltenAnzahl = tabelle.getModel().getColumnCount();
		int reihenAnzahl = tabelle.getModel().getRowCount();
		for (int reihe = 0; reihe < reihenAnzahl; reihe++) {
			JRadioButton rb = (JRadioButton) tabelle.getValueAt(reihe, spaltenAnzahl-1);
			if (rb != null && rb.isSelected()) {
				String name = JOptionPane.showInputDialog(null,"Bitte neuen Namen eingeben",null,JOptionPane.PLAIN_MESSAGE);
				Playlist p = (Playlist)tabelle.getValueAt(reihe, spaltenAnzahl-2);
				if (name == null) {
					return;
				}
				if (!name.isEmpty()) {
					Einlesen.playlistUmbenennen(p, name, "Musik/CSV Dateien/Playlists.csv");
					p.setName(name);
				} else {
					JOptionPane.showMessageDialog(null, "Keinen leeren Namen eingeben!");
				}
			}
		}
	}
	
	public static JTable getRenameTable() {
		return tabelleUmbenennen;
	}

	//Gibt ArrayList von Musikstueck-Objekten einer Playlist zurueck
    public static List<Musikstueck> getSongsInPlaylist(String playlistName) {
        List<Musikstueck> songsInPlaylist = new ArrayList<>();
        
        for (Playlist playlist : Verwaltung.getPlaylists()) {
            if (playlist.getName().equalsIgnoreCase(playlistName)) {
                songsInPlaylist.addAll(playlist.getPlayList());
                break; 
            }
        }
        
        return songsInPlaylist;
    }
    
    

}