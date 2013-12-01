package connect4;

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
    private int[] firstRow;
    private int boardCount, count, col, maxDepth;
    private final int DEPTH = 4; //Set higher to allow for deeper scans and harder AI
	private boolean yellowWin, redWin;


	public ComputerPlayer20057028(LocationState playerState) {
		super(playerState);
        this.boardCount = 0;
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



        return 0;
	}

    private void createGame() {
        pmax = new ComputerPlayer20057028(this.getPlayerState());
        LocationState ls = (pmax.getPlayerState() == LocationState.RED) ? LocationState.YELLOW : LocationState.RED;
        pmin = new ComputerPlayer20057028(ls);
        this.c4 = new Connect4(pmax, pmin, gameBoard);

    }

    public int getScore() {
        int score = 0,
        row = getRow();

        return minimax(this.pmax);
    }

    private int getRow() {
        for (int i = gameBoard.getNoRows() - 1; i >=0; i++) {
            if (gameBoard.getLocationState(new Location(col, i))== LocationState.EMPTY)
                return i;
        }
        return -1;
    }

    private int minimax(IPlayer player) {
        redWin = yellowWin = false;

        if (player.getPlayerState() == LocationState.RED) {
            scoreRed(0, 1, -1, Integer.MIN_VALUE + 1, Integer.MAX_VALUE - 1);
            if (redWin) return col;
            redWin = yellowWin = false;
            scoreYellow(0, 1, -1, Integer.MIN_VALUE + 1, Integer.MAX_VALUE - 1);
            if (yellowWin) return col;
            scoreRed(0, maxDepth, -1, Integer.MIN_VALUE + 1, Integer.MAX_VALUE - 1);
        } else {
            scoreYellow(0, 1, -1, Integer.MIN_VALUE + 1, Integer.MAX_VALUE - 1);
            if (yellowWin) return col;
            redWin = yellowWin = false;
            scoreRed(0, 1, -1, Integer.MIN_VALUE + 1, Integer.MAX_VALUE - 1);
            if (redWin) return col;
            scoreYellow(0, maxDepth, -1, Integer.MIN_VALUE + 1, Integer.MAX_VALUE - 1);
        }

        return col;
    }

    private int scoreYellow(int i, int i1, int i2, int i3, int i4) {

    }

    private int scoreRed(int depth, int maxDepth, int col, int alpha, int beta) {
        boardCount++;
        int max = Integer.MIN_VALUE, score = 0;
        if (col != -1) {
            score = calcScore();
            if (c4.isWin(gameBoard)) {
                redWin = true;
                return score;
            }
        }

        if (depth == maxDepth) return score;

        for (int i = 0; i < gameBoard.getNoCols(); i++) {
            if (isColAvail(i)) {
                gameBoard.setLocationState(new Location(col, i), pmax.getPlayerState());
                int value = scoreYellow(depth + 1, maxDepth, i, alpha, beta);
                gameBoard.setLocationState(new Location(col, i), LocationState.EMPTY);
                if (value > max) {
                    max = value;
                    if (depth == 0) col = i;
                }
                if (value > alpha) alpha = value;
                if (alpha >= beta) return alpha;
            }
        }
        if (max == Integer.MIN_VALUE) return 0;

        return max;
    }

    public boolean isColAvail(int c) {
        for (int i = gameBoard.getNoRows(); i >= 0; i--) {
            Location l = new Location(c, i);
            if (gameBoard.getLocationState(l) == LocationState.EMPTY) return true;
        }

        return false;
    }

}
