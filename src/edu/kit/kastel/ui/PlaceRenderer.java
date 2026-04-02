package edu.kit.kastel.ui;

import edu.kit.kastel.models.Cell;
import edu.kit.kastel.models.results.MergeResult;
import edu.kit.kastel.models.results.PlaceResult;

import java.util.StringJoiner;

/**
 * Utility class to render the result of placing a unit onto the board.
 *
 * @author udkcf
 * @version 1.0
 */
public final class PlaceRenderer {
    private static final String OUTPUT_TEAM_PLACES_UNIT = "%s places %s on %s.";
    private static final String OUTPUT_UNIT_ELIMINATED = "%s was eliminated!";

    private PlaceRenderer() {
    }

    /**
     * Renders a string describing the result of placing a unit.
     *
     * @param result the place result
     * @param selectedCell the cell where the unit was placed
     * @return the formatted string
     */
    public static String render(PlaceResult result, Cell selectedCell) {
        StringJoiner joiner = new StringJoiner(System.lineSeparator());

        joiner.add(String.format(OUTPUT_TEAM_PLACES_UNIT, result.teamName(), result.unitName(), selectedCell));

        MergeResult mergeResult = result.mergeResult();

        if (mergeResult != null) {
            joiner.add(MergeRenderer.render(mergeResult, selectedCell));
        } else if (result.isEliminated()) {
            joiner.add(String.format(OUTPUT_UNIT_ELIMINATED, result.unitName()));
        }

        return joiner.toString();
    }
}
