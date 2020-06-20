package com.ipalacios.linesafari;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Line Safari
// https://www.codewars.com/kata/59c5d0b0a25c8c99ca000237/train/java
public class Dinglemouse {

    private char[] grid;
    private int numRows;
    private int numCols;


    static final String MOVE_UP = "UP";
    static final String MOVE_RIGHT = "RIGHT";
    static final String MOVE_LEFT = "LEFT";
    static final String MOVE_DOWN = "DOWN";
    static final String FINISH = "END";

    static final char VERTICAL = '|';
    static final char HORIZONTAL = '-';
    static final char BLANK = ' ';
    static final char CORNER = '+';
    static final char STAR = 'X';

    private Dinglemouse(char[][] grid) {

        this.numRows = grid.length;
        this.numCols = grid[0].length;
        this.grid = new char[numRows * numCols];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j ++) {
                this.grid[i * numCols + j] = grid[i][j];
            }
        }
    }

    private int[] getMoveVector(String move) {
        switch (move) {
            case MOVE_UP:
                return new int[] {-1, 0};
            case MOVE_RIGHT:
                return new int[] {0, 1};
            case MOVE_LEFT:
                return new int[] {0, -1};
            case MOVE_DOWN:
                return new int[] {1, 0};
            default:
                return new int[] {0, 0};
        }
    }

    private char getPos(int x, int y, char[] grid) {
        if (x >= 0 && x < numRows && y >= 0 && y < numCols) {
            return grid[x * numCols + y];
        }
        return '\0';
    }

    private List<String> getValidMoves(int x, int y, char[] grid) {

        List<String> moves = new ArrayList<>();


        switch (getPos(x, y, grid)) {
            case VERTICAL:
                if (x > 0) {
                    if (getPos(x - 1, y, grid) == VERTICAL) {
                        moves.add(MOVE_UP);
                    }
                    if (getPos( x - 1, y, grid) == CORNER) {
                        if (isCornerValid(x - 1, y, grid)) {
                            moves.add(MOVE_UP);
                        }
                    }
                }
                if (x < numRows) {
                    if (getPos(x + 1, y, grid) == VERTICAL) {
                        moves.add(MOVE_DOWN);
                    }
                    if (getPos(x +1, y, grid) == CORNER) {
                        if (isCornerValid(x + 1, y, grid)) {
                            moves.add(MOVE_DOWN);
                        }
                    }
                }
                break;
            case HORIZONTAL:
                if (y > 0) {
                    switch(getPos(x, y - 1, grid)) {
                        case HORIZONTAL:
                            moves.add(MOVE_LEFT);
                            break;
                        case CORNER:
                            if (isCornerValid(x, y - 1, grid)) {
                                moves.add(MOVE_LEFT);
                            }
                            break;
                        case STAR:
                            moves.add(FINISH);
                            break;
                        default:
                            break;
                    }
                }
                if (y < numCols) {

                    switch(getPos(x, y + 1, grid)) {
                        case HORIZONTAL:
                            moves.add(MOVE_RIGHT);
                            break;
                        case CORNER:
                            if (isCornerValid(x, y + 1, grid)) {
                                moves.add(MOVE_RIGHT);
                            }
                            break;
                        case STAR:
                            moves.add(FINISH);
                            break;
                        default:
                            break;
                    }
                }
                break;
            case STAR:
                if (getPos(x - 1, y, grid) == VERTICAL) {
                    moves.add(MOVE_UP);
                } else if (getPos(x - 1, y, grid) == STAR) {
                    moves.add(FINISH);
                }
                if (getPos(x, y + 1, grid) == HORIZONTAL ) {
                    moves.add(MOVE_RIGHT);
                } else if (getPos(x, y + 1, grid) == STAR) {
                    moves.add(FINISH);
                }
                if (getPos(x + 1, y, grid) == VERTICAL) {
                    moves.add(MOVE_DOWN);
                } else if (getPos(x + 1, y, grid) == STAR) {
                    moves.add(FINISH);
                }
                if (getPos(x, y - 1, grid) == HORIZONTAL) {
                    moves.add(MOVE_LEFT);
                } else if (getPos(x, y - 1, grid) == STAR) {
                    moves.add(FINISH);
                }
                if (moves.size() > 1) {
                    return new ArrayList<>();
                }
                break;

        }
        return moves;
    }

    public char[] getGridCopy(char[] grid) {
        return Arrays.copyOf(grid, numCols * numRows);
    }

    private boolean isCornerValid(int x, int y, char[] grid) {
        int dirs = 0;
        dirs += getPos(x -1 , y, grid) == VERTICAL ? 1 : 0;
        dirs += getPos(x, y + 1, grid) == HORIZONTAL ? 1 : 0;
        dirs += getPos(x + 1, y, grid) == VERTICAL ? 1 : 0;
        dirs += getPos(x, y - 1, grid) == HORIZONTAL ? 1 : 0;

        return dirs == 2;
    }

    private int[] findStart(char[] grid) {
        int[] pos = new int[2];
        int index = new String(grid).indexOf(STAR);
        if (index != -1 ) {
            pos[0] = index / numCols;
            pos[1] = index % numCols;
        }
        return pos;
    }

    private int[] move(int[] pos, int[] where, char[] grid) {
        int[] newPos = {pos[0], pos[1]};
        if (pos[0] + where[0] > 0
                && pos[0] + where[0] < numRows
                && pos[1] + where[1] > 0
                && pos[1] + where[1] < numCols) {
            newPos[0] += where[0];
            newPos[1] += where[1];
            grid[pos[0] * numCols + pos[1]] = ' ';

        }
        return newPos;
    }

    private boolean followPath(int x, int y, char[] grid) {

        int numPaths = 0;

        while (numPaths == 0) {
            List<String> moves = getValidMoves(x, y, grid);
            if (moves.isEmpty()) {
                return false;
            }
            if (moves.size() == 1) {
                if (moves.contains(FINISH)) {
                    numPaths++;
                } else {
                    int[] newPos = move(new int[]{x, y}, getMoveVector(moves.get(0)), grid);
                    x = newPos[0];
                    y = newPos[1];
                }
            } else {
                int count = 0;
                for (String move: moves) {
                    grid[x * numCols + y] = ' ';
                    char[] newGrid = getGridCopy(grid);
                    int[] newPos = move(new int[]{x, y}, getMoveVector(move), grid);
                    if (followPath(newPos[0], newPos[1], newGrid)) {
                        numPaths++;
                    }
                }
            }
        }
        return numPaths == 1;
    }

    public void printGrid(char[][] grid) {

        for (int i = 0; i< numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                System.out.print(grid[i][j]);
            }
            System.out.println("");
        }
    }

    public char[] getGrid() {
        return this.grid;
    }

    public static boolean line(final char [][] grid) {

        Dinglemouse dinglemouse = new Dinglemouse(grid);
        dinglemouse.printGrid(grid);

        int[] pos = dinglemouse.findStart(dinglemouse.getGrid());
        return dinglemouse.followPath(pos[0], pos[1], dinglemouse.getGrid());

    }

}