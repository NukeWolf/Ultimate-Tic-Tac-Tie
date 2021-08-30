import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    private int[] board;
    private int winner;
    public Board() {
        board = new int[9];
        winner = 0;
    }
    public Board(Board original) {
        this.board = Arrays.copyOf(original.board,original.board.length);
        this.winner = original.winner;
    }
    public void makeMove(int pos, int move){
        board[pos] = move;
        checkWin();
    }
    public ArrayList<Integer> getValidMoves(){
        ArrayList<Integer> moves = new ArrayList<Integer>();
        for (int i = 0;i < 9;i++){
            if(board[i] == 0)
                moves.add(i);
        }
        return moves;
    }
    public boolean checkWin(){
        if (winner != 0) return true;
        // Check each row for win.
        for(int row = 0;row<3;row++){
            int count = 0;
            for(int col = 0;col<3;col++){
                count+=board[row*3 + col];
            }
            if(checkCount(count)){
                return true;
            }
        }
        //Check each Col for win.
        for(int col = 0; col<3;col++){
            int count = 0;
            for(int row = 0;row<3;row++){
                count+=board[row*3 + col];
            }
            if(checkCount(count)){
                return true;
            }
        }
        //Check a diagonal
        int count = 0;
        for(int i = 0;i<3;i++){
            count += board[i*3 + i];
        }
        if(checkCount(count)){
            return true;
        }
        //Check other diagonal
        count = 0;
        for(int i = 0;i<3;i++){
            count += board[i*3 + 2-i];
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

    public int getWinner(){
        return winner;
    }
    public int[] getRow(int row){
        return Arrays.copyOfRange(board, row*3, row*3+3);
    }
    public boolean isFull(){
        for (int i = 0;i < 9;i++){
            if(board[i] == 0)
                return false;
        }
        return true;
    }

    //Same logic as the ultimate board but numbers are scaled down as these scores mean less.
    public int getAdvantage(){
        int adv = 0;
        int numOfTwoCounts = 0;
        // Check for who is about to win a board.
        // Check each row for win.
        for(int row = 0;row<3;row++){
            int count = 0;
            for(int col = 0;col<3;col++){
                count+=board[row*3 + col];
            }
            if(Math.abs(count) == 2){
                numOfTwoCounts += count;
            }
        }
        //Check each Col for win.
        for(int col = 0; col<3;col++){
            int count = 0;
            for(int row = 0;row<3;row++){
                count+=board[row*3 + col];
            }
            if(Math.abs(count) == 2){
                numOfTwoCounts += count;
            }
        }
        //Check a diagonal
        int count = 0;
        for(int i = 0;i<3;i++){
            count += board[i*3 + i];
        }
        if(Math.abs(count) == 2){
            numOfTwoCounts += count;
        }
        //Check other diagonal
        count = 0;
        for(int i = 0;i<3;i++){
            count += board[i*3 + 2-i];
        }
        if(Math.abs(count) == 2){
            numOfTwoCounts += count;
        }
        if (numOfTwoCounts > 0){
            adv += 90;
        }
        else if(numOfTwoCounts < 0){
            adv -= 90;
        }
        for(int val : board){
            adv += val;
        }
        return adv;
    }
}
