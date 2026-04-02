package de.bendyukov.farm.cli;

import java.util.Optional;

/**
 * The expected command-line argument keys.
 *
 * @author Arseniy Bendyukov
 * @version 1.0
 */
enum CommandLineArguments {
    /** The seed for RNG. */
    SEED("seed"),
    /** Custom board symbols file. */
    BOARD_SYMBOLS("board"),
    /** The unit stats file. */
    UNITS("units"),
    /** The shared deck counts file. */
    DECK("deck"),
    /** The player 1 deck counts file. */
    DECK1("deck1"),
    /** The player 2 deck counts file. */
    DECK2("deck2"),
    /** Player 1's team name. */
    PLAYER_NAME("team1"),
    /** Player 2's team name. */
    ENEMY_NAME("team2"),
    /** Verbosity configuration. */
    VERBOSITY("verbosity");

    private final String identifier;

    CommandLineArguments(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return this.identifier;
    }

    /**
     * Parses a string key into a CommandLineArguments enum.
     *
     * @param argumentStringified the string key
     * @return the corresponding enum value, or empty if no match is found
     */ 
    public static Optional<CommandLineArguments> fromString(String argumentStringified) {
        for (CommandLineArguments argument : values()) {
            if (argument.toString().equals(argumentStringified)) {
                return Optional.of(argument);
            }
        }
        return Optional.empty();
    }
}

