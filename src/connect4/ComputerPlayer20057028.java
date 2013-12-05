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
    private Board gameBoard;
    private int bestColumn;
    private final int DEPTH = 10; //Set higher to allow for deeper scans and harder AI
    private final int DIFFICULTY = 3000; //Multiplier for scores to increase difficulty

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

        ArrayList<Location> moves = getLegalMoves(gameBoard);
        //ArrayList<connect4.Location> moves = generateMoves();

        for (Location move: moves) {
            System.out.println("Get Move Method Trying move at: " + move.getX());
            gameBoard.setLocationState(move, pmax.getPlayerState());
            negamax(gameBoard, DEPTH, -Integer.MAX_VALUE, Integer.MAX_VALUE, 1, pmax);
            gameBoard.setLocationState(move, LocationState.EMPTY);
        }
        int choice = bestColumn;
        return choice;
	}

    private int negamax(Board b, int depth, int alpha, int beta, int sign, IPlayer player) {

        if (isWin(b, player)) return Integer.MAX_VALUE;

        if (isDraw(b) || depth == 0) {
            return sign * eval(b, player);
        }
        IPlayer opp;
        opp = (pmax.getPlayerState() == player.getPlayerState()) ? pmin : pmax;

        int max = alpha;
        ArrayList<Location> moves = getLegalMoves(b);

        for (Location move : moves) {
           // System.out.println("Trying move at: " + move.getX());
            b.setLocationState(move, player.getPlayerState());
            int x = -negamax(b, depth - 1, -beta, -alpha, sign * -1, opp);
            b.setLocationState(move, LocationState.EMPTY);
           // System.out.println("Undoing move at: " + move.getX());
            if (x > max) {
                max = x;
                bestColumn = move.getX();
            //    System.out.println("Best Move: " + bestColumn);
            }
            if (x > alpha) alpha = x;
            if (alpha >= beta) return alpha;
        }
       // System.out.println(max);
        return max;
    }

    private boolean isWin(Board b, IPlayer p) {
        for (int i = 0; i < b.getNoCols() - 4; i++) {
            for (int j = 0; j < b.getNoRows(); j++) {
                if (b.getLocationState(new Location(i, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 1, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 2, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 3, j)) == p.getPlayerState())
                    return true;
            }
        }

        for (int i = 0; i < b.getNoCols(); i++) {
            for (int j = b.getNoRows() - 1; j >= 3; j--) {
                if (b.getLocationState(new Location(i, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i, j - 1)) == p.getPlayerState()
                        && b.getLocationState(new Location(i, j - 2)) == p.getPlayerState()
                        && b.getLocationState(new Location(i, j - 3)) == p.getPlayerState())
                    return true;
            }
        }

        for (int i = 0; i < b.getNoCols() - 3; i++) {
            for (int j = b.getNoRows() - 4; j >= 0; j--) {
                if (b.getLocationState(new Location(i, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 1, j + 1)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 2, j + 2)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 3, j + 3)) == p.getPlayerState())
                    return true;
            }
        }

        for (int i =  b.getNoCols() - 1; i >= 3; i--) {
            for (int j = b.getNoRows() - 4; j >= 0; j--) {
                if (b.getLocationState(new Location(i, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i - 1, j + 1)) == p.getPlayerState()
                        && b.getLocationState(new Location(i - 2, j + 2)) == p.getPlayerState()
                        && b.getLocationState(new Location(i - 3, j + 3)) == p.getPlayerState())
                    return true;
            }
        }

        return false;
    }

    private boolean isDraw(Board b) {
        for (int i = 0; i < b.getNoCols(); i++) {
            if (b.getLocationState(new Location(i, 0)) == LocationState.EMPTY) return false;
        }
        return true;
    }

    private int getRow(int col, Board b) {
        for (int i = b.getNoRows() - 1; i >= 0; i--)
            if (b.getLocationState(new Location(col, i)) == LocationState.EMPTY) return i;
        return -1;
    }

    private ArrayList<Location> getLegalMoves(Board b) {
        ArrayList<Location> list = new ArrayList<Location>();
        for (int i = 0; i < b.getNoCols(); i++) {
            int row = getRow(i, b);
            if (row >= 0) {
                Location l = new Location(i, row);
                list.add(l);
                //System.out.println("Legal Move " + list.size() + " is at: " + l.getX() + ", " + l.getY());
            }
        }
        return list;
    }

    private ArrayList<Location> generateMoves(Location m, IPlayer p) {
        ArrayList<Location> moves = new ArrayList<Location>();
        gameBoard.setLocationState(m, p.getPlayerState());
        for (int i = 0; i < gameBoard.getNoCols(); i++) {
            int row = getRow(i, gameBoard);
            if (row >= 0) {
                Location l = new Location(i, row);
                moves.add(l);
            }
        }
        gameBoard.setLocationState(m, LocationState.EMPTY);
        return moves;
    }




    public int eval(Board b, IPlayer p) {

        int score = 0;
        //Multipliers based on whether possible rows are vertical, horizontal or diagonal
        int v = 1, d = 2, h = 3;
        //Scores for tokens in a row
        int oneInRow = 1, twoInRow = 10, threeInRow = 1000;

        /******************************************************************************
         *                                                                            *
         *                        1 IN A ROW CHECKS                                   *
         *                                                                            *
         ******************************************************************************/

        //Vertical 1 in a row
        // 0
        // x
        // Only way it can occur
        for (int i = 0; i < b.getNoCols(); i++) {
            for (int j = b.getNoRows() - 1; j >= 1; j--) {
                if (b.getLocationState(new Location(i, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i, j - 1)) == LocationState.EMPTY)
                    score += oneInRow * v;
            }
        }


        /******************************************************************************
         *                                                                            *
         *                        2 IN A ROW CHECKS                                   *
         *                                                                            *
         ******************************************************************************/

        //Horizontal 2 in a row
        for (int i = 0; i < b.getNoCols() - 4; i++) {
            for (int j = 0; j < b.getNoRows(); j++) {
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

        //Vertical 2 in a row
        // 0
        // x
        // x
        // Only way it can occur
        for (int i = 0; i < b.getNoCols(); i++) {
            for (int j = b.getNoRows() - 1; j >= 2; j--) {
                if (b.getLocationState(new Location(i, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i, j - 1)) == p.getPlayerState()
                        && b.getLocationState(new Location(i, j - 2)) == LocationState.EMPTY)
                    score += twoInRow * v;
            }
        }

        //Check for forward diagonal 2 in a rows
        for (int i =  b.getNoCols() - 1; i >= 3; i--) {
            for (int j = b.getNoRows() - 4; j >= 0; j--) {
                //    x
                //   x
                //  0
                // 0
                if (b.getLocationState(new Location(i, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i - 1, j + 1)) == p.getPlayerState()
                        && b.getLocationState(new Location(i - 2, j + 2)) == LocationState.EMPTY
                        && b.getLocationState(new Location(i - 3, j + 3)) == LocationState.EMPTY)
                    score += twoInRow * d;
                //    x
                //   0
                //  0
                // x
                else if (b.getLocationState(new Location(i, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i - 3, j + 3)) == p.getPlayerState()
                        && b.getLocationState(new Location(i - 1, j + 1)) == LocationState.EMPTY
                        && b.getLocationState(new Location(i - 2, j + 2)) == LocationState.EMPTY)
                    score += twoInRow * d;
                //    0
                //   x
                //  x
                // 0
                else if (b.getLocationState(new Location(i, j)) == LocationState.EMPTY
                        && b.getLocationState(new Location(i - 3, j + 3)) == LocationState.EMPTY
                        && b.getLocationState(new Location(i - 1, j + 1)) == p.getPlayerState()
                        && b.getLocationState(new Location(i - 2, j + 2)) == p.getPlayerState())
                    score += twoInRow * d;
                //    0
                //   x
                //  0
                // x
                else if (b.getLocationState(new Location(i, j)) == LocationState.EMPTY
                        && b.getLocationState(new Location(i - 2, j + 2)) == LocationState.EMPTY
                        && b.getLocationState(new Location(i - 1, j + 1)) == p.getPlayerState()
                        && b.getLocationState(new Location(i - 3, j + 3)) == p.getPlayerState())
                    score += twoInRow * d;
                //    0
                //   0
                //  x
                // x
                else if (b.getLocationState(new Location(i, j)) == LocationState.EMPTY
                        && b.getLocationState(new Location(i - 1, j + 1)) == LocationState.EMPTY
                        && b.getLocationState(new Location(i - 2, j + 2)) == p.getPlayerState()
                        && b.getLocationState(new Location(i - 3, j + 3)) == p.getPlayerState())
                    score += twoInRow * d;
                //    x
                //   0
                //  x
                // 0
                else if (b.getLocationState(new Location(i, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i - 2, j + 2)) == p.getPlayerState()
                        && b.getLocationState(new Location(i - 1, j + 1)) == LocationState.EMPTY
                        && b.getLocationState(new Location(i - 3, j + 3)) == LocationState.EMPTY)
                    score += twoInRow * d;
            }
        }

        //Check for backward diagonal 2 in a rows
        for (int i = 0; i < b.getNoCols() - 3; i++) {
            for (int j = b.getNoRows() - 4; j >= 0; j--) {
                // x
                //  x
                //   0
                //    0
                if (b.getLocationState(new Location(i, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 1, j + 1)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 2, j + 2)) == LocationState.EMPTY
                        && b.getLocationState(new Location(i + 3, j + 3)) == LocationState.EMPTY)
                    score += twoInRow * d;
                // x
                //  0
                //   0
                //    x
                else if (b.getLocationState(new Location(i, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 3, j + 3)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 1, j + 1)) == LocationState.EMPTY
                        && b.getLocationState(new Location(i + 2, j + 2)) == LocationState.EMPTY)
                    score += twoInRow * d;
                // 0
                //  x
                //   x
                //    0
                else if (b.getLocationState(new Location(i, j)) == LocationState.EMPTY
                        && b.getLocationState(new Location(i + 3, j + 3)) == LocationState.EMPTY
                        && b.getLocationState(new Location(i + 1, j + 1)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 2, j + 2)) == p.getPlayerState())
                    score += twoInRow * d;
                // 0
                //  x
                //   0
                //    x
                else if (b.getLocationState(new Location(i, j)) == LocationState.EMPTY
                        && b.getLocationState(new Location(i + 2, j + 2)) == LocationState.EMPTY
                        && b.getLocationState(new Location(i + 1, j + 1)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 3, j + 3)) == p.getPlayerState())
                    score += twoInRow * d;
                // 0
                //  0
                //   x
                //    x
                else if (b.getLocationState(new Location(i, j)) == LocationState.EMPTY
                        && b.getLocationState(new Location(i + 1, j + 1)) == LocationState.EMPTY
                        && b.getLocationState(new Location(i + 2, j + 2)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 3, j + 3)) == p.getPlayerState())
                    score += twoInRow * d;
                // x
                //  0
                //   x
                //    0
                else if (b.getLocationState(new Location(i, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 2, j + 2)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 1, j + 1)) == LocationState.EMPTY
                        && b.getLocationState(new Location(i + 3, j + 3)) == LocationState.EMPTY)
                    score += twoInRow * d;
            }
        }

        /******************************************************************************
         *                                                                            *
         *                        3 IN A ROW CHECKS                                   *
         *                                                                            *
         ******************************************************************************/

        //Horizontal checking of 3 in a row
        for (int i = 0; i < b.getNoCols() - 4; i++) {
            for (int j = 0; j < b.getNoRows(); j++) {
                // (xxx0)
                if (b.getLocationState(new Location(i, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 1, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 2, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 3, j)) == LocationState.EMPTY)
                    score += threeInRow * h;
                // (xx0x)
                else if (b.getLocationState(new Location(i, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 1, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 3, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 2, j)) == LocationState.EMPTY)
                    score += threeInRow * h;
                // (x0xx)
                else if (b.getLocationState(new Location(i, j))== p.getPlayerState()
                        && b.getLocationState(new Location(i + 2, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 3, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 1, j)) == LocationState.EMPTY)
                    score += threeInRow * h;
                else if (b.getLocationState(new Location(i, j))== LocationState.EMPTY
                        && b.getLocationState(new Location(i + 1, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 2, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 3, j)) == p.getPlayerState())
                    score += threeInRow * h;
            }
        }

        //Vertical checking of 3 in a row
        // 0
        // x
        // x
        // x
        // Only way it can occur
        for (int i = 0; i < b.getNoCols(); i++) {
            for (int j = b.getNoRows() - 1; j >= 3; j--) {
                if (b.getLocationState(new Location(i, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i, j - 1)) == p.getPlayerState()
                        && b.getLocationState(new Location(i, j - 2)) == p.getPlayerState()
                        && b.getLocationState(new Location(i, j - 3)) == LocationState.EMPTY)
                    score += threeInRow * v;
            }
        }

        //3 in a row checking forward diagonal
        for (int i =  b.getNoCols() - 1; i >= 3; i--) {
            for (int j = b.getNoRows() - 4; j >= 0; j--) {
                //    x
                //   x
                //  x
                // 0
                if (b.getLocationState(new Location(i, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i - 1, j + 1)) == p.getPlayerState()
                        && b.getLocationState(new Location(i - 2, j + 2)) == p.getPlayerState()
                        && b.getLocationState(new Location(i - 3, j + 3)) == LocationState.EMPTY)
                    score += threeInRow * d;
                //    x
                //   x
                //  0
                // x
                else if (b.getLocationState(new Location(i, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i - 1, j + 1)) == p.getPlayerState()
                        && b.getLocationState(new Location(i - 3, j + 3)) == p.getPlayerState()
                        && b.getLocationState(new Location(i - 2, j + 2)) == LocationState.EMPTY)
                    score += threeInRow * d;
                //    0
                //   x
                //  x
                // x
                else if (b.getLocationState(new Location(i, j)) == LocationState.EMPTY
                        && b.getLocationState(new Location(i - 1, j + 1)) == p.getPlayerState()
                        && b.getLocationState(new Location(i - 2, j + 2)) == p.getPlayerState()
                        && b.getLocationState(new Location(i - 3, j + 3)) == p.getPlayerState())
                    score += threeInRow * d;
                //    x
                //   0
                //  x
                // x
                else if (b.getLocationState(new Location(i, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i - 2, j + 2)) == p.getPlayerState()
                        && b.getLocationState(new Location(i - 3, j + 3)) == p.getPlayerState()
                        && b.getLocationState(new Location(i - 1, j + 1)) == LocationState.EMPTY)
                    score += threeInRow * d;
            }
        }

        //Check for backward diagonal 3 in a rows
        for (int i = 0; i < b.getNoCols() - 3; i++) {
            for (int j = b.getNoRows() - 4; j >= 0; j--) {
                // x
                //  x
                //   x
                //    0
                if (b.getLocationState(new Location(i, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 1, j + 1)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 2, j + 2)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 3, j + 3)) == LocationState.EMPTY)
                    score += threeInRow * d;
                // x
                //  x
                //   0
                //    x
                else if (b.getLocationState(new Location(i, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 1, j + 1)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 3, j + 3)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 2, j + 2)) == LocationState.EMPTY)
                    score += threeInRow * d;
                // 0
                //  x
                //   x
                //    x
                else if (b.getLocationState(new Location(i, j)) == LocationState.EMPTY
                        && b.getLocationState(new Location(i + 1, j + 1)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 2, j + 2)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 3, j + 3)) == p.getPlayerState())
                    score += threeInRow * d;
                // x
                //  0
                //   x
                //    x
                else if (b.getLocationState(new Location(i, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 2, j + 2)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 3, j + 3)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 1, j + 1)) == LocationState.EMPTY)
                    score += threeInRow * d;
            }
        }

        return score * DIFFICULTY;
    }

    private void createGame() {
        pmax = new ComputerPlayer20057028(this.getPlayerState());
        LocationState ls = (pmax.getPlayerState() == LocationState.RED) ? LocationState.YELLOW : LocationState.RED;
        pmin = new ComputerPlayer20057028(ls);
    }
}
