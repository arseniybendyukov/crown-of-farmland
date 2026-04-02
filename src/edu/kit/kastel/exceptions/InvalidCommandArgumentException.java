package edu.kit.kastel.exceptions;

/**
 * Thrown when the player provides invalid arguments to a command.
 *
 * @author udkcf
 * @version 1.0
 */
public class InvalidCommandArgumentException extends PrefixedException {
    /**
     * Constructs the exception.
     *
     * @param message the error message
     */
    public InvalidCommandArgumentException(String message) {
        super(message);
    }
}
