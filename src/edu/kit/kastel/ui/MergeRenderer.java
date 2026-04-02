package edu.kit.kastel.ui;

import edu.kit.kastel.models.Cell;
import edu.kit.kastel.models.results.MergeResult;

import java.util.StringJoiner;

/**
 * Utility class to render the result of a unit merge.
 *
 * @author udkcf
 * @version 1.0
 */
public final class MergeRenderer {
    private static final String OUTPUT_UNITS_MERGED = "%s and %s on %s join forces!";
    private static final String OUTPUT_SUCCESSFUL_MERGE = "Success!";
    private static final String OUTPUT_FAILED_MERGE = "Union failed. %s was eliminated.";

    private MergeRenderer() {
    }

    /**
     * Renders a string describing the result of merging two units.
     *
     * @param result the merge result
     * @param selectedCell the cell where the merge took place
     * @return the formatted string
     */
    public static String render(MergeResult result, Cell selectedCell) {
        StringJoiner joiner = new StringJoiner(System.lineSeparator());

        joiner.add(String.format(OUTPUT_UNITS_MERGED, result.firstUnit(), result.secondUnit(), selectedCell));

        if (result.isSuccess()) {
            joiner.add(OUTPUT_SUCCESSFUL_MERGE);
        } else {
            joiner.add(String.format(OUTPUT_FAILED_MERGE, result.secondUnit()));
        }

        return joiner.toString();
    }
}
