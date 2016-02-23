package student;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

/**
 * RaggedArrayList.java
 *  
 * Initial starting code by Prof. Boothe Sep 2015
 *
 * The RaggedArrayList is a 2 level data structure that is an array of arrays.
 *  
 * It keeps the items in sorted order according to the comparator.
 * Duplicates are allowed.
 * New items are added after any equivalent items.
 *
 * NOTE: normally fields, internal nested classes and non API methods should 
 *  all be private, however they have been made public so that the tester code 
 *  can set them
 * @author boothe 2015
 * Revised by Anne Applin 2016
 * 
 * Modified by Zachary MacKeil 2016
 * -added increment size to add() because common sense
 * -implemented ListLoc moveToNext()
 * -implemented ListLoc findFront(E item) tested and found it works
 * -implemented ListLoc findEnd(E item) tested it and found it works
 * -Commented all of his code
 * -Created basic structure of add()
 * -implemented split section of add()
 * -implemented contains()
 * -implemented itr.next()
 * 
 * Modified by Nicolas Desjardins 2016
 * -added comments to his code
 * -implemented double section of add()
 * -implemented double l1Array section of add()
 * 
 * @param <E>
 */

public class RaggedArrayList<E> implements Iterable<E> {

    private static final int MINIMUM_SIZE = 4;    // must be even so when split get two equal pieces

    /**
     *
     */
    public int size;

    /**
     * really is an array of L2Array, but compiler won't let me cast to that
     */
    public Object[] l1Array;  

    /**
     *
     */
    public int l1NumUsed;
    private Comparator<E> comp;


    /**
     * create an empty list
     * always have at least 1 second level array even if empty, makes 
     * code easier 
     * (DONE - do not change)
     * @param c a comparator object
     */
    public RaggedArrayList(Comparator<E> c) {
        size = 0;
        l1Array = new Object[MINIMUM_SIZE];// you can't create an array of a generic type
        l1Array[0] = new L2Array(MINIMUM_SIZE);  // first 2nd level array
        l1NumUsed = 1;
        comp = c;
    }

    /**
     * nested class for 2nd level arrays
     * (DONE - do not change)
     */
    public class L2Array {

        /**
         *  the array of items
         */
        public E[] items;

        /**
         * number of item in this L2Array with values
         */
        public int numUsed;

        /**
         * Constructor for the L2Array
         * @param capacity the initial length of the array
         */
        public L2Array(int capacity) {
             // you can't create an array of a generic type
            items = (E[]) new Object[capacity]; 
            numUsed = 0;
        }
    }// end of nested class L2Array

    /**
     *  total size (number of entries) in the entire data structure
     *  (DONE - do not change)
     * @return total size of the data structure
     */
    public int size() {
            return size;
    }


    /**
     * null out all references so garbage collector can grab them
     * but keep otherwise empty l1Array and 1st L2Array
     * (DONE - Do not change)
     */
    public void clear() {
        size = 0;
        // clear all but first l2 array
        Arrays.fill(l1Array, 1, l1Array.length, null);  
        l1NumUsed = 1;
        L2Array l2Array = (L2Array) l1Array[0];
        // clear out l2array
        Arrays.fill(l2Array.items, 0, l2Array.numUsed, null); 
        l2Array.numUsed = 0;
    }


    /**
     *  nested class for a list position
     *  used only internally
     *  2 parts: level 1 index and level 2 index
     */
    public class ListLoc {

        /**
         * Level 1 index
         */
         public int level1Index;

        /**
         * Level 2 index
         */
         public int level2Index;

        /**
         * Parameterized constructor
         * @param level1Index input value for property
         * @param level2Index input value for property
         */
        public ListLoc(int level1Index, int level2Index) {
           this.level1Index = level1Index;
           this.level2Index = level2Index;
        }


        /**
         * test if two ListLoc's are to the same location 
         * (done -- do not change)
         * @param otherObj 
         * @return
         */
        public boolean equals(Object otherObj) {
            // not really needed since it will be ListLoc
            if (getClass() != otherObj.getClass()) {
                return false;
            }
            ListLoc other = (ListLoc) otherObj;

            return level1Index == other.level1Index && 
                    level2Index == other.level2Index;
        }
        /**
         * move ListLoc to next entry
         *  when it moves past the very last entry it will be 1 index past the 
         *  last value in the used level 2 array can be used internally to 
         *  scan through the array for sublist also can be used to implement 
         *  the iterator
        */
        public void moveToNext() {
            // create a L2Array to test level2Index against
            RaggedArrayList.L2Array l2Array = (L2Array)l1Array[level1Index];
            if(level2Index <= l2Array.numUsed){
                //as long as we are still in used space for the l2Array step 
                //through
                level2Index++;
            }
            else if(level2Index > l2Array.numUsed 
                    && level1Index < l1Array.length){
                //go up one whole level because we reached the end of this one
                level1Index++;
                level2Index = 0;//start at the beginning of this level
            }
            else{
                //clearly we met the end of our structure so go back to the
                //beginning
                level1Index = 0;
                level2Index = 0;
            }
        }
    }


