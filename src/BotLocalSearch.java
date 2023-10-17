import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import java.util.Random;
import javafx.util.Pair;

public class BotLocalSearch extends Bot {
    public int[] local_search(char[][] board) {
        // Local search initial state
        int playerOScore = 0;
        int playerXScore = 0;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(board[i][j] == 'O'){
                    playerOScore++;
                } else {
                    playerXScore++;
                }
            }
        }

        // Inisialisasi nilai 
        int maxObj = objective_function(playerXScore, playerOScore);
        
        // Buat tabel nilai objective value suksesor
        int tableValues[][] = generate_successors(board, maxObj);

        // Evaluasi nilai terbesar dan pilih dia
        Pair<Integer, Integer> bestSucc = best_successor(tableValues, maxObj);

        int[] result = {(int) bestSucc.getKey(), (int)bestSucc.getValue()};

        return result;


    }

    public int[][] generate_successors(char[][]board, int currentObjValue){

        // Making temporary board
       
        int values[][] = new int[8][8];
        for(int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                // Misalkan ini dia mau gerak ke sini
                if (board[i][j] == ' '){

                    // Temporary board dulu dibuat
                     char[][] tempBoard = copyBoard(board);
                     Pair<Integer, Integer> coor = new Pair<>(i, j);
                     checkAdjacency(tempBoard, coor, "Bot");
                     values[i][j] = objective_value(tempBoard);
                    //

                } else {
                    values[i][j] = -999;
                }
            }
        }

        return values;
    }
    public int max(char[][] board, int currentValue){
        return 0;
    }

    public int objective_value(char[][] board){
        int playerOScore = 0;
        int playerXScore = 0;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(board[i][j] == 'O'){
                    playerOScore++;
                } else {
                    playerXScore++;
                }
            }
        }

        return objective_function(playerXScore, playerOScore);
    }

    public int[] move(int playerXScore, int playerOScore, int roundsLeft, Button[][] buttons) {
        // Create temporary board array for search
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

        return local_search(board);
    }

    public Pair<Integer, Integer> best_successor(int[][] objValues, int initialValue){
        int max = initialValue;
        int rol = 0;
        int col = 0;
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                if (objValues[i][j] >= max){
                    max = objValues[i][j];
                    rol = i;
                    col = j;
                }
            }
        }
        return Pair<Integer, Integer> best = new Pair<>(rol, col);
    }


}
