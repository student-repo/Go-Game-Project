package gogame.game.engine;

import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import gogame.game.exceptions.IncorrectMoveException;

public class FieldGroup {
	
	public HashSet<Point> fieldsInGroup;
	public HashSet<Point> fieldsToKillThisGroup;
	private GameBoard mainGameBoard;
	
	public FieldGroup() {
		this.fieldsInGroup = new HashSet<Point>();
		this.fieldsToKillThisGroup = new HashSet<Point>();
	}
	
	public FieldGroup(GameBoard gameBoard) {
		this.mainGameBoard = gameBoard;
		this.fieldsInGroup = new HashSet<Point>();
		this.fieldsToKillThisGroup = new HashSet<Point>();
	}
	
	public boolean addToGroup(Point newPoint) {
		if (fieldsToKillThisGroup.size()==1)
			return false;
		int x = newPoint.x;
		int y = newPoint.y;
		fieldsInGroup.add(newPoint);
		fieldsToKillThisGroup.remove(newPoint);
		Point point;
		if (y>1) {
			point = new Point(x, y-1);
			if (!(this.contains(point)) && mainGameBoard.isEmpty(point)) 
				fieldsToKillThisGroup.add(point);
		}
		if (y<19) {
			point = new Point(x, y+1);
			if (!(this.contains(point)) && mainGameBoard.isEmpty(point)) 
				fieldsToKillThisGroup.add(point);
		}
		if (x>1) {
			point = new Point(x-1, y);
			if (!(this.contains(point)) && mainGameBoard.isEmpty(point)) 
				fieldsToKillThisGroup.add(point);
		}
		if (x<19) {
			point = new Point(x+1, y);
			if (!(this.contains(point)) && mainGameBoard.isEmpty(point)) 
				fieldsToKillThisGroup.add(point);
		}
		return true;
		
	}
	
	/**
	 * Merges this group and another one given as parameter and returns a new one made from those two
	 * @param gr second group to merge
	 * @return a new bigger group
	 * @throws IncorrectMoveException if move will result in a death of group exception is thrown
	 */
	public FieldGroup mergeGroups(FieldGroup gr) throws IncorrectMoveException{
		FieldGroup newGroup;
		newGroup = new FieldGroup(mainGameBoard);
		HashSet<Point> pointsToAdd;
		pointsToAdd = gr.getPoints();
		Iterator<Point> it;
		it = fieldsInGroup.iterator();
		while (it.hasNext()) {
			newGroup.addToGroup(it.next());
		}
		it = pointsToAdd.iterator();
		while (it.hasNext()) {
			newGroup.addToGroup(it.next());
		}
		if (newGroup.getBreathOfGroup() == 0)
			throw new IncorrectMoveException("This is a suicidal move. It is not permitted.");
//		System.out.println("Merge group: "+newGroup.fieldsInGroup + "  " + newGroup.fieldsToKillThisGroup);
		return newGroup;
	}
	
	public HashSet<Point> getPoints() {
		return this.fieldsInGroup;
	}
	
	/**
	 * Returns number of breaths this group has left
	 * @return Amount of breaths
	 */
	public int getBreathOfGroup() {
		return fieldsToKillThisGroup.size();
	}
	
	
	/**
	 * Gets how many stones are connected within this group
	 * @return Amount of stones i nthis group
	 */
	public int getGroupSize() {
		return this.fieldsInGroup.size();
	}
	
	public void setFieldAsOccupied(Point point) {
		fieldsToKillThisGroup.remove(point);
	}
	
	public boolean isNextTo (Point point) {
		return fieldsToKillThisGroup.contains(point);
	}
	
	public boolean contains (Point point) {
		return fieldsInGroup.contains(point);
	}

}
