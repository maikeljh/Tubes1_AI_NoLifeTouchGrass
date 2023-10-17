import javafx.scene.control.Button;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import javafx.util.Pair;

/**
 * BotMinimax class.  It uses minimax algorithm to evaluate 
 * best movement for current state
 * @author Marcel Ryan Antony 13521127
 * @author Raynard Tanadi 13521143
 * 
 */
public class BotMinimax extends Bot {
    /**
     * Constructor
     * @param ownedSymbol Bot's symbol
     * @param enemySymbol Enemy's symbol
     *
     */
    public BotMinimax(char ownedSymbol, char enemySymbol) {
        this.ownedSymbol = ownedSymbol;
        this.enemySymbol = enemySymbol;
    }

    /**
     * Main function to find best movement with minimax algorithm
     * @param board Game's board containing X and O symbols
     * @param depth Current tree's depth
     * @param alpha Alpha value for alpha beta pruning
     * @param beta Beta value for alpha beta pruning
     * @param ownedSymbol Bot's symbol
     * @param enemySymbol Enemy's symbol
     * @param botTurn Determines if current turn is bot or enemy
     * @return Pair[]
     *
     */
    public int minimax_search(char[][] board, int depth, int alpha, int beta, int enemyScore, int ownedScore, boolean botTurn, long startTime){
        int[] tempScore;
        int row = board.length;
        int col = board[0].length;

        // Cut depth tree or if board already full
        if (depth == 12 || checkFull(board) || System.currentTimeMillis() - startTime >= 4800){
            return objective_function(enemyScore, ownedScore);
        }

        // Maximizer
        if (botTurn){
            int bestScore = Integer.MIN_VALUE;

            // Optimizing minimax with beam search
            Pair[] beam = this.evaluate_beam(board, true, 5);

            for (Pair pair : beam){
                int i = (int) pair.getKey();
                int j = (int) pair.getValue();

                if (board[i][j] == ' '){
                    char[][] tempBoard = copyBoard(board);

                    tempBoard[i][j] = this.ownedSymbol;
                    Pair currentCoor = new Pair(i, j);
                    checkAdjacency(tempBoard, currentCoor, "Bot");

                    tempScore = updateScore(tempBoard);
                    ownedScore = tempScore[0];
                    enemyScore = tempScore[1];
                    
                    int score = minimax_search(tempBoard, depth + 1, alpha, beta, enemyScore, ownedScore, false, startTime);
                    bestScore = Math.max(score, bestScore);
                    alpha = Math.max(alpha, bestScore);

                    // Alpha beta pruning
                    if (alpha >= beta){
                        break;
                    }
                }
            }
            return bestScore;
        } else {
            // Minimizer
            int bestScore = Integer.MAX_VALUE;

            // Optimizing minimax with beam search
            Pair[] beam = this.evaluate_beam(board, false, 5);

            for (Pair pair : beam){
                int i = (int) pair.getKey();
                int j = (int) pair.getValue();

                if (board[i][j] == ' '){
                    char[][] tempBoard = copyBoard(board);

                    tempBoard[i][j] = this.enemySymbol;
                    Pair currentCoor = new Pair(i, j);
                    checkAdjacency(tempBoard, currentCoor, "Enemy");

                    tempScore = updateScore(tempBoard);
                    ownedScore = tempScore[0];
                    enemyScore = tempScore[1];
                    
                    int score = minimax_search(tempBoard, depth + 1, alpha, beta, enemyScore, ownedScore, true, startTime);
                    bestScore = Math.min(score, bestScore);
                    beta = Math.min(beta, bestScore);

                    // Alpha beta pruning
                    if (alpha >= beta){
                        break;
                    }
                }
            }
            return bestScore;
        }
    }

    /**
     * Function that returns array of W best successors with beam search
     * @param board Game's board containing X and O symbols
     * @param isBot Determines if current turn is bot or enemy
     * @param width Determines the amount of selected best successors
     * @return Pair[]
     *
     */
    public Pair[] evaluate_beam(char[][] board, boolean isBot, int width) {
        int row = board.length;
        int col = board[0].length;
        Pair[] result = new Pair[64];

        /* Bedain bot dan musuh, bot akan mengambil best successorsnya berdasarkan objective tertinggi,
           sedangkan musuh akan mengambil best successornya berdasarkan objective terendah
           (Tetap mengikut prinsip maximizer minimizer)
        */

        if (isBot) {
            // Calculate amount of empty cells to create array of pair
            int numValidMoves = 0;
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    if (board[i][j] == ' ') {
                        numValidMoves++;
                    }
                }
            }
            
