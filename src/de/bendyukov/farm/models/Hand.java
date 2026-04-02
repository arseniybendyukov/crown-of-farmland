package de.bendyukov.farm.models;

import de.bendyukov.farm.exceptions.GameException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents the current hand of units.
 *
 * @author Arseniy Bendyukov
 * @version 1.0
 */
public class Hand {
    /** The initial size of the hand. */
    public static final int INITIAL_SIZE = 5;
    private static final String ERROR_INDEX_OUT_OF_BOUNDS = "Index out of bounds of the hand";
    private static final int MIN_INDEX = 0;
    private static final int INDEX_OFFSET = 1;

    private final List<Unit> units = new ArrayList<>();

    /**
     * Adds a unit to the hand.
     *
     * @param unit the unit to add
     */
    public void addUnit(Unit unit) {
        units.add(unit);
    }

    /**
     * Gets the units in the hand.
     *
     * @return the units in the hand
     */
    public List<Unit> getUnits() {
        return List.copyOf(units);
    }

    /**
     * Checks if the hand has reached its maximum size.
     *
     * @return true if the hand is full, false otherwise
     */
    public boolean isFull() {
        return units.size() == INITIAL_SIZE;
    }

    /**
     * Retrieves and removes a unit at a specific index from the hand.
     *
     * @param index the zero-based index of the unit
     * @return the unit that was removed
     * @throws GameException if the index is out of bounds
     */
    public Unit getAndDiscardUnit(int index) throws GameException {
        if (index < MIN_INDEX || index >= units.size()) {
            throw new GameException(ERROR_INDEX_OUT_OF_BOUNDS);
        }
        Unit unit = units.get(index);
        units.remove(index);
        return unit;
    }

    /**
     * Retrieves and removes multiple units at specific indices from the hand.
     *
     * @param indices an array of zero-based indices of the units
     * @return a list of units that were removed
     * @throws GameException if any of the indices are out of bounds
     */
    public List<Unit> getAndDiscardUnits(int[] indices) throws GameException {
        List<Unit> selectedUnits = new ArrayList<>();

        for (int index : indices) {
            if (index < MIN_INDEX || index >= units.size()) {
                throw new GameException(ERROR_INDEX_OUT_OF_BOUNDS);
            }
            selectedUnits.add(units.get(index));
        }

        int[] sortedIndices = Arrays.copyOf(indices, indices.length);
        Arrays.sort(sortedIndices);
        for (int i = sortedIndices.length - INDEX_OFFSET; i >= MIN_INDEX; i--) {
            units.remove(sortedIndices[i]);
        }
        return selectedUnits;
    }
}
