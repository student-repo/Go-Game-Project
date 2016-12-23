package gogame.bot.alphabot;

import java.awt.Point;
import java.util.Random;

import gogame.game.engine.*;
import gogame.game.exceptions.*;

public class AlphaBot implements Player{
	
	private GameBoard myGameBoard;
	private GameEngine game;
	private GameEngineStatus currentStatus;
	private BoardFieldOwnership color;
	private Random rand = new Random();
	
	public AlphaBot() {
		this.myGameBoard = new GameBoard();
	}
	
	public AlphaBot(GameEngine game) {
		this.myGameBoard = new GameBoard();
		this.game = game;
	}

	private void makeMove() {
		if (currentStatus == GameEngineStatus.GAME) {
			Point p;
			boolean valid = false;
			p = prepareMove();
			while (valid) {
				try {
					game.makeMove(p.x, p.y, this);
					valid = true;
				}
				catch (IncorrectMoveException e) {
					break;
				}
			}
			if (valid) {
				myGameBoard.placeStone(p, getColor());
			}
			else {
				game.passTurn();
			}
		}
		else if (currentStatus == GameEngineStatus.NEGOTIATION) {
			
		}
	}
	
	private Point prepareMove() {
		Point goodPoint;
		
		goodPoint = new Point (rand.nextInt(19), rand.nextInt(19));
		
		return goodPoint;
	}

	@Override
	public void stonePlaced(Point opponentPoint, BoardFieldOwnership player) {
		if (player != color) 
			myGameBoard.placeStone(opponentPoint, player);
		makeMove();
		
	}

	@Override
	public void playerPassedTurn(BoardFieldOwnership player) {
		makeMove();
		
	}

	@Override
	public void setColor(BoardFieldOwnership color) {
		this.color = color;
		
	}

	@Override
	public BoardFieldOwnership getColor() {
		return this.color;
	}

	@Override
	public void notifyGameStateChanged(GameEngineStatus newStatus) {
		currentStatus = newStatus;
		
	}

	@Override
	public void announceWinner(BoardFieldOwnership winner, int blackScore, int whiteScore) {
		
	}
	
	

}
