package edu.kit.kastel.commands;

import edu.kit.kastel.exceptions.InvalidCommandArgumentException;
import edu.kit.kastel.models.Cell;
import edu.kit.kastel.models.Game;

/**
 * Command for selecting a cell on the board.
 *
 * @author udkcf
 * @version 1.0
 */
public class SelectCommand extends Command {
    private static final String COMMAND_NAME = "select";

    /*
     * The regex looks for select followed by a field identifier
     * consisting of a capital letter from A to G and a digit from 1 to 7.
     */
    private static final String COMMAND_REGEX = "select [A-G][1-7]";

    /**
     * Constructs the select command.
     *
     * @param commandHandler the command handler
     * @param game the game instance
     */
    protected SelectCommand(CommandHandler commandHandler, Game game) {
        super(COMMAND_NAME, COMMAND_REGEX, commandHandler, game);
    }

    /**
     * Executes the select command.
     *
     * @param commandArguments the command arguments
     * @throws InvalidCommandArgumentException if the arguments or game state are invalid
     */
    @Override
    public void execute(String[] commandArguments) throws InvalidCommandArgumentException {
        forbidIfYieldHandMode();
        Cell cell = validateCell(commandArguments);
        game.getBoard().selectCell(cell);
        renderBoard();
        renderSelectedUnit();
    }
}
