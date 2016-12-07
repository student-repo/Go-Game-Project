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

	public boolean isNextTo (Point point) {
		return fieldsToKillThisGroup.contains(point);
	}

	public boolean contains (Point point) {
		return fieldsInGroup.contains(point);
	}

}