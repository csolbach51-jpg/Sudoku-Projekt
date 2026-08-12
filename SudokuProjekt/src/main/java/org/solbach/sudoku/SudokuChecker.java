/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.solbach.sudoku;

/**
 *
 * @author csolbach51
 */
public class SudokuChecker {

    // Prüft, ob eine Zahl nach den Sudoku-Regeln gesetzt werden darf
    private static boolean isValid(int[][] board, int row, int col, int num) {
        for (int i = 0; i < 9; i++) {
            // Zeile & Spalte prüfen
            if (board[row][i] == num || board[i][col] == num) {
                return false;
            }
            // 3x3 Block prüfen
            int boxRow = 3 * (row / 3) + i / 3;
            int boxCol = 3 * (col / 3) + i % 3;
            if (board[boxRow][boxCol] == num) {
                return false;
            }
        }
        return true;
    }

    // Zählt die Anzahl der Lösungen (bricht ab, sobald > 1 gefunden wurden)
    public static int countSolutions(int[][] board) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (board[r][c] == 0) {
                    int solutions = 0;
                    for (int num = 1; num <= 9; num++) {
                        if (isValid(board, r, c, num)) {
                            board[r][c] = num;
                            solutions += countSolutions(board);
                            board[r][c] = 0; // Backtracking

                            // Früher Abbruch: Mehr als 1 Lösung -> Nicht eindeutig
                            if (solutions >= 2) {
                                return solutions;
                            }
                        }
                    }
                    return solutions;
                }
            }
        }
        return 1; // Ein vollständiger Lösungsweg wurde gefunden
    }

    // Hilfsmethode für eine tiefe Kopie des Arrays
    public static int[][] deepCopy(int[][] original) {
        int[][] copy = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, 9);
        }
        return copy;
    }
}
