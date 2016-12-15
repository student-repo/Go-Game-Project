package gogame.bot.alphabot;

import java.awt.Point;

import gogame.game.engine.GameBoard;
import gogame.game.engine.GameEngine;
import gogame.game.engine.Player;

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

	@Override
	public void yourTurnAnnotation(Point opponentPoint) {
		// TODO Auto-generated method stub
		
	}
	
	private Point prepareMove() {
		return null;
	}
	
	

}
