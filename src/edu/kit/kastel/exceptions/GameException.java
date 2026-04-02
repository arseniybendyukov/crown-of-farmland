package edu.kit.kastel.exceptions;

/**
 * Thrown when an error occurs during game execution.
 *
 * @author udkcf
 * @version 1.0
 */
public class GameException extends Exception {
    /**
     * Constructs the exception.
     *
     * @param message the error message
     */
    public GameException(String message) {
        super(message);
    }
}
