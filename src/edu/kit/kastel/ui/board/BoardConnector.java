package edu.kit.kastel.ui.board;

import edu.kit.kastel.models.Board;
import edu.kit.kastel.models.Cell;

import java.util.List;

/**
 * Orchestrates rendering of board edges and corners.
 *
 * @author udkcf
 * @version 1.0
 */
public class BoardConnector {
    private static final char STANDARD_CORNER = '+';
    private static final char STANDARD_HORIZONTAL_EDGE = '-';
    private static final int HORIZONTAL_EDGE_LENGTH = 3;
    private static final char STANDARD_VERTICAL_EDGE = '|';
    private static final char STANDARD_SELECTED_CORNER = '#';
    private static final char STANDARD_SELECTED_HORIZONTAL_EDGE = '=';
    private static final char STANDARD_SELECTED_VERTICAL_EDGE = 'N';

    private static final int NO_SELECTED = -1;
    private static final int STEP = 1;
    private static final int MIN_COORDINATE = 0;
    private static final int MAX_COORDINATE = Board.SIZE;

    private final int coord1;
    private final int coord2;
    private final BoardConnectorType type;

    /**
     * Constructs the board connector.
     *
     * @param coord1 the first coordinate
     * @param coord2 the second coordinate
     * @param type the type of connector
     */
    public BoardConnector(int coord1, int coord2, BoardConnectorType type) {
        this.coord1 = coord1;
        this.coord2 = coord2;
        this.type = type;
    }

    /**
     * Renders an edge or corner of the board based on selected cell and board symbols.
     *
     * @param boardSymbols the symbols representing the board, or null
     * @param selectedCell the selected cell
     * @return the string representation
     */
    public String render(List<Character> boardSymbols, Cell selectedCell) {
        boolean hasSelectedCell = selectedCell != null;
        int selectedI = hasSelectedCell ? selectedCell.getI() : NO_SELECTED;
        int selectedJ = hasSelectedCell ? selectedCell.getJ() : NO_SELECTED;

        return switch (type) {
            case CORNER -> String.valueOf(resolveCorner(boardSymbols, hasSelectedCell, selectedI, selectedJ));
            case HORIZONTAL_EDGE -> String.valueOf(resolveHorizontalEdge(boardSymbols, hasSelectedCell, selectedI, selectedJ))
                    .repeat(HORIZONTAL_EDGE_LENGTH);
            default -> String.valueOf(resolveVerticalEdge(boardSymbols, hasSelectedCell, selectedI, selectedJ));
        };
    }

    private char resolveCorner(List<Character> boardSymbols, boolean hasSelectedCell, int selectedI, int selectedJ) {
        char corner;

        if (boardSymbols == null) {
            if (hasSelectedCell && isCornerTouched(selectedI, selectedJ)) {
                corner = STANDARD_SELECTED_CORNER;
            } else {
                corner = STANDARD_CORNER;
            }
        } else if ((coord1 == MIN_COORDINATE || coord1 == MAX_COORDINATE) && (coord2 == MIN_COORDINATE || coord2 == MAX_COORDINATE)) {
            corner = resolveOuterCorner(boardSymbols, hasSelectedCell, selectedI, selectedJ);
        } else if (coord1 == MIN_COORDINATE) {
            corner = resolveTopMidCorner(boardSymbols, hasSelectedCell, selectedI, selectedJ);
        } else if (coord2 == MAX_COORDINATE) {
            corner = resolveRightMidCorner(boardSymbols, hasSelectedCell, selectedI, selectedJ);
        } else if (coord1 == MAX_COORDINATE) {
            corner = resolveBottomMidCorner(boardSymbols, hasSelectedCell, selectedI, selectedJ);
        } else if (coord2 == MIN_COORDINATE) {
            corner = resolveLeftMidCorner(boardSymbols, hasSelectedCell, selectedI, selectedJ);
        } else {
            corner = resolveCentralCorner(boardSymbols, hasSelectedCell, selectedI, selectedJ);
        }

        return corner;
    }