    /**
     * find 1st matching entry
     * @param item  we are searching for a place to put.
     * @return ListLoc of 1st matching item or of 1st item greater than the 
     * item if no match this might be an unused slot at the end of a 
     * level 2 array
     */
    public ListLoc findFront(E item) {
        // create a L2Array to work with
        RaggedArrayList.L2Array l2Array = (L2Array) l1Array[0];
        // Declare a ListLoc at 0,0 to step through
        ListLoc Current = new ListLoc(0,0);
        //run through the lists and compare the largest value to our item
        while(Current.level1Index < l1NumUsed -1
                && comp.compare(item, (E) l2Array.items[l2Array.numUsed -1]) > 0){
            Current.level1Index++;//go to the next level and check that one
            //keep our Level two array up to date
            l2Array = (L2Array) l1Array[Current.level1Index];
        }
        //if we found a value that was larger than our item check it against 
        //the values in the array
        while(l2Array.items[Current.level2Index] != null 
                && comp.compare(item, (E) l2Array.items[Current.level2Index]) > 0){
            //iterate through the array and check each one.
            Current.level2Index++;
        }
        
        return Current; // send our location as a return
    }


    /**
     * find location after the last matching entry or if no match, it finds 
     * the index of the next larger item this is the position to add a new 
     * entry this might be an unused slot at the end of a level 2 array
     * @param item
     * @return the location where this item should go 
     */
    public ListLoc findEnd(E item) {
        //create a ListLoc to run with
        ListLoc L = findFront(item);// Don't reinvent the wheel
        //L2Array to check what we got against
        L2Array l2Array =(L2Array) l1Array[L.level1Index];
        //We don't want to check anything that is null
        if(l2Array.items[L.level2Index] != null){
            //Limit the number of times we compare the values
            int holder = comp.compare(item, l2Array.items[L.level2Index]);
            if(holder < 0){//if our item is less than the return quit
                //this is looking both ways down a one way street
               return L;
            }
            //if our item is the same as findFront()'s return run a check
            else if(holder == 0){
                //Check for duplicates in other levels
                while(comp.compare(l2Array.items[L.level2Index],l2Array.items[l2Array.numUsed - 1]) == 0
                        && L.level2Index != l2Array.numUsed - 1){
                    //as long as we are still finding matching items keep 
                    //moving forward
                    L.moveToNext();
                    if(L.level2Index == l2Array.numUsed -2){
                        //if we hit the end of our array
                        L.level1Index++;//go up one level
                        L.level2Index = 0;//set the level 2 index to the beginning
                        //intialize a new L2Array at the new level
                        l2Array = (L2Array) l1Array[L.level1Index];
                    }
                }
                //as long as we aren't at the end of useable space on this level
                if(l2Array.items[L.level2Index + 1] != null){
                    // as long as the next item is the same as this one
                    while(comp.compare(l2Array.items[L.level2Index], l2Array.items[L.level2Index + 1]) == 0){
                        L.level2Index++;//go to the next one
                    }
                }
                //no matter what we want to go one index past where we are
                L.level2Index++;
                //return our indexes
                return L;
            }
            //if we got something that is too big decriment by one
            else if(L.level2Index > 0){
                L.level2Index--;
            }
        }
        
        return L; // catch all return statement

    }
  
