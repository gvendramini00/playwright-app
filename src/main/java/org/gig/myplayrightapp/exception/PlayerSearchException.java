package org.gig.myplayrightapp.exception;

/**
 * Thrown when a player search in the Back Office returns an error modal,
 * indicating a back-end failure (e.g. DB unreachable, ProxySQL error).
 */
public class PlayerSearchException extends RuntimeException {

    public PlayerSearchException(String message) {
        super(message);
    }

    public PlayerSearchException(String message, Throwable cause) {
        super(message, cause);
    }
}
