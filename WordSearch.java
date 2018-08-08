import javafx.animation.AnimationTimer;
import javafx.application.*;
import javafx.event.*;
import javafx.scene.shape.Box;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.control.*;
import javafx.scene.canvas.*;
import javafx.scene.input.*;
import javafx.stage.Stage;

/**
 * Basic word search game using JavaFX, created for submission for KIT101 custom
 * program task 7.4DN.
 * 
 * This program manages the GUI, and uses JavaFX
 * 
 * The following files are also required to run:
 * 
 * Base.java
 * WordList.java
 * WordSelect.java
 * 
 * @version 2.0
 * @author Seth Hilder (478393)
 * @version 14 May 2018
 */
public class WordSearch extends Application implements EventHandler<KeyEvent> {
    enum Difficulty {
        EASY, MEDIUM, HARD
    }

    final int windowSize = 600; // Sets the window size
    int boardSize;
    int colSelection = 4; // The column that the user currently has selected
    int rowSelection = 6; // The row that the user currently has selected
    final int VBOX_SPACING = 25;
    Difficulty difficulty; // Enum variable to contain difficulty setting
    Scene difficultyScene, mainScene, endScene; // The three scenes to contain the three views of Difficulty Selection,
                                                // Main Game, and Game Over
    GraphicsContext gc; // Graphics Context used to print the Main Game screen
    String wordIn = new String(); // Stores the word that the user selects
    Base game = new Base();

    public static void main(String[] args) {
        launch(args); // Runs start()
    }

    /**
     * Runs the basic game starting, and printing of the start and end scene, as well as button
     * handling and the Animation Timer for the mainGame
     */
    public void start(Stage gameStage) {
        gameStage.setTitle("Word Search v2.0");

        Label introLabel = new Label("Welcome to this Word Search");
        Label instructions1 = new Label("Use arrow keys and enter to select each letter of the word");
        Label instructions2 = new Label("Use backspace to delete the most recent letter");
        Label difficultyLabel = new Label("Select Difficulty:");
        Button difficultyEasy = new Button("Easy");
        Button difficultyMedium = new Button("Medium");
        Button difficultyHard = new Button("Hard");

        VBox rootForDifficulty = new VBox(VBOX_SPACING);
        rootForDifficulty.getChildren().addAll(introLabel, instructions1, instructions2, difficultyLabel,
                difficultyEasy, difficultyMedium, difficultyHard); // Adds labels and buttons
        difficultyScene = new Scene(rootForDifficulty, windowSize, windowSize); // Sets the window size and content to
                                                                                // show

        Canvas canvas = new Canvas(windowSize, windowSize);
        gc = canvas.getGraphicsContext2D();

        Box keyboardNode = new Box();
        keyboardNode.setFocusTraversable(true);
        keyboardNode.requestFocus();
        keyboardNode.setOnKeyPressed(this);

        Group rootForMain = new Group();
        rootForMain.getChildren().addAll(canvas, keyboardNode); // Adds canvas for printing to and the keyboard listener
        mainScene = new Scene(rootForMain, windowSize, windowSize); // Sets the window size and content to show

        Label finalLabel1 = new Label("Well Done!");
        Label finalLabel2 = new Label("You Won!");
        Label finalLabel3 = new Label("Play Again?");
        Button againYes = new Button("Yes");
        Button againNo = new Button("No");

        VBox rootForEndScene = new VBox(VBOX_SPACING);
        rootForEndScene.getChildren().addAll(finalLabel1, finalLabel2, finalLabel3, againYes, againNo); // Adds labels
                                                                                                        // and buttons
        endScene = new Scene(rootForEndScene, windowSize, windowSize); // Sets the window size and content to show

        AnimationTimer mainGame = new AnimationTimer() {
            @Override
            public void handle(long arg0) {
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // Clears the canvas to prevent jagged
                                                                           // looking characters
                run();
                wordIn = game.getWordIn();
                checkForEnd(gameStage);
            }
        };

        gameStage.setScene(difficultyScene); // Sets the inital stage to be shown
        gameStage.show(); // Shows the stage

        difficultyEasy.setOnAction(e -> { // When Easy selected
            difficulty = Difficulty.EASY; // Sets difficulty level
            game.initGameBoard(difficulty);
            boardSize = game.getBoardSize();
            gameStage.setScene(mainScene); // Sets the stage to show main game scene
            mainGame.start(); // Starts the animation timer that prints the gameboard

        });

        difficultyMedium.setOnAction(e -> { // When Medium selected
            difficulty = Difficulty.MEDIUM; // Sets difficulty level
            game.initGameBoard(difficulty);
            boardSize = game.getBoardSize();
            gameStage.setScene(mainScene); // Sets the stage to show main game scene
            mainGame.start(); // Starts the animation timer that prints the gameboard
        });

        difficultyHard.setOnAction(e -> { // When Hard selected
            difficulty = Difficulty.HARD; // Sets difficulty level
            game.initGameBoard(difficulty);
            boardSize = game.getBoardSize();
            gameStage.setScene(mainScene); // Sets the stage to show main game scene
            mainGame.start(); // Starts the animation timer that prints the gameboard
        });

        againYes.setOnAction(e -> { // If use chooses to play again
            mainGame.stop(); // Stops main game loop
            gameStage.setScene(difficultyScene); // Resets stage to show starting screen
        });

        againNo.setOnAction(e -> Platform.exit()); // If user doesnt want to play again, quit
    }

