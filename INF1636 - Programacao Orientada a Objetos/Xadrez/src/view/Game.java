package view;

import java.util.ArrayList;

// JSwing
import javax.swing.JPanel;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

// Java 2d
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Dimension;

// Imagens
import java.util.HashMap;
import java.util.List;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

// ModelAPI
import model.ModelAPI;
import model.Observer;
import controller.Event;

class Game extends JPanel
{
	private HashMap<String, BufferedImage> image_map = new HashMap<>();
	
	private List<int[]> highlighted_path = new ArrayList<>();
	private int[] highlighted_tile;
	
	JMenuItem queen = new JMenuItem("Rainha");
	JMenuItem rook = new JMenuItem("Torre");
	JMenuItem bishop = new JMenuItem("Bispo");
	JMenuItem horse = new JMenuItem("Cavalo");
	
	protected Game() {}
	
	protected JMenuItem getMenuItem(String item)
	{ 
		switch (item)
		{
			case "Queen":
				return queen;
			case "Rook":
				return rook;
			case "Bishop":
				return bishop;
			case "Horse":
				return horse;
		}
		
		return null;
	}
	
	protected void setUp()
	{
		setPreferredSize(new java.awt.Dimension(64 * 8, 64 * 8));
		loadImages();
	}

	// Paint
	
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        
        int x, y;
        // Desenha as casas
        for(int row=0; row<8; row++) 
        {
        	for(int column=0; column<8; column++)
        	{
        		// Pinta as casas
        		
        		if ( (row+column) % 2 == 0 )
        			g2d.setColor(java.awt.Color.WHITE);
        		else
        			g2d.setColor(java.awt.Color.BLACK);
        		
        		x = column * 64;	
        		y = row * 64;
        		
        		g2d.fillRect(x, y, 64, 64);
        		
                // Pinta os possíveis caminhos
        		
                if ( isHighlighted(row,column) )
                {
                    g2d.setColor(new Color(0, 100, 255, 100));
                    g2d.fillRect(x, y, 64, 64);
                }
                
                // Pinta a casa do roque
                if ( highlighted_tile != null && row == highlighted_tile[0] && column == highlighted_tile[1] )
                {
                    g2d.setColor(new Color(255, 100, 0, 100));
                    g2d.fillRect(x, y, 64, 64);
                }
        		
        		// Pinta as imagens
        		
        		char color = ModelAPI.getPieceColor(row, column);
        		char symbol = ModelAPI.getPieceSymbol(row, column);
        		
                if (symbol != '-') 
                {
                	String color_string = colorString(color);
                	String piece_string = pieceString(symbol);
                	
                	String image_name = color_string + piece_string;
                	
                    BufferedImage pieceImage = image_map.get(image_name);
                    g2d.drawImage(pieceImage, x, y, 64, 64, this);
                }
        	}
        }
    }
    
    // Imagens
    
    private void loadImages() 
    {
        String[] colors = {"White", "Black"};
        String[] pieces = {"Bishop", "Horse", "King", "Pawn", "Queen", "Rook"};

        for (String color : colors) 
        {
            for (String piece : pieces) 
            {
                String path = "/images/" + color + piece + ".png";
                try 
                {
                    BufferedImage image = ImageIO.read(getClass().getResourceAsStream(path));
                    image_map.put(color + piece, image);
                } 
                catch (IOException | IllegalArgumentException e)
                {
                    System.out.println("Erro ao carregar imagem: " + path);
                }
            }
        }
    }
    
    // Callbacks
    
    protected void highlightPath(int row, int column)
    {
    	highlighted_path = ModelAPI.getPossibleMoves(row, column);
    	repaint();
    }
    
    protected void highlightTile(int row, int column)
    {
    	highlighted_tile = new int[] { row, column };
    	repaint();
    }
    
    protected void clearHighlightedPath()
    {
    	highlighted_path.clear();
    	repaint();
    }
    
    protected void clearHighlightedTile()
    {
    	highlighted_tile = null;
    	repaint();
    }
    
    protected void checkMateCallback()
    {
    	JOptionPane.showMessageDialog(
    		    this,
    		    "Xeque-mate!",
    		    "Fim de jogo",
    		    JOptionPane.INFORMATION_MESSAGE
    		);
    	
    	ViewAPI.showMenu();
    }
    
    protected void checkCallback()
    {
    	JOptionPane.showMessageDialog(
    		    this,
    		    "Xeque!",
    		    "Jogador em xeque",
    		    JOptionPane.INFORMATION_MESSAGE
    		);
    }
    
    protected void staleMateCallback()
    {
    	JOptionPane.showMessageDialog(
    		    this,
    		    "Congelamento!",
    		    "Fim de jogo",
    		    JOptionPane.INFORMATION_MESSAGE
    		);
    	
    	ViewAPI.showMenu();
    }
    
    protected void pawnPromotionCallback()
    {
    	JPopupMenu pawnPromotionMenu = new JPopupMenu();
    	
        JLabel label = new JLabel("Promoção do peão: escolha uma peça");
        label.setEnabled(false);
        pawnPromotionMenu.add(label);
        pawnPromotionMenu.addSeparator();
    	
    	pawnPromotionMenu.add(queen);
    	pawnPromotionMenu.add(rook);
    	pawnPromotionMenu.add(bishop);
    	pawnPromotionMenu.add(horse);
    	
        // Centro da tela
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = screenSize.width / 2;
        int centerY = screenSize.height / 2;

        // Coordenadas absolutas -> Coordenadas relativas
        Point componentLocationOnScreen = this.getLocationOnScreen();
        int x = centerX - componentLocationOnScreen.x;
        int y = centerY - componentLocationOnScreen.y;
    	
        pawnPromotionMenu.show(this, x, y);
    }
    
    // Auxiliares
    
    private String colorString(char color)
    {
    	switch (color) 
    	{
			case 'W':
				return "White";
			case 'B':
				return "Black";
    	}
		return null;
    }
    
    private String pieceString(char symbol)
    {
    	switch (symbol)
    	{
    		case 'B':
    			return "Bishop";
    		case 'H':
    			return "Horse";
    		case 'K':
    			return "King";
    		case 'P':
    			return "Pawn";
    		case 'Q':
    			return "Queen";
    		case 'R':
    			return "Rook";
    	}
    	return null;
    }

    private boolean isHighlighted(int row, int column)
    {
        for (int[] pos : highlighted_path) {
            if (pos[0] == row && pos[1] == column) {
                return true;
            }
        }
        
        return false;
    }

}