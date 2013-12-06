package connect4;

//import edu.princeton.cs.introcs.StdOut;


import edu.princeton.cs.introcs.StdIn;
import edu.princeton.cs.introcs.StdOut;
import edu.princeton.cs.introcs.Stopwatch;

import java.util.InputMismatchException;

/**
 * 
 * Class to manage the connect 4 game
 *
 */
public class Connect4 {


    private final String RED =    "R";
    private final String YELLOW = "Y";
    private final String EMPTY =  " ";


	private IPlayer p1, p2;
	private Board board;
	private IPlayer currentPlayer;
	private int numTurns = 0;

    public Connect4(IPlayer p1, IPlayer p2, Board board) {
        this.p1 = p1;
        this.p2 = p2;
        this.board = board;
        this.currentPlayer = p1;
    }

	
	/**
	 * Toggles current player 
	 */
	public void nextPlayer() {
		if (currentPlayer == p1)
			currentPlayer = p2;
		else 
			currentPlayer = p1;
	}

	/**
	 * Checks if there's a winner
	 * @param board to evaluate for winner 
	 * @return boolean to detect winner
	 */
	public boolean isWin(Board board) {
       //if (numTurns < 7) return false;
		return checkVertical(board)|| checkHorizontal(board) || checkDiagFor(board) || checkDiagBack(board);
	}

    private boolean checkHorizontal(Board board) {
        for (int i = 0; i < board.getNoCols() - 3; i++) {
            for (int j = 0; j < board.getNoRows(); j++) {
                if (board.getLocationState(new Location(i, j)) == currentPlayer.getPlayerState()
                        && board.getLocationState(new Location(i + 1, j)) == currentPlayer.getPlayerState()
                        && board.getLocationState(new Location(i + 2, j)) == currentPlayer.getPlayerState()
                        && board.getLocationState(new Location(i + 3, j)) == currentPlayer.getPlayerState())
                    return true;
            }
        }
        return false;
    }

    private boolean checkVertical(Board board) {
        for (int i = 0; i < board.getNoCols(); i++) {
            for (int j = board.getNoRows() - 1; j >= 3; j--) {
                if (board.getLocationState(new Location(i, j)) == currentPlayer.getPlayerState()
                        && board.getLocationState(new Location(i, j - 1)) == currentPlayer.getPlayerState()
                        && board.getLocationState(new Location(i, j - 2)) == currentPlayer.getPlayerState()
                        && board.getLocationState(new Location(i, j - 3)) == currentPlayer.getPlayerState())
                    return true;
            }
        }
        return false;
    }

    private boolean checkDiagBack(Board board) {
        for (int i = 0; i < board.getNoCols() - 3; i++) {
            for (int j = board.getNoRows() - 4; j >= 0; j--) {
                if (board.getLocationState(new Location(i, j)) == currentPlayer.getPlayerState()
                        && board.getLocationState(new Location(i + 1, j + 1)) == currentPlayer.getPlayerState()
                        && board.getLocationState(new Location(i + 2, j + 2)) == currentPlayer.getPlayerState()
                        && board.getLocationState(new Location(i + 3, j + 3)) == currentPlayer.getPlayerState())
                    return true;
            }
        }
        return false;
    }

    private boolean checkDiagFor(Board board) {
        for (int i =  board.getNoCols() - 1; i >= 3; i--) {
            for (int j = board.getNoRows() - 4; j >= 0; j--) {
                if (board.getLocationState(new Location(i, j)) == currentPlayer.getPlayerState()
                        && board.getLocationState(new Location(i - 1, j + 1)) == currentPlayer.getPlayerState()
                        && board.getLocationState(new Location(i - 2, j + 2)) == currentPlayer.getPlayerState()
                        && board.getLocationState(new Location(i - 3, j + 3)) == currentPlayer.getPlayerState())
                    return true;
            }
        }
        return false;
    }

    /**
	 * Checks for a draw
	 * @return
	 */
	public boolean isDraw() {
		return numTurns == board.getNoCols() * board.getNoRows();
	}

	/**
	 * Method called to get next move from player
	 * 
	 * @return boolean indicating move take successfully
	 */
	public boolean takeTurn() {
		int col = currentPlayer.getMove(board);

		for (int i = board.getNoRows() - 1; i >= 0; i--) {
			if (board.getLocationState(new Location(col, i)) == LocationState.EMPTY) {
				board.setLocationState(new Location(col, i),
						currentPlayer.getPlayerState());
				numTurns++;
				return true;
			}
		}
		return false;
	}

