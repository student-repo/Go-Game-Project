package gogame.game.engine;

public class GameEngine {

	private GameBoard gameBoard, prevGameBoard;
	private int whiteScore, blackScore;
	private GameEngineStatus gameStatus;
	
	public GameEngine() {
		this.gameStatus = GameEngineStatus.PREPARING;
		this.gameBoard = new GameBoard();
		this.prevGameBoard = new GameBoard();
		this.whiteScore = 0;
		this.blackScore = 0;
	}
	
	public void makeMove(int x, int y) {
		
	}
	
	
}
