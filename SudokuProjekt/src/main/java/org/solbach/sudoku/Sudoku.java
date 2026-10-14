package org.solbach.sudoku;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Sudoku extends Frame implements TextListener, ActionListener {

    public TextField[][] inputs = new TextField[9][9];
    public TextField[][] ip = new TextField[9][9];
    private Button buttonCreate; 
    private Button buttonSolve;  
    private Panel raster;
    
    private int[][] currentSolution = new int[9][9];
    
    // Zielverzeichnis im Home-Ordner des Benutzers
    private final String TARGET_DIR = System.getProperty("user.home") + File.separator + "Sudokubuch";

    public Sudoku() {
        super("SUDOKU MASTER CHALLENGE");
        initUI(this);
        
        // Ordner erstellen und 360 Rätsel über 3 Stufen exportieren
        createTargetDirectory();
        generate360PuzzlesWithLevels();
    }

    private void createTargetDirectory() {
        File dir = new File(TARGET_DIR);
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                System.out.println("Ordner erstellt: " + TARGET_DIR);
            }
        }
    }

    public void clearMainField() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (inputs[i][j] != null) inputs[i][j].setText("");
                if (ip[i][j] != null) ip[i][j].setText("");
            }
        }
    }

        // Schreibt das Gitter kompakt als 81 Zeichen in die Textdateien
    // PRÜFT JETZT LIVE BEIM SCHREIBEN AUF DOPPELTE ZAHLEN!
    private void writeGridToFile(int[][] grid, String filename) {
        // Wenn wir in die Spieldatei schreiben, machen wir den ultimativen Sicherheits-Check
        if (filename.equals("sudos.txt")) {
            if (!isFinalGridValid(grid)) {
                System.out.println("WARNUNG: Ein fehlerhaftes Sudoku mit Doppelungen wurde abgefangen und nicht geschrieben!");
                return; // Illegaler Zustand: Schreibvorgang wird blockiert!
            }
        }

        File file = new File(TARGET_DIR + File.separator + filename);
        try (FileWriter writer = new FileWriter(file, true)) {
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    writer.write(grid[i][j] == 0 ? "0" : Integer.toString(grid[i][j]));
                }
            }
            writer.write("\n");
        } catch (IOException e) {
            System.out.println("Fehler beim Schreiben in die Datei " + filename + ": " + e.getMessage());
        }
    }

    // Ein absolut sicherer, isolierter Zeilen-, Spalten- und Block-Prüfer vor dem Dateiexport
    private boolean isFinalGridValid(int[][] grid) {
        // 1. Zeilen prüfen
        for (int r = 0; r < 9; r++) {
            boolean[] seen = new boolean[10];
            for (int c = 0; c < 9; c++) {
                int val = grid[r][c];
                if (val != 0) {
                    if (seen[val]) return false; // DOPPELTE ZAHL IN ZEILE GEFUNDEN!
                    seen[val] = true;
                }
            }
        }

        // 2. Spalten prüfen
        for (int c = 0; c < 9; c++) {
            boolean[] seen = new boolean[10];
            for (int r = 0; r < 9; r++) {
                int val = grid[r][c];
                if (val != 0) {
                    if (seen[val]) return false; // DOPPELTE ZAHL IN SPALTE GEFUNDEN!
                    seen[val] = true;
                }
            }
        }

        // 3. Blöcke prüfen
        for (int block = 0; block < 9; block++) {
            boolean[] seen = new boolean[10];
            int rowStart = (block / 3) * 3;
            int colStart = (block % 3) * 3;
            for (int r = rowStart; r < rowStart + 3; r++) {
                for (int c = colStart; c < colStart + 3; c++) {
                    int val = grid[r][c];
                    if (val != 0) {
                        if (seen[val]) return false; // DOPPELTE ZAHL IM BLOCK GEFUNDEN!
                        seen[val] = true;
                    }
                }
            }
        }
        return true; // Gitter ist absolut fehlerfrei
    }
    
    public void generate360PuzzlesWithLevels() {
    Random rnd = new Random();
    System.out.println("Starte Massengenerierung (3 Stufen mit Ausbruchschutz)...");
    
    File sudoFile = new File(TARGET_DIR + File.separator + "sudos.txt");
    File loesFile = new File(TARGET_DIR + File.separator + "lösungen.txt");
    sudoFile.delete();
    loesFile.delete();

    for (int num = 1; num <= 360; num++) {
        boolean puzzleValid = false;

        // Dieser Zähler verhindert, dass wir bei einem "schlechten" Gitter feststecken
        int masterAttempts = 0; 

        while (!puzzleValid && masterAttempts < 200) {
            masterAttempts++;
            int[][] solutionGrid = new int[9][9];
            
            if (fillBoard(solutionGrid, rnd)) {
                int[][] puzzleGrid = new int[9][9];
                for (int i = 0; i < 9; i++) {
                    System.arraycopy(solutionGrid[i], 0, puzzleGrid[i], 0, 9);
                }

                // Exakte Zielvorgaben für die Buchaufteilung (1-120 / 121-240 / 241-360)
                int targetRemove = (num <= 120) ? 36 : (num <= 240) ? 50 : 54; 

                ArrayList<Integer> positions = new ArrayList<>();
                for (int i = 0; i < 81; i++) positions.add(i);
                Collections.shuffle(positions, rnd);

                int removed = 0;

                for (int pos : positions) {
                    if (removed >= targetRemove) break;

                    int x = pos / 9;
                    int y = pos % 9;
                    int targetX = 8 - x;
                    int targetY = 8 - y;

                    if (puzzleGrid[x][y] != 0) {
                        int temp1 = puzzleGrid[x][y];
                        int temp2 = puzzleGrid[targetX][targetY];

                        puzzleGrid[x][y] = 0;
                        puzzleGrid[targetX][targetY] = 0;

                        if (countSolutions(puzzleGrid, 0, 0) == 1) {
                            removed += (x == targetX && y == targetY) ? 1 : 2;
                        } else {
                            puzzleGrid[x][y] = temp1;
                            puzzleGrid[targetX][targetY] = temp2;
                        }
                    }
                }

                // Validierungs-Check für den erfolgreichen Export
                if (num <= 120 && removed >= targetRemove) {
                    puzzleValid = true;
                } else if (num <= 240 && removed >= 48) { // Tolleranz für Mittel
                    puzzleValid = true;
                } else if (num > 240 && removed >= 44) { // Stabile Untergrenze für Eindeutigkeit bei "Schwer"
                    puzzleValid = true;
                }

                if (puzzleValid) {
                    writeGridToFile(puzzleGrid, "sudos.txt");
                    writeGridToFile(solutionGrid, "lösungen.txt");
                }
            }
        }
        
        // Falls ein Rätsel nach 200 Versuchen nicht klappt, erzwingen wir den Durchbruch
        if (!puzzleValid) {
            num--; // Schleifendurchlauf für diese Nummer wiederholen mit komplett neuem Gitter!
        }
    }
    System.out.println("Massengenerierung beendet. Alle 360 Rätsel stabil exportiert!");
}


    
    private boolean check(int[][] grid, int row, int col, int val) {
    // 1. Zeile prüfen
    for (int c = 0; c < 9; c++) {
        if (grid[row][c] == val) return false;
    }

    // 2. Spalte prüfen
    for (int r = 0; r < 9; r++) {
        if (grid[r][col] == val) return false;
    }

    // 3. 3x3-Block prüfen
    int rowStart = (row / 3) * 3;
    int colStart = (col / 3) * 3;
    for (int r = rowStart; r < rowStart + 3; r++) {
        for (int c = colStart; c < colStart + 3; c++) {
            if (grid[r][c] == val) return false;
        }
    }

    return true; // Zahl darf hier platziert werden
}



    // Unabhängiger, unfehlbarer Validierungs-Scanner für das Startgitter
    private boolean isGridValidFromStart(int[][] grid) {
        // Prüfe alle 9 Zeilen separat auf doppelte Zahlen
        for (int r = 0; r < 9; r++) {
            boolean[] seen = new boolean[10];
            for (int c = 0; c < 9; c++) {
                int val = grid[r][c];
                if (val != 0) {
                    if (seen[val]) return false; // Zahl doppelt in dieser Zeile!
                    seen[val] = true;
                }
            }
        }

        // Prüfe alle 9 Spalten separat auf doppelte Zahlen
        for (int c = 0; c < 9; c++) {
            boolean[] seen = new boolean[10];
            for (int r = 0; r < 9; r++) {
                int val = grid[r][c];
                if (val != 0) {
                    if (seen[val]) return false; // Zahl doppelt in dieser Spalte!
                    seen[val] = true;
                }
            }
        }

        // Prüfe alle neun 3x3 Blöcke separat auf doppelte Zahlen
        for (int block = 0; block < 9; block++) {
            boolean[] seen = new boolean[10];
            int rowStart = (block / 3) * 3;
            int colStart = (block % 3) * 3;
            for (int r = rowStart; r < rowStart + 3; r++) {
                for (int c = colStart; c < colStart + 3; c++) {
                    int val = grid[r][c];
                    if (val != 0) {
                        if (seen[val]) return false; // Zahl doppelt in diesem 3x3 Block!
                        seen[val] = true;
                    }
                }
            }
        }

        return true; // Das Rätsel ist im Ausgangszustand absolut fehlerfrei und legal!
    }


    
    
    
    
    public void performCreate() throws IllegalArgumentException {
        clearMainField();
        Random rnd = new Random();
        int[][] solutionGrid = new int[9][9];
        
        if (fillBoard(solutionGrid, rnd)) {
            for (int i = 0; i < 9; i++) {
                System.arraycopy(solutionGrid[i], 0, currentSolution[i], 0, 9);
            }
            int[][] puzzleGrid = new int[9][9];
            for (int i = 0; i < 9; i++) {
                System.arraycopy(solutionGrid[i], 0, puzzleGrid[i], 0, 9);
            }
            int cellsToRemove = 49; 
            while (cellsToRemove > 0) {
                int x = rnd.nextInt(9);
                int y = rnd.nextInt(9);
                if (puzzleGrid[x][y] != 0) {
                    puzzleGrid[x][y] = 0;
                    cellsToRemove--;
                }
            }
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (puzzleGrid[i][j] != 0) {
                        String valStr = Integer.toString(puzzleGrid[i][j]);
                        inputs[i][j].setText(valStr);
                        ip[i][j].setText(valStr);
                    } else {
                        inputs[i][j].setText("");
                        ip[i][j].setText("");
                    }
                }
            }
        }
    }

    private boolean fillBoard(int[][] grid, Random rnd) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (grid[row][col] == 0) {
                    ArrayList<Integer> numbers = new ArrayList<>();
                    for (int i = 1; i <= 9; i++) numbers.add(i);
                    Collections.shuffle(numbers, rnd);
                    for (int num : numbers) {
                        if (isValid(grid, row, col, num)) {
                            grid[row][col] = num;
                            if (fillBoard(grid, rnd)) return true;
                            grid[row][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValid(int[][] grid, int row, int col, int num) {
        for (int i = 0; i < 9; i++) {
            if (grid[row][i] == num || grid[i][col] == num) return false;
        }
        int boxRowStart = row - row % 3;
        int boxColStart = col - col % 3;
        for (int r = boxRowStart; r < boxRowStart + 3; r++) {
            for (int c = boxColStart; c < boxColStart + 3; c++) {
                if (grid[r][c] == num) return false;
            }
        }
        return true;
    }
     @Override
    public void textValueChanged(TextEvent ev) {}

    @Override
    public void actionPerformed(ActionEvent ev) {
        if (ev.getSource() == buttonCreate || ev.getActionCommand().equals("Create")) {
            try { 
                performCreate(); 
            } catch (IllegalArgumentException ex) { 
                clearMainField(); 
            }
        }
        else if (ev.getSource() == buttonSolve || ev.getActionCommand().equals("Solve")) {
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (currentSolution[i][j] != 0) {
                        inputs[i][j].setText(Integer.toString(currentSolution[i][j]));
                        ip[i][j].setText(Integer.toString(currentSolution[i][j]));
                    }
                }
            }
        }
    }

    private void initUI(Sudoku s) {
        s.setLayout(new FlowLayout());
        s.setSize(540, 600);
        raster = new Panel();
        raster.setLayout(new GridLayout(9, 9, 2, 2));
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                s.inputs[i][j] = new TextField(1);
                s.ip[i][j] = new TextField(1);
                s.inputs[i][j].setFont(new Font("SansSerif", Font.BOLD, 25));
                s.inputs[i][j].addTextListener(s);
                raster.add(s.inputs[i][j]);
            }
        }
        buttonCreate = new Button("Generieren");
        buttonCreate.setActionCommand("Create");
        buttonCreate.addActionListener(s);
        buttonSolve = new Button("Lösen");
        buttonSolve.setActionCommand("Solve");
        buttonSolve.addActionListener(s);
        s.add(raster);
        s.add(buttonCreate);
        s.add(buttonSolve);
        s.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) { System.exit(0); }
        });
        s.setVisible(true);
    }

    // Diese neue Hilfsmethode zählt, wie viele Lösungen ein Gitter hat
private int countSolutions(int[][] grid, int row, int col) {
    // Wenn wir am Ende des Brettes sind, haben wir EINE Lösung gefunden
    if (row == 9) return 1;

    int nextRow = (col == 8) ? row + 1 : row;
    int nextCol = (col == 8) ? 0 : col + 1;

    if (grid[row][col] != 0) {
        return countSolutions(grid, nextRow, nextCol);
    }

    int solutionsFound = 0;
    for (int val = 1; val <= 9; val++) {
        if (check(grid, row, col, val)) { // Standard-Validitäts-Check
            grid[row][col] = val;
            solutionsFound += countSolutions(grid, nextRow, nextCol);
            grid[row][col] = 0; // Backtrack
            
            // Performance-Optimierung: Wenn wir schon mehr als 1 Lösung 
            // gefunden haben, können wir sofort abbrechen (Rätsel ist ungültig!)
            if (solutionsFound > 1) return solutionsFound;
        }
    }
    return solutionsFound;
}

    
    public static void main(String[] args) {
        new Sudoku();
    }
}