package net.devk.kalah;

import java.util.Arrays;

/**
 * represents Kalah game , Player One starts the game by default
 * 
 * @see <a href="https://en.wikipedia.org/wiki/Kalah" >Kalah game</a>
 */
public class KalahGame {

	// fixes locations of Kalah storage for two players
	private static final int OPPOSITE_FIT_INDEX_COUNT = 7;
	private static final int PLAYER_ONE_KALAH_INDEX = 6;
	private static final int PLAYER_TWO_KALAH_INDEX = 13;

	// fixed size of game board with two kalah(s)
	private int[] fits = new int[14];

	private Player turn;
	private GameStatus gameStatus;

	public KalahGame(Gems gems) {
		// initializing board
		for (int i = 0; i < fits.length; i++) {
			if (((i + 1) % OPPOSITE_FIT_INDEX_COUNT) != 0) {
				fits[i] = gems.getCount();
			}
		}
		// it seems that there is no difference who starts the game
		turn = Player.PLAYER_ONE;
		gameStatus = GameStatus.PENDING;
	}

	public KalahGame() {
		this(Gems.FOUR_GEMS);
	}

	public int[] getFitsStatus() {
		return fits;
	}

	public GameStatus getGameStatus() {
		return gameStatus;
	}

	public Player getTurn() {
		return turn;
	}

	/**
	 * @param player
	 * @return value of player's kalah
	 */
	public int getPlayerKalahValue(Player player) {
		if (player == Player.PLAYER_ONE)
			return fits[PLAYER_ONE_KALAH_INDEX];
		else
			return fits[PLAYER_TWO_KALAH_INDEX];
	}

	public int[] getPlayerFits(Player player) {
		if (player == Player.PLAYER_ONE) {
			return Arrays.copyOfRange(fits, 0, PLAYER_ONE_KALAH_INDEX - 1);
		} else
			return Arrays.copyOfRange(fits, PLAYER_ONE_KALAH_INDEX + 1, PLAYER_TWO_KALAH_INDEX - 1);
	}

	public int getPlayerGemsCountOnBourd(Player player) {
		int sum = 0;
		if (player == Player.PLAYER_ONE) {
			for (int i = 0; i <= PLAYER_ONE_KALAH_INDEX - 1; i++) {
				sum += fits[i];
			}
		} else {
			for (int i = PLAYER_ONE_KALAH_INDEX + 1; i <= PLAYER_TWO_KALAH_INDEX - 1; i++) {
				sum += fits[i];
			}
		}
		return sum;
	}

