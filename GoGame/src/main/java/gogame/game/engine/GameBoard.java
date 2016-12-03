package gogame.game.engine;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import gogame.game.exceptions.IncorrectMoveException;

public class GameBoard {

	private HashMap<Point, BoardFieldOwnership> boardFields;
	private ArrayList<FieldGroup> blackGroups, whiteGroups;
	
	public GameBoard() {
		boardFields = new HashMap<Point, BoardFieldOwnership>();
		
		for (int i=1; i<=19; i++) {
			for (int j=1; j<=19; j++) {
				boardFields.put(new Point(i, j), BoardFieldOwnership.FREE);
			}
		}
		blackGroups = new ArrayList<FieldGroup>();
		whiteGroups = new ArrayList<FieldGroup>();
	}
	
	/**
	 * Places a stone on given position and returns true if did it successfully, false otherwise
	 * @param x 1st coordinate
	 * @param y 2nd coordinate
	 * @param player enum value describing which player placed it
	 * @return true if move was successfully executed, false otherwise
	 */
	public boolean placeStone(int x, int y, BoardFieldOwnership player) {
		if (!(this.isEmpty(x, y))) {
			return false;
		}
		Point point = new Point(x, y);
		FieldGroup newGroup = new FieldGroup(this);
		newGroup.addToGroup(point);
		ArrayList<FieldGroup> tempList;
		if (player == BoardFieldOwnership.BLACK) {
			tempList = getNearbyWhiteGroups(point);
			for (FieldGroup gr : tempList) {
				if (gr.getBreathOfGroup() == 1) {
					for (FieldGroup grp : tempList) {
						grp.setFieldAsOccupied(point);
						if (grp.getBreathOfGroup() == 0) {
							this.clearFields(grp);
							whiteGroups.remove(grp);
						}
					}
				}
			}
			tempList = getNearbyBlackGroups(point);
			for (FieldGroup gr : tempList) {
				try {
					newGroup.mergeGroups(gr);
				} catch (IncorrectMoveException e) {
					return false;
				}
			}
			blackGroups.add(newGroup);
		}
		else {
			tempList = getNearbyBlackGroups(point);
			for (FieldGroup gr : tempList) {
				if (gr.getBreathOfGroup() == 1) {
					for (FieldGroup grp : tempList) {
						grp.setFieldAsOccupied(point);
						if (grp.getBreathOfGroup() == 0) {
							this.clearFields(grp);
							blackGroups.remove(grp);
						}
					}
				}
			}
			tempList = getNearbyWhiteGroups(point);
			for (FieldGroup gr : tempList) {
				try {
					newGroup.mergeGroups(gr);
				} catch (IncorrectMoveException e) {
					return false;
				}
			}
			whiteGroups.add(newGroup);
		}
		return true;
	}
	
	private void clearFields(FieldGroup grp) {
		HashSet<Point> points;
		points = grp.getPoints();
		for (Point pt : points) {
			this.boardFields.put(pt, BoardFieldOwnership.FREE);
		}
		
	}

	/**
	 * Returns current board size
	 * @return board size as Integer
	 */
	public int getBoardSize() {
		return boardFields.size();
	}
	
	/**
	 * Checks if field with coordinates x and y is empty
	 * @param x x axis coordinate
	 * @param y y axis coordinate 
	 * @return true if is empty, false if not
	 */
	public boolean isEmpty(int x, int y) {
		Point point;
		point = new Point(x, y);
		if (boardFields.get(point) == BoardFieldOwnership.FREE)
			return true;
		else
			return false;
	}
	
	/**
	 * Checks if field is empty
	 * @param point point to be checked
	 * @return true if is empty, false if not
	 */
	public boolean isEmpty(Point point) {
		if (boardFields.get(point) == BoardFieldOwnership.FREE)
			return true;
		else
			return false;
	}
	
	private ArrayList<FieldGroup> getNearbyWhiteGroups(Point point) {
		ArrayList<FieldGroup> nearbyGroups;
		nearbyGroups = new ArrayList<FieldGroup>();
		for (FieldGroup gr : whiteGroups) {
			if (gr.isNextTo(point))
				nearbyGroups.add(gr);
		}
		return nearbyGroups;
	}
	
	private ArrayList<FieldGroup> getNearbyBlackGroups(Point point) {
		ArrayList<FieldGroup> nearbyGroups;
		nearbyGroups = new ArrayList<FieldGroup>();
		for (FieldGroup gr : blackGroups) {
			if (gr.isNextTo(point))
				nearbyGroups.add(gr);
		}
		return nearbyGroups;
	}
}
