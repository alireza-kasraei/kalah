package net.devk.kalah.game.dto;

import java.util.Map;

public class GameStatus extends Game {

	private final Map<String, String> status;

	public GameStatus(String gameId, String url, Map<String, String> status) {
		super(gameId, url);
		this.status = status;
	}

	public Map<String, String> getStatus() {
		return status;
	}

}
