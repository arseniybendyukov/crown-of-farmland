package de.bendyukov.farm.models.enemy;

import de.bendyukov.farm.exceptions.GameException;
import de.bendyukov.farm.models.Board;
import de.bendyukov.farm.models.Cell;
import de.bendyukov.farm.models.Game;
import de.bendyukov.farm.models.Hand;
import de.bendyukov.farm.models.MathUtils;
import de.bendyukov.farm.models.TeamId;
import de.bendyukov.farm.models.Unit;
import de.bendyukov.farm.models.results.Compatibility;
import de.bendyukov.farm.models.results.MoveResult;
import de.bendyukov.farm.models.results.PlaceResult;
import de.bendyukov.farm.models.results.YieldResult;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Represents the bot controlling the enemy team's actions in the game.
 *
 * @author Arseniy Bendyukov
 * @version 1.0
 */
public class EnemyBot {
    private static final Direction[] KING_MOVE_STEPS = {
        Direction.TOP,
        Direction.RIGHT,
        Direction.BOTTOM,
        Direction.LEFT,
        Direction.EN_PLACE
    };
    private static final Direction[] KING_SURROUNDING_STEPS = {
        Direction.TOP,
        Direction.TOP_RIGHT,
        Direction.RIGHT,
        Direction.BOTTOM_RIGHT,
        Direction.BOTTOM,
        Direction.BOTTOM_LEFT,
        Direction.LEFT,
        Direction.TOP_LEFT
    };
    private static final Direction[] UNIT_CROSS_STEPS = {
        Direction.TOP,
        Direction.RIGHT,
        Direction.BOTTOM,
        Direction.LEFT
    };
    private static final int FELLOW_PRESENT_SCORE = 1;
    private static final int FELLOW_ABSENT_SCORE = 0;
    private static final int MIN_INDEX = 0;
    private static final int ENEMIES_KING_MOVE_SCORE_MULTIPLIER = -2;
    private static final int FELLOW_PRESENT_SCORE_MULTIPLIER = -3;
    private static final int ENEMIES_PLACE_SCORE_MULTIPLIER = 2;
    private static final int MOVE_DISTANCE_MULTIPLIER = 10;
    private static final int HIDDEN_OFFSET = 500;
    private static final int DUEL_MULTIPLIER = 2;
    private static final int MIN_BLOCK_SCORE = 1;
    private static final int MIN_EN_PLACE_SCORE = 0;
    private static final int INITIAL_SCORE = 0;
    private static final int POSITIVE_SCORE_CHECK = 0;

    private final Game game;

    /**
     * Constructs a new EnemyBot.
     *
     * @param game the current game instance
     */
    public EnemyBot(Game game) {
        this.game = game;
    }

    /**
     * Executes the best calculated move for the enemy king.
     *
     * @return the MoveResult of the king's movement
     * @throws GameException should never occur but is thrown because of the game.move() method
     */
    public MoveResult moveKing() throws GameException {
        Cell kingCell = game.getEnemy().getKingCell();
        Board board = game.getBoard();
        Random rng = game.getConfig().getRng();

        List<Cell> validMoveCells = getCandidateCells(
                kingCell,
                board,
                KING_MOVE_STEPS,
                unit -> unit == null || unit.getTeamId() == TeamId.ENEMY
        );

        List<WeightedObject<Cell>> weightedMoves = new ArrayList<>();
        for (Cell moveCell : validMoveCells) {
            weightedMoves.add(new WeightedObject<>(getKingMoveScore(board, kingCell, moveCell), moveCell));
        }

        List<WeightedObject<Cell>> bestMoves = WeightedObject.getMaxWeightObjects(weightedMoves);

        Cell pickedCell = WeightedObject.pickAmongBest(rng, bestMoves);

        board.selectCell(kingCell);
        return game.move(pickedCell);
    }

