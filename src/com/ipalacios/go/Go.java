package com.ipalacios.go;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

// Game of Go
// https://www.codewars.com/kata/59de9f8ff703c4891900005c/train/java
public class Go {

    private static final char BLACK = 'x';
    private static final char WHITE = 'o';
    private static final char BLANK = '.';

    private Stack<Go> historial = new Stack<>();


    private int numRows, numCols;
    private char[][] board;
    private char currentTurn = BLACK;

    public Go() {
        super();
    }

    public Go(int n) {
        this(n, n);
    }

    public Go(int height, int width) {
        if (height <= 0 || width <= 0 || height >= 32 || width >= 32) {
            throw new IllegalArgumentException();
        }
        this.numRows = height;
        this.numCols = width;
        this.board = new char[numRows][numCols];
        this.reset();

        this.save();
    }

    public char[][] getBoard() {
        return this.board;
    }

    public void setBoard(char[][] board) {
        this.board = board;
    }

    public void move(String move) {
        int[] pos = parseCoords(move);

        if (this.board[pos[0]][pos[1]] != BLANK) {
            throw new IllegalArgumentException();
        }
        this.board[pos[0]][pos[1]] = this.currentTurn == BLACK ? BLACK : WHITE;
        passTurn();
    }

    public void move(String[] moves) throws IllegalArgumentException {
        for (String move : moves) {
            move(move);
        }
    }

    public void move(String move, String... moves) {
        this.move(move);
        this.move(moves);
    }

    public Map<String, Integer> getSize() {
        Map<String, Integer> map = new HashMap<>();
        map.put("height", this.numRows);
        map.put("width", this.numCols);
        return map;
    }

    public char getPosition(String coord) {
        int[] pos = parseCoords(coord);
        return this.board[pos[0]][pos[1]];
    }

    public void handicapStones(int num) throws IllegalArgumentException {
        if (this.numRows != 9 && this.numRows != 13 && this.numRows != 19) {
            throw new IllegalArgumentException();
        }
        int[][] handicap = null;
        if (this.numRows == 9) {
            handicap = new int[][]{{2, 6}, {6, 2}, {6, 6}, {2, 2}, {4, 4}};
        } else if (this.numRows == 13) {
            handicap = new int[][]{{3, 9}, {9, 3}, {9, 9}, {3, 3}, {6, 6}, {6, 3}, {6, 9}, {3, 6}, {9, 6}};
        } else if (this.numRows == 19) {
            handicap = new int[][]{{3, 15}, {15, 3}, {15, 15}, {3, 3}, {9, 9}, {9, 3}, {9, 15}, {3, 9}, {15, 9}};
        }
        for (int i = 0; i < num;  i++) {
            this.board[handicap[i][0]][handicap[i][1]] = BLACK;
        }

    }

    public String getTurn() {
        if (currentTurn == WHITE) {
            return "white";
        }
        return "black";
    }

    public void passTurn() {
        this.currentTurn = this.currentTurn == WHITE ? BLACK : WHITE;
    }

    public void reset() {
        this.currentTurn = BLACK;
        for (int i = 0; i < numRows; i++) {
            Arrays.fill(this.board[i], BLANK);
        }
    }

    public void rollBack(int num) {
        Go back = null;
        for (int i = 0; i < num && !this.historial.empty(); i++) {
            back = this.historial.pop();
        }
        this.setBoard(copyBoard(back.board));
        this.currentTurn = back.currentTurn;
    }


    private int[] parseCoords(String strCoords) {
        int[] coords = new int[2];
        int index = 0;
        while(index < strCoords.length() && String.valueOf(strCoords.charAt(index)).matches(("[0-9]"))) {
            index++;
        }
        if (index == 0 || index == strCoords.length()) {
            throw new IllegalArgumentException();
        }
        int row = Integer.valueOf(strCoords.substring(0, index)).intValue();
        if (row < 0 || row > numRows ) {
            throw new IllegalArgumentException();
        }
        coords[0] = numRows - row;
        char col = strCoords.charAt(index);
        coords[1] = (int)col < 73 ? col - 65 : col - 66;
        if (coords[1] < 0 || coords[1] > numCols) {
            throw new IllegalArgumentException();
        }
        return coords;
    }

    private void save() {
        Go stage = new Go();
        stage.setBoard(copyBoard(this.board));
        stage.currentTurn = this.currentTurn;
        this.historial.push(stage);
    }

    public char[][] copyBoard(char[][] board) {
        char[][] copy = new char[board.length][board[0].length];
        for (int i = 0; i < numRows; i++) {
            copy[i] = Arrays.copyOf(board[i], board[i].length);
        }
        return copy;

    }
}
