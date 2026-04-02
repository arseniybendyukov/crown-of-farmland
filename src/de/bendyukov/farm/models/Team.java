package de.bendyukov.farm.models;

import java.util.List;
import java.util.Random;

/**
 * Represents a playing team.
 *
 * @author Arseniy Bendyukov
 * @version 1.0
 */
public class Team {
    /** The initial amount of life points. */
    public static final int INITIAL_POINTS = 8000;
    
    private final String name;
    private final Deck deck;
    private final Hand hand;
    private int lifePoints = INITIAL_POINTS;
    private Cell kingCell;
    private int lastPlaceTurn;

    /**
     * Constructs a new team and initializes its deck and hand.
     *
     * @param name the name of the team
     * @param units the list of units for the deck
     * @param kingCell the starting cell of the team's king
     * @param rng the random generator to shuffle the deck
     */
    public Team(String name, List<Unit> units, Cell kingCell, Random rng) {
        this.name = name;
        this.kingCell = kingCell;
        this.deck = new Deck(units);
        deck.shuffle(rng);
        this.hand = new Hand();

        for (int i = 0; i < Hand.INITIAL_SIZE; i++) {
            hand.addUnit(deck.getAndRemoveFirst());
        }
    }

    /**
     * Checks if the team can place a unit on the given turn.
     *
     * @param currentTurn the current game turn
     * @return true if the team can place, false otherwise
     */
    public boolean canPlace(int currentTurn) {
        return currentTurn != lastPlaceTurn;
    }

    /**
     * Sets the turn in which the team last placed a unit.
     *
     * @param lastPlaceTurn the turn index
     */
    public void setLastPlaceTurn(int lastPlaceTurn) {
        this.lastPlaceTurn = lastPlaceTurn;
    }

    /**
     * Gets the name of the team.
     *
     * @return the team name
     */
    public String getName() {
        return name;
    }

    /**
     * Deals damage to the team's life points and checks if they have lost.
     *
     * @param damage the amount of damage to deal
     * @return true if the team's life points dropped to zero or below (lost), false otherwise
     */
    public boolean dealDamage(int damage) {
        if (damage >= lifePoints) {
            return true;
        }
        lifePoints -= damage;
        return false;
    }

    /**
     * Gets the current cell of the team's king.
     *
     * @return the king's cell
     */
    public Cell getKingCell() {
        return kingCell;
    }

    /**
     * Sets a new cell location for the team's king.
     *
     * @param kingCell the new king cell
     */
    public void setKingCell(Cell kingCell) {
        this.kingCell = kingCell;
    }

    /**
     * Gets the team's hand.
     *
     * @return the hand
     */
    public Hand getHand() {
        return hand;
    }

    /**
     * Gets the team's deck.
     *
     * @return the deck
     */
    public Deck getDeck() {
        return deck;
    }

    /**
     * Gets the current life points of the team.
     *
     * @return the life points
     */
    public int getLifePoints() {
        return lifePoints;
    }
}
