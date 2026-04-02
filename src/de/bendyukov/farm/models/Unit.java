package de.bendyukov.farm.models;

import de.bendyukov.farm.models.results.Compatibility;

/**
 * Represents a game unit (including the king).
 *
 * @author Arseniy Bendyukov
 * @version 1.0
 */
public class Unit {
    private static final String NAME_SEPARATOR = " ";
    private static final String KING_QUALIFIER = "Farmer";
    private static final String KING_ROLE = "King";
    private static final int KING_ATK = 0;
    private static final int KING_DEF = 0;
    private static final int NO_PLACEMENT_ORDER = -1;

    private final TeamId teamId;
    private final String qualifier;
    private final String role;
    private final int atk;
    private final int def;
    private final boolean isKing;
    private int lastMoveTurn;
    private boolean isHidden;
    private boolean isBlocking = false;
    private int placementOrder = NO_PLACEMENT_ORDER;


    /**
     * Constructs a king.
     *
     * @param teamId the team identity of the king
     */
    public Unit(TeamId teamId) {
        this.isKing = true;
        this.teamId = teamId;
        this.qualifier = KING_QUALIFIER;
        this.role = KING_ROLE;
        this.atk = KING_ATK;
        this.def = KING_DEF;
        this.isHidden = false;
    }

    /**
     * Constructs a standard unit.
     *
     * @param teamId the team identity
     * @param qualifier the qualifier name
     * @param role the role name
     * @param atk the attack value
     * @param def the defense value
     */
    public Unit(TeamId teamId, String qualifier, String role, int atk, int def) {
        this.isKing = false;
        this.teamId = teamId;
        this.qualifier = qualifier;
        this.role = role;
        this.atk = atk;
        this.def = def;
        this.isHidden = true;
    }

    /**
     * Gets the placement order of this unit.
     *
     * @return the placement order
     */
    public int getPlacementOrder() {
        return placementOrder;
    }

    /**
     * Sets the placement order of this unit if not already set.
     *
     * @param placementOrder the order to assign
     */
    public void setPlacementOrder(int placementOrder) {
        if (this.placementOrder == NO_PLACEMENT_ORDER) {
            this.placementOrder = placementOrder;
        }
    }

    /**
     * Gets the team identity of this unit.
     *
     * @return the team ID
     */
    public TeamId getTeamId() {
        return teamId;
    }

    /**
     * Checks if the unit can move on the current turn.
     *
     * @param currentTurn the current turn number
     * @return true if the unit can move, false if already moved this turn
     */
    public boolean canMove(int currentTurn) {
        return currentTurn != lastMoveTurn;
    }

    /**
     * Updates the last move turn of this unit.
     *
     * @param lastMoveTurn the turn number
     */
    public void setLastMoveTurn(int lastMoveTurn) {
        this.lastMoveTurn = lastMoveTurn;
    }

    private String getQualifier() {
        return qualifier;
    }

    private String getRole() {
        return role;
    }

    /**
     * Gets the attack of the unit.
     *
     * @return the attack
     */
    public int getAtk() {
        return atk;
    }

    /**
     * Gets the defense of the unit.
     *
     * @return the defense
     */
    public int getDef() {
        return def;
    }

    /**
     * Checks if the unit is hidden.
     *
     * @return true if hidden, false otherwise
     */
    public boolean isHidden() {
        return isHidden;
    }

    /**
     * Checks if this unit is a king.
     *
     * @return true if king, false otherwise
     */
    public boolean isKing() {
        return isKing;
    }

    /**
     * Flips the unit to become not hidden.
     */
    public void flip() {
        isHidden = false;
    }

    /**
     * Checks if the unit is blocking.
     *
     * @return true if blocking, false otherwise
     */
    public boolean isBlocking() {
        return isBlocking;
    }

    /**
     * Sets the unit to a blocking state.
     */
    public void block() {
        isBlocking = true;
    }

    /**
     * Sets the unit to a non-blocking state.
     */
    public void unblock() {
        isBlocking = false;
    }

    /**
     * Gets the full combined name of the unit (Qualifier + Role).
     *
     * @return the unit name
     */
    public String getName() {
        return qualifier + NAME_SEPARATOR + role;
    }

    /**
     * Calculates the merge compatibility between this unit and another unit.
     *
     * @param other the unit to check compatibility with
     * @return a Compatibility result
     */
    public Compatibility getCompatibility(Unit other) {
        if (getName().equals(other.getName())) {
            return Compatibility.incompatible();
        }

        int otherAtk = other.getAtk();
        int otherDef = other.getDef();

        if (atk > otherAtk && atk == otherDef && otherAtk == def) {
            return Compatibility.compatible(atk, otherDef);
        }

        int maxGcd = Math.max(MathUtils.gcd(atk, otherAtk), MathUtils.gcd(def, otherDef));
        int minGcd = MathUtils.MIN_GCD;

        if (maxGcd > minGcd) {
            return Compatibility.compatible(atk + otherAtk - maxGcd, def + otherDef - maxGcd);
        }

        if (maxGcd == minGcd
                && (MathUtils.isPrime(atk / minGcd) && MathUtils.isPrime(otherAtk / minGcd)
                || MathUtils.isPrime(def / minGcd) && MathUtils.isPrime(otherDef / minGcd))
        ) {
            return Compatibility.compatible(atk + otherAtk, def + otherDef);
        }

        return Compatibility.incompatible();
    }

    /**
     * Merges this unit with another unit considering the Compatibility result.
     *
     * @param other the unit to merge with
     * @param atk the new attack
     * @param def the new defense
     * @param placementOrder the new placement order
     * @return the merged unit
     */
    public Unit merge(Unit other, int atk, int def, int placementOrder) {
        Unit mergedUnit = new Unit(teamId, other.getQualifier() + NAME_SEPARATOR + qualifier, other.getRole(), atk, def);
        mergedUnit.setPlacementOrder(placementOrder);
        return mergedUnit;
    }
}
