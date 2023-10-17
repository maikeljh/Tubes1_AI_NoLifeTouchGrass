import javafx.scene.control.Button;
import javafx.util.Pair;

public abstract class Bot {
    public char ownedSymbol;
    public char enemySymbol;

    public int objective_function(int playerXScore, int playerOScore){
        return playerOScore - playerXScore;
    }

    public abstract int[] move(int playerXScore, int playerOScore, int roundsLeft, Button[][] buttons);

    public boolean borderCheck(int row, int col){
        if (row >= 0 && row < 8 && col >= 0 && col < 8){
            return true;
        } else {
            return false;
        }
    }

    public void checkAdjacency(char[][] board, Pair coor, String currentMove){
        int row = (int) coor.getKey();
        int col = (int) coor.getValue();

        if (currentMove.equals("Player")){
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
}
