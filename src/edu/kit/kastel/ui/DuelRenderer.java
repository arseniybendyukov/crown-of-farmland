package edu.kit.kastel.ui;

import edu.kit.kastel.models.Cell;
import edu.kit.kastel.models.results.DuelResult;
import edu.kit.kastel.models.results.DuelUnit;
import edu.kit.kastel.models.results.TeamDamage;

import java.util.StringJoiner;

/**
 * Utility class to render the result of a duel.
 *
 * @author udkcf
 * @version 1.0
 */
public final class DuelRenderer {
    private static final String DUEL_OUTPUT = "%s attacks %s on %s!";
    private static final String UNIT_OUTPUT = "%s (%d/%d)";
    private static final String FLIP_OUTPUT = "%s was flipped on %s!";
    private static final String ELIMINATED_OUTPUT = "%s was eliminated!";
    private static final String DAMAGE_OUTPUT = "%s takes %d damage!";
    private static final String MOVE_OUTPUT = "%s moves to %s.";
    private static final String LOST_OUTPUT = "%s's life points dropped to 0!";
    private static final String HIDDEN = "???";

    private DuelRenderer() {
    }

    /**
     * Renders a string describing the result of a duel.
     *
     * @param result the duel result
     * @return the formatted string
     */
    public static String render(DuelResult result) {
        StringJoiner joiner = new StringJoiner(System.lineSeparator());

        DuelUnit attacker = result.attacker();
        DuelUnit defender = result.defender();

        String attackerStringified = UNIT_OUTPUT.formatted(attacker.name(), attacker.atk(), attacker.def());
        String defenderStringified = UNIT_OUTPUT.formatted(defender.name(), defender.atk(), defender.def());

        if (defender.isKing()) {
            joiner.add(String.format(DUEL_OUTPUT, attackerStringified, defender.name(), defender.cell()));
        } else {
            joiner.add(String.format(
                    DUEL_OUTPUT,
                    attackerStringified,
                    defender.hasAccess() ? defenderStringified : HIDDEN,
                    defender.cell()
            ));
        }

        if (attacker.isFlipped()) {
            joiner.add(String.format(FLIP_OUTPUT, attackerStringified, attacker.cell()));
        }

        if (defender.isFlipped()) {
            joiner.add(String.format(FLIP_OUTPUT, defenderStringified, defender.cell()));
        }

        if (defender.isEliminated()) {
            joiner.add(String.format(ELIMINATED_OUTPUT, defender.name()));
        }

        if (attacker.isEliminated()) {
            joiner.add(String.format(ELIMINATED_OUTPUT, attacker.name()));
        }

        TeamDamage teamDamage = result.teamDamage();
        if (teamDamage != null) {
            joiner.add(String.format(DAMAGE_OUTPUT, teamDamage.teamName(), teamDamage.damage()));
        }

        Cell movedToCell = result.movedToCell();
        if (movedToCell != null) {
            joiner.add(String.format(MOVE_OUTPUT, attacker.name(), movedToCell));
        }

        if (teamDamage != null && teamDamage.lost()) {
            joiner.add(String.format(LOST_OUTPUT, teamDamage.teamName()));
        }

        return joiner.toString();
    }
}
