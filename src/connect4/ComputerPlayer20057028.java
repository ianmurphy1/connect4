package connect4;

import java.util.ArrayList;

/**
 * 
 * Example Computer Player.
 * CREATE YOUR OWN VERSION OF THIS, REPLACING THE NUMBER IN THE CLASS NAME 
 * WITH YOUR STUDENT NUMBER.
 * @author Frank
 *
 */
public class ComputerPlayer20057028 extends IPlayer {

    private IPlayer pmax, pmin;
    private Connect4 c4;
    private Board gameBoard;
    int[] sign = new int[] {1, -1};
    private int[] moves;
    private int bestColumn;
    private final int DEPTH = 4; //Set higher to allow for deeper scans and harder AI
	private boolean yellowWin, redWin, yellowWinFound, redWinFound;


	public ComputerPlayer20057028(LocationState playerState) {
		super(playerState);
	}

    private Board copyBoard(Board board) {
        Board b = new Board(board.getNoCols(), board.getNoRows());
        for (int i = 0; i < b.getNoCols(); i++)
            for (int j = 0; j < b.getNoRows(); j++) {
                Location loc = new Location(i, j);
                b.setLocationState(loc, board.getLocationState(loc));
            }
        return b;
    }

    @Override
	public int getMove(Board board) {
        this.gameBoard = copyBoard(board);
        createGame();
        return negamax(gameBoard, DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, pmax);
	}

    private int negamax(Board b, int depth, int alpha, int beta, IPlayer player) {
        if (c4.isWin(b) || c4.isDraw() || depth == 0) {
            int i;
            i = (player.getPlayerState() == pmax.getPlayerState()) ? 0 : 1;
            return sign[i] * eval(b);
        }

        IPlayer opp;
        LocationState ls = (player.getPlayerState() == LocationState.RED) ? LocationState.YELLOW : LocationState.RED;
        opp = new ComputerPlayer20057028(ls);

        int max = Integer.MIN_VALUE;
        moves = getLegalMoves(b);

        for (int i = 0; i < moves.length; i++) {
            Board c = copyBoard(b);
            c.setLocationState(new Location(moves[i], getRow(i, c)), player.getPlayerState());
            int x = - negamax(c, depth - 1, -alpha, -beta, opp);
            if (x > max) max = x;
            if (x > alpha) alpha = x;
            if (alpha >= beta) return alpha;
        }
        return max;
    }

    private int getRow(int col, Board b) {
        int row;

        for (int i = b.getNoRows() - 1; i >=0; i--)
            if (b.getLocationState(new Location(col, i)) == LocationState.EMPTY) return i;

        return 0;
    }

    private int[] getLegalMoves(Board b) {
        ArrayList<Integer> m = new ArrayList<Integer>();
        for (int i = 0; i < b.getNoCols(); i++) {
            if (b.getLocationState(new Location(i, 0)) == LocationState.EMPTY) m.add(i);
        }
        return convertArray(m);
    }

    private int[] convertArray(ArrayList<Integer> m) {
        int[] arr = new int[m.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = m.get(i).intValue();
        }
        return arr;  //To change body of created methods use File | Settings | File Templates.
    }

    private int eval(Board b) {
        return 0;  //To change body of created methods use File | Settings | File Templates.
    }

    private void createGame() {
        pmax = new ComputerPlayer20057028(this.getPlayerState());
        LocationState ls = (pmax.getPlayerState() == LocationState.RED) ? LocationState.YELLOW : LocationState.RED;
        pmin = new ComputerPlayer20057028(ls);
        this.c4 = new Connect4(pmax, pmin, gameBoard);
    }
}
