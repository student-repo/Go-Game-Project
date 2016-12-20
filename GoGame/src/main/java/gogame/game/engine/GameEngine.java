package gogame.game.engine;

import java.awt.Point;

import gogame.game.exceptions.IncorrectMoveException;

public class GameEngine {

	private GameBoard gameBoard;
	private GameEngineStatus gameStatus;
	private BoardFieldOwnership currentPlayer;
	private Player blackPlayer;
	private Player whitePlayer;
	private int turnCounter, 
				passCounter; //Checking if both player passed a turn
	private int whiteScore,
				blackScore;
	
	public GameEngine() {
		this.gameStatus = GameEngineStatus.PREPARING;
		this.gameBoard = new GameBoard();
		this.currentPlayer = BoardFieldOwnership.BLACK;
		this.turnCounter = 0;
		this.blackScore = 0;
		this.whiteScore = 0;
		this.gameStatus = GameEngineStatus.GAME;
		blackPlayer.notifyGameStateChanged(gameStatus);
		whitePlayer.notifyGameStateChanged(gameStatus);
	}
	
	public GameEngine(Player white, Player black) {
		this.gameStatus = GameEngineStatus.PREPARING;
		this.gameBoard = new GameBoard();
		this.currentPlayer = BoardFieldOwnership.BLACK;
		this.blackPlayer = black;
		this.whitePlayer = white;
		this.turnCounter = 0;
		this.blackScore = 0;
		this.whiteScore = 0;
		this.gameStatus = GameEngineStatus.GAME;
		blackPlayer.notifyGameStateChanged(gameStatus);
		whitePlayer.notifyGameStateChanged(gameStatus);
	}
	
	public void makeMove(int x, int y, Player player)  throws IncorrectMoveException {
		if (player.getColor() != currentPlayer)
			throw new IncorrectMoveException("Not your turn");
		if (player == blackPlayer) {
			if (!gameBoard.placeStone(new Point (x, y), BoardFieldOwnership.BLACK))
				throw new IncorrectMoveException("Move not permitted");
			else {
				passCounter = 0;
				turnCounter++;
				changeCurrentPlayer();
			}
		}
		else if (player == whitePlayer) {
			if (!gameBoard.placeStone(new Point (x, y), BoardFieldOwnership.WHITE))
				throw new IncorrectMoveException("Move not permitted");
			else {
				passCounter = 0;
				changeCurrentPlayer();
				turnCounter++;
			}
			
		}
		else ;
		
	}
	
	public void resumeGame(Player player) {
		this.gameStatus = GameEngineStatus.GAME;
	}
	
	public void endGame() {
		this.gameStatus = GameEngineStatus.FINISHED;
		blackPlayer.notifyGameStateChanged(gameStatus);
		whitePlayer.notifyGameStateChanged(gameStatus);
		whitePlayer.announceWinner(getWinner(), blackScore, whiteScore);
		blackPlayer.announceWinner(getWinner(), blackScore, whiteScore);
	}
	
	public BoardFieldOwnership getCurrentPlayer() {
		return currentPlayer;
	}
	
	public void passTurn() {
		changeCurrentPlayer();
		turnCounter++;
		passCounter++;
		if (passCounter == 2) {
			gameStatus = GameEngineStatus.NEGOTIATION;
			blackPlayer.notifyGameStateChanged(gameStatus);
			whitePlayer.notifyGameStateChanged(gameStatus);
		}
	}
	
	public int getTurnCounter() {
		return this.turnCounter;
	}
	
	public void concedeGame(Player player) {
		if (player.getColor() == BoardFieldOwnership.BLACK) {
			blackScore = -10;
			gameStatus = GameEngineStatus.FINISHED;
			blackPlayer.notifyGameStateChanged(gameStatus);
			whitePlayer.notifyGameStateChanged(gameStatus);
		}
		else {
			whiteScore = -10;
			gameStatus = GameEngineStatus.FINISHED;
			blackPlayer.notifyGameStateChanged(gameStatus);
			whitePlayer.notifyGameStateChanged(gameStatus);
		}
	}
	
	private BoardFieldOwnership getWinner() {
		if((gameBoard.getCapturedWhiteStones() + 6) >= gameBoard.getCapturedBlackStones())
			return BoardFieldOwnership.WHITE;
		else
			return BoardFieldOwnership.BLACK;
	}
	
	private void changeCurrentPlayer() {
		if (currentPlayer == BoardFieldOwnership.BLACK) {
			this.currentPlayer = BoardFieldOwnership.WHITE;
		}
		else
			this.currentPlayer = BoardFieldOwnership.BLACK;
	}
	
}
