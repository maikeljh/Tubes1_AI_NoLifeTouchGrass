import javafx.scene.control.Button;
import javafx.util.Pair;

public abstract class Bot {
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
                if (board[row-1][col] == 'O' || board[row-1][col] == ' '){
                    board[row-1][col] = 'X';
                }
            }

            // Above
            if (borderCheck(row+1, col)){
                if (board[row+1][col] == 'O' || board[row+1][col] == ' '){
                    board[row+1][col] = 'X';
                }
            }
            
            // Left
            if (borderCheck(row, col-1)){
                if (board[row][col-1] == 'O' || board[row][col-1] == ' '){
                    board[row][col-1] = 'X';
                }
            }

            // Right
            if (borderCheck(row, col+1)){
                if (board[row][col+1] == 'O' || board[row][col+1] == ' '){
                    board[row][col+1] = 'X';
                }
            }
        } else {
            // For Bot
            // Under
            if (borderCheck(row-1, col)){
                if (board[row-1][col] == 'X' || board[row-1][col] == ' '){
                    board[row-1][col] = 'O';
                }
            }

            // Above
            if (borderCheck(row+1, col)){
                if (board[row+1][col] == 'X' || board[row+1][col] == ' '){
                    board[row+1][col] = 'O';
                }
            }
            
            // Left
            if (borderCheck(row, col-1)){
                if (board[row][col-1] == 'X' || board[row][col-1] == ' '){
                    board[row][col-1] = 'O';
                }
            }

            // Right
            if (borderCheck(row, col+1)){
                if (board[row][col+1] == 'X' || board[row][col+1] == ' '){
                    board[row][col+1] = 'O';
                }
            }
        }
    }
}