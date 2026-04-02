package de.bendyukov.farm.exceptions;

/**
 * Thrown when the player provides invalid arguments to a command.
 *
 * @author Arseniy Bendyukov
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
