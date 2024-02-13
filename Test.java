package musikplayer3001;

import java.io.File;
import java.util.ArrayList;

public class Test {

	//Testklasse kann in Main-Funktion aktiviert werden (Auskommentierung entfernen)

	//Testet das Abspielen eines Songs
    public static void testPlaySong() {
        // Erstellt ein Musikobjekt fuer Test
        File audioFile = new File("Testmusik/Deadfire.wav");
        Musikstueck dummyMusikstueck = new Musikstueck("Fiktiver Song", "Irgendein Artist", "WasauchImmer Album","Quatsch Genre", audioFile);

        PlayerGUI playerGUI = new PlayerGUI(new Player(),dummyMusikstueck);
        playerGUI.createAndShowGUI();

        // Simultiertes Klicken des Play-Buttons
        playerGUI.playSong();
        System.out.println("Test: Spielt der Song ab?");
        playerGUI.stopSong();
    }

    public static void testPauseAndResume() {
        // Erstellt Musikstueck fuer test
        File audioFile = new File("Testmusik/Deadfire.wav");
        Musikstueck dummyMusikstueck = new Musikstueck("Fiktiver Song", "Irgendein Artist", "WasauchImmer Album","Quatsch Genre", audioFile);

        PlayerGUI playerGUI = new PlayerGUI(new Player(),dummyMusikstueck);
        playerGUI.createAndShowGUI();

        playerGUI.playSong();
        playerGUI.pauseSong();
        playerGUI.fortsetzenSong();
        System.out.println("Test: Funktioniert Pause and Fortsetzen?");
        
        playerGUI.stopSong();

    }
    
    //Test der Datenstrukur : Wird ein Array zurueckgegeben, wenn wir einen erstellen wollen?
    public static void testArray() {
    	
    	//Funktion zum Einlesen der Musik aus Datenbanl
		Einlesen.ladeMusik();
		//Mit getAll() Funktion werden Musikstueckobjekte in Object [][] 
		Object[][] musikstuecke = Verwaltung.getAllMusikstuecke();
		//Die ersten Songtitel sollen mit Array uebereinstimmen
		String songtitel1 = "Hungaria";
		String songtitel2 = "Blue Bossa";
		String songtitel3 = "If I Wait";
		boolean richtig = false;
		if (songtitel1.equals(musikstuecke[1][0]) && songtitel2.equals(musikstuecke[5][0]) && songtitel3.equals(musikstuecke[2][0])) {
			richtig = true;
			System.out.println("Songtitel richtig");
		}
		else
			//Wenn Reihenfolge anders ist, wird ausgegeben welche Songitiel stattdessen im Array sind
			System.out.println("Musikstück an 1,0 "+musikstuecke[1][0]+" statt "+songtitel1+
					"\n Musikstück an 5,0 "+musikstuecke[5][0]+" statt "+songtitel2+
					"\n Musikstück an 2,0 "+musikstuecke[2][0]+" statt "+songtitel3);
		
    }
    
    
}