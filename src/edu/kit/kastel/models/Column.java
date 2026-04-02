package edu.kit.kastel.models;

import java.util.Optional;

/**
 * Represents the columns on the game board.
 *
 * @author udkcf
 * @author Programmieren-Team
 * @version 1.0
 */
public enum Column {
    /** Column A. */
    A("A", 0),
    /** Column B. */
    B("B", 1),
    /** Column C. */
    C("C", 2),
    /** Column D. */
    D("D", 3),
    /** Column E. */
    E("E", 4),
    /** Column F. */
    F("F", 5),
    /** Column G. */
    G("G", 6);

    private final String identifier;
    private final int index;

    Column(String identifier, int index) {
        this.identifier = identifier;
        this.index = index;
    }

    /**
     * Parses a string into a Column enum.
     *
     * @param columnStringified the potential string identifier
     * @return an Optional containing the parsed Column, or empty if not found
     */
    public static Optional<Column> fromString(String columnStringified) {
        for (Column coulmn : values()) {
            if (coulmn.toString().equals(columnStringified)) {
                return Optional.of(coulmn);
            }
        }
        return Optional.empty();
    }

    /**
     * Parses an index into a Column enum.
     *
     * @param index the zero-based index of the column
     * @return an Optional containing the parsed Column, or empty if not found
     */
    public static Optional<Column> fromIndex(int index) {
        for (Column column : values()) {
            if (column.index == index) {
                return Optional.of(column);
            }
        }
        return Optional.empty();
    }

    /**
     * Gets the index of the column.
     *
     * @return the zero-based index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Returns the string identifier of the column.
     *
     * @return the string identifier
     */
    @Override
    public String toString() {
        return this.identifier;
    }
}
