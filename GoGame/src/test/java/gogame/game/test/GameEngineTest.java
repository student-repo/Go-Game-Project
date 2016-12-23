package gogame.game.test;

import static org.junit.Assert.*;

import org.junit.*;

import gogame.game.engine.BoardFieldOwnership;
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
	public void testPassTurn() {
		BoardFieldOwnership prevPlayer;
		prevPlayer = testEngine.getCurrentPlayer();
//		testEngine.passTurn();
		assertNotEquals(prevPlayer, testEngine.getCurrentPlayer());
	}
	
	

}
