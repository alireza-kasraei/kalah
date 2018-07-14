package net.devk.kalah.game;

import java.util.Map;

public interface GameService {

	public String createGame();

	public Map<String, String> move(String gameId, int pitId);

}
