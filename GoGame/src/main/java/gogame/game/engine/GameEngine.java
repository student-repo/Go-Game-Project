package gogame.game.engine;

import gogame.game.exceptions.IncorrectMoveException;

public class GameEngine {

	private GameBoard gameBoard, prevGameBoard;
	private int whiteScore, blackScore;
	private GameEngineStatus gameStatus;
	private BoardFieldOwnership currentPlayer;
	
	public GameEngine() {
		this.gameStatus = GameEngineStatus.PREPARING;
		this.gameBoard = new GameBoard();
		this.prevGameBoard = new GameBoard();
		this.whiteScore = 0;
		this.blackScore = 0;
		this.currentPlayer = BoardFieldOwnership.BLACK;
	}
	
	public void makeMove(int x, int y)  throws IncorrectMoveException {
		
		
	}
	
	public BoardFieldOwnership getCurrentPlayer() {
		return currentPlayer;
	}
	
	public void passTurn() {
		changeCurrentPlayer();
	}
	
	private void changeCurrentPlayer() {
		if (this.currentPlayer.compareTo(BoardFieldOwnership.BLACK)==0) {
			this.currentPlayer = BoardFieldOwnership.WHITE;
		}
		else
			this.currentPlayer = BoardFieldOwnership.BLACK;
	}

	public BoardFieldOwnership getFieldOwnership(int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
