import javafx.scene.control.Alert;
import javafx.scene.control.Button;

public class Bot {
    private String algorithm;

    public Bot(String algorithm){
        this.algorithm = algorithm;
    }

    public int objective_function(int playerXScore, int playerOScore){
        return playerOScore - playerXScore;
    }

    public int[] minimax_search(){
        return new int[]{(int) (Math.random()*8), (int) (Math.random()*8)};
    }

    public int[] beam_search(){
        return new int[]{(int) (Math.random()*8), (int) (Math.random()*8)};
    }

    public int[] genetic_search(){
        return new int[]{(int) (Math.random()*8), (int) (Math.random()*8)};
    }

    public int[] move(int playerXScore, int playerOScore, int roundsLeft, Button[][] buttons) {
        // Create temporary board array for search
        String[][] board = new String[buttons.length][buttons[0].length];
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                board[i][j] = buttons[i][j].getText();
            }
        }

        // Search based on picked algorithm
        if (this.algorithm.equals("Minimax")){
            return minimax_search();
        } else if (this.algorithm.equals("Beam Search")){
            return beam_search();
        } else if (this.algorithm.equals("Genetic Algorithm")){
            return genetic_search();
        } else {
            new Alert(Alert.AlertType.ERROR, "Invalid Algorithm. Exiting.").showAndWait();
            System.exit(1);
            return null;
        }
    }
}
