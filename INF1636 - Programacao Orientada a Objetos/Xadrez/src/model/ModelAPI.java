package model;

import java.util.ArrayList;
import java.util.List;

import controller.Event;

import view.ViewAPI;

public class ModelAPI
{
	static Board board;
	
	private static List<Observer> observers = new ArrayList<>();
	
	private ModelAPI() { }
	
	public static void newGame() { board = new Board(); }
	
	// Observer
	
    public static void addObserver(Observer obs) { observers.add(obs); }

    public static void removeObserver(Observer obs) { observers.remove(obs); }

    private static void notifyObservers(Event event) 
    {
        for (Observer obs : observers) 
        {
            obs.update(event);
        }
    }
    
    public static void registerObserver() { addObserver( ViewAPI.getInstance() ); } 
	
	// Métodos get()
	
	public static char getPieceColor(int row, int column) { return board.getPieceColor(row, column); }
	
	public static char getPieceSymbol(int row, int column) { return board.getPieceSymbol(row, column); }
		
	public static char setGameState(String game_state) { return board.setGameState(game_state); }
	
	public static String getGameState() { return board.getGameState(); }
	
	// Peças
		
	public static boolean isThereAPiece(int row, int column) { return board.isThereAPiece(row, column); }
	
	public static boolean movePiece(int row, int column, int target_row, int target_column)
	{ 
		boolean flag = board.movePiece(row, column, target_row, target_column); 
		
		if (flag)
			notifyObservers(Event.getEvent("PIECE_MOVEMENT"));
		
		return flag;
	}	
	
	// Movimentação
	
	public static List<int[]> getPossibleMoves(int row, int column) { return board.getPossibleMoves(row, column); }
	
	// Regras
	
	public static boolean isCheck(char color)
	{ 
		boolean flag = board.isCheck(color); 

		if (flag)
			notifyObservers(Event.getEvent("CHECK"));
		
		return flag;
		
	}
	
	public static boolean isCheckMate(char color) 
	{ 
		boolean flag = board.isCheckMate(color); 
		
		if (flag)
			notifyObservers(Event.getEvent("CHECKMATE"));
		
		return flag;
	}
	
	public static boolean isStaleMate(char color) 
	{ 
		boolean flag = board.isStaleMate(color); 
		
		if (flag)
			notifyObservers(Event.getEvent("STALEMATE"));
		
		return flag;
	}
	
	public static boolean checkPawnPromotion(char color) 
	{
		boolean flag = board.checkPawnPromotion(color); 
		
		if (flag)
			notifyObservers(Event.getEvent("PAWN_PROMOTION"));
		
		return flag;
	}	
	
	public static void promotePawn(String piece, int row, int column) 
	{ 
		board.promotePawn(piece, row, column); 
		notifyObservers(Event.getEvent("PAWN_PROMOTED"));
	}
	
	// Movimentos especiais
	
	public static boolean isCastleKing(int row, int column, char color) { return board.isCastleKing(row, column, color); }
	
	public static boolean isCastleRook(int row, int column, char color) { return board.isCastleRook(row, column, color); }
	
	public static boolean canCastle(char color, String type) { return board.canCastle(color, type); }
	
	public static void performCastle(char color, String type) 
	{ 
		board.performCastle(color, type); 
		notifyObservers(Event.getEvent("CASTLE"));
	}
	
}