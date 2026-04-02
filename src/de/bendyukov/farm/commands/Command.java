package de.bendyukov.farm.commands;

import de.bendyukov.farm.exceptions.InvalidCommandArgumentException;
import de.bendyukov.farm.models.Board;
import de.bendyukov.farm.models.Cell;
import de.bendyukov.farm.models.Column;
import de.bendyukov.farm.models.Config;
import de.bendyukov.farm.models.Game;
import de.bendyukov.farm.models.Row;
import de.bendyukov.farm.models.Team;
import de.bendyukov.farm.models.Unit;
import de.bendyukov.farm.ui.SelectedUnitRenderer;
import de.bendyukov.farm.ui.board.BoardRenderer;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Abstract base class representing a user-executable command within the game.
 *
 * @author Arseniy Bendyukov
 * @version 1.0
 */
public abstract class Command {
    private static final int CELL_STRING_LENGTH = 2;
    private static final int COLUMN_INDEX = 0;
    private static final int ROW_INDEX = 1;
    private static final String ERROR_INVALID_CELL = "%s is not a valid cell";
    private static final String ERROR_SELECTED_CELL_REQUIRED = "This command requires a selected cell";
    private static final int ONE_BASED_OFFSET = 1;
    private static final String ERROR_INVALID_INDEX = "Indices must be integers >= 1";
    private static final String ERROR_DUPLICATE_INDEX = "Duplicate indices are forbidden";
    private static final String TEAM_WINS = "%s wins!";
    private static final int NO_ARGUMENTS = 0;
    private static final int FIRST_ARGUMENT_OPTIONAL_INDEX = 0;
    private static final String ERROR_DISCARD_MODE = "Cannot execute the command (only 'yield' and 'hand' are allowed)";
    private static final int INITIAL_INDEX = 0;

    /**
     * This is the command handler.
     */
    protected final CommandHandler commandHandler;

    /**
     * This is the game orchestrator.
     */
    protected final Game game;

    private final String commandName;
    private final String commandRegex;

    /**
     * Commands that have the same name as regex, for example quit, do not have a seperate regex.
     *
     * @param commandName The name of the command
     * @param commandRegex The regex to match the command against
     * @param commandHandler The command handler
     * @param game The Game
     */
    protected Command(String commandName, String commandRegex, CommandHandler commandHandler, Game game) {
        this.commandName = commandName;
        this.commandRegex = commandRegex;
        this.commandHandler = commandHandler;
        this.game = game;
    }

    /**
     * Validates a given cell representation string into a Cell.
     *
     * @param commandArguments the command arguments
     * @return the parsed Cell
     * @throws InvalidCommandArgumentException if the input is not a valid cell representation
     */
    protected static Cell validateCell(String[] commandArguments) throws InvalidCommandArgumentException {
        String cellStringified = commandArguments[FIRST_ARGUMENT_OPTIONAL_INDEX];
        if (cellStringified.length() != CELL_STRING_LENGTH) {
            throw new InvalidCommandArgumentException(ERROR_INVALID_CELL.formatted(cellStringified));
        }

        Optional<Column> column = Column.fromString(String.valueOf(cellStringified.charAt(COLUMN_INDEX)));
        Optional<Row> row = Row.fromString(String.valueOf(cellStringified.charAt(ROW_INDEX)));

        if (column.isEmpty() || row.isEmpty()) {
            throw new InvalidCommandArgumentException(ERROR_INVALID_CELL.formatted(cellStringified));
        }

        return new Cell(column.get(), row.get());
    }

    /**
     * Validates an array of string representations of 1-based indices into an array of 0-based indices.
     *
     * @param indicesStringified the string representations of the 1-based indices
     * @return an array of unique 0-based integer indices
     * @throws InvalidCommandArgumentException if indices are invalid or duplicated
     */
    protected static int[] validateIndices(String[] indicesStringified) throws InvalidCommandArgumentException {
        Set<Integer> indices = new LinkedHashSet<>();

        for (String indexStringified : indicesStringified) {
            try {
                int index = Integer.parseInt(indexStringified) - ONE_BASED_OFFSET;
                if (!indices.add(index)) {
                    throw new InvalidCommandArgumentException(ERROR_DUPLICATE_INDEX);
                }
            } catch (NumberFormatException exception) {
                throw new InvalidCommandArgumentException(ERROR_INVALID_INDEX);
            }
        }

        int[] result = new int[indices.size()];
        int resultIndex = INITIAL_INDEX;
        for (Integer index : indices) {
            result[resultIndex] = index;
            resultIndex++;
        }
        return result;
    }

