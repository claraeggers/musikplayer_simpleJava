package musikplayer3001;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.LineNumberReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.Charset;
import java.io.File;
import java.io.FileNotFoundException;

public class Einlesen {
	
	//Funktion zum Dateneinlesen der Musikverwaltungs-Datenbank
	public static Playlist ladeMusik () {
		
        String dateiPfad = "Musik//CSV Dateien/Musik.csv";
        List<String> songDaten = new ArrayList<>();
        
        try {
            songDaten = extrahiereDaten(dateiPfad);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
        try(LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(new File(dateiPfad)))) {
          	lineNumberReader.skip(Long.MAX_VALUE);
        } catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
               
        // Schleife liest Komponenten aus txt Datei aus und setzt MusikstueckAttribute
        Playlist p4 = new Playlist("Gesamtplaylist");
        for (int i = 0; i < songDaten.size() ; i++) {
        	String titel = songDaten.get(i);
        	String artist = songDaten.get(i+1);
        	String genre = songDaten.get(i+2);
        	String album = songDaten.get(i+3);
        	String filepath = songDaten.get(i+4);
        	i = i+4;
        	Verwaltung.addMusikstueck(new Musikstueck(titel,artist,genre,album,new File(filepath)));
        	p4.addSong(new Musikstueck(titel, artist, genre, album, new File(filepath)));
        }
        return p4;
    }
    
	//Einlesen der Datenbank fuer bestehende Playlists
	public static ArrayList<Playlist> allePlaylistsEinlesen() {

		String dateiPfad = "Musik/CSV Dateien/Playlists.csv";
		List<String> playlistDaten = new ArrayList<>();
		
		try {
			playlistDaten = extrahiereDaten(dateiPfad);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try(LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(new File(dateiPfad)))) {
			lineNumberReader.skip(Long.MAX_VALUE);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ArrayList<Playlist> playlistList = new ArrayList<>();
		for (int i = 0; i < playlistDaten.size(); i++) {
			String name = playlistDaten.get(i);
			Playlist p = new Playlist (name);
			i++;
			while (i < playlistDaten.size() && istIndex(playlistDaten.get(i))) {
				p.addSong(Verwaltung.getMusikstuecke().get(Integer.parseInt(playlistDaten.get(i))));
				i++;
			}
			i--;
			playlistList.add(p);
			Verwaltung.addPlaylist(p);
		}
		return playlistList;
	}
	// Funktion liest Daten in Array-Liste eines spezifischen Musikstuecks
    public static List<String> extrahiereDaten(String dateiPfad) throws IOException {
       
    	List<String> songDaten = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(dateiPfad))) {
            String zeile;
            while ((zeile = reader.readLine()) != null) {
                String[] abschnitte = zeile.split(",");
                for (String teil : abschnitte) {
                	// Trim loescht ueberfluessige Zeichen
                    songDaten.add(teil.trim()); 
                }
            }
        }
        
        return songDaten;
    }
    // Funktion loescht Zeile aus txt, die zu Loeschenden Song enthaelt
    public static void loescheSong(String titel, String artist, String filePath) {
    	//erstellt Zeilenliste in die alle bleibenden Zeilen reinkopiert werden
        List<String> zeilenliste = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String zeile;
            while ((zeile = reader.readLine()) != null) {
                String[] teil = zeile.split(",");
                String songTitel = teil[0].trim();
                String songArtist = teil[1].trim();

                // Zeile wird der Zeilenliste hinzugefügt, wenn sie nicht dem zu Loeschenden artist oder titel gleicht
                if (!songTitel.equals(titel) || !songArtist.equals(artist)) {
                    zeilenliste.add(zeile);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        // Zeilen werden in txt Dokument überschrieben, leere Zeilen sollen nicht entstehen 
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (int i = 0; i < zeilenliste.size(); i++) {
                writer.write(zeilenliste.get(i));
                if (i < zeilenliste.size() - 1) {
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    // Funktion extrahiert Song-Attribte aus Musikstueckobjekt und erzeugt string mit Kommata, String wird in Txt datei geschrieben
    public static void hinzufuegenSong(Musikstueck musikstueck, String filePath) {
        String songDaten = musikstueck.getTitle() + "," +
                          musikstueck.getArtist() + "," +
                          musikstueck.getGenre() + "," +
                          musikstueck.getAlbum() + "," +
                          musikstueck.getAudioURL()+ ",";

        // Schreibt String in txt datei
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
	    writer.newLine();
            writer.write(songDaten);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //Methode extrahiert aus Playlist Objekt String mit Namen und Songindexes getrennt durch Kommata, schreibt String in txt Datei
    public static void hinzufuegenPlaylist(Playlist playlist, String filePath) {
    	String playlistDaten = getPlaylistDaten(playlist);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
	    writer.newLine();
            writer.write(playlistDaten);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //Methode löscht ähnlich wie auch loescheSong(...) eine Playlist aus der txt
    public static void loeschePlaylist(Playlist playlist, String dateipfad) {
    	 List<String> zeilenliste = new ArrayList<>();

    	 List<String> playlistDaten = new ArrayList<>();
         try (BufferedReader reader = new BufferedReader(new FileReader(dateipfad))) {
             String zeile;
             String vergleich = getPlaylistDaten(playlist);
             while ((zeile = reader.readLine()) != null) {
                 if (!zeile.equals(vergleich)) {
                	 zeilenliste.add(zeile);
                 }
             }
         } catch (IOException e) {
             e.printStackTrace();
             return;
         }
         // Zeilen werden in txt Dokument überschrieben, leere Zeilen sollen nicht entstehen 
         try (BufferedWriter writer = new BufferedWriter(new FileWriter(dateipfad))) {
             for (int i = 0; i < zeilenliste.size(); i++) {
                 writer.write(zeilenliste.get(i));
                 if (i < zeilenliste.size() - 1) {
                     writer.newLine();
                 }
             }
         } catch (IOException e) {
             e.printStackTrace();
         }
    }
    
    // Benennt Playlist in Datenbank um
    public static void playlistUmbenennen(Playlist playlist,String neuerName, String dateipfad) {
    	Path pfad = Paths.get(dateipfad);
    	Charset charset = StandardCharsets.UTF_8;
    	String inhalt = null;
		try {
			inhalt = new String(Files.readAllBytes(pfad));
		} catch (IOException e) {
			e.printStackTrace();
		}
    	inhalt = inhalt.replaceAll(playlist.getName(), neuerName);
    	try {
			Files.write(pfad, inhalt.getBytes(charset));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private static String getPlaylistDaten(Playlist playlist) {
    	ArrayList<Musikstueck> songs = playlist.playList;
    	String daten = playlist.getName() + ",";
    	for (int i = 0; i < songs.size(); i++) {
    		daten = daten + Verwaltung.getMusikstuecke().indexOf(songs.get(i))+",";
    	}
    	return daten;
    }

    private static boolean istIndex (String string) {
    	if (string == null) {
    		return false;
    	}
    	try {
    		int i = Integer.parseInt(string);
    	} catch (NumberFormatException e) {
    		return false;
    	}
    	return true;
    }
}