package de.bendyukov.farm.models;

import java.util.Optional;

/**
 * Represents the verbosity level of the game output.
 *
 * @author Arseniy Bendyukov
 * @version 1.0
 */
public enum Verbosity {
    /** Render all board rows. */
    ALL("all"),
    /** Skip connector rows while rendering the board. */
    COMPACT("compact");

    private final String identifier;

    Verbosity(String identifier) {
        this.identifier = identifier;
    }

    /**
     * Parses a string into a Verbosity enum.
     *
     * @param verbosityStringified the potential string identifier
     * @return an Optional containing the parsed Verbosity, or empty if not found
     */
    public static Optional<Verbosity> fromString(String verbosityStringified) {
        for (Verbosity verbosity : values()) {
            if (verbosity.toString().equals(verbosityStringified)) {
                return Optional.of(verbosity);
            }
        }
        return Optional.empty();
    }

    /**
     * Returns the string identifier of the verbosity.
     *
     * @return the string identifier
     */
    @Override
    public String toString() {
        return this.identifier;
    }
}
