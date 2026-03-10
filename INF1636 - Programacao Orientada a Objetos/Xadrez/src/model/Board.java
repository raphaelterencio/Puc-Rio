package model;

import java.util.ArrayList;
import java.util.List;

import controller.Main;

class Board 
{
	private Piece[][] tiles;
	
	protected Board()
	{
		tiles = new Piece[8][8];
		setUp();
	}
	
	protected void setUp()
	{
		// Bispos
		tiles[0][2] = new Bishop('B');
		tiles[0][5] = new Bishop('B');
		tiles[7][2] = new Bishop('W');
		tiles[7][5] = new Bishop('W');
		
		// Cavalos
		tiles[0][1] = new Horse('B');
		tiles[0][6] = new Horse('B');
		tiles[7][1] = new Horse('W');
		tiles[7][6] = new Horse('W');
		
		// Reis
		tiles[0][4] = new King('B');
		tiles[7][4] = new King('W');
		
		// Peões
		for(int column=0; column<8; column++)
		{
			tiles[1][column] = new Pawn('B');
			tiles[6][column] = new Pawn('W');
		}
		
		// Rainhas
		tiles[0][3] = new Queen('B');
		tiles[7][3] = new Queen('W');
		
		// Torres
		tiles[0][0] = new Rook('B');
		tiles[0][7] = new Rook('B');
		tiles[7][0] = new Rook('W');
		tiles[7][7] = new Rook('W');
	}
	
	// Métodos get()
	
	protected char getPieceColor(int row, int column)
	{ 
		Piece piece = tiles[row][column];
		if (piece == null)
			return '-';
		return piece.getColor(); 
	}
	
	protected char getPieceSymbol(int row, int column) 
	{ 
		Piece piece = tiles[row][column];
		if (piece == null)
			return '-';
		return piece.getSymbol(); 
	}
	
	protected boolean getPieceMovementStatus(int row, int column)
	{
		Piece piece = tiles[row][column];
		
		if (piece instanceof King)
			return ( (King) piece ).hasMoved();
		
		else if (piece instanceof Rook)
			return ( (Rook) piece ).hasMoved();
		
		else
			return false;
	}
	
	protected char setGameState(String game_state)
	{
	    String[] lines = game_state.split("\n");

	    char round_color = lines[0].charAt(0);

	    for (int row = 0; row < 8; row++) 
	    {
	        String[] tokens = lines[row + 1].split(" ");

	        for (int col = 0; col < 8; col++) 
	        {
	            String token = tokens[col];

	            if (token.equals("."))
	            {
	                tiles[row][col] = null;
	            }
	            else if (token.length() == 3)
	            {
	                char color = token.charAt(0);
	                char symbol = token.charAt(1);
	                char movement = token.charAt(2);

	                Piece piece = Piece.createPiece(color, symbol);
	                tiles[row][col] = piece;

	                // Restaurar hasMoved se aplicável
	                if (piece instanceof King && movement == 'T')
	                	((King) piece).pieceMoved();
	                
	                else if (piece instanceof Rook && movement == 'T')
	                    ((Rook) piece).pieceMoved();
	                   
	            }
	        }
	    }

	    return round_color;
	}

	
	protected String getGameState()
	{
	    StringBuilder sb = new StringBuilder();

	    char round_color = Main.getRoundColor();
	    sb.append(round_color);
	    sb.append("\n");
	    
	    for (int row = 0; row < 8; row++) 
	    {
	        for (int col = 0; col < 8; col++) 
	        {
	            Piece piece = tiles[row][col];

	            if (piece == null)
	                sb.append(".");
	            else 
	            {
	                char color = piece.getColor();
	                char symbol = piece.getSymbol();
	            
	                char movement;
	                if (piece instanceof King)
	                {
	                    if ( ((King) piece).hasMoved() )
	                    	movement = 'T';
	                    else 
	                    	movement = 'F';
	                }
	                else if (piece instanceof Rook)
	                {
	                    if ( ((Rook) piece).hasMoved() )
	                    	movement = 'T';
	                    else 
	                    	movement = 'F';
	                }
	                else
	                	movement = '-';
	                
	                sb.append(color).append(symbol).append(movement);
	            }

	            if (col < 7)
	                sb.append(" ");
	        }
	        
	        sb.append("\n");
	    }

	    return sb.toString();
	}

