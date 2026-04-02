package de.bendyukov.farm.ui.board;

import de.bendyukov.farm.models.Board;
import de.bendyukov.farm.models.Cell;
import de.bendyukov.farm.models.TeamId;
import de.bendyukov.farm.models.Unit;
import de.bendyukov.farm.models.Verbosity;

import java.util.List;
import java.util.StringJoiner;

/**
 * Utility class for rendering the game board.
 *
 * @author Arseniy Bendyukov
 * @version 1.0
 */
public final class BoardRenderer {
    private static final String PLAYER_UNIT = "x";
    private static final String ENEMY_UNIT = "y";
    private static final String PLAYER_KING = "X";
    private static final String ENEMY_KING = "Y";
    private static final String BOARD_FILLER = " ";
    private static final String BLOCKING_SUFFIX = "b";
    private static final String CAN_MOVE_PREFIX = "*";
    private static final String BOARD_COLUMNS = "    A   B   C   D   E   F   G";
    private static final int CONNECTOR_ROW_COUNT = Board.SIZE + 1;
    private static final String EMPTY_CELL = BOARD_FILLER + BOARD_FILLER + BOARD_FILLER;

    private BoardRenderer() {
    }

    /**
     * Renders the board into a string representation.
     *
     * @param board the board
     * @param currentTurn the current turn number
     * @param activeTeamId the ID of the active team
     * @param boardSymbols the symbols representing the board, or null
     * @param verbosity determines whether connector rows are rendered or skipped
     * @return the string representation of the board
     */
    public static String render(Board board, int currentTurn, TeamId activeTeamId, List<Character> boardSymbols, Verbosity verbosity) {
        Cell selectedCell = board.getSelectedCell();

        StringJoiner joiner = new StringJoiner(System.lineSeparator());

        for (int i = 0; i < CONNECTOR_ROW_COUNT; i++) {
            boolean hasUnitRow = i != Board.SIZE;

            StringBuilder connectorRow = new StringBuilder();
            connectorRow.append(BOARD_FILLER).append(BOARD_FILLER);

            StringBuilder unitRow = null;

            if (hasUnitRow) {
                unitRow = new StringBuilder();
                unitRow.append(Board.SIZE - i).append(BOARD_FILLER);
            }

            for (int j = 0; j < Board.SIZE; j++) {
                BoardConnector corner = new BoardConnector(i, j, BoardConnectorType.CORNER);
                connectorRow.append(corner.render(boardSymbols, selectedCell));

                BoardConnector horizontalEdge = new BoardConnector(i, j, BoardConnectorType.HORIZONTAL_EDGE);
                connectorRow.append(horizontalEdge.render(boardSymbols, selectedCell));

                if (hasUnitRow) {
                    BoardConnector verticalEdge = new BoardConnector(i, j, BoardConnectorType.VERTICAL_EDGE);
                    unitRow.append(verticalEdge.render(boardSymbols, selectedCell));
                    unitRow.append(renderUnit(board.getUnit(i, j), currentTurn, activeTeamId));
                }
            }

            BoardConnector lastCorner = new BoardConnector(i, Board.SIZE, BoardConnectorType.CORNER);
            connectorRow.append(lastCorner.render(boardSymbols, selectedCell));

            if (verbosity == Verbosity.ALL) {
                joiner.add(connectorRow);
            }

            if (hasUnitRow) {
                BoardConnector lastVerticalEdge = new BoardConnector(i, Board.SIZE, BoardConnectorType.VERTICAL_EDGE);
                unitRow.append(lastVerticalEdge.render(boardSymbols, selectedCell));
                joiner.add(unitRow);
            }
        }

        joiner.add(BOARD_COLUMNS);
        return joiner.toString();
    }

    private static String renderUnit(Unit unit, int currentTurn, TeamId activeTeamId) {
        if (unit == null) {
            return EMPTY_CELL;
        }

        StringBuilder builder = new StringBuilder();

        if (unit.getTeamId() == activeTeamId && unit.canMove(currentTurn)) {
            builder.append(CAN_MOVE_PREFIX);
        } else {
            builder.append(BOARD_FILLER);
        }

        if (unit.getTeamId() == TeamId.PLAYER) {
            if (unit.isKing()) {
                builder.append(PLAYER_KING);
            } else {
                builder.append(PLAYER_UNIT);
            }
        } else {
            if (unit.isKing()) {
                builder.append(ENEMY_KING);
            } else {
                builder.append(ENEMY_UNIT);
            }
        }

        if (unit.isBlocking()) {
            builder.append(BLOCKING_SUFFIX);
        } else {
            builder.append(BOARD_FILLER);
        }

        return builder.toString();
    }
}
