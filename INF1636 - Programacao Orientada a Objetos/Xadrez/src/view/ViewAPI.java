package view;

import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JMenuItem;

import controller.Event;
import model.Observer;

public class ViewAPI implements Observer
{
	private static ViewAPI instance;
	
	static WindowFrame window_frame;
	static Menu menu_panel;
	static Game game_panel;
	
	private ViewAPI() {}
	
	// Observer

	public static ViewAPI getInstance()
	{
		if (instance == null)
			instance = new ViewAPI();
		
		return instance;
	}
	
	@Override
	public void update(Event event)
	{		
		game_panel.repaint();
				
		String event_name = Event.getEvent(event);
		
		switch (event_name)
		{
		case "PIECE_MOVEMENT":
			break;
		case "CHECK":
			game_panel.checkCallback();
			break;
		case "CHECKMATE":
			game_panel.checkMateCallback();
			break;
		case "STALEMATE":
			game_panel.staleMateCallback();
			break;
		case "PAWN_PROMOTION":
			game_panel.pawnPromotionCallback();
			break;	
		case "PAWN_PROMOTED":
			break;
		case "CASTLE":
			break;
		}
		
	}

	public static void openWindow()
	{ 
		window_frame = new WindowFrame();
		menu_panel = window_frame.getMenuPanel();
		game_panel = window_frame.getGamePanel();
	}
	
	public static Game getObserver() { return game_panel; }
	
    public static void addMouseListener(MouseListener listener) { game_panel.addMouseListener(listener); }
    
    public static JMenuItem getMenuItem(String item) { return game_panel.getMenuItem(item); }
    
    public static JButton getButton(String button) { return menu_panel.getButton(button); }
    
    // Callbacks
    
    public static void highlightPath(int row, int column) { game_panel.highlightPath(row, column); }
    
    public static void clearHighlightedPath() { game_panel.clearHighlightedPath(); }
    
    public static void highlightTile(int row, int column) { game_panel.highlightTile(row, column); }
    
    public static void clearHighlightedTile() { game_panel.clearHighlightedTile(); } 
    
    // Telas
    
    public static void showMenu() { window_frame.showPanel(menu_panel); }
    
    public static void showBoard() { window_frame.showPanel(game_panel); }
    
}