	// Peças
	
	protected boolean isThereAPiece(int row, int column) 
	{ 
		if (tiles[row][column] != null) return true;
		return false;
	}

	protected boolean movePiece(int row, int column, int target_row, int target_column)
	{		
		List<int[]> possibleMoves = getPossibleMoves(row, column);
		if ( !isPossibleMove(target_row, target_column, possibleMoves) )
			return false;
		
		Piece piece = tiles[row][column];
		Piece cmp_piece = tiles[target_row][target_column];
		
		if (piece instanceof King)
			( (King) piece ).pieceMoved();
		
		else if (piece instanceof Rook)
			( (Rook) piece ).pieceMoved();
			
		tiles[target_row][target_column] = tiles[row][column];
		tiles[row][column] = null;			
		
		return true;
	}
	
    private boolean isPossibleMove(int row, int column, List<int[]> possibleMoves)
    {
        for (int[] pos : possibleMoves) {
            if (pos[0] == row && pos[1] == column) {
                return true;
            }
        }
        
        return false;
    }
	
	// Movimentação
	
	private List<int[]> getFixedPath(int row, int column, int target_row, int target_column)
	{
		Piece piece = tiles[row][column]; 
		Piece cmp_piece;
				
		// Obtém o caminho a ser percorrido desconsiderando colisão
		List<int[]> path = piece.getPath(row, column, target_row, target_column);
		List<int[]> fixed_path = new ArrayList<>();
				
		// Percorre o caminho até a posição
		for(int[] coords : path) 
		{	
			cmp_piece = tiles[coords[0]][coords[1]];
			
			// Se não houver uma peça no caminho
			if(cmp_piece == null)
			{
				// Confere se o movimento é um peão tentando andar na diagonal sem comer ninguém
				if( piece instanceof Pawn)
				{
					int row_diff = Math.abs(coords[0] - row);
					int column_diff = Math.abs(coords[1] - column);
					
					if (row_diff == 1 && column_diff == 1)
						continue;
				}
				
				fixed_path.add(new int[] { coords[0], coords[1] });
			}
			
			// Se houver uma peça no caminho
			else 
			{ 
				// Se a casa não for o destino final
				if ( coords[0] != target_row || coords[1] != target_column) 
					break;
				
				// Se a casa for o destino final
				else
				{  
					// Se for uma peça de cor diferente
					if ( piece.getColor() != cmp_piece.getColor() ) 
					{
						// Se não for um peão
						if( !(piece instanceof Pawn) )
							fixed_path.add(new int[] { coords[0], coords[1] });
						
						// Se for um peão, conferir se ele não está tentando comer pra frente
						else
						{
							int row_diff = Math.abs(coords[0] - row);
							int column_diff = Math.abs(coords[1] - column);
							
							if (row_diff == 1 && column_diff == 1) 
								fixed_path.add(new int[] { coords[0], coords[1] });
						}
							
					}
					break;
				} 
			}
		}
		
		return fixed_path;
	}

    protected List<int[]> getPossibleMoves(int row, int column) 
    {
        List<int[]> possibleMoves = new ArrayList<>();
        Piece piece = tiles[row][column];
        boolean alreadyExists;

        for (int target_row = 0; target_row < 8; target_row++) {
            for (int target_column = 0; target_column < 8; target_column++) {
            	
                // Ignora a posição atual da peça
                if (row == target_row && column == target_column) 
                	continue;

                // Se for uma posição possível de se chegar
                if (piece.canMove(row, column, target_row, target_column)) 
                {
                	// Percorre o caminho até a posição
                	for (int[] move : getFixedPath(row, column, target_row, target_column)) 
                	{
                	    alreadyExists = false;
                	    
                	    // Confere se a posição já foi selecionada
                	    for (int[] existing : possibleMoves) 
                	    {
                	        if (move[0] == existing[0] && move[1] == existing[1]) 
                	        { 
                	        	alreadyExists = true; 
                	        	break; 
                	        }
                	    }
                	    
                	    // Se a posição ainda não foi selecionada, adicionar ela à lista
                	    if (!alreadyExists)
                	    	possibleMoves.add(move);
                	}
                    
                }
            }
        }

        return possibleMoves;
    }

