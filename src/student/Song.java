package student;

import java.util.*;

/*
 * Song class to hold strings for a song's artist, title, and lyrics
 * Do not add any methods, just implement the ones that are here.
 * Starting code by Prof. Boothe 2015
 * Modified by Anne Applin 2016
 *
 * Modified by Roger Parent 2016
 * - Finished empty methods in Song class
 * - Implemented compareTo and fixed an error in documentation
 * - Fixed toString test that didn't actually test anything
 * -- Started Part 2
 * - Implemented Comparator subclass CmpArtist
 * - Re-worked compareTo to use the Comparator
 * - Added an Artist-only constructor
 *
 *
 * Modified by Zachary MacKeil 2016
 * - added error checking for comparator
 * - changed compareTo method to have try catch block
 */
/**
 *
 * @author boothe
 */
public class Song implements Comparable<Song> {
    // fields
    private String artist;
    private String title;
    private String lyrics;

    /**
     * Artist Comparator. Compares two Song objects, and counts the amount of
     * comparisons made to do so.
     */
    public static class CmpArtist extends CmpCnt implements Comparator<Song>{
        public int compare(Song s1, Song s2){
            cmpCnt++;//add to the count of times compared
            //
            return s1.toString().compareToIgnoreCase(s2.toString());
           
        }
    }
    /**
     * Title Comparator. Compares two Song objects, and counts the comparisons 
     * made to do so
     */
    public static class CmpTitle extends CmpCnt implements Comparator<Song>{
        public int compare(Song s1, Song s2){
            cmpCnt++;//add to the count of times compared
            //compare just the titles of the two songs
            return s1.title.compareTo(s2.title);
        }
    }
    
    /**
     * Parameterized constructor
     * @param artist the author of the song
     * @param title the title of the song
     * @param lyrics the lyrics as a string with linefeeds embedded
     */
    public Song(String artist, String title, String lyrics) {
        this.artist = artist;
        this.title = title;
        this.lyrics = lyrics;
    }
    
    /**
     * Artist-only Parameterized constructor
     * @param artist the author of a song
     */
    public Song(String artist) {
        this(artist, "", "");
    }

    /**
     *
     * @return this song's artist.
     */
    public String getArtist() {
        return artist;
    }

    /**
     *
     * @return this song's lyrics.
     */
    public String getLyrics() {
        return lyrics;
    }

    /**
     *
     * @return this song's title.
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @return
     */
    public String toString() {
        return String.format("%s, \"%s\"", artist, title);
    }

    /**
     * the default comparison of songs
     * primary key: artist, secondary key: title
     * used for sorting and searching the song array
     * if two songs have the same artist and title they are considered the same
     * @param song2
     * @return an integer less than, greater to, or equal to 0 
     *    depending on whether this song should be before, after or is the same. 
     *    Used for a "natural" sorting order.  In this case first by author
     *    then by title so that the all of an artist's songs are together, 
     *    but in alpha order.
     */
    public int compareTo(Song song2){
        int EQUAL = 0;//if equal return 0
        int compared = this.getArtist().compareToIgnoreCase(song2.getArtist());
        if(this == song2){//if they are the same thing return equal
            return EQUAL;
        }
        if(compared < 0){//if this is before that return -1
           // return -1;
           return compared;
        }
        compared = this.getTitle().compareToIgnoreCase(song2.getTitle());
        if(compared > 0){//if this is greater than that return 1
            //return 1;
            return compared;
        }
        return EQUAL;//if we got here then they are equal
       
    }
    
    /**
     * testing method to unit test this class
     * @param args
     */
    public static void main(String[] args) {
        Song s1 = new Song("Professor B",
                "Small Steps",
                "Write your programs in small steps\n"
                + "small steps, small steps\n"
                + "Write your programs in small steps\n"
                + "Test and debug every step of the way.\n");

        Song s2 = new Song("Brian Dill",
                "Ode to Bobby B",
                "Professor Bobby B., can't you see,\n"
                + "sometimes your data structures mystify me,\n"
                + "the biggest algorithm pro since Donald Knuth,\n"
                + "here he is, he's Robert Boothe!\n");

        Song s3 = new Song("Professor B",
                "Debugger Love",
                "I didn't used to like her\n"
                + "I stuck with what I knew\n"
                + "She was waiting there to help me,\n"
                + "but I always thought print would do\n\n"
                + "Debugger love .........\n"
                + "Now I'm so in love with you\n");

        System.out.println("testing getArtist: " + s1.getArtist());
        System.out.println("testing getTitle: " + s1.getTitle());
        System.out.println("testing getLyrics:\n" + s1.getLyrics());

        System.out.println("testing toString: "+s1+"\n");
        System.out.println("Song 1: " + s1);
        System.out.println("Song 2: " + s2);
        System.out.println("Song 3: " + s3);

        System.out.println("testing compareTo:");
        System.out.println("Song1 vs Song2 = " + s1.compareTo(s2));
        System.out.println("Song2 vs Song1 = " + s2.compareTo(s1));
        System.out.println("Song1 vs Song3 = " + s1.compareTo(s3));
        System.out.println("Song3 vs Song1 = " + s3.compareTo(s1));
        System.out.println("Song1 vs Song1 = " + s1.compareTo(s1));
        
        int x = 4;
        int sum = 0;
        int counter = 0;
        while(sum < 10514){
            sum += x-1;
            ++counter;
            if(counter == x - 1){
                x *= 2;
            }
        }
        System.out.println("worst columns: " + x + " sum is: " + sum);
    }
}
