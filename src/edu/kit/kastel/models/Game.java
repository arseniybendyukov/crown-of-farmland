package edu.kit.kastel.models;

import edu.kit.kastel.exceptions.GameException;
import edu.kit.kastel.models.results.MoveResult;
import edu.kit.kastel.models.results.PlaceResult;
import edu.kit.kastel.models.results.YieldResult;

import java.util.List;

/**
 * Represents the main game state orchestrator.
 *
 * @author udkcf
 * @version 1.0
 */
public class Game {
    private static final String ERROR_SELECTED_UNIT_REQUIRED = "A unit must be selected";
    private static final String ERROR_ACCESS_DENIED = "The unit belongs to another team";
    private static final String ERROR_UNIT_ACTION_FORBIDDEN = "Action forbidden: The unit has already moved during this turn";

    private static final int INITIAL_TURN = 1;
    private static final int INITIAL_PLACEMENT_ORDER = 0;
    private static final Cell PLAYER_KING_START = new Cell(Column.D, Row.ONE);
    private static final Cell ENEMY_KING_START = new Cell(Column.D, Row.SEVEN);

    private final Board board = new Board();
    private final Team player;
    private final Team enemy;
    private final Config config;
    private int currentTurn = INITIAL_TURN;
    private TeamId activeTeamId = TeamId.PLAYER;
    private boolean yieldHandMode = false;
    private Team winner = null;
    private int nextPlacementOrder = INITIAL_PLACEMENT_ORDER;

    /**
     * Initializes a new game.
     *
     * @param seed         the random seed
     * @param boardSymbols the symbols representing the board
     * @param playerUnits  the list of units for the player's deck
     * @param enemyUnits   the list of units for the enemy's deck
     * @param playerName   the player's name
     * @param enemyName    the enemy's name
     * @param verbosity    the verbosity level
     */
    public Game(
            int seed,
            List<Character> boardSymbols,
            List<Unit> playerUnits,
            List<Unit> enemyUnits,
            String playerName,
            String enemyName,
            Verbosity verbosity
    ) {
        this.config = new Config(verbosity, seed, boardSymbols);

        Unit playerKing = new Unit(TeamId.PLAYER);
        Unit enemyKing = new Unit(TeamId.ENEMY);

        board.place(PLAYER_KING_START, playerKing);
        board.place(ENEMY_KING_START, enemyKing);

        this.player = new Team(playerName, playerUnits, PLAYER_KING_START, config.getRng());
        this.enemy = new Team(enemyName, enemyUnits, ENEMY_KING_START, config.getRng());
    }

    /**
     * Increments and returns the next placement order.
     *
     * @return the next placement order
     */
    int getNextPlacementOrder() {
        nextPlacementOrder++;
        return nextPlacementOrder;
    }

    /**
     * Checks if the game is currently in yield-hand mode (waiting for discard).
     *
     * @return true if waiting for discard, false otherwise
     */
    public boolean isYieldHandMode() {
        return yieldHandMode;
    }

    /**
     * Sets the yield-hand mode state.
     *
     * @param yieldHandMode true to enable yield-hand mode, false to disable
     */
    void setYieldHandMode(boolean yieldHandMode) {
        this.yieldHandMode = yieldHandMode;
    }

    /**
     * Gets the team identity of the currently active player.
     *
     * @return the active team identity
     */
    public TeamId getActiveTeamId() {
        return activeTeamId;
    }

    /**
     * Sets the active team identity.
     *
     * @param activeTeamId the team identity to set as active
     */
    void setActiveTeamId(TeamId activeTeamId) {
        this.activeTeamId = activeTeamId;
    }

    /**
     * Returns the team name of a unit.
     *
     * @param unit the unit
     * @return the team name, or null if the unit is null
     */
    public String getUnitTeamName(Unit unit) {
        if (unit == null) {
            return null;
        }
        if (unit.getTeamId() == TeamId.PLAYER) {
            return player.getName();
        } else {
            return enemy.getName();
        }
    }

