package de.bendyukov.farm.models.enemy;

/**
 * Functional interface for getting a weight from a WeightedObject.
 *
 * @author Arseniy Bendyukov
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