    /**
     * Attempts to place an enemy unit on an optimal cell near the enemy king.
     *
     * @return an Optional containing the PlaceResult, or empty if no valid placements
     * @throws GameException should never occur but is thrown because of the game.place() method
     */
    public Optional<PlaceResult> place() throws GameException {
        Cell kingCell = game.getEnemy().getKingCell();
        Cell opponentKingCell = game.getPlayer().getKingCell();
        Board board = game.getBoard();
        Random rng = game.getConfig().getRng();

        List<Cell> validPlaceCells = getCandidateCells(
                kingCell,
                board,
                KING_SURROUNDING_STEPS,
                unit -> unit == null || unit.getTeamId() == TeamId.ENEMY
        );

        if (validPlaceCells.isEmpty()) {
            return Optional.empty();
        }

        List<WeightedObject<Cell>> weightedCells = new ArrayList<>();
        for (Cell placeCell : validPlaceCells) {
            weightedCells.add(new WeightedObject<>(getPlaceScore(board, placeCell, opponentKingCell), placeCell));
        }

        List<WeightedObject<Cell>> bestCells = WeightedObject.getMaxWeightObjects(weightedCells);

        Cell pickedCell = WeightedObject.pickAmongBest(rng, bestCells);

        board.selectCell(pickedCell);

        List<Unit> units = game.getEnemy().getHand().getUnits();

        List<WeightedObject<Integer>> weightedIndices = new ArrayList<>();
        for (int index = 0; index < units.size(); index++) {
            Unit unit = units.get(index);
            weightedIndices.add(new WeightedObject<>(unit.getAtk(), index));
        }

        int pickedIndex = WeightedObject.randomPick(rng, weightedIndices).orElseThrow();

        int[] indices = {pickedIndex};
        return Optional.of(game.place(indices).getFirst());
    }

    /**
     * Evaluates and executes the best regular unit move or block action for the enemy.
     *
     * @return the MoveResult of the chosen action
     * @throws GameException should never occur but is thrown because of the game.move() method
     */
    public MoveResult move() throws GameException {
        Board board = game.getBoard();
        Cell opponentKingCell = game.getPlayer().getKingCell();
        Random rng = game.getConfig().getRng();
        List<Cell> movableUnitCells = board.getMovableUnitCells(TeamId.ENEMY, game.getCurrentTurn());
        List<WeightedObject<EnemyUnitMove>> unitMoves = new ArrayList<>();
        for (Cell fromCell : movableUnitCells) {
            Unit fromUnit = board.getUnit(fromCell);
            List<Cell> validMoveCells = getCandidateCells(
                    fromCell,
                    board,
                    UNIT_CROSS_STEPS, unit -> unit == null || !(unit.isKing() && unit.getTeamId() == TeamId.ENEMY)
            );
            List<WeightedObject<EnemyMoveAction>> weightedActions = new ArrayList<>();
            int totalScore = INITIAL_SCORE;
            boolean hasPositiveMovement = false;
            for (Cell toCell : validMoveCells) {
                int score = getUnitMoveScore(board, fromUnit, toCell, opponentKingCell);
                totalScore += score;
                if (score > POSITIVE_SCORE_CHECK) {
                    hasPositiveMovement = true;
                }
                weightedActions.add(new WeightedObject<>(score, () -> {
                    board.selectCell(fromCell);
                    return game.move(toCell);
                }));
            }
            int maxStraightAtk = board.getMaxStraightAtk(fromCell, TeamId.ENEMY);
            int blockScore = Math.max(MIN_BLOCK_SCORE, (fromUnit.getDef() - maxStraightAtk) / MathUtils.MIN_GCD);
            totalScore += blockScore;
            EnemyMoveAction blockAction = () -> {
                board.selectCell(fromCell);
                return game.blockSelectedUnit();
            };
            weightedActions.add(new WeightedObject<>(blockScore, blockAction));
            int enPlaceScore = Math.max(MIN_EN_PLACE_SCORE, (fromUnit.getAtk() - maxStraightAtk) / MathUtils.MIN_GCD);
            totalScore += enPlaceScore;
            if (enPlaceScore > POSITIVE_SCORE_CHECK) {
                hasPositiveMovement = true;
            }
            weightedActions.add(
                    new WeightedObject<>(enPlaceScore, () -> {
                        board.selectCell(fromCell);
                        return game.move(fromCell);
                    })
            );
            unitMoves.add(new WeightedObject<>(
                    totalScore,
                    new EnemyUnitMove(fromUnit, hasPositiveMovement, blockAction, weightedActions)
            ));
        }
        List<WeightedObject<EnemyUnitMove>> bestUnits = WeightedObject.getMaxWeightObjects(unitMoves);
        bestUnits.sort(Comparator.comparingInt(weightedUnit -> weightedUnit.getObject().unit().getPlacementOrder()));
        EnemyUnitMove pickedUnitChoice = WeightedObject.pickAmongBest(rng, bestUnits);
        if (!pickedUnitChoice.hasPositiveMovement()) {
            return pickedUnitChoice.blockAction().execute();
        }
        EnemyMoveAction pickedAction = WeightedObject.randomPick(rng, pickedUnitChoice.weightedActions()).orElseThrow();
        return pickedAction.execute();
    }

