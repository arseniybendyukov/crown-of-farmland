package de.bendyukov.farm.cli;

import de.bendyukov.farm.models.Unit;

import java.util.List;

/**
 * Wrapper for the player's and enemy's composed unit lists.
 *
 * @param playerUnits the player's unit list
 * @param enemyUnits  the enemy's unit list
 * 
 * @author Arseniy Bendyukov
 * @version 1.0
 */
record ParsedDecks(List<Unit> playerUnits, List<Unit> enemyUnits) {
    /**
     * Constructs the parsed decks.
     *
     * @param playerUnits the player's unit list
     * @param enemyUnits the enemy's unit list
     */
    public ParsedDecks {
        if (playerUnits != null) {
            playerUnits = List.copyOf(playerUnits);
        }

        if (enemyUnits != null) {
            enemyUnits = List.copyOf(enemyUnits);
        }
    }
}
