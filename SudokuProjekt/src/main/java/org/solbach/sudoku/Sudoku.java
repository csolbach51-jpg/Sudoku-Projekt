package org.solbach.sudoku;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.io.*;

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
  public TextField[][] inputs = null;
  public Button button = null;
  public Button button1 = null;
  public Sudoku()
  {
   inputs = new TextField[9][9];
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

        public LinkedList testForEmpty(TextField[][] inputs)
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
        public void performCreate() throws IllegalArgumentException
        {
          clearMainField();
          TextField[][] ip = new TextField[9][9];
          for (int i=0;i<9;i++)
    {
      for(int j=0;j<9;j++)
      {
                                ip[i][j] = new TextField(1);
      }
    }
          Calendar cal = new GregorianCalendar();
          String[][] initial = new String[9][9];
          LinkedList<String> ll = testForEmpty(ip);
          while ( !ll.isEmpty())
          {
                Random rnd = new Random(cal.getTimeInMillis());
                String dummy = ll.get(rnd.nextInt(ll.size()));
                int x = Integer.parseInt(dummy.substring(0,1));
                int y = Integer.valueOf(dummy.substring(1,2));
                ValueAndCandidatesSudoku candis[][] = SolveEngineSudoku.setCandidates(ip);
                int count = 0;
                for (int i = 1;i<10;i++)
                    if(candis[x+1][y+1].candidates[i])
                                            count++;
                int[] pso = new int[count];
                count = 0;
                for(int i = 1;i<10;i++)
                     if(candis[x+1][y+1].candidates[i])
                     {
                                             pso[count] = i;
                                             count++;
                     }
                String test = Integer.toString(pso[rnd.nextInt(pso.length)]);
                initial[x][y] = test;
                //System.out.println(test);
                inputs[x][y].setText(initial[x][y]);
                ip[x][y].setText(initial[x][y]);
                SolveEngineSudoku.solver(ip);
                ll = testForEmpty(ip);

           }
        }

  @Override
  public void actionPerformed(ActionEvent ev)
  {        if(ev.getSource()==button)
                       SolveEngineSudoku.solver(inputs);
                 else
                 {   try
                     {
                           FileWriter fs = new FileWriter("./sudos.txt");
                     try (BufferedWriter ds = new BufferedWriter(fs)) {
                         for(int i = 1;i<=10;i++)
                         {
                             boolean solveable = true;
                             try
                             {
                                 performCreate();
                             } catch(IllegalArgumentException ex){solveable = false;clearMainField();}
                             if(solveable)
                             {
                                 for(int k = 0;k<9;k++)
                                 {
                                     String dummy = ",";
                                     for (int l=0;l<9;l++)
                                     {
                                         if (inputs[k][l].getText().equals(""))
                                         {
                                             dummy = dummy+"0";
                                         }
                                         else
                                             dummy = dummy + inputs[k][l].getText();
                                         
                                     }
                                     //dummy=dummy;
                                     ds.write(dummy);
                                     ds.newLine();
                                     
                                 }
                                 ds.newLine();
                             } else {
                                 i--;
                             } // end of if-else
                         } }
                }
                    catch(IOException ex){}
                }
  }

  @Override
  public void textValueChanged(TextEvent te)
  {
    if(((TextField)te.getSource()).getText().matches("[123456789]"));
    else
      ((TextField)te.getSource()).setText(null);
  }


  public static void main(String args[])
  {
    frame = new Sudoku();
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
        frame.inputs[i][j] = new TextField(1);
        raster.add(frame.inputs[i][j]);
        frame.inputs[i][j].addTextListener(frame);
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
