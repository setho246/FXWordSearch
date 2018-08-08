import java.util.ArrayList;

/**
 * This manages the wordSelect ArrayList for the Word Search game. The ArrayList
 * is used to track characters the user has selected so far.
 * 
 * The following files are also required to run.
 * 
 * WordSearch.java Base.java WordList.java
 * 
 * @version 2.0
 * @author Seth Hilder (478393)
 * @version 14 May 2018
 */

public class WordSelected {

    private ArrayList<Character> wordSelect = new ArrayList<Character>(); // Stores the list of words to find

    /**
     * Adds a letter to wordSelect
     */
    public void addLetter(char val) {
        wordSelect.add(val);
    }

    /**
     * Returns the number of characters currently stored in wordSelect
     */
    public int getSize() {
        return wordSelect.size();
    }

    /**
     * Returns the character at a given position
     * 
     * @param pos Position of value that is returned
     */
    public char getValue(int pos) {
        return wordSelect.get(pos);
    }

    /**
     * Clears all values from the wordSelect ArrayList
     */
    public void clearArrayList() {
        wordSelect.clear();
    }

    /**
     * Removes a value from a given position
     * 
     * @param pos Position of the value to remove
     */
    public void removeValue(int pos) {
        wordSelect.remove(pos);
    }

    /**
     * Remove the last value that was added to wordSelect
     */
    public void removeLastValue() {
        wordSelect.remove(wordSelect.size() - 1);
    }
}