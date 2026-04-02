package edu.kit.kastel.commands;

import edu.kit.kastel.exceptions.InvalidCommandArgumentException;
import edu.kit.kastel.models.Game;


/**
 * Command for displaying details of the currently selected unit.
 *
 * @author udkcf
 * @version 1.0
 */
public class ShowCommand extends Command {
    private static final String COMMAND_NAME = "show";

    /**
     * Constructs the show command.
     *
     * @param commandHandler the command handler
     * @param game the game instance
     */
    protected ShowCommand(CommandHandler commandHandler, Game game) {
        super(COMMAND_NAME, COMMAND_NAME, commandHandler, game);
    }

    /**
     * Executes the show command.
     *
     * @param commandArguments the command arguments
     * @throws InvalidCommandArgumentException if the game state is invalid
     */
    @Override
    public void execute(String[] commandArguments) throws InvalidCommandArgumentException {
        forbidIfYieldHandMode();
        requireSelectedCell();
        renderSelectedUnit();
    }
}
