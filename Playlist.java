package musikplayer3001;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;



public class Playlist{
	
    ArrayList<Musikstueck> playList;
	List<Musikstueck> musikstuecke;
	String name;
	

	public Playlist(String n) {
		 playList = new ArrayList<Musikstueck>();
		 name = n; 
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	// Länge der Playlist in Anzahl der Musikstuecke
	public int getSize() {
		return playList.size();
	}
    // Getter fuer PLaylists
    public ArrayList<Musikstueck> getPlayList() {
        return playList;
    }

	// Random Name Generator fuer Playlists
	public String generierePlaylistName() {
		String[] pl_name = {"RocknRoll","Chill","Party","study","fun","focus","nostalgic", "Favs"};
		String[] pl_surname = {"mix","playlist","jam"};

		Random random = new Random();
		String name = pl_name[random.nextInt(pl_name.length)] +" "+ pl_surname[random.nextInt(pl_surname.length)];
		return name;
	}
	
	//Funktion zum Erstellen von Playlists nach Genre
	public ArrayList<Musikstueck> playlistNachGenre(String genre) {
		 musikstuecke = Verwaltung.getMusikstuecke();
	
		 ArrayList<Musikstueck> songsNachGenre = new ArrayList<>(); 
		 for(int i=0; i< musikstuecke.size();i++) {
			 if (musikstuecke.get(i).getGenre().equalsIgnoreCase(genre)) {
				songsNachGenre.add(musikstuecke.get(i));
			}
		playList=songsNachGenre;
		}
	return playList;
	}
		
	//Funktion erstellt PLaylist nach INterpret
	public ArrayList<Musikstueck> playlistNachInterpret (String interpret) {
		musikstuecke = Verwaltung.getMusikstuecke();	
		ArrayList <Musikstueck> songsNachInterpret = new ArrayList<>();
		
		for(int i=0; i< musikstuecke.size(); i++) {
			if(musikstuecke.get(i).getArtist().equalsIgnoreCase(interpret)) {
				songsNachInterpret.add(musikstuecke.get(i));
			}
		}
		playList=songsNachInterpret;
	
		return playList;
	}
	
	//Funktion erstellt Playlist nach Album
	public ArrayList<Musikstueck> playlistNachAlbum(String album) {
		 musikstuecke = Verwaltung.getMusikstuecke();
		 ArrayList<Musikstueck> songsNachAlbum = new ArrayList<>(); 
		 for(int i=0; i< musikstuecke.size();i++) {
			 if (musikstuecke.get(i).getAlbum().equalsIgnoreCase(album)) {
				songsNachAlbum.add(musikstuecke.get(i));
			}
		playList=songsNachAlbum;
		}
	return playList;
	}		
	//Rueckgabe von konkretem INdex bei Suche von musikstueck in playlist
	public Musikstueck get(int index) {
	    if (index >= 0 && index < playList.size()) {
	        return playList.get(index);
	    }
	    return null;
	}
	
	//Rueckgabe von konkretem Musikstueck bei Suche nach Index in playlist
	public int indexOf(Musikstueck musikstueck) {
	    return playList.indexOf(musikstueck);
	}
	
	//Fuegt Song zu PLaylist hinzu
    public void addSong(Musikstueck musikstueck) {
        playList.add(musikstueck);
    }
       
    
    //Erstellt Playlist mit allen Musikstuecken
    public static ArrayList<Musikstueck> allePlaylist(){
    	ArrayList<Musikstueck> alle = new ArrayList<>();
    	alle = Einlesen.ladeMusik().playList;
		return alle;
	}
	
    
    
    
}