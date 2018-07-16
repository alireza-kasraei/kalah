package net.devk.kalah;

import org.junit.Assert;
import org.junit.Test;

public class KalahGameTests {

	@Test
	public void testInitialGemsCount() {
		KalahGame kalahGame = new KalahGame();
		int[] playerOnePits = kalahGame.getPlayerPits(Player.PLAYER_ONE);
		int[] playerTwoPits = kalahGame.getPlayerPits(Player.PLAYER_TWO);
		for (int i = 0; i < playerOnePits.length; i++) {
			Assert.assertEquals(Gems.FOUR_GEMS.getCount(), playerOnePits[i]);
		}
		for (int i = 0; i < playerTwoPits.length; i++) {
			Assert.assertEquals(Gems.FOUR_GEMS.getCount(), playerTwoPits[i]);
		}
		Assert.assertEquals(0, kalahGame.getPlayerKalahValue(Player.PLAYER_ONE));
		Assert.assertEquals(0, kalahGame.getPlayerKalahValue(Player.PLAYER_TWO));

		Assert.assertEquals(Player.PLAYER_ONE, kalahGame.getTurn());

		Assert.assertEquals(GameStatus.PENDING, kalahGame.getGameStatus());

	}

	@Test(expected = IllegalPlayException.class)
	public void testStarterPlayerTurn() {
		KalahGame kalahGame = new KalahGame();
		kalahGame.play(7);
	}

	@Test
	public void testPlayerTwoTurn() {
		KalahGame kalahGame = new KalahGame();
		kalahGame.play(3);
		Assert.assertEquals(Player.PLAYER_TWO, kalahGame.getTurn());
		// new game
		kalahGame = new KalahGame();
		kalahGame.play(2);
		Assert.assertEquals(Player.PLAYER_ONE, kalahGame.getTurn());
	}

	@Test
	public void testPlayerOneEmptyPitRules() {
		KalahGame kalahGame = new KalahGame();
		kalahGame.play(4);
		kalahGame.play(7);
		kalahGame.play(0);
		Assert.assertEquals(7, kalahGame.getPlayerKalahValue(Player.PLAYER_ONE));

	}

	@Test
	public void testPlayerTurnAround() {
		KalahGame kalahGame = new KalahGame();
		kalahGame.play(2);
		kalahGame.play(3);
		kalahGame.play(12);
		Assert.assertEquals(1, kalahGame.getPlayerKalahValue(Player.PLAYER_TWO));
		Assert.assertEquals(2, kalahGame.getPlayerKalahValue(Player.PLAYER_ONE));
	}

}
