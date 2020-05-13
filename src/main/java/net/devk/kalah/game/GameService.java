package net.devk.kalah.game;

import java.util.Map;

/**
 * service interface for game
 */
public interface GameService {

	/**
	 * creates a {@link KalahGame} and stores it in a repository
	 * 
	 * @return id of a created game
	 */
	public String createGame();

	/**
	 * make a move on a game with a given game id
	 * 
	 * @param gameId id of a game
	 * @param pitId  id of a pit
	 * @return status of pits as a map
	 */
	public Map<String, String> move(String gameId, int pitId);

}
