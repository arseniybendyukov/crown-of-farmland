package de.bendyukov.farm.models;

import java.util.Objects;

/**
 * Represents a coordinate position on the game board.
 *
 * @author Arseniy Bendyukov
 * @author Programmieren-Team
 * @version 1.0
 */
public class Cell {
    private static final int MAX_NEIGHBOR_DISTANCE = 1;
    private static final int SAME_POSITION = 0;
    private static final String INVALID_ROW_INDEX = "Invalid row index: ";
    private static final String INVALID_COLUMN_INDEX = "Invalid column index: ";

    private final Column column;
    private final Row row;

    /**
     * Constructs a cell from a specific column and row.
     *
     * @param column the column of the cell
     * @param row the row of the cell
     */
    public Cell(Column column, Row row) {
        this.column = column;
        this.row = row;
    }

    /**
     * Constructs a cell from zero-based indices.
     *
     * @param row the vertical index
     * @param column the horizontal index
     */
    public Cell(int row, int column) {
        this.row = Row.fromIndex(row).orElseThrow(() -> new IllegalArgumentException(INVALID_ROW_INDEX + row));
        this.column = Column.fromIndex(column).orElseThrow(() -> new IllegalArgumentException(INVALID_COLUMN_INDEX + column));
    }

    /**
     * Gets the row index of this cell.
     *
     * @return the row index
     */
    public int getI() {
        return row.getIndex();
    }

    /**
     * Gets the column index of this cell.
     *
     * @return the column index
     */
    public int getJ() {
        return column.getIndex();
    }

    /**
     * Checks if this cell is surrounding another cell.
     *
     * @param otherCell the cell to check against
     * @return true if the cell is surrounding the other cell, false otherwise
     */
    public boolean isSurrounding(Cell otherCell) {
        int rowDifference = Math.abs(getI() - otherCell.getI());
        int columnDifference = Math.abs(getJ() - otherCell.getJ());

        return rowDifference <= MAX_NEIGHBOR_DISTANCE
                && columnDifference <= MAX_NEIGHBOR_DISTANCE
                && !(rowDifference == SAME_POSITION && columnDifference == SAME_POSITION);
    }

    /**
     * Checks if this cell is a cross neighbor to another cell.
     *
     * @param otherCell the cell to check against
     * @return true if the cell is a cross neighbor to the other cell, false otherwise
     */
    public boolean isCrossNeighbor(Cell otherCell) {
        return distance(otherCell) <= MAX_NEIGHBOR_DISTANCE;
    }

    /**
     * Calculates the Manhattan Distance to a cell.
     *
     * @param otherCell a cell to calculate the distance to
     * @return the distance to the cell
     */
    public int distance(Cell otherCell) {
        int rowDifference = Math.abs(getI() - otherCell.getI());
        int columnDifference = Math.abs(getJ() - otherCell.getJ());

        return rowDifference + columnDifference;
    }

    /**
     * Checks whether the cell is equal to another cell.
     *
     * @param obj the cell to compare to
     * @return true if the cell is equal to the other cell, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        if (this.getClass() != obj.getClass()) {
            return false;
        }

        Cell cell = (Cell) obj;
        return (this.column == cell.column) && (this.row == cell.row);
    }

    /**
     * Returns a hash code of the cell.
     *
     * @return a hash code of the cell
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.column, this.row);
    }

    /**
     * Returns the stringified cell.
     *
     * @return the stringified cell
     */
    @Override
    public String toString() {
        return this.column.toString() + this.row.toString();
    }
}
