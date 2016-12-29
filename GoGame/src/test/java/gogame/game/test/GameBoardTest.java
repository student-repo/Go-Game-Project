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
		for (int i = 0; i< 19;i++) {
			for (int j = 0; j < 19; j++) {
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
		//Field should be empty
		assertTrue(testGameBoard.isEmpty(new Point(9, 10)));
		assertEquals(1, testGameBoard.getCapturedWhiteStones());
		assertEquals(0, testGameBoard.getCapturedBlackStones());
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
		assertEquals(5, testGameBoard.getCapturedWhiteStones());
		assertEquals(0, testGameBoard.getCapturedBlackStones());
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
		assertEquals(4, testGameBoard.getCapturedBlackStones());
		assertEquals(0, testGameBoard.getCapturedWhiteStones());

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
		assertEquals(5, testGameBoard.getCapturedBlackStones());
		assertEquals(0, testGameBoard.getCapturedWhiteStones());
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
		assertEquals(4, testGameBoard.getCapturedWhiteStones());
		assertEquals(0, testGameBoard.getCapturedBlackStones());
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
	public void testRemovingGroupAndUpdatingAllOtherGroups() {
		assertTrue(testGameBoard.placeStone(new Point(10, 9), bl));
		assertTrue(testGameBoard.placeStone(new Point(9, 9), wh));
		assertTrue(testGameBoard.placeStone(new Point(10,10), wh));
		assertTrue(testGameBoard.placeStone(new Point(10, 8), wh));
		assertTrue(testGameBoard.placeStone(new Point(11, 9), wh));
		assertTrue(testGameBoard.isEmpty(new Point(10, 9)));
		//removing one black stone
		assertTrue(testGameBoard.placeStone(new Point(8, 9), bl));
		assertTrue(testGameBoard.placeStone(new Point(9, 8), bl));
		assertTrue(testGameBoard.placeStone(new Point(9, 10), bl));
		//White stone should not be removed
		assertFalse(testGameBoard.isEmpty(new Point(9, 9)));
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
		//Checking score
		assertEquals(2, testGameBoard.getCapturedBlackStones());
		
		
		
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
	public void testCapturingAtBorder() {
		assertTrue(testGameBoard.placeStone(new Point(11, 1), bl));
		assertTrue(testGameBoard.placeStone(new Point(12, 1), bl));
		assertTrue(testGameBoard.placeStone(new Point(10, 1), wh));
		assertTrue(testGameBoard.placeStone(new Point(11, 2), wh));
		assertTrue(testGameBoard.placeStone(new Point(12, 2), wh));
		assertTrue(testGameBoard.placeStone(new Point(13, 1), wh));
		//Black group should be gone
		assertTrue(testGameBoard.isEmpty(new Point(11, 1)));
		assertTrue(testGameBoard.isEmpty(new Point(12, 1)));
	}
	
	@Test
	public void testRemovingTwoGroupsWithNoBreaths() {
		//Setting up
		assertTrue(testGameBoard.placeStone(new Point(9, 9), wh));
		assertTrue(testGameBoard.placeStone(new Point(10, 8), wh));
		assertTrue(testGameBoard.placeStone(new Point(10, 10), wh));
		assertTrue(testGameBoard.placeStone(new Point(11, 9), wh));
		assertTrue(testGameBoard.placeStone(new Point(9, 10), bl));
		assertTrue(testGameBoard.placeStone(new Point(10, 11), bl));
		assertTrue(testGameBoard.placeStone(new Point(11, 8), bl));
		assertTrue(testGameBoard.placeStone(new Point(11, 10), bl));
		assertTrue(testGameBoard.placeStone(new Point(12, 9), bl));
		//Placing black stone to kill two separated white stones
		assertTrue(testGameBoard.placeStone(new Point(10, 9), bl));
		//Checking if stones are removed
		assertTrue(testGameBoard.isEmpty(new Point(10, 10)));
		assertTrue(testGameBoard.isEmpty(new Point(11, 9)));
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
	
	@Test
	public void testRemoveDeadGroups() {
		assertTrue(testGameBoard.placeStone(new Point(4, 3), bl));

		assertTrue(testGameBoard.placeStone(new Point(4, 4), wh));
		assertTrue(testGameBoard.placeStone(new Point(4, 6), wh));
		assertTrue(testGameBoard.placeStone(new Point(4, 5), wh));
		
		testGameBoard.changeGroupStatus(new Point(4, 4));
		testGameBoard.changeGroupStatus(new Point(4, 3));
		
		testGameBoard.removeAllDeadGroups();
		
		assertEquals(1, testGameBoard.getCapturedBlackStones());
		assertEquals(3, testGameBoard.getCapturedWhiteStones());
	}

}
