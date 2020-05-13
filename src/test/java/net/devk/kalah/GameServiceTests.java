package net.devk.kalah;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import net.devk.kalah.game.GameNotFoundException;
import net.devk.kalah.game.GameService;
import net.devk.kalah.game.IllegalPlayException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GameServiceTests {

	@Autowired
	GameService gameService;

	private static Map<String, String> firstStepExpectedMap = new HashMap<>();
	private static Map<String, String> secondStepExpectedMap = new HashMap<>();
	private static Map<String, String> thirdStepExpectedMap = new HashMap<>();

	@BeforeClass
	public static void init() {
		firstStepExpectedMap.put("1", "0");
		firstStepExpectedMap.put("2", "7");
		firstStepExpectedMap.put("3", "7");
		firstStepExpectedMap.put("4", "7");
		firstStepExpectedMap.put("5", "7");
		firstStepExpectedMap.put("6", "7");
		firstStepExpectedMap.put("7", "1");
		firstStepExpectedMap.put("8", "6");
		firstStepExpectedMap.put("9", "6");
		firstStepExpectedMap.put("10", "6");
		firstStepExpectedMap.put("11", "6");
		firstStepExpectedMap.put("12", "6");
		firstStepExpectedMap.put("13", "6");
		firstStepExpectedMap.put("14", "0");

		secondStepExpectedMap.put("1", "0");
		secondStepExpectedMap.put("2", "7");
		secondStepExpectedMap.put("3", "7");
		secondStepExpectedMap.put("4", "7");
		secondStepExpectedMap.put("5", "7");
		secondStepExpectedMap.put("6", "0");
		secondStepExpectedMap.put("7", "2");
		secondStepExpectedMap.put("8", "7");
		secondStepExpectedMap.put("9", "7");
		secondStepExpectedMap.put("10", "7");
		secondStepExpectedMap.put("11", "7");
		secondStepExpectedMap.put("12", "7");
		secondStepExpectedMap.put("13", "7");
		secondStepExpectedMap.put("14", "0");

		thirdStepExpectedMap.put("1", "1");
		thirdStepExpectedMap.put("2", "7");
		thirdStepExpectedMap.put("3", "7");
		thirdStepExpectedMap.put("4", "7");
		thirdStepExpectedMap.put("5", "7");
		thirdStepExpectedMap.put("6", "0");
		thirdStepExpectedMap.put("7", "2");
		thirdStepExpectedMap.put("8", "0");
		thirdStepExpectedMap.put("9", "8");
		thirdStepExpectedMap.put("10", "8");
		thirdStepExpectedMap.put("11", "8");
		thirdStepExpectedMap.put("12", "8");
		thirdStepExpectedMap.put("13", "8");
		thirdStepExpectedMap.put("14", "1");
	}

	@Test
	public void testCorrentPlay() {
		String gameId = gameService.createGame();
		assertThat(gameService.move(gameId, 1), is(firstStepExpectedMap));
		assertThat(gameService.move(gameId, 6), is(secondStepExpectedMap));
		assertThat(gameService.move(gameId, 8), is(thirdStepExpectedMap));
	}

	@Test(expected = IllegalPlayException.class)
	public void testWrongPlay() {
		String gameId = gameService.createGame();
		assertThat(gameService.move(gameId, 1), is(firstStepExpectedMap));
		assertThat(gameService.move(gameId, 8), is(secondStepExpectedMap));
	}

	@Test(expected = GameNotFoundException.class)
	public void testInvalidGameId() {
		gameService.move("2", 1);
	}

}