    /**
     * Event handler for keyboard events
     */
    public void handle(KeyEvent e) {

        if (e.getCode() == KeyCode.LEFT) { // If left button pressed
            if (colSelection - 1 >= 0) { // And not on edge
                colSelection--; // Move selection square to the left
            }
        }
        if (e.getCode() == KeyCode.RIGHT) { // If right button pressed
            if (colSelection + 1 < boardSize) { // And not on edge
                colSelection++; // Move selection square to the right
            }
        }
        if (e.getCode() == KeyCode.UP) { // If up button pressed
            if (rowSelection - 1 >= 0) { // And not on edge
                rowSelection--; // Move selection square up
            }
        }
        if (e.getCode() == KeyCode.DOWN) { // If down button pressed
            if (rowSelection + 1 < boardSize) { // And not on edge
                rowSelection++; // Move selection square down
            }
        }
        if (e.getCode() == KeyCode.ENTER) { // If ENTER is presed
            game.selectFoundWord(rowSelection, colSelection);
        }
        if (e.getCode() == KeyCode.DELETE || e.getCode() == KeyCode.BACK_SPACE) { // If delete or backspace is pressed
            game.deleteLastLetter();
            game.wordSelectToString();
        }
    }

    /**
     * Is called in the animation timer, at a rate of 60 times a second. Is
     * responsible for printing all graphic components of the main game
     */
    public void run() {
        gcPrintGameBoard();
        showSelection();
        printWordList();
        printSelectedWord();
    }

    /**
     * Prints the word list
     */
    public void printWordList() {
        int rowCounter = 0;
        int colCounter = 0;
        int indent = 15;
        for (int i = 0; i < game.getWordListSize(); i++) {
            gc.fillText(game.getWordListValue(i), (indent + (125 * colCounter)), (indent + (20 * rowCounter)));
            colCounter++;
            if (colCounter == 4) {
                colCounter = 0;
                rowCounter++;
            }
        }
    }

    /**
     * This prints the contents of the gameBoard char array to the console, to be
     * used for debugging purposes only.
     * 
     * <p>
     * To run, uncomment the "printGameBoard()" line in initGameBoard()
     * <p>
     */
    public void printGameBoard() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                System.out.print(game.getBoardPos(i, j) + "  ");
            }
            System.out.println();
        }
    }

    /**
     * This prints the gameBoard char array to the canvas, and is used to
     * graphically show the game board to the user
     */
    public void gcPrintGameBoard() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                gc.fillText(String.valueOf(game.getBoardPos(i, j)), (25 + (20 * j)), (100 + (20 * i)));
            }
        }
    }

    /**
     * Prints the black square with white text that indicates the currently selected
     * char
     */
    public void showSelection() {
        gc.save(); // Saves gc state before printing
        gc.setFill(Color.BLACK);
        gc.fillRect((22 + (20 * colSelection)), (88 + (20 * rowSelection)), 15, 15);
        gc.setFill(Color.WHITE);
        gc.fillText(String.valueOf(game.getBoardPos(rowSelection, colSelection)), (25 + (20 * colSelection)),
                (100 + (20 * rowSelection)));
        gc.restore(); // Restores saved gc state
    }

    /**
     * Displays the word that the user has entered on the canvas
     */
    public void printSelectedWord() {
        gc.fillText("Word Selected: " + wordIn, 15, 75);
    }

    /**
     * Checks if wordList is empty, and if it is shows the endScene
     * 
     * @param gameStage The stage for the main JavaFX screen that is changed to the
     *                  final screen
     */
    public void checkForEnd(Stage gameStage) {
        if (game.getWordListSize() == 0) {
            gameStage.setScene(endScene);
        }
    }
}