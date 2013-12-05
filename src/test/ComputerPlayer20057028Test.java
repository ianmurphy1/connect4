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
        assertEquals(1000 * p1.getDIFFICULTY(), p1.eval(board, p1));
        board.clear();
    }

    @Test
    public void testTwoInRow() {
        board.setLocationState(new Location(0, 5), p1.getPlayerState());
        board.setLocationState(new Location(2, 5), p2.getPlayerState());
        board.setLocationState(new Location(4, 5), p1.getPlayerState());
        board.setLocationState(new Location(0, 4), p1.getPlayerState());
        //p1.getMove(board);
        assertEquals(3500 * p1.getDIFFICULTY(), p1.eval(board, p1));
        board.clear();

        //diag
        board.setLocationState(new Location(4, 3), p2.getPlayerState());
        board.setLocationState(new Location(3, 4), p2.getPlayerState());
        board.setLocationState(new Location(3, 5), p1.getPlayerState());
        board.setLocationState(new Location(4, 4), p1.getPlayerState());
        assertEquals(5000 * p1.getDIFFICULTY(), p1.eval(board, p1));
        board.clear();
    }

    @Test
    public void threeInRow() {
        //hor
        board.setLocationState(new Location(3, 3), p1.getPlayerState());
        board.setLocationState(new Location(4, 3), p1.getPlayerState());
        board.setLocationState(new Location(5, 3), p1.getPlayerState());
        //3 one in a row = 1500
        //1 horizontal 2 in a row = 7500
        //1 horizontal 3 in a row = 75000
        assertEquals(84000 * p1.getDIFFICULTY(), p1.eval(board, p1));
        board.clear();
    }

    @Test
    public void testWin() {
        board.setLocationState(new Location(3, 3), p1.getPlayerState());
        board.setLocationState(new Location(4, 3), p1.getPlayerState());
        board.setLocationState(new Location(5, 3), p1.getPlayerState());
        board.setLocationState(new Location(6, 3), p1.getPlayerState());
        assertTrue(board.getLocationState(new Location(6,3)) == LocationState.RED);
        assertTrue(p1.isWin(board, p1));
    }
}