    /**
     * Validates an optional string representation of a 1-based index from the command arguments.
     *
     * @param commandArguments the command arguments
     * @return the 0-based integer index, or null if no arguments are provided
     * @throws InvalidCommandArgumentException if the input is not a valid non-negative integer
     */
    protected static Integer validateOptionalIndex(String[] commandArguments) throws InvalidCommandArgumentException {
        if (commandArguments.length == NO_ARGUMENTS) {
            return null;
        }
        try {
            return Integer.parseInt(commandArguments[FIRST_ARGUMENT_OPTIONAL_INDEX]) - ONE_BASED_OFFSET;
        } catch (NumberFormatException exception) {
            throw new InvalidCommandArgumentException(ERROR_INVALID_INDEX);
        }
    }

    /**
     * This returns the command name.
     *
     * @return The name of the command.
     */
    public final String getCommandName() {
        return commandName;
    }

    /**
     * This returns the regex that the input has to match against for the command to be executed.
     *
     * @return The pattern of the command.
     */
    public final String getCommandRegex() {
        return commandRegex;
    }

    /**
     * Executes a given command. The arguments are already split by the command handler.
     *
     * @param commandArguments The arguments the command needs to run. Can contain optional arguments
     * @throws InvalidCommandArgumentException If the arguments don't match the required types or formats, this exception will be thrown
     */
    public abstract void execute(String[] commandArguments) throws InvalidCommandArgumentException;

    /**
     * Enforces that a cell must be currently selected.
     *
     * @throws InvalidCommandArgumentException if no cell is selected
     */
    protected void requireSelectedCell() throws InvalidCommandArgumentException {
        if (game.getBoard().getSelectedCell() == null) {
            throw new InvalidCommandArgumentException(ERROR_SELECTED_CELL_REQUIRED);
        }
    }

    /**
     * Renders the current visualization of the game board.
     */
    protected void renderBoard() {
        Config config = game.getConfig();
        System.out.println(BoardRenderer.render(
                game.getBoard(),
                game.getCurrentTurn(),
                game.getActiveTeamId(),
                config.getBoardSymbols(),
                config.getVerbosity()
        ));
    }

    /**
     * Renders details about the currently selected unit, unconditionally.
     */
    protected void renderSelectedUnit() {
        renderSelectedUnit(false);
    }

    /**
     * Outputs details about the currently selected unit, if one exists.
     */
    protected void renderSelectedUnitIfPresent() {
        renderSelectedUnit(true);
    }

    private void renderSelectedUnit(boolean renderOnlyIfSelected) {
        Board board = game.getBoard();

        if (renderOnlyIfSelected) {
            Cell selectedCell = board.getSelectedCell();
            if (selectedCell == null) {
                return;
            }
        }

        Unit selectedUnit = board.getSelectedUnit();

        System.out.println(SelectedUnitRenderer.render(
                selectedUnit,
                game.getUnitTeamName(selectedUnit),
                game.hasAccessToUnit(selectedUnit)
        ));
    }

    /**
     * Verifies if any team has won and stops the game if so.
     *
     * @return true if the game has ended, false otherwise
     */
    protected boolean endGameIfWinner() {
        Team winner = game.getWinner();
        if (winner != null) {
            System.out.println(String.format(TEAM_WINS, winner.getName()));
            commandHandler.quit();
            return true;
        }
        return false;
    }

    /**
     * Prevents the execution of a command if the game is waiting for a unit discard.
     *
     * @throws InvalidCommandArgumentException if the game is waiting for a unit discard
     */
    protected void forbidIfYieldHandMode() throws InvalidCommandArgumentException {
        if (game.isYieldHandMode()) {
            throw new InvalidCommandArgumentException(ERROR_DISCARD_MODE);
        }
    }
}
