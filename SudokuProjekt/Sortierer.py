import os

# Pfade zu Ihren Dateien (anpassen falls nötig)
INPUT_FILE = "sudos.txt"
OUTPUT_MITTEL = "sudos_mittel.txt"
OUTPUT_SCHWER = "sudos_schwer.txt"

def count_givens(sudoku_str):
    """Zählt, wie viele vorgegebene Zahlen (keine Nullen/Punkte) im Sudoku sind."""
    # Entfernt Kommata, Zeilenumbrüche und Leerzeichen
    clean_str = sudoku_str.replace(",", "").replace("\n", "").replace(" ", "")
    
    # Zählt alle Zeichen, die NICHT '0' oder '.' oder leer sind
    givens = sum(1 for char in clean_str if char not in ('0', '.', ''))
    return givens

def sort_sudokus():
    if not os.path.exists(INPUT_FILE):
        print(f"Fehler: Die Datei {INPUT_FILE} wurde nicht gefunden.")
        return

    mittel_count = 0
    schwer_count = 0

    # Dateien öffnen
    with open(INPUT_FILE, "r") as infile, \
         open(OUTPUT_MITTEL, "w") as out_mittel, \
         open(OUTPUT_SCHWER, "w") as out_schwer:
        
        current_sudoku = ""
        
        for line in infile:
            # Wenn eine neue Zeile mit einem Komma beginnt, ist das oft ein neues Rätsel 
            # (oder wir sammeln Zeilen, falls Sie 9x9 Blöcke mit Komma davor nutzen)
            if line.strip() == "":
                continue
                
            current_sudoku += line
            
            # Prüfen, ob wir ein vollständiges Sudoku (81 Zahlen) gesammelt haben
            # Wir entfernen Kommata und Umbrüche für die Längenprüfung
            check_len = current_sudoku.replace(",", "").replace("\n", "").replace(" ", "")
            
            if len(check_len) == 81:
                givens = count_givens(current_sudoku)
                
                # Sortierlogik nach Ihren KDP-Kriterien:
                # 27 bis 31 Zahlen -> Mittel
                # Weniger als 27 Zahlen -> Schwer / Extrem
                if 27 <= givens <= 31:
                    out_mittel.write(current_sudoku + "\n")
                    mittel_count += 1
                elif givens < 27:
                    out_schwer.write(current_sudoku + "\n")
                    schwer_count += 1
                else:
                    # Alles über 31 bleibt "Leicht" und wird hier ignoriert oder ausgesondert
                    pass
                
                # Zurücksetzen für das nächste Sudoku
                current_sudoku = ""

    print("--- Sortierung erfolgreich beendet ---")
    print(f"📁 Mittlere Sudokus exportiert: {mittel_count}")
    print(f"📁 Schwere Sudokus exportiert: {schwer_count}")

if __name__ == "__main__":
    sort_sudokus()
