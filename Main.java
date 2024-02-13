package musikplayer3001;

public class Main {

	public static void main(String[] args) {
	
	Einlesen.ladeMusik();
	Einlesen.allePlaylistsEinlesen();

	MusikGUI musik = new MusikGUI(); 
	
	// Testfaelle zum Testen von Grundfunktionen, Funktionen nacheinander auskommentieren und testen
    // Dafuer alle Zeilen in der Main - außer Zeile 19 + gewuenschte Testfunktion - auskommentieren 
	
	//Test test = new Test();
	//Test.testPauseAndResume();
	//Test.testPlaySong();
	//Test.testArray();
	   

         
	}
}