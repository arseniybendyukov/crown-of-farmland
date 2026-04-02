package edu.kit.kastel.models.enemy;

import edu.kit.kastel.models.Unit;

/**
 * Functional interface for checking if a unit satisfies a certain condition.
 *
 * @author udkcf
 * @version 1.0
 */
@FunctionalInterface
interface UnitCondition {
    /**
     * Checks if the unit satisfies the condition.
     *
     * @param unit the unit
     * @return true if satisfies, false otherwise
     */
    boolean test(Unit unit);
}