	// Regras
	
	protected boolean isCheck(char color)
	{
		// Procura o rei
		int king_row = -1, king_column = -1;
		for(int row=0; row<8; row++)
		{
			for(int column=0; column<8; column++)
			{
				Piece piece = tiles[row][column];
				if(piece instanceof King && piece.getColor() == color)
				{
					king_row = row;
					king_column = column;
					break;
				}
			}
		}
		// Se o rei não for encontrado
		if (king_row == -1) return false;
		
		// Obtém a cor do oponente
		char opponent_color = (color == 'W') ? 'B' : 'W';
	
		// Verifica se o oponente consegue chegar no rei
		for(int row=0; row<8; row++)
		{
			for(int column=0; column<8; column++)
			{
				Piece piece = tiles[row][column];
				if(piece != null && piece.getColor() == opponent_color)
				{
	                List<int[]> moves = getPossibleMoves(row, column);
	                for (int[] move : moves)
	                {
	                    if (move[0] == king_row && move[1] == king_column)
	                        return true; // A peça pode atingir o rei
	                }
				}
					
			}
		}
		
		return false;
	}
	
	protected boolean isCheckMate(char color)
	{
		// Se não estiver em xeque, não pode ser xeque-mate
		if (!isCheck(color)) return false;

		// Procura todas as peças da cor
		for(int row=0; row<8; row++)
		{
			for(int column=0; column<8; column++)
			{
				Piece piece = tiles[row][column];
				if(piece != null && piece.getColor() == color)
				{
					// Tenta todos os movimentos possíveis da peça
					List<int[]> possibleMoves = getPossibleMoves(row, column);
					for(int[] move : possibleMoves)
					{						
						int targetRow = move[0];
						int targetColumn = move[1];
						
						// Salva o estado atual
						Piece originalPiece = tiles[targetRow][targetColumn];
						
						// Faz o movimento
						tiles[targetRow][targetColumn] = piece;
						tiles[row][column] = null;
						
						// Verifica se ainda está em xeque
						boolean stillInCheck = isCheck(color);
						
						// Desfaz o movimento
						tiles[row][column] = piece;
						tiles[targetRow][targetColumn] = originalPiece;
						
						// Se encontrou um movimento que tira do xeque, não é xeque-mate
						if (!stillInCheck) 
							return false;
					}
				}
			}
		}
		
		return true;
	}

	protected boolean isStaleMate(char color)
	{
		// Se estiver em xeque, não pode ser congelamento
		if (isCheck(color)) return false;
		
		// Procura todas as peças da cor
		for(int row=0; row<8; row++)
		{
			for(int column=0; column<8; column++)
			{
				Piece piece = tiles[row][column];
				if(piece != null && piece.getColor() == color)
				{
					// Tenta todos os movimentos possíveis da peça
					List<int[]> possibleMoves = getPossibleMoves(row, column);
					for(int[] move : possibleMoves)
					{
						int targetRow = move[0];
						int targetColumn = move[1];
						
						// Salva o estado atual
						Piece originalPiece = tiles[targetRow][targetColumn];
						
						// Faz o movimento
						tiles[targetRow][targetColumn] = piece;
						tiles[row][column] = null;
						
						// Verifica se o movimento coloca o rei em xeque
						boolean putsInCheck = isCheck(color);
						
						// Desfaz o movimento
						tiles[row][column] = piece;
						tiles[targetRow][targetColumn] = originalPiece;
						
						// Se encontrou um movimento legal, não é congelamento
						if (!putsInCheck) return false;
					}
				}
			}
		}
		
		return true;
	}

	protected boolean checkPawnPromotion(char color)
	{
	    for (int column = 0; column < 8; column++) 
	    {
	        int row = (color == 'W') ? 0 : 7;

	        Piece piece = tiles[row][column];

	        if (piece instanceof Pawn && piece.getColor() == color) 
	        {
	            return true;
	        }
	    }

	    return false;
	}
	
