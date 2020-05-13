package net.devk.kalah.game;

import java.net.URI;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import net.devk.kalah.game.dto.Game;
import net.devk.kalah.game.dto.LastGameStatus;

@RestController
@RequestMapping("/games")
public class GameController {

	private GameService gameService;

	public GameController(GameService gameService) {
		this.gameService = gameService;
	}

	/**
	 * create a {@link Game} and returns its
	 * 
	 * @return generated {@link Game}
	 */
	@PostMapping
	public ResponseEntity<Game> createGame() {
		String gameId = gameService.createGame();
		URI uri = createURI(gameId);
		return ResponseEntity.status(HttpStatus.CREATED).body(new Game(gameId, uri.toString()));
	}

	/**
	 * move a pit with the given pitId for a game with the given gameId
	 * 
	 * @param gameId
	 * @param pitId
	 * @return {@link LastGameStatus} that contains status of the board
	 */
	@PutMapping(value = "/{gameId}/pits/{pitId}")
	public ResponseEntity<LastGameStatus> move(@PathVariable String gameId, @PathVariable int pitId) {
		Map<String, String> status = gameService.move(gameId, pitId);
		URI uri = createURI(gameId);
		return ResponseEntity.status(HttpStatus.OK).body(new LastGameStatus(gameId, uri.toString(), status));
	}

	/**
	 * @param gameId
	 * @return location of a game with the given game id
	 */
	private URI createURI(String gameId) {
		ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentContextPath();
		builder.path("/games");
		builder.pathSegment(gameId);
		return builder.build().toUri();
	}

}
