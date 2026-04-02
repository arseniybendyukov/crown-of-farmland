package de.bendyukov.farm.cli;

import de.bendyukov.farm.exceptions.CommandLineParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Parses the configuration file containing symbols that represent the board.
 *
 * @author Arseniy Bendyukov
 * @version 1.0
 */
class BoardSymbolsParser {
    private static final int EXPECTED_BOARD_SYMBOLS_LINES_COUNT = 1;
    private static final String ERROR_INVALID_BOARD_SYMBOLS_LINES_COUNT = "The board file must contain exactly one line";
    private static final int EXPECTED_BOARD_SYMBOLS_COUNT = 29;
    private static final String ERROR_INVALID_BOARD_SYMBOLS_COUNT = "The board file must contain exactly 29 characters";

    private final Map<CommandLineArguments, String> argsMap;

    /**
     * Constructs the board symbols parser.
     *
     * @param argsMap the parsed command-line key-value mapping
     */
    BoardSymbolsParser(Map<CommandLineArguments, String> argsMap) {
        this.argsMap = Map.copyOf(argsMap);
    }

    /**
     * Parses the board symbols file into a list of characters.
     *
     * @return the list of custom characters, or null if the argument was not provided
     * @throws CommandLineParseException if the symbols file violates formatting rules
     */
    public List<Character> parse() throws CommandLineParseException {
        if (!argsMap.containsKey(CommandLineArguments.BOARD_SYMBOLS)) {
            return null;
        }

        List<String> lines = FileReaderUtil.readAndPrint(argsMap.get(CommandLineArguments.BOARD_SYMBOLS));

        if (lines.size() != EXPECTED_BOARD_SYMBOLS_LINES_COUNT) {
            throw new CommandLineParseException(ERROR_INVALID_BOARD_SYMBOLS_LINES_COUNT);
        }

        String line = lines.getFirst();

        if (line.length() != EXPECTED_BOARD_SYMBOLS_COUNT) {
            throw new CommandLineParseException(ERROR_INVALID_BOARD_SYMBOLS_COUNT);
        }

        List<Character> characters = new ArrayList<>();

        for (int i = 0; i < line.length(); i++) {
            characters.add(line.charAt(i));
        }

        return characters;
    }
}
