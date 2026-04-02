package edu.kit.kastel.models.results;

/**
 * Represents a unit that was discarded from the hand.
 *
 * @param name the name of the discarded unit
 * @param atk the attack of the discarded unit
 * @param def the defense of the discarded unit
 * 
 * @author udkcf
 * @version 1.0
 */
public record DiscardedUnit(String name, int atk, int def) {
}
