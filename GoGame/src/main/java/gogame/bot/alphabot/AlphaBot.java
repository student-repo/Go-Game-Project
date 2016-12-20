package gogame.bot.alphabot;

import java.awt.Point;

import gogame.game.engine.*;

public class AlphaBot implements Player{
	
	private GameBoard myGameBoard;
	private GameEngine game;
	private boolean isWhite;
	
	public AlphaBot() {
		this.myGameBoard = new GameBoard();
	}
	
	public AlphaBot(GameEngine game) {
		this.myGameBoard = new GameBoard();
		this.game = game;
	}

	
	
	private Point prepareMove() {
		return null;
	}

	@Override
	public void stonePlaced(Point opponentPoint, BoardFieldOwnership player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playerPassedTurn(BoardFieldOwnership player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setColor(BoardFieldOwnership color) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BoardFieldOwnership getColor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void notifyGameStateChanged(GameEngineStatus newStatus) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void announceWinner(BoardFieldOwnership winner, int blackScore, int whiteScore) {
		// TODO Auto-generated method stub
		
	}
	
	

}
