package edu.kit.kastel.models.results;

/**
 * Represents the result of a compatibility check for merging two units.
 *
 * @param isCompatible true if the units are compatible and can merge
 * @param atk the new attack if compatible
 * @param def the new defense if compatible
 * 
 * @author udkcf
 * @version 1.0
 */
public record Compatibility(boolean isCompatible, int atk, int def) {
    private static final int NO_VALUE = 0;

    /**
     * Creates a successful compatibility result.
     *
     * @param atk the new attack
     * @param def the new defense
     * @return a compatible Compatibility instance
     */
    public static Compatibility compatible(int atk, int def) {
        return new Compatibility(true, atk, def);
    }

    /**
     * Creates a failed compatibility result.
     *
     * @return an incompatible Compatibility instance
     */
    public static Compatibility incompatible() {
        return new Compatibility(false, NO_VALUE, NO_VALUE);
    }
}
