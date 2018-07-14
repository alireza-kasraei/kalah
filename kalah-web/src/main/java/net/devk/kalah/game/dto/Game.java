package net.devk.kalah.game.dto;

public class Game {
	private final String id;
	private final String url;

	public Game(String id, String url) {
		super();
		this.id = id;
		this.url = url;
	}

	public String getId() {
		return id;
	}

	public String getUrl() {
		return url;
	}

}
