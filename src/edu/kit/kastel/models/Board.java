package edu.kit.kastel.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the playing board.
 *
 * @author udkcf
 * @version 1.0
 */
public class Board {
    /** The size of the board. */
    public static final int SIZE = 7;
    private static final int MAX_UNITS = 5;
    private static final int NO_UNITS = 0;
    private static final int INITIAL_MAX_ATK = 0;

    private final Unit[][] grid = new Unit[SIZE][SIZE];
    private Cell selectedCell;

    /**
     * Places a unit on a cell on the board.
     *
     * @param cell the cell
     * @param unit the unit to place
     */
    public void place(Cell cell, Unit unit) {
        grid[cell.getI()][cell.getJ()] = unit;
    }

    /**
     * Clears the cell from a unit.
     *
     * @param cell the cell to clear
     */
    public void clear(Cell cell) {
        grid[cell.getI()][cell.getJ()] = null;
    }

    
    /**
     * Retrieves the unit by cell.
     *
     * @param cell the cell
     * @return the unit, or null if empty
     */
    public Unit getUnit(Cell cell) {
        return grid[cell.getI()][cell.getJ()];
    }

    /**
     * Retrieves the unit by row and column.
     *
     * @param row the zero-based row
     * @param column the zero-based column
     * @return the unit, or null if empty
     */
    public Unit getUnit(int row, int column) {
        return grid[row][column];
    }


    /**
     * Gets the currently selected unit, if a cell is selected.
     *
     * @return the selected unit, or null if no cell is selected or empty
     */
    public Unit getSelectedUnit() {
        if (selectedCell == null) {
            return null;
        }
        return grid[selectedCell.getI()][selectedCell.getJ()];
    }

    /**
     * Selects a cell.
     *
     * @param cell the cell to select
     */
    public void selectCell(Cell cell) {
        selectedCell = cell;
    }

    /**
     * Resets the selected cell to null.
     */
    public void resetSelectedCell() {
        selectedCell = null;
    }

    /**
     * Gets the currently selected cell.
     *
     * @return the cell, or null if no cell is selected
     */
    public Cell getSelectedCell() {
        return selectedCell;
    }

    /**
     * Counts the total number of regular units (excluding king) for a team.
     *
     * @param teamId the team identity
     * @return the unit count
     */
    public int countUnits(TeamId teamId) {
        return countMatchingUnits((unit, currentCell) -> unit.getTeamId() == teamId && !unit.isKing());
    }

    /**
     * Counts the number of fellows surrounding a cell (the king is excluded).
     *
     * @param cell the center cell
     * @param teamId the team whose units to count
     * @return the count of surrounding fellows
     */
    public int countSurrondingFellows(Cell cell, TeamId teamId) {
        return countMatchingUnits((unit, currentCell) ->
                cell.isSurrounding(currentCell)
                        && unit.getTeamId() == teamId
                        && !unit.isKing()
        );
    }

    /**
     * Counts the number of enemies surrounding a cell.
     *
     * @param cell the center cell
     * @param teamId the team whose enemies to count
     * @return the count of surrounding enemies
     */
    public int countSurrondingEnemies(Cell cell, TeamId teamId) {
        return countMatchingUnits((unit, currentCell) ->
                cell.isSurrounding(currentCell)
                && unit.getTeamId() != teamId
        );
    }

    /**
     * Counts the number of fellows cross-neighboring a cell.
     *
     * @param cell the center cell
     * @param teamId the team whose units to count
     * @return the count of cross-neighboring fellows
     */
    public int countCrossFellows(Cell cell, TeamId teamId) {
        return countMatchingUnits((unit, currentCell) ->
                cell.isCrossNeighbor(currentCell)
                && !cell.equals(currentCell)
                && unit.getTeamId() == teamId
        );
    }

    /**
     * Counts the number of enemies cross-neighboring a cell.
     *
     * @param cell the center cell
     * @param teamId the team whose enemies to count
     * @return the count of cross-neighboring enemies
     */
    public int countCrossEnemies(Cell cell, TeamId teamId) {
        return countMatchingUnits((unit, currentCell) ->
                cell.isCrossNeighbor(currentCell)
                && !cell.equals(currentCell)
                && unit.getTeamId() != teamId
        );
    }

    private int countMatchingUnits(BoardCondition condition) {
        int count = NO_UNITS;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Unit unit = grid[i][j];
                Cell currentCell = new Cell(i, j);
                if (unit != null && condition.test(unit, currentCell)) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Retrieves all cells containing regular units of a team that can move this turn.
     *
     * @param teamId the team identity
     * @param currentTurn the current game turn
     * @return a list of cells with movable units
     */
    public List<Cell> getMovableUnitCells(TeamId teamId, int currentTurn) {
        List<Cell> movableUnitCells = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Unit unit = grid[i][j];
                if (unit != null && unit.getTeamId() == teamId && unit.canMove(currentTurn) && !unit.isKing()) {
                    movableUnitCells.add(new Cell(i, j));
                }
            }
        }
        return movableUnitCells;
    }

    /**
     * Calculates the maximum attack among revealed enemies,
     * that are in the same row or column as the target cell.
     *
     * @param cell the target cell
     * @param teamId the team identity
     * @return the maximum attack
     */
    public int getMaxStraightAtk(Cell cell, TeamId teamId) {
        int maxAtk = INITIAL_MAX_ATK;

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Cell currentCell = new Cell(i, j);
                if (cell.isCrossNeighbor(currentCell) && !cell.equals(currentCell)) {
                    Unit unit = grid[i][j];
                    if (unit != null && unit.getTeamId() != teamId && !unit.isHidden()) {
                        maxAtk = Math.max(maxAtk, unit.getAtk());
                    }
                }
            }
        }

        return maxAtk;
    }

    /**
     * Checks if a team has reached their maximum allowed placed units on the board.
     *
     * @param teamId the team identity
     * @return true if max units reached, false otherwise
     */
    public boolean reachedMaxUnits(TeamId teamId) {
        return countUnits(teamId) >= MAX_UNITS;
    }
}
