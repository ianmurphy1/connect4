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
public class ComputerPlayer20057028COPY extends IPlayer {

    private IPlayer pmax, pmin;  //pmax = this player, pmin is the player the ai is playing against.
    private Board gameBoard;
    private int bestColumn, bestColumnScore;
    private boolean isWin;
    private final int DEPTH = 5; //Set higher to allow for deeper scans and harder AI
    private final int DIFFICULTY = 4000; //Multiplier for scores to increase difficulty

	public ComputerPlayer20057028COPY(LocationState playerState) {
		super(playerState);
	}

    /**
     *
     * @param board
     * @return
     */
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
        ArrayList<Location> dups = new ArrayList<Location>();
        boolean moreThanOne = false;
        // If column 3 is empty place a piece there
        if (gameBoard.getLocationState(new Location(3, gameBoard.getNoRows() - 1)) == LocationState.EMPTY)
            return 3;

        // if column 3 has other players token, place it in 2
        if (gameBoard.getLocationState(new Location(3, gameBoard.getNoRows() - 1)) == pmin.getPlayerState()
                && gameBoard.getLocationState(new Location(2, gameBoard.getNoRows() - 1)) == LocationState.EMPTY)
            return 2;
        // if col 3 and 2 had opponent tokens play 4
        else if (gameBoard.getLocationState(new Location(3, gameBoard.getNoRows() - 1)) == pmin.getPlayerState()
                && gameBoard.getLocationState(new Location(2, gameBoard.getNoRows() - 1)) == pmin.getPlayerState()
                && gameBoard.getLocationState(new Location(4, gameBoard.getNoRows() - 1)) == LocationState.EMPTY)
            return 4;


