package edu.kit.kastel.cli;

/**
 * Record defining the stats of a unit.
 *
 * @param qualifier the qualifier
 * @param role the role
 * @param atk the attack
 * @param def the defense
 * 
 * @author udkcf
 * @version 1.0
 */
record ParsedRawUnit(String qualifier, String role, int atk, int def) {
}