    /**
     * add object after any other matching values findEnd will give the
     * insertion position
     * @param item
     * @return 
     */
    public boolean add(E item) {
        size++; //because common sense
        // set our ListLoc to where we want to add the item
        ListLoc whereToAdd = findEnd(item);
        //initialize an L2Array to work with
        L2Array l2Array = (L2Array) l1Array[whereToAdd.level1Index];//silly name I know
        //if the only item left is the last one go to contingency
        if(l2Array.numUsed == l2Array.items.length - 1){
            if(l2Array.items.length < l1Array.length){
                //we're gonna double it
                // We're movin' on up
                // @nicolasedesjardins
                
                // The new and improved L2Array
                L2Array longL2Array = new L2Array(l2Array.items.length * 2);
                
                // Copy over the previous entries
                for (int i = 0; i < l2Array.numUsed; i++) {
                    longL2Array.items[i] = l2Array.items[i];
                }
                
                // Copy over the previous fields
                longL2Array.numUsed = l2Array.numUsed;
                // Replace the old memory address with new
                l2Array = longL2Array;
                // Re-assign entry in level1 to new memory address
                l1Array[whereToAdd.level1Index] = l2Array;
                
                // Make space for the new item, where it should be
                for(int i = l2Array.numUsed; i >= whereToAdd.level2Index; i--){
                    l2Array.items[i + 1] = l2Array.items[i];
                }
                
                // Add item
                l2Array.items[whereToAdd.level2Index] = item;
                // Increment number of used slots
                l2Array.numUsed++;
            }
            else{               
                //we're gonna split it
                // We're getting a divorce
                //create a new array to add in
                L2Array splitArray = new L2Array(l2Array.items.length);
                //add our item first.  This is much easier than the other way
                //around which I totally didn't try and fail at over and over.
                for(int h = l2Array.numUsed; h > whereToAdd.level2Index; h--){
                    l2Array.items[h] = l2Array.items[h - 1];
                }
                l2Array.items[whereToAdd.level2Index] = item;
                l2Array.numUsed += 1;//because common sense
                //make a copy of the second half of the array and put it into
                //our new one
                System.arraycopy(l2Array.items, l2Array.numUsed / 2, 
                        splitArray.items, 0, l2Array.numUsed / 2);
                //the new one initialized with null values so we just need to
                //fill the second half of the old one
                Arrays.fill(l2Array.items, l2Array.numUsed /2, 
                        l2Array.items.length, null);
                //change the numUsed to reflect current totals
                splitArray.numUsed = l2Array.numUsed / 2;
                l2Array.numUsed /= 2;
                
                //add the new array into the l1Array
                for(int i = l1NumUsed ; i > whereToAdd.level1Index; i--){
                    l1Array[i] = l1Array[i - 1];
                }
                //add the new L2Array to the l1Array
                l1Array[whereToAdd.level1Index + 1] = splitArray;
                //update the original
                l1Array[whereToAdd.level1Index] = l2Array;
                l1NumUsed++;//because common sense
            }
        }
        else{
            //add it in were we said and adjust the array to fit around it
            //step from front to back
            for(int i = l2Array.numUsed; i >= whereToAdd.level2Index; i--){
                //push all existing items that are beyond our add point up one
                l2Array.items[i + 1] = l2Array.items[i];
            }
            //add our item into the array
            l2Array.items[whereToAdd.level2Index] = item;
            l2Array.numUsed++;//because common sense
        }
        
        // If number of used slots in l1Array has reached close to length,
        // double size of l1Array
        // @nicolasedesjardins
        if (l1NumUsed == l1Array.length) {
            Object[] longL1Array = new Object[l1Array.length * 2];
            
            for (int i = 0; i < l1NumUsed; i++) {
                longL1Array[i] = l1Array[i];
            }
            
            l1Array = longL1Array;
        }
        
        return true;
    }

    /**
     * check if list contains a match
     * @param item
     * @return 
     */
    public boolean contains(E item) {
        //use findFront() to find the item, should it exist
        ListLoc L = findFront(item);//reinventing the wheel is bad
        //Create a L2Array to test against.
        L2Array l2Array = (L2Array) l1Array[L.level1Index];
        //if and only if (iff) findFront() returned the spot where the item 
        //really is return true
        if(l2Array.items[L.level2Index] == item){
            return true;
        }
        //otherwise return false
        return false;//The End
    }

    /**
     * copy the contents of the RaggedArrayList into the given array
     *
     * @param a - an array of the actual type and of the correct size
     * @return the filled in array
     */
    public E[] toArray(E[] a) {
        // TO DO

        return a;
    }

    /**
     * returns a new independent RaggedArrayList whose elements range from
     * fromElemnt, inclusive, to toElement, exclusive the original list is
     * unaffected findStart and findEnd will be useful
     *
     * @param fromElement
     * @param toElement
     * @return the sublist
     */
    public RaggedArrayList<E> subList(E fromElement, E toElement) {
        // TO DO

        RaggedArrayList<E> result = new RaggedArrayList<E>(comp);
        return result;
    }

    /**
     * returns an iterator for this list this method just creates an instance of
     * the inner Itr() class (DONE)
     * @return 
     */
    public Iterator<E> iterator() {
        return new Itr();
    }

    /**
     * Iterator is just a list loc it starts at (0,0) and finishes with index2 1
     * past the last item in the last block
     */
    private class Itr implements Iterator<E> {

        private ListLoc loc;

        /*
         * create iterator at start of list
         * (DONE)
         */
        Itr() {
            loc = new ListLoc(0, 0);
        }

        /**
         * check if more items
         */
        public boolean hasNext() {
            //TODO 1 line only

            return false;
        }

        /**
         * return item and move to next throws NoSuchElementException if off end
         * of list
         */
        public E next() {
            //attempt to move to the next spot
            try{
                //move to the next spot
                loc.moveToNext();
                //create the L2Array to pull from
                L2Array l2Array = (L2Array) l1Array[loc.level2Index];
                //return the item at the spot we said to.
                return l2Array.items[loc.level2Index]; 
            }
            //should we end up outside where we wanted to be.
            catch(IndexOutOfBoundsException e){
                throw new IndexOutOfBoundsException();
            }
        }

        /**
         * Remove is not implemented. Just use this code. 
         * (DONE)
         */
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
