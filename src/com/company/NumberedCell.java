package com.company;

public class NumberedCell extends Cell{

    NumberedCell(){
        super();
        this.content = 0;
    }
    /** Exchanged coordinates because it is a two dimensioned array */
    private boolean checkLeft(){
        return this.position.x-1 >= 0 && MineSweeper.grid[this.position.y][this.position.x-1].content == -1;
    }
    private boolean checkUpperLeft(){
        return (this.position.x-1 >= 0 && this.position.y-1 >= 0) && MineSweeper.grid[this.position.y-1][this.position.x-1].content == -1;
    }
    private boolean checkLowerLeft(){
        return (this.position.x-1 >= 0 && this.position.y+1 < MineSweeper.gridDimensions) && MineSweeper.grid[this.position.y+1][this.position.x-1].content == -1;
    }
    private boolean checkAbove(){
        return this.position.y-1 >= 0 && MineSweeper.grid[this.position.y-1][this.position.x].content == -1;
    }
    private boolean checkUnder(){
        return this.position.y+1 < MineSweeper.gridDimensions && MineSweeper.grid[this.position.y+1][this.position.x].content == -1;
    }
    private boolean checkUpperRight(){
        return (this.position.x+1 < MineSweeper.gridDimensions && this.position.y-1 >= 0) && MineSweeper.grid[this.position.y-1][this.position.x+1].content == -1;
    }
    private boolean checkRight(){
        return  this.position.x+1 < MineSweeper.gridDimensions && MineSweeper.grid[this.position.y][this.position.x+1].content == -1;
    }
    private boolean checkLowerRight(){
        return  (this.position.x+1 < MineSweeper.gridDimensions && this.position.y+1 < MineSweeper.gridDimensions) && MineSweeper.grid[this.position.y+1][this.position.x+1].content == -1;
    }

    public void setAdjacentMines(){
        if(checkLeft())
            content++;
        if(checkUpperLeft())
            content++;
        if(checkLowerLeft())
            content++;
        if(checkAbove())
            content++;
        if(checkUnder())
            content++;
        if(checkUpperRight())
            content++;
        if(checkRight())
            content++;
        if(checkLowerRight())
            content++;
    }
}