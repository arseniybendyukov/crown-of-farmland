package de.bendyukov.farm.cli;

import de.bendyukov.farm.models.Unit;
import de.bendyukov.farm.models.Verbosity;

import java.util.List;

/**
 * A data record representing the completely parsed configurations and inputs for the game.
 *
 * @param seed the RNG seed
 * @param boardSymbols optional list of characters representing the board
 * @param playerUnits the player's initial unit list
 * @param enemyUnits the enemy's initial unit list
 * @param playerName the player's team name
 * @param enemyName the enemy's team name
 * @param verbosity the rendering verbosity setting
 * 
 * @author Arseniy Bendyukov
 * @version 1.0
 */
public record CommandLineInput(
    int seed,
    List<Character> boardSymbols,
    List<Unit> playerUnits,
    List<Unit> enemyUnits,
    String playerName,
    String enemyName,
    Verbosity verbosity
) {
    /**
     * Constructs the command line input.
     *
     * @param seed the RNG seed
     * @param boardSymbols optional list of characters representing the board
     * @param playerUnits the player's initial unit list
     * @param enemyUnits the enemy's initial unit list
     * @param playerName the player's team name
     * @param enemyName the enemy's team name
     * @param verbosity the rendering verbosity setting
     */
    public CommandLineInput {
        if (boardSymbols != null) {
            boardSymbols = List.copyOf(boardSymbols);
        }

        if (playerUnits != null) {
            playerUnits = List.copyOf(playerUnits);
        }

        if (enemyUnits != null) {
            enemyUnits = List.copyOf(enemyUnits);
        }
    }
}
