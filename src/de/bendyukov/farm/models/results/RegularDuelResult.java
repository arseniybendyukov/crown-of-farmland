package de.bendyukov.farm.models.results;

import de.bendyukov.farm.models.Cell;

/**
 * Represents the outcome of a standard duel between units.
 *
 * @param movedToCell the cell the attacker ended up on, or null
 * @param isAttackerEliminated true if the attacker was destroyed
 * @param isDefenderEliminated true if the defender was destroyed
 * @param teamDamage damage dealt to the teams involved, if any
 * 
 * @author Arseniy Bendyukov
 * @version 1.0
 */
public record RegularDuelResult(
        Cell movedToCell,
        boolean isAttackerEliminated,
        boolean isDefenderEliminated,
        TeamDamage teamDamage
) {
}
