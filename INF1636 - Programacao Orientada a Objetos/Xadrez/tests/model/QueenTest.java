package model;

import static org.junit.Assert.*;

import org.junit.Test;

public class QueenTest {

	Queen queen = new Queen('W');
	
	@Test
	public void testQueenValidMove() 
	{
		assertTrue(queen.canMove(4, 4, 3, 4));
	}
	
	@Test
	public void testQueenInvalidMove()
	{
		assertFalse(queen.canMove(4, 4, 2, 5));
	}
}
