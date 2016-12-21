package gogame.game.engine;

import java.awt.Point;
import java.util.*;


public class GameBoard {

	private HashMap<Point, BoardFieldOwnership> boardFields;
	private ArrayList<FieldGroup> blackGroups;
	private ArrayList<FieldGroup> whiteGroups;
	private int capturedWhiteStones = 0, capturedBlackStones = 0;
	private Point koPoint = null;
	private boolean koTestNeeded = false;
	private boolean whiteMove = true;

	/**
	 *  Default constructor
	 */
	public GameBoard() {
		boardFields = new HashMap<Point, BoardFieldOwnership>();

		for (int i=1; i<=19; i++) {
			for (int j=1; j<=19; j++) {
				boardFields.put(new Point(i, j), BoardFieldOwnership.FREE);
			}
		}
		blackGroups = new ArrayList<FieldGroup>();
		whiteGroups = new ArrayList<FieldGroup>();
		
	}
	

	/**
	 * Places a stone on given position and returns true if did it successfully, false otherwise
	 * @param point coordinate
	 * @param player enum value describing which player placed it
	 * @return true if move was successfully executed, false otherwise
	 */
	public boolean placeStone(Point point, BoardFieldOwnership player) {
		ArrayList<FieldGroup> friendlyGroups, enemyGroups;
		boolean isNotSuicide = false;
		FieldGroup newGroup = new FieldGroup(this);
		HashSet<Point> points;
		int blackStonesRemoved = 0, 
				whiteStonesRemoved = 0;
		if (!(this.isEmpty(point))) {
			return false;
		}
		if (koTestNeeded) {
			if (point.equals(koPoint))
				return false;
		}
		if (player == BoardFieldOwnership.BLACK) {
			enemyGroups = getNearbyGroups(point, BoardFieldOwnership.WHITE);
			friendlyGroups = getNearbyGroups(point, player);
			if (enemyGroups.size() != 0) {
				for(FieldGroup gr : enemyGroups) {
					if (gr.getBreathsLeft() == 1) {
						if (gr.getGroupSize()==1){
							this.koPoint = gr.getKoPoint();
						}
						isNotSuicide = true;
						
						points = gr.getAllPointsInGroup();
						for (Point p : points) {
							ArrayList<FieldGroup> fgs = getNearbyGroups(p, player);
							for (FieldGroup fg : fgs)
								fg.notifyEmpty(p);
						}
						whiteStonesRemoved += gr.killThisGroup();
						whiteGroups.remove(gr);
					}
				}
			}
			
			if (friendlyGroups.size() == 0 && !isNotSuicide && !hasEmptyNearbyFields(point))
				return false;
			else {
				newGroup.addToGroup(point);
				for (FieldGroup gr : friendlyGroups) {
					points = gr.getAllPointsInGroup();
					for (Point p : points) {
						newGroup.addToGroup(p);
					}
				}
				if (newGroup.getBreathsLeft() == 0)
					return false;
				else {
					blackGroups.add(newGroup);
					for (FieldGroup gr : friendlyGroups) {
						blackGroups.remove(gr);
					}
				}
			}
				
		}
		else {
			enemyGroups = getNearbyGroups(point, BoardFieldOwnership.BLACK);
			friendlyGroups = getNearbyGroups(point, player);
			if (enemyGroups.size() != 0) {
				for(FieldGroup gr : enemyGroups) {
					if (gr.getBreathsLeft() == 1) {
						if (gr.getGroupSize()==1){
							this.koPoint = gr.getKoPoint();
						}
						isNotSuicide = true;
						
						points = gr.getAllPointsInGroup();
						for (Point p : points) {
							ArrayList<FieldGroup> fgs = getNearbyGroups(p, player);
							for (FieldGroup fg : fgs)
								fg.notifyEmpty(p);
						}
						blackStonesRemoved += gr.killThisGroup();
						blackGroups.remove(gr);
					}
				}
			}
			
			if (friendlyGroups.size() == 0 && !isNotSuicide && !hasEmptyNearbyFields(point))
				return false;
			else {
				newGroup.addToGroup(point);
				for (FieldGroup gr : friendlyGroups) {
					points = gr.getAllPointsInGroup();
					for (Point p : points) {
						newGroup.addToGroup(p);
					}
				}
				if (newGroup.getBreathsLeft() == 0)
					return false;
				else {
					whiteGroups.add(newGroup);
					for (FieldGroup gr : friendlyGroups) {
						whiteGroups.remove(gr);
					}
				}
			}
		}
		if(blackStonesRemoved == 1 || whiteStonesRemoved == 1) {
			koTestNeeded = true;
		}
		else
			koTestNeeded = false;
		this.capturedBlackStones += blackStonesRemoved;
		this.capturedWhiteStones += whiteStonesRemoved;
		boardFields.put(point, player);
		for(FieldGroup gr : enemyGroups) {
			gr.updateBreaths(point);
		}
		return true;
	}
	
