package edu.kit.kastel.models;

import edu.kit.kastel.exceptions.GameException;
import edu.kit.kastel.models.results.MoveResult;

/**
 * Service responsible for blocking a unit.
 *
 * @author udkcf
 * @version 1.0
 */
class BlockService {
    private static final String ERROR_IMPOSSIBLE_KING_BLOCK = "The king cannot block";

    private final Game game;

    /**
     * Constructs a new block service.
     *
     * @param game the game instance
     */
    BlockService(Game game) {
        this.game = game;
    }

    /**
     * Sets the currently selected unit to a blocking state.
     *
     * @return a MoveResult containing the outcome of the blockade
     * @throws GameException if the selected unit is a king or the action is forbidden
     */
    MoveResult blockSelectedUnit() throws GameException {
        game.validateSelectedUnit();

        Unit selectedUnit = game.getBoard().getSelectedUnit();

        if (selectedUnit.isKing()) {
            throw new GameException(ERROR_IMPOSSIBLE_KING_BLOCK);
        }

        selectedUnit.block();

        selectedUnit.setLastMoveTurn(game.getCurrentTurn());

        return MoveResult.block(selectedUnit.getName());
    }
}
