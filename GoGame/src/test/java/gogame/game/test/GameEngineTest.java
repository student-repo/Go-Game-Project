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
		testEngine.passTurn();
		//assertNotEquals(prevPlayer, testEngine.getCurrentPlayer());
	}
	
	@Test
	public void testGoodMove() throws IncorrectMoveException{
		testEngine.makeMove(1, 1);
		//assertNotEquals(BoardFieldOwnership.FREE, testEngine.getFieldOwnership(1,1));
	}
	
	@Test (expected = IncorrectMoveException.class)
	public void testSmallGroupSuicideMove() throws IncorrectMoveException {
		testEngine.makeMove(1, 1);
		testEngine.passTurn();
		testEngine.makeMove(1, 3);
		testEngine.passTurn();
		testEngine.makeMove(2, 0);
		testEngine.passTurn();
		testEngine.makeMove(2, 2);
		testEngine.makeMove(2, 1);
	}
	
	@Test (expected = IncorrectMoveException.class)
	public void testNotEmptyFieldMove() throws IncorrectMoveException {
		testEngine.makeMove(1, 1);
		testEngine.makeMove(1, 1);
	}

}
