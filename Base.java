import java.util.Random;

/**
 * This runs the data structure for the Word Search game
 * 
 * This program manages the underlying data model.
 * 
 * The following files are also required to run:
 * 
 * WordSearch.java WordList.java WordSelect.java
 * 
 * @version 2.0
 * @author Seth Hilder (478393)
 * @version 14 May 2018
 */

public class Base {

    private WordList wordList = new WordList(); // Hold the list of words that the user still needs to find
    private WordSelected wordSelect = new WordSelected(); // Holds a list of selected characters

    private char[][] gameBoard; // Becomes the gameBoard, and is initialized with a size from WordSearch.java
    private int boardSize; // Holds the size of the board
    private String wordIn = new String(); // Stores the word that the user selects
    private int oldRow = -1; // Set to the row of the first letter of wordSelect
    private int oldCol = -1; // Set to the column of the first letter of wordSelect
    private final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; // Conatins all possible random letters
    
    private final int EASY_BOARD_SIZE = 15;
    private final int MEDIUM_BOARD_SIZE = 20;
    private final int HARD_BOARD_SIZE = 25;
    private final int EASY_WORD_SIZE = 4;
    private final int MEDIUM_WORD_SIZE = 8;
    private final int HARD_WORD_SIZE = 12;

    final static String[] WORD_LIBRARY = { "COMPUTER", "UTAS", "JAVA", "INFORMATION", "COMMUNICATION", "TECHNOLOGY",
            "PROGRAMMING", "CLASS", "FUNCTION", "INTERNET", "LAPTOP", "DESKTOP", "PROCESSOR", "SERVER", "INTEGER",
            "FLOAT", "BOOLEAN", "WINDOWS", "APPLE", "SOFTWARE", "HARDWARE", "SECURITY", "LECTURE", "TUTORIAL", "PASS",
            "LINUX", "GOOGLE", "FACEBOOK", "LINKEDIN", "METHOD", "BROWSER", "APPLICATION", "DATA", "DEVELOPMENT",
            "ETHICS", "VULNERABILITY", "SYSTEM" }; // Library of words that are possible to be hidden

    /**
     * Starts the game board with the size depending on difficulty selected.
     * 
     * @param difficulty Difficulty setting chosen by user, used to set gameboard
     *                   and wordList size
     */
    public void initGameBoard(WordSearch.Difficulty difficulty) {
        switch (difficulty) {
        case EASY:
            boardSize = EASY_BOARD_SIZE;
            randomizeWordList(EASY_WORD_SIZE); // Sets wordList to 4 random words
            startGameBoard(boardSize); // Starts gameboard with size of 15x15
            break;
        case MEDIUM:
            boardSize = MEDIUM_BOARD_SIZE;
            randomizeWordList(MEDIUM_WORD_SIZE); // Sets wordList to 8 random words
            startGameBoard(boardSize); // Starts gameboard with size of 20x20
            break;
        case HARD:
            boardSize = HARD_BOARD_SIZE;
            randomizeWordList(HARD_WORD_SIZE); // Sets wordList to 12 random words
            startGameBoard(boardSize); // Starts gameboard with size of 25x25
            break;
        }
        populateGameBoard(); // Places the words on the board
        fillGameBoard(); // Randomizes unfilled positions to a random Char
        // printGameBoard();
    }
    
    /**
     * Initializes the gameBoard
     * 
     * @param size Sets the 2d array size as sizeXsize (e.g a 15x15 grid)
     */
    public void startGameBoard(int size) {
        gameBoard = new char[size][size];
    }
    
