import javafx.scene.control.Button;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;

public class BotMinimax extends Bot {
    private long startTime;

    //Minimax algorithm
    public int minimax_search(char[][] board, int depth, int alpha, int beta, int playerXScore, int playerOScore, boolean botTurn){
        int[] tempScore;
        int row = board.length;
        int col = board[0].length;

        // Limiting depth
        if (depth == 4){
            return objective_function(playerXScore, playerOScore);
        }

        //Maximizing
        if (botTurn){
            int bestScore = Integer.MIN_VALUE;

            for (int i = 0; i < row; i++){
                for (int j = 0; j< col; j++){
                    if (board[i][j] == ' '){

                        // Copy board
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

                        //alpha beta pruning
                        if (alpha >= beta){
                            break;
                        }
                    }
                }
            }
            return bestScore;
        }
        //Minimizing
        else {
            int bestScore = Integer.MAX_VALUE;

            for (int i = 0; i < row; i++){
                for (int j = 0; j< col; j++){
                    if (board[i][j] == ' '){

                        //copy board
                        char[][] tempBoard = new char[row][col];
                        for (int a = 0; a < row; a++){
                            System.arraycopy(board[a], 0, tempBoard[a], 0, col);
                        }

                        tempBoard[i][j] = 'X';
                        Pair currentCoor = new Pair(i, j);
                        checkAdjacency(tempBoard, currentCoor, "Bot");

                        tempScore = updateScore(tempBoard);
                        playerOScore = tempScore[0];
                        playerXScore = tempScore[1];
                        
                        int score = minimax_search(tempBoard, depth + 1, alpha, beta, playerXScore, playerOScore, true);
                        bestScore = Math.min(score, bestScore);
                        beta = Math.min(beta, bestScore);

                        //alpha beta pruning
                        if (alpha >= beta){
                            break;
                        }
                    }
                }
            }
            return bestScore;
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

        for (int i = 0; i < row; i++){
            for (int j = 0; j< col; j++){
                if (board[i][j] == ' '){

                    //copy board
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
                    
                    //do minimax search
                    int score = minimax_search(tempBoard, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, playerXScore, playerOScore, false);

                    //get best move with highest score
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove[0] = i;
                        bestMove[1] = j;
                    }
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
    