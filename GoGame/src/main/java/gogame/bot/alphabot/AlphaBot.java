package gogame.bot.alphabot;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import gogame.game.engine.*;
import gogame.game.exceptions.*;

public class AlphaBot implements Player{

	private GameEngine gameEngine;
	private BoardFieldOwnership color;
	private GameEngineStatus currentStatus;
	private String name;

	public AlphaBot(BoardFieldOwnership color, GameEngineStatus currentStatus){
		this.color = color;
		this.currentStatus = currentStatus;
		name = "AplhaBot";
	}

	public String makeMove(){
		int x = ThreadLocalRandom.current().nextInt(0, 19);
		int y = ThreadLocalRandom.current().nextInt(0, 19);

		if(currentStatus == GameEngineStatus.GAME){
			try{
				gameEngine.makeMove(x, y, this);
				return x + " " + y;
			}
			catch (Exception e){
				gameEngine.passTurn(this);
				return "PASS";
			}
		}
		return "";
	}

	public void suggestTerritory(){
		TerritoryBoard territoryMode = new TerritoryBoard(gameEngine.getBoardFields(), color);
		territoryMode.getFinishBoardFields(color);
	}

	public void setGameEngine(GameEngine gameEngine){
		this.gameEngine = gameEngine;
	}

	public String getName(){
		return name;
	}

	@Override
	public void stonePlaced(Point opponentPoint, BoardFieldOwnership player) {
//		if (player != color)
//			myGameBoard.placeStone(opponentPoint, player);
//		makeMove();
		
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

	@Override
	public void territoryProposition(ArrayList<Point> blackTerritory, ArrayList<Point> whiteTerritory,
			BoardFieldOwnership player) {
		// TODO Auto-generated method stub
		
	}
	
	

}
