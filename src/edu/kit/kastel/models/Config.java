package edu.kit.kastel.models;

import java.util.List;
import java.util.Random;

/**
 * Configuration for the game setup holding general settings.
 *
 * @author udkcf
 * @version 1.0
 */
public class Config {
    private final Verbosity verbosity;
    private final Random rng;
    private final List<Character> boardSymbols;

    /**
     * Constructs a new config instance.
     *
     * @param verbosity the verbosity level for the game output
     * @param seed the random seed
     * @param boardSymbols the board symbols, or null as default
     */
    public Config(Verbosity verbosity, int seed, List<Character> boardSymbols) {
        this.verbosity = verbosity;
        this.rng = new Random(seed);

        if (boardSymbols != null) {
            this.boardSymbols = List.copyOf(boardSymbols);
        } else {
            this.boardSymbols = null;
        }
    }

    /**
     * Gets the verbosity level.
     *
     * @return the verbosity level
     */
    public Verbosity getVerbosity() {
        return verbosity;
    }

    /**
     * Gets the random number generator.
     *
     * @return the random number generator
     */
    public Random getRng() {
        return rng;
    }

    /**
     * Gets the list of board symbols, or null if no symbols were provided.
     *
     * @return a list of characters representing board symbols
     */
    public List<Character> getBoardSymbols() {
        if (boardSymbols == null) {
            return null;
        }
        return List.copyOf(boardSymbols);
    }
}
