/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.io.*;
import java.util.*;
import student.Song.CmpTitle;
import student.RaggedArrayList;
/**
 *
 * @author zack
 */
public class SearchByTitlePrefix {
    private RaggedArrayList<Song> RALS;
    private CmpTitle c;
    
    public SearchByTitlePrefix(SongCollection sc){
        RALS = new RaggedArrayList(c);//initialize the datastructure
        //loop through the SongCollection and add each one to the 
        //RaggedArrayList
        for(Song i : sc.getAllSongs()){
            RALS.add(i);
        }
    }
    
}
