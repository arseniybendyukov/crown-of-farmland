package edu.kit.kastel.commands;

import edu.kit.kastel.exceptions.InvalidCommandArgumentException;
import edu.kit.kastel.models.Deck;
import edu.kit.kastel.models.Game;
import edu.kit.kastel.models.Hand;
import edu.kit.kastel.models.Team;
import edu.kit.kastel.models.TeamId;

/**
 * Command for displaying the game state.
 *
 * @author udkcf
 * @version 1.0
 */
public class StateCommand extends Command {
    private static final String COMMAND_NAME = "state";
    private static final String OUTPUT_LIFE_POINTS = "%d/%d LP";
    private static final String OUTPUT_DECK_COUNT = "DC: %d/%d";
    private static final String OUTPUT_BOARD_COUNT = "BC: %d/%d";
    private static final String PADDING = " ";
    private static final int OUTPUT_LINE_LENGTH = 31;
    private static final int OFFSET_LENGTH = 2;

    /**
     * Constructs the state command.
     *
     * @param commandHandler the command handler
     * @param game the game instance
     */
    protected StateCommand(CommandHandler commandHandler, Game game) {
        super(COMMAND_NAME, COMMAND_NAME, commandHandler, game);
    }

    /**
     * Executes the state command.
     *
     * @param commandArguments the command arguments
     * @throws InvalidCommandArgumentException if the game state is invalid
     */
    @Override
    public void execute(String[] commandArguments) throws InvalidCommandArgumentException {
        forbidIfYieldHandMode();

        Team player = game.getPlayer();
        Team enemy = game.getEnemy();

        displayWithPadding(player.getName(), enemy.getName());
        displayWithPadding(
                String.format(OUTPUT_LIFE_POINTS, player.getLifePoints(), Team.INITIAL_POINTS),
                String.format(OUTPUT_LIFE_POINTS, enemy.getLifePoints(), Team.INITIAL_POINTS)
        );
        displayWithPadding(
                String.format(OUTPUT_DECK_COUNT, player.getDeck().size(), Deck.INITIAL_SIZE),
                String.format(OUTPUT_DECK_COUNT, enemy.getDeck().size(), Deck.INITIAL_SIZE)
        );
        displayWithPadding(
                String.format(OUTPUT_BOARD_COUNT, game.getBoard().countUnits(TeamId.PLAYER), Hand.INITIAL_SIZE),
                String.format(OUTPUT_BOARD_COUNT, game.getBoard().countUnits(TeamId.ENEMY), Hand.INITIAL_SIZE)
        );

        renderBoard();
        renderSelectedUnitIfPresent();
    }

    private void displayWithPadding(String firstPart, String lastPart) {
        StringBuilder builder = new StringBuilder();
        String offset = PADDING.repeat(OFFSET_LENGTH);
        builder.append(offset);
        builder.append(firstPart);
        int paddingLength = OUTPUT_LINE_LENGTH - OFFSET_LENGTH - firstPart.length() - lastPart.length();
        String padding = PADDING.repeat(paddingLength);
        builder.append(padding);
        builder.append(lastPart);
        System.out.println(builder);
    }
}
