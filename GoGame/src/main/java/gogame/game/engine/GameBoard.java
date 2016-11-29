package gogame.game.engine;

import gogame.game.exceptions.IncorrectMoveException;

public class GameBoard {

	private BoardField[][] boardFields;
	
	public GameBoard() {
		boardFields = new BoardField[19][19];
	}
	
	public void changeFieldOwner (int x, int y, BoardFieldOwnership newOwner) throws IncorrectMoveException {
		
	}
}
