package student;

import java.io.*;
import java.util.*;
import student.Song.CmpArtist;

/**
 * SearchByArtistPrefix.java 
 * starting code Boothe 2015 
 * revised by aapplin
 * 
 * Contributed to by Roger Parent
 * - implemented search
 * - fixed a bug where searching for "Z" would cause an Index OOB error
 * 
 * Contributed to by Zachary MacKeil
 * -removed an if-else that implemented a break
 * -changed while loop conditions to streamline loop
 * 
 */
public class SearchByArtistPrefix {
    // keep a direct reference to the song array
    private Song[] songs;  

    /**
     * constructor initializes the property.
     * @param sc a SongCollection object
     */
    public SearchByArtistPrefix(SongCollection sc) {
        songs = sc.getAllSongs();
    }

    /**
     * find all songs matching artist prefix uses binary search should operate
     * in time log n + k (# matches)
     *
     * @param artistPrefix all or part of the artist's name
     * @return an array of songs by artists with substrings that match 
     *    the prefix
     */
    public Song[] search(String artistPrefix) {
        int loopComparisons = 0;
        
        int prefixLength = artistPrefix.length();
        int firstIndex, lastIndex;
        Song keySong = new Song(artistPrefix);
        CmpArtist comparator = new CmpArtist();
        boolean matchesPrefix = true;
        
        //Indexes to create an array slice with
        firstIndex = Arrays.binarySearch(songs, keySong, comparator);
        /* If there are no full matches, convert the insertion point 
         * to look for partials. */
        if (firstIndex < 0) { firstIndex = -firstIndex - 1; }
        lastIndex = firstIndex;
        
        Song currentSong;
        while(matchesPrefix && lastIndex < songs.length)
        {
            currentSong = songs[lastIndex];
            
            String currentArtist = currentSong.getArtist();
            
            //If the current artist is longer than the search key, truncate it.
            if(currentArtist.length() > prefixLength)
            { currentArtist = currentArtist.substring(0, prefixLength); }
            
            //Compare the keys, and increment lastIndex is the current matches.
            if ( currentArtist.equalsIgnoreCase(artistPrefix) )
            {
                lastIndex++;
            }
            else//Otherwise, stop the loop
            { matchesPrefix = false; }
            //Increment loop comparisons
            loopComparisons++;
        }
        
        System.out.printf("The binary search did %s comparisons, "+
                "and the scoop loop did %s\n", 
                comparator.getCmpCnt(), loopComparisons);
        
        return Arrays.copyOfRange(songs, firstIndex, lastIndex);
    }

    /**
     * testing method for this unit
     * @param args  command line arguments
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("usage: prog songfile [search string]");
            return;
        }
        
        SongCollection sc = new SongCollection(args[0]);
        SearchByArtistPrefix sbap = new SearchByArtistPrefix(sc);

        if (args.length > 1) {
            System.out.println("searching for: " + args[1]);
            Song[] byArtistResult = sbap.search(args[1]);

            SongCollection.printTenSongs(byArtistResult);
        }
    }
}
