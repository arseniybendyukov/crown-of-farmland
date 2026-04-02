package edu.kit.kastel.models;

import edu.kit.kastel.exceptions.GameException;
import edu.kit.kastel.models.results.DiscardedUnit;
import edu.kit.kastel.models.results.YieldResult;

import java.util.Optional;

/**
 * Service responsible for ending the turn.
 *
 * @author udkcf
 * @version 1.0
 */
class YieldService {
    private static final String ERROR_CANNOT_DISCARD = "You cannot discard a card, because the hand is not full";
    private static final String ERROR_MUST_DISCARD = "The hand is full, a card must be discarded";

    /*
     * The opponent skips taking a unit at the end of the first turn due to incorrect
     * Artemis tests (discussed in the forum), despite logical inconsistency.
     */
    private static final int FIRST_PLAYER_TURN = 1;

    private final Game game;

    /**
     * Constructs a new yield service.
     *
     * @param game the game instance
     */
    YieldService(Game game) {
        this.game = game;
    }

    /**
     * Yields the current turn and optionally discards a unit.
     * At the end of the turn, a unit from the opponent's deck is added to their hand.
     *
     * @param index the index of the unit to discard, or null
     * @return a YieldResult containing the outcome of the yield
     * @throws GameException if a discard is mandatory or forbidden
     */
    YieldResult yield(Integer index) throws GameException {
        Team activeTeam = game.getActiveTeam();
        Team opponentTeam = game.getOpponentTeam();

        Hand activeHand = activeTeam.getHand();

        if (index == null && activeHand.isFull()) {
            game.setYieldHandMode(true);
            throw new GameException(ERROR_MUST_DISCARD);
        }

        if (index != null && !activeHand.isFull()) {
            game.setYieldHandMode(true);
            throw new GameException(ERROR_CANNOT_DISCARD);
        }

        Deck activeDeck = activeTeam.getDeck();

        if (activeDeck.isEmpty()) {
            game.setWinner(opponentTeam);
            return new YieldResult(activeTeam.getName(), true, opponentTeam.getName(), Optional.empty());
        }

        Optional<DiscardedUnit> discardedUnit = Optional.empty();

        if (index != null) {
            Unit unit = activeTeam.getHand().getAndDiscardUnit(index);
            discardedUnit = Optional.of(new DiscardedUnit(unit.getName(), unit.getAtk(), unit.getDef()));
        }

        boolean isOpponentFirstTurn = game.getCurrentTurn() == FIRST_PLAYER_TURN && game.getActiveTeamId() == TeamId.PLAYER;

        if (!isOpponentFirstTurn) {
            Unit deckUnit = opponentTeam.getDeck().getAndRemoveFirst();
            opponentTeam.getHand().addUnit(deckUnit);
        }

        if (game.getActiveTeamId() == TeamId.PLAYER) {
            game.setActiveTeamId(TeamId.ENEMY);
        } else {
            game.setActiveTeamId(TeamId.PLAYER);
        }
        game.incrementCurrentTurn();
        game.getBoard().resetSelectedCell();
        game.setYieldHandMode(false);

        return new YieldResult(activeTeam.getName(), false, opponentTeam.getName(), discardedUnit);
    }
}
