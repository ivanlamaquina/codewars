package com.ipalacios;

import java.util.Arrays;

// Battleship field validator II
// https://www.codewars.com/kata/571ec81d7e8954ce1400014f/train/java
public class BF {

    private static final int N = 10;

    private final int[][] boardGame;
    private final int[] shipsCounter = {0, 4, 3, 2, 1};



    public BF(int[][] field) {
        this.boardGame = field;

    }

    public void print(int[][] board) {
        System.out.println();
        for (int i = 0; i < N; i++) {
            StringBuilder str = new StringBuilder("{");
            for (int j = 0; j < N; j++) {
                str.append(board[i][j]);
                if (j < N - 1) {
                    str.append(", ");
                }
            }
            str.append("}");
            if (i < N - 1) {
                str.append(",");
            }
            System.out.println(str);
        }
    }

    public int[][] cloneBoard(int[][] board) {
        int[][] newBoard = new int[N][];
        for (int i = 0; i < N; i++) {
            newBoard[i] = board[i].clone();
        }
        return newBoard;
    }

    public int[][] findShipBySize(int[][] board, int x, int y, int shipSize) {

        if (y + shipSize <= N) {
            int count = 0;
            for (int i = 0; i < shipSize && board[x][i + y] == 1; i++) {
                count++;
            }
            if (count == shipSize) {
                int[][] newBoard = this.cloneBoard(board);
                for (int i = 0; i < shipSize; i++) {
                    newBoard[x][i + y] = 0;
                }
                return newBoard;
            }
        }

        if (x + shipSize <= N) {
            int count = 0;
            for (int i = 0; i < shipSize && board[i + x][y] == 1; i++) {
                count++;
            }
            if (count == shipSize) {
                int[][] newBoard = this.cloneBoard(board);
                for (int i = 0; i < shipSize; i++) {
                    newBoard[x + i][y] = 0;
                }
                return newBoard;
            }
        }
        return null;
    }

    public boolean validate(int[][] board, int sizeToSearch) {

        if (sizeToSearch == 1) {
            return Arrays.stream(board).flatMapToInt(Arrays::stream).sum() == 4;
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (board[i][j] == 1) {
                    int[][] found = findShipBySize(board, i,j, sizeToSearch);
                    if (found != null) {
                        System.out.println("________FOUND_________");
                        this.print(found);
                        this.shipsCounter[sizeToSearch]--;
                        if (validate(found, this.shipsCounter[sizeToSearch] == 0 ? sizeToSearch -1: sizeToSearch)) {
                            return true;
                        }
                        this.shipsCounter[sizeToSearch]++;
                    }
                }
            }
        }
        return false;
    }

    public boolean validate() {
        this.print(this.boardGame);
        if (Arrays.stream(this.boardGame).flatMapToInt(Arrays::stream).sum() != 20) {
            return false;
        }
        return validate(this.boardGame, 4);
    }
}
