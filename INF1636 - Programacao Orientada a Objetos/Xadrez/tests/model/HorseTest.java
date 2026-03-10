package model;

import static org.junit.Assert.*;

import org.junit.Test;

public class HorseTest {

	Horse horse = new Horse('W');
	
	@Test
	public void testHorseValidMove() 
	{
		assertTrue(horse.canMove(4, 4, 2, 5));
	}
	
	@Test
	public void testHorseInvalidMove()
	{
		assertFalse(horse.canMove(4, 4, 4, 5));
	}
}
