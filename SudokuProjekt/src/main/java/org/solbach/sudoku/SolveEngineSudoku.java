package org.solbach.sudoku;
//import org.solbach.sudoku.*;
import java.util.*;
import java.awt.*;
import static java.util.Arrays.deepEquals;

class SolveEngineSudoku
{

        static boolean comperator(ValueAndCandidatesSudoku[][] a, ValueAndCandidatesSudoku[][] b)
        {
          var result = true;
          for (var i = 1;i<10;i++)
          {
                result = result && (deepEquals(a[i],b[i]));
          }
          return(result);
        }
        
        static boolean valuechanged = false;
        static void solver(TextField[][] inputs)
        {
                        ValueAndCandidatesSudoku[][] vcs = null;
                        vcs = setCandidates(inputs);
			do
			{
                                 valuechanged = false;



				 for(var i = 1;i<10;i++)
				 {
				 	for(var k = 1;k<10;k++)
					{

				 		vcs[i][k] = solveLonelys(vcs[i][k]);


						if(vcs[i][k].candidates[0]!= true)
						{
							inputs[i-1][k-1].setText(vcs[i][k].result.toString());
							vcs = setCandidates(inputs);

						}

					}
     }

				vcs = setCandidates(inputs);
				vcs = solveRow(vcs);
				for (var i = 1;i<10;i++)
				{
					for(var k = 1;k<10;k++)
					{
						if(vcs[i][k].candidates[0]!= true)
						{
                                                  inputs[i-1][k-1].setText(vcs[i][k].result.toString());
						}
					}
				}
				vcs = setCandidates(inputs);
				vcs = solveColumn(vcs);
				for (var i = 1;i<10;i++)
				{
					for(var k = 1;k<10;k++)
					{
						if(vcs[i][k].candidates[0]!= true)
						{
                                                  inputs[i-1][k-1].setText(vcs[i][k].result.toString());
						}
					}
				}
				vcs = setCandidates(inputs);
				vcs = solveRect(vcs);
				for (var i = 1;i<10;i++)
				{
					for(var k = 1;k<10;k++)
					{
						if(vcs[i][k].candidates[0]!= true)
						{
							inputs[i-1][k-1].setText(vcs[i][k].result.toString());
						}
					}
				}
			/*	vcs = setCandidates(inputs);
				for (int i = 1;i<10;i++)
				{
					for(int k = 1;k<10;k++)
					{
						if(vcs[i][k].candidates[0]!= true)
							inputs[i-1][k-1].setText(vcs[i][k].result.toString());
					}
				}*/

		        //	vcs = setCandidates(inputs);
			}
			while(valuechanged);
		//	System.out.println("fertig");
        }


	static ValueAndCandidatesSudoku solveLonelys(ValueAndCandidatesSudoku vcs)
					{
						var countvalues = 0;
						for (var i = 1;i<10;i++)
						{
							if(vcs.candidates[i])
							 countvalues++;
						}
						if(countvalues == 1)
						{	int i;
							for(i = 1;i<10;i++)
							if(vcs.candidates[i])
							{
                                                               valuechanged = true;
								return(new ValueAndCandidatesSudoku(i));
							}
						}
						return(vcs);

					}

	static ValueAndCandidatesSudoku[][] solveRect(ValueAndCandidatesSudoku vcs[][])
					    {
						for(var i  = 1;i < 10;i++)
						{
							for(var m = 0;m<9;m = m + 3)
							{
								for(var j = 0;j < 9;j = j+3)
								{
									var countvaluesquad = 0;
									for(var k = 1;k < 4;k++)
									{
										for(var l = 1;l<4;l++)
										{
											if(vcs[j+l][m+k].candidates[i])
											countvaluesquad++;
										}
									}
									if(countvaluesquad == 1)
									for(var k = 1;k < 4;k++)
									{
										for(var l = 1;l<4;l++)
										{
											if(vcs[j+l][m+k].candidates[i])
											{
												vcs[j+l][m+k] = new ValueAndCandidatesSudoku(i);
												valuechanged=true;
											}


										}
									}
								}
							}
						}
						return(vcs);
				 	}
	static ValueAndCandidatesSudoku[][] solveRow(ValueAndCandidatesSudoku vcs[][])
						{
							for(var i  = 1;i < 10;i++)
							{
								for(var j = 1;j < 10;j++)
								{
									var countvaluesrow = 0;

									for(var k = 1;k < 10;k++)
									{
										if(vcs[j][k].candidates[i])
											countvaluesrow++;
									}
									if(countvaluesrow == 1)
									{
										int l;
										for(l = 1;l<10;l++)
										if(vcs[j][l].candidates[i])
										{
											vcs[j][l] = new ValueAndCandidatesSudoku(i);
											valuechanged=true;
										}
									}
								}
							}
							return(vcs);
						}

