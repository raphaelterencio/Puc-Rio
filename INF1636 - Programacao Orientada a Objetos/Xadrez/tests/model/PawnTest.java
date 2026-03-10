package model;

import static org.junit.Assert.*;

import org.junit.Test;

public class PawnTest {

	Pawn pawn = new Pawn('W');
	
	@Test
	public void testPawnValidMove() 
	{
		assertTrue(pawn.canMove(4, 4, 3, 4));
	}
	
	@Test
	public void testPawnInvalidMove()
	{
		assertFalse(pawn.canMove(4, 4, 4,5));
	}

}
