import java.util.ArrayList;
import java.util.List;

public class Node {
    protected char[][] board;
    protected List<Node> children;
    protected Integer score;

    public Node(char[][] board){
        this.board = board;
        this.children = new ArrayList<Node>();
        this.score = null;
    }

    public void generateChildren(){
        for (int i = 0; i < this.board.length; i++){
            for (int j = 0; j < this.board[0].length; j++){
                if (this.board[i][j] == ' '){
                    char[][] newBoard = copyBoard(board);
                    newBoard[i][j] = 'O';
                    Node newNode = new Node(newBoard);
                    children.add(newNode);
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

    public void setScore() {
        int playerScore = 0;
        int botScore = 0;

        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[0].length; j++) {
                if (this.board[i][j] == 'X') {
                    playerScore++;
                }
                else if (this.board[i][j] == 'O') {
                    botScore++;
                }
            }
        }

        this.score = botScore - playerScore;
    }
}
