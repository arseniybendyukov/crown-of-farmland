package de.bendyukov.farm.models.results;

import java.util.Optional;

/**
 * Represents the outcome of yielding a turn.
 *
 * @param teamName the team yielding
 * @param lost whether the team lost
 * @param opponentName the opponent team's name
 * @param discardedUnit the optionally discarded unit
 * 
 * @author Arseniy Bendyukov
 * @version 1.0
 */
public record YieldResult(String teamName, boolean lost, String opponentName, Optional<DiscardedUnit> discardedUnit) {
}
