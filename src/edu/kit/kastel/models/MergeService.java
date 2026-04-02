package edu.kit.kastel.models;

import edu.kit.kastel.models.results.Compatibility;
import edu.kit.kastel.models.results.MergeResult;

/**
 * Service responsible for merging two units.
 *
 * @author udkcf
 * @version 1.0
 */
class MergeService {
    private final Game game;

    /**
     * Constructs a new merge service.
     *
     * @param game the game instance
     */
    MergeService(Game game) {
        this.game = game;
    }

    /**
     * Merges two units on a cell.
     *
     * @param firstUnit the first unit
     * @param secondUnit the second unit
     * @param cell the cell where the merge takes place
     * @return a MergeResult containing the outcome of the merge
     */
    MergeResult merge(Unit firstUnit, Unit secondUnit, Cell cell) {
        Compatibility compatibility = firstUnit.getCompatibility(secondUnit);

        if (compatibility.isCompatible()) {
            game.getBoard().place(
                    cell,
                    firstUnit.merge(secondUnit, compatibility.atk(), compatibility.def(), game.getNextPlacementOrder())
            );
            return new MergeResult(true, firstUnit.getName(), secondUnit.getName());
        } else {
            game.getBoard().place(cell, firstUnit);
            firstUnit.setPlacementOrder(game.getNextPlacementOrder());
            return new MergeResult(false, firstUnit.getName(), secondUnit.getName());
        }
    }
}
