import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import java.util.Random;

public class BotBeamSearch extends Bot {
    private int beamWidth;

    public BotBeamSearch(){
        this.beamWidth = 5;
    }

    public int[] beam_search(char[][] board) {
        // Parameter char[][] board adalah initial statenya atau current state dari permainan
        // Node bestSuccessor[] = new Node(initialBord);
        // Pertama, pembacaan nilai initial value ato current depth.
        // Kedua, generate semua successor dan evaluate nilainya
        // Ketiga, pilih score top 5 (Mungkin sort (?))
        // Top 5 di beam search lagi sampe dapet nilai ujung sampe finishnya TERBESAR
        // Nilai terbesar adalah picked path yang dipake buat jalan.
        // Player jalan, new initial value, rinse and repeat. 
    }

    public int objective_value(){
        return 0;
    }
    public int[] move(int playerXScore, int playerOScore, int roundsLeft, Button[][] buttons) {
        // Create temporary board array for search
        char[][] board = new char[buttons.length][buttons[0].length];
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                board[i][j] = buttons[i][j].getText();
            }
        }

        return beam_search(board);
    }


}
