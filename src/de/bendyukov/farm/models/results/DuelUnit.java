package de.bendyukov.farm.models.results;

import de.bendyukov.farm.models.Cell;

/**
 * Represents a participating unit in a duel.
 *
 * @param isKing whether the unit is a king
 * @param name the unit's name
 * @param atk the unit's attack
 * @param def the unit's defense
 * @param cell the cell where the unit was located
 * @param isFlipped whether the unit was flipped during the duel
 * @param isEliminated whether the unit was eliminated
 * @param hasAccess whether the active player is allowed to know its stats
 * 
 * @author Arseniy Bendyukov
 * @version 1.0
 */
public record DuelUnit(
        boolean isKing,
        String name,
        int atk,
        int def,
        Cell cell,
        boolean isFlipped,
        boolean isEliminated,
        boolean hasAccess
) {
}