            if (numValidMoves < 64) {
                result = new Pair[numValidMoves];
            }

            // Evaluate all successors
            int idx = 0;
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    if (board[i][j] == ' ') {
                        char[][] tempBoard = copyBoard(board);

                        tempBoard[i][j] = this.ownedSymbol;
                        Pair currentCoor = new Pair(i, j);
                        checkAdjacency(tempBoard, currentCoor, "Bot");

                        int[] tempScore = updateScore(tempBoard);

                        result[idx++] = new Pair(currentCoor, this.objective_function(tempScore[1], tempScore[0]));
                    }
                }
            }

            // Sort all successor based on objective value DESCENDING
            Arrays.sort(result, (a, b) -> Integer.compare((int) b.getValue(), (int) a.getValue()));

            // Return W best successors or available best successors if amount less than width
            if(result.length < width){
                Pair[] bestResults = new Pair[result.length];
                for (int i = 0; i < result.length; i++) {
                    bestResults[i] = (Pair) result[i].getKey();
                }

                return bestResults;
            } else {
                Pair[] bestResults = new Pair[width];
                for (int i = 0; i < width; i++) {
                    bestResults[i] = (Pair) result[i].getKey();
                }

                return bestResults;
            }
        } else {
            // For enemy
            // Calculate amount of empty cells to create array of pair
            int numValidMoves = 0;
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    if (board[i][j] == ' ') {
                        numValidMoves++;
                    }
                }
            }

            if (numValidMoves < 64) {
                result = new Pair[numValidMoves];
            }

            // Evaluate all successors
            int idx = 0;
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    if (board[i][j] == ' ') {
                        char[][] tempBoard = copyBoard(board);

                        tempBoard[i][j] = this.enemySymbol;
                        Pair currentCoor = new Pair(i, j);
                        checkAdjacency(tempBoard, currentCoor, "Enemy");

                        int[] tempScore = updateScore(tempBoard);


                        result[idx++] = new Pair(currentCoor, this.objective_function(tempScore[1], tempScore[0]));
                    }
                }
            }

            // Sort all successor based on objective value ASCENDING
            Arrays.sort(result, (a, b) -> Integer.compare((int) a.getValue(), (int) b.getValue()));

            // Return W best successors or available best successors if amount less than width
            if(result.length < width){
                Pair[] bestResults = new Pair[result.length];
                for (int i = 0; i < result.length; i++) {
                    bestResults[i] = (Pair) result[i].getKey();
                }

                return bestResults;
            } else {
                Pair[] bestResults = new Pair[width];
                for (int i = 0; i < width; i++) {
                    bestResults[i] = (Pair) result[i].getKey();
                }

                return bestResults;
            }
        }
    }

    /**
     * Function that returns the choosen move based on minimax algorithm
     * @param roundsLeft Remaining rounds
     * @param buttons Game board buttons containing O and X symbols
     * @return int[]
     *
     */
    public int[] move(int roundsLeft, Button[][] buttons){
        int[] bestMove = new int[2];
        int bestScore = Integer.MIN_VALUE;
        int[] tempScore;
        
        // Set timer
        long startTime = System.currentTimeMillis();

        // Fill board
        char[][] board = createBoardFromButtons(buttons);

        int row = board.length;
        int col = board[0].length;

        // Optimizing minimax with beam search
        Pair[] beam = this.evaluate_beam(board, true, 5);
        for (Pair pair : beam){
            // Check if it almost passes 5 seconds
            long currentTime = System.currentTimeMillis();
            if (currentTime - startTime >= 4800) {
                return bestMove;
            }

            int i = (int) pair.getKey();
            int j = (int) pair.getValue();

            if (board[i][j] == ' '){
                char[][] tempBoard = copyBoard(board);

                tempBoard[i][j] = this.ownedSymbol;
                Pair currentCoor = new Pair(i, j);
                checkAdjacency(tempBoard, currentCoor, "Bot");

                tempScore = updateScore(tempBoard);
                int ownedScore = tempScore[0];
                int enemyScore = tempScore[1];
                
                int score = minimax_search(tempBoard, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, enemyScore, ownedScore, false, startTime);

                if (score > bestScore) {
                    bestScore = score;
                    bestMove[0] = i;
                    bestMove[1] = j;
                }
            }
        }
        return bestMove;
    }
}
    