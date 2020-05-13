package net.devk.kalah;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import net.devk.kalah.game.GameController;
import net.devk.kalah.game.GameService;
import net.devk.kalah.game.IllegalPlayException;

@RunWith(SpringRunner.class)
@WebMvcTest(GameController.class)
public class GameControllerTests {

	@MockBean
	private GameService gameService;

	@Autowired
	private MockMvc mockMvc;

	private static Map<String, String> firstStepExpectedMap = new HashMap<>();

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
	}

	@Test
	public void testCreateGame() throws Exception {
		String content = "{\"id\":\"1234\",\"uri\":\"http://localhost/games/1234\"}";
		given(this.gameService.createGame()).willReturn("1234");
		this.mockMvc.perform(post("/games").accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
				.andExpect(content().json(content));
	}

	@Test
	public void testMove() throws Exception {
		String content = "{\"id\":\"1234\",\"uri\":\"http://localhost/games/1234\",\"status\":{\"11\":\"6\",\"12\":\"6\",\"13\":\"6\",\"14\":\"0\",\"1\":\"0\",\"2\":\"7\",\"3\":\"7\",\"4\":\"7\",\"5\":\"7\",\"6\":\"7\",\"7\":\"1\",\"8\":\"6\",\"9\":\"6\",\"10\":\"6\"}}";
		given(this.gameService.move("1234", 1)).willReturn(firstStepExpectedMap);
		this.mockMvc.perform(put("/games/1234/pits/1").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(content));
	}

	@Test
	public void testInvalidMove() throws Exception {
		given(this.gameService.move("1234", 1)).willThrow(new IllegalPlayException("blah blah"));
		this.mockMvc.perform(put("/games/1234/pits/1").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

}
