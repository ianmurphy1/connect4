import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import connect4.*;

/**
 * @author Ian Murphy - 20057028
 *         Date: 30/11/13
 */
public class DiffTest {
    IPlayer p1, p2;
    Board board;
    Connect4 c4;


    @Before
    public void setup() {
        p1 = new ComputerPlayer20057028(LocationState.YELLOW);
        p2 = new ComputerPlayer20057028(LocationState.RED);
        board = new Board(7, 6);
        c4 = new Connect4(p1, p2, board);
    }

    @After
    public void tearDown() {
        board.clear();
    }

    @Test
    public void testVertWin() {
        board.setLocationState(new Location(0, 1), LocationState.RED);
        board.setLocationState(new Location(0, 2), LocationState.RED);
        board.setLocationState(new Location(0, 3), LocationState.RED);
        board.setLocationState(new Location(0, 4), LocationState.RED);
        c4.nextPlayer();
        assertTrue(c4.isWin(board));
        board.clear();

        board.setLocationState(new Location(0, 1), LocationState.RED);
        board.setLocationState(new Location(0, 2), LocationState.RED);
        board.setLocationState(new Location(0, 3), LocationState.RED);
        assertFalse(c4.isWin(board));
        board.clear();

        board.setLocationState(new Location(4, 1), LocationState.RED);
        board.setLocationState(new Location(4, 2), LocationState.RED);
        board.setLocationState(new Location(4, 3), LocationState.YELLOW);
        board.setLocationState(new Location(4, 4), LocationState.RED);
        assertFalse(c4.isWin(board));
        board.clear();
    }

    @Test
    public void testHorWin() {
        board.setLocationState(new Location(0, 0), LocationState.RED);
        board.setLocationState(new Location(1, 0), LocationState.RED);
        board.setLocationState(new Location(2, 0), LocationState.RED);
        board.setLocationState(new Location(3, 0), LocationState.RED);
        c4.nextPlayer();
        assertTrue(c4.isWin(board));
        board.clear();

        board.setLocationState(new Location(1, 0), LocationState.RED);
        board.setLocationState(new Location(2, 0), LocationState.RED);
        board.setLocationState(new Location(3, 0), LocationState.RED);
        assertFalse(c4.isWin(board));
        board.clear();

        board.setLocationState(new Location(1, 3), LocationState.RED);
        board.setLocationState(new Location(2, 3), LocationState.RED);
        board.setLocationState(new Location(3, 3), LocationState.YELLOW);
        board.setLocationState(new Location(4, 3), LocationState.RED);
        assertFalse(c4.isWin(board));
        board.clear();
    }

    @Test
    public void testForDiagWin() {
        board.setLocationState(new Location(0, 5), LocationState.YELLOW);
        board.setLocationState(new Location(1, 4), LocationState.YELLOW);
        board.setLocationState(new Location(2, 3), LocationState.YELLOW);
        board.setLocationState(new Location(3, 2), LocationState.YELLOW);
        assertTrue(c4.isWin(board));
        board.clear();

        board.setLocationState(new Location(0, 4), LocationState.RED);
        board.setLocationState(new Location(1, 3), LocationState.RED);
        board.setLocationState(new Location(2, 2), LocationState.RED);
        board.setLocationState(new Location(3, 1), LocationState.RED);
        c4.nextPlayer();
        assertTrue(c4.isWin(board));
        board.clear();
    }

    @Test
    public void testBackDiagWin() {
        board.setLocationState(new Location(6, 5), LocationState.YELLOW);
        board.setLocationState(new Location(5, 4), LocationState.YELLOW);
        board.setLocationState(new Location(4, 3), LocationState.YELLOW);
        board.setLocationState(new Location(3, 2), LocationState.YELLOW);
        assertTrue(c4.isWin(board));
        board.clear();

        board.setLocationState(new Location(6, 5), LocationState.YELLOW);
        board.setLocationState(new Location(5, 4), LocationState.YELLOW);
        board.setLocationState(new Location(4, 3), LocationState.RED);
        board.setLocationState(new Location(3, 2), LocationState.YELLOW);
        assertFalse(c4.isWin(board));
        board.clear();
    }

}
