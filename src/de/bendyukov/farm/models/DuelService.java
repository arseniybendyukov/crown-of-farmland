package de.bendyukov.farm.models;

import de.bendyukov.farm.models.results.DuelResult;
import de.bendyukov.farm.models.results.DuelUnit;
import de.bendyukov.farm.models.results.RegularDuelResult;
import de.bendyukov.farm.models.results.TeamDamage;

/**
 * Service responsible for duels between units.
 *
 * @author Arseniy Bendyukov
 * @version 1.0
 */
class DuelService {
    private final Game game;

    /**
     * Constructs a new duel service.
     *
     * @param game the game instance
     */
    DuelService(Game game) {
        this.game = game;
    }

    /**
     * Resolves a duel between an attacker and a defender.
     *
     * @param attacker the attacking unit
     * @param attackerCell the cell of the attacker
     * @param defender the defending unit
     * @param defenderCell the cell of the defender
     * @return a DuelResult containing the outcome of the duel
     */
    DuelResult duel(Unit attacker, Cell attackerCell, Unit defender, Cell defenderCell) {
        Team activeTeam = game.getActiveTeam();
        Team notActiveTeam = game.getOpponentTeam();
        Cell movedToCell = null;
        boolean isAttackerFlipped = false;
        boolean isDefenderFlipped = false;
        boolean isAttackerEliminated = false;
        boolean isDefenderEliminated = false;
        TeamDamage teamDamage = null;
        boolean hasAcsessToDefender = !defender.isHidden();
        if (attacker.isHidden()) {
            isAttackerFlipped = true;
            attacker.flip();
        }
        if (defender.isHidden()) {
            isDefenderFlipped = true;
            defender.flip();
        }
        if (defender.isBlocking()) {
            if (attacker.getAtk() > defender.getDef()) {
                game.getBoard().place(defenderCell, attacker);
                game.getBoard().clear(attackerCell);
                movedToCell = defenderCell;
                isDefenderEliminated = true;
            } else if (attacker.getAtk() < defender.getDef()) {
                int damage = defender.getDef() - attacker.getAtk();
                boolean lost = activeTeam.dealDamage(damage);
                if (lost) {
                    game.setWinner(notActiveTeam);
                }
                teamDamage = new TeamDamage(activeTeam.getName(), damage, lost);
            }
        } else if (defender.isKing()) {
            int damage = attacker.getAtk();
            boolean lost = notActiveTeam.dealDamage(damage);
            if (lost) {
                game.setWinner(activeTeam);
            }
            teamDamage = new TeamDamage(notActiveTeam.getName(), damage, lost);
        } else {
            RegularDuelResult regularDuelResult = resolveRegularDuel(attacker, attackerCell,
                    defender, defenderCell, activeTeam, notActiveTeam);
            movedToCell = regularDuelResult.movedToCell();
            isAttackerEliminated = regularDuelResult.isAttackerEliminated();
            isDefenderEliminated = regularDuelResult.isDefenderEliminated();
            teamDamage = regularDuelResult.teamDamage();
        }
        return new DuelResult(
                movedToCell,
                new DuelUnit(false, attacker.getName(), attacker.getAtk(), attacker.getDef(),
                        attackerCell, isAttackerFlipped, isAttackerEliminated, true),
                new DuelUnit(defender.isKing(), defender.getName(), defender.getAtk(), defender.getDef(),
                        defenderCell, isDefenderFlipped, isDefenderEliminated, hasAcsessToDefender),
                teamDamage
        );
    }

    private RegularDuelResult resolveRegularDuel(
            Unit attacker,
            Cell attackerCell,
            Unit defender,
            Cell defenderCell,
            Team activeTeam,
            Team notActiveTeam
    ) {
        Cell movedToCell = null;
        boolean isAttackerEliminated = false;
        boolean isDefenderEliminated = false;
        TeamDamage teamDamage = null;

        if (attacker.getAtk() > defender.getAtk()) {
            game.getBoard().place(defenderCell, attacker);
            game.getBoard().clear(attackerCell);
            movedToCell = defenderCell;
            isDefenderEliminated = true;

            int damage = attacker.getAtk() - defender.getAtk();
            boolean lost = notActiveTeam.dealDamage(damage);
            if (lost) {
                game.setWinner(activeTeam);
            }
            teamDamage = new TeamDamage(notActiveTeam.getName(), damage, lost);
        } else if (defender.getAtk() > attacker.getAtk()) {
            game.getBoard().clear(attackerCell);
            isAttackerEliminated = true;

            int damage = defender.getAtk() - attacker.getAtk();
            boolean lost = activeTeam.dealDamage(damage);
            if (lost) {
                game.setWinner(notActiveTeam);
            }
            teamDamage = new TeamDamage(activeTeam.getName(), damage, lost);
        } else {
            game.getBoard().clear(attackerCell);
            game.getBoard().clear(defenderCell);
            isAttackerEliminated = true;
            isDefenderEliminated = true;
        }

        return new RegularDuelResult(movedToCell, isAttackerEliminated, isDefenderEliminated, teamDamage);
    }
}