    /**
     * Places the words in wordList in the board. Runs checks so that all positions
     * are valid and not overwriting another word
     */
    public void populateGameBoard() {
        Random rand = new Random(); // Random value generator
        int modifier; // Changes the location that is currently being written to
        int orientation, randCol, randRow; // These store the values of the orientaion, the column of the first letter,
                                           // and the row of the first letter respectively
        boolean added = false; // Stores whether ir not a given word has been added

        for (int i = 0; i < wordList.getSize(); i++) {
            do {
                added = false;
                orientation = rand.nextInt(4);
                randRow = rand.nextInt(boardSize);
                randCol = rand.nextInt(boardSize);
                if (checkInBounds(gameBoard, orientation, randRow, randCol, wordList.getValue(i).length())) {
                    if (checkForOverwrite(gameBoard, orientation, randRow, randCol, wordList.getValue(i).length())) {
                        for (int j = 0; j < wordList.getValue(i).length(); j++) {
                            modifier = j; // Increments the position that is currently being written to
                            switch (orientation) {
                            case 0: // If orientation is verical up
                                gameBoard[randRow - modifier][randCol] = wordList.getValue(i).charAt(modifier);
                                break;
                            case 1: // If orientation is horizontal right
                                gameBoard[randRow][randCol + modifier] = wordList.getValue(i).charAt(modifier);
                                break;
                            case 2: // If orientation is verical down
                                gameBoard[randRow + modifier][randCol] = wordList.getValue(i).charAt(modifier);
                                break;
                            case 3: // If orientation is horizontal left
                                gameBoard[randRow][randCol - modifier] = wordList.getValue(i).charAt(modifier);
                                break;
                            }
                            added = true;
                        }
                    }
                }
            } while (!added); // Loop until word is added
        }
    }

    /**
     * Prevents words from randomizing into positions that cause them to overwrite
     * already writen words.
     * 
     * @param orientation Orientation that the word will be put into.
     * @param row         Row that the first letter of the word will be placed in
     * @param col         Column that the first letter of the word will be place in
     * @param length      Length of the word that is being checked
     * 
     * @return Returns true if word will not overwrite another word
     */
    public boolean checkForOverwrite(char[][] gameBoard, int orientation, int row, int col, int length) {
        boolean checker = true;
        int modifier;

        for (int i = 0; i < length; i++) {
            modifier = i;
            switch (orientation) {
            case 0:
                if (gameBoard[row - modifier][col] != 0) {
                    checker = false;
                }
                break;
            case 1:
                if (gameBoard[row][col + modifier] != 0) {
                    checker = false;
                }
                break;
            case 2:
                if (gameBoard[row + modifier][col] != 0) {
                    checker = false;
                }
                break;
            case 3:
                if (gameBoard[row][col - modifier] != 0) {
                    checker = false;
                }
                break;
            }
        }
        return checker;
    }

    /**
     * Prevents words from randomising into positions that cause them to pass
     * outside of the gameBoard array bounds
     * 
     * @param orientation Orientation that the word will be put into.
     * @param row         Row that the first letter of the word will be placed in
     * @param col         Column that the first letter of the word will be place in
     * @param length      Length of the word that is being checked
     * 
     * @return Returns true if word will not pass out of array bounds
     */
    public boolean checkInBounds(char[][] gameBoard, int orientation, int row, int col, int length) {
        boolean inbounds = false;

        switch (orientation) {
        case 0:
            if ((row + 1) - length >= 0) {
                inbounds = true;
            }
            break;
        case 1:
            if (col + length <= boardSize) {
                inbounds = true;
            }
            break;
        case 2:
            if ((row - 1) + length < boardSize) {
                inbounds = true;
            }
            break;
        case 3:
            if ((col + 1) - length >= 0) {
                inbounds = true;
            }
        }

        return inbounds;
    }

    /**
     * Randomizes the words in wordList
     * 
     * @param count Sets how many words to be put in wordList
     */
    public void randomizeWordList(int count) {
        Random rand = new Random();
        wordList.clearArrayList(); // Clears the word list
        int wordsAdded = 0; // Tracks how many words have been added already
        while (wordsAdded < count) {
            int randomVal = rand.nextInt(WORD_LIBRARY.length);

            if (!wordList.checkContains(WORD_LIBRARY[randomVal])) { // If word isn't in wordList yet
                wordList.addWord(WORD_LIBRARY[randomVal]);
                wordsAdded++;
            }
        }
    }

