package edu.kit.kastel.models.enemy;

/**
 * Represents the eight directions, plus en place, on the board.
 *
 * @author udkcf
 * @version 1.0
 */
enum Direction {
    /** Up direction. */
    TOP(-1, 0),
    /** Up-right direction. */
    TOP_RIGHT(-1, 1),
    /** Right direction. */
    RIGHT(0, 1),
    /** Down-right direction. */
    BOTTOM_RIGHT(1, 1),
    /** Down direction. */
    BOTTOM(1, 0),
    /** Down-left direction. */
    BOTTOM_LEFT(1, -1),
    /** Left direction. */
    LEFT(0, -1),
    /** Up-left direction. */
    TOP_LEFT(-1, -1),
    /** En place. */
    EN_PLACE(0, 0);

    private final int rowStep;
    private final int columnStep;

    Direction(int rowStep, int columnStep) {
        this.rowStep = rowStep;
        this.columnStep = columnStep;
    }
    
    /**
     * Gets the row step.
     * 
     * @return the row step
     */
    public int getRowStep() {
        return rowStep;
    }

    /**
     * Gets the column step.
     * 
     * @return the column step
     */
    public int getColumnStep() {
        return columnStep;
    }
}
