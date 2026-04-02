package de.bendyukov.farm.models.enemy;

import de.bendyukov.farm.exceptions.GameException;
import de.bendyukov.farm.models.results.MoveResult;

/**
 * Functional interface representing an executable movement or blockade by the enemy bot.
 *
 * @author Arseniy Bendyukov
 * @version 1.0
 */
@FunctionalInterface
interface EnemyMoveAction {
    /**
     * Executes the movement or blockade.
     *
     * @return the move result
     * @throws GameException if an error occurs during execution
     */
    MoveResult execute() throws GameException;
}
