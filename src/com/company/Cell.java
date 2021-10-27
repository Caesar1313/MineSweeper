package com.company;

public abstract class Cell {
    public int content;
    public CellState cellState;
    public Position position;

    public Cell() {
        this.position = new Position();
    }

    public void reveal(){
        this.cellState = CellState.REVEALED;
        if(this.content == -1){     // if the cell was a MineCell
            MineSweeper.mineExploded = true;
            for(int i = 0; i < MineSweeper.gridDimensions; i++)                 // Reveal all mines
                for(int j = 0; j < MineSweeper.gridDimensions; j++)             // Reveal all mines
                    if(MineSweeper.grid[i][j] instanceof MineCell)              // Reveal all mines
                        MineSweeper.grid[i][j].cellState = CellState.REVEALED;  // Reveal all mines
        }
    }
}