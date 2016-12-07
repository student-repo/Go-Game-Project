package gogame.game.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import gogame.game.engine.BoardFieldOwnership;
import gogame.game.engine.GameBoard;

import java.awt.*;

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

	@Test
	public void testAllFieldsEmpty() {
		assertEquals(19*19, testGameBoard.getBoardFields().size());
		for (int i = 1; i<=19;i++) {
			for (int j = 1; j <= 19; j++) {
				assertTrue(testGameBoard.isEmpty(new Point(i,j)));
			}
		}
	}

	@Test
	public void testCapturingOneStone() {
		assertTrue(testGameBoard.placeStone(new Point(9, 9) , bl));
		assertFalse(testGameBoard.isEmpty(new Point(9, 9)));
		testGameBoard.placeStone(new Point(9, 11), bl);
		assertFalse(testGameBoard.isEmpty(new Point(9, 11)));
		testGameBoard.placeStone(new Point(8, 10), bl);
		assertFalse(testGameBoard.isEmpty(new Point(8, 10)));
		testGameBoard.placeStone(new Point(9, 10), wh);
		assertFalse(testGameBoard.isEmpty(new Point(9, 10)));
		assertTrue(testGameBoard.isEmpty(new Point(10, 10)));
		testGameBoard.placeStone(new Point(10, 10), bl);
		assertFalse(testGameBoard.isEmpty(new Point(10, 10)));

	}

	@Test
	public void testCapturingSmallGroup() {
		assertTrue(testGameBoard.placeStone(new Point(2, 4), bl));

		assertTrue(testGameBoard.placeStone(new Point(3, 3), bl));

		assertTrue(testGameBoard.placeStone(new Point(4, 2), bl));

		assertTrue(testGameBoard.placeStone(new Point(3, 4), wh));

		assertTrue(testGameBoard.placeStone(new Point(4, 4), wh));

		assertTrue(testGameBoard.placeStone(new Point(4, 3), wh));

		assertTrue(testGameBoard.placeStone(new Point(5, 3), wh));

		assertTrue(testGameBoard.placeStone(new Point(5, 2), wh));

		assertTrue(testGameBoard.placeStone(new Point(3, 5), bl));

		assertTrue(testGameBoard.placeStone(new Point(4, 5), bl));

		assertTrue(testGameBoard.placeStone(new Point(5, 5), bl));

		assertTrue(testGameBoard.placeStone(new Point(5, 4), bl));

		assertTrue(testGameBoard.placeStone(new Point(6, 3), bl));

		assertTrue(testGameBoard.placeStone(new Point(6, 2), bl));

		assertTrue(testGameBoard.placeStone(new Point(5, 1), bl));

		assertTrue(testGameBoard.isEmpty(new Point(3, 4)));
		assertTrue(testGameBoard.isEmpty(new Point(4, 3)));
		assertTrue(testGameBoard.isEmpty(new Point(4, 4)));
		assertTrue(testGameBoard.isEmpty(new Point(5, 3)));
		assertTrue(testGameBoard.isEmpty(new Point(5, 2)));
	}



}
