package de.bendyukov.farm.cli;

import java.util.List;

/**
 * Record tracking the unit counts for both teams.
 *
 * @param playerUnitCounts counts of units for the player
 * @param enemyUnitCounts counts of units for the enemy
 * 
 * @author Arseniy Bendyukov
 * @version 1.0
 */
record ParsedUnitCounts(List<Integer> playerUnitCounts, List<Integer> enemyUnitCounts) {
    /**
     * Constructs the parsed unit counts.
     *
     * @param playerUnitCounts counts of units for the player
     * @param enemyUnitCounts counts of units for the enemy
     */
    public ParsedUnitCounts {
        if (playerUnitCounts != null) {
            playerUnitCounts = List.copyOf(playerUnitCounts);
        }

        if (enemyUnitCounts != null) {
            enemyUnitCounts = List.copyOf(enemyUnitCounts);
        }
    }
}
