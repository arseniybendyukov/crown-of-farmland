package de.bendyukov.farm;

import de.bendyukov.farm.cli.CommandLineInput;
import de.bendyukov.farm.cli.CommandLineParser;
import de.bendyukov.farm.commands.CommandHandler;
import de.bendyukov.farm.exceptions.CommandLineParseException;
import de.bendyukov.farm.models.Game;

/**
 * This is the main entry class for the program.
 *
 * @author Arseniy Bendyukov
 * @version 1.0
 */
public final class Main {
    private static final String GAME_START_MESSAGE = "Use one of the following commands: "
            + "select, board, move, flip, block, hand, place, show, yield, state, quit.";

    private Main() {
    }

    /**
     * Entry point of the application.
     * Parses command line arguments and initializes the game and command handler.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        CommandLineInput input;
        try {
            CommandLineParser parser = new CommandLineParser(args);
            input = parser.parse();
        } catch (CommandLineParseException exception) {
            System.err.println(exception.getMessage());
            return;
        }

        Game game = new Game(
                input.seed(),
                input.boardSymbols(),
                input.playerUnits(),
                input.enemyUnits(),
                input.playerName(),
                input.enemyName(),
                input.verbosity()
        );

        System.out.println(GAME_START_MESSAGE);

        CommandHandler handler = new CommandHandler(game);
        handler.handleUserInput();
    }
}
