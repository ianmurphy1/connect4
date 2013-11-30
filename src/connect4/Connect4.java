package connect4;

/**
 * 
 * Class to manage the connect 4 game
 *
 */
public class Connect4 {

	private IPlayer human, computer;
	private Board board;
	private IPlayer currentPlayer;
	private int numTurns = 0;

    public Connect4(IPlayer human, IPlayer computer, Board board) {
        this.human = human;
        this.computer = computer;
        this.board = board;
        this.currentPlayer = human;
    }

	
	/**
	 * Toggles current player 
	 */
	public void nextPlayer() {

		if (currentPlayer == human) 
			currentPlayer = computer;
		else 
			currentPlayer = human;
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
        for (int i = 0; i < board.getNoCols() - 4; i++) {
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
		//TODO
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
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
