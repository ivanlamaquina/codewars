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
    private boolean gameStarted = false;

    public Go() {
        super();
    }

    public Go(int n) {
        this(n, n);
    }

    public Go(int height, int width) {
        if (height <= 0 || width <= 0 || width >= 25) {
            throw new IllegalArgumentException();
        }
        this.numRows = height;
        this.numCols = width;
        this.board = new char[numRows][numCols];
        this.reset();
    }

    public char[][] getBoard() {
        return this.board;
    }

    private void setBoard(char[][] board) {
        this.board = board;
    }

    public void move(String... moves) {
        for (String move : moves) {
            this.move(move);
        }
    }

    public void move(String move) {
        int[] pos = parseCoords(move);
        int x = pos[0];
        int y = pos[1];

        if (this.board[x][y] != BLANK) {
            throw new IllegalArgumentException();
        }

        save();

        this.board[x][y] = this.currentTurn;
        capture(x, y, this.currentTurn);

        if (isCaptured(x, y, this.currentTurn, null)) {
            this.rollBack(1);
            throw new IllegalArgumentException();
        }
        changeTurn();
        if (isKo()) {
            this.rollBack(1);
            throw new IllegalArgumentException();
        }
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
        if ((this.numRows != 9 && this.numRows != 13 && this.numRows != 19) || !this.historial.empty()) {
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
        if (num > handicap.length) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < num;  i++) {
            if (this.board[handicap[i][0]][handicap[i][1]] != BLANK) {
                throw new IllegalArgumentException();
            }
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
        save();
        changeTurn();
    }

    public void reset() {
        this.historial.clear();
        this.gameStarted = false;
        this.currentTurn = BLACK;
        for (int i = 0; i < numRows; i++) {
            Arrays.fill(this.board[i], BLANK);
        }
    }

    public void rollBack(int num) {
        if (num > this.historial.size()) {
            throw new IllegalArgumentException();
        }
        Go back = null;
        for (int i = 0; i < num && !this.historial.empty(); i++) {
            back = this.historial.pop();
        }
        this.setBoard(back.board);
        this.currentTurn = back.currentTurn;
    }

    private boolean isKo() {
        if (historial.size() < 2) {
            return false;
        }
        Go old = this.historial.elementAt(this.historial.size() - 2);
        for (int i = 0; i < numRows; i++) {
            if (!Arrays.equals(this.board[i], old.board[i])) {
                return false;
            }
        }
        return true;
    }

    private void capture(int x, int y, char color) {
        char colorToCapture = color == WHITE ? BLACK : WHITE;
        if (x - 1 >= 0 && this.board[x - 1][y] == colorToCapture) {
            if (isCaptured(x - 1, y, colorToCapture, null)) {
                cleanPosition(x - 1, y, colorToCapture);
            }
        }
        if (x + 1 < numRows && this.board[x + 1][y] == colorToCapture) {
            if (isCaptured(x + 1, y, colorToCapture, null)) {
                cleanPosition(x + 1, y, colorToCapture);
            }
        }
        if (y - 1 >= 0 && this.board[x][y - 1] == colorToCapture) {
            if (isCaptured(x, y - 1, colorToCapture, null)) {
                cleanPosition(x, y - 1, colorToCapture);
            }
        }
        if (y + 1 < numCols && this.board[x][y + 1] == colorToCapture) {
            if (isCaptured(x, y + 1, colorToCapture, null)) {
                cleanPosition(x, y + 1, colorToCapture);
            }
        }
    }

    private void changeTurn() {
        this.currentTurn = this.currentTurn == WHITE ? BLACK : WHITE;
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
        stage.setBoard(copyBoard());
        stage.currentTurn = this.currentTurn;
        this.historial.push(stage);
    }

    private char[][] copyBoard() {
        char[][] copy = new char[this.board.length][this.board[0].length];
        for (int i = 0; i < numRows; i++) {
            copy[i] = Arrays.copyOf(this.board[i], this.board[i].length);
        }
        return copy;

    }

    private boolean isCaptured(int x, int y, char color, char[][] visited) {

        if (visited == null) {
            visited = new char[numRows][numCols];
            for (int i = 0; i < numRows; i++) {
                Arrays.fill(visited[i], BLANK);
            }
        }

        char value = this.board[x][y];

        char notColor = color == WHITE ? BLACK : WHITE;

        if (value == BLANK) {
            return false;
        }
        if (visited[x][y] != BLANK) {
            return true;
        }
        visited[x][y] = value;

        char top = x - 1 >= 0 ? board[x - 1][y] : notColor;
        char bottom = x + 1 < this.numRows ? this.board[x + 1][y] : notColor;
        char left = y - 1 >= 0 ? this.board[x][y - 1] : notColor;
        char right = y + 1 < numCols ? this.board[x][y + 1] : notColor;

        if (top == BLANK || bottom == BLANK || right == BLANK || left == BLANK) {
            return false;
        }

        return (top == notColor || (top == color && isCaptured(x - 1, y, color, visited)))
                && (bottom == notColor || (bottom == color && isCaptured(x + 1, y, color, visited)))
                && (left == notColor || (left == color && isCaptured(x, y - 1, color, visited)))
                && (right == notColor || (right == color && isCaptured(x, y + 1, color, visited)));

    }

    private void cleanPosition(int x, int y, char color) {
        if (this.board[x][y] == color) {
            this.board[x][y] = BLANK;
            if (x - 1 >= 0) {
                cleanPosition(x - 1, y, color);
            }
            if (x + 1 < numRows) {
                cleanPosition(x + 1, y, color);
            }
            if (y - 1 >= 0) {
                cleanPosition(x, y - 1, color);
            }
            if (y + 1 < numCols) {
                cleanPosition(x, y + 1, color);
            }
        }
    }

    public void printBoard() {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
    }
}
