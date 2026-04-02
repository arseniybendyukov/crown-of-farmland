package edu.kit.kastel.commands;

import edu.kit.kastel.exceptions.GameException;
import edu.kit.kastel.exceptions.InvalidCommandArgumentException;
import edu.kit.kastel.models.Board;
import edu.kit.kastel.models.Cell;
import edu.kit.kastel.models.Game;
import edu.kit.kastel.models.Unit;

/**
 * Command for flipping a selected unit.
 *
 * @author udkcf
 * @version 1.0
 */
public class FlipCommand extends Command {
    private static final String COMMAND_NAME = "flip";
    private static final String COMMAND_OUTPUT = "%s (%d/%d) was flipped on %s!";

    /**
     * Constructs the flip command.
     *
     * @param commandHandler the command handler
     * @param game the game instance
     */
    protected FlipCommand(CommandHandler commandHandler, Game game) {
        super(COMMAND_NAME, COMMAND_NAME, commandHandler, game);
    }

    /**
     * Executes the flip command.
     *
     * @param commandArguments the command arguments
     * @throws InvalidCommandArgumentException if the arguments or game state are invalid
     */
    @Override
    public void execute(String[] commandArguments) throws InvalidCommandArgumentException {
        forbidIfYieldHandMode();
        requireSelectedCell();

        try {
            game.flipSelectedUnit();
        } catch (GameException exception) {
            throw new InvalidCommandArgumentException(exception.getMessage());
        }

        Board board = game.getBoard();
        Unit selectedUnit = board.getSelectedUnit();
        Cell selectedCell = board.getSelectedCell();
        System.out.println(String.format(
                COMMAND_OUTPUT,
                selectedUnit.getName(),
                selectedUnit.getAtk(),
                selectedUnit.getDef(),
                selectedCell
        ));

        renderBoard();
        renderSelectedUnit();
    }
}
