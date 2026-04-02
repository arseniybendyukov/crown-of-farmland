package edu.kit.kastel.ui;

import edu.kit.kastel.models.Unit;

import java.util.StringJoiner;

/**
 * Utility class to render the details of a selected unit.
 *
 * @author udkcf
 * @version 1.0
 */
public final class SelectedUnitRenderer {
    private static final String KING_OUTPUT = "%s's %s";
    private static final String NO_UNIT_MESSAGE = "<no unit>";
    private static final String UNIT_NAME_OUTPUT = "%s (Team %s)";
    private static final String ATK_OUTPUT = "ATK: %s";
    private static final String DEF_OUTPUT = "DEF: %s";
    private static final String HIDDEN = "???";

    private SelectedUnitRenderer() {
    }

    /**
     * Renders a string containing the visible stats and name of the selected unit.
     *
     * @param selectedUnit the unit, or null
     * @param teamName the name of the team owning the unit
     * @param hasAccessToSelectedUnit true if the current active team can see the unit's stats
     * @return the formatted string
     */
    public static String render(Unit selectedUnit, String teamName, boolean hasAccessToSelectedUnit) {
        if (selectedUnit == null) {
            return NO_UNIT_MESSAGE;
        } else {
            if (selectedUnit.isKing()) {
                return String.format(KING_OUTPUT, teamName, selectedUnit.getName());
            } else {
                StringJoiner output = new StringJoiner(System.lineSeparator());

                String name = hasAccessToSelectedUnit ? selectedUnit.getName() : HIDDEN;
                String atk = hasAccessToSelectedUnit ? String.valueOf(selectedUnit.getAtk()) : HIDDEN;
                String def = hasAccessToSelectedUnit ? String.valueOf(selectedUnit.getDef()) : HIDDEN;

                output.add(String.format(UNIT_NAME_OUTPUT, name, teamName))
                        .add(String.format(ATK_OUTPUT, atk))
                        .add(String.format(DEF_OUTPUT, def));

                return output.toString();
            }
        }
    }
}
