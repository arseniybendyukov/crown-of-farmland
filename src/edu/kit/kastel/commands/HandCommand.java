package edu.kit.kastel.commands;

import edu.kit.kastel.models.Game;
import edu.kit.kastel.models.Hand;
import edu.kit.kastel.models.Unit;

import java.util.List;

/**
 * Command for displaying the active team's hand.
 *
 * @author udkcf
 * @version 1.0
 */
public class HandCommand extends Command {
    private static final String COMMAND_NAME = "hand";
    private static final String UNIT_OUTPUT = "[%d] %s (%d/%d)";
    private static final int DISPLAY_INDEX_OFFSET = 1;

    /**
     * Constructs the hand command.
     *
     * @param commandHandler the command handler
     * @param game the game instance
     */
    protected HandCommand(CommandHandler commandHandler, Game game) {
        super(COMMAND_NAME, COMMAND_NAME, commandHandler, game);
    }

    /**
     * Executes the hand command.
     *
     * @param commandArguments the command arguments
     */
    @Override
    public void execute(String[] commandArguments) {
        Hand activeTeamHand = game.getActiveTeam().getHand();
        List<Unit> units = activeTeamHand.getUnits();

        for (int i = 0; i < units.size(); i++) {
            Unit unit = units.get(i);
            System.out.println(String.format(UNIT_OUTPUT, i + DISPLAY_INDEX_OFFSET, unit.getName(), unit.getAtk(), unit.getDef()));
        }
    }
}
