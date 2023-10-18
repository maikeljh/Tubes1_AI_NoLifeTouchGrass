import javafx.scene.control.Button;
import javafx.util.Pair;

/**
 * Bot class.  It uses it's choosen algorithm to evaluate 
 * best movement for current state
 *
 */
public abstract class Bot {
    public char ownedSymbol;
    public char enemySymbol;

    /**
     * Main objective function that is used by the bot
     * @param enemyScore Enemy's score
     * @param ownedScore Bot's score
     * @return int
     *
     */
    public int objective_function(int enemyScore, int ownedScore){
        return ownedScore - enemyScore;
    }

    /**
     * Abstract function that returns the choosen move based on bot's algorithm
     * @param roundsLeft Remaining rounds
     * @param buttons Game board buttons containing O and X symbols
     * @return int[]
     *
     */
    public abstract int[] move(int roundsLeft, Button[][] buttons);

    /**
     * Function to check if the coordinate is inside board game or not
     * @param row index of row
     * @param col index of column
     * @return boolean
     *
     */
    public boolean borderCheck(int row, int col){
        if (row >= 0 && row < 8 && col >= 0 && col < 8){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Function to replace symbols in board game based on the adjacency rules and current turn
     * @param board Current game state
     * @param coor The coordinate where the last button was placed
     * @param currentMove Determines if the current turn is bot or enemy
     *
     */
    public void checkAdjacency(char[][] board, Pair coor, String currentMove){
        int row = (int) coor.getKey();
        int col = (int) coor.getValue();

        if (currentMove.equals("Enemy")){
            // Under
            if (borderCheck(row-1, col)){
                if (board[row-1][col] == ownedSymbol){
                    board[row-1][col] = enemySymbol;
                }
            }

            // Above
            if (borderCheck(row+1, col)){
                if (board[row+1][col] == ownedSymbol){
                    board[row+1][col] = enemySymbol;
                }
            }
            
            // Left
            if (borderCheck(row, col-1)){
                if (board[row][col-1] == ownedSymbol){
                    board[row][col-1] = enemySymbol;
                }
            }

            // Right
            if (borderCheck(row, col+1)){
                if (board[row][col+1] == ownedSymbol){
                    board[row][col+1] = enemySymbol;
                }
            }
        } else {
            // For Bot
            // Under
            if (borderCheck(row-1, col)){
                if (board[row-1][col] == enemySymbol){
                    board[row-1][col] = ownedSymbol;
                }
            }

            // Above
            if (borderCheck(row+1, col)){
                if (board[row+1][col] == enemySymbol){
                    board[row+1][col] = ownedSymbol;
                }
            }
            
            // Left
            if (borderCheck(row, col-1)){
                if (board[row][col-1] == enemySymbol){
                    board[row][col-1] = ownedSymbol;
                }
            }

            // Right
            if (borderCheck(row, col+1)){
                if (board[row][col+1] == enemySymbol){
                    board[row][col+1] = ownedSymbol;
                }
            }
        }
    }

    /**
     * Function to create 2D array board game from JavaFX Buttons
     * @param buttons 2D Array of buttons from board game
     * @return char[][]
     *
     */
    public char[][] createBoardFromButtons(Button[][] buttons) {
        char[][] board = new char[buttons.length][buttons[0].length];
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                String buttonText = buttons[i][j].getText();
                if (buttonText.isEmpty()) {
                    board[i][j] = ' ';
                } else {
                    board[i][j] = buttonText.charAt(0);
                }
            }
        }

        return board;
    }

    /**
     * Function to copy 2D array board game from original board
     * @param originalBoard original board to copy
     * @return char[][]
     *
     */
    public char[][] copyBoard(char[][] originalBoard){
        int rows = originalBoard.length;
        int cols = originalBoard[0].length;
        char[][] copy = new char[rows][cols];
        for (int i = 0; i< rows; i++){
            for (int j = 0; j< cols; j++){
                copy[i][j] = originalBoard[i][j];
            }
        }
        return copy;
    }

    /**
     * Function to check if the board is full or not
     * @param board Current board state
     * @return boolean
     *
     */
    public boolean checkFull(char[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Function to calculate bot and enemy's score based on a board state
     * @param board Game's board containing X and O symbols
     * @return int[]
     *
     */
    public int[] updateScore(char[][] board) {
        // Function to update score enemy and bot
        int scoreEnemy = 0;
        int scoreBot = 0;
        int[] score = new int[2];
    
        // calculate bot and enemy score
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == this.enemySymbol) {
                    scoreEnemy++;
                } else if (board[i][j] == this.ownedSymbol) {
                    scoreBot++;
                }
            }
        }
        score[0] = scoreBot;
        score[1] = scoreEnemy;
        return score;
    }

    /**
     * Function to calculate objective function value based on a board state
     * @param board Game's board containing X and O symbols
     * @return int
     *
     */
    public int objective_value(char[][] board){
        int[] scores = updateScore(board);
        int ownedScore = scores[0];
        int enemyScore = scores[1];

        return objective_function(enemyScore, ownedScore);
    }
}
