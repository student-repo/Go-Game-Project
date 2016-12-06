package gogame.game.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import gogame.game.engine.BoardFieldOwnership;
import gogame.game.engine.GameBoard;

public class GameBoardTest {

	private GameBoard testGameBoard;
	private BoardFieldOwnership 
			bl = BoardFieldOwnership.BLACK, 
			wh = BoardFieldOwnership.WHITE, 
			em = BoardFieldOwnership.FREE;
	
	@Before
	public void setUp() throws Exception {
		this.testGameBoard = new GameBoard();
	}

	@After
	public void tearDown() throws Exception {
	}
//
//	@Test
//	public void testAllFieldsEmpty() {
//		assertEquals(19*19, testGameBoard.getBoardSize());
//		for (int i = 1; i<=19;i++) {
//			for (int j = 1; j <= 19; j++) {
//				assertTrue(testGameBoard.isEmpty(i, j));
//			}
//		}
//	}
//
//	@Test
//	public void testCapturingOneStone() {
//		assertTrue(testGameBoard.placeStone(9, 9, bl));
//		assertFalse(testGameBoard.isEmpty(9, 9));
//		testGameBoard.placeStone(9, 11, bl);
//		assertFalse(testGameBoard.isEmpty(9, 11));
//		testGameBoard.placeStone(8, 10, bl);
//		assertFalse(testGameBoard.isEmpty(8, 10));
//		testGameBoard.placeStone(9, 10, wh);
//		assertFalse(testGameBoard.isEmpty(9, 10));
//		assertTrue(testGameBoard.isEmpty(10, 10));
//		testGameBoard.placeStone(10, 10, bl);
//		assertFalse(testGameBoard.isEmpty(10, 10));
//
//	}
//
//	@Test
//	public void testCapturingSmallGroup() {
//		assertTrue(testGameBoard.placeStone(2, 4, bl));
//
//		assertTrue(testGameBoard.placeStone(3, 3, bl));
//
//		assertTrue(testGameBoard.placeStone(4, 2, bl));
//
//		assertTrue(testGameBoard.placeStone(3, 4, wh));
//
//		assertTrue(testGameBoard.placeStone(4, 4, wh));
//
//		assertTrue(testGameBoard.placeStone(4, 3, wh));
//
//		assertTrue(testGameBoard.placeStone(5, 3, wh));
//
//		assertTrue(testGameBoard.placeStone(5, 2, wh));
//
//		assertTrue(testGameBoard.placeStone(3, 5, bl));
//
//		assertTrue(testGameBoard.placeStone(4, 5, bl));
//
//		assertTrue(testGameBoard.placeStone(5, 5, bl));
//
//		assertTrue(testGameBoard.placeStone(5, 4, bl));
//
//		assertTrue(testGameBoard.placeStone(6, 3, bl));
//
//		assertTrue(testGameBoard.placeStone(6, 2, bl));
//
//		assertTrue(testGameBoard.placeStone(5, 1, bl));
//
//		assertTrue(testGameBoard.isEmpty(3, 4));
//		assertTrue(testGameBoard.isEmpty(4, 3));
//		assertTrue(testGameBoard.isEmpty(4, 4));
//		assertTrue(testGameBoard.isEmpty(5, 3));
//		assertTrue(testGameBoard.isEmpty(5, 2));
//	}

}
