package musikplayer3001;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.table.TableColumn;
import javax.swing.JTextField;
import javax.swing.JFileChooser;


public class MusikGUI extends JFrame {
	
	private GridBagConstraints gbc;
	//Sidebar
	private JPanel mainSidebar;
	private JPanel musikSidebar;
	private JPanel playlistSidebar;
	//Central Panels
	private JPanel centrePanel;
	private JTable musikTabelle;
	private JTable playlistTabelle;
	private DefaultTableModel musikModel;
	private DefaultTableModel playlistModel;
	private DefaultTableModel suchModel;
	//external Tables
	private JTable loescheTabelle;
	private JTable sucheTabelle;
	private JTable umbenennungTabelle;
	
	private int sichtbareCard = 1;
	private String suchtext;
	
    public MusikGUI() {
    	JFrame frame = new JFrame("Musicplayer 3001 Ultra FX 2");
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setSize(1000, 600);
    	JPanel mainPanel = new JPanel();
    	mainPanel.setLayout(new GridBagLayout());
    	mainPanel.setBackground(Color.black);
    	gbc = new GridBagConstraints();
    	
    	//Header
    	gbc.gridx = 0;
    	gbc.gridy = 0;
    	gbc.gridwidth = 5;
    	gbc.gridheight = 1;
    	gbc.fill = GridBagConstraints.HORIZONTAL;
    	mainPanel.add(createHeader(), gbc);
    	
    	//Sidebar
    	gbc.gridx = 0;
    	gbc.gridy = 1;
    	gbc.gridheight = 4;
    	gbc.gridwidth = 2;
    	gbc.fill = GridBagConstraints.VERTICAL;
    	createMainSidebar();
    	mainPanel.add(mainSidebar, gbc);
    	
    	//Centre
    	gbc.gridx = 2;
    	gbc.gridy = 1;
    	gbc.gridwidth = 3;
    	gbc.gridheight = 3;
    	gbc.fill = GridBagConstraints.BOTH;
    	gbc.weightx = 1.0;
    	gbc.weighty = 1.0;
    	createCentralPanel();
    	mainPanel.add(centrePanel, gbc);
    	
    	frame.add(mainPanel);
    	frame.pack();
    	frame.setLocationRelativeTo(null);
    	frame.setVisible(true);
    }
    
    //Erstellt zentrales Panel als CardLayout, so dass zwischen den verschiedenen Tabellen gewechselt werden kann
	private void createCentralPanel() {
		centrePanel = new JPanel();
		centrePanel.setLayout(new CardLayout());
		centrePanel.setBackground(Color.blue);
		createMusiktabelle();
		createPlaylisttabelle();
		sucheTabelle = new JTable(suchModel);
		centrePanel.add(new JScrollPane(musikTabelle),"Musiktabelle");
		centrePanel.add(new JScrollPane(playlistTabelle),"Playlisttabelle");
		centrePanel.add(new JScrollPane(sucheTabelle),"Suchtabelle");
		musikTabelle.addMouseListener(new TabellenklickListener(musikTabelle));
		playlistTabelle.addMouseListener(new TabellenklickListener(playlistTabelle));
	}
	
	//Erstellt initiale Musiktabelle zur Darstellung im zentralen Panel, liest nötige Informationen aus Array in Verwaltung
	//Spalte mit Musikstueck Objekten wird versteckt, benötigt zum Auslesen des Stücks	
	private void createMusiktabelle() {
		Object[][] stuecke = Verwaltung.getAllMusikstuecke();
    	musikModel = new DefaultTableModel(
    			stuecke,
    			new String[] {"Titel","Interpret","Genre","Album","Hidden"}
    		);
    	musikTabelle = new JTable(musikModel);
    	spalteVerstecken(musikTabelle,1);
	}
	
