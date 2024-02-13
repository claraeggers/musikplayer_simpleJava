package musikplayer3001;

import java.io.File;

public class Musikstueck {
	
	private String title;
	private String artist;
	private String genre;
	private String album;
	private File audioURL;
	
	public Musikstueck(String title, String artist, String genre, String album, File audioURL) {
	        this.title = title;
	        this.artist = artist;
	        this.genre = genre;
	        this.album = album;
            this.audioURL = audioURL;	        
	}
	    
	@Override
	public String toString() {
			return "Title: " + title +
	               ", Artist: " + artist +
	               ", Album: " + album +
	               ", Genre: " + genre;
	    }

	// Getter und Setter fuer die Attribute 

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}
	    
	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}   
	    
	public File getAudioURL() {
		return audioURL;
	}

	public void setAudioURL(File audioURL) {
		this.audioURL = audioURL;
	}

}