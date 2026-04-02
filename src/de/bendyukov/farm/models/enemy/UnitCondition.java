package de.bendyukov.farm.models.enemy;

import de.bendyukov.farm.models.Unit;

/**
 * Functional interface for checking if a unit satisfies a certain condition.
 *
 * @author Arseniy Bendyukov
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
