package edu.kit.kastel.models.enemy;

import edu.kit.kastel.exceptions.GameException;
import edu.kit.kastel.models.results.MoveResult;

/**
 * Functional interface representing an executable movement or blockade by the enemy bot.
 *
 * @author udkcf
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
