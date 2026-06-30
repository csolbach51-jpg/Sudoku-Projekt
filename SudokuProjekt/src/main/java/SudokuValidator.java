/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author csolbach51
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class SudokuValidator {

    public static void main(String[] args) {
        // Pfad zu deiner generierten Rätsel-Datei anpassen
        String filePath; 
        filePath = "/home/csolbach51/NetbeansProjekts/Sudoku-Projekt/SudokuProjekt/sudos.txt";
        validateFile(filePath);
    }

    public static void validateFile(String filePath) {
        int errorCount = 0;
        int totalCount = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue; // Leere Zeilen überspringen
                
                totalCount++;
                
                // Falls deine Datei Header oder Nummern enthält (z.B. "Rätsel 79: ...")
                // müsstest du den String hier vorher splitten, sodass nur die 81 Zeichen übrig bleiben.
                
                if (!isValidSudokuGrid(line)) {
                    System.err.println("❌ Fehlerhaftes Rätsel gefunden in Zeile/Index " + totalCount + "!");
                    System.err.println("   String: " + line);
                    errorCount++;
                }
            }
            
            System.out.println("\n--- VALIDIERUNG BEENDET ---");
            System.out.println("Geprüfte Rätsel gesamt: " + totalCount);
            System.out.println("Gefundene Fehler: " + errorCount);
            
        } catch (IOException e) {
            System.err.println("Fehler beim Lesen der Datei: " + e.getMessage());
        }
    }

    private static boolean isValidSudokuGrid(String gridStr) {
        // Sicherstellen, dass die Länge passt
        if (gridStr.length() != 81) {
            return false; 
        }

        char[][] grid = new char[9][9];
        for (int i = 0; i < 81; i++) {
            grid[i / 9][i % 9] = gridStr.charAt(i);
        }

        // 1. Zeilen prüfen
        for (int row = 0; row < 9; row++) {
            Set<Character> set = new HashSet<>();
            for (int col = 0; col < 9; col++) {
                if (!isClue(grid[row][col])) continue;
                if (!set.add(grid[row][col])) return false; // Doppelt in Zeile
            }
        }

        // 2. Spalten prüfen
        for (int col = 0; col < 9; col++) {
            Set<Character> set = new HashSet<>();
            for (int row = 0; row < 9; row++) {
                if (!isClue(grid[row][col])) continue;
                if (!set.add(grid[row][col])) return false; // Doppelt in Spalte
            }
        }

        // 3. 3x3 Blöcke prüfen
        for (int block = 0; block < 9; block++) {
            Set<Character> set = new HashSet<>();
            int rowOffset = (block / 3) * 3;
            int colOffset = (block % 3) * 3;
            
            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 3; c++) {
                    char val = grid[rowOffset + r][colOffset + c];
                    if (!isClue(val)) continue;
                    if (!set.add(val)) return false; // Doppelt im Block
                }
            }
        }

        return true;
    }

    private static boolean isClue(char c) {
        return c >= '1' && c <= '9';
    }
}
