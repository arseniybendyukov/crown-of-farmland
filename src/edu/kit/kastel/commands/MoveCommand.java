package edu.kit.kastel.commands;

import edu.kit.kastel.exceptions.GameException;
import edu.kit.kastel.exceptions.InvalidCommandArgumentException;
import edu.kit.kastel.models.Cell;
import edu.kit.kastel.models.Game;
import edu.kit.kastel.models.results.MoveResult;
import edu.kit.kastel.ui.MoveRenderer;

/**
 * Command for moving the selected unit to a cross-neighbouring cell.
 *
 * @author udkcf
 * @version 1.0
 */
public class MoveCommand extends Command {
    private static final String COMMAND_NAME = "move";

    /*
     * The regex looks for move followed by a field identifier
     * consisting of a capital letter from A to G and a digit from 1 to 7.
     */
    private static final String COMMAND_REGEX = "move [A-G][1-7]";

    /**
     * Constructs the move command.
     *
     * @param commandHandler the command handler
     * @param game the game instance
     */
    protected MoveCommand(CommandHandler commandHandler, Game game) {
        super(COMMAND_NAME, COMMAND_REGEX, commandHandler, game);
    }

    /**
     * Executes the move command.
     *
     * @param commandArguments the command arguments
     * @throws InvalidCommandArgumentException if the arguments or game state are invalid
     */
    @Override
    public void execute(String[] commandArguments) throws InvalidCommandArgumentException {
        forbidIfYieldHandMode();

        Cell targetCell = validateCell(commandArguments);

        try {
            MoveResult result = game.move(targetCell);
            System.out.println(MoveRenderer.render(result, game.getBoard().getSelectedCell()));
        } catch (GameException exception) {
            throw new InvalidCommandArgumentException(exception.getMessage());
        }

        endGameIfWinner();
        renderBoard();
        renderSelectedUnit();
    }
}
