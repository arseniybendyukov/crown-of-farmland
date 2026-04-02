package edu.kit.kastel.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Represents a deck of units.
 *
 * @author udkcf
 * @version 1.0
 */
public class Deck {
    /** The initial size of the deck. */
    public static final int INITIAL_SIZE = 40;

    private final List<Unit> units;

    /**
     * Constructs a deck with a list of units.
     *
     * @param units the list of units
     */
    public Deck(List<Unit> units) {
        this.units = new ArrayList<>(units);
    }

    /**
     * Gets the current number of units in the deck.
     *
     * @return the size of the deck
     */
    public int size() {
        return units.size();
    }

    /**
     * Checks if the deck contains no units.
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return units.isEmpty();
    }

    /**
     * Removes and returns the first unit from the deck.
     *
     * @return the unit that was removed
     */
    public Unit getAndRemoveFirst() {
        return units.removeFirst();
    }

    /**
     * Shuffles the deck using the provided random number generator.
     *
     * @param rng the random number generator
     */
    public void shuffle(Random rng) {
        Collections.shuffle(units, rng);
    }
}
