package com.company;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class MineSweeper implements Runnable {
    public static final Player player = new Player();
    //public static MineSweeper mineSweeper = null;
    public static GameState state;
    public static int gridDimensions;
    public static int minesCount;
    public static int score = 0;
    public static boolean mineExploded = false;
    static String[][] asChars;
    public static Cell[][] grid;

    public MineSweeper(int gridDimensions, int minesCount) {
        System.out.println("Starting MineSweeper...");
        MineSweeper.gridDimensions = gridDimensions;
        MineSweeper.minesCount = minesCount;
        grid = new Cell[gridDimensions][gridDimensions];
        asChars = new String[gridDimensions][gridDimensions];
        StartGame();
    }

    private void PlaceMines() {
        int placedMines = 0;
        Random rand = new Random(MineSweeper.gridDimensions);
        while(placedMines != MineSweeper.minesCount) {
            int r1 = rand.nextInt(MineSweeper.gridDimensions);
            int r2 = rand.nextInt(MineSweeper.gridDimensions);
            if (!(MineSweeper.grid[r1][r2] instanceof MineCell)) {
                placedMines++;
                MineSweeper.grid[r1][r2] = new MineCell();
                MineSweeper.grid[r1][r2].position.x = r2;
                MineSweeper.grid[r1][r2].position.y = r1;
            }
        }
    }

    private void NumberedOrEmpty(int i, int j) {
        NumberedCell numberedCell = new NumberedCell();
        MineSweeper.grid[i][j] = numberedCell;
        MineSweeper.grid[i][j].position.x = j;
        MineSweeper.grid[i][j].position.y = i;
        numberedCell.setAdjacentMines();
        if (numberedCell.content == 0) {
            MineSweeper.grid[i][j] = new EmptyCell();
            MineSweeper.grid[i][j].position.x = j;
            MineSweeper.grid[i][j].position.y = i;
        }
    }

    private void PlaceRemainedCells() {
        for (int i = 0; i < MineSweeper.gridDimensions; i++)
            for (int j = 0; j < MineSweeper.gridDimensions; j++) {
                if (!(MineSweeper.grid[i][j] instanceof MineCell)) {
                    NumberedOrEmpty(i, j);
                }
            }
    }

    private void PlaceCells() {
        PlaceMines();
        PlaceRemainedCells();
    }

    private void fillGrid(){
        for (int i = 0; i < gridDimensions; i++)
            for (int j = 0; j < gridDimensions; j++) {
                Cell cell = new EmptyCell();
                MineSweeper.grid[i][j] = cell;
            }
    }

    private void InitializeGame() {
        fillGrid();
        PlaceCells();
        for (int i = 0; i < gridDimensions; i++)
            for (int j = 0; j < gridDimensions; j++) {
                grid[i][j].cellState = CellState.HIDDEN;
            }
    }

    private void StartGame() {
        InitializeGame();
        Timer time = Timer.getTimer();
        Thread gameThread = new Thread(this);
        Thread timeThread = new Thread(time);
        MineSweeper.state = GameState.RUNNINGGAME;
        timeThread.start();
        gameThread.start();
    }

    private void updateArray(){
        for(int i = 0; i < MineSweeper.gridDimensions; i++) {
            for (int j = 0; j < MineSweeper.gridDimensions; j++) {
                if(MineSweeper.grid[i][j].cellState == CellState.HIDDEN){
                    MineSweeper.asChars[i][j] = "H";
                }else{
                    MineSweeper.asChars[i][j] = String.valueOf(MineSweeper.grid[i][j].content);
                }
            }
        }
    }

    private void printArray(){
        for(int i = 0; i < MineSweeper.gridDimensions; i++) {
            System.out.print("\n");
            for (int j = 0; j < MineSweeper.gridDimensions; j++) {
                System.out.print(MineSweeper.asChars[i][j] + "   ");
            }
        }
    }

    private void displayGrid(){
        updateArray();
        printArray();
    }

    private int getX() throws InvalidCoordinateException{
        Scanner scanner = new Scanner(System.in);
        int x;
        x = scanner.nextInt();
        if(x >= MineSweeper.gridDimensions || x < 0)
            throw new InvalidCoordinateException("Coordinate Out Of Grid Dimensions.");
        return x;
    }

    private int getY() throws InvalidCoordinateException{
        Scanner scanner = new Scanner(System.in);
        int y;
        y = scanner.nextInt();
        if(y >= MineSweeper.gridDimensions || y < 0)
            throw new InvalidCoordinateException("Coordinate Out Of Grid Dimensions.");
        return y;
    }

    @Override
    public void run() {
        displayGrid();
        while (MineSweeper.state == GameState.RUNNINGGAME) {
            System.out.print("\nEnter the coordinates of the cell you want to reveal: ");
            try {
                int x = getX();
                int y = getY();
                if (MineSweeper.grid[y][x].cellState == CellState.REVEALED) {
                    System.out.println("Already revealed cell. Choose another.");
                    continue;
                }
                else
                    player.selectCell(x, y);
            }catch (InvalidCoordinateException e){
                System.err.println("\nException: " + e);
                continue;
            }catch (InputMismatchException exception){
                System.err.println("\nException: Enter An Integer!!");
                continue;
            }
            System.out.print("\n");
            displayGrid();
            GameRules.Consult();
        }
    }

    public static class GameRules {
        public static boolean GameOver() {
            return MineSweeper.mineExploded;
        }

        public static boolean PlayerWin() {
            for (int i = 0; i < MineSweeper.gridDimensions; i++)
                for (int j = 0; j < MineSweeper.gridDimensions; j++) {
                    if (!(MineSweeper.grid[i][j] instanceof MineCell)) {            // could use if grid[i][j].content == -1
                        if (MineSweeper.grid[i][j].cellState == CellState.HIDDEN)
                            return false;
                    }
                }
            return true;
        }

        private static void Consult() {
            if (GameOver()) {
                state = GameState.LOSE;
                System.out.println("\nMine Hit!");
                System.out.println("Seconds Spent: "+Timer.time + " seconds");
                System.out.println("Score: " + MineSweeper.score);
                System.exit(-1);
            } else if (PlayerWin()) {
                Player.clicksMade++;    // I have put it here because the last click should be counted :) .
                state = GameState.WIN;
                score = ((100000 * (gridDimensions * gridDimensions)) / (Player.clicksMade * Timer.time));
                System.out.println("\nMines Swept!");
                System.out.println("Seconds Spent: "+Timer.time + " seconds");
                System.out.println("Score: " + MineSweeper.score);
                System.exit(-1);
            } else {
                Player.clicksMade++; // I have put it here because with each click I am checking the 'Consult' function.
                state = GameState.RUNNINGGAME;
            }
        }
    }

    private static class InvalidCoordinateException extends Exception {
        InvalidCoordinateException(String s){
            super(s);
        }
    }
}