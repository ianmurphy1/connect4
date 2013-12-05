package test;

import connect4.*;

import org.junit.After;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;



/**
 * @author Ian Murphy - 20057028
 *         Date: 05/12/13
 */
public class ComputerPlayer20057028Test {
    ComputerPlayer20057028 p1;
    IPlayer p2;
    Board board;
    Connect4 con4;

    @Before
    public void setUp() throws Exception {
        p1 = new ComputerPlayer20057028(LocationState.RED);
        p2 = new ComputerPlayer20057028(LocationState.YELLOW);
        board = new Board(7, 6);
        con4 = new Connect4(p1, p2, board);
    }

    @After
    public void tearDown() throws Exception {
        board.clear();
    }

    @Test
    public void testOneInRow() {
        board.setLocationState(new Location(0, 5), p1.getPlayerState());
        board.setLocationState(new Location(2, 5), p2.getPlayerState());
        board.setLocationState(new Location(4, 5), p1.getPlayerState());
        //p1.getMove(board);
        assertEquals(200, p1.eval(board, p1));
        board.clear();
    }

    @Test
    public void testTwoInRow() {
        board.setLocationState(new Location(0, 5), p1.getPlayerState());
        board.setLocationState(new Location(2, 5), p2.getPlayerState());
        board.setLocationState(new Location(4, 5), p1.getPlayerState());
        board.setLocationState(new Location(0, 4), p1.getPlayerState());
        //p1.getMove(board);
        assertEquals(1200, p1.eval(board, p1));
        board.clear();
    }

}
