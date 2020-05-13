package net.devk.kalah.game;

import java.util.Arrays;

/**
 * represents Kalah game , Player One starts the game by default
 * 
 * @see <a href="https://en.wikipedia.org/wiki/Kalah" >Kalah game</a>
 */
public class KalahGame {

	// fixes locations of Kalah storage for two players
	private static final int OPPOSITE_PIT_INDEX_COUNT = 7;
	private static final int PLAYER_ONE_KALAH_INDEX = 6;
	private static final int PLAYER_TWO_KALAH_INDEX = 13;

	// fixed size of game board with two kalah(s)
	private int[] pits = new int[14];

	private Player turn;
	private GameStatus gameStatus;

	public KalahGame(Gems gems) {
		// initializing board
		for (int i = 0; i < pits.length; i++) {
			if (((i + 1) % OPPOSITE_PIT_INDEX_COUNT) != 0) {
				pits[i] = gems.getCount();
			}
		}
		// it seems that there is no difference who starts the game
		turn = Player.PLAYER_ONE;
		gameStatus = GameStatus.PENDING;
	}

	public KalahGame() {
		this(Gems.FOUR_GEMS);
	}

	public int[] getPitsStatus() {
		return pits;
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
			return pits[PLAYER_ONE_KALAH_INDEX];
		else
			return pits[PLAYER_TWO_KALAH_INDEX];
	}

	public int[] getPlayerPits(Player player) {
		if (player == Player.PLAYER_ONE) {
			return Arrays.copyOfRange(pits, 0, PLAYER_ONE_KALAH_INDEX - 1);
		} else
			return Arrays.copyOfRange(pits, PLAYER_ONE_KALAH_INDEX + 1, PLAYER_TWO_KALAH_INDEX - 1);
	}

	public int getPlayerGemsCountOnBourd(Player player) {
		int sum = 0;
		if (player == Player.PLAYER_ONE) {
			for (int i = 0; i <= PLAYER_ONE_KALAH_INDEX - 1; i++) {
				sum += pits[i];
			}
		} else {
			for (int i = PLAYER_ONE_KALAH_INDEX + 1; i <= PLAYER_TWO_KALAH_INDEX - 1; i++) {
				sum += pits[i];
			}
		}
		return sum;
	}

	// pitIndex start with zero
	public void play(int pitIndex) {
		// check to see if game is running or finished
		if (gameStatus != GameStatus.PENDING) {
			throw new IllegalPlayException(String.format("the game is over , status is %s", gameStatus.name()));
		}
		// pit index validation for the given user
		if ((turn == Player.PLAYER_ONE) && ((pitIndex < 0) || (pitIndex >= PLAYER_ONE_KALAH_INDEX)))
			throw new IllegalPlayException(String.format("Illegal play for player one with pit index %d", pitIndex));

		if ((turn == Player.PLAYER_TWO)
				&& ((pitIndex <= PLAYER_ONE_KALAH_INDEX) || (pitIndex >= PLAYER_TWO_KALAH_INDEX)))
			throw new IllegalPlayException(String.format("Illegal play for player two with pit index %d", pitIndex));
		// no gems to play
		if (pits[pitIndex] == 0)
			throw new IllegalPlayException(String.format("pit with index %d does not have any gems.", pitIndex));

		final int gemCount = pits[pitIndex];
		// drain the pit and play with its gems
		pits[pitIndex] = 0;
		// player must skip the oponent's kalah
		int skipIndex = turn == Player.PLAYER_ONE ? PLAYER_TWO_KALAH_INDEX : PLAYER_ONE_KALAH_INDEX;
		// we need the index of last pit for applying game rules
		int lastIndex = 0;
		// drop each gem on next pits
		for (int i = 1; i <= gemCount; i++) {
			// we have to call fixIndex whenever index is incremented
			int index = fixIndex(pitIndex + i);
			if (pits[index] == skipIndex) {
				index = fixIndex(index += 1);
			}
			// put gem in the pit
			pits[index]++;
			lastIndex = index;
		}
		// applying game rules

		switch (turn) {
		case PLAYER_ONE: {
			// empty pit rule
			if (lastIndex < PLAYER_ONE_KALAH_INDEX && pits[lastIndex] == 1) {
				pits[PLAYER_ONE_KALAH_INDEX] += (pits[lastIndex + OPPOSITE_PIT_INDEX_COUNT] + 1);
				pits[lastIndex] = 0;
				pits[lastIndex + OPPOSITE_PIT_INDEX_COUNT] = 0;
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
			if ((lastIndex > PLAYER_ONE_KALAH_INDEX) && (lastIndex < PLAYER_TWO_KALAH_INDEX) && pits[lastIndex] == 1) {
				pits[PLAYER_TWO_KALAH_INDEX] += (pits[lastIndex - OPPOSITE_PIT_INDEX_COUNT] + 1);
				pits[lastIndex] = 0;
				pits[lastIndex - OPPOSITE_PIT_INDEX_COUNT] = 0;
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
	 * @return fixed position of index on pits array
	 */
	private int fixIndex(int index) {
		if (index > pits.length - 1)
			return index %= pits.length;
		else
			return index;
	}

	/**
	 * checks to see if the game is over and sets the winner , the game is over
	 * while all pits for each player is empty
	 */
	private void checkGameStatus() {

		int playerOneGemsOnBoard = getPlayerGemsCountOnBourd(Player.PLAYER_ONE);
		int playerTwoGemsOnBoard = getPlayerGemsCountOnBourd(Player.PLAYER_TWO);

		if (playerOneGemsOnBoard == 0) {
			pits[PLAYER_TWO_KALAH_INDEX] += playerTwoGemsOnBoard;
			setGameResultStatus();
		} else if (playerTwoGemsOnBoard == 0) {
			pits[PLAYER_ONE_KALAH_INDEX] += playerOneGemsOnBoard;
			setGameResultStatus();
		}

	}

	/**
	 * the winner is whoever has more gems in its kalah otherwise the game has no
	 * winner
	 */
	private void setGameResultStatus() {
		if (pits[PLAYER_ONE_KALAH_INDEX] > pits[PLAYER_TWO_KALAH_INDEX]) {
			gameStatus = GameStatus.PLAYER_ONE_WON;
		}
		if (pits[PLAYER_ONE_KALAH_INDEX] == pits[PLAYER_TWO_KALAH_INDEX]) {
			gameStatus = GameStatus.EQUAL;
		}
		if (pits[PLAYER_ONE_KALAH_INDEX] < pits[PLAYER_TWO_KALAH_INDEX]) {
			gameStatus = GameStatus.PLAYER_TWO_WON;
		}

	}

}
