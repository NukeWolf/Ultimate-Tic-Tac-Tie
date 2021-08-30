import java.util.ArrayList;
import java.util.Scanner;


public class Main {
    public static int maxDepth = 6;
    //How many recursion instances there can be.
    public static int maxComputation = 100000;


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        UltimateBoard ub = new UltimateBoard();
        println("Welcome!");

        while(true){
            ub.printBoard();
            ArrayList<Integer>[] allMoves = ub.getValidMoves();
            println("Possible Moves:");
            printPossibleMoves(allMoves);
            println("");
            println("Make a move by entering: <Ultimate Board #> <Board #>");
            println("Example: 2 8 | This selects the top right board, and the bottom right corner of that board.");
            try{
                String s = sc.nextLine();
                String[] params = s.split(" ");
                int board = Integer.parseInt(params[0]);
                int pos = Integer.parseInt(params[1]);
                if(allMoves[board] != null && allMoves[board].contains(pos)){
                    ub.makeMove(board,pos);

                    //Gets the best move according to an AI
                    s = determineProb(ub);
                    params = s.split(" ");
                    ub.makeMove(Integer.parseInt(params[0]),Integer.parseInt(params[1]));
                }
                else{
                    println("Invalid Move!");
                }
            }
            catch (Exception e){
                println("Invalid Input!");
            }
        }
    }
    public static void println(String s){
        System.out.println(s);
    }
    public static void printPossibleMoves(ArrayList<Integer>[] moves){
        for(int i = 0; i<9;i++){
            if(moves[i] != null){
                System.out.print("Board " + i+": ");
                for(int val:moves[i]){
                    System.out.print(val + ",");
                }
                System.out.println();
            }
        }
    }
    public static String determineProb(UltimateBoard ub){
        ArrayList<Integer>[] allMoves = ub.getValidMoves();
        int best = -99999;
        String bestBoard = "";
        String bestPos = "";
        for(int board = 0;board<9;board++){
            ArrayList<Integer> moves = allMoves[board];
            if(moves == null)
                continue;
            //Iterate through all moves and get the best move to make.
            System.out.println("What AI is thinking:");
            for (int pos : moves){
                //Creates a new board and emulates the possible move.
                UltimateBoard newUb = new UltimateBoard(ub);
                newUb.makeMove(board,pos);
                //Multiply it by -1, because it will get the enemies advantage score.
                // The enemy may have an advantage in this board state which is a positive integer, which is a disadvantage to this player.
                int adv = determineProb(newUb,1) * -1;
                System.out.println("Board:"+board+" Pos:" + pos + " Adv:" + adv);
                if (adv > best){
                    best = adv;
                    bestBoard = "" + board;
                    bestPos = "" + pos;
                }
            }
        }
        return bestBoard+" " + bestPos;
    }
    public static int determineProb(UltimateBoard ub, int computation){
        // If the Board has been won, the previous move's player has won. This is one possible end condition
        if(ub.getWinner() != 0){
         return -10000;
        }
        //Once we get to a max recursion limit, we stop doing recursion and find an advantage score of the board based off an algorithm to determine optimal moves.
       if(computation >= maxComputation){
        return ub.getAdvantage();
       }
       ArrayList<Integer>[] allMoves = ub.getValidMoves();
       int best = -99999;
        for(int board = 0;board<9;board++){
            ArrayList<Integer> moves = allMoves[board];
            if(moves == null)
                continue;
            for (int pos : moves){
                UltimateBoard newUb = new UltimateBoard(ub);
                newUb.makeMove(board,pos);
                int adv = determineProb(newUb,computation*moves.size()) * -1;
                if (adv > best){
                    best = adv;
                }
            }
        }
        return best;
    }
    //Uses a maximum depth limit instead of amount of recursion instances
    /*public static int determineProb(UltimateBoard ub, int depth){
        if(ub.getWinner() != 0){
            return -10000;
        }
        if(depth == maxDepth){
            return ub.getAdvantage();
        }
        ArrayList<Integer>[] allMoves = ub.getValidMoves();
        int best = -99999;
        for(int board = 0;board<9;board++){
            ArrayList<Integer> moves = allMoves[board];
            if(moves == null)
                continue;
            for (int pos : moves){
                UltimateBoard newUb = new UltimateBoard(ub);
                newUb.makeMove(board,pos);
                int adv = determineProb(newUb,depth+1) * -1;
                if (adv > best){
                    best = adv;
                }
            }
        }
        return best;
    }*/

}
