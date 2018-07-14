package net.devk.kalah;

import org.junit.Assert;
import org.junit.Test;

public class KalahGameTests {

	@Test
	public void testInitialGemsCount() {
		KalahGame kalahGame = new KalahGame();
		int[] playerOneFits = kalahGame.getPlayerFits(Player.PLAYER_ONE);
		int[] playerTwoFits = kalahGame.getPlayerFits(Player.PLAYER_TWO);
		for (int i = 0; i < playerOneFits.length; i++) {
			Assert.assertEquals(Gems.FOUR_GEMS.getCount(), playerOneFits[i]);
		}
		for (int i = 0; i < playerTwoFits.length; i++) {
			Assert.assertEquals(Gems.FOUR_GEMS.getCount(), playerTwoFits[i]);
		}
		Assert.assertEquals(0, kalahGame.getPlayerKalahValue(Player.PLAYER_ONE));
		Assert.assertEquals(0, kalahGame.getPlayerKalahValue(Player.PLAYER_TWO));

		Assert.assertEquals(Player.PLAYER_ONE, kalahGame.getTurn());

		Assert.assertEquals(GameStatus.PENDING, kalahGame.getGameStatus());

	}

	@Test(expected = RuntimeException.class)
	public void testStarterPlayerTurn() {
		KalahGame kalahGame = new KalahGame();
		kalahGame.play(Player.PLAYER_TWO, 7);
	}

	@Test(expected = RuntimeException.class)
	public void testPlayerOneFitNumber() {
		KalahGame kalahGame = new KalahGame();
		kalahGame.play(Player.PLAYER_ONE, 7);
	}

	@Test
	public void testPlayerTwoTurn() {
		KalahGame kalahGame = new KalahGame();
		kalahGame.play(Player.PLAYER_ONE, 3);
		Assert.assertEquals(Player.PLAYER_TWO, kalahGame.getTurn());
		// new game
		kalahGame = new KalahGame();
		kalahGame.play(Player.PLAYER_ONE, 2);
		Assert.assertEquals(Player.PLAYER_ONE, kalahGame.getTurn());
	}

	@Test
	public void testPlayerOneEmptyFitRules() {
		KalahGame kalahGame = new KalahGame();
		kalahGame.play(Player.PLAYER_ONE, 4);
		kalahGame.play(Player.PLAYER_TWO, 7);
		kalahGame.play(Player.PLAYER_ONE, 0);
		Assert.assertEquals(7, kalahGame.getPlayerKalahValue(Player.PLAYER_ONE));

	}

	@Test
	public void testPlayerTurnAround() {
		KalahGame kalahGame = new KalahGame();
		kalahGame.play(Player.PLAYER_ONE, 2);
		kalahGame.play(Player.PLAYER_ONE, 3);
		kalahGame.play(Player.PLAYER_TWO, 12);
		Assert.assertEquals(1, kalahGame.getPlayerKalahValue(Player.PLAYER_TWO));
		Assert.assertEquals(2, kalahGame.getPlayerKalahValue(Player.PLAYER_ONE));
	}

}
