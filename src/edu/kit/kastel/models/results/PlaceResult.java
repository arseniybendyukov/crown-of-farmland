package edu.kit.kastel.models.results;

/**
 * Represents the outcome of placing a unit onto the board.
 *
 * @param teamName the team placing the unit
 * @param unitName the unit placed
 * @param isEliminated true if it was eliminated
 * @param mergeResult the outcome of merging if placed onto a fellow unit
 * 
 * @author udkcf
 * @version 1.0
 */
public record PlaceResult(String teamName, String unitName, boolean isEliminated, MergeResult mergeResult) {
    /**
     * Creates a PlaceResult for a standard placement on an empty cell.
     *
     * @param teamName the placing team
     * @param unitName the placed unit
     * @return the PlaceResult
     */
    public static PlaceResult standard(String teamName, String unitName) {
        return new PlaceResult(teamName, unitName, false, null);
    }

    /**
     * Creates a PlaceResult representing a successful merge upon placement.
     *
     * @param teamName the placing team
     * @param unitName the placed unit
     * @param mergeUnitName the unit it merged into
     * @return the PlaceResult
     */
    public static PlaceResult successfulMerge(String teamName, String unitName, String mergeUnitName) {
        return new PlaceResult(teamName, unitName, false, new MergeResult(true, unitName, mergeUnitName));
    }

    /**
     * Creates a PlaceResult representing a failed merge upon placement.
     *
     * @param teamName the placing team
     * @param unitName the placed unit
     * @param mergeUnitName the unit it merged into
     * @return the PlaceResult
     */
    public static PlaceResult failedMerge(String teamName, String unitName, String mergeUnitName) {
        return new PlaceResult(teamName, unitName, false, new MergeResult(false, unitName, mergeUnitName));
    }

    /**
     * Creates a PlaceResult representing the placed unit was immediately eliminated.
     *
     * @param teamName the placing team
     * @param unitName the placed unit
     * @return the PlaceResult
     */
    public static PlaceResult eliminated(String teamName, String unitName) {
        return new PlaceResult(teamName, unitName, true, null);
    }
}
