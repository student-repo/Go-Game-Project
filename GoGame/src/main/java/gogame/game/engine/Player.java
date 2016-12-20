package gogame.game.engine;

import java.awt.Point;

public interface Player {
	
	public void stonePlaced(Point opponentPoint, BoardFieldOwnership player);
	
	public void playerPassedTurn(BoardFieldOwnership player);
	
	public void setColor (BoardFieldOwnership color);
	
	public BoardFieldOwnership getColor ();
	
	public void notifyGameStateChanged (GameEngineStatus newState);
	
	public void announceWinner(BoardFieldOwnership winner, int blackScore, int whiteScore);
	

}
