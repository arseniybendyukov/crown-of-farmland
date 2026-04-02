package de.bendyukov.farm.commands;

import de.bendyukov.farm.models.Game;

/**
 * Implements the quit command.
 *
 * @author Arseniy Bendyukov
 * @author Programmieren-Team
 * @version 1.0
 */
public class QuitCommand extends Command {
    private static final String COMMAND_NAME = "quit";

    /**
     * Creates a new quit command object.
     * @param commandHandler The command handler
     * @param game The Game
     */
    protected QuitCommand(CommandHandler commandHandler, Game game) {
        super(COMMAND_NAME, COMMAND_NAME, commandHandler, game);
    }

    /**
     * Executes the quit command, immediately stopping the command handler.
     *
     * @param commandArguments the command arguments
     */
    @Override
    public void execute(String[] commandArguments) {
        commandHandler.quit();
    }
}
