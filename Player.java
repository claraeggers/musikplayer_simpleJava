package musikplayer3001;

import javax.sound.sampled.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Player {

    private AudioInputStream audioInputStream;
    private SourceDataLine line;
    private ExecutorService executor;
    private long pausedPosition = 0;
    private boolean istPausiert = false;
    private boolean manuellGestoppt = false;
    private Playlist playlist;
    private Musikstueck musikstueck;

    private ArrayList<Musikstueck> aktuellePlaylist;
    private Musikstueck aktuellesStueck;
    private PlayerGUI gui;
    public Player() {
        executor = Executors.newSingleThreadExecutor();
    }

    //Play Funktion zum Audio-Abspielen von .wav.Datein, oeffnet GUI
    public void play(Musikstueck musikstueck, PlayerGUI gui) {
    	this.gui = gui;
    	this.manuellGestoppt = false;
        executor.execute(() -> {
            try {
                File audioFile = musikstueck.getAudioURL();
                String audioFilePath = audioFile.getAbsolutePath();
                audioInputStream = AudioSystem.getAudioInputStream(new File(audioFilePath));
                AudioFormat audioFormat = audioInputStream.getFormat();
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);

                line = (SourceDataLine) AudioSystem.getLine(info);
                line.open(audioFormat);
                line.start();

                LineListener lineListener = event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                    	if (!this.manuellGestoppt) {
                    		automatischesAbspielen();
                    	}
                    }
                };
                line.addLineListener(lineListener);

                int bufferSize = 4096;
                byte[] data = new byte[bufferSize];
                int bytesRead;

                while ((bytesRead = audioInputStream.read(data, 0, data.length)) != -1) {
                    if (!istPausiert) {
                        line.write(data, 0, bytesRead);
                    } else {
                        try {
                            //Verzoegerung gegen Ueberlastung 
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                line.drain();
                line.stop();
                line.close();

            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException exception) {
                exception.printStackTrace();
            }
        });
    }

    // Funktion stoppt wiedergabe
    public void stop() {
        if (line != null && line.isOpen()) {
            this.manuellGestoppt = true;
            line.stop();
            line.close();
        }
    }

    //Funktion gibt zurueck, ob Musikstueck abgespielt wird
    public boolean isPlaying() {
        return line != null && line.isOpen();
    }

    // Beendet Wiedergabemodus
    public void shutdown() {
        executor.shutdown();
    }

    //Naechster Song wird abgespielt nach beendetem Lied
    private void automatischesAbspielen() {
    	gui.closeWindow();
    	naechsterSong();
    }
    //Pause Funktion fuer Unterbrechungen beim Anhoeren
    public boolean pausieren() {
        if (isPlaying() && !istPausiert) {
        	this.manuellGestoppt = true;
            line.stop();
            pausedPosition = line.getLongFramePosition();
            istPausiert = true;
        }
        return istPausiert;
    }
    //Fortsetzen der Wiedergabe nach Pause
    public void fortsetzen() {
        if (istPausiert) {
        	this.manuellGestoppt = false;
            line.start();
            istPausiert = false;
        }
    }
    
    //Funktion um einen nächsten Song abzuspielen
    public void naechsterSong() {
    	//Getted das aktuelle Stueck und die aktuelle Playlist
        Musikstueck stueck = getAbspieldatenStueck();
        ArrayList<Musikstueck> liste = getAbspieldatenListe();
        Musikstueck naechstes;
        //Hält die aktuelle Wiedergabe des Titels an
        if (line != null && line.isOpen()) {
        	stop();
        }
        //Index des musikstueckes wird herausgefunden
        int index = -1;
        for (int i = 0; i<liste.size(); i++) {
        	if (liste.get(i).getTitle().equals(stueck.getTitle()) && liste.get(i).getArtist().equals(stueck.getArtist()))
        		index = i;
        }
        //nachseste Musikstueck mit index+1 zum weiteren Abspielen
        if (index+1 >= liste.size()) {
        	naechstes = liste.get(0);
        } else {
        	naechstes = liste.get(index + 1);
        }
        PlayerGUI pg = new PlayerGUI(this,naechstes);
        this.gui = pg;
        setAbspieldatenStueck(naechstes);
        setAbspieldatenListe(liste);
        this.gui.createAndShowGUI();
        this.gui.playSong();       
    } 
    

    	
 // Springt ein Lied zurueck und spielt vorherigen Song ab   
    public void vorherigerSong() {
    	//Liest aktuelle Abspieldaten ein
    	Musikstueck stueck = getAbspieldatenStueck();
    	ArrayList<Musikstueck> list = getAbspieldatenListe();
    	Musikstueck vorheriges;
    	
    	//unterbricht wiedergabe
    	stop();

    	//Waehlt aus Playlist vorheriges Musikstueck aus
    	int index = -1;
        for (int i = 0; i<list.size(); i++) {
        	if (list.get(i).getTitle().equals(stueck.getTitle()) && list.get(i).getArtist().equals(stueck.getArtist()))
        		index = i;
        }
        if (index-1 < 0) {
        	vorheriges = list.get(list.size()-1);
        } else {
        	vorheriges = list.get(index-1);
        }
        //Oeffnet Abspielmodus und setzt neues Musikstueck als aktuelle Abspieldaten
        PlayerGUI pg = new PlayerGUI(this,vorheriges);
        this.gui = pg;
        setAbspieldatenStueck(vorheriges);
        setAbspieldatenListe(list);
        this.gui.createAndShowGUI();
        this.gui.playSong();
    }
    
    //Aufgerufen durch TableClickListener, Setzt einen Array der die Playlist beschreibt, die gerade abgespielt wird
    // null bei Abspielen von Musikstueck, Arraylist bei Abspielen von Playlist
    public void setAbspieldatenListe(ArrayList<Musikstueck> liste) {
        this.aktuellePlaylist = liste;
    }
    // Getter für die "Nächster" Funktion
    public ArrayList<Musikstueck> getAbspieldatenListe() {
    	if (aktuellePlaylist == null) {
    		return null;
    	}
    	else {
    	return aktuellePlaylist;		
    	}

    }
    
    //TableClickListener Setzt das aktuell abgespielte Musikstueck, wird in "Nächster" Funktion mit nächstem abgespielten Song überschrieben
    public void setAbspieldatenStueck(Musikstueck aktuellesStueck) {   
    	this.aktuellesStueck = aktuellesStueck;
    }
    // Getter für "Nächster" Funktion
    public Musikstueck getAbspieldatenStueck() {
    	return aktuellesStueck;
    }
    
    public void setManuellGestoppt(boolean manuellGestoppt) {
    	this.manuellGestoppt = manuellGestoppt;
    }
    
    public boolean getManuellGestoppt() {
    	return manuellGestoppt;
    }
    
    
}