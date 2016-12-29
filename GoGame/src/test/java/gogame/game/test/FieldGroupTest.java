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
		assertTrue(testGroup.isNextTo(new Point(testPoint.x-1, testPoint.y)));
		assertTrue(testGroup.isNextTo(new Point(testPoint.x+1, testPoint.y)));
		assertTrue(testGroup.isNextTo(new Point(testPoint.x, testPoint.y-1)));
		assertTrue(testGroup.isNextTo(new Point(testPoint.x, testPoint.y+1)));
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
		Point testPoint = new Point(18, 18);
		testGroup.addToGroup(testPoint);
		
		assertTrue(testGroup.contains(testPoint));
		assertEquals(2, testGroup.getBreathsLeft());
		
	}
	
	@Test
	public void testAddingPointAtEdge() {
		Point testPoint = new Point(4, 18);
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
	public void testEyeFormation() {
		testGroup.addToGroup(new Point(9, 9));
		testGroup.addToGroup(new Point(9, 10));
		testGroup.addToGroup(new Point(9, 11));
		testGroup.addToGroup(new Point(10, 9));
		testGroup.addToGroup(new Point(10, 11));
		testGroup.addToGroup(new Point(11, 9));
		testGroup.addToGroup(new Point(11, 10));
		testGroup.addToGroup(new Point(11, 11));
		testGroup.addToGroup(new Point(12, 9));
		testGroup.addToGroup(new Point(12, 11));
		testGroup.addToGroup(new Point(13, 9));
		testGroup.addToGroup(new Point(13, 10));
		testGroup.addToGroup(new Point(13, 11));
		
		assertEquals(18, testGroup.getBreathsLeft());
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
