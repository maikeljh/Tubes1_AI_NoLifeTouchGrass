import javafx.scene.control.Button;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import javafx.util.Pair;

public class BotMinimax extends Bot {
    private long startTime;

    //Minimax algorithm
    public int minimax_search(char[][] board, int depth, int alpha, int beta, int playerXScore, int playerOScore, boolean botTurn){
        int[] tempScore;
        int row = board.length;
        int col = board[0].length;

        if (depth == 11){
            return objective_function(playerXScore, playerOScore);
        }

        //Maximizing
        if (botTurn){
            int bestScore = Integer.MIN_VALUE;
            Pair[] beam = this.evaluate_beam(board, true, 5);
            for (Pair pair : beam){
                int i = (int) pair.getKey();
                int j = (int) pair.getValue();
                if (board[i][j] == ' '){
                    char[][] tempBoard = new char[row][col];
                    for (int a = 0; a < row; a++){
                        System.arraycopy(board[a], 0, tempBoard[a], 0, col);
                    }

                    tempBoard[i][j] = 'O';
                    Pair currentCoor = new Pair(i, j);
                    checkAdjacency(tempBoard, currentCoor, "Bot");

                    tempScore = updateScore(tempBoard);
                    playerOScore = tempScore[0];
                    playerXScore = tempScore[1];
                    
                    int score = minimax_search(tempBoard, depth + 1, alpha, beta, playerXScore, playerOScore, false);
                    bestScore = Math.max(score, bestScore);
                    alpha = Math.max(alpha, bestScore);

                    if (alpha >= beta){
                        break;
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            Pair[] beam = this.evaluate_beam(board, false, 5);
            for (Pair pair : beam){
                int i = (int) pair.getKey();
                int j = (int) pair.getValue();
                if (board[i][j] == ' '){
                    char[][] tempBoard = new char[row][col];
                    for (int a = 0; a < row; a++){
                        System.arraycopy(board[a], 0, tempBoard[a], 0, col);
                    }

                    tempBoard[i][j] = 'X';
                    Pair currentCoor = new Pair(i, j);
                    checkAdjacency(tempBoard, currentCoor, "Player");

                    tempScore = updateScore(tempBoard);
                    playerOScore = tempScore[0];
                    playerXScore = tempScore[1];
                    
                    int score = minimax_search(tempBoard, depth + 1, alpha, beta, playerXScore, playerOScore, true);
                    bestScore = Math.min(score, bestScore);
                    beta = Math.min(beta, bestScore);

                    if (alpha >= beta){
                        break;
                    }
                }
            }
            return bestScore;
        }
    }

    public Pair[] evaluate_beam(char[][] board, boolean isBot, int width) {
        int row = board.length;
        int col = board[0].length;
        Pair[] result = new Pair[64];

        if (isBot) {
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

            int idx = 0;
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    if (board[i][j] == ' ') {
                        char[][] tempBoard = new char[row][col];
                        for (int a = 0; a < row; a++) {
                            System.arraycopy(board[a], 0, tempBoard[a], 0, col);
                        }
                        tempBoard[i][j] = 'O';
                        Pair currentCoor = new Pair(i, j);
                        checkAdjacency(tempBoard, currentCoor, "Bot");

                        int[] tempScore = updateScore(tempBoard);


                        result[idx++] = new Pair(currentCoor, this.objective_function(tempScore[1], tempScore[0]));
                    }
                }
            }
            Arrays.sort(result, (a, b) -> Integer.compare((int) b.getValue(), (int) a.getValue()));

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

            int idx = 0;
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    if (board[i][j] == ' ') {
                        char[][] tempBoard = new char[row][col];
                        for (int a = 0; a < row; a++) {
                            System.arraycopy(board[a], 0, tempBoard[a], 0, col);
                        }
                        tempBoard[i][j] = 'X';
                        Pair currentCoor = new Pair(i, j);
                        checkAdjacency(tempBoard, currentCoor, "Player");

                        int[] tempScore = updateScore(tempBoard);


                        result[idx++] = new Pair(currentCoor, this.objective_function(tempScore[1], tempScore[0]));
                    }
                }
            }
            Arrays.sort(result, (a, b) -> Integer.compare((int) a.getValue(), (int) b.getValue()));

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

    public int[] move(int playerXScore, int playerOScore, int roundsLeft, Button[][] buttons){
        int[] bestMove = new int[2];
        int bestScore = Integer.MIN_VALUE;
        int[] tempScore;
        char[][] board = new char[buttons.length][buttons[0].length];
        int row = board.length;
        int col = board[0].length;

        //fill board
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                String buttonText = buttons[i][j].getText();
                if (buttonText.isEmpty()) {
                    board[i][j] = ' ';
                } else {
                    board[i][j] = buttonText.charAt(0);
                }
            }
        }

        Pair[] beam = this.evaluate_beam(board, true, 5);
        for (Pair pair : beam){
            int i = (int) pair.getKey();
            int j = (int) pair.getValue();
            if (board[i][j] == ' '){
                char[][] tempBoard = new char[row][col];
                for (int a = 0; a < row; a++){
                    System.arraycopy(board[a], 0, tempBoard[a], 0, col);
                }

                tempBoard[i][j] = 'O';
                Pair currentCoor = new Pair(i, j);
                checkAdjacency(tempBoard, currentCoor, "Bot");

                tempScore = updateScore(tempBoard);
                playerOScore = tempScore[0];
                playerXScore = tempScore[1];
                
                int score = minimax_search(tempBoard, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, playerXScore, playerOScore, false);

                if (score > bestScore) {
                    bestScore = score;
                    bestMove[0] = i;
                    bestMove[1] = j;
                }
            }
        }
        return bestMove;
    }

    public int[] updateScore(char[][] board) {
        // Function to update score player and bot
        int scorePlayer = 0;
        int scoreBot = 0;
        int[] score = new int[2];
    
        // calculate bot and player score
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 'X') {
                    scorePlayer++;
                } else if (board[i][j] == 'O') {
                    scoreBot++;
                }
            }
        }
        score[0] = scoreBot;
        score[1] = scorePlayer;
        return score;
    }
}
    