package student;
/*
 * SongCollection.java
 * Read the specified data file and build an array of songs.
 * 
 * Starting code by Prof. Boothe 2015
 * Modfied by Anne Applin 2016
 * 
 * Further modified by Roger Parent 2016
 * - Implemented constructor
 * - Added printTenSongs method
 * - Split printTenSongs into one method that takes a SongCollection
 *   and another that takes an array of Songs. The former calls the latter.
 */
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author boothe
 */
public class SongCollection {

    private Song[] songs;

    /**
     * Note: in any other language, reading input inside a class is simply not
     * done!! No I/O inside classes because you would normally provide
     * precompiled classes and I/O is OS and Machine dependent and therefore not
     * portable. Java runs on a virtual machine that IS portable. So this is
     * permissable because we are programming in Java.
     *
     * @param filename The path and filename to the datafile that we are using.
     */
    public SongCollection(String filename) {
        ArrayList<Song> list = new ArrayList<>();
        Scanner inFile = null;
	// use a try catch block
        // read in the song file and build the songs array
        // you must use a StringBuilder to read in the lyrics!
        try{
            inFile = new Scanner(new FileReader(filename));
            //Throws an exception if the file isn't found.
        } catch (FileNotFoundException ex) {
            //Catch the exception and print an error message and gracefully exit
            System.out.println("File "+filename+" not found!");
            System.exit(1);
        }
        String artist, title, lyrics, currentLine;
        
        while(inFile.hasNextLine()){
            //Read in the artist 
            currentLine = inFile.nextLine();
            //ARTIST=" to the second-to-last character
            artist = currentLine.substring(8, currentLine.length()-1);
            
            //Read in the title
            currentLine = inFile.nextLine();
            //TITLE=" to the second-to-last character
            title = currentLine.substring(7, currentLine.length()-1);
            
            //Read in the first line of lyrics and append it to LyricsBuilder
            StringBuilder lyricsBuilder = new StringBuilder();
            currentLine = inFile.nextLine();
            lyricsBuilder.append(currentLine.substring(8)).append("\n");
            
            //Append every subsequent line to the lyrics until hitting the singlequote line.
            currentLine = inFile.nextLine();
            while(!currentLine.equals("\"")) {
                lyricsBuilder.append(currentLine).append("\n");
                currentLine = inFile.nextLine();
            }
            lyrics = lyricsBuilder.toString();
            
            //Add the scraped song into the DB
            list.add(new Song(artist, title, lyrics));
        }
        
        songs = list.toArray(new Song[list.size()]);
        // sort the songs array
        Arrays.sort(songs);
    }
 
    /**
     * this is used as the data source for building other data structures
     * @return the songs array
     */
    public Song[] getAllSongs() {
        return songs;
    }
 
    /**
     *
     * @param sc A song collection to read from
     */
    public static void printTenSongs(SongCollection sc) {
        printTenSongs(sc.getAllSongs());
    }
    
    /**
     *
     * @param songs An array of songs to read from
     */
    public static void printTenSongs(Song[] songs)
    {
        //If there are less than 10 songs, show all songs.
        int len = songs.length;
        if (len > 10) { len = 10; }
        
        System.out.printf("Total songs = %s. First songs: \n", songs.length);
        for ( int i = 0; i < len; i++)
        {
            System.out.printf("%s  \"%s\"\n", songs[i].getArtist(), songs[i].getTitle());
        }
    }
    
    /**
     * unit testing method
     * @param args
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("usage: prog songfile");
            return;
        }

        SongCollection sc = new SongCollection(args[0]);
        
        printTenSongs(sc);
    }
}
