import os

# Pfade zu Ihren Dateien
INPUT_FILE = "/home/csolbach51/NetbeansProjekts/Sudoku-Projekt/SudokuProjekt/sudos.txt"
OUTPUT_LEICHT = "/home/csolbach51/NetbeansProjekts/Sudoku-Projekt/SudokuProjekt/sudos_leicht.txt"
OUTPUT_MITTEL = "/home/csolbach51/NetbeansProjekts/Sudoku-Projekt/SudokuProjekt/sudos_mittel.txt"
OUTPUT_SCHWER = "/home/csolbach51/NetbeansProjekts/Sudoku-Projekt/SudokuProjekt/sudos_schwer.txt"

def count_givens(sudoku_str):
    """Zählt, wie viele vorgegebene Zahlen im Sudoku sind."""
    clean_str = sudoku_str.replace(",", "").replace("\n", "").replace(" ", "")
    return sum(1 for char in clean_str if char not in ('0', '.', ''))

def sort_sudokus():
    if not os.path.exists(INPUT_FILE):
        print(f"Fehler: Die Datei {INPUT_FILE} wurde nicht gefunden.")
        return

    leicht_count = 0
    mittel_count = 0
    schwer_count = 0
    ignoriert_count = 0

    # Alle Ausgabedateien öffnen
    with open(INPUT_FILE, "r") as infile, \
         open(OUTPUT_LEICHT, "w") as out_leicht, \
         open(OUTPUT_MITTEL, "w") as out_mittel, \
         open(OUTPUT_SCHWER, "w") as out_schwer:
        
        current_sudoku = ""
        
        for line in infile:
            if line.strip() == "":
                continue
                
            current_sudoku += line
            check_len = current_sudoku.replace(",", "").replace("\n", "").replace(" ", "")
            
            # Größer oder gleich 81 fängt auch kleine Formatfehler ab
            if len(check_len) >= 81:
                # Falls exakt 81 oder knapp drüber, nehmen wir die Givens
                givens = count_givens(current_sudoku)
                
                if givens > 31:
                    out_leicht.write(current_sudoku + "\n")
                    leicht_count += 1
                elif 27 <= givens <= 31:
                    out_mittel.write(current_sudoku + "\n")
                    mittel_count += 1
                elif givens < 27:
                    out_schwer.write(current_sudoku + "\n")
                    schwer_count += 1
                
                # Immer zurücksetzen, um Kettenreaktionen bei Fehlern zu vermeiden
                current_sudoku = ""

    print("--- Sortierung erfolgreich beendet ---")
    print(f"📁 Leichte Sudokus exportiert:  {leicht_count}  (Zahlen > 31)")
    print(f"📁 Mittlere Sudokus exportiert: {mittel_count} (Zahlen 27-31)")
    print(f"📁 Schwere Sudokus exportiert:  {schwer_count}  (Zahlen < 27)")
    print(f"📊 Gesamt sortiert:             {leicht_count + mittel_count + schwer_count}")

if __name__ == "__main__":
    sort_sudokus()