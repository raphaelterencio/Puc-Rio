package model;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	BishopTest.class, 
	BoardTest.class, 
	HorseTest.class, 
	KingTest.class, 
	PawnTest.class, 
	QueenTest.class,
	RookTest.class
})

public class AllTests {

}