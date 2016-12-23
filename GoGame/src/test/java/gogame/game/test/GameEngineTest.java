package gogame.game.test;

import static org.junit.Assert.*;

import org.junit.*;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

import gogame.game.engine.*;
import gogame.game.exceptions.IncorrectMoveException;

public class GameEngineTest {
	
	@Mock private Player blackPlayer, whitePlayer;
	private GameEngine testEngine;
	


	@Before
	public void setUp() throws Exception {
		//Preparing black player interface to mock
		blackPlayer = Mockito.mock(Player.class);
		when(blackPlayer.getColor()).thenReturn(BoardFieldOwnership.BLACK);
		//Now with white player
		whitePlayer = Mockito.mock(Player.class);
		when(whitePlayer.getColor()).thenReturn(BoardFieldOwnership.WHITE);
		testEngine = new GameEngine(whitePlayer, blackPlayer);
	}

	@After
	public void tearDown() throws Exception {
		testEngine = null;
	}
	
	@Test
	public void correctColors() {
		
		assertEquals(BoardFieldOwnership.BLACK, blackPlayer.getColor());
		assertEquals(BoardFieldOwnership.WHITE, whitePlayer.getColor());
	}

	@Test
	public void testPassTurn() {
		
		
		assertEquals(BoardFieldOwnership.BLACK, testEngine.getCurrentPlayer());
		assertTrue(testEngine.passTurn(blackPlayer));
		assertEquals(BoardFieldOwnership.WHITE, testEngine.getCurrentPlayer());
	}
	
	
	@Test
	public void testTurnCounter() {
		assertEquals(0, testEngine.getTurnCounter());
		try {
			testEngine.makeMove(5, 5, blackPlayer);
			testEngine.makeMove(7, 7, whitePlayer);
			testEngine.makeMove(5, 6, blackPlayer);
		}
		catch (IncorrectMoveException e) {
			fail("Should not catch an exception here");
		}
		assertEquals(3,  testEngine.getTurnCounter());
	}
	
	@Test
	public void testIncorrectMove() {
		try {
			testEngine.makeMove(5, 5, blackPlayer);
			testEngine.makeMove(5, 5, whitePlayer);
			fail();
		}
		catch (IncorrectMoveException e) {
			
		}
	}
	
	@Test
	public void testWrongPlayerMakesMove() {
		try {
			testEngine.makeMove(5, 5, blackPlayer);
			testEngine.makeMove(5, 7, blackPlayer);
			fail();
		}
		catch (IncorrectMoveException e) {
			
		}
	}
	
	@Test
	public void enteringNegotiationPhase() {
		testEngine.passTurn(blackPlayer);
		testEngine.passTurn(whitePlayer);
		assertEquals(GameEngineStatus.NEGOTIATION, testEngine.getStatus());
	}
	
	@Test
	public void testConcedeGame() {
		testEngine.concedeGame(blackPlayer);
		assertTrue(testEngine.getBlackScore() < testEngine.getWhiteScore());
		assertEquals(GameEngineStatus.FINISHED, testEngine.getStatus());
	}
	

}
