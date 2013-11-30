package connect4;


import edu.princeton.cs.introcs.StdIn;
import edu.princeton.cs.introcs.StdOut;

import java.util.InputMismatchException;

public class HumanPlayer extends IPlayer {

    public HumanPlayer(LocationState playerState) {
        super(playerState);
    }

    @Override
    public int getMove(Board board) {
        int col = getInt("Enter column number: ");

        int x = checkCol(col, board);
        if (x >= 0) return x;

        return -1;
    }

    private int checkCol(int col, Board board) {
        for (int i = board.getNoRows() - 1; i >= 0; i++) {
            if (board.getLocationState(new Location(col, i)) == LocationState.EMPTY) return col;
        }
        return -1;
    }

    private int getInt(String prompt) throws InputMismatchException {
        StdOut.print(prompt);
        try {
            int output = StdIn.readInt();
            return output;
        } catch (InputMismatchException e) {
            StdIn.readLine();
            StdOut.println("Not valid");
            StdOut.println("Go again.");
        }
        return -1;
    }

}