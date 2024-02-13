package musikplayer3001;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import javax.sound.sampled.UnsupportedAudioFileException;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public abstract class Verwaltung {
	

	    private static List<Musikstueck> musikstuecke = new ArrayList<>();
	    private static List<Playlist> playlists = new ArrayList<>();   

	    //Funktion die alle Musikstueckpbjekte in Array [][] ausgibt
	    public static Object[][] getAllMusikstuecke() {
	    	Object[][] stuecke = new Object[musikstuecke.size()][6];
	    	for (int i = 0; i < musikstuecke.size(); i++) {
	    		stuecke[i][0] = musikstuecke.get(i).getTitle();
	    		stuecke[i][1] = musikstuecke.get(i).getArtist();
	    		stuecke[i][2] = musikstuecke.get(i).getGenre();
	    		stuecke[i][3] = musikstuecke.get(i).getAlbum();
	    		stuecke[i][4] = musikstuecke.get(i);
	    	}
	    	return stuecke;
	    }
	    
	    //Funktion die alle Playlists in Array [][] ausgibt
	    public static Object[][] getAllPlaylists() {
	    	Object[][] allPlaylists = new Object[playlists.size()][3];
	    	for (int i = 0; i < playlists.size(); i++) {
	    		allPlaylists[i][0] = playlists.get(i).getName();
	    		allPlaylists[i][1] = playlists.get(i).getSize();
	    		allPlaylists[i][2] = playlists.get(i);
	    	}
	    	return allPlaylists;
	    }
	    
	    //Erstellt Array mit allen Artists für Playlisterstellung
	    public static String[] getAllArtists() {
	    	List<String> liste = new ArrayList<>();
	    	for (int i = 0; i<musikstuecke.size(); i++) {
	    		String a = musikstuecke.get(i).getArtist();
	    		if (!liste.contains(a) && a != null) {
	    			liste.add(a);
	    		}
	    	}
	    	String[] artists = liste.toArray(new String[0]);
	    	return artists;
	    }
	    
	    //Erstellt Array mit allen Genres für Playlisterstellung
	    public static String[] getAllGenres() {
	    	List<String> liste = new ArrayList<>();
	    	for (int i = 0; i<musikstuecke.size(); i++) {
	    		String g = musikstuecke.get(i).getGenre();
	    		if (!liste.contains(g) && g != null) {
	    			liste.add(g);
	    		}
	    	}
	    	String[] genres = liste.toArray(new String[0]);
	    	return genres;
	    }
	    
	    //Erstellt Array mit allen Alben für Playlisterstellung
	    public static String[] getAllAlbums() {
	    	List<String> liste = new ArrayList<>();
	    	for (int i = 0; i<musikstuecke.size(); i++) {
	    		String a = musikstuecke.get(i).getAlbum();
	    		if (!liste.contains(a) && a != null) {
	    			liste.add(a);
	    		}
	    	}
	    	String[] albums = liste.toArray(new String[0]);
	    	return albums;
	    }

	    //Nimmt Informationen aus MusikGUI an und löscht das entsprechende Element
	    public static void loescheElement(Object object) {
	    	if (object instanceof Playlist) {
	    		for (int i = 0; i < playlists.size(); i++) {
	    			if (object.equals(playlists.get(i))) {
	    				loeschePlaylist((Playlist)object);
	    				break;
	    			}
	    		}
	    	} else if (object instanceof Musikstueck) {
	    		for (int i = 0; i < musikstuecke.size(); i++) {
	    			if (object.equals(musikstuecke.get(i))) {
	    				loescheMusikstueck((Musikstueck)object);
	    				break;
	    			}	    			
	    		}
	    	} else 
	    		System.out.println("Klappt nicht");
	    }
	    
	    
	    
	    // Funktion zum Hinzufuegen eines neuen Musikstuecks
	    public static void addMusikstueck(Musikstueck musikstueck) {
	        musikstuecke.add(musikstueck);
	    }
	    
	    // Funktion zum Loeschen eines Musikstuecks
	    public static void loescheMusikstueck(Musikstueck musikstueck) {
	        musikstuecke.remove(musikstueck);
	    }
	    
	    // Funktion zum Suchen eines Musikstuecks nach Titel
	    public Musikstueck findeMusikstueckNachTitle(String title) {
	        for (Musikstueck musikstueck : musikstuecke) {
	            if (musikstueck.getTitle().equals(title)) {
	                return musikstueck;
	            }
	        }
	        return null;
	    }

	    // Funktion zum Hinzufuegen einer neuen Playlist
	    public static void addPlaylist(Playlist playlist) {
	        playlists.add(playlist);
	    }

	    // Funktion zum Loeschen einer Playlist
	    public static void loeschePlaylist(Playlist playlist) {
	        playlists.remove(playlist);
	    }

	    // Funktion zum Suchen einer Playlist nach Name
	    public Playlist findePlaylistnachNamen(String name) {
	        for (Playlist playlist : playlists) {
	            if (playlist.getName().equals(name)) {
	                return playlist;
	            }
	        }
	        return null;
	    }

	    // Getter fuer die Liste aller Musikstuecke
	    public static List<Musikstueck> getMusikstuecke() {
	        return musikstuecke;
	    }

	    // Getter fuer die Liste aller Playlists
	    public static List<Playlist> getPlaylists() {
	        return playlists;
	    }
	    
	    //nimmt Informationen aus MusikGUI hinzufuegenDialog() entgegen und erstellt ein neues Musikstück
		public static void erstelleNeuesStueck(String text, String interpret, String genre, String album, File file) {
			Musikstueck m = new Musikstueck(text,interpret,genre,album,file);
			Einlesen.hinzufuegenSong(m, "Musik/CSV Dateien/Musik.csv");
			addMusikstueck(m);
		}
	    

}