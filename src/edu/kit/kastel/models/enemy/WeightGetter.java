package edu.kit.kastel.models.enemy;

/**
 * Functional interface for getting a weight from a WeightedObject.
 *
 * @author udkcf
 * @version 1.0
 */
@FunctionalInterface
interface WeightGetter {
    /**
     * Gets the weight of the given weighted object.
     *
     * @param weightedObject the weighted object
     * @return the weight
     */
    int getWeight(WeightedObject<?> weightedObject);
}
