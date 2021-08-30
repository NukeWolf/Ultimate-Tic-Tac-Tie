import java.util.ArrayList;

public class UltimateBoard {
    private Board[] board;
    private int currentBoard;
    private int winner;
    private int currentPlayer;

    public UltimateBoard() {
        board = new Board[9];
        for(int i = 0;i<9;i++){
            board[i] = new Board();
        }
        winner = 0;
        currentBoard = -1;
        currentPlayer = 1;
    }
    public UltimateBoard(UltimateBoard original) {
        board = new Board[9];
        for(int i = 0;i<9;i++){
            board[i] = new Board(original.board[i]);
        }
        currentBoard = original.currentBoard;
        currentPlayer = original.currentPlayer;
    }
    public void makeMove(int boardPos, int pos){
        Board selectedBoard = board[boardPos];
        selectedBoard.makeMove(pos,currentPlayer);
        if(board[pos].isFull()){
            currentBoard = -1;
        }
        else
            currentBoard = pos;
        currentPlayer*=-1;
        checkWin();
    }
    public ArrayList<Integer>[] getValidMoves(){
        ArrayList<Integer>[] possibleMoves = new ArrayList[9];
        if(currentBoard != -1){
            possibleMoves[currentBoard] = board[currentBoard].getValidMoves();
        }
        else{
            for(int i = 0; i<9;i++){
                possibleMoves[i] = board[i].getValidMoves();
            }
        }
        return possibleMoves;
    }
   public boolean checkWin(){
        // Check each row for win.
        for(int row = 0;row<3;row++){
            int count = 0;
            for(int col = 0;col<3;col++){
                count += board[row*3 + col].getWinner();
            }
            if(checkCount(count)){
                return true;
            }
        }
        //Check each Col for win.
        for(int col = 0; col<3;col++){
            int count = 0;
            for(int row = 0;row<3;row++){
                count+=board[row*3 + col].getWinner();
            }
            if(checkCount(count)){
                return true;
            }
        }
        //Check a diagonal
        int count = 0;
        for(int i = 0;i<3;i++){
            count += board[i*3 + i].getWinner();
        }
        if(checkCount(count)){
            return true;
        }
        //Check other diagonal
        count = 0;
        for(int i = 0;i<3;i++){
            count += board[i*3 + 2-i].getWinner();
        }
        if(checkCount(count)){
            return true;
        }
        return false;
    }

    public boolean checkCount(int count){
        if (count==3){
            winner = 1;
            return true;
        }
        else if(count == -3){
            winner = -1;
            return true;
        }
        return false;
    }
    public void printBoard(){
        for(int ubRow = 0;ubRow<3;ubRow++){
            int count2 = 0;
            for(int bRow = 0; bRow<3;bRow++){
                int count1 = 0;
                count2++;
                for(int bCol = 0; bCol<3; bCol++){
                    count1++;
                    int count = 0;
                    for(int val : board[ubRow*3 + bCol].getRow(bRow)){
                        count ++;
                        String s;
                        if(val == -1){
                            s = "O";
                        }
                        else if(val == 1){
                            s = "X";
                        }
                        else if(ubRow*3 + bCol == currentBoard){
                            s = "@";
                        }
                        else{
                            s = " ";
                        }
                        if(count == 3){
                            System.out.print(s);
                        }
                        else
                            System.out.print(s + "|");
                    };
                    if(count1 != 3)
                        System.out.print("  |  ");
                }
                if(count2 != 3){
                    System.out.println();
                    System.out.println("-----  |  -----  |  -----");
                }
            }
            System.out.println();
            for(int i = 0;i<3;i++){
                System.out.print("Board " + (ubRow*3 + i) + "  ");
            }
            System.out.println();
            for(int i = 0;i<3;i++){
                Board selectedBoard = board[ubRow*3 + i];
                int winner = selectedBoard.getWinner();
                if (winner == 0){
                    if(selectedBoard.isFull()){
                        System.out.print("  Tie    ");
                    }
                    else
                        System.out.print("         ");
                }
                else{
                    String s;
                    if(winner == 1)
                        s = "X";
                    else
                        s = "O";
                    System.out.print(s + " won    ");
                }
            }
            System.out.println();
            System.out.println("----------------------------------------------");
            System.out.println();
        }
    }
    public int getWinner(){
        return winner;
    }

    /*
    * Advantage Scale:
    * UltimateBoard Win or Loss : 10000
    * UltimateBoard 2 Letters next to each other : 1000
    * UltimateBoard How many winning pieces in general. 100
    * Board Almost finished : 90
    * Board Relevant extra pieces : 1
    *
    * */
    public int getAdvantage(){
        //Positive advantage means the current player is winning. Negative advantage means the other player is winning.
        int adv = 0;
        // Check how many almost finished rows there are.
        for(int row = 0;row<3;row++){
            int count = 0;
            for(int col = 0;col<3;col++){
                count += board[row*3 + col].getWinner();
            }
            if(Math.abs(count) == 2){
                //Adds 1000 to advantage for every row of 2
                adv += count* 500;
            }
        }
        //Check each Col for win.
        for(int col = 0; col<3;col++){
            //Count represents amount of winning pieces in a row. 1st player is positive, 2nd player is negative.
            //Extra score is given when a player has almost completed a row
            int count = 0;
            for(int row = 0;row<3;row++){
                count+=board[row*3 + col].getWinner();
            }
            if(Math.abs(count) == 2){
                adv += count* 500;
            }
        }
        //Check a diagonal
        int count = 0;
        for(int i = 0;i<3;i++){
            count += board[i*3 + i].getWinner();
        }
        if(Math.abs(count) == 2){
            adv += count* 500;
        }
        //Check other diagonal
        count = 0;
        for(int i = 0;i<3;i++){
            count += board[i*3 + 2-i].getWinner();
        }
        if(Math.abs(count) == 2){
            adv += count* 500;
        }
        //Check for how many pieces each player has.
        for(int i = 0;i<9;i++){
            Board selected = board[0];
            int winner = selected.getWinner();
            // Check if a board has been won or not.
            if(winner == 0){
                adv += selected.getAdvantage();
            }
            else{
                adv += winner*100;
            }
        }
        //Current player is represented by a -1 or 1
        return adv*currentPlayer;

    }
}
