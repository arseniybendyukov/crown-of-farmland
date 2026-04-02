package edu.kit.kastel.models.enemy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.random.RandomGenerator;

/**
 * Wrapper class assigning a weight to an object for random selection.
 *
 * @param <T> the type of the contained object
 *
 * @author udkcf
 * @version 1.0
 */
final class WeightedObject<T> {
    private static final int INTERVAL_OFFSET = 1;
    private static final int INITIAL_SUM = 0;
    private static final int EMPTY_INTERVAL = 0;
    private static final int MIN_WEIGHT = 0;
    private static final int ONE_BEST = 1;
    private static final int EQUAL_WEIGHT = 1;

    private final int weight;
    private final T object;

    /**
     * Constructs a WeightedObject with a given weight and object.
     *
     * @param weight the weight
     * @param object the object
     */
    WeightedObject(int weight, T object) {
        this.weight = weight;
        this.object = object;
    }

    private static int getRandomInInterval(int interval, RandomGenerator rng) {
        return rng.nextInt(INTERVAL_OFFSET, interval + INTERVAL_OFFSET);
    }

    /**
     * Randomly picks an object based on the given weights.
     *
     * @param rng the random generator
     * @param weightedObjects weighted objects
     * @param <T> the type of the objects
     * @return an Optional containing the picked object, or empty if no object can be selected
     */
    public static <T> Optional<T> randomPick(RandomGenerator rng, List<WeightedObject<T>> weightedObjects) {
        if (weightedObjects.isEmpty()) {
            return Optional.empty();
        }

        return pick(rng, weightedObjects, WeightedObject::getWeight);
    }

    /**
     * Randomly picks an object with probability inverse to its weight.
     *
     * @param rng the random generator
     * @param weightedObjects weighted objects
     * @param <T> the type of the objects
     * @return an Optional containing the picked object, or empty if no object can be selected
     */
    public static <T> Optional<T> inverseRandomPick(RandomGenerator rng, List<WeightedObject<T>> weightedObjects) {
        if (weightedObjects.isEmpty()) {
            return Optional.empty();
        }

        int maxWeight = getMaxWeight(weightedObjects);

        return pick(rng, weightedObjects, weightedObject -> maxWeight - weightedObject.getWeight());
    }

    private static <T> Optional<T> pick(RandomGenerator rng, List<WeightedObject<T>> weightedObjects, WeightGetter weightGetter) {
        int interval = INITIAL_SUM;
        for (WeightedObject<T> weightedObject : weightedObjects) {
            interval += Math.max(MIN_WEIGHT, weightGetter.getWeight(weightedObject));
        }

        if (interval == EMPTY_INTERVAL) {
            return Optional.empty();
        }

        int pick = getRandomInInterval(interval, rng);

        int sum = INITIAL_SUM;
        for (WeightedObject<T> weightedObject : weightedObjects) {
            sum += Math.max(MIN_WEIGHT, weightGetter.getWeight(weightedObject));
            if (pick <= sum) {
                return Optional.of(weightedObject.getObject());
            }
        }

        return Optional.empty();
    }

    /**
     * Retrieves all objects sharing the maximum computed weight.
     *
     * @param weightedObjects the list of weighted objects
     * @param <T> the type of the objects
     * @return a list containing only the objects with the highest weight
     */
    public static <T> List<WeightedObject<T>> getMaxWeightObjects(List<WeightedObject<T>> weightedObjects) {
        int maxWeight = getMaxWeight(weightedObjects);
        List<WeightedObject<T>> maxWeightObjects = new ArrayList<>();
        for (WeightedObject<T> weightedObject : weightedObjects) {
            if (weightedObject.getWeight() == maxWeight) {
                maxWeightObjects.add(weightedObject);
            }
        }
        return maxWeightObjects;
    }

    /**
     * Picks an object from a list of best/equal-weight candidates.
     *
     * @param rng the random generator
     * @param bestObjects the equivalently weighted best objects
     * @param <T> the type of the objects
     * @return the picked object
     */
    public static <T> T pickAmongBest(RandomGenerator rng, List<WeightedObject<T>> bestObjects) {
        if (bestObjects.size() == ONE_BEST) {
            return bestObjects.getFirst().getObject();
        }

        List<WeightedObject<T>> equalObjects = new ArrayList<>();
        for (WeightedObject<T> weightedObject : bestObjects) {
            equalObjects.add(new WeightedObject<>(EQUAL_WEIGHT, weightedObject.getObject()));
        }

        return randomPick(rng, equalObjects).orElseThrow();
    }

    private static <T> int getMaxWeight(List<WeightedObject<T>> weightedObjects) {
        int maxWeight = Integer.MIN_VALUE;
        for (WeightedObject<T> weightedObject : weightedObjects) {
            if (weightedObject.getWeight() > maxWeight) {
                maxWeight = weightedObject.getWeight();
            }
        }
        return maxWeight;
    }

    private int getWeight() {
        return weight;
    }

    /**
     * Gets the contained object.
     *
     * @return the object
     */
    public T getObject() {
        return object;
    }
}
