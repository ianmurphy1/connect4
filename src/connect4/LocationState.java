package connect4;

/**
 * Emum class to represent possible location states in a connect 4 board  
 * @author Frank
 *
 */
public enum LocationState {
	EMPTY, RED, YELLOW;

    public LocationState getOpponent() {

        if (this==EMPTY)
            return null;

        if (this==RED)
            return YELLOW;
        else
            return RED;

    }
}