	public Board getBoard() {
		return board;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws InterruptedException {
        Board board = new Board(7, 6);
        IPlayer player1, player2;
        player1 = player2 = null;
        Connect4 connect4 = new Connect4(player1, player2, board);
        connect4.run();
	}

    private void run() throws InterruptedException {
        int option = setUpMenu();
        createGame(option);
        this.currentPlayer = p1;
        StdOut.println("Player one: " + p1.getPlayerState());
        StdOut.println("Player two: " + p2.getPlayerState());
        StdOut.println("Current: " + currentPlayer.getPlayerState());
        while (true) {
            printGrid();
            while(true) {
                if (takeTurn()) {
                    if (currentPlayer instanceof ComputerPlayer20057028) Thread.sleep(1000);
                    break;
                }
            }
            if (isDraw()) {
                StdOut.println("Game is a draw!!");
                break;
            }
            if (isWin(board)) {
                printGrid();
                StdOut.println("\nWinner is: " + currentPlayer.getPlayerState());
                break;
            }
            nextPlayer();
        }

    }

    private void printGrid() {
        StdOut.println();
        for (int i = 0; i < board.getNoRows(); i++) {
            StdOut.print(i + " ");
            for (int j = 0; j < board.getNoCols(); j++) {
                StdOut.print("| ");
                if (board.getLocationState(new Location(j, i)) == LocationState.RED)
                    StdOut.print(RED);
                else if (board.getLocationState(new Location(j, i)) == LocationState.YELLOW)
                    StdOut.print(YELLOW);
                else if (board.getLocationState(new Location(j, i)) == LocationState.EMPTY)
                    StdOut.print(EMPTY);
                //StdOut.print(" ");

                if (j == board.getNoCols() - 1)StdOut.print("|");
            }

           // StdOut.print(" |");
           StdOut.println();
        }
        for(int i = 0; i < board.getNoCols(); i ++) {
            if (i == 0) StdOut.print("   [" + i + "]");
            if (i > 0)StdOut.print("[" + i + "]");
        }
        StdOut.println();
    }

    private void createGame(int choice) {
        switch (choice) {
            case 1:
                this.p1 = new HumanPlayer(LocationState.RED);
                this.p2 = new ComputerPlayer20057028(LocationState.YELLOW);
                break;
            case 2:
                this.p1 = new ComputerPlayer20057028(LocationState.RED);
                this.p2 = new HumanPlayer(LocationState.YELLOW);
                break;
            case 3:
                this.p1 = new HumanPlayer(LocationState.RED);
                this.p2 = new HumanPlayer(LocationState.YELLOW);
                break;
            case 4:
                this.p1 = new ComputerPlayer20057028(LocationState.RED);
                this.p2 = new ComputerPlayer20057028(LocationState.YELLOW);
                break;
            case 5:
                this.p1 = new RandomPlayer(LocationState.RED);
                this.p2 = new ComputerPlayer20057028(LocationState.YELLOW);
                break;
            default:
                setUpMenu();
                break;
        }
    }

    private static int setUpMenu() {
        StdOut.println(" _____________________________________ ");
        StdOut.println("|                                     | ");
        StdOut.println("|             CONNECT 4               | ");
        StdOut.println("|                                     | ");
        StdOut.println("| | | | | | | | | | | | | | | | | | | | ");
        StdOut.println("|           Choose type of game       | ");
        StdOut.println("|                                     | ");
        StdOut.println("|\t1: Human vs Computer               | ");
        StdOut.println("|\t2: Computer vs Human               | ");
        StdOut.println("|\t3: Human vs Human                  | ");
        StdOut.println("|\t4: Computer vs Computer            | ");
        StdOut.println("|\t5: DumbPlayer vs Computer          | ");
        StdOut.println(" _____________________________________ ");
        int choice = getInt("|\tChoose Option:                     | ");
        StdOut.println("|                                     | ");
        StdOut.println("|                                     | ");
        StdOut.println(" _____________________________________ ");

        return choice;
    }




    private static int getInt(String prompt) throws InputMismatchException {
        StdOut.println(prompt);
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

