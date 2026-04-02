package de.bendyukov.farm.exceptions;

/**
 * An abstract base exception that adds an error prefix to all messages.
 *
 * @author Arseniy Bendyukov
 * @version 1.0
 */
class PrefixedException extends Exception {
    private static final String ERROR_PREFIX = "ERROR: ";

    /**
     * Constructs the exception by adding an error prefix to the message.
     *
     * @param message the error message
     */
    protected PrefixedException(String message) {
        super(ERROR_PREFIX + message);
    }
}
