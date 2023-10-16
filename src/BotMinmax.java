import javafx.scene.control.Button;
import java.util.ArrayList;
import java.util.List;

public class BotMinmax extends Bot {
    public int minimax_search(Node node, int depth, int alpha, int beta, int playerXScore, int playerOScore){
        
        if (depth == 0){
            return objective_function(playerXScore, playerOScore);
        }

        if (node.botTurn){
            int best = Integer.MIN_VALUE;
            for (Node child : node.children){
                int score = minimax_search(child, depth - 1, alpha, beta, playerXScore, playerOScore);
                best = Math.max(best, score);
                alpha = Math.max(alpha, best);
                if (beta <= alpha){
                    break;
                }
            }
            return best;
        }

        else{
            int best = Integer.MAX_VALUE;
            for (Node child : node.children){
                int score = minimax_search(child, depth - 1, alpha, beta, playerXScore, playerOScore);
                best = Math.max(best, score);
                alpha = Math.max(beta, best);
                if (alpha >= beta){
                    break;
                }
            }
            return best;
        }

    }

    public Node getBestMove(Node node, int playerXScore, int playerOScore){
        Node bestChild = null;
        int best;
        if (node.botTurn){
            best = Integer.MIN_VALUE;
        }
        else {
            best = Integer.MAX_VALUE;
        }

        for (Node child : node.children){
            int score = minimax_search(child, 1, Integer.MIN_VALUE, Integer.MAX_VALUE, playerXScore, playerOScore);
            if ((node.botTurn && score > best) || (!node.botTurn && score < best)){
                bestChild = child;
                best = score;
            }
        }
        return bestChild;
    }


    public int[] move(int playerXScore, int playerOScore, int roundsLeft, Button[][] buttons) {
        // Create temporary board array for search
        char[][] board = new char[buttons.length][buttons[0].length];
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                board[i][j] = buttons[i][j].getText().charAt(0);
            }
        }
        Node node = new Node(board, true);
        Node bestMoveNode = getBestMove(node, playerXScore, playerOScore);

        int[] bestMove = new int[2];

        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                if (buttons[i][j].getText().charAt(0) != bestMoveNode.board[i][j]) {
                    bestMove[0] = i;
                    bestMove[1] = j;
                }
            }
        }

        return bestMove;
    }

}
class Node{
    public char[][] board;
    public boolean botTurn;
    public List<Node> children;
    public Integer score;

    public Node(char[][] board, boolean botTurn){
        this.board = board;
        this.botTurn = botTurn;
        this.children = new ArrayList<Node>();
        this.score = null;
    }

    public void generateChildren(){
        for (int i = 0; i< board.length; i++){
            for (int j = 0; j<board[0].length; j++){
                if (board[i][j] == ' '){
                    char[][] newBoard = copyBoard(board);
                    newBoard[i][j] = botTurn ? 'O' : 'X';
                    Node newNode = new Node(newBoard, !botTurn);
                    children.add(newNode);
                }

                if (children.size() >= 10){
                    return;
                }
            }
        }
    }

    private char[][] copyBoard(char[][] originalBoard){
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