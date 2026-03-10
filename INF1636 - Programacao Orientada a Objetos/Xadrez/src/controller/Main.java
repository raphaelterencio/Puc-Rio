package controller;

import model.ModelAPI;
import view.ViewAPI;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Main
{	
	static char round_color = 'W';
			
	static boolean isPieceSelected = false;
	static int selected_row = -1;
	static int selected_column = -1;
	
	static int origin_row = -1;
	static int origin_column = -1;
	
	static String castle_type = null;
	static int castle_row = -1;
	static int castle_column = -1;
	
	static int backup_row = -1;
	static int backup_column = -1;
	
	static List<int[]> highlighted_path = new ArrayList<>();
		
	public static void main(String[] args)
	{			
		ViewAPI.openWindow();
		ModelAPI.registerObserver();
	
		userLeftClickHandler();
		userRightClickHandler();
		userMiddleClickHandler();
		
		pawnPromotionHandler();
		menuHandler();
	}
	
	// Métodos get()
	
	public static char getRoundColor() { return round_color; }
	
	// Callbacks
	
	private static void userLeftClickHandler()
	{
        ViewAPI.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
            	if (e.getButton() == MouseEvent.BUTTON1) 
            	{
	                int x = e.getX();
	                int y = e.getY();
	                
	                backup_row = selected_row = y / 64;
	                backup_column = selected_column = x / 64;
	                	           
	                // Selecionando peça a ser movida
	                if ( !isPieceSelected )
	                {
	                	// Restringe à casas onde existem peças
	                	if (ModelAPI.isThereAPiece(selected_row, selected_column))
	                	{
	                		// Restringe à casas com peças da cor do round atual
	                		if ( ModelAPI.getPieceColor(selected_row, selected_column) == round_color )
	                		{
	                			// Guarda posição de origem da peça
		                		origin_row = selected_row;
		                		origin_column = selected_column;
		                		
		                		// Movimentos possíveis
		                		ViewAPI.highlightPath(selected_row, selected_column);
		                		highlighted_path = ModelAPI.getPossibleMoves(selected_row, selected_column);
		                		
		                		// Roque (Rei)
		                		if(ModelAPI.isCastleKing(selected_row, selected_column, round_color))
		                		{
			                		if (ModelAPI.canCastle(round_color, "Curto"))
			                		{
			                			castle_row = origin_row;
			                			castle_column = origin_column + 2;
			                			castle_type = "Curto";
			                			ViewAPI.highlightTile(castle_row, castle_column);
			                		}
			                		
			                		if (ModelAPI.canCastle(round_color, "Longo"))
			                		{
			                			castle_row = origin_row;
			                			castle_column = origin_column - 2;
			                			castle_type = "Longo";
			                			ViewAPI.highlightTile(castle_row, castle_column);
			                		}
		                		}
		                		
		                		// Roque (Torre)
		                		else if (ModelAPI.isCastleRook(selected_row, selected_column, round_color))
		                		{
		                			if (ModelAPI.canCastle(round_color, "Curto"))
		                			{
		                				castle_row = origin_row;
		                				castle_column = selected_column - 2;
		                				castle_type = "Curto";
		                				ViewAPI.highlightTile(castle_row, castle_column);
		                			}
		                			else if (ModelAPI.canCastle(round_color, "Longo"))
		                			{
		                				castle_row = origin_row;
		                				castle_column = selected_column + 3;
		                				castle_type = "Longo";
		                				ViewAPI.highlightTile(castle_row, castle_column);
		                			}
		                		}

		                		// Peça selecionada
		                		isPieceSelected = !isPieceSelected;
	                		}
	                	}
	                }
	                // Selecionando posição para mover a peça
	                else 
	                {
	                	// Restringe à casas onde é possível mover a peça por movimento normal
	                	if ( isHighlighted(selected_row, selected_column) || (selected_row == castle_row && selected_column == castle_column) )
	                	{
	                		// Roque
	                		if (selected_row == castle_row && selected_column == castle_column)
	                			ModelAPI.performCastle(round_color, castle_type);
	                		
	                		// Movimento padrão da peça
	                		else
	                			ModelAPI.movePiece(origin_row, origin_column, selected_row, selected_column);
	            			
	                		// Promoção de peão
	            	    	ModelAPI.checkPawnPromotion(round_color);
	            	    	
	            	    	afterMoveProcedures();
	                		round_color = (round_color == 'W') ? 'B' : 'W';
	                		afterMoveProcedures();
	                		
	                		selected_row = -1; selected_column = -1;
	                		castle_row = -1; castle_column = -1;
	                		origin_row = -1; origin_column = -1;
	                		castle_type = null;
	                		
	                		ViewAPI.clearHighlightedPath();
	                		ViewAPI.clearHighlightedTile();
	                	}
	                	
	                	// Cancela o movimento caso o usuário clique em uma casa vazia ou de sua cor
	                	else
	                	{
	                		selected_row = -1; selected_column = -1;
	                		castle_row = -1; castle_column = -1;
	                		origin_row = -1; origin_column = -1;
	                		castle_type = null;
	                		
	                		ViewAPI.clearHighlightedPath();
	                		ViewAPI.clearHighlightedTile();
	                	}
	                	
	                	// Peça não selecionada
	                	isPieceSelected = !isPieceSelected;
	                }
            	}
            }
        });
    }
	
	private static void userRightClickHandler()
	{
	    ViewAPI.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mousePressed(MouseEvent e) 
	        {
	            if (e.getButton() == MouseEvent.BUTTON3) 
	            	saveGame();
	        }
	    });
	}
	
	private static void userMiddleClickHandler()
	{
	    ViewAPI.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mousePressed(MouseEvent e) 
	        {
	            if (e.getButton() == MouseEvent.BUTTON2) 
	            	exitGame();
	        }
	    });
	}
	
	private static void pawnPromotionHandler()
	{
		ViewAPI.getMenuItem("Queen").addActionListener(e -> formalizePawnPromotion("Queen"));
		ViewAPI.getMenuItem("Rook").addActionListener(e -> formalizePawnPromotion("Rook"));
		ViewAPI.getMenuItem("Bishop").addActionListener(e -> formalizePawnPromotion("Bishop"));
		ViewAPI.getMenuItem("Horse").addActionListener(e -> formalizePawnPromotion("Horse"));
	}
	
	private static void menuHandler()
	{
		ViewAPI.getButton("NewGame").addActionListener(e -> newGame());
		ViewAPI.getButton("LoadGame").addActionListener(e -> loadGame());
	}
	
	// Auxiliares
	
	// Confere se uma casa está destacada
    private static boolean isHighlighted(int row, int column)
    {
        for (int[] pos : highlighted_path) {
            if (pos[0] == row && pos[1] == column) {
                return true;
            }
        }
        
        return false;
    }
    
    private static void afterMoveProcedures() 
    {	
    	if (ModelAPI.isCheckMate(round_color))
    		return; // Acabou o jogo
    	
    	if (ModelAPI.isStaleMate(round_color))
    		return; // Acabou o jogo
    	
    	ModelAPI.isCheck(round_color);
    } 
    
    private static void formalizePawnPromotion(String piece){ ModelAPI.promotePawn(piece, backup_row, backup_column); }
    
    // Estado do jogo
    
    private static void newGame()
    {
		ModelAPI.newGame();
		ViewAPI.showBoard();
    }
    
    private static void loadGame()
    {
    	String game_state = getGameState();
    	
    	if (game_state != null)
    	{
    		ModelAPI.newGame();
    		round_color = ModelAPI.setGameState(game_state);
    		ViewAPI.showBoard();
    	}
    }
    
    private static void saveGame()
    {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar partida");
        
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Arquivo de texto (*.txt)", "txt");
        fileChooser.setFileFilter(filter);

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) 
        {
            File fileToSave = fileChooser.getSelectedFile();
            
            // Garante que o arquivo tenha a extensão .txt
            if (!fileToSave.getName().toLowerCase().endsWith(".txt"))
                fileToSave = new File(fileToSave.getParentFile(), fileToSave.getName() + ".txt");
            
            try (FileWriter writer = new FileWriter(fileToSave)) 
            {
                String gameState = ModelAPI.getGameState();
                writer.write(gameState);
            } 
            catch (IOException ex) {}
        }
    }
    
    private static String getGameState() 
    {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Carregar partida");

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Arquivo de texto (*.txt)", "txt");
        fileChooser.setFileFilter(filter);

        int userSelection = fileChooser.showOpenDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION)
        {
            File selectedFile = fileChooser.getSelectedFile();

            try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) 
            {
                StringBuilder content = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) 
                {
                    content.append(line).append("\n");
                }

                return content.toString();
            } 
            catch (IOException ex) {}
        }
        return null;
    }
    
    private static void exitGame()
    {    	
    	round_color = 'W';
		
    	isPieceSelected = false;
    	selected_row = -1;
    	selected_column = -1;
    	
    	origin_row = -1;
    	origin_column = -1;
    	
    	castle_type = null;
    	castle_row = -1;
    	castle_column = -1;
    	
    	backup_row = -1;
    	backup_column = -1;
    	
    	highlighted_path = new ArrayList<>();
    	
    	ViewAPI.showMenu();
    }
}