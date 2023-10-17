import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import java.util.Random;
import javafx.util.Pair;

/**
 * BotHillClimbing class.  It uses steepest ascent hill climbing algorithm to evaluate 
 * best movement for current state
 * @author Kenneth Dave Bahana 13521145
 * 
 */
public class BotHillClimbing extends Bot {
    /**
     * Constructor
     * @param ownedSymbol Bot's symbol
     * @param enemySymbol Enemy's symbol
     *
     */
    public BotHillClimbing(char ownedSymbol, char enemySymbol) {
        this.ownedSymbol = ownedSymbol;
        this.enemySymbol = enemySymbol;
    }

    /**
     * Main function for hill climbing search
     * @param board Game's board containing X and O symbols
     * @return int[]
     *
     */
    public int[] local_search(char[][] board) {
        // Hill Climbing initial state
        int maxObj = objective_value(board);

        // Buat tabel nilai objective value suksesor
        int tableValues[][] = generate_successors(board, maxObj);

        // Evaluasi nilai terbesar dan pilih dia
        Pair<Integer, Integer> bestSucc = best_successor(tableValues, maxObj);

        // Dapetin koordinat dari best successor
        int[] result = {(int) bestSucc.getKey(), (int)bestSucc.getValue()};

        return result;
    }

    /**
     * Function to generate all successor from current state
     * @param board Game's board containing X and O symbols
     * @param currentObjValue Current state's objective value
     * @return int[][]
     *
     */
    public int[][] generate_successors(char[][]board, int currentObjValue){
        // Making temporary board
        int values[][] = new int[8][8];
        for(int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                // Misalkan ini dia mau gerak ke sini
                if (board[i][j] == ' '){
                    // Temporary board dulu dibuat
                    char[][] tempBoard = copyBoard(board);
                    tempBoard[i][j] = ownedSymbol;
                    Pair<Integer, Integer> coor = new Pair<>(i, j);
                    checkAdjacency(tempBoard, coor, "Bot");
                    values[i][j] = objective_value(tempBoard);
                } else {
                    values[i][j] = -999;
                }
            }
        }

        return values;
    }

    /**
     * Function that returns the choosen move based on steepest ascent hill climbing
     * @param roundsLeft Remaining rounds
     * @param buttons Game board buttons containing O and X symbols
     * @return int[]
     *
     */
    public int[] move(int roundsLeft, Button[][] buttons) {
        // Create temporary board array for search
        char[][] board = createBoardFromButtons(buttons);

        return local_search(board);
    }

    /**
     * Function that best successor from current state
     * @param objValues All successors' objective value
     * @param initialValue Current state's objective value
     * @return Pair<Integer, Integer>
     *
     */
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
        Pair<Integer, Integer> best = new Pair<>(rol, col);
        return best;
    }
}
