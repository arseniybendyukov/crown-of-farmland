package edu.kit.kastel.models.results;

/**
 * Represents the outcome of an attempt to merge two units of the same team.
 *
 * @param isSuccess true if the merge is successful
 * @param firstUnit the name of the first unit
 * @param secondUnit the name of the second unit
 * 
 * @author udkcf
 * @version 1.0
 */
public record MergeResult(boolean isSuccess, String firstUnit, String secondUnit) {
}
