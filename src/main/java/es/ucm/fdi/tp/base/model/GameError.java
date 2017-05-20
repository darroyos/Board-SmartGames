package es.ucm.fdi.tp.base.model;

/**
 * Describe illegal game actions or other game errors.
 */
public class GameError extends RuntimeException {
	private static final long serialVersionUID = 4703354133717328836L;

	/**
	 * A game error
	 * @param msg that describes the error
	 */
	public GameError(String msg) {
		super(msg);
	}

	/**
	 * A game error that wraps an exception
	 * @param msg that describes the error
	 * @param cause of the error
	 */
	public GameError(String msg, Throwable cause) {
		super(msg, cause);
	}
}
