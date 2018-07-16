package net.devk.kalah.game;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import net.devk.kalah.Gems;
import net.devk.kalah.KalahGame;

@Service
class GameServiceImpl implements GameService {

	// repository of games
	private Map<String, KalahGame> games = new HashMap<>();
	// id generator for games
	private AtomicInteger idGenerator = new AtomicInteger();

	@Override
	public String createGame() {
		// generating an id for game
		final String gameId = String.valueOf(idGenerator.incrementAndGet());
		// we want Six Gems as document says
		KalahGame kalahGame = new KalahGame(Gems.SIX_GEMS);
		// put it in repository and return the game id
		games.put(gameId, kalahGame);
		return gameId;
	}

	@Override
	public Map<String, String> move(String gameId, int pitId) {
		// finds game by its id from the repository
		KalahGame kalahGame = games.get(gameId);
		// generate an exception in case of there is no game with the given id
		if (kalahGame == null)
			throw new GameNotFoundException(String.format("game not found with id %s", gameId));
		// pitIndex starts from zero !
		kalahGame.play(pitId - 1);
		// status of pits after a move action
		final int[] pitsStatus = kalahGame.getPitsStatus();
		// convert to a map and returns it
		Map<String, String> status = new HashMap<>();
		for (int i = 0; i < pitsStatus.length; i++) {
			status.put(String.valueOf(i + 1), String.valueOf(pitsStatus[i]));
		}
		return Collections.unmodifiableMap(status);
	}

}
