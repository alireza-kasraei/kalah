package net.devk.kalah.game;

/**
 * Game Types
 */
public enum Gems {
	THREE_GEMS(3), FOUR_GEMS(4), SIX_GEMS(6);

	private final int count;

	private Gems(int count) {
		this.count = count;
	}

	public int getCount() {
		return count;
	}

}