	//Erstellt initiale Playlisttabelle parallel zur Erstellung der Musiktabelle
	//Spalte mit Playlist Objekten wird versteckt, benötigt zum Auslesen der Playlist bei Klick
    private void createPlaylisttabelle() {
    	Object[][] playlists = Verwaltung.getAllPlaylists();
    	playlistModel = new DefaultTableModel(
    			playlists,
    			new String[] {"Name","Zahl Songs","Hidden"}
    		);
    	playlistTabelle = new JTable(playlistModel);
    	spalteVerstecken(playlistTabelle,1);
	}
    
    //Die Sidebar wird erstellt, mit dem CardLayout kann beim moduswechsel() Button zwischen den Modi geschaltet werden
	private void createMainSidebar() {
		mainSidebar = new JPanel();
		mainSidebar.setLayout(new CardLayout());
		createMusikSidebar();
		createPlaylistSidebar();
		mainSidebar.add(musikSidebar,"Musiksidebar");
		mainSidebar.add(playlistSidebar,"Playlistsidebar");
	}
    
	//Erstellt die (klickbaren) Label für den Musikverwaltungsmodus
	//@musiText sorgt dafür, dass die Ansicht im CardLayout wieder auf die Musiktabelle gesetzt wird, falls sie vorher im Suchmodus war
	//@suchText sorgt dafür, dass nach der Sucheingabe eine Tabelle mit den Suchergebnissen im zentralen Panel auftaucht
	private void createMusikSidebar() {
		musikSidebar = new JPanel();
		musikSidebar.setLayout(new GridLayout(4,2));
		musikSidebar.setBackground(Color.BLACK);
        JLabel musikIMG = new JLabel(new ImageIcon("Bilder/Musiknote.jpg"));
        musikSidebar.add(musikIMG);
        JLabel musikText = new JLabel("Musik");
        musikText.setForeground(Color.white);
        musikText.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        musikText.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked (MouseEvent e) {
        		CardLayout zentraleCard = (CardLayout) centrePanel.getLayout();
        		zentraleCard.show(centrePanel, "Musiktabelle");
        		sichtbareCard = 1;
        	}
        });
        musikSidebar.add(musikText);
        
        JLabel suchIMG = new JLabel(new ImageIcon("Bilder/Lupe.jpg"));
        musikSidebar.add(suchIMG);
        JLabel suchText = new JLabel("Stück suchen");
        suchText.setForeground(Color.white);
        suchText.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        suchText.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked (MouseEvent e) {
        		searchDialog();
        		CardLayout zentraleCard = (CardLayout) centrePanel.getLayout();
        		zentraleCard.show(centrePanel, "Suchtabelle");
        		sichtbareCard = 3;
        	}
        });
        musikSidebar.add(suchText);
        
        JLabel addIMG = new JLabel(new ImageIcon("Bilder/Plus.jpg"));
        musikSidebar.add(addIMG);
        JLabel addText = new JLabel("Song hinzufügen");
        addText.setForeground(Color.white);
        addText.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addText.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked (MouseEvent e) {
        		hinzufuegenDialog();
        	}
        });
        musikSidebar.add(addText);
        
        JLabel loeschIMG = new JLabel(new ImageIcon("Bilder/MusiknoteDelete.jpg"));
        musikSidebar.add(loeschIMG);
        JLabel loeschText = new JLabel("Stück löschen");
        loeschText.setForeground(Color.white);
        loeschText.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loeschText.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked (MouseEvent e) {
        		loeschenDialog();
        	}
        });
        musikSidebar.add(loeschText);
	}
	
	//Methode erstellt Sidebar für Playlistmodus mit allen Funktionen
	private void createPlaylistSidebar() {
		playlistSidebar = new JPanel();
		playlistSidebar.setLayout(new GridLayout(4,2));
		playlistSidebar.setBackground(Color.BLACK);
		playlistSidebar.setPreferredSize(new Dimension(150, 0));
        JLabel playlistIMG = new JLabel(new ImageIcon("Bilder/Playlist.jpg"));
        playlistSidebar.add(playlistIMG);
        JLabel playlistText = new JLabel("Playlists");
        playlistText.setForeground(Color.white);
        playlistText.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        playlistText.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked (MouseEvent e) {
        		playlistTabelleUpdaten();
        	}
        });
        playlistSidebar.add(playlistText);
        
        JLabel renameIMG = new JLabel(new ImageIcon("Bilder/PlaylistRename.jpg"));
        playlistSidebar.add(renameIMG);
        JLabel renameText = new JLabel("Umbenennen");
        renameText.setForeground(Color.white);
        renameText.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        renameText.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked (MouseEvent e) {
        		playlistUmbenennen();
        	}
        });
        playlistSidebar.add(renameText);
        
        JLabel addIMG = new JLabel(new ImageIcon("Bilder/PlaylistPlus.jpg"));
        playlistSidebar.add(addIMG);
        JLabel addText = new JLabel("Playlist erstellen");
        addText.setForeground(Color.white);
        addText.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addText.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked (MouseEvent e) {
        		playlistDialog();
        	}
        });
        playlistSidebar.add(addText);
        
        JLabel loeschIMG = new JLabel(new ImageIcon("Bilder/PlaylistDelete.jpg"));
        playlistSidebar.add(loeschIMG);
        JLabel loeschText = new JLabel("Playlist löschen");
        loeschText.setForeground(Color.white);
        loeschText.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loeschText.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked (MouseEvent e) {
        		loeschenDialog();
        	}
        });
        playlistSidebar.add(loeschText);
	}
	
	//Methode erstellt den Header, der den Button zum Moduswechsel beinhaltet
	private Component createHeader() {
    	JPanel panelHeader = new JPanel();
    	panelHeader.setLayout(new GridLayout(1,5));
    	panelHeader.setBackground(Color.black);
    	JToggleButton toggleButton = new JToggleButton("Zu Playlistmodus wechseln");
    	toggleButton.addChangeListener(new ChangeListener() {
    		@Override
    		public void stateChanged(ChangeEvent e) {
    			if (toggleButton.isSelected())
    				toggleButton.setText("Zu Musikmodus wechseln");
    			else
    				toggleButton.setText("Zu Playlistmodus wechseln");
    		}
    	});
    	panelHeader.add(toggleButton);
    	toggleButton.addActionListener(e -> moduswechsel(toggleButton));
    	return panelHeader;
	}
	
	//Methode zum Wechsel zwischen Playlist- und Musikmodus, wechselt zwischen Karten des CardLayouts in der MainSidbar und CentrePanel
	private void moduswechsel(JToggleButton button) {
		CardLayout sideCard = (CardLayout) mainSidebar.getLayout();
		CardLayout centreCard = (CardLayout) centrePanel.getLayout();
		if (button.isSelected()) {
			sideCard.show(mainSidebar,"Playlistsidebar");
			centreCard.show(centrePanel, "Playlisttabelle");
			sichtbareCard = 2;
		}
		else {
			sideCard.show(mainSidebar,"Musiksidebar");
			centreCard.show(centrePanel, "Musiktabelle");
			sichtbareCard = 1;
		}
	}
	
	//Methode zum Aufruf der Suche, erzeugt neues Fenster zum Suchen und dann neue Tabelle mit Suchergebnissen
	private void searchDialog() {
		JTextField searchField = new JTextField(20);
		
		JPanel panel = new JPanel();
		panel.add(new JLabel("Bitte Suche eingeben:"));
		panel.add(searchField);
		JOptionPane.showOptionDialog(
				centrePanel,
				panel,
				"Stücksuche",
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE,
				null,
				new Object[] {"Suchen"},
				"Suchen"
		);
		suchtext = searchField.getText();
		SuchVerwaltung.sucheTabelle(musikTabelle, suchtext);
		sucheTabelle.setModel(SuchVerwaltung.getSucheModel());
		sucheTabelle.addMouseListener(new SuchtabellenklickListener(sucheTabelle));
		spalteVerstecken(sucheTabelle,1);
	}
	
	//Dialogfenster zum Erstellen von neuen Playlists. Enthält alle vorhandenen Interpreten, Genres und Albennamen und lässt nach diesen eine Playlist erstellen
	private void playlistDialog() {
		String[] types = {"Interpret","Genre","Album"};
		JComboBox<String> typeBox = new JComboBox<>(types);
		JComboBox<String> specificBox = new JComboBox<>(Verwaltung.getAllArtists());
		
		typeBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedCategory = (String) typeBox.getSelectedItem();
				
				switch (selectedCategory) {
				case "Interpret":
					specificBox.setModel(new DefaultComboBoxModel<>(Verwaltung.getAllArtists()));
					break;
				case "Genre":
					specificBox.setModel(new DefaultComboBoxModel<>(Verwaltung.getAllGenres()));
					break;
				case "Album":
					specificBox.setModel(new DefaultComboBoxModel<>(Verwaltung.getAllAlbums()));
					break;
				default:
					specificBox.setModel(new DefaultComboBoxModel<>(Verwaltung.getAllArtists()));
					break;
				}
			}
		});
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(4,1));
		panel.add(new JLabel("Nach welchem Kriterium soll eine Playlist erstellt werden?"));
		panel.add(typeBox);
		panel.add(new JLabel("Spezifisches Kriterium auswählen:"));
		panel.add(specificBox);
		
		int closed = JOptionPane.showOptionDialog(
					centrePanel,
					panel, 
					"Playlisterstellung", 
					JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.PLAIN_MESSAGE,
					null,
					new Object[] {"Erstellen"},
					"Erstellen"
					);
		if (closed != JOptionPane.CLOSED_OPTION) {
			String typeChoice = (String) typeBox.getSelectedItem();
			String specificChoice = (String) specificBox.getSelectedItem();
			PlaylistVerwaltung.createPlaylist(typeChoice, specificChoice);
			playlistTabelleUpdaten();
		} else {
			return;
		}
	}
	
	//Dialogfenster zum Löschen von Objekten aus aktuel angezeigter Tabelle. Erstellt über LoescheVerwaltung neue Tabelle mit Buttons zur Auswahl zu löschender Objekte
	private void loeschenDialog() {
		JPanel panel = new JPanel();
		panel.add(new JLabel("Bitte zu löschende Elemente auswählen"));
		switch(sichtbareCard) {
		case 1:
			LoescheVerwaltung.erstelleTabelle(musikTabelle);
			break;
		case 2: 
			LoescheVerwaltung.erstelleTabelle(playlistTabelle);
			break;
		case 3:
			LoescheVerwaltung.erstelleTabelle(sucheTabelle);
			break;
		default:
			break;
		}
		loescheTabelle = LoescheVerwaltung.getDeleteTable();
		panel.add(new JScrollPane(loescheTabelle));
		spalteVerstecken(loescheTabelle,2);
		JOptionPane.showOptionDialog(
				centrePanel,
				panel,
				"Stücke löschen",
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE,
				null,
				new Object[] {"Löschen"},
				"Löschen"
				);
		LoescheVerwaltung.loescheReihen(loescheTabelle);
		switch(sichtbareCard) {
		case 1:
			musikTabelleUpdaten();
	    	break;
		case 2:
			playlistTabelleUpdaten();
	    	break;
		case 3:
			musikTabelleUpdaten();
			suchTabelleUpdaten();
	    	break;
		default:
			break;
		}
	}
	
	//Dialogfenster zum Hinzufügen neuer Stücke
	//Erst wird Datei ausgewählt, dann entsprechende Dateneingabe gefordert und alles an Einlesen übergeben
	private void hinzufuegenDialog() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("WAV Dateien","wav");
		chooser.setFileFilter(filter);
		int rueckgabewert = chooser.showOpenDialog(centrePanel);
		String audioURL;
		if (chooser.getSelectedFile() != null) {
			audioURL = chooser.getSelectedFile().getAbsolutePath();
		} else {
			return;
		}
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(8,1));
		panel.add(new JLabel("Songtitel: "));
		JTextField feldTitel = new JTextField(30);
		feldTitel.setText(chooser.getSelectedFile().getName().replaceFirst("[.][^.]+$", ""));
		panel.add(feldTitel);
		panel.add(new JLabel("Interpret (Auswahl oder Eingabe): "));
		JComboBox<String> boxInterpreten = new JComboBox<String>(Verwaltung.getAllArtists());
		boxInterpreten.setEditable(true);
		panel.add(boxInterpreten);
		panel.add(new JLabel("Genre (Auswahl oder Eingabe): "));
		JComboBox<String> boxGenres = new JComboBox<String>(Verwaltung.getAllGenres());
		boxGenres.setEditable(true);
		panel.add(boxGenres);
		panel.add(new JLabel("Album (Auswahl oder Eingabe): "));
		JComboBox<String> boxAlben = new JComboBox<String>(Verwaltung.getAllAlbums());
		boxAlben.setEditable(true);
		panel.add(boxAlben);
		
		int ergebnis = JOptionPane.showConfirmDialog(
							centrePanel,
							panel,
							"Bitte Songdaten angeben",
							JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.PLAIN_MESSAGE
						);
		if (ergebnis == JOptionPane.OK_OPTION) {
			Verwaltung.erstelleNeuesStueck(feldTitel.getText(),(String)boxInterpreten.getSelectedItem(),(String)boxGenres.getSelectedItem(),(String)boxAlben.getSelectedItem(),new File(audioURL));
			musikTabelleUpdaten();
		}
	}
	
	//Methode zur Umbenennung einer Playlist
	private void playlistUmbenennen() {
		PlaylistVerwaltung.createTable(playlistTabelle);
		JPanel panel = new JPanel();
		umbenennungTabelle = spalteVerstecken(PlaylistVerwaltung.getRenameTable(),2);
		panel.add(new JScrollPane(umbenennungTabelle));
		JOptionPane.showMessageDialog(centrePanel, panel);
		PlaylistVerwaltung.renamePlaylist(umbenennungTabelle);
		playlistTabelleUpdaten();
	}
	
	//Methode, die eine entsprechende Spalte einer JTable versteckt.
	//@int abzug bestimmt, die wievielte Spalte versteckt werden muss, da durch die Button Spalte in manchen Tabellen eine Verallgemeinerung nicht möglich ist
	private JTable spalteVerstecken(JTable table, int abzug) {
		TableColumn hidden = table.getColumnModel().getColumn(table.getModel().getColumnCount()-abzug);
		hidden.setMinWidth(0);
		hidden.setMaxWidth(0);
		hidden.setWidth(0);
		table.setAutoCreateRowSorter(true);
		table.revalidate();
		table.repaint();
		return table;
	}
	
	//3 Methoden zur Erneuerung der Tabellen nach einer Änderung
	
	private void playlistTabelleUpdaten() {
		Object[][] playlists = Verwaltung.getAllPlaylists();
		playlistModel = new DefaultTableModel(
				playlists,
				new String[] {"Name","Zahl Songs","Hidden"}
				);
		playlistTabelle.setModel(playlistModel);
		spalteVerstecken(playlistTabelle,1);
	}
	
	private void musikTabelleUpdaten() {
		Object[][] stuecke = Verwaltung.getAllMusikstuecke();
		musikModel = new DefaultTableModel(
    			stuecke,
    			new String[] {"Titel","Interpret","Genre","Album","Hidden"}
    		);
    	musikTabelle.setModel(musikModel);
    	spalteVerstecken(musikTabelle,1);
	}
	
	private void suchTabelleUpdaten() {
		SuchVerwaltung.sucheTabelle(musikTabelle, suchtext);
		sucheTabelle.setModel(SuchVerwaltung.getSucheModel());
		sucheTabelle.addMouseListener(new SuchtabellenklickListener(sucheTabelle));
		spalteVerstecken(sucheTabelle,1);
	}
}