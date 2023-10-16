import javafx.scene.control.Button;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;

public class BotMinmax extends Bot {
    public int minimax_search(NodeMinmax node, int depth, int alpha, int beta, int playerXScore, int playerOScore){
        
        if (depth == 10){
            return objective_function(playerXScore, playerOScore);
        }

        if (node.botTurn){
            int best = Integer.MIN_VALUE;
            for (NodeMinmax child : node.children){
                int score = minimax_search(child, depth + 1, alpha, beta, playerXScore, playerOScore);
                best = Math.max(best, score);
                alpha = Math.max(alpha, best);
                if (alpha >= beta){
                    break;
                }
            }
            return best;
        }

        else{
            int best = Integer.MAX_VALUE;
            for (NodeMinmax child : node.children){
                int score = minimax_search(child, depth + 1, alpha, beta, playerXScore, playerOScore);
                best = Math.min(best, score);
                alpha = Math.min(beta, best);
                if (alpha >= beta){
                    break;
                }
            }
            return best;
        }

    }

    public NodeMinmax getBestMove(NodeMinmax node, int playerXScore, int playerOScore){
        NodeMinmax bestChild = null;
        int best;

        node.generateChildren();

        if (node.botTurn){
            best = Integer.MIN_VALUE;
        }
        else {
            best = Integer.MAX_VALUE;
        }

        for (NodeMinmax child : node.children){
            int score = minimax_search(child, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, playerXScore, playerOScore);
            if ((node.botTurn && score > best) || (!node.botTurn && score < best)){
                bestChild = child;
                best = score;
            }
        }
        return bestChild;
    }


    public int[] move(int playerXScore, int playerOScore, int roundsLeft, Button[][] buttons) {
        char[][] board = new char[buttons.length][buttons[0].length];
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[0].length; j++) {
                String buttonText = buttons[i][j].getText();
                if (buttonText.isEmpty()) {
                    board[i][j] = ' ';
                } else {
                    board[i][j] = buttonText.charAt(0);
                }
            }
        }
        NodeMinmax node = new NodeMinmax(board, true);
        NodeMinmax bestMoveNode = getBestMove(node, playerXScore, playerOScore);

        int[] bestMove = new int[2];
        char[][] tempBoard = new char[board.length][board[0].length];

        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[0].length; j++) {
                String buttonText = buttons[i][j].getText();
                // if(!buttonText.isEmpty()){
                //     System.out.print(buttonText.charAt(0));
                //     System.out.println("--------");
                // }
                // System.out.print(bestMoveNode.board[i][j]);
                // System.out.print(i);
                // System.out.print(j);
                if (!buttonText.isEmpty() && buttonText.charAt(0) != bestMoveNode.board[i][j]) {
                    bestMove[0] = i;
                    bestMove[1] = j;

                    Pair currentCoor = new Pair(i, j);
                    if(bestMoveNode.botTurn) {
                        tempBoard[(int) currentCoor.getKey()][(int) currentCoor.getValue()] = 'O';
                        checkAdjacency(tempBoard, currentCoor, "Bot");
                    } else {
                        tempBoard[(int) currentCoor.getKey()][(int) currentCoor.getValue()] = 'X';
                        checkAdjacency(tempBoard, currentCoor, "Player");
                    }
    
                    buttons[i][j].setDisable(true);
                }
            }
        }
        System.out.println(bestMove[0]);
        System.out.println(bestMove[1]);
        return bestMove;
    }

}