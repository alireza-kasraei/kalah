package net.devk.kalah.game.dto;

import java.util.Collections;
import java.util.Map;

public class LastGameStatus extends Game {

	// this have to be unmodifiable too
	private final Map<String, String> status;

	public LastGameStatus(String id, String uri, Map<String, String> status) {
		super(id, uri);
		this.status = Collections.unmodifiableMap(status);
	}

	public Map<String, String> getStatus() {
		return status;
	}

}
