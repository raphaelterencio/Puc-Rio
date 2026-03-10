package model;

import java.util.List;

abstract class Piece 
{
    private char color;
    private char symbol;
    
    protected Piece(char color) 
    {
        this.color = color;
        this.symbol = '-'; 
    }
    
    protected static Piece createPiece(char color, char symbol) 
    {
        switch (symbol)
        {
        	case 'B': return new Bishop(color);
            case 'H': return new Horse(color);
            case 'K': return new King(color);
            case 'P': return new Pawn(color);
            case 'Q': return new Queen(color);
            case 'R': return new Rook(color);
        }
        
        return null;
    }
    
    protected boolean hasMoved() { return false; }
    
    protected char getColor() { return color; }
    
    protected void setColor(char color) { this.color = color; }
    
    protected char getSymbol() { return this.symbol; }
    
    protected void setSymbol(char symbol) { this.symbol = symbol; }
    
    protected abstract boolean canMove(int row, int column, int target_row, int target_column);
    
    protected abstract List<int[]> getPath(int row, int column, int target_row, int target_column);
}