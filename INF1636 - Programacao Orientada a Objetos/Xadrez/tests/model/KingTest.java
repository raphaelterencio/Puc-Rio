package model;

import static org.junit.Assert.*;

import org.junit.Test;

public class KingTest {

	King king = new King('W');
	
	@Test
	public void testKingValidMove() 
	{
		assertTrue(king.canMove(4, 4, 5, 5));
	}
	
	@Test
	public void testKingInvalidMove()
	{
		assertFalse(king.canMove(4, 4, 2, 5));
	}

}
