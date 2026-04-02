package de.bendyukov.farm.models.enemy;

import de.bendyukov.farm.models.Unit;

import java.util.List;

/**
 * Represents a set of potential moves for a regular enemy unit.
 *
 * @param unit the unit to act upon
 * @param hasPositiveMovement true if there is a move with score > 0
 * @param blockAction the action of blocking this unit
 * @param weightedActions a collection of moves with their scores
 *
 * @author Arseniy Bendyukov
 * @version 1.0
 */
record EnemyUnitMove(
        Unit unit,
        boolean hasPositiveMovement,
        EnemyMoveAction blockAction,
        List<WeightedObject<EnemyMoveAction>> weightedActions
) {
    /**
     * Constructs an enemy unit move.
     *
     * @param unit the unit to act upon
     * @param hasPositiveMovement true if there is a move with score > 0
     * @param blockAction the action of blocking this unit
     * @param weightedActions a collection of moves with their scores
     */
    public EnemyUnitMove {
        if (weightedActions != null) {
            weightedActions = List.copyOf(weightedActions);
        }
    }
}
