package model;

import java.util.ArrayList;
import java.util.List;

class Horse extends Piece{

	protected Horse(char color)
	{ 
		super(color);
		this.setSymbol('H');
	}
	
	@Override
	// Movimento em L
	protected boolean canMove(int row, int column, int target_row, int target_column) 
	{
		int diff_row = Math.abs(row - target_row);
		int diff_column = Math.abs(column - target_column);
		if ( (diff_row == 2 && diff_column == 1) || (diff_row == 1 && diff_column == 2) ) return true;

		return false;
	}
	
	@Override
	protected List<int[]> getPath(int row, int column, int target_row, int target_column)
	{
		List<int[]> path = new ArrayList<>();
		
		// Não confere se o movimento for impossível
		if( !canMove(row,column, target_row, target_column) ) 
			return path;
		
		path.add(new int[] {target_row, target_column});
		
		return path;
	}
}