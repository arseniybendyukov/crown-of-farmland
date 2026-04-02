package de.bendyukov.farm.models;

/**
 * Functional interface for checking if a unit at a cell satisfies a certain condition.
 *
 * @author Arseniy Bendyukov
 * @version 1.0
 */
@FunctionalInterface
interface BoardCondition {
    /**
     * Checks if the unit and the cell satisfy the condition.
     *
     * @param unit the unit
     * @param cell the cell
     * @return true if satisfies, false otherwise
     */
    boolean test(Unit unit, Cell cell);
}
