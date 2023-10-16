public class NodeMinmax extends Node {
    private boolean botTurn;

    public NodeMinmax(char[][] board, boolean botTurn) {
        super(board);
    }

    @Override
    public void generateChildren(){
        for (int i = 0; i< this.board.length; i++){
            for (int j = 0; j< this.board[0].length; j++){
                if (this.board[i][j] == ' '){
                    char[][] newBoard = copyBoard(this.board);
                    newBoard[i][j] = this.botTurn ? 'X' : '0';

                    Node newNode = new NodeMinmax(newBoard, !this.botTurn);
                    this.children.add(newNode);
                }

                if (this.children.size() >= 10){
                    break;

                }
            }
        }
    }
}