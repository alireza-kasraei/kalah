package net.devk.kalah;

public class IllegalTurnException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public IllegalTurnException() {
		super("its not your turn!");
	}

}
