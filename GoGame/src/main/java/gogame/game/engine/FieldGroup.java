package gogame.game.engine;

import java.awt.Point;
import java.util.ArrayList;
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
	
	public int getGroupSize() {
		return this.fieldsInGroup.size();
	}

	public void addToGroup(Point newPoint) {
		int x = newPoint.x;
		int y = newPoint.y;
		fieldsInGroup.add(newPoint);
		fieldsToKillThisGroup.remove(newPoint);
		Point point;
		if (y>1) {
			point = new Point(x, y-1);
			if (!fieldsInGroup.contains(point) && mainGameBoard.isEmpty(point))
				fieldsToKillThisGroup.add(point);
		}
		if (y<19) {
			point = new Point(x, y+1);
			if (!fieldsInGroup.contains(point) && mainGameBoard.isEmpty(point))
				fieldsToKillThisGroup.add(point);
		}
		if (x>1) {
			point = new Point(x-1, y);
			if (!fieldsInGroup.contains(point) && mainGameBoard.isEmpty(point))
				fieldsToKillThisGroup.add(point);
		}
		if (x<19) {
			point = new Point(x+1, y);
			if (!fieldsInGroup.contains(point) && mainGameBoard.isEmpty(point))
				fieldsToKillThisGroup.add(point);
		}
	}
	
	public Point getKoPoint() {
		if(this.fieldsInGroup.size()==1) {
			Iterator<Point> it = fieldsInGroup.iterator();
			if (it.hasNext())
				return it.next();
		}
		return null;
	}
	
	public void updateBreaths(Point point) {
		fieldsToKillThisGroup.remove(point);
	}
	
	public HashSet<Point> getAllPointsInGroup() {
		return this.fieldsInGroup;
	}

	public boolean isNextTo (Point point) {
		return fieldsToKillThisGroup.contains(point);
	}

	public boolean contains (Point point) {
		return fieldsInGroup.contains(point);
	}
	
	public int getBreathsLeft() {
		return this.fieldsToKillThisGroup.size();
	}

	public int killThisGroup() {
		for (Point p : fieldsInGroup) {
			mainGameBoard.emptyField(p);
		}
		return this.fieldsInGroup.size();
	}

}