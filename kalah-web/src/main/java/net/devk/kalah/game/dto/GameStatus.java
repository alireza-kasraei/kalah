package net.devk.kalah.game.dto;

import java.util.Map;

public class GameStatus extends Game {

	//this have to be unmodifiable too
	private final Map<String, String> status;

	public GameStatus(String id, String uri, Map<String, String> status) {
		super(id, uri);
		this.status = status;
	}

	public Map<String, String> getStatus() {
		return status;
	}

}