        ArrayList<Location> moves = getLegalMoves(gameBoard); // Get locations of legal moves
        bestColumnScore = -Integer.MAX_VALUE; //Set the best column to track which move is best
        for (Location move: moves) {
            //System.out.println("Placing move at: (" + move.getX() + ", " + move.getY() + ").");
            gameBoard.setLocationState(move, pmax.getPlayerState());
            //call to the recursive negamax algorithm
            negamax(gameBoard, DEPTH, -Integer.MAX_VALUE, Integer.MAX_VALUE, 1, pmax);
            int score = negamax(gameBoard, DEPTH, -Integer.MAX_VALUE, Integer.MAX_VALUE, 1, pmax);
            //System.out.println("This moves score is: " + score);
            //if (score == bestColumnScore) {
            //    dups.add(move);
             //   moreThanOne = true;
            //}

            if (score > bestColumnScore) {  //If score form negamax is better than the current column score
                bestColumnScore = score;    //change the best column score
                bestColumn = move.getX();
            }
            gameBoard.setLocationState(move, LocationState.EMPTY);
        }
        //if (moreThanOne) {
            //Random rand = new Random();
           // Collections.shuffle(dups);
           // bestColumn = dups.get(rand.nextInt(dups.size())).getX();
       // }
        //System.out.println("Taking move at: " + bestColumn);
        //if (moreThanOne) System.out.println("From Random Choice.");
        int choice = bestColumn;
        return choice;
	}

    /**
     *
     *
     *
     * @param b
     * @param depth
     * @param alpha
     * @param beta
     * @param sign
     * @param player
     * @return
     */
    private int negamax(Board b, int depth, int alpha, int beta, int sign, IPlayer player) {
        isWin = false;
        //if (isWin(b, player)) return 2147483600; //If move results in win, return biggest value possible
        if (isTerminal(b, player) || depth == 0) {
            return sign * eval(b, player);
        }
        IPlayer opp;
        opp = (pmax.getPlayerState() == player.getPlayerState()) ? pmin : pmax;
        int max = -Integer.MAX_VALUE;
        ArrayList<Location> moves = getLegalMoves(b);
        for (Location move : moves) {
            b.setLocationState(move, player.getPlayerState());
           // System.out.println("Placing move at: (" + move.getX() + ", " + move.getY() + ").");
            int x = -negamax(b, depth - 1, -beta, -alpha, -sign, opp);
            b.setLocationState(move, LocationState.EMPTY);
            if (x > max) {
                max = x;
                if (x > bestColumnScore) {  //If score form negamax is better than the current column score
                    bestColumnScore = x;    //change the best column score
                    bestColumn = move.getX();
                    //System.out.println("This moves score is: " + bestColumnScore);
                    //System.out.println("Placing move at: (" + move.getX() + ").");
                }
            }
            if (x > alpha) alpha = x;
            if (alpha >= beta) return alpha;
        }
        return max;
    }

    private boolean isTerminal(Board b, IPlayer player) {
        if (isDraw(b) || isWin(b, player)) return true;
        return false;
    }

    /**
     * This method returns whether or not a board is at a win state.
     *
     * @param b The state of the board to be played
     * @param p The current player down the tree
     * @return Whether or not the board is at a win state
     */
    public boolean isWin(Board b, IPlayer p) {
        for (int i = 0; i < b.getNoCols() - 3; i++) {
            for (int j = 0; j < b.getNoRows(); j++) {
                if (b.getLocationState(new Location(i, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 1, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 2, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 3, j)) == p.getPlayerState()) {
                    isWin = true;
                    return true;
                }
            }
        }

        for (int i = 0; i < b.getNoCols(); i++) {
            for (int j = b.getNoRows() - 1; j >= 3; j--) {
                if (b.getLocationState(new Location(i, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i, j - 1)) == p.getPlayerState()
                        && b.getLocationState(new Location(i, j - 2)) == p.getPlayerState()
                        && b.getLocationState(new Location(i, j - 3)) == p.getPlayerState()) {
                    isWin = true;
                    return true;
                }
            }
        }

        for (int i = 0; i < b.getNoCols() - 3; i++) {
            for (int j = b.getNoRows() - 4; j >= 0; j--) {
                if (b.getLocationState(new Location(i, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 1, j + 1)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 2, j + 2)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 3, j + 3)) == p.getPlayerState()) {
                    isWin = true;
                    return true;
                }
            }
        }

        for (int i =  b.getNoCols() - 1; i >= 3; i--) {
            for (int j = b.getNoRows() - 4; j >= 0; j--) {
                if (b.getLocationState(new Location(i, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i - 1, j + 1)) == p.getPlayerState()
                        && b.getLocationState(new Location(i - 2, j + 2)) == p.getPlayerState()
                        && b.getLocationState(new Location(i - 3, j + 3)) == p.getPlayerState()) {
                    isWin = true;
                    return true;
                }
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

    public int eval(Board b, IPlayer p) {

        int score = 0;
        //Multipliers based on whether possible rows are vertical, horizontal or diagonal
        int v, d, h;
        v = d = 5;
        h = 8;
        //Scores for tokens in a row
        int oneInRow = 50, twoInRow = 200, threeInRow = 700, winner = 70000000;


        /******************************************************************************
         *                                                                            *
         *                             CHECK WINNER                                   *
         *                                                                            *
         ******************************************************************************/
        if (isWin) {
        //Horizontal Win
        for (int i = 0; i < b.getNoCols() - 3; i++) {
            for (int j = b.getNoRows() - 1; j >= 0; j--) {
                if (b.getLocationState(new Location(i, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 1, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 2, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 3, j)) == p.getPlayerState())
                    score += winner;
            }
        }

        //Vertical win
        for (int i = 0; i < b.getNoCols(); i++) {
            for (int j = b.getNoRows() - 1; j >= 3; j--) {
                if (b.getLocationState(new Location(i, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i, j - 1)) == p.getPlayerState()
                        && b.getLocationState(new Location(i, j - 2)) == p.getPlayerState()
                        && b.getLocationState(new Location(i, j - 3)) == p.getPlayerState())
                    score += winner;
            }
        }

        //Diagonal Win
        for (int i = 0; i < b.getNoCols() - 3; i++) {
            for (int j = b.getNoRows() - 4; j >= 0; j--) {
                if (b.getLocationState(new Location(i, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 1, j + 1)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 2, j + 2)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 3, j + 3)) == p.getPlayerState())
                    score += winner;
            }
        }

        //Diagonal Win
        for (int i =  b.getNoCols() - 1; i >= 3; i--) {
            for (int j = b.getNoRows() - 4; j >= 0; j--) {
                if (b.getLocationState(new Location(i, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i - 1, j + 1)) == p.getPlayerState()
                        && b.getLocationState(new Location(i - 2, j + 2)) == p.getPlayerState()
                        && b.getLocationState(new Location(i - 3, j + 3)) == p.getPlayerState())
                    score += winner * d;
            }
        } }

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

        /******************************************************************************
         *                                                                            *
         *                       3 IN A ROW OPEN CHECKS                               *
         *                                                                            *
         ******************************************************************************/

        // Check for open horizontal (0xxx0)
        for (int i = 0; i < b.getNoCols() - 5; i++) {
            for (int j = 0; j < b.getNoRows(); j++) {
                if (b.getLocationState(new Location(i, j))== LocationState.EMPTY
                        && b.getLocationState(new Location(i + 1, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 2, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 3, j)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 4, j)) == p.getPlayerState())
                    score += 2 * threeInRow * h;
            }
        }

        // Diagonal back
        // 0
        //  x
        //   x
        //    x
        //     0
        for (int i = 0; i < b.getNoCols() - 4; i++) {
            for (int j = b.getNoRows() - 5; j >= 0; j--) {
                if (b.getLocationState(new Location(i, j)) == LocationState.EMPTY
                        && b.getLocationState(new Location(i + 4, j + 4)) == LocationState.EMPTY
                        && b.getLocationState(new Location(i + 1, j + 1)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 2, j + 2)) == p.getPlayerState()
                        && b.getLocationState(new Location(i + 3, j + 3)) == p.getPlayerState())
                    score += 2 * threeInRow * d;
            }
        }

        //Diag Forward
        //     0
        //    x
        //   x
        //  x
        // 0
        for (int i =  b.getNoCols() - 1; i >= 4; i--) {
            for (int j = b.getNoRows() - 5; j >= 0; j--) {
                if (b.getLocationState(new Location(i, j)) == LocationState.EMPTY
                        && b.getLocationState(new Location(i - 4, j + 4)) == LocationState.EMPTY
                        && b.getLocationState(new Location(i - 1, j + 1)) == p.getPlayerState()
                        && b.getLocationState(new Location(i - 2, j + 2)) == p.getPlayerState()
                        && b.getLocationState(new Location(i - 3, j + 3)) == p.getPlayerState())
                    score += 2 * threeInRow * d;
            }
        }
        return score * DIFFICULTY;
    }

    private void createGame() {
        pmax = this;
        LocationState ls = (this.getPlayerState() == LocationState.RED) ? LocationState.YELLOW : LocationState.RED;
        pmin = new ComputerPlayer20057028COPY(ls);
    }

    public int getDIFFICULTY() {
        return DIFFICULTY;
    }
}