	/**
	 * Makes field empty
	 * @param point Field to be emptied
	 */
	public void emptyField(Point point) {
		boardFields.put(point, BoardFieldOwnership.FREE);
	}

	/**
	 * Checks if field is empty
	 * @param point point to be checked
	 * @return true if is empty, false if not
	 */
	public boolean isEmpty(Point point) {
		if (boardFields.get(point) == BoardFieldOwnership.FREE)
			return true;
		else
			return false;
	}
	
	
	/**
	 * Changes group status if it consists given Point
	 * @param point Point inside group
	 * @return true if point was found, false otherwise
	 */
	public boolean changeGroupStatus (Point point) {
		for (FieldGroup g : blackGroups) {
			if (g.contains(point)) {
				g.changAliveStatus();
				return true;
			}
		}
		for (FieldGroup g : whiteGroups) {
			if (g.contains(point)) {
				g.changAliveStatus();
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Creates an array of GroupField objects near a Point p
	 * @param p Point around which we will look for groups
	 * @param color color of groups to be found
	 * @return An array of GroupField, if no groups were found return empty ArrayList
	 */
	
	private ArrayList<FieldGroup> getNearbyGroups (Point p, BoardFieldOwnership color) {
		ArrayList<FieldGroup> nearbyGroups = new ArrayList<>();
		if (color.equals(BoardFieldOwnership.BLACK)){
			for(FieldGroup gr : blackGroups) {
				if (gr.isNextTo(p)) {
					nearbyGroups.add(gr);
				}
			}
		} else {
			for(FieldGroup gr : whiteGroups) {
				if (gr.isNextTo(p)) {
					nearbyGroups.add(gr);
				}
			}
		}
		return nearbyGroups;
	}

	/**
	 * Gets how many white stones were captured
	 * @return Captured white stones
	 */
	public int getCapturedWhiteStones() {
		return capturedWhiteStones;
	}

	/**
	 * Gets how many black stones were captured
	 * @return Captured black stones
	 */
	public int getCapturedBlackStones() {
		return capturedBlackStones;
	}

	/**
	 * Returns current board as HashMap
	 * @return
	 */
	public HashMap<Point, BoardFieldOwnership> getBoardFields(){
		return boardFields;
	}
    
	/**
	 * Checks if point has empty fields next to it
	 * @param p point tested
	 * @return true if at least one field was empty
	 */
    private boolean hasEmptyNearbyFields (Point p){
    	int x = p.x;
		int y = p.y;
		Point point;
		if (y>1) {
			point = new Point(x, y-1);
			if (isEmpty(point))
				return true;
		}
		if (y<19) {
			point = new Point(x, y+1);
			if (isEmpty(point))
				return true;
		}
		if (x>1) {
			point = new Point(x-1, y);
			if (isEmpty(point))
				return true;
		}
		if (x<19) {
			point = new Point(x+1, y);
			if (isEmpty(point))
				return true;
		}
    	return false;
    }

    public String boardFieldsToString(){
		String white = "WHITE ";
		String black = "BLACK ";

		for (Map.Entry<Point, BoardFieldOwnership> entry : boardFields.entrySet()) {
			Point p = entry.getKey();
			BoardFieldOwnership value = entry.getValue();
			if(value == BoardFieldOwnership.BLACK){
				black += (int)p.getX() + " " + (int)p.getY() + " ";
			}
			else if (value == BoardFieldOwnership.WHITE){
				white += (int)p.getX() + " " + (int)p.getY() + " ";
			}
		}
		return white + " " + black;

	}

	public HashMap<Point, BoardFieldOwnership> stringToBoardFiels(String str){
		HashMap<Point, BoardFieldOwnership> boardFields2 = new HashMap<Point, BoardFieldOwnership>();
		for (int i=1; i<=19; i++) {
			for (int j=1; j<=19; j++) {
				boardFields2.put(new Point(i, j), BoardFieldOwnership.FREE);
			}
		}
		ArrayList<String> sss = new ArrayList<String>(Arrays.asList(str.split("\\s* \\s*")));
		int k = 1;
		while(!sss.get(k).equals("BLACK")){
			int x = Integer.parseInt(sss.get(k));
			int y = Integer.parseInt(sss.get(k + 1));

			Point p = new Point(x, y);
			boardFields2.put(p, BoardFieldOwnership.WHITE);
			k++;
			k++;
		}
		k++;

		while(k < sss.size()){
			int x1 = Integer.parseInt(sss.get(k));
			int y1 = Integer.parseInt(sss.get(k + 1));

			Point p = new Point(x1, y1);
			boardFields2.put(p, BoardFieldOwnership.BLACK);
			k++;
			k++;
		}

		return boardFields2;
	}
	public boolean whiteMove(){
		return whiteMove;
	}

	public void changeMove(){
		whiteMove = !whiteMove;
	}

	public static void main(String[] args){
		GameBoard g = new GameBoard();
//		g.placeStone(new Point(3, 4), BoardFieldOwnership.BLACK);
//		g.placeStone(new Point(4, 4), BoardFieldOwnership.BLACK);
//		g.placeStone(new Point(5, 4), BoardFieldOwnership.BLACK);
//		g.placeStone(new Point(6, 4), BoardFieldOwnership.BLACK);
//		g.placeStone(new Point(7, 3), BoardFieldOwnership.BLACK);
//		g.placeStone(new Point(8, 2), BoardFieldOwnership.BLACK);
//		g.placeStone(new Point(9, 1), BoardFieldOwnership.BLACK);
//		g.placeStone(new Point(10, 1), BoardFieldOwnership.BLACK);
//		g.placeStone(new Point(11, 3), BoardFieldOwnership.BLACK);
//		g.placeStone(new Point(12, 2), BoardFieldOwnership.BLACK);
//		g.placeStone(new Point(13, 2), BoardFieldOwnership.BLACK);

//		g.placeStone(new Point(17, 4), BoardFieldOwnership.WHITE);
//		g.placeStone(new Point(16, 2), BoardFieldOwnership.WHITE);
//		g.placeStone(new Point(15, 11), BoardFieldOwnership.WHITE);
//		g.placeStone(new Point(14, 12), BoardFieldOwnership.WHITE);
//		g.placeStone(new Point(13, 13), BoardFieldOwnership.WHITE);
//		g.placeStone(new Point(12, 14), BoardFieldOwnership.WHITE);
//		g.placeStone(new Point(11, 15), BoardFieldOwnership.WHITE);
//		g.placeStone(new Point(10, 16), BoardFieldOwnership.WHITE);
//		g.placeStone(new Point(9, 17), BoardFieldOwnership.WHITE);


//		System.out.println(g.boardFieldsToString());
//		g.stringToBoardFiels(g.boardFieldsToString());
//		System.out.println(g.stringToBoardFiels(g.boardFieldsToString()));

		HashMap<Point, BoardFieldOwnership> boardFields3 = g.stringToBoardFiels(g.boardFieldsToString());

//		boardFields3.put(new Point(9, 17), BoardFieldOwnership.BLACK);
		System.out.println(g.boardFields.toString().equals(boardFields3.toString()));

//		for (Map.Entry<Point, BoardFieldOwnership> entry : g.stringToBoardFiels(g.boardFieldsToString()).entrySet()) {
//			Point p = entry.getKey();
//			BoardFieldOwnership value = entry.getValue();
//			if(!(value == BoardFieldOwnership.FREE)){
//				System.out.println(p + " " + value + " | ");
//			}
//		}
	}

}
