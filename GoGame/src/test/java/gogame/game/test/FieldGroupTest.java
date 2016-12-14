package gogame.game.test;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import gogame.game.engine.BoardFieldOwnership;
import gogame.game.engine.FieldGroup;
import gogame.game.engine.GameBoard;

public class FieldGroupTest {
	private FieldGroup testGroup;
	private GameBoard mainGameBoard;

	@Before
	public void setUp() throws Exception {
		mainGameBoard = new GameBoard();
		testGroup = new FieldGroup(mainGameBoard);
	}

	@Test
	public void testAddingPoint() {
		Point testPoint = new Point(9, 9);
		testGroup.addToGroup(testPoint);
		
		assertTrue(testGroup.contains(testPoint));
		assertEquals(4, testGroup.getBreathsLeft());
		
	}
	
	@Test
	public void testAddingPointWithOccupiedNearbyField() {
		Point testPoint = new Point(8, 9);
		mainGameBoard.placeStone(testPoint, BoardFieldOwnership.BLACK);
		
		testPoint = new Point (9, 9);
		testGroup.addToGroup(testPoint);
		
		assertTrue(testGroup.contains(testPoint));
		assertEquals(3, testGroup.getBreathsLeft());
		
	}
	
	@Test
	public void testAddingPointInCorner() {
		Point testPoint = new Point(19, 19);
		testGroup.addToGroup(testPoint);
		
		assertTrue(testGroup.contains(testPoint));
		assertEquals(2, testGroup.getBreathsLeft());
		
	}
	
	@Test
	public void testAddingPointAtEdge() {
		Point testPoint = new Point(4, 19);
		testGroup.addToGroup(testPoint);
		
		assertTrue(testGroup.contains(testPoint));
		assertEquals(3, testGroup.getBreathsLeft());
	}
	
	@Test
	public void testIsNearby() {
		Point testPoint = new Point(9, 9);
		testGroup.addToGroup(testPoint);
		
		testPoint = new Point(8, 9);
		assertTrue(testGroup.isNextTo(testPoint));
	}
	
	@Test
	public void testClearingFields() {
		ArrayList<Point> testPoints = new ArrayList<>();
		for (int i=9; i<13; i++) {
			testPoints.add(new Point(i, 9));
			
		}
		for (Point p : testPoints) {
			mainGameBoard.placeStone(p, BoardFieldOwnership.BLACK);
			testGroup.addToGroup(p);
		}
		
		for (Point p : testPoints) {
			assertTrue(testGroup.contains(p));
		}
		assertEquals(10, testGroup.getBreathsLeft());
		
		assertEquals(4, testGroup.killThisGroup());
		
		for (Point p : testPoints) {
			assertTrue(mainGameBoard.isEmpty(p));
		}
	}

}
