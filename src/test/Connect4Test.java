package test;

import connect4.Board;
import connect4.IPlayer;
import connect4.Location;
import connect4.LocationState;
import org.junit.Test;

import static org.junit.*;

public class Connect4Test {

	@Test
	public void testBoard() {
		Board board = new Board(8, 7);
		assertTrue(board.getLocationState(new Location(0, 0)) == LocationState.EMPTY);
		board.setLocationState(new Location(7, 6), LocationState.RED);
		assertTrue(board.getLocationState(new Location(7, 6)) == LocationState.RED);
	}

	@Test
	public void testMakeMove() {
	
	}

	

	/**
	 * THIS WILL FAIL UNTIL YOU CREATE YOUR COMPUTER PLAYER
	 */
	@Test
	public void testComputerPlayer() {
		Board board = new Board(8, 7);

		assertTrue(board.getLocationState(new Location(0, 0)) == LocationState.EMPTY);

		//CHANGE THIS TO INCLUDE YOUR COMPUTER PLAYER 
		IPlayer computerPlayer = new ComputerPlayer20057028(LocationState.YELLOW);

		int move = computerPlayer.getMove(board);

		assertTrue(move >= 0 && move <= board.getNoCols());

		assertTrue(board.getLocationState(new Location(move, 0)) == LocationState.EMPTY);

		board.setLocationState(new Location(move, 0), LocationState.YELLOW);

		assertTrue(board.getLocationState(new Location(move, 0)) == LocationState.YELLOW);

	}

}
