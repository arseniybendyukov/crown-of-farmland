package edu.kit.kastel.commands;

import edu.kit.kastel.exceptions.InvalidCommandArgumentException;
import edu.kit.kastel.models.Game;

/**
 * Command for displaying the board.
 *
 * @author udkcf
 * @version 1.0
 */
public class BoardCommand extends Command {
    private static final String COMMAND_NAME = "board";

    /**
     * Constructs the board command.
     *
     * @param commandHandler the command handler
     * @param game the game instance
     */
    protected BoardCommand(CommandHandler commandHandler, Game game) {
        super(COMMAND_NAME, COMMAND_NAME, commandHandler, game);
    }

    /**
     * Executes the board command.
     *
     * @param commandArguments the command arguments
     * @throws InvalidCommandArgumentException if the game is in yield hand mode
     */
    @Override
    public void execute(String[] commandArguments) throws InvalidCommandArgumentException {
        forbidIfYieldHandMode();
        renderBoard();
    }
}