    /**
     * Loops through the gameboard and anywhere there is a blank position fills it
     * with a random char
     */
    public void fillGameBoard() {
        Random randChar = new Random(); // Random value generator
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (gameBoard[i][j] == 0) {
                    gameBoard[i][j] = ALPHABET.charAt(randChar.nextInt(ALPHABET.length()));
                }
            }
        }
    }

    /**
     * Adds currently selected character to wordSelect, then checks if a word is
     * complete
     */
    public void selectFoundWord(int rowSelection, int colSelection) {
        addLetter(getBoardPos(rowSelection, colSelection));
        checkForFinishedWord(rowSelection, colSelection);
    }

    /**
     * Checks that the user hasnt cheated, converts the wordSelect ArrayList into
     * string stored in wordIn, then checks if wordIn is a word in wordList.
     * 
     * <p>
     * If a wordIn matches a word, removes the word from wordList, and resets
     * wordSelect and wordIn to hold nothing
     */
    public void checkForFinishedWord(int rowSelection, int colSelection) {
        checkForSameRowCol(rowSelection, colSelection);
        wordSelectToString();
        for (int i = 0; i < getWordListSize(); i++) {
            if (wordIn.equals(getWordListValue(i))) {
                removeWordListValue(i);
                clearWordSelect();
                wordIn = "";
            }
        }
    }

    /**
     * Checks that the user hasn't cheated by clearing wordSelected if the most
     * recent letter is in a different row and column to the first letter selected
     */
    public void checkForSameRowCol(int rowSelection, int colSelection) {
        if (wordIn.length() == 0) {
            oldCol = colSelection;
            oldRow = rowSelection;
        } else if (!(oldRow == rowSelection || oldCol == colSelection)) {
            clearWordSelect();
        }
    }

    /**
     * Converts the wordSelect ArrayList to a string stored in wordIn by concating
     * each character on the the end of the string
     */
    public void wordSelectToString() {
        wordIn = "";
        for (int i = 0; i < getWordSelectSize(); i++) {
            wordIn = wordIn.concat(String.valueOf(getWordSelectValue(i)));
        }
    }

    /**
     * Removes the last letter added to the arrayList wordSelect, then calls
     * wordSelectToString() to refresh the game window
     */
    public void deleteLastLetter() {
        if (wordSelect.getSize() > 0) {
            wordSelect.removeLastValue();
        }
    }

    /**
     * Adds a given letter to wordSelect
     */
    public void addLetter(char val) {
        wordSelect.addLetter(val);
    }

    /**
     * Clears all values from wordSelect
     */
    public void clearWordSelect() {
        wordSelect.clearArrayList();
    }

    /**
     * Returns the size of wordSelect
     */
    public int getWordSelectSize() {
        return wordSelect.getSize();
    }

    /**
     * Returns the char at a given position of wordSelect
     */
    public char getWordSelectValue(int pos) {
        return wordSelect.getValue(pos);
    }

    /**
     * Returns the size of wordList
     */
    public int getWordListSize() {
        return wordList.getSize();
    }

    /**
     * Returns the value at a given position in wordList
     */
    public String getWordListValue(int pos) {
        return wordList.getValue(pos);
    }

    /**
     * Removes a given position from wordList
     */
    public void removeWordListValue(int pos) {
        wordList.removeWordListValue(pos);
    }

    /**
     * Returns the character at a given position in the gameBoard
     */
    public char getBoardPos(int row, int col) {
        return gameBoard[row][col];
    }

    /**
     * Returns the value of the boardSize variable
     */
    public int getBoardSize() {
        return boardSize;
    }

    /**
     * Returns the value of wordIn
     */
    public String getWordIn() {
        return wordIn;
    }
}
