package de.bendyukov.farm.ui.board;

/**
 * Indices of symbols used to represent the board.
 *
 * @author Arseniy Bendyukov
 * @version 1.0
 */
public enum BoardSymbolIndex {
    /** Horizontal edge. */
    HORIZONTAL_EDGE(8),
    /** Selected horizontal edge. */
    HORIZONTAL_EDGE_SELECTED(23),
    /** Vertical edge. */
    VERTICAL_EDGE(9),
    /** Selected vertical edge. */
    VERTICAL_EDGE_SELECTED(24),
    /** Top left corner. */
    TOP_LEFT_CORNER(0),
    /** Top right corner. */
    TOP_RIGHT_CORNER(1),
    /** Bottom left corner. */
    BOTTOM_LEFT_CORNER(2),
    /** Bottom right corner. */
    BOTTOM_RIGHT_CORNER(3),
    /** Top left corner selected. */
    TOP_LEFT_CORNER_SELECTED(11),
    /** Top right corner selected. */
    TOP_RIGHT_CORNER_SELECTED(12),
    /** Bottom left corner selected. */
    BOTTOM_LEFT_CORNER_SELECTED(13),
    /** Bottom right corner selected. */
    BOTTOM_RIGHT_CORNER_SELECTED(14),
    /** Top mid corner. */
    TOP_MID_CORNER(4),
    /** Right mid corner. */
    RIGHT_MID_CORNER(5),
    /** Bottom mid corner. */
    BOTTOM_MID_CORNER(6),
    /** Left mid corner. */
    LEFT_MID_CORNER(7),
    /** Top mid left selected. */
    TOP_MID_CORNER_LEFT_SELECTED(15),
    /** Top mid right selected. */
    TOP_MID_CORNER_RIGHT_SELECTED(16),
    /** Right mid top selected. */
    RIGHT_MID_CORNER_TOP_SELECTED(17),
    /** Right mid bottom selected. */
    RIGHT_MID_CORNER_BOTTOM_SELECTED(18),
    /** Bottom mid left selected. */
    BOTTOM_MID_CORNER_LEFT_SELECTED(19),
    /** Bottom mid right selected. */
    BOTTOM_MID_CORNER_RIGHT_SELECTED(20),
    /** Left mid top selected. */
    LEFT_MID_CORNER_TOP_SELECTED(21),
    /** Left mid bottom selected. */
    LEFT_MID_CORNER_BOTTOM_SELECTED(22),
    /** Central corner. */
    CENTRAL_CORNER(10),
    /** Central top left selected. */
    CENTRAL_CORNER_TOP_LEFT_SELECTED(25),
    /** Central top right selected. */
    CENTRAL_CORNER_TOP_RIGHT_SELECTED(26),
    /** Central bottom left selected. */
    CENTRAL_CORNER_BOTTOM_LEFT_SELECTED(27),
    /** Central bottom right selected. */
    CENTRAL_CORNER_BOTTOM_RIGHT_SELECTED(28);

    private final int index;

    BoardSymbolIndex(int index) {
        this.index = index;
    }

    /**
     * Gets the index of the symbol.
     *
     * @return the index
     */
    public int getIndex() {
        return index;
    }
}