	// fit numbers start with zero
	// TODO change RuntimeException with meaningful one
	public void play(Player player, int fitNumber) {
		// check to see if game is running or finished
		if (gameStatus != GameStatus.PENDING) {
			throw new RuntimeException(String.format("the game is over , status is %s", gameStatus.name()));
		}
		// turn validation
		if (turn != player)
			throw new IllegalTurnException();
		// fit number validation for the given user
		if ((turn == Player.PLAYER_ONE) && ((fitNumber < 0) || (fitNumber > PLAYER_ONE_KALAH_INDEX - 1)))
			throw new IllegalPlayException(String.format("Illegal play for player one with fit number %d", fitNumber));

		if ((turn == Player.PLAYER_TWO)
				&& ((fitNumber < PLAYER_ONE_KALAH_INDEX + 1) || (fitNumber > PLAYER_TWO_KALAH_INDEX - 1)))
			throw new IllegalPlayException(String.format("Illegal play for player two with fit number %d", fitNumber));
		// no gems to play
		if (fits[fitNumber] == 0)
			throw new IllegalPlayException(String.format("fit with number %d does not have any gems.", fitNumber));

		final int gemCount = fits[fitNumber];
		// drain the fit and play with its gems
		fits[fitNumber] = 0;
		// player must skip the oponent's kalah
		int skipIndex = turn == Player.PLAYER_ONE ? PLAYER_TWO_KALAH_INDEX : PLAYER_ONE_KALAH_INDEX;
		// we need the index of last fit for applying game rules
		int lastIndex = 0;
		// drop each gem on next fits
		for (int i = 1; i <= gemCount; i++) {
			// we have to call fixIndex whenever index is incremented
			int index = fixIndex(fitNumber + i);
			if (fits[index] == skipIndex) {
				index = fixIndex(index += 1);
			}
			// put gem in the fit
			fits[index]++;
			lastIndex = index;
		}
		// applying game rules

		switch (turn) {
		case PLAYER_ONE: {
			// empty fit rule
			if (lastIndex < PLAYER_ONE_KALAH_INDEX && fits[lastIndex] == 1) {
				fits[PLAYER_ONE_KALAH_INDEX] += (fits[lastIndex + OPPOSITE_FIT_INDEX_COUNT] + 1);
				fits[lastIndex] = 0;
				fits[lastIndex + OPPOSITE_FIT_INDEX_COUNT] = 0;
				// avoids additional below check
				turn = Player.PLAYER_TWO;
				break;
			}
			if (lastIndex == PLAYER_ONE_KALAH_INDEX) {
				turn = Player.PLAYER_ONE;
			} else {
				turn = Player.PLAYER_TWO;
			}

			break;
		}
		case PLAYER_TWO: {
			if ((lastIndex > PLAYER_ONE_KALAH_INDEX) && (lastIndex < PLAYER_TWO_KALAH_INDEX) && fits[lastIndex] == 1) {
				fits[PLAYER_TWO_KALAH_INDEX] += (fits[lastIndex - OPPOSITE_FIT_INDEX_COUNT] + 1);
				fits[lastIndex] = 0;
				fits[lastIndex - OPPOSITE_FIT_INDEX_COUNT] = 0;
				// avoids additional below check
				turn = Player.PLAYER_ONE;
				break;
			}
			if (lastIndex == PLAYER_TWO_KALAH_INDEX) {
				turn = Player.PLAYER_TWO;
			} else {
				turn = Player.PLAYER_ONE;
			}
		}
		default:
			break;
		}

		checkGameStatus();

	}

	/**
	 * @param index
	 * @return fixed position of index on fits array
	 */
	private int fixIndex(int index) {
		if (index > fits.length - 1)
			return index %= fits.length;
		else
			return index;
	}

	/**
	 * checks to see if the game is over and sets the winner , the game is over
	 * while all fits for each player is empty
	 */
	private void checkGameStatus() {

		int playerOneGemsOnBoard = getPlayerGemsCountOnBourd(Player.PLAYER_ONE);
		int playerTwoGemsOnBoard = getPlayerGemsCountOnBourd(Player.PLAYER_TWO);

		if (playerOneGemsOnBoard == 0) {
			fits[PLAYER_TWO_KALAH_INDEX] += playerTwoGemsOnBoard;
			setGameResultStatus();
		} else if (playerTwoGemsOnBoard == 0) {
			fits[PLAYER_ONE_KALAH_INDEX] += playerOneGemsOnBoard;
			setGameResultStatus();
		}

	}

	/**
	 * the winner is whoever has more gems in its kalah otherwise the game has no
	 * winner
	 */
	private void setGameResultStatus() {
		if (fits[PLAYER_ONE_KALAH_INDEX] > fits[PLAYER_TWO_KALAH_INDEX]) {
			gameStatus = GameStatus.PLAYER_ONE_WON;
		}
		if (fits[PLAYER_ONE_KALAH_INDEX] == fits[PLAYER_TWO_KALAH_INDEX]) {
			gameStatus = GameStatus.EQUAL;
		}
		if (fits[PLAYER_ONE_KALAH_INDEX] < fits[PLAYER_TWO_KALAH_INDEX]) {
			gameStatus = GameStatus.PLAYER_TWO_WON;
		}

	}

}
