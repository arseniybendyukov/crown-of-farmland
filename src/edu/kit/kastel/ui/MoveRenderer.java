package edu.kit.kastel.ui;

import edu.kit.kastel.models.Cell;
import edu.kit.kastel.models.results.DuelResult;
import edu.kit.kastel.models.results.MergeResult;
import edu.kit.kastel.models.results.MoveResult;

import java.util.StringJoiner;

/**
 * Utility class to render the result of moving or blocking a unit.
 *
 * @author udkcf
 * @version 1.0
 */
public final class MoveRenderer {
    private static final String NO_LONGER_BLOCKS = "%s no longer blocks.";
    private static final String UNIT_MOVES_TO_FIELD = "%s moves to %s.";
    private static final String UNIT_BLOCKS = "%s (%s) blocks!";


    private MoveRenderer() {
    }

    /**
     * Renders a string describing the outcome of a move or block action.
     *
     * @param result the move result
     * @param selectedCell the current or final cell of the unit
     * @return the formatted string
     */
    public static String render(MoveResult result, Cell selectedCell) {
        if (result.isBlockMove()) {
            return String.format(UNIT_BLOCKS, result.unitName(), selectedCell);
        }

        StringJoiner joiner = new StringJoiner(System.lineSeparator());

        if (result.noLongerBlocks()) {
            joiner.add(String.format(NO_LONGER_BLOCKS, result.unitName()));
        }

        DuelResult duelResult = result.duelResult();

        if (duelResult != null) {
            joiner.add(DuelRenderer.render(duelResult));
        } else {
            joiner.add(String.format(UNIT_MOVES_TO_FIELD, result.unitName(), selectedCell));

            MergeResult mergeResult = result.mergeResult();
            if (mergeResult != null) {
                joiner.add(MergeRenderer.render(mergeResult, selectedCell));
            }
        }

        return joiner.toString();
    }
}
