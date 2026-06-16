package org.solbach.sudoku;
import java.util.*;


class ValueAndCandidatesSudoku
{
	Integer result = null;
	boolean[] candidates = new boolean[10];

	ValueAndCandidatesSudoku(Integer value)
	{
		result = value;
		candidates[0] = false;
	}
	
	ValueAndCandidatesSudoku(LinkedList impossiblecandidates)
	{
		candidates[0] = true;	

		for(var i = 1;i<10;i++)
		{
			candidates[i] = true;
			if(impossiblecandidates.indexOf(i)> -1)
				candidates[i] = false;
		}
	}

	
	
}