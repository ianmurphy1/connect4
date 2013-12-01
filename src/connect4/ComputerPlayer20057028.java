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
    public static final int[] INCREMENT = {0, 1, 4, 32, 128, 512};
    private int boardCount, count, col, maxDepth;
    private final int DEPTH = 4; //Set higher to allow for deeper scans and harder AI
	private boolean yellowWin, redWin, yellowWinFound, redWinFound;


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



        return minimax(this.pmax);
	}

    private void createGame() {
        pmax = new ComputerPlayer20057028(this.getPlayerState());
        LocationState ls = (pmax.getPlayerState() == LocationState.RED) ? LocationState.YELLOW : LocationState.RED;
        pmin = new ComputerPlayer20057028(ls);
        this.c4 = new Connect4(pmax, pmin, gameBoard);
    }


    private int getRow(int c) {
        for (int i = gameBoard.getNoRows() - 1; i >=0; i--) {
            if (gameBoard.getLocationState(new Location(c, i))== LocationState.EMPTY)
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

    private int scoreRed(int depth, int maxDepth, int col, int alpha, int beta) {
        boardCount++;
        int max = Integer.MIN_VALUE, score = 0;
        if (col != -1) {
            score = calcScore(pmin, col, depth, maxDepth);
            if (yellowWinFound) {
                yellowWin = true;
                return score;
            }
        }

        if (depth == maxDepth) return score;

        for (int i = 0; i < gameBoard.getNoCols(); i++) {
            if (isColAvail(i)) {
                int r = getRow(i);
                gameBoard.setLocationState(new Location(i, r), pmax.getPlayerState());
                int value = scoreYellow(depth + 1, maxDepth, i, alpha, beta);
                gameBoard.setLocationState(new Location(i, r), LocationState.EMPTY);
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

    private int scoreYellow(int depth, int maxDepth, int col, int alpha, int beta) {
        boardCount++;
        int min = Integer.MAX_VALUE, score = 0;
        if (col != -1) {
            score = calcScore(pmin, col, depth, maxDepth);
            if (redWinFound) {
                redWin = true;
                return score;
            }
        }

        if (depth == maxDepth) return score;

        for (int i = 0; i < gameBoard.getNoCols(); i++) {
            if (isColAvail(i)) {
                int r = getRow(i);
                gameBoard.setLocationState(new Location(i, r), pmin.getPlayerState());
                int value = scoreRed(depth + 1, maxDepth, i, alpha, beta);
                gameBoard.setLocationState(new Location(i, r), LocationState.EMPTY);
                if (value < min) {
                    min = value;
                    if (depth == 0) col = i;
                }
                if (value < beta) beta = value;
                if (alpha >= beta) return beta;
            }
        }
        if (min == Integer.MAX_VALUE) return 0;

        return min;
    }


    private boolean isColAvail(int c) {
        for (int i = gameBoard.getNoRows() - 1; i >= 0; i--) {
            Location l = new Location(c, i);
            if (gameBoard.getLocationState(l) == LocationState.EMPTY) return true;
        }

        return false;
    }

    private int calcScore(IPlayer player, int col, int depth, int maxDepth) {
        int score = 0, row = getRow(col), redCount, yellowCount;
        redWinFound = yellowWinFound= false;

        //Check rows
        redCount = yellowCount = 0;
        LocationState[] boardRow = getBoardRow(gameBoard, row);

        int cStart = col - 3, colStart = cStart >= 0 ? cStart : 0,
                colEnd = gameBoard.getNoCols() - 3 - (colStart - cStart);

        for (int i = colStart; i < colEnd; i++) {
            redCount = yellowCount = 0;
            for (int val = 0; val < 4; val++) {
                LocationState state = boardRow[i + val];
                if (state == LocationState.YELLOW) yellowCount++;
                else if (state == LocationState.RED) redCount++;
            }
            if (redCount == 4) {
                redWinFound = true;
                if (depth <= 2) return Integer.MIN_VALUE + 1;
            } else if (yellowCount == 4) {
                yellowWinFound = true;
                if (depth <= 2) return Integer.MAX_VALUE - 1;
            }
        }
        score += getIncrement(redCount, yellowCount, player);

        //Columns

        redCount = yellowCount = 0;
        int rowEnd = Math.min(gameBoard.getNoRows(), row + 4);
        for (int r = row; r < rowEnd; r++) {
            LocationState state = gameBoard.getLocationState(new Location(col, r));
            if (state == LocationState.RED) redCount++;
            else if (state == LocationState.YELLOW) yellowCount++;
        }

        if (redCount == 4) {
            redWinFound = true;
            if (depth <= 2) return Integer.MIN_VALUE + 1;
        } else if (yellowCount == 4) {
            yellowWinFound = true;
            if (depth <= 2) return Integer.MAX_VALUE - 1;
        }
        score += getIncrement(redCount, yellowCount, player);

        //Diag

        int minValue = Math.min(col, row), rowStart = row - minValue;
        colStart = col - minValue;

        for (int r = rowStart, c = colStart; r <= gameBoard.getNoRows() - 4 && c <= gameBoard.getNoCols() - 4; r++, c++) {
            redCount = yellowCount = 0;
            for (int val = 0; val < 4; val++) {
                LocationState state = gameBoard.getLocationState(new Location(c + val, r + val));
                if (state == LocationState.RED) redCount++;
                else if (state == LocationState.YELLOW) yellowCount++;
            }
            if (redCount == 4) {
                redWinFound = true;
                if (depth <= 2) return Integer.MIN_VALUE + 1;
            } else if (yellowCount == 4) {
                yellowWinFound = true;
                if (depth <= 2) return Integer.MAX_VALUE - 1;
            }
            score += getIncrement(redCount, yellowCount, player);
        }

        //Diag
        minValue = Math.min(gameBoard.getNoRows() - 1 - row, col);
        rowStart = row + minValue;
        colStart = col - minValue;

        for (int c = colStart, r = rowStart; c <= gameBoard.getNoCols() - 4 && r >= 3; c++, r--) {
            redCount = yellowCount = 0;
            for (int val = 0; val < 4; val++) {
                LocationState state = gameBoard.getLocationState(new Location(c + val, r - val));
                if (state == LocationState.RED) redCount++;
                else if (state == LocationState.YELLOW) yellowCount++;
            }
            if (redCount == 4) {
                redWinFound = true;
                if (depth <= 2) return Integer.MIN_VALUE + 1;
            } else if (yellowCount == 4) {
                yellowWinFound = true;
                if (depth <= 2) return Integer.MAX_VALUE - 1;
            }
            score += getIncrement(redCount, yellowCount, player);
        }
        return score;
    }

    private int getIncrement(int redCount, int yellowCount, IPlayer player) {
        LocationState state = player.getPlayerState();

        if (redCount == yellowCount) {
            if (state == LocationState.RED) {
                return -1;
            }
            return 1;
        } else if (redCount < yellowCount) {
            if (state == LocationState.RED) {
                return INCREMENT[yellowCount] - INCREMENT[redCount];
            }
            return INCREMENT[yellowCount + 1] + INCREMENT[redCount];
        } else {
            if (state == LocationState.RED) return -INCREMENT[redCount] - INCREMENT[yellowCount];
            return -INCREMENT[redCount] + INCREMENT[yellowCount];
        }
    }

    private LocationState[] getBoardRow(Board gameBoard, int row) {
        LocationState[] r = new LocationState[gameBoard.getNoCols()];
        for (int i = 0; i < gameBoard.getNoCols(); i++) {
            LocationState l = gameBoard.getLocationState(new Location(i, row));
            r[i] = l;
        }
        return r;
    }

}
