package connect4;

/**
 * 
 * Example Computer Player.
 * CREATE YOUR OWN VERSION OF THIS, REPLACING THE NUMBER IN THE CLASS NAME 
 * WITH YOUR STUDENT NUMBER.
 * @author Frank
 *
 */
public class ComputerPlayer20057028 extends IPlayer {
	
	public ComputerPlayer20057028(LocationState playerState) {
		super(playerState);
	}
	
	@Override
	public int getMove(Board board) {
        int x = (int) Math.random() * 5;
		//TODO
		return x;
		
	}
}
