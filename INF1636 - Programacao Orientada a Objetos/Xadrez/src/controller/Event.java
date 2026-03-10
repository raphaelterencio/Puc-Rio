package controller;

public enum Event 
{
	PIECE_MOVEMENT,
	CHECK,
	CHECKMATE,
	STALEMATE,
	PAWN_PROMOTION,
	PAWN_PROMOTED,
	CASTLE;
	
	public static Event getEvent(String key) { return Event.valueOf(key); }
	
	public static String getEvent(Event event) { return event.name(); }
}