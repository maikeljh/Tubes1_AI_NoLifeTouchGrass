import java.util.ArrayList;
import java.util.List;

public class NodeMinmax extends Node {
    public boolean botTurn;
    public List<NodeMinmax> children;

    public NodeMinmax(char[][] board, boolean botTurn) {
        super(board);
        this.botTurn = botTurn;
        this.children = new ArrayList<>();
    }

    @Override
    public void generateChildren(){
        for (int i = 0; i< this.board.length; i++){
            for (int j = 0; j< this.board[0].length; j++){
                if (this.board[i][j] == ' '){
                    char[][] newBoard = copyBoard(this.board);
                    newBoard[i][j] = this.botTurn ? 'O' : 'X';

                    NodeMinmax newNode = new NodeMinmax(newBoard, !this.botTurn);
                    this.children.add(newNode);
                    
                }

                if (this.children.size() >= 10){
                    break;

                }
            }
        }

        // for (NodeMinmax node : this.children){
        //     System.out.println(node.score);
        // }
    }
}