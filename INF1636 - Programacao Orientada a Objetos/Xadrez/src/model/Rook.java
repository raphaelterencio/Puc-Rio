package model;

import java.util.ArrayList;
import java.util.List;

class Rook extends Piece{

	private boolean hasMoved;
	
	protected Rook(char color)
	{ 
		super(color); 
		this.setSymbol('R');
		this.hasMoved = false;
	}
	
	protected void pieceMoved() { this.hasMoved = true; }
	
	protected boolean hasMoved() { return this.hasMoved; }
	
	@Override
	// Frente/trás, esquerda/direita
	protected boolean canMove(int row, int column, int target_row, int target_column) 
	{
		if (row == target_row || column == target_column) return true;

		return false;
	}
	
	@Override
	protected List<int[]> getPath(int row, int column, int target_row, int target_column)
	{	
		List<int[]> path = new ArrayList<>();
		
		// Não confere se o movimento for impossível
		if( !canMove(row,column, target_row, target_column) ) 
			return path;
		
		// Auxiliares para percorrer o caminho da peça
		int step_row, step_column;
		
		if (target_row > row) step_row = 1; // Para baixo
		else if (target_row < row) step_row = -1; // Para cima
		else step_row = 0; // Se mantém na mesma linha
		
		if (target_column > column) step_column = 1; // Para direita
		else if (target_column < column) step_column = -1; // Para esquerda
		else step_column = 0; // Se mantém na mesma coluna
		
		int current_row = row + step_row; 
		int current_column = column + step_column;
		
		while(current_row != target_row || current_column != target_column)
		{
			path.add(new int[] {current_row, current_column});
			current_row += step_row;
			current_column += step_column;
		}
		path.add(new int[] {current_row, current_column});

		return path;
	}
}