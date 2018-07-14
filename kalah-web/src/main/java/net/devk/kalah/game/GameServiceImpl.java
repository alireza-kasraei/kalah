package net.devk.kalah.game;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import net.devk.kalah.Gems;
import net.devk.kalah.KalahGame;
import net.devk.kalah.Player;

@Service
class GameServiceImpl implements GameService {

	private Map<String, KalahGame> games = new HashMap<>();
	private AtomicInteger idGenerator = new AtomicInteger();

	@Override
	public String createGame() {
		final String gameId = String.valueOf(idGenerator.incrementAndGet());
		KalahGame kalahGame = new KalahGame(Gems.SIX_GEMS);
		games.put(gameId, kalahGame);
		return gameId;
	}

	@Override
	public Map<String, String> move(String gameId, int pitId) {
		KalahGame kalahGame = games.get(gameId);
		Player player = (pitId < 7) ? Player.PLAYER_ONE : Player.PLAYER_TWO;
		kalahGame.play(player, pitId);
		int[] fitsStatus = kalahGame.getFitsStatus();
		Map<String, String> status = new HashMap<>();
		for (int i = 0; i < fitsStatus.length; i++) {
			status.put(String.valueOf(i + 1), String.valueOf(fitsStatus[i]));
		}
		return status;
	}

}
