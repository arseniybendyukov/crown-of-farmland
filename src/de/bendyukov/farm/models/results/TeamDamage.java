package de.bendyukov.farm.models.results;

/**
 * Represents damage dealt to a team's life points.
 *
 * @param teamName the name of the damaged team
 * @param damage the amount of damage dealt
 * @param lost true if the team lost because of this damage
 * 
 * @author Arseniy Bendyukov
 * @version 1.0
 */
public record TeamDamage(String teamName, int damage, boolean lost) {
}
