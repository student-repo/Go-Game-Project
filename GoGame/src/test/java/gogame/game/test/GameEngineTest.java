package gogame.game.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import gogame.game.engine.GameEngine;
import gogame.game.exceptions.IncorrectMoveException;

public class GameEngineTest {
	
	private GameEngine testEngine;

	@Before
	public void setUp() throws Exception {
		testEngine = new GameEngine();
	}

	@After
	public void tearDown() throws Exception {
		testEngine = null;
	}

	@Test
	public void testGoodMove() {
		fail("Not yet implemented");
	}
	
	@Test(expected = IncorrectMoveException.class)
	public void testSuicideMove() {
		
	}
	
	@Test(expected = IncorrectMoveException.class)
	public void testNotEmptyMove() {
		
	}

}
