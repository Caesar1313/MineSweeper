package com.company;

public class Player {

    private boolean selectedEmptyCell = false;
    public static int clicksMade = 0;

    private boolean canRevealAbove(int x, int y) {
        return y - 1 >= 0 && MineSweeper.grid[y - 1][x].cellState == CellState.HIDDEN;
    }

    private boolean canRevealUnder(int x, int y) {
        return y + 1 < MineSweeper.gridDimensions && MineSweeper.grid[y + 1][x].cellState == CellState.HIDDEN;
    }

    private boolean canRevealLeft(int x, int y) {
        return x - 1 >= 0 && MineSweeper.grid[y][x - 1].cellState == CellState.HIDDEN;
    }

    private boolean canRevealRight(int x, int y) {
        return x + 1 < MineSweeper.gridDimensions && MineSweeper.grid[y][x + 1].cellState == CellState.HIDDEN;
    }

    private boolean canRevealUpperRight(int x, int y) {
        return y - 1 >= 0 && x + 1 < MineSweeper.gridDimensions && MineSweeper.grid[y - 1][x + 1].cellState == CellState.HIDDEN;
    }

    private boolean canRevealUpperLeft(int x, int y) {
        return y - 1 >= 0 && x - 1 >= 0 && MineSweeper.grid[y - 1][x - 1].cellState == CellState.HIDDEN;
    }

    private boolean canRevealLowerRight(int x, int y) {
        return y + 1 < MineSweeper.gridDimensions && x + 1 < MineSweeper.gridDimensions && MineSweeper.grid[y + 1][x + 1].cellState == CellState.HIDDEN;
    }

    private boolean canRevealLowerLeft(int x, int y) {
        return y + 1 < MineSweeper.gridDimensions && x - 1 >= 0 && MineSweeper.grid[y + 1][x - 1].cellState == CellState.HIDDEN;
    }

    private boolean shouldStopMine(int x, int y) {
        return MineSweeper.grid[y][x] instanceof MineCell;
    }

    private boolean shouldStopNumbered(int x, int y) {
        return MineSweeper.grid[y][x] instanceof NumberedCell;
    }

    private void revealAdjacentCells(int x, int y) {
        if (canRevealAbove(x, y))
            selectCell(x, y - 1);
        if (canRevealUnder(x, y))
            selectCell(x, y + 1);
        if (canRevealLeft(x, y))
            selectCell(x - 1, y);
        if (canRevealRight(x, y))
            selectCell(x + 1, y);
        if (canRevealUpperRight(x, y))
            selectCell(x + 1, y - 1);
        if (canRevealUpperLeft(x, y))
            selectCell(x - 1, y - 1);
        if (canRevealLowerRight(x, y))
            selectCell(x + 1, y + 1);
        if (canRevealLowerLeft(x, y))
            selectCell(x - 1, y + 1);
    }

    private void selectMineCell(int x, int y) {
        MineSweeper.grid[y][x].reveal();
    }

    public void selectCell(int x, int y) {

        //if the selected cell was a MineCell
        if (MineSweeper.grid[y][x] instanceof MineCell) {
            selectMineCell(x,y);
            return;
        }

        //if the selected cell was a NumberedCell
        if (MineSweeper.grid[y][x] instanceof NumberedCell && !selectedEmptyCell) {
            MineSweeper.grid[y][x].reveal();
        }
        //if the selected cell was an EmptyCell
        else {
            selectedEmptyCell = true;
            if (shouldStopMine(x, y)) return;
            if (shouldStopNumbered(x, y)) {
                MineSweeper.grid[y][x].reveal();
                return;
            }
            MineSweeper.grid[y][x].reveal();
            revealAdjacentCells(x, y);
        }
    }
}