    /**
     * Sets the currently selected unit to a blocking state.
     *
     * @return a MoveResult containing the outcome of the blockade
     * @throws GameException if the blockade is invalid or forbidden
     */
    public MoveResult blockSelectedUnit() throws GameException {
        return new BlockService(this).blockSelectedUnit();
    }

    /**
     * Flips the currently selected hidden unit.
     *
     * @throws GameException if the unit is already revealed, has moved, or the player has no access
     */
    public void flipSelectedUnit() throws GameException {
        new FlipService(this).flipSelectedUnit();
    }

    /**
     * Places one or more units from the hand onto the board.
     *
     * @param indices the array of hand indices
     * @return a list of PlaceResult detailing the outcome of the placement
     * @throws GameException if the placement is invalid
     */
    public List<PlaceResult> place(int[] indices) throws GameException {
        return new PlaceService(this).place(indices);
    }

    /**
     * Moves the currently selected unit to a target cell.
     *
     * @param targetCell the target cell
     * @return a MoveResult containing the outcome of the move, which could involve duel or merge
     * @throws GameException if the move is invalid or forbidden
     */
    public MoveResult move(Cell targetCell) throws GameException {
        return new MoveService(this).move(targetCell);
    }

    /**
     * Yields the current turn and optionally discards a unit if the hand is full.
     *
     * @param index the optional index of the unit to discard
     * @return a YieldResult containing the outcome of the yield
     * @throws GameException if a discard is mandatory or forbidden
     */
    public YieldResult yield(Integer index) throws GameException {
        return new YieldService(this).yield(index);
    }

    /**
     * Gets the winning team.
     *
     * @return the winner, or null if no winner yet
     */
    public Team getWinner() {
        return winner;
    }

    /**
     * Sets the winning team.
     *
     * @param winner the winning team
     */
    void setWinner(Team winner) {
        this.winner = winner;
    }

    /**
     * Checks if the active team is allowed to see this unit.
     *
     * @param unit the unit to check
     * @return true if the unit is accessible, false otherwise
     */
    public boolean hasAccessToUnit(Unit unit) {
        return unit != null && (unit.getTeamId() == activeTeamId || !unit.isHidden());
    }

    /**
     * Gets the game board.
     *
     * @return the board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Gets the current turn.
     *
     * @return the current turn
     */
    public int getCurrentTurn() {
        return currentTurn;
    }

    /**
     * Increments the current turn by one.
     */
    void incrementCurrentTurn() {
        this.currentTurn++;
    }

    private Team getTeam(boolean isActive) {
        boolean playerActive = activeTeamId == TeamId.PLAYER;
        return (isActive == playerActive) ? player : enemy;
    }

    /**
     * Gets the team whose on turn.
     *
     * @return the active team
     */
    public Team getActiveTeam() {
        return getTeam(true);
    }

    /**
     * Gets the opponent team.
     *
     * @return the opponent team
     */
    Team getOpponentTeam() {
        return getTeam(false);
    }

    /**
     * Gets the player's team.
     *
     * @return the player team
     */
    public Team getPlayer() {
        return player;
    }

    /**
     * Gets the enemy's team.
     *
     * @return the enemy team
     */
    public Team getEnemy() {
        return enemy;
    }

    /**
     * Gets the game configuration.
     *
     * @return the configuration
     */
    public Config getConfig() {
        return config;
    }

    /**
     * Validates that a unit is selected, belongs to the active team, and can move.
     *
     * @throws GameException if no unit is selected, the unit belongs to another team, or the unit has already moved
     */
    void validateSelectedUnit() throws GameException {
        Unit selectedUnit = board.getSelectedUnit();

        if (selectedUnit == null) {
            throw new GameException(ERROR_SELECTED_UNIT_REQUIRED);
        }

        if (selectedUnit.getTeamId() != activeTeamId) {
            throw new GameException(ERROR_ACCESS_DENIED);
        }

        if (!selectedUnit.canMove(currentTurn)) {
            throw new GameException(ERROR_UNIT_ACTION_FORBIDDEN);
        }
    }
}
