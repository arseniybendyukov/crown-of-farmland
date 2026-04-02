package edu.kit.kastel.commands;

import edu.kit.kastel.exceptions.InvalidCommandArgumentException;
import edu.kit.kastel.models.Game;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Implement the command handler that manages
 * the registration, parsing, and execution of available commands.
 *
 * @author udkcf
 * @author Programmieren-Team
 * @version 1.0
 */
public class CommandHandler {
    private static final String COMMAND_DELIMITER_REGEX = "\\s+";
    private static final String COMMAND_DELIMITER_REPLACEMENT = " ";
    private static final String COMMAND_NOT_FOUND_ERROR = "ERROR: Command '%s' not recognised by any pattern" + System.lineSeparator();
    private static final int COMMAND_ARGUMENTS_START_INDEX = 1;

    private final Game game;
    private final Map<String, Command> commands;
    private boolean running = false;


    /**
     * Initializes the command handler and registers all available commands.
     *
     * @param game the game instance
     */
    public CommandHandler(Game game) {
        this.game = game;
        this.commands = new HashMap<>();
        this.initCommands();
    }

    /**
     * This method handles the input of the user.
     * The input is taken so long, as this (command handler) was not stopped by the quit command.
     */
    public void handleUserInput() {
        this.running = true;

        try (Scanner scanner = new Scanner(System.in)) {
            while (running) {
                executeCommand(scanner.nextLine());
            }

        }
    }

    /**
     * Ends the program.
     */
    public void quit() {
        this.running = false;
    }

    private void executeCommand(String inputString) {
        String strippedInput = inputString.strip().replaceAll(COMMAND_DELIMITER_REGEX, COMMAND_DELIMITER_REPLACEMENT);
        String[] splitCommand = strippedInput.split(COMMAND_DELIMITER_REGEX);
        String[] commandArguments = Arrays.copyOfRange(splitCommand, COMMAND_ARGUMENTS_START_INDEX, splitCommand.length);
        for (Command command : commands.values()) {
            if (strippedInput.matches(command.getCommandRegex())) {
                try {
                    command.execute(commandArguments);
                } catch (InvalidCommandArgumentException exception) {
                    System.err.println(exception.getMessage());
                }
                return;
            }
        }
        System.err.printf(COMMAND_NOT_FOUND_ERROR, inputString);
    }

    private void initCommands() {
        this.addCommand(new BlockCommand(this, game));
        this.addCommand(new BoardCommand(this, game));
        this.addCommand(new FlipCommand(this, game));
        this.addCommand(new HandCommand(this, game));
        this.addCommand(new MoveCommand(this, game));
        this.addCommand(new PlaceCommand(this, game));
        this.addCommand(new QuitCommand(this, game));
        this.addCommand(new SelectCommand(this, game));
        this.addCommand(new ShowCommand(this, game));
        this.addCommand(new StateCommand(this, game));
        this.addCommand(new YieldCommand(this, game));
    }

    private void addCommand(Command command) {
        this.commands.put(command.getCommandName(), command);
    }
}
