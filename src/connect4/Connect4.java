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
		//TODO
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

		for (int i = 0; i < board.getNoRows(); i++) {
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
