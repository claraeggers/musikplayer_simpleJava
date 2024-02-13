package musikplayer3001;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;

public class SuchtabellenklickListener extends MouseAdapter {
	private JTable tabelle;
	private PlayerGUI playerGUI;
	
	public SuchtabellenklickListener (JTable tabelle) {
		this.tabelle = tabelle;
	}
	
	@Override
	//Funktion zum Event von Mausklick auf Tabellenspalte im Playlist- oder Musikverwaltungsmodus
	public void mouseClicked(MouseEvent e) {
		int reihe = tabelle.rowAtPoint(e.getPoint());
		int spalte = tabelle.getColumnCount()-1;
		
		if (reihe >= 0 && spalte >= 0) {
			//Wählt aus Musikverwaltung aus
			Musikstueck stueck = (Musikstueck) tabelle.getValueAt(reihe, spalte);
			//Erstellt Player und GUI Instanz zum Abspielen
			Player p = new Player();
			//Setzt Funktionen für "Nächster" & "Vorheriger" Funktionen
			p.setAbspieldatenStueck(stueck);
			Playlist list = SuchVerwaltung.getSuchPlaylist();
			p.setAbspieldatenListe(list.playList);
			playerGUI = new PlayerGUI(p, stueck);
			playerGUI.createAndShowGUI();
		}
	}
}

