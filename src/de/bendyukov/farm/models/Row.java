package de.bendyukov.farm.models;

import java.util.Optional;

/**
 * Represents the rows on the game board.
 *
 * @author Arseniy Bendyukov
 * @author Programmieren-Team
 * @version 1.0
 */
public enum Row {
    /** Row 1. */
    ONE("1", 6),
    /** Row 2. */
    TWO("2", 5),
    /** Row 3. */
    THREE("3", 4),
    /** Row 4. */
    FOUR("4", 3),
    /** Row 5. */
    FIVE("5", 2),
    /** Row 6. */
    SIX("6", 1),
    /** Row 7. */
    SEVEN("7", 0);

    private final String identifier;
    private final int index;

    Row(String row, int index) {
        this.identifier = row;
        this.index = index;
    }

    /**
     * Parses a string into a Row enum.
     *
     * @param rowStringified the potential string identifier
     * @return an Optional containing the parsed Row, or empty if not found
     */
    public static Optional<Row> fromString(String rowStringified) {
        for (Row row : values()) {
            if (row.toString().equals(rowStringified)) {
                return Optional.of(row);
            }
        }
        return Optional.empty();
    }

    /**
     * Parses an integer index into a Row enum.
     *
     * @param index the zero-based index of the row
     * @return an Optional containing the parsed Row, or empty if not found
     */
    public static Optional<Row> fromIndex(int index) {
        for (Row row : values()) {
            if (row.index == index) {
                return Optional.of(row);
            }
        }
        return Optional.empty();
    }

    /**
     * Gets the index of the row.
     *
     * @return the zero-based index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Returns the string identifier of the row.
     *
     * @return the string identifier
     */
    @Override
    public String toString() {
        return this.identifier;
    }
}
