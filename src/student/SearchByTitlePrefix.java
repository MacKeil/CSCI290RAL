/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.io.*;
import java.util.*;
import student.Song.*;
import student.RaggedArrayList;
/**
 *
 * @author zack
 */
public class SearchByTitlePrefix {
    private RaggedArrayList<Song> SongRAL;
    private CmpTitle c;
    
    public SearchByTitlePrefix(SongCollection sc){
        c = new CmpTitle();//initialize the comparator
        //initialize the datastructure
        SongRAL = new RaggedArrayList(c);
        //loop through the SongCollection and add each one to the 
        //RaggedArrayList
        for(Song i : sc.getAllSongs()){
            SongRAL.add(i);
        }
    }
    
    /**
     * Find all songs that match the prefix given.
     * 
     * @param String prefix
     * @return an array of songs that match the prefix
     */
    public Song[] search(String prefix){
       //TODO use RALS to seach through
       //use sublist to create a new one with the right values
       //and toArray to get our return.
       Song testSong = new Song("",prefix,"");
       char[] charPrefix = prefix.toCharArray();
       charPrefix[charPrefix.length - 1]++;
       SongRAL = SongRAL.subList(testSong, new Song("",charPrefix.toString(),""));
       Song[] arraySongs = new Song[SongRAL.size];
       arraySongs = SongRAL.toArray(arraySongs);
       return arraySongs;
    }
    
    public static void main(String[] args){
        /*if (args.length < 2) {
            System.err.println("usage: prog songfile [search string]");
            return;
        }*/
        
        SongCollection sc = new SongCollection(/*args[0]*/"allSongs.txt");
        SearchByTitlePrefix sbtp = new SearchByTitlePrefix(sc);
        
        if(args.length > 1){
            System.out.println("searching for: " + args[1]);
            Song[] byTitleResult = sbtp.search(/*args[1]*/ "search");
            
            System.out.println("Total number of matches is: " + byTitleResult.length);

            SongCollection.printTenSongs(byTitleResult);
        }
    }
}
