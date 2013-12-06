package connect4;

import edu.princeton.cs.introcs.StdRandom;

/**
 * Class of a player that returns a number between 1 and 7.
 *
 *
 * @author Ian Murphy - 20057028
 *         Date: 05/12/13
 */
public class RandomPlayer extends IPlayer {
    public RandomPlayer(LocationState playerState) {
        super(playerState);

    }

    @Override
    public int getMove(Board board) {
        int x = StdRandom.uniform(0, 7);
        return x;
    }
}