	protected void promotePawn(String piece, int row, int column)
	{		
		char color = tiles[row][column].getColor();
		
		switch (piece)
		{
		case "Queen":
			tiles[row][column] = new Queen(color);
			break;
		case "Rook":
			tiles[row][column] = new Rook(color);
			((Rook) tiles[row][column]).pieceMoved();
			break;
		case "Bishop":
			tiles[row][column] = new Bishop(color);
			break;
		case "Horse":
			tiles[row][column] = new Horse(color);
			break;
		}
	}
	
	// Movimentos especiais
	
	protected boolean isCastleKing(int row, int column, char color)
	{
		Piece piece = tiles[row][column];
		if (!(piece instanceof King)) return false;
		if (piece.getColor() != color) return false;
		if (((King) piece).hasMoved()) return false;

		int expectedRow = (color == 'W') ? 7 : 0;
		return row == expectedRow && column == 4;
	}

	protected boolean isCastleRook(int row, int column, char color)
	{
		Piece piece = tiles[row][column];
		if (!(piece instanceof Rook)) return false;
		if (piece.getColor() != color) return false;
		if (((Rook) piece).hasMoved()) return false;

		int expectedRow = (color == 'W') ? 7 : 0;
		if (row != expectedRow) return false;

		// Apenas as colunas 0 e 7 são consideradas para roque
		if (column == 0)
			return canCastle(color, "Longo");
		else if (column == 7)
			return canCastle(color, "Curto");

		return false;
	}

	
	protected boolean canCastle(char color, String type)
	{
		// Verifica se o rei está em xeque
	    if (isCheck(color)) return false;

	    boolean isShort;
	    if ( type.equals("Curto") )
	        isShort = true;
	    else 
	        isShort = false;

	    int row = (color == 'W') ? 7 : 0;
	    int kingCol = 4;
	    int rookCol = isShort ? 7 : 0;

	    Piece king = tiles[row][kingCol];
	    Piece rook = tiles[row][rookCol];

	    // Verifica se o rei ou a torre estão no lugar que deviam
	    if (!(king instanceof King) || !(rook instanceof Rook)) return false;
	    if (king.getColor() != color || rook.getColor() != color) return false;
	    
	    // Verifica se o rei ou a torre já se moveram
	    if (king.hasMoved() || rook.hasMoved()) return false;

	    // Verifica se há peças entre o rei e a torre
	    int start = Math.min(kingCol, rookCol) + 1;
	    int end = Math.max(kingCol, rookCol);
	    
	    for (int col = start; col < end; col++) 
	    {
	        if (tiles[row][col] != null)
	        	return false;
	    }

	    // Verifica se o rei passa por casas atacadas
	    int step = isShort ? 1 : -1;
	    
	    for (int i = 1; i <= 2; i++) 
	    {
	        int col = kingCol + i * step;
	        
            // Simula o movimento do rei
            Piece originalSource = tiles[row][kingCol];
            Piece originalTarget = tiles[row][col];

            tiles[row][kingCol] = null;
            tiles[row][col] = king;

            boolean inCheck = isCheck(color);

            // Desfaz o movimento
            tiles[row][col] = originalTarget;
            tiles[row][kingCol] = originalSource;

            if (inCheck)
            	return false;
	    }

	    return true;
	}

	
	protected void performCastle(char color, String type) 
	{
	    boolean isShort;
	    if ( type.equals("Curto") )
	        isShort = true;
	    else 
	        isShort = false;
		
		int row = (color == 'W') ? 7 : 0;
		int kingCol = 4;
		int rookCol = isShort ? 7 : 0;
		
		Piece king = tiles[row][kingCol];
		Piece rook = tiles[row][rookCol];
		
		// Move o rei
		int targetKingCol = isShort ? kingCol + 2 : kingCol - 2;
		tiles[row][targetKingCol] = king;
		tiles[row][kingCol] = null;
		
		// Move a torre
		int targetRookCol = isShort ? targetKingCol - 1 : targetKingCol + 1;
		tiles[row][targetRookCol] = rook;
		tiles[row][rookCol] = null;
		
		((King) king).pieceMoved();
		((Rook) rook).pieceMoved();
	}

} 