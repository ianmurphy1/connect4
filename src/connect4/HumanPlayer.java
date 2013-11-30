package connect4;

public class HumanPlayer extends IPlayer {

    public HumanPlayer(LocationState playerState) {
        super(playerState);
    }

    @Override
    public int getMove(Board board) {
        return 0;
    }

}