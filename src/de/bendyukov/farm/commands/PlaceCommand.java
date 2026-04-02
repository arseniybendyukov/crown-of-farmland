package de.bendyukov.farm.commands;

import de.bendyukov.farm.exceptions.GameException;
import de.bendyukov.farm.exceptions.InvalidCommandArgumentException;
import de.bendyukov.farm.models.Cell;
import de.bendyukov.farm.models.Game;
import de.bendyukov.farm.models.results.PlaceResult;
import de.bendyukov.farm.ui.PlaceRenderer;

import java.util.List;

/**
 * Command for placing one or more units from the hand onto the selected board cell.
 *
 * @author Arseniy Bendyukov
 * @version 1.0
 */
public class PlaceCommand extends Command {
    private static final String COMMAND_NAME = "place";

    /*
     * The regex looks for place followed by one or more indices (1-based) separated by spaces.
     */
    private static final String COMMAND_REGEX = "place [1-9]\\d*( [1-9]\\d*)*";

    /**
     * Constructs the place command.
     *
     * @param commandHandler the command handler
     * @param game the game instance
     */
    protected PlaceCommand(CommandHandler commandHandler, Game game) {
        super(COMMAND_NAME, COMMAND_REGEX, commandHandler, game);
    }

    /**
     * Executes the place command.
     *
     * @param commandArguments the command arguments
     * @throws InvalidCommandArgumentException if the arguments or game state are invalid
     */
    @Override
    public void execute(String[] commandArguments) throws InvalidCommandArgumentException {
        forbidIfYieldHandMode();
        requireSelectedCell();
        int[] indices = validateIndices(commandArguments);
        try {
            List<PlaceResult> results = game.place(indices);
            Cell selectedCell = game.getBoard().getSelectedCell();
            for (PlaceResult result : results) {
                System.out.println(PlaceRenderer.render(result, selectedCell));
            }
            renderBoard();
            renderSelectedUnit();
        } catch (GameException exception) {
            throw new InvalidCommandArgumentException(exception.getMessage());
        }
    }
}
