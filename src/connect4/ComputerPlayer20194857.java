package connect4;

import java.util.ArrayList;

public class ComputerPlayer20194857 extends IPlayer {

	public static final int SCORE[] = { 0, 1, 1, 4, 1, 2, 4, 32, 1, 2, 2, 5, 4,
			5, 32, 10000 };

	public ComputerPlayer20194857(LocationState playerState) {
		super(playerState);

	}

	@Override
	public int getMove(Board board) {
		Heuristic h = new Heuristic();
		Location bestMoveLocation = null;
		int bestScore = Integer.MIN_VALUE;
		int worstScore = Integer.MAX_VALUE;
		ArrayList<Location> possibleMoves = h.getPossibleMoves(board);
		for (Location possibleMove : possibleMoves) {
			int score = h.minimax(possibleMove, board, 6, getPlayerState());
			if (score >= bestScore) {
				bestMoveLocation = possibleMove;
				bestScore = score;
			}
			if (score <= worstScore) {
				worstScore = score;
			}
		}
		
		if (bestScore == worstScore) {
			return possibleMoves.get(possibleMoves.size() / 2).getX();
		} else {
			return bestMoveLocation.getX();
		}
	}

	public class Heuristic {

		public int minimax(Location l, Board b, int depth, LocationState player) {
			
			int score;
			
			b.setLocationState(l, player);
				
			if (Connect4.checkWinner(b) == player) {
				b.setLocationState(l, LocationState.EMPTY);
				return 100000;
			}
			
			if (depth == 0) {
				score = scoreMove(l, b);
				b.setLocationState(l, LocationState.EMPTY);
				return score;
			}
			score = Integer.MAX_VALUE; int temp;
			for (Location opponentLocation : getPossibleMoves(b)) {
				temp = -minimax(opponentLocation, b, depth - 1, player.getOpponent());
				score = Math.min(score,temp);	
			}
			b.setLocationState(l, LocationState.EMPTY);
			return score;
		}


		public ArrayList<Location> getPossibleMoves(Board board) {

			ArrayList<Location> moves = new ArrayList<Location>();
			for (int col = 0; col < board.getNoCols(); col++) {
				for (int i = 0; i < board.getNoRows(); i++) {
					if (board.getLocationState(new Location(col, i)) == LocationState.EMPTY) {
						moves.add(new Location(col, i));
						break;
					}
				}
			}

			return moves;
		}

		public int rowHeuristic(Location location, Board b) {

			LocationState locationState = b.getLocationState(location);

			if (locationState == LocationState.EMPTY)
				return -10000;

			LocationState opponentState = b.getLocationState(location)
					.getOpponent();

			int locationX = location.getX();
			int locationY = location.getY();
			int maxX = b.getNoCols();

			int[] offsets = { 0, 1, 2, 3 };

			int score = 0;

			for (int offset : offsets) {

				int xWithOffset = locationX - offset;

				if (xWithOffset >= 0 && xWithOffset + 3 < maxX) {
					int index = 0;
					int arrayLoc = 0;
					for (int i = xWithOffset; i <= xWithOffset + 3; i++) {

						if (b.getLocationState(new Location(i, locationY)) == locationState) {
							arrayLoc += Math.pow(2, index);
						} else if (b
								.getLocationState(new Location(i, locationY)) == opponentState) {
							arrayLoc = 0;
							break;
						}

						index++;
					}

					score += SCORE[arrayLoc];

				}

			}

			return score;

		}

		public int colHeuristic(Location location, Board b) {

			LocationState locationState = b.getLocationState(location);

			if (locationState == LocationState.EMPTY)
				return -10000;

			LocationState opponentState = b.getLocationState(location)
					.getOpponent();

			int locationX = location.getX();
			int locationY = location.getY();
			int maxY = b.getNoRows();

			int[] offsets = { 0, 1, 2, 3 };

			int score = 0;

			for (int offset : offsets) {
				int index = 0;
				int arrayLoc = 0;
				int yWithOffset = locationY - offset;
				if (yWithOffset >= 0 && yWithOffset + 3 < maxY) {

					for (int i = yWithOffset; i <= yWithOffset + 3; i++) {

						//need to improve this - grab 4 bits and conv to dec.
						if (b.getLocationState(new Location(locationX, i)) == locationState) {
							arrayLoc += Math.pow(2, index);
						} else if (b
								.getLocationState(new Location(locationX, i)) == opponentState) {
							arrayLoc = 0;
							break;
						}
						index++;
					}
					score += SCORE[arrayLoc];

				}

			}

			return score;

		}

		public int diagDownHeuristic(Location location, Board b) {

			LocationState locationState = b.getLocationState(location);

			if (locationState == LocationState.EMPTY)
				return -10000;

			LocationState opponentState = b.getLocationState(location)
					.getOpponent();

			int locationX = location.getX();
			int locationY = location.getY();
			int maxY = b.getNoRows();
			int maxX = b.getNoCols();

			int[] offsets = { 0, 1, 2, 3 };

			int score = 0;

			for (int offset : offsets) {

				int xWithOffset = locationX - offset;
				int yWithOffset = locationY - offset;
				if ((yWithOffset >= 0 && yWithOffset + 3 < maxY)
						&& (xWithOffset >= 0 && xWithOffset + 3 < maxX)) {
					int index = 0;
					int arrayLoc = 0;
					for (int i = yWithOffset; i <= yWithOffset + 3; i++) {

						// Need to improve this
						if (b.getLocationState(new Location(xWithOffset, i)) == locationState) {
							arrayLoc += Math.pow(2, index);
						} else if (b.getLocationState(new Location(xWithOffset,
								i)) == opponentState) {

							arrayLoc = 0;
							break;
						}
						xWithOffset++;
						index++;
					}

					score += SCORE[arrayLoc];
				}

			}

			return score;

		}

		public int diagUpHeuristic(Location location, Board b) {

			LocationState locationState = b.getLocationState(location);

			if (locationState == LocationState.EMPTY)
				return -10000;

			LocationState opponentState = b.getLocationState(location)
					.getOpponent();

			int locationX = location.getX();
			int locationY = location.getY();
			int maxY = b.getNoRows();
			int maxX = b.getNoCols();

			int[] offsets = { 0, 1, 2, 3 };

			int score = 0;

			for (int offset : offsets) {

				int xWithOffset = locationX - offset;
				int yWithOffset = locationY + offset;
				if ((yWithOffset - 3 >= 0 && yWithOffset < maxY)
						&& (xWithOffset >= 0 && xWithOffset + 3 < maxX)) {
					int index = 0;
					int arrayLoc = 0;
					for (int i = yWithOffset; i >= yWithOffset - 3; i--) {

						// Need to improve this
						if (b.getLocationState(new Location(xWithOffset, i)) == locationState) {
							arrayLoc += Math.pow(2, index);
						} else if (b.getLocationState(new Location(xWithOffset,
								i)) == opponentState) {

							arrayLoc = 0;
							break;
						}
						xWithOffset++;
						index++;
					}
					score += SCORE[arrayLoc];
				}

			}

			return score;

		}

		public int scoreMove(Location location, Board b) {
			return (diagDownHeuristic(location, b)
					+ diagUpHeuristic(location, b) + rowHeuristic(location, b)
					+ colHeuristic(location, b));

		}


		

	}

}