    private char resolveOuterCorner(List<Character> boardSymbols, boolean hasSelectedCell, int selectedI, int selectedJ) {
        boolean isSelected = hasSelectedCell && isCornerTouched(selectedI, selectedJ);
        BoardSymbolIndex index;

        if (coord1 == MIN_COORDINATE && coord2 == MIN_COORDINATE) {
            index = isSelected ? BoardSymbolIndex.TOP_LEFT_CORNER_SELECTED : BoardSymbolIndex.TOP_LEFT_CORNER;
        } else if (coord1 == MIN_COORDINATE && coord2 == MAX_COORDINATE) {
            index = isSelected ? BoardSymbolIndex.TOP_RIGHT_CORNER_SELECTED : BoardSymbolIndex.TOP_RIGHT_CORNER;
        } else if (coord1 == MAX_COORDINATE && coord2 == MIN_COORDINATE) {
            index = isSelected ? BoardSymbolIndex.BOTTOM_LEFT_CORNER_SELECTED : BoardSymbolIndex.BOTTOM_LEFT_CORNER;
        } else {
            index = isSelected ? BoardSymbolIndex.BOTTOM_RIGHT_CORNER_SELECTED : BoardSymbolIndex.BOTTOM_RIGHT_CORNER;
        }

        return boardSymbols.get(index.getIndex());
    }

    private char resolveTopMidCorner(List<Character> boardSymbols, boolean hasSelectedCell, int selectedI, int selectedJ) {
        char corner;
        if (hasSelectedCell && coord1 == selectedI && coord2 == selectedJ + STEP) {
            corner = boardSymbols.get(BoardSymbolIndex.TOP_MID_CORNER_LEFT_SELECTED.getIndex());
        } else if (hasSelectedCell && coord1 == selectedI && coord2 == selectedJ) {
            corner = boardSymbols.get(BoardSymbolIndex.TOP_MID_CORNER_RIGHT_SELECTED.getIndex());
        } else {
            corner = boardSymbols.get(BoardSymbolIndex.TOP_MID_CORNER.getIndex());
        }
        return corner;
    }

    private char resolveRightMidCorner(List<Character> boardSymbols, boolean hasSelectedCell, int selectedI, int selectedJ) {
        char corner;
        if (hasSelectedCell && coord1 == selectedI && coord2 == selectedJ + STEP) {
            corner = boardSymbols.get(BoardSymbolIndex.RIGHT_MID_CORNER_BOTTOM_SELECTED.getIndex());
        } else if (coord1 == selectedI + STEP && coord2 == selectedJ + STEP) {
            corner = boardSymbols.get(BoardSymbolIndex.RIGHT_MID_CORNER_TOP_SELECTED.getIndex());
        } else {
            corner = boardSymbols.get(BoardSymbolIndex.RIGHT_MID_CORNER.getIndex());
        }
        return corner;
    }

    private char resolveBottomMidCorner(List<Character> boardSymbols, boolean hasSelectedCell, int selectedI, int selectedJ) {
        char corner;
        if (hasSelectedCell && coord1 == selectedI + STEP && coord2 == selectedJ + STEP) {
            corner = boardSymbols.get(BoardSymbolIndex.BOTTOM_MID_CORNER_LEFT_SELECTED.getIndex());
        } else if (hasSelectedCell && coord1 == selectedI + STEP && coord2 == selectedJ) {
            corner = boardSymbols.get(BoardSymbolIndex.BOTTOM_MID_CORNER_RIGHT_SELECTED.getIndex());
        } else {
            corner = boardSymbols.get(BoardSymbolIndex.BOTTOM_MID_CORNER.getIndex());
        }
        return corner;
    }

