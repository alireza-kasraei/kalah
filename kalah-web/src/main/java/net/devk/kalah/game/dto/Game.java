package net.devk.kalah.game.dto;

/**
 * DTO class for Game Response
 */
public class Game {
	private final String id;
	// there is a url not url , for the move operation in document
	private final String uri;

	public Game(String id, String uri) {
		this.id = id;
		this.uri = uri;
	}

	public String getId() {
		return id;
	}

	public String getUri() {
		return uri;
	}

}
