package edu.kit.kastel.models.results;

import edu.kit.kastel.models.Cell;

/**
 * Represents the outcome of a duel between two units.
 *
 * @param movedToCell the cell the attacker ended up on, if any
 * @param attacker the attacking unit
 * @param defender the defending unit
 * @param teamDamage the damage dealt to either team, if any
 * 
 * @author udkcf
 * @version 1.0
 */
public record DuelResult(Cell movedToCell, DuelUnit attacker, DuelUnit defender, TeamDamage teamDamage) {
}
