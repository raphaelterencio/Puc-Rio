package model;

import static org.junit.Assert.*;

import org.junit.Test;

public class RookTest {

	Rook rook = new Rook('W');
	
	@Test
	public void testRookValidMove() 
	{
		assertTrue(rook.canMove(4, 4, 3, 4));
	}
	
	@Test
	public void testRookInvalidMove()
	{
		assertFalse(rook.canMove(4, 4, 2, 5));
	}

}
