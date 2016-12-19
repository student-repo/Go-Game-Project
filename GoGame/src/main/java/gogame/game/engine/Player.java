package gogame.game.engine;

import java.awt.Point;

public interface Player {
	
	public void stonePlaced(Point opponentPoint, BoardFieldOwnership player);
	
	public void playerPassedTurn(BoardFieldOwnership player);
	
	public void setColor (BoardFieldOwnership color);
	

}
