package de.bendyukov.farm.commands;

import de.bendyukov.farm.exceptions.InvalidCommandArgumentException;
import de.bendyukov.farm.models.Game;


/**
 * Command for displaying details of the currently selected unit.
 *
 * @author Arseniy Bendyukov
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
