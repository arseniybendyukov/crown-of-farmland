package edu.kit.kastel.ui;

import edu.kit.kastel.models.results.YieldResult;

import java.util.StringJoiner;

/**
 * Utility class to render the result of yielding a turn.
 *
 * @author udkcf
 * @version 1.0
 */
public final class YieldRenderer {
    private static final String TEAM_DISCARDED_UNIT = "%s discarded %s (%d/%d).";
    private static final String OPPONENT_TURN = "It is %s's turn!";
    private static final String LOST_OUTPUT = "%s has no cards left in the deck!";

    private YieldRenderer() {
    }

    /**
     * Renders a string representing the result of yielding a turn.
     *
     * @param result the yield result
     * @return the formatted string
     */
    public static String render(YieldResult result) {
        StringJoiner joiner = new StringJoiner(System.lineSeparator());

        result.discardedUnit().ifPresent(unit -> joiner.add(String.format(
                TEAM_DISCARDED_UNIT,
                result.teamName(),
                unit.name(),
                unit.atk(),
                unit.def()
        )));

        joiner.add(String.format(OPPONENT_TURN, result.opponentName()));

        if (result.lost()) {
            joiner.add(String.format(LOST_OUTPUT, result.teamName()));
        }

        return joiner.toString();
    }
}
