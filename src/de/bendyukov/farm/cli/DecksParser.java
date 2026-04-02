package de.bendyukov.farm.cli;

import de.bendyukov.farm.exceptions.CommandLineParseException;
import de.bendyukov.farm.models.Deck;
import de.bendyukov.farm.models.TeamId;
import de.bendyukov.farm.models.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Parses the base units and deck counts to create the initial decks for both teams.
 *
 * @author Arseniy Bendyukov
 * @version 1.0
 */
class DecksParser {
    private static final String UNITS_SEPARATOR = ";";
    private static final int EXPECTED_UNIT_PARTS_COUNT = 4;
    private static final String ERROR_UNEXPECTED_UNIT_PARTS_COUNT =
            "Units must consist of qualifier, role, ATK and DEF (separated with ';')";
    private static final int QUALIFIER_INDEX = 0;
    private static final int ROLE_INDEX = 1;
    private static final int ATK_INDEX = 2;
    private static final int DEF_INDEX = 3;
    private static final String ERROR_INVALID_ATK_DEF = "ATK and DEF must be non-negative integers";
    private static final int MAX_UNITS = 80;
    private static final String ERROR_TOO_MANY_UNITS = "Too many units: %s, expected max: %s";
    private static final String ERROR_DECK_REQUIRED = "Either deck or deck1 and deck2 must be specified (but not both)";
    private static final String ERROR_INVALID_UNIT_COUNTS = "Unit counts must be non-negative integers";
    private static final String ERROR_INVALID_DECK = "The number of units does not match unit counts";
    private static final String ERROR_PLAYER_WRONG_INIT_SIZE = "Wrong player's deck size: %d, expected %d";
    private static final String ERROR_ENEMY_WRONG_INIT_SIZE = "Wrong enemy's deck size: %d, expected %d";
    private static final int MIN_STAT_VALUE = 0;
    private static final int MIN_UNIT_COUNT = 0;

    private final Map<CommandLineArguments, String> argsMap;

    /**
     * Constructs the deck parser.
     *
     * @param argsMap the command line arguments
     */
    DecksParser(Map<CommandLineArguments, String> argsMap) {
        this.argsMap = Map.copyOf(argsMap);
    }

    /**
     * Reads the units file and deck counts file to compose the initial decks.
     *
     * @return a wrapper around the player's and enemy's unit decks
     * @throws CommandLineParseException if file contents are invalid or missing
     */
    public ParsedDecks parse() throws CommandLineParseException {
        List<ParsedRawUnit> rawUnits = parseRawUnits();

        ParsedUnitCounts unitCounts = parseUnitCounts(rawUnits.size());
        List<Integer> playerUnitCounts = unitCounts.playerUnitCounts();
        List<Integer> enemyUnitCounts = unitCounts.enemyUnitCounts();

        return composeDecks(rawUnits, playerUnitCounts, enemyUnitCounts);
    }

    private List<ParsedRawUnit> parseRawUnits() throws CommandLineParseException {
        List<ParsedRawUnit> rawUnits = new ArrayList<>();

        for (String line : FileReaderUtil.readAndPrint(argsMap.get(CommandLineArguments.UNITS))) {
            String[] parts = line.split(UNITS_SEPARATOR);

            if (parts.length != EXPECTED_UNIT_PARTS_COUNT) {
                throw new CommandLineParseException(ERROR_UNEXPECTED_UNIT_PARTS_COUNT);
            }

            String qualifier = parts[QUALIFIER_INDEX];
            String role = parts[ROLE_INDEX];
            String atkString = parts[ATK_INDEX];
            String defString = parts[DEF_INDEX];

            int atk;
            int def;

            try {
                atk = Integer.parseInt(atkString);
                def = Integer.parseInt(defString);
            } catch (NumberFormatException e) {
                throw new CommandLineParseException(ERROR_INVALID_ATK_DEF);
            }

            if (atk < MIN_STAT_VALUE || def < MIN_STAT_VALUE) {
                throw new CommandLineParseException(ERROR_INVALID_ATK_DEF);
            }

            rawUnits.add(new ParsedRawUnit(qualifier, role, atk, def));
        }

        if (rawUnits.size() > MAX_UNITS) {
            throw new CommandLineParseException(String.format(ERROR_TOO_MANY_UNITS, rawUnits.size(), MAX_UNITS));
        }

        return rawUnits;
    }

