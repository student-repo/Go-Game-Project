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

	@Test
	public void testCapturingTwoGroups() {

		assertTrue(testGameBoard.placeStone(new Point(1, 3), wh));

		assertTrue(testGameBoard.placeStone(new Point(2, 2), bl));

		assertTrue(testGameBoard.placeStone(new Point(2, 1), wh));

		assertTrue(testGameBoard.placeStone(new Point(2, 3), bl));

		assertTrue(testGameBoard.placeStone(new Point(5, 4), wh));

		assertTrue(testGameBoard.placeStone(new Point(4, 4), bl));

		assertTrue(testGameBoard.placeStone(new Point(4, 3), wh));

		assertTrue(testGameBoard.placeStone(new Point(3, 4), bl));

		assertTrue(testGameBoard.placeStone(new Point(3, 3), wh));

		assertTrue(testGameBoard.placeStone(new Point(4, 5), wh));

		assertTrue(testGameBoard.placeStone(new Point(3, 5), wh));

		assertTrue(testGameBoard.placeStone(new Point(3, 2), wh));

		assertTrue(testGameBoard.placeStone(new Point(1, 2), wh));

		assertTrue(testGameBoard.placeStone(new Point(2, 4), wh));

		assertTrue(testGameBoard.isEmpty(new Point(2, 2)));
		assertTrue(testGameBoard.isEmpty(new Point(2, 3)));
		assertTrue(testGameBoard.isEmpty(new Point(3, 4)));
		assertTrue(testGameBoard.isEmpty(new Point(4, 4)));

	}

	@Test
	public void testCapturingThreeGroups() {
		assertTrue(testGameBoard.placeStone(new Point(1, 2), wh));

		assertTrue(testGameBoard.placeStone(new Point(5, 3), bl));

		assertTrue(testGameBoard.placeStone(new Point(6, 3), wh));

		assertTrue(testGameBoard.placeStone(new Point(3, 4), bl));

		assertTrue(testGameBoard.placeStone(new Point(3, 5), wh));

		assertTrue(testGameBoard.placeStone(new Point(2, 2), bl));

		assertTrue(testGameBoard.placeStone(new Point(2, 3), wh));

		assertTrue(testGameBoard.placeStone(new Point(3, 2), bl));

		assertTrue(testGameBoard.placeStone(new Point(3, 1), wh));

		assertTrue(testGameBoard.placeStone(new Point(4, 3), bl));

		assertTrue(testGameBoard.placeStone(new Point(2, 1), wh));

		assertTrue(testGameBoard.placeStone(new Point(5, 2), wh));

		assertTrue(testGameBoard.placeStone(new Point(4, 4), wh));

		assertTrue(testGameBoard.placeStone(new Point(2, 4), wh));

		assertTrue(testGameBoard.placeStone(new Point(5, 4), wh));

		assertTrue(testGameBoard.placeStone(new Point(4, 2), wh));

		assertTrue(testGameBoard.placeStone(new Point(3, 3), wh));

		assertTrue(testGameBoard.isEmpty(new Point(2, 2)));
		assertTrue(testGameBoard.isEmpty(new Point(3, 2)));
		assertTrue(testGameBoard.isEmpty(new Point(3, 4)));
		assertTrue(testGameBoard.isEmpty(new Point(4, 3)));
		assertTrue(testGameBoard.isEmpty(new Point(5, 3)));
	}

	@Test
	public void testCapturingFourGroups() {
		assertTrue(testGameBoard.placeStone(new Point(1, 5), bl));

		assertTrue(testGameBoard.placeStone(new Point(4, 5), wh));

		assertTrue(testGameBoard.placeStone(new Point(3, 4), wh));

		assertTrue(testGameBoard.placeStone(new Point(5, 5), bl));

		assertTrue(testGameBoard.placeStone(new Point(3, 6), wh));

		assertTrue(testGameBoard.placeStone(new Point(2, 6), bl));

		assertTrue(testGameBoard.placeStone(new Point(2, 5), wh));

		assertTrue(testGameBoard.placeStone(new Point(4, 4), bl));

		assertTrue(testGameBoard.placeStone(new Point(3, 3), bl));

		assertTrue(testGameBoard.placeStone(new Point(3, 7), bl));

		assertTrue(testGameBoard.placeStone(new Point(4, 6), bl));

		assertTrue(testGameBoard.placeStone(new Point(2, 4), bl));

		assertTrue(testGameBoard.placeStone(new Point(3, 5), bl));


		assertTrue(testGameBoard.isEmpty(new Point(2, 5)));
		assertTrue(testGameBoard.isEmpty(new Point(4, 5)));
		assertTrue(testGameBoard.isEmpty(new Point(3, 6)));
		assertTrue(testGameBoard.isEmpty(new Point(2, 5)));
	}

	@Test
	public void testSuicideMove() {
		assertTrue(testGameBoard.placeStone(new Point(1, 5), bl));

		assertTrue(testGameBoard.placeStone(new Point(4, 5), wh));

		assertTrue(testGameBoard.placeStone(new Point(3, 4), wh));

		assertTrue(testGameBoard.placeStone(new Point(5, 5), bl));

		assertTrue(testGameBoard.placeStone(new Point(3, 6), wh));

		assertTrue(testGameBoard.placeStone(new Point(2, 6), bl));

		assertTrue(testGameBoard.placeStone(new Point(2, 5), wh));

		assertTrue(testGameBoard.placeStone(new Point(4, 4), bl));

		assertTrue(testGameBoard.placeStone(new Point(3, 3), bl));

		assertTrue(testGameBoard.placeStone(new Point(3, 7), bl));

		assertTrue(testGameBoard.placeStone(new Point(4, 6), bl));

		assertTrue(testGameBoard.placeStone(new Point(2, 4), bl));

		assertFalse(testGameBoard.placeStone(new Point(3, 5), wh));
	}


	@Test
	public void testEternityKo() {
		assertTrue(testGameBoard.placeStone(new Point(2, 4), wh));

		assertTrue(testGameBoard.placeStone(new Point(2, 3), bl));

		assertTrue(testGameBoard.placeStone(new Point(3, 3), wh));

		assertTrue(testGameBoard.placeStone(new Point(3, 2), bl));

		assertTrue(testGameBoard.placeStone(new Point(3, 5), wh));

		assertTrue(testGameBoard.placeStone(new Point(4, 3), bl));

		assertTrue(testGameBoard.placeStone(new Point(4, 4), wh));

		assertTrue(testGameBoard.placeStone(new Point(3, 4), bl));

		assertFalse(testGameBoard.placeStone(new Point(3, 3), wh));
	}
	
	@Test
	public void testKillGroupWithPossibleSuicide() {
		//Setting up
		assertTrue(testGameBoard.placeStone(new Point(9, 9), wh));
		assertTrue(testGameBoard.placeStone(new Point(9, 10), wh));
		assertTrue(testGameBoard.placeStone(new Point(10, 11), wh));
		assertTrue(testGameBoard.placeStone(new Point(10, 8), wh));
		assertTrue(testGameBoard.placeStone(new Point(11, 10), wh));
		assertTrue(testGameBoard.placeStone(new Point(10, 9), bl));
		assertTrue(testGameBoard.placeStone(new Point(10, 10), bl));
		assertTrue(testGameBoard.placeStone(new Point(11, 8), bl));
		assertTrue(testGameBoard.placeStone(new Point(11, 11), bl));
		assertTrue(testGameBoard.placeStone(new Point(12, 9), bl));
		assertTrue(testGameBoard.placeStone(new Point(12, 10), bl));
		//Move tested
		assertTrue(testGameBoard.placeStone(new Point(11, 9), wh));
		//Group was deleted
		assertTrue(testGameBoard.isEmpty(new Point(10, 9)));
		assertTrue(testGameBoard.isEmpty(new Point(10, 10)));
		
		
	}
	
	@Test
	public void testEyeFormation() {
		//Setting up test
		assertTrue(testGameBoard.placeStone(new Point(9, 9), wh));
		assertTrue(testGameBoard.placeStone(new Point(9, 10), wh));
		assertTrue(testGameBoard.placeStone(new Point(9, 11), wh));
		assertTrue(testGameBoard.placeStone(new Point(10, 11), wh));
		assertTrue(testGameBoard.placeStone(new Point(10, 8), wh));
		assertTrue(testGameBoard.placeStone(new Point(11, 8), wh));
		assertTrue(testGameBoard.placeStone(new Point(11, 12), wh));
		assertTrue(testGameBoard.placeStone(new Point(12, 8), wh));
		assertTrue(testGameBoard.placeStone(new Point(12, 12), wh));
		assertTrue(testGameBoard.placeStone(new Point(13, 8), wh));
		assertTrue(testGameBoard.placeStone(new Point(13, 12), wh));
		assertTrue(testGameBoard.placeStone(new Point(14, 9), wh));
		assertTrue(testGameBoard.placeStone(new Point(14, 12), wh));
		assertTrue(testGameBoard.placeStone(new Point(15, 10), wh));
		assertTrue(testGameBoard.placeStone(new Point(15, 11), wh));
		assertTrue(testGameBoard.placeStone(new Point(10, 9), bl));
		assertTrue(testGameBoard.placeStone(new Point(10, 10), bl));
		assertTrue(testGameBoard.placeStone(new Point(11, 9), bl));
		assertTrue(testGameBoard.placeStone(new Point(11, 11), bl));
		assertTrue(testGameBoard.placeStone(new Point(12, 9), bl));
		assertTrue(testGameBoard.placeStone(new Point(12, 10), bl));
		assertTrue(testGameBoard.placeStone(new Point(12, 11), bl));
		assertTrue(testGameBoard.placeStone(new Point(13, 9), bl));
		assertTrue(testGameBoard.placeStone(new Point(13, 11), bl));
		assertTrue(testGameBoard.placeStone(new Point(14, 10), bl));
		assertTrue(testGameBoard.placeStone(new Point(14, 11), bl));
		//Now white can't place stone inside black group and black cannot be removed
		assertFalse(testGameBoard.placeStone(new Point(11, 10), wh));
		assertFalse(testGameBoard.placeStone(new Point(13, 10), wh));
		
		
	}

	@Test
	public void testMoveToBusyField() {
		assertTrue(testGameBoard.placeStone(new Point(4, 3), bl));

		assertTrue(testGameBoard.placeStone(new Point(4, 4), wh));

		assertFalse(testGameBoard.placeStone(new Point(4, 3), bl));

		assertFalse(testGameBoard.placeStone(new Point(4, 3), wh));

		assertFalse(testGameBoard.placeStone(new Point(4, 4), wh));

		assertFalse(testGameBoard.placeStone(new Point(4, 4), bl));
	}

}
