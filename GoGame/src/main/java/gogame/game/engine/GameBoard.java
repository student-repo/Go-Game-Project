package gogame.game.engine;

import java.awt.Point;
import java.util.HashMap;

import gogame.game.exceptions.IncorrectMoveException;

public class GameBoard {

	private HashMap boardFields;
	
	public GameBoard() {
		boardFields = new HashMap<Point, BoardFieldOwnership>();
	}
	
	public boolean changeFieldOwner (int x, int y, BoardFieldOwnership newOwner) {
		return false;
	}
	
	
	
	
}
