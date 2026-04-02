package edu.kit.kastel.commands;

import edu.kit.kastel.exceptions.GameException;
import edu.kit.kastel.exceptions.InvalidCommandArgumentException;
import edu.kit.kastel.models.Game;
import edu.kit.kastel.models.results.MoveResult;
import edu.kit.kastel.ui.MoveRenderer;

/**
 * Command for blocking a selected unit.
 *
 * @author udkcf
 * @version 1.0
 */
public class BlockCommand extends Command {
    private static final String COMMAND_NAME = "block";

    /**
     * Constructs the block command.
     *
     * @param commandHandler the command handler
     * @param game the game instance
     */
    protected BlockCommand(CommandHandler commandHandler, Game game) {
        super(COMMAND_NAME, COMMAND_NAME, commandHandler, game);
    }

    /**
     * Executes the block command.
     *
     * @param commandArguments the command arguments
     * @throws InvalidCommandArgumentException if the arguments or game state are invalid
     */
    @Override
    public void execute(String[] commandArguments) throws InvalidCommandArgumentException {
        forbidIfYieldHandMode();
        requireSelectedCell();

        try {
            MoveResult result = game.blockSelectedUnit();
            System.out.println(MoveRenderer.render(result, game.getBoard().getSelectedCell()));
        } catch (GameException exception) {
            throw new InvalidCommandArgumentException(exception.getMessage());
        }

        renderBoard();
        renderSelectedUnit();
    }
}
