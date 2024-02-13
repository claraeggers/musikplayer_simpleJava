package musikplayer3001;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Arrays;

public class PlayerGUI {

    private Player player;
    private Musikstueck musikstueck;
    private JPanel playPausePanel;
    private CardLayout ppCard;
    private GridBagConstraints gbc;
    private JFrame mframe;
    private String imageDirectory = "Bilder";
    private Playlist playlist;
    private Playlist aktuellePlaylist;
    private Musikstueck aktuellesStueck;
    private Boolean closeWindow;
    

    public PlayerGUI(Player p, Musikstueck stueck) {
        this.player = p;
        musikstueck = stueck;
    }

	public void createAndShowGUI() {
		closeWindow=false;
        mframe = new JFrame("Music Player");
        mframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mframe.setLayout(new GridBagLayout());
        mframe.setSize(400, 420);
        mframe.setLocationRelativeTo(null);
        gbc = new GridBagConstraints();
        
        //Titel Constraints
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.CENTER;
        JPanel titelPanel = new JPanel();
        titelPanel.add(new JLabel(musikstueck.getTitle() + " by " + musikstueck.getArtist()));
        
        mframe.add(titelPanel,gbc);
        
        // Bild Constraints
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 4;
        gbc.gridheight = 3;
        gbc.fill = GridBagConstraints.BOTH;

        JPanel bildPanel = new JPanel(); 
        if (Arrays.asList(Verwaltung.getAllGenres()).contains(musikstueck.getGenre())) {
        	bildPanel.add(new JLabel(new ImageIcon(imageDirectory + File.separator + musikstueck.getGenre() + ".jpeg")));
        } else {
        	bildPanel.add(new JLabel(new ImageIcon(imageDirectory + File.separator + "placeholder.jpg")));
        }

        mframe.add(bildPanel, gbc);
        
        //Button Panel Constraints
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 4;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel buttonPanel = new JPanel();
        JButton vorheriger = new JButton("Zurück");

        playPausePanel = new JPanel();
        playPausePanel.setLayout(new CardLayout());
        
        JButton play = new JButton("Play");
        JButton pause = new JButton("Pause");
        JButton fortsetzen = new JButton("Fortsetzen");
        JButton stop = new JButton("Stop");
        JButton naechster = new JButton("Nächster");
        JButton previous = new JButton("Vorheriger");
        playPausePanel.add(play, "play");
        playPausePanel.add(pause, "pause");
        playPausePanel.add(fortsetzen, "fortsetzen");

        //Action Listener fuer Funktionalitaeten
        play.addActionListener(e -> playSong());
        pause.addActionListener(e -> pauseSong());
        fortsetzen.addActionListener(e -> fortsetzenSong());
        stop.addActionListener(e -> stopSong());
        vorheriger.addActionListener(e -> vorherigerSong());
        naechster.addActionListener(e -> naechsterSong());

    	buttonPanel.add(vorheriger);
        buttonPanel.add(playPausePanel);
        buttonPanel.add(stop);
        buttonPanel.add(naechster);

        mframe.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                player.stop();
                player.shutdown();
            }
        });

        mframe.add(buttonPanel, gbc);
        mframe.setVisible(true);
    }

	//Hauptfunktion zum Abspielen, wird von play() Fkt aus Player Klasse aufgerufen
    public void playSong() {
        player.play(musikstueck,this);
        ppCard = (CardLayout) playPausePanel.getLayout();
        ppCard.show(playPausePanel, "pause");

        // Laden des Bildes nach Genre des abgespielten Musikstuecks
        String genre = musikstueck.getGenre();
        String imagePath;
        if (Arrays.asList(Verwaltung.getAllGenres()).contains(musikstueck.getGenre())) {
        	imagePath = imageDirectory + File.separator + genre + ".jpeg";
        } else {
        	//Ersetzen durch Platzhalter falls kein passendes Bild gefunden wird
        	imagePath = imageDirectory + File.separator + "placeholder.jpg";
        }
        ImageIcon genreImage = new ImageIcon(imagePath);
        JLabel imageLabel = new JLabel(genreImage);

        // Loesche Platzhalter und ersetze durch Genre-Bild
        JPanel bildPanel = (JPanel) mframe.getContentPane().getComponent(1);
        bildPanel.removeAll();
        bildPanel.add(imageLabel);
        bildPanel.revalidate();
        bildPanel.repaint();
    }

    //Ruft pausieren() Funktion auf und aendert GUI Darstellung des Buttons
    public void pauseSong() {
        if (player.pausieren()) {
            ppCard = (CardLayout) playPausePanel.getLayout();
            ppCard.show(playPausePanel, "fortsetzen");
        }
    }

    //Ruft fortsetzen() Funktion auf und aendert GUI Darstellung des Buttons
    public void fortsetzenSong() {
        player.fortsetzen();
        ppCard = (CardLayout) playPausePanel.getLayout();
        ppCard.show(playPausePanel, "pause");
    }

    //Ruft stop() Funktion auf und aendert GUI Darstellung des Buttons
    public void stopSong() {
        player.stop();
        ppCard = (CardLayout) playPausePanel.getLayout();
        ppCard.show(playPausePanel, "play");
    }
    
    //Ruft naechsterSong() Funktion auf und schliesst vorher laufendes Player-Fenster
    public void naechsterSong() {
    	closeWindow();
    	player.naechsterSong();
    }
   
    public void closeWindow() {
		mframe.dispose();		
    }
    //Ruft vorherigerSong() Funktion auf und schliesst vorher laufendes Player-Fenster   
    public void vorherigerSong() {
    	closeWindow();
    	player.vorherigerSong();
    }

}