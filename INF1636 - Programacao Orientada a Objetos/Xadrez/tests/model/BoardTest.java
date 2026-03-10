package model;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;

public class BoardTest {

	private Board board;
	
	@Before
	public void setUp() { 
		board = new Board(); 
		board.setUp();
	}
	
	@After
	public void tearDown() { board = null; }
	
	@Test
	public void testCheckRegular()
	{
        board.movePiece(1, 4, 3, 4); // Move peão preto para liberar rainha
        board.movePiece(6, 5, 5, 5); // Move peão branco para abrir caminho
        board.movePiece(0, 3, 4, 7); // Move rainha preta
        assertTrue(board.isCheck('W')); // Rei branco em xeque
	}

	@Test
    public void testCheckDiscovered() {
        board.movePiece(1, 4, 3, 4); // Move peão preto para liberar rainha
        board.movePiece(0, 3, 4, 7); // Move rainha preta
        board.movePiece(6, 5, 5, 5); // Move peão branco para abrir caminho
        assertTrue(board.isCheck('W')); // Rei branco em xeque descoberto
    }
	
}
