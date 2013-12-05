package connect4;

/**
 * 
 * Example Computer Player.
 * CREATE YOUR OWN VERSION OF THIS, REPLACING THE NUMBER IN THE CLASS NAME 
 * WITH YOUR STUDENT NUMBER.
 * @author Frank
 *
 */
public class ComputerPlayer20057028_Random extends IPlayer {

    public ComputerPlayer20057028_Random(LocationState playerState) {
		super(playerState);
	}

    @Override
	public int getMove(Board board) {
        return (int) (Math.random() * 7);
	}



}
