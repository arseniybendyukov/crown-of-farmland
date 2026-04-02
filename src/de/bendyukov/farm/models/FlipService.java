package de.bendyukov.farm.models;

import de.bendyukov.farm.exceptions.GameException;

/**
 * Service responsible for flipping a hidden unit.
 *
 * @author Arseniy Bendyukov
 * @version 1.0
 */
class FlipService {
    private static final String ERROR_UNIT_ALREADY_REVEALED = "The unit is already revealed";

    private final Game game;

    /**
     * Constructs a new flip service.
     *
     * @param game the game instance
     */
    FlipService(Game game) {
        this.game = game;
    }

    /**
     * Flips the currently selected unit.
     *
     * @throws GameException if the unit is already revealed or the action is forbidden
     */
    void flipSelectedUnit() throws GameException {
        game.validateSelectedUnit();

        Unit selectedUnit = game.getBoard().getSelectedUnit();

        if (!selectedUnit.isHidden()) {
            throw new GameException(ERROR_UNIT_ALREADY_REVEALED);
        }
        selectedUnit.flip();
    }
}