	static ValueAndCandidatesSudoku[][] solveColumn(ValueAndCandidatesSudoku vcs[][])
						{
							for(var i  = 1;i < 10;i++)
							{
								for(var j = 1;j < 10;j++)
								{
									var countvaluescolumn = 0;
									for(var k = 1;k < 10;k++)
									{
										if(vcs[k][j].candidates[i])
												countvaluescolumn++;
									}
									if(countvaluescolumn == 1)
									{
										int l;
										for(l = 1;l<10;l++)
										if(vcs[l][j].candidates[i])
										{
											vcs[l][j] = new ValueAndCandidatesSudoku(i);
											valuechanged=true;
										}
									}
								}
							}
							return(vcs);
						}



	static ValueAndCandidatesSudoku[][] setCandidates(TextField[][] inputs)
	{
		var values = new ValueAndCandidatesSudoku[10][10];
		for(var i = 1;i<10;i++)
		{
			for (var j = 1;j<10;j++)
			{		var ll = new LinkedList();
				if(inputs[i-1][j-1].getText().equals(""))
		                {
					for(var k = 1;k<10;k++)
					{
						if(inputs[i-1][k-1].getText().equals("")!=true)
							ll.add(Integer.valueOf(inputs[i-1][k-1].getText()));
						if(inputs[k-1][j-1].getText().equals("") !=true)
							ll.add(Integer.valueOf(inputs[k-1][j-1].getText()));
					}
					for(var u = 0;u < 3; u++)
					{
						for(var v = 0; v < 3;v++)
						{
							if(inputs[(i-1)-(i-1)%3+u][(j-1)-(j-1)%3+v].getText().equals("")!=true)
							{
								ll.add(Integer.valueOf(inputs[(i-1)-(i-1)%3+u][(j-1)-(j-1)%3+v].getText()));
							}
						}
					}

					values[i][j] = new ValueAndCandidatesSudoku(ll);
				}
				else
				{
					values[i][j] = new ValueAndCandidatesSudoku(Integer.valueOf(inputs[i-1][j-1].getText()));
				}
			}
		}
		return(values);
	}
        
        
        // Prüft, ob 'num' an 'row, col' gesetzt werden darf
private static boolean isSafe(TextField[][] inputs, int row, int col, int num) {
    String numStr = String.valueOf(num);
    for (int i = 0; i < 9; i++) {
        // Zeile und Spalte prüfen
        if (inputs[row][i].getText().equals(numStr) || inputs[i][col].getText().equals(numStr)) return false;
    }
    // 3x3 Block prüfen
    int startRow = row - row % 3, startCol = col - col % 3;
    for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
            if (inputs[i + startRow][j + startCol].getText().equals(numStr)) return false;
        }
    }
    return true;
}

// Der rekursive Solver
public static boolean solveWithBacktracking(TextField[][] inputs) {
    for (int row = 0; row < 9; row++) {
        for (int col = 0; col < 9; col++) {
            if (inputs[row][col].getText().equals("")) { // Leeres Feld gefunden
                for (int num = 1; num <= 9; num++) {
                    if (isSafe(inputs, row, col, num)) {
                        inputs[row][col].setText(String.valueOf(num)); // "Raten"

                        if (solveWithBacktracking(inputs)) return true; // Rekursion

                        inputs[row][col].setText(""); // Backtrack (Reset)
                    }
                }
                return false; // Keine Zahl passt, zurück zum vorherigen Schritt
            }
        }
    }
    return true; // Alle Felder gefüllt
}
        
        }


