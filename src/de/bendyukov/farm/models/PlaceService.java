package de.bendyukov.farm.models;

import de.bendyukov.farm.exceptions.GameException;
import de.bendyukov.farm.models.results.MergeResult;
import de.bendyukov.farm.models.results.PlaceResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Service responsible for placing units from the hand onto the board.
 *
 * @author Arseniy Bendyukov
 * @version 1.0
 */
class PlaceService {
    private static final String ERROR_INVALID_PLACE = "Cannot place on an opponent's unit";
    private static final String ERROR_ALREADY_PLACED = "Already placed during this turn";
    private static final String ERROR_FORBIDDEN_CELL_PLACE = "It is forbidden to place a unit on this cell";

    private final Game game;

    /**
     * Constructs a new place service.
     *
     * @param game the game instance
     */
    PlaceService(Game game) {
        this.game = game;
    }

    /**
     * Places one or more units from the active team's hand onto the board.
     *
     * @param indices the array of unit indices in the hand
     * @return a list of PlaceResult describing the outcome of each placement
     * @throws GameException if the placement is invalid or forbidden
     */
    List<PlaceResult> place(int[] indices) throws GameException {
        Board board = game.getBoard();
        Team activeTeam = game.getActiveTeam();
        Cell selectedCell = board.getSelectedCell();

        if (!activeTeam.canPlace(game.getCurrentTurn())) {
            throw new GameException(ERROR_ALREADY_PLACED);
        }

        if (!activeTeam.getKingCell().isSurrounding(selectedCell)) {
            throw new GameException(ERROR_FORBIDDEN_CELL_PLACE);
        }

        if (board.getSelectedUnit() != null && board.getSelectedUnit().getTeamId() != game.getActiveTeamId()) {
            throw new GameException(ERROR_INVALID_PLACE);
        }

        List<Unit> units = activeTeam.getHand().getAndDiscardUnits(indices);

        List<PlaceResult> results = new ArrayList<>();

        for (Unit unit : units) {
            if (board.reachedMaxUnits(game.getActiveTeamId())) {
                results.add(PlaceResult.eliminated(activeTeam.getName(), unit.getName()));
                continue;
            }

            Unit selectedUnit = board.getSelectedUnit();

            if (selectedUnit == null) {
                board.place(selectedCell, unit);
                unit.setPlacementOrder(game.getNextPlacementOrder());
                results.add(PlaceResult.standard(activeTeam.getName(), unit.getName()));
            } else {
                MergeResult mergeResult = new MergeService(game).merge(unit, selectedUnit, selectedCell);

                if (mergeResult.isSuccess()) {
                    results.add(PlaceResult.successfulMerge(
                            activeTeam.getName(),
                            mergeResult.firstUnit(),
                            mergeResult.secondUnit()
                    ));
                } else {
                    results.add(PlaceResult.failedMerge(
                            activeTeam.getName(),
                            mergeResult.firstUnit(),
                            mergeResult.secondUnit()
                    ));
                }
            }
        }

        activeTeam.setLastPlaceTurn(game.getCurrentTurn());
        return results;
    }
}
