package org.solbach.sudoku;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import javax.swing.*;
class FensterLauscher extends WindowAdapter
{
      @Override
      public void windowClosing(WindowEvent ev)
      {
        
        System.exit(0);
      }
}
@SuppressWarnings("serial")
public class Sudoku extends Frame implements TextListener,ActionListener
{


  public static Sudoku frame = null;
  public JTextField[][] inputs = null;
  public Button button = null;
  public Button button1 = null;
  public JTextField[][] ip = null;
  public Sudoku()
  {
   inputs = new JTextField[9][9];
   ip = new JTextField[9][9];
   button = new Button("Start");
   button1 = new Button("Create");
   FensterLauscher fl = new FensterLauscher();
   addWindowListener(fl);
  }
        public void clearMainField()
        {
            
                for (int i=0;i<9;i++)
    {
      for(int j=0;j<9;j++)
      {
        inputs[i][j].setText("");
      }
    }
        }
        
      public void performCreate() {
    // 1. Spielfeld komplett leeren
    clearMainField();

    // 2. Die erste Zeile zufällig mit Zahlen von 1 bis 9 belegen,
    // damit jedes der 350 Rätsel absolut einzigartig wird
    java.util.List<Integer> numbers = java.util.Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
    java.util.Collections.shuffle(numbers);
    for (int c = 0; c < 9; c++) {
        inputs[0][c].setText(String.valueOf(numbers.get(c)));
    }

    // 3. Den Backtracking-Solver das Spielfeld VOLLSTÄNDIG ausfüllen lassen
    // Da die erste Zeile gewürfelt wurde, baut er uns jedes Mal ein neues, perfektes Muster
    SolveEngineSudoku.solveWithBacktracking(inputs);

    // 4. Die Lösung in das 'ip'-Array sichern, damit dein Export (lösungen.txt) sie findet
    for (int r = 0; r < 9; r++) {
        for (int c = 0; c < 9; c++) {
            ip[r][c].setText(inputs[r][c].getText());
        }
    }

    // 5. Jetzt "kratzen" wir Zahlen für das eigentliche Rätsel heraus!
    // Für ein echtes "Master"-Sudoku lassen wir ca. 24 Zahlen stehen (wir löschen also 57)
    int ziffernZuLoeschen =36; 
    java.util.Random rand = new java.util.Random();
    
    while (ziffernZuLoeschen > 0) {
        int r = rand.nextInt(9);
        int c = rand.nextInt(9);
        
        // Nur löschen, wenn das Feld nicht schon leer ist
        if (!inputs[r][c].getText().equals("")) {
            inputs[r][c].setText("");
            ziffernZuLoeschen--;
        }
        
        
    }
    
    // Das Rätsel steht jetzt spielbereit in 'inputs', 
    // und die vollständige Lösung ist sicher in 'ip' hinterlegt!
}

        public LinkedList testForEmpty(JTextField[][] inputs)
        {
               @SuppressWarnings("unchecked")
               LinkedList<String> ll = new LinkedList();
               for(int i = 0;i<9;i++)
               {
                       for(int k=0;k<9;k++)
                       {
                                       if(inputs[i][k].getText().equals(""))
                                       {
                                              ll.add(String.valueOf(i)+String.valueOf(k));

                                       }
                       }
               }
               return(ll);
        }
       
          
        

  
  @Override
  public void textValueChanged(TextEvent te)
  {
    if(((TextComponent)te.getSource()).getText().matches("[123456789]"));
    else
      ((TextComponent)te.getSource()).setText(null);
  }
  
    /**
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button1)  { 
            try {
                // Die beiden Dateien für den Export öffnen
                java.io.FileWriter writerSudos = new java.io.FileWriter("/home/csolbach51/Sudokubuch/sudos.txt");
                java.io.FileWriter writerLoesungen = new java.io.FileWriter("/home/csolbach51/Sudokubuch/lösungen.txt");

                // Schleife für die 350 Rätsel
                for (int i = 1; i <= 350; i++) {
                    
                    // 1. Rätsel und Lösung gleichzeitig generieren
                    performCreate(); 

                    // 2. Das generierte Rätsel (mit Nullen für leere Felder) auslesen
                    StringBuilder raetselString = new StringBuilder();
                    for (int r = 0; r < 9; r++) {
                        for (int c = 0; c < 9; c++) {
                            String s = inputs[r][c].getText();
                            if (s.equals("")) {
                                raetselString.append("0");
                            } else {
                                raetselString.append(s);
                            }
                        }
                    }

                    // 3. Die fertige Lösung aus dem 'ip'-Gitter auslesen
                    StringBuilder loesungsString = new StringBuilder();
                    for (int r = 0; r < 9; r++) {
                        for (int c = 0; c < 9; c++) {
                            String s = ip[r][c].getText();
                            loesungsString.append(s);
                        }
                    }

                    // 4. In die jeweiligen Textdateien schreiben
                    writerSudos.write(raetselString.toString() + "\n");
                    writerLoesungen.write(loesungsString.toString() + "\n");
                }

                // Nach der Schleife die Streams sauber schließen
                writerSudos.close();
                writerLoesungen.close();
                
                System.out.println("350 Master-Sudokus erfolgreich generiert!");

            } catch (java.io.IOException ex) {
                ex.printStackTrace();
            }
        }    
            else if (button == e.getSource()) { 
            // Ruft deine Backtracking-Engine auf, die das Gitter im Fenster live ausfüllt!
            SolveEngineSudoku.solver(inputs); 
            System.out.println("Sudoku erfolgreich im Fenster aufgelöst!");
        
    
            
        }
    }
    

    
  public static void main(String args[])
  {  Sudoku s = new Sudoku();
    // Weise sie der statischen Variable zu
    frame = s;
 //   frame.addWindowListener(new FensterLauscher());
    frame.setLayout(new FlowLayout());
    Panel raster = new Panel();
    
    raster.setMinimumSize(new Dimension(500,500));
    raster.setMaximumSize(new Dimension(500,500));
    raster.setPreferredSize(new Dimension(500,500));
            
    
    
    
    
    raster.setLayout(new GridLayout(9,9));
    for (int i=0;i<9;i++)
    {
      for(int j=0;j<9;j++)
      {
        frame.inputs[i][j] = new JTextField(1);
        frame.ip[i][j] = new JTextField(1);
        frame.inputs[i][j].setFont(new Font("SansSerif", Font.BOLD, 25));
        raster.add(frame.inputs[i][j]);
        
        // Falls Sie JTextField (Swing) nutzen:
frame.inputs[i][j].setFocusTraversalKeysEnabled(true);

// UND absolut sicherstellen, dass das Feld den Fokus erhalten darf:
frame.inputs[i][j].setFocusable(true);
        
        
       frame.inputs[i][j].addActionListener(frame);
      }
    }
    
    
    frame.add(raster);
    frame.button.addActionListener(frame);
    frame.button1.addActionListener(frame);
    Panel input = new Panel();
    input.add(frame.button);
    input.add(frame.button1);
    frame.add(input);
    frame.pack();
    frame.setVisible(true);

  }

  

}
