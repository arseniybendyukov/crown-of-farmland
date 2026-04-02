package de.bendyukov.farm.cli;

import de.bendyukov.farm.exceptions.CommandLineParseException;
import de.bendyukov.farm.models.Unit;
import de.bendyukov.farm.models.Verbosity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main parser of the initial application arguments into a standardized CommandLineInput.
 *
 * @author Arseniy Bendyukov
 * @version 1.0
 */
public class CommandLineParser {
    private static final String KEY_VALUE_SEPARATOR = "=";
    private static final int KEY_VALUE_SEPARATOR_LIMIT = 2;
    private static final int KEY_INDEX = 0;
    private static final int VALUE_INDEX = 1;
    private static final String DEFAULT_PLAYER_NAME = "Player";
    private static final String DEFAULT_ENEMY_NAME = "Enemy";
    private static final String ERROR_UNKNOWN_ARGUMENT = "Unknown argument";
    private static final String ERROR_DUPLICATE_ARGUMENT = "Duplicate arguments are forbidden";
    private static final String ERROR_SEED_REQUIRED = "Seed is required";
    private static final String ERROR_INVALID_SEED = "Seed must be a valid integer";
    private static final int MAX_NAME_LENGTH = 14;
    private static final String ERROR_TOO_LONG_NAME = "Too long team's name";
    private static final String ERROR_INVALID_VERBOSITY = "Verbosity can be either all or compact";
    private static final String ERROR_UNITS_REQUIRED = "File with units is required";
    private static final String ERROR_DECK_REQUIRED = "Either deck or deck1 and deck2 must be specified (but not both)";

    private final Map<CommandLineArguments, String> argsMap;

    /**
     * Constructs the parser and initially checks for unrecognized keys and duplicate arguments.
     *
     * @param args the string arguments array given to main
     * @throws CommandLineParseException if args contain unrecognized keys or duplicate arguments
     */
    public CommandLineParser(String[] args) throws CommandLineParseException {
        this.argsMap = buildArgsMap(args);
        validateRequiredArgs();
    }

    /**
     * Parses the prepared argument map into a CommandLineInput record.
     *
     * @return the CommandLineInput record
     * @throws CommandLineParseException if required arguments are missing or formatting is invalid
     */
    public CommandLineInput parse() throws CommandLineParseException {
        int seed = parseSeed();

        BoardSymbolsParser boardSymbolsParser = new BoardSymbolsParser(argsMap);
        List<Character> boardSymbols = boardSymbolsParser.parse();

        DecksParser decksParser = new DecksParser(argsMap);
        ParsedDecks decks = decksParser.parse();
        List<Unit> playerUnits = decks.playerUnits();
        List<Unit> enemyUnits = decks.enemyUnits();

        String playerName = parseName(CommandLineArguments.PLAYER_NAME, DEFAULT_PLAYER_NAME);

        String enemyName = parseName(CommandLineArguments.ENEMY_NAME, DEFAULT_ENEMY_NAME);

        Verbosity verbosity = parseVerbosity();

        return new CommandLineInput(seed, boardSymbols, playerUnits, enemyUnits, playerName, enemyName, verbosity);
    }

    private static Map<CommandLineArguments, String> buildArgsMap(String[] args) throws CommandLineParseException {
        Map<CommandLineArguments, String> argsMap = new HashMap<>();

        for (String arg : args) {
            String[] parts = arg.split(KEY_VALUE_SEPARATOR, KEY_VALUE_SEPARATOR_LIMIT);
            String key = parts[KEY_INDEX];
            String value = parts[VALUE_INDEX];

            CommandLineArguments commandLineArgument = CommandLineArguments.fromString(key)
                    .orElseThrow(() -> new CommandLineParseException(ERROR_UNKNOWN_ARGUMENT));

            if (argsMap.containsKey(commandLineArgument)) {
                throw new CommandLineParseException(ERROR_DUPLICATE_ARGUMENT);
            }

            argsMap.put(commandLineArgument, value);
        }

        return argsMap;
    }

    private int parseSeed() throws CommandLineParseException {
        try {
            return Integer.parseInt(argsMap.get(CommandLineArguments.SEED));
        } catch (NumberFormatException e) {
            throw new CommandLineParseException(ERROR_INVALID_SEED);
        }
    }

    private String parseName(CommandLineArguments key, String defaultName) throws CommandLineParseException {
        String name = argsMap.getOrDefault(key, defaultName);

        if (name.length() > MAX_NAME_LENGTH) {
            throw new CommandLineParseException(ERROR_TOO_LONG_NAME);
        }

        return name;
    }

    private Verbosity parseVerbosity() throws CommandLineParseException {
        if (!argsMap.containsKey(CommandLineArguments.VERBOSITY)) {
            return Verbosity.ALL;
        }

        return Verbosity.fromString(argsMap.get(CommandLineArguments.VERBOSITY))
                .orElseThrow(() -> new CommandLineParseException(ERROR_INVALID_VERBOSITY));
    }

    private void validateRequiredArgs() throws CommandLineParseException {
        if (!argsMap.containsKey(CommandLineArguments.SEED)) {
            throw new CommandLineParseException(ERROR_SEED_REQUIRED);
        }

        if (!argsMap.containsKey(CommandLineArguments.UNITS)) {
            throw new CommandLineParseException(ERROR_UNITS_REQUIRED);
        }

        boolean hasOneDeck = argsMap.containsKey(CommandLineArguments.DECK);
        boolean hasTwoDecks = argsMap.containsKey(CommandLineArguments.DECK1) && argsMap.containsKey(CommandLineArguments.DECK2);

        if (!(hasOneDeck && !hasTwoDecks || !hasOneDeck && hasTwoDecks)) {
            throw new CommandLineParseException(ERROR_DECK_REQUIRED);
        }
    }
}