    private ParsedUnitCounts parseUnitCounts(int expectedTotal) throws CommandLineParseException {
        boolean hasOneDeck = argsMap.containsKey(CommandLineArguments.DECK);
        boolean hasTwoDecks = argsMap.containsKey(CommandLineArguments.DECK1) && argsMap.containsKey(CommandLineArguments.DECK2);

        List<Integer> playerUnitCounts;
        List<Integer> enemyUnitCounts;

        if (hasOneDeck && !hasTwoDecks) {
            List<Integer> counts = readAndPrintCounts(argsMap.get(CommandLineArguments.DECK));
            playerUnitCounts = List.copyOf(counts);
            enemyUnitCounts = List.copyOf(counts);
        } else if (!hasOneDeck && hasTwoDecks) {
            playerUnitCounts = readAndPrintCounts(argsMap.get(CommandLineArguments.DECK1));
            enemyUnitCounts = readAndPrintCounts(argsMap.get(CommandLineArguments.DECK2));
        } else {
            throw new CommandLineParseException(ERROR_DECK_REQUIRED);
        }

        if (playerUnitCounts.size() != expectedTotal || enemyUnitCounts.size() != expectedTotal) {
            throw new CommandLineParseException(ERROR_INVALID_DECK);
        }

        return new ParsedUnitCounts(playerUnitCounts, enemyUnitCounts);
    }

    private ParsedDecks composeDecks(
        List<ParsedRawUnit> rawUnits,
        List<Integer> playerUnitCounts,
        List<Integer> enemyUnitCounts
    ) throws CommandLineParseException {
        List<Unit> playerUnits = new ArrayList<>();
        List<Unit> enemyUnits = new ArrayList<>();

        for (int i = 0; i < rawUnits.size(); i++) {
            ParsedRawUnit rawUnit = rawUnits.get(i);

            String qualifier = rawUnit.qualifier();
            String role = rawUnit.role();
            int atk = rawUnit.atk();
            int def = rawUnit.def();

            int playerUnitCount = playerUnitCounts.get(i);
            for (int j = 0; j < playerUnitCount; j++) {
                playerUnits.add(new Unit(TeamId.PLAYER, qualifier, role, atk, def));
            }

            int enemyUnitCount = enemyUnitCounts.get(i);
            for (int j = 0; j < enemyUnitCount; j++) {
                enemyUnits.add(new Unit(TeamId.ENEMY, qualifier, role, atk, def));
            }
        }

        if (playerUnits.size() != Deck.INITIAL_SIZE) {
            throw new CommandLineParseException(String.format(ERROR_PLAYER_WRONG_INIT_SIZE, playerUnits.size(), Deck.INITIAL_SIZE));
        }

        if (enemyUnits.size() != Deck.INITIAL_SIZE) {
            throw new CommandLineParseException(String.format(ERROR_ENEMY_WRONG_INIT_SIZE, enemyUnits.size(), Deck.INITIAL_SIZE));
        }

        return new ParsedDecks(playerUnits, enemyUnits);
    }

    private static List<Integer> readAndPrintCounts(String path) throws CommandLineParseException {
        List<Integer> counts = new ArrayList<>();
        for (String line : FileReaderUtil.readAndPrint(path)) {
            int count;
            try {
                count = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                throw new CommandLineParseException(ERROR_INVALID_UNIT_COUNTS);
            }
            if (count < MIN_UNIT_COUNT) {
                throw new CommandLineParseException(ERROR_INVALID_UNIT_COUNTS);
            }
            counts.add(count);
        }
        return counts;
    }
}
