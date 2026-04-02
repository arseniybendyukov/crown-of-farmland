package de.bendyukov.farm.models.results;

/**
 * Represents the outcome of a unit movement or blocking.
 *
 * @param isBlockMove true if the action was a blockade
 * @param noLongerBlocks true if moving removed the blocking state
 * @param unitName the name of the moved unit
 * @param mergeResult the result of a merge, if any
 * @param duelResult the result of a duel, if any
 * 
 * @author Arseniy Bendyukov
 * @version 1.0
 */
public record MoveResult(boolean isBlockMove, boolean noLongerBlocks, String unitName, MergeResult mergeResult, DuelResult duelResult) {
    /**
     * Creates a MoveResult for a blockade.
     *
     * @param unitName the name of the blocked unit
     * @return a MoveResult for a blockade
     */
    public static MoveResult block(String unitName) {
        return new MoveResult(true, false, unitName, null, null);
    }

    /**
     * Creates a MoveResult for a regular move.
     *
     * @param noLongerBlocks true if moving removed the blocking state
     * @param unitName the name of the moved unit
     * @param mergeResult the result of a merge, if any
     * @param duelResult the result of a duel, if any
     * @return a MoveResult for a regular move
     */
    public static MoveResult move(boolean noLongerBlocks, String unitName, MergeResult mergeResult, DuelResult duelResult) {
        return new MoveResult(false, noLongerBlocks, unitName, mergeResult, duelResult);
    }
}
