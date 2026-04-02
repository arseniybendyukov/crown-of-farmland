package edu.kit.kastel.models;

import edu.kit.kastel.exceptions.GameException;
import edu.kit.kastel.models.results.DuelResult;
import edu.kit.kastel.models.results.MergeResult;
import edu.kit.kastel.models.results.MoveResult;

/**
 * Service responsible for moving units on the board.
 *
 * @author udkcf
 * @version 1.0
 */
class MoveService {
    private static final String ERROR_FORBIDDEN_CELL_MOVE = "It is forbidden to move a unit on this cell";
    private static final String ERROR_FORBIDDEN_KING_MOVE = "King cannot move on an opponent's unit";
    private static final String ERROR_CANNOT_MOVE_ON_KING = "A unit cannot move on its king";

    private final Game game;

    /**
     * Constructs a new move service.
     *
     * @param game the game instance
     */
    MoveService(Game game) {
        this.game = game;
    }

    /**
     * Moves the currently selected unit to a target cell.
     *
     * @param targetCell the target cell to move to
     * @return a MoveResult containing the outcome of the move
     * @throws GameException if the move is invalid or forbidden
     */
    MoveResult move(Cell targetCell) throws GameException {
        Board board = game.getBoard();
        Team activeTeam = game.getActiveTeam();
        Cell selectCell = board.getSelectedCell();
        Unit selectedUnit = board.getSelectedUnit();

        game.validateSelectedUnit();
        if (!selectCell.isCrossNeighbor(targetCell)) {
            throw new GameException(ERROR_FORBIDDEN_CELL_MOVE);
        }

        boolean noLongerBlocks = false;
        if (selectedUnit.isBlocking()) {
            noLongerBlocks = true;
            selectedUnit.unblock();
        }

        if (selectCell.equals(targetCell)) {
            selectedUnit.setLastMoveTurn(game.getCurrentTurn());
            board.selectCell(targetCell);
            return MoveResult.move(noLongerBlocks, selectedUnit.getName(), null, null);
        }

        if (activeTeam.getKingCell() == targetCell) {
            throw new GameException(ERROR_CANNOT_MOVE_ON_KING);
        }

        Unit targetUnit = board.getUnit(targetCell);
        if (targetUnit != null) {
            if (targetUnit.getTeamId() == game.getActiveTeamId() && !selectedUnit.isKing()) {
                MergeResult mergeResult = new MergeService(game).merge(selectedUnit, targetUnit, targetCell);
                if (mergeResult.isSuccess()) {
                    board.clear(selectCell);
                }
                selectedUnit.setLastMoveTurn(game.getCurrentTurn());
                board.selectCell(targetCell);
                return MoveResult.move(noLongerBlocks, selectedUnit.getName(), mergeResult, null);
            } else if (targetUnit.getTeamId() != game.getActiveTeamId()) {
                if (selectedUnit.isKing()) {
                    throw new GameException(ERROR_FORBIDDEN_KING_MOVE);
                }
                DuelResult duelResult = new DuelService(game).duel(selectedUnit, selectCell, targetUnit, targetCell);
                selectedUnit.setLastMoveTurn(game.getCurrentTurn());
                if (duelResult.movedToCell() != null) {
                    board.selectCell(duelResult.movedToCell());
                }
                return MoveResult.move(noLongerBlocks, selectedUnit.getName(), null, duelResult);
            }
        }

        board.place(targetCell, selectedUnit);
        board.clear(selectCell);
        selectedUnit.setLastMoveTurn(game.getCurrentTurn());
        board.selectCell(targetCell);
        if (selectedUnit.isKing()) {
            activeTeam.setKingCell(targetCell);
        }
        return MoveResult.move(noLongerBlocks, selectedUnit.getName(), null, null);
    }
}
