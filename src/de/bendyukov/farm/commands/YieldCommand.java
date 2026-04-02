package de.bendyukov.farm.commands;

import de.bendyukov.farm.exceptions.GameException;
import de.bendyukov.farm.exceptions.InvalidCommandArgumentException;
import de.bendyukov.farm.models.Board;
import de.bendyukov.farm.models.Game;
import de.bendyukov.farm.models.TeamId;
import de.bendyukov.farm.models.enemy.EnemyBot;
import de.bendyukov.farm.models.results.MoveResult;
import de.bendyukov.farm.models.results.PlaceResult;
import de.bendyukov.farm.models.results.YieldResult;
import de.bendyukov.farm.ui.MoveRenderer;
import de.bendyukov.farm.ui.PlaceRenderer;
import de.bendyukov.farm.ui.YieldRenderer;

import java.util.Optional;

/**
 * Command for yielding the current turn and optionally discarding a card.
 * On success, the enemy turn is taken.
 *
 * @author Arseniy Bendyukov
 * @version 1.0
 */
public class YieldCommand extends Command {
    private static final String COMMAND_NAME = "yield";

    /*
     * The regex looks for yield optionally followed by a single index (1-based).
     */
    private static final String COMMAND_REGEX = "yield(?: [1-9]\\d*)?";

    /**
     * Constructs the yield command.
     *
     * @param commandHandler the command handler
     * @param game the game instance
     */
    protected YieldCommand(CommandHandler commandHandler, Game game) {
        super(COMMAND_NAME, COMMAND_REGEX, commandHandler, game);
    }

    /**
     * Executes the yield command and the enemy turn.
     *
     * @param commandArguments the command arguments
     * @throws InvalidCommandArgumentException if the arguments or game state are invalid
     */
    @Override
    public void execute(String[] commandArguments) throws InvalidCommandArgumentException {
        Integer index = validateOptionalIndex(commandArguments);

        try {
            YieldResult result = game.yield(index);
            System.out.println(YieldRenderer.render(result));
        } catch (GameException exception) {
            throw new InvalidCommandArgumentException(exception.getMessage());
        }

        endGameIfWinner();

        if (game.getActiveTeamId() == TeamId.ENEMY) {
            takeEnemyTurn();
        }
    }

    private void takeEnemyTurn() throws InvalidCommandArgumentException {
        EnemyBot enemyBot = new EnemyBot(game);
        Board board = game.getBoard();

        try {
            MoveResult result = enemyBot.moveKing();
            System.out.println(MoveRenderer.render(result, board.getSelectedCell()));
        } catch (GameException exception) {
            throw new InvalidCommandArgumentException(exception.getMessage());
        }

        renderBoard();
        renderSelectedUnit();

        try {
            Optional<PlaceResult> result = enemyBot.place();
            result.ifPresent(placeResult -> System.out.println(PlaceRenderer.render(placeResult, board.getSelectedCell())));
        } catch (GameException exception) {
            throw new InvalidCommandArgumentException(exception.getMessage());
        }

        renderBoard();
        renderSelectedUnit();

        boolean isEnd = false;

        while (!board.getMovableUnitCells(TeamId.ENEMY, game.getCurrentTurn()).isEmpty()) {
            try {
                MoveResult result = enemyBot.move();
                System.out.println(MoveRenderer.render(result, board.getSelectedCell()));
            } catch (GameException exception) {
                throw new InvalidCommandArgumentException(exception.getMessage());
            }

            isEnd = endGameIfWinner();
            renderBoard();
            renderSelectedUnit();

            if (isEnd) {
                break;
            }
        }

        if (!isEnd) {
            try {
                YieldResult result = enemyBot.yield();
                System.out.println(YieldRenderer.render(result));
            } catch (GameException exception) {
                throw new InvalidCommandArgumentException(exception.getMessage());
            }

            endGameIfWinner();
        }
    }
}