    private int getUnitMoveScore(Board board, Unit fromUnit, Cell toCell, Cell opponentKing) {
        Unit toUnit = board.getUnit(toCell);

        int score;

        if (toUnit == null) {
            int distance = toCell.distance(opponentKing);
            score = MOVE_DISTANCE_MULTIPLIER * distance - board.countCrossEnemies(toCell, TeamId.ENEMY);
        } else if (toUnit.getTeamId() == TeamId.ENEMY) {
            Compatibility compatibility = fromUnit.getCompatibility(toUnit);
            if (compatibility.isCompatible()) {
                score = compatibility.atk() + compatibility.def() - fromUnit.getAtk() - fromUnit.getDef();
            } else {
                score = -toUnit.getAtk() - toUnit.getDef();
            }
        } else {
            if (toUnit.isKing()) {
                score = fromUnit.getAtk();
            } else if (toUnit.isHidden()) {
                score = fromUnit.getAtk() - HIDDEN_OFFSET;
            } else if (toUnit.isBlocking()) {
                score = fromUnit.getAtk() - toUnit.getDef();
            } else {
                score = DUEL_MULTIPLIER * (fromUnit.getAtk() - toUnit.getAtk());
            }
        }

        return score;
    }

    private int getKingMoveScore(Board board, Cell kingCell, Cell moveCell) {
        return board.countSurrondingFellows(moveCell, TeamId.ENEMY)
                + ENEMIES_KING_MOVE_SCORE_MULTIPLIER * board.countSurrondingEnemies(moveCell, TeamId.ENEMY)
                - kingCell.distance(moveCell)
                + FELLOW_PRESENT_SCORE_MULTIPLIER * getFellowPresentScore(moveCell, kingCell);
    }

    private int getPlaceScore(Board board, Cell placeCell, Cell opponentKing) {
        return -placeCell.distance(opponentKing)
                + ENEMIES_PLACE_SCORE_MULTIPLIER * board.countCrossEnemies(placeCell, TeamId.ENEMY)
                - board.countCrossFellows(placeCell, TeamId.ENEMY);
    }

    private int getFellowPresentScore(Cell cell, Cell kingCell) {
        if (cell.equals(kingCell)) {
            return FELLOW_ABSENT_SCORE;
        }
        Unit unit = game.getBoard().getUnit(cell);
        return unit != null && unit.getTeamId() == TeamId.ENEMY ? FELLOW_PRESENT_SCORE : FELLOW_ABSENT_SCORE;
    }

    private List<Cell> getCandidateCells(Cell kingCell, Board board, Direction[] directions, UnitCondition condition) {
        List<Cell> cells = new ArrayList<>();

        int kingRow = kingCell.getI();
        int kingColumn = kingCell.getJ();

        for (Direction direction : directions) {
            int row = kingRow + direction.getRowStep();
            int column = kingColumn + direction.getColumnStep();

            if (row >= MIN_INDEX && row < Board.SIZE && column >= MIN_INDEX && column < Board.SIZE) {
                Unit unit = board.getUnit(row, column);
                if (condition.test(unit)) {
                    cells.add(new Cell(row, column));
                }
            }
        }

        return cells;
    }

    /**
     * Executes a yield for the enemy, potentially discarding the worst unit.
     *
     * @return the YieldResult of the yield
     * @throws GameException should never occur but is thrown because of the game.yield() method
     */
    public YieldResult yield() throws GameException {
        Hand hand = game.getEnemy().getHand();
        Random rng = game.getConfig().getRng();

        Integer discardIndex = null;

        if (hand.isFull()) {
            List<Unit> units = hand.getUnits();

            List<WeightedObject<Integer>> weightedIndices = new ArrayList<>();
            for (int index = 0; index < units.size(); index++) {
                Unit unit = units.get(index);
                weightedIndices.add(new WeightedObject<>(unit.getAtk() + unit.getDef(), index));
            }

            discardIndex = WeightedObject.inverseRandomPick(rng, weightedIndices).orElseThrow();
        }

        return game.yield(discardIndex);
    }
}
