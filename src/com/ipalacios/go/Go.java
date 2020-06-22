package com.ipalacios.go;

import java.util.Arrays;

// Game of Go
// https://www.codewars.com/kata/59de9f8ff703c4891900005c/train/java
public class Go {

    private static final char BLACK = 'x';
    private static final char WHITE = 'o';

    private int numRows, numCols;
    private char[][] board;
    private char currentTurn = BLACK;


    public Go(int n) {
        this(n, n);
    }

    public Go(int height, int width) {
        this.numRows = height;
        this.numCols = width;
        this.board = new char[numRows][numCols];

    }

    public char[][] getBoard() {
        return this.board;
    }

    public void move(String move) throws IllegalArgumentException {

    }

    public void handicapStones(int num) throws IllegalArgumentException {
        if (numCols != numRows && numRows != 9 && numRows != 13 && numRows != 19)) {
            throw new IllegalArgumentException();
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
            Arrays.fill(this.board[i], '.');
        }
    }

}
