package musikplayer3001;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JTable;

public class TabellenklickListener extends MouseAdapter {
	private JTable tabelle;
	private PlayerGUI playerGUI;
	
	public TabellenklickListener (JTable tabelle) {
		this.tabelle = tabelle;
	}
	
	@Override
	//Funktion zum Event von Mausklick auf Tabellenspalte im Playlist- oder Musikverwaltungsmodus
	public void mouseClicked(MouseEvent e) {
		int reihe = tabelle.rowAtPoint(e.getPoint());
		int spalte = tabelle.getColumnCount()-1;
		
		//Es wurde aus Musikverwaltung ausgewaehlt
		if (reihe >= 0 && spalte >= 0) {
			if (tabelle.getValueAt(reihe, spalte) instanceof Musikstueck) {
				Musikstueck stueck = (Musikstueck) tabelle.getValueAt(reihe, spalte);
				//Erstellt Player und GUI Instanz zum Abspielen
				Player p = new Player();
				//Setzt Funktionen für "Nächster" & "Vorheriger" Funktionen
				p.setAbspieldatenStueck(stueck);
				p.setAbspieldatenListe(Playlist.allePlaylist());
			    playerGUI = new PlayerGUI(p, stueck);
				playerGUI.createAndShowGUI();

				
			//Es wurde aus PlaylistVerwaltung-Tabele ausgewaehlt
			} else if (tabelle.getValueAt(reihe, spalte) instanceof Playlist) {
				//Erstellt neue Playlist und Arraylist
				Playlist playlist= (Playlist)tabelle.getValueAt(reihe, spalte);
				ArrayList<Musikstueck> liste = new ArrayList<>();
				//Funktioniert! Überführt Songs als Tabellenvalue in Playlist und dann in ArrayList
				for(int i = 0; i < playlist.getSize(); i++ ) {
					Musikstueck song = playlist.get(i);
					liste.add(song);
				}
				// Erstellt neue PlayerInstanz
				Player p = new Player();
				Musikstueck stueck = liste.get(0);
				// Setzt Funktionen für Nächster Funktion (übergabe abspielliste und aktuelles stück)
				p.setAbspieldatenListe(liste);
				p.setAbspieldatenStueck(stueck);
				// Öffnet Musikstück der Playlist
			    playerGUI = new PlayerGUI(p, stueck);
				playerGUI.createAndShowGUI();
			}
		}
	}
}	