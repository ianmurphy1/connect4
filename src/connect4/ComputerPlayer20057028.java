package connect4;

import java.util.ArrayList;
import java.util.Iterator;

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
        if (gameBoard.getLocationState(new Location(3, gameBoard.getNoRows() - 1)) == LocationState.EMPTY) return 3;
        negamax(gameBoard, DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, pmax);
        return bestColumn;
	}

    private int negamax(Board b, int depth, int alpha, int beta, IPlayer player) {
        if (c4.isWin(b) || isDraw(b) || depth == 0) {
            int i = (player.getPlayerState() == pmax.getPlayerState()) ? 0 : 1;
            return sign[i] * eval(b, player);
        }

        IPlayer opp;
        LocationState ls = (player.getPlayerState() == LocationState.RED) ? LocationState.YELLOW : LocationState.RED;
        opp = new ComputerPlayer20057028(ls);

        int max = Integer.MIN_VALUE;
        moves = getLegalMoves(b);

        for (int i = 0; i < moves.length; i++) {
            Board c = copyBoard(b);
            c.setLocationState(new Location(moves[i], getRow(moves[i], c)), player.getPlayerState());
            int x = -(negamax(c, depth - 1, -alpha, -beta, opp));
            if (x > max) {
                max = x;
                bestColumn = i;
            }
            c.setLocationState(new Location(moves[i], getRow(moves[i], c)), LocationState.EMPTY);
            if (x > alpha) alpha = x;
            if (alpha >= beta) return alpha;
        }
        return max;
    }

    private boolean isDraw(Board b) {
        for (int i = 0; i < b.getNoCols(); i++) {
            if (b.getLocationState(new Location(i, 0)) != LocationState.EMPTY) return false;
        }
        return true;
    }

    private int getRow(int col, Board b) {
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
        Iterator<Integer> it = m.iterator();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = it.next().intValue();
        }
        return arr;  //To change body of created methods use File | Settings | File Templates.
    }

    private int eval(Board b, IPlayer p) {
        int score = 0;
        //Multipliers based on whether possible rows are vertical, horizontal or diagonal
        int v = 1, d = 2, h = 3;
        //Scores for tokens in a row
        int twoInRow = 10, threeInRow = 1000;
        //Check win horizontally
        for (int i = 0; i < b.getNoCols() - 4; i++) {
            for (int j = 0; j < b.getNoRows(); j++) {
                if (b.getLocationState(new Location(i, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 1, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 2, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 3, j)) == p.getPlayerState()) {
                    if (p.getPlayerState() == pmax.getPlayerState()) return Integer.MAX_VALUE;
                    else return Integer.MIN_VALUE;
                }
            }
        }
        //Check vertically
        for (int i = 0; i < b.getNoCols(); i++) {
            for (int j = b.getNoRows() - 1; j >= 3; j--) {
                if (b.getLocationState(new Location(i, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i, j - 1)) == p.getPlayerState()
                        && b.getLocationState(new Location(i, j - 2)) == p.getPlayerState()
                        && b.getLocationState(new Location(i, j - 3)) == p.getPlayerState()) {
                    if (p.getPlayerState() == pmax.getPlayerState()) return Integer.MAX_VALUE;
                    else return Integer.MIN_VALUE;
                }
            }
        }
        //Checking diagonally forward
        for (int i =  b.getNoCols() - 1; i >= 3; i--) {
            for (int j = b.getNoRows() - 4; j >= 0; j--) {
                if (b.getLocationState(new Location(i, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i - 1, j + 1)) == p.getPlayerState()
                        && b.getLocationState(new Location(i - 2, j + 2)) == p.getPlayerState()
                        && b.getLocationState(new Location(i - 3, j + 3)) == p.getPlayerState()) {
                    if (p.getPlayerState() == pmax.getPlayerState()) return Integer.MAX_VALUE;
                    else return Integer.MIN_VALUE;
                }
            }
        }

        //Horizontal
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 6; j++) {
                //checking (xx00)
                if (b.getLocationState(new Location(i, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 1, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 2, j)) == LocationState.EMPTY
                        && b.getLocationState(new Location(i + 3, j)) == LocationState.EMPTY)
                    score += twoInRow * h;
                //(x0x0)
                else if (b.getLocationState(new Location(i, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 2, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 1, j)) == LocationState.EMPTY
                        && b.getLocationState(new Location(i + 3, j)) == LocationState.EMPTY)
                    score += twoInRow * h;
                //(x00x)
                else if (b.getLocationState(new Location(i, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 3, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 1, j)) == LocationState.EMPTY
                        && b.getLocationState(new Location(i + 2, j)) == LocationState.EMPTY)
                    score += twoInRow * h;
                //(0xx0)
                else if (b.getLocationState(new Location(i, j)) == LocationState.EMPTY
                        && b.getLocationState(new Location(i + 3, j)) == LocationState.EMPTY
                        && b.getLocationState(new Location(i + 1, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 2, j)) == p.getPlayerState())
                    score += twoInRow * h;
                //(0x0x)
                else if (b.getLocationState(new Location(i, j)) == LocationState.EMPTY
                        && b.getLocationState(new Location(i + 2, j)) == LocationState.EMPTY
                        && b.getLocationState(new Location(i + 1, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 3, j)) == p.getPlayerState())
                    score += twoInRow * h;
                //(00xx)
                else if (b.getLocationState(new Location(i, j)) == LocationState.EMPTY
                        && b.getLocationState(new Location(i + 1, j)) == LocationState.EMPTY
                        && b.getLocationState(new Location(i + 2, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 3, j)) == p.getPlayerState())
                    score += twoInRow * h;
            }
        }

        return score;
    }

    private void createGame() {
        pmax = new ComputerPlayer20057028(this.getPlayerState());
        LocationState ls = (pmax.getPlayerState() == LocationState.RED) ? LocationState.YELLOW : LocationState.RED;
        pmin = new ComputerPlayer20057028(ls);
        this.c4 = new Connect4(pmax, pmin, gameBoard);
    }
}