    private char resolveLeftMidCorner(List<Character> boardSymbols, boolean hasSelectedCell, int selectedI, int selectedJ) {
        char corner;
        if (hasSelectedCell && coord1 == selectedI && coord2 == selectedJ) {
            corner = boardSymbols.get(BoardSymbolIndex.LEFT_MID_CORNER_BOTTOM_SELECTED.getIndex());
        } else if (hasSelectedCell && coord1 == selectedI + STEP && coord2 == selectedJ) {
            corner = boardSymbols.get(BoardSymbolIndex.LEFT_MID_CORNER_TOP_SELECTED.getIndex());
        } else {
            corner = boardSymbols.get(BoardSymbolIndex.LEFT_MID_CORNER.getIndex());
        }
        return corner;
    }

    private char resolveCentralCorner(List<Character> boardSymbols, boolean hasSelectedCell, int selectedI, int selectedJ) {
        if (!hasSelectedCell) {
            return boardSymbols.get(BoardSymbolIndex.CENTRAL_CORNER.getIndex());
        }

        BoardSymbolIndex index;
        if (coord1 == selectedI + STEP && coord2 == selectedJ + STEP) {
            index = BoardSymbolIndex.CENTRAL_CORNER_TOP_LEFT_SELECTED;
        } else if (coord1 == selectedI + STEP && coord2 == selectedJ) {
            index = BoardSymbolIndex.CENTRAL_CORNER_TOP_RIGHT_SELECTED;
        } else if (coord1 == selectedI && coord2 == selectedJ + STEP) {
            index = BoardSymbolIndex.CENTRAL_CORNER_BOTTOM_LEFT_SELECTED;
        } else if (coord1 == selectedI && coord2 == selectedJ) {
            index = BoardSymbolIndex.CENTRAL_CORNER_BOTTOM_RIGHT_SELECTED;
        } else {
            index = BoardSymbolIndex.CENTRAL_CORNER;
        }

        return boardSymbols.get(index.getIndex());
    }

    private char resolveHorizontalEdge(List<Character> boardSymbols, boolean hasSelectedCell, int selectedI, int selectedJ) {
        boolean isTouched = coord1 == selectedI && coord2 == selectedJ || coord1 == selectedI + STEP && coord2 == selectedJ;
        char edge;

        if (boardSymbols == null) {
            if (hasSelectedCell && isTouched) {
                edge = STANDARD_SELECTED_HORIZONTAL_EDGE;
            } else {
                edge = STANDARD_HORIZONTAL_EDGE;
            }
        } else if (hasSelectedCell && isTouched) {
            edge = boardSymbols.get(BoardSymbolIndex.HORIZONTAL_EDGE_SELECTED.getIndex());
        } else {
            edge = boardSymbols.get(BoardSymbolIndex.HORIZONTAL_EDGE.getIndex());
        }

        return edge;
    }

    private char resolveVerticalEdge(List<Character> boardSymbols, boolean hasSelectedCell, int selectedI, int selectedJ) {
        boolean isTouched = coord1 == selectedI && coord2 == selectedJ || coord1 == selectedI && coord2 == selectedJ + STEP;
        char edge;

        if (boardSymbols == null) {
            if (hasSelectedCell && isTouched) {
                edge = STANDARD_SELECTED_VERTICAL_EDGE;
            } else {
                edge = STANDARD_VERTICAL_EDGE;
            }
        } else if (hasSelectedCell && isTouched) {
            edge = boardSymbols.get(BoardSymbolIndex.VERTICAL_EDGE_SELECTED.getIndex());
        } else {
            edge = boardSymbols.get(BoardSymbolIndex.VERTICAL_EDGE.getIndex());
        }

        return edge;
    }

    private boolean isCornerTouched(int selectedI, int selectedJ) {
        boolean isTopCornerTouched = coord1 == selectedI && coord2 == selectedJ
                || coord1 == selectedI + STEP && coord2 == selectedJ;
        boolean isBottomCornerTouched = coord1 == selectedI && coord2 == selectedJ + STEP
                || coord1 == selectedI + STEP && coord2 == selectedJ + STEP;
        return isTopCornerTouched || isBottomCornerTouched;
    }
}
