package com.ipalacios.linesafari;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Line Safari
// https://www.codewars.com/kata/59c5d0b0a25c8c99ca000237/train/java
public class Dinglemouse {

    private char[][] grid;
    private int numRows;
    private int numCols;
    private int currentX;
    private int currentY;
    private String lastMove = "";

    static final String MOVE_UP = "UP";
    static final String MOVE_RIGHT = "RIGHT";
    static final String MOVE_LEFT = "LEFT";
    static final String MOVE_DOWN = "DOWN";

    static final char VERTICAL = '|';
    static final char HORIZONTAL = '-';
    static final char BLANK = ' ';
    static final char CORNER = '+';
    static final char STAR = 'X';

    private Dinglemouse(char[][] grid) {

        this.numRows = grid.length;
        this.numCols = grid[0].length;
        this.grid = new char[grid.length][];
        for (int i = 0; i < this.numRows; i++) {
            this.grid[i] = Arrays.copyOf(grid[i], this.numCols);
        }
    }

    private void setStart(boolean start) {

        int count = 0;
        for (int i = 0; i < this.numRows; i++) {
            for (int j = 0; j < this.numCols; j++) {
                if (this.grid[i][j] == STAR) {
                    count++;
                    if (start || count > 1) {
                        this.currentX = i;
                        this.currentY = j;
                        return;
                    }
                }
            }
        }
    }

    public void printGrid(char[][] grid) {
        System.out.println("");
        for (int i = 0; i< numRows; i++) {
            System.out.print("\"");
            for (int j = 0; j < numCols; j++) {
                System.out.print(grid[i][j]);
            }
            System.out.println("\"");
        }
        System.out.println("");
    }

    public boolean isGridClear() {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numRows; j++) {
                if (this.grid[i][j] != BLANK) {
                    return false;
                }
            }
        }
        return true;
    }

    public List<String> getValidMoves() {
        List<String> moves = new ArrayList<>();

        char top = this.currentX > 0 ? this.grid[this.currentX - 1][this.currentY] : BLANK;
        char right = this.currentY < numCols - 1 ? this.grid[this.currentX][this.currentY + 1] : BLANK;
        char bottom = this.currentX < numRows - 1 ? this.grid[this.currentX + 1][this.currentY] : BLANK;
        char left = this.currentY > 0 ? this.grid[this.currentX][this.currentY - 1] : BLANK;
        char current = this.grid[this.currentX][this.currentY];

        if (current == CORNER) {
            if ((top == STAR || top == VERTICAL || top == CORNER) &&  lastMove != MOVE_UP) {
                moves.add((MOVE_UP));
            }
            if ((right == STAR || right == HORIZONTAL || right == CORNER) &&  lastMove != MOVE_RIGHT) {
                moves.add((MOVE_RIGHT));
            }
            if ((bottom == STAR || bottom == VERTICAL || bottom == CORNER) &&  lastMove != MOVE_DOWN) {
                moves.add((MOVE_DOWN));
            }
            if ((left == STAR || left == HORIZONTAL || left == CORNER) &&  lastMove != MOVE_LEFT) {
                moves.add((MOVE_LEFT));
            }
        }
        if (current == VERTICAL || current == STAR) {
            if (top == STAR || top == VERTICAL || top == CORNER) {
                moves.add(MOVE_UP);
            }
            if (bottom == STAR || bottom == VERTICAL || bottom == CORNER) {
                moves.add(MOVE_DOWN);
            }
        }
        if (current == HORIZONTAL || current == STAR) {
            if (right == STAR || right == HORIZONTAL || right == CORNER) {
                moves.add(MOVE_RIGHT);
            }
            if (left == STAR || left == HORIZONTAL || left == CORNER) {
                moves.add(MOVE_LEFT);
            }
        }
        return moves;
    }

    private boolean move() {

        List<String> moves = getValidMoves();
        if (moves.isEmpty() || moves.size() > 1) {
            return false;
        }
        String move = moves.get(0);
        this.grid[currentX][currentY] = BLANK;
        this.currentX += move.equals(MOVE_UP) ? -1 : 0;
        this.currentX += move.equals(MOVE_DOWN) ? 1 : 0;
        this.currentY += move.equals(MOVE_RIGHT) ? 1 : 0;
        this.currentY += move.equals(MOVE_LEFT) ? -1 : 0;
        this.lastMove = move;
        return true;
    }

    public boolean doPath() {
        if (!move()) {
            return false;
        }
        while(this.grid[this.currentX][this.currentY] != STAR) {
            if (!move()) {
                return false;
            }
        }
        this.grid[this.currentX][this.currentY] = BLANK;
        return true;
    }


    public static boolean line(final char [][] grid) {
        Dinglemouse dinglemouse = new Dinglemouse(grid);
        dinglemouse.printGrid(grid);
        dinglemouse.setStart(true);
        if (dinglemouse.doPath()) {
            return dinglemouse.isGridClear();
        }
        dinglemouse = new Dinglemouse(grid);
        dinglemouse.setStart(false);
        return dinglemouse.doPath() && dinglemouse.isGridClear();

    }

}