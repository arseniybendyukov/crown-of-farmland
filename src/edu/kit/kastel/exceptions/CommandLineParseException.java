package edu.kit.kastel.exceptions;

/**
 * Thrown when the initial command line arguments provided to the application are invalid.
 *
 * @author udkcf
 * @version 1.0
 */
public class CommandLineParseException extends PrefixedException {
    /**
     * Constructs the exception.
     *
     * @param message the error message
     */
    public CommandLineParseException(String message) {
        super(message);
    }
}
