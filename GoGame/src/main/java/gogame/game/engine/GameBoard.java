package gogame.game.engine;

import java.awt.Point;
import java.util.*;

import gogame.game.exceptions.IncorrectMoveException;

public class GameBoard {

	private HashMap<Point, BoardFieldOwnership> boardFields;
	private ArrayList<FieldGroup> blackGroups;
	private ArrayList<FieldGroup> whiteGroups;
	
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
	public static void main(String[] args){
		GameBoard g = new GameBoard();
//		System.out.println(g.boardFields.toString());
		g.placeStone(2,2,BoardFieldOwnership.BLACK);
		g.placeStone(2,4,BoardFieldOwnership.BLACK);
		g.placeStone(4,2,BoardFieldOwnership.BLACK);
		g.placeStone(3,2,BoardFieldOwnership.BLACK);
		g.placeStone(2,3,BoardFieldOwnership.BLACK);
//		g.placeStone(3,6,BoardFieldOwnership.WHITE);
		g.placeStone(2,1,BoardFieldOwnership.WHITE);
		g.placeStone(4,3,BoardFieldOwnership.WHITE);
		g.placeStone(3,1,BoardFieldOwnership.WHITE);
		g.placeStone(3,3,BoardFieldOwnership.WHITE);
		g.placeStone(1,4,BoardFieldOwnership.WHITE);
		g.placeStone(1,2,BoardFieldOwnership.WHITE);
		g.placeStone(4,1,BoardFieldOwnership.WHITE);
		g.placeStone(5,2,BoardFieldOwnership.WHITE);
		g.placeStone(1,3,BoardFieldOwnership.WHITE);
		g.placeStone(2,5,BoardFieldOwnership.WHITE);




//		System.out.println("amount white groups: " + whiteGroups.size());
//		System.out.println("amount black groups: " + blackGroups.size());
//		for(FieldGroup f: blackGroups){
//			System.out.println(f.fieldsInGroup.toString().replaceAll("java.awt.Point", "") + " amount group: " + f.fieldsInGroup.size());
//			System.out.println(f.fieldsToKillThisGroup.toString().replaceAll("java.awt.Point", "") + " amount to kill: " + f.fieldsToKillThisGroup.size());
//		}
//
//		System.out.println();
//		System.out.println();
//		System.out.println();
//		for(FieldGroup f: whiteGroups){
//			System.out.println(f.fieldsInGroup.toString().replaceAll("java.awt.Point", "") + " amount group: " + f.fieldsInGroup.size());
//			System.out.println(f.fieldsToKillThisGroup.toString().replaceAll("java.awt.Point", "") + " amount to kill: " + f.fieldsToKillThisGroup.size());
//		}

//		for(Point p: boardFields.keySet()){
//			if(boardFields.get(p) != BoardFieldOwnership.FREE){
//				System.out.println(p.toString().replace("java.awt.Point", "") + "  " + boardFields.get(p));
//			}
//		}


//		for(Point h: boardFields.keySet()){
//
//			if(boardFields.get(h).equals(BoardFieldOwnership.BLACK)){
//				System.out.println(h);
//			}
//		}

	}
	
	/**
	 * Places a stone on given position and returns true if did it successfully, false otherwise
	 * @param x 1st coordinate
	 * @param y 2nd coordinate
	 * @param player enum value describing which player placed it
	 * @return true if move was successfully executed, false otherwise
	 */
	public boolean placeStone(int x, int y, BoardFieldOwnership player) {
		if (!(this.isEmpty(x, y))) {
			return false;
		}
		Point point = new Point(x, y);
		FieldGroup newGroup = new FieldGroup(this);
		newGroup.addToGroup(point);
		ArrayList<FieldGroup> tempList;
		if (player == BoardFieldOwnership.BLACK) {

			tempList = getNearbyBlackGroups(point);
			for (FieldGroup gr : tempList) {
					newGroup.fieldsInGroup.addAll(gr.fieldsInGroup);
					newGroup.fieldsToKillThisGroup.addAll(gr.fieldsToKillThisGroup);
			}
			newGroup.fieldsInGroup.add(new Point(x,y));
			newGroup.fieldsToKillThisGroup.remove(new Point(x,y));
			blackGroups.add(newGroup);
			boardFields.put(new Point(x,y), BoardFieldOwnership.BLACK);
			foo2(foo1(new Point(x,y)));

			for(int i=0; i< whiteGroups.size(); i++){
				Point p = new Point(x,y);
				if(whiteGroups.get(i).fieldsToKillThisGroup.contains(p)){
					whiteGroups.get(i).fieldsToKillThisGroup.remove(p);
				}
				if(whiteGroups.get(i).fieldsToKillThisGroup.size() == 0){
					for(Point o: whiteGroups.get(i).fieldsInGroup){
						boardFields.put(o,BoardFieldOwnership.FREE);
						for(int k = 0; k < blackGroups.size(); k++){
							if(blackGroups.get(k).fieldsInGroup.contains(new Point(o.x - 1, o.y)) || blackGroups.get(k).fieldsInGroup.contains(new Point(o.x, o.y - 1)) || blackGroups.get(k).fieldsInGroup.contains(new Point(o.x + 1, o.y)) || blackGroups.get(k).fieldsInGroup.contains(new Point(o.x, o.y + 1))){
								blackGroups.get(k).fieldsToKillThisGroup.add(o);
							}
						}
					}
					whiteGroups.remove(i);
				}
			}
		}
		else {
			tempList = getNearbyWhiteGroups(point);
			for (FieldGroup gr : tempList) {
				newGroup.fieldsInGroup.addAll(gr.fieldsInGroup);
				newGroup.fieldsToKillThisGroup.addAll(gr.fieldsToKillThisGroup);
			}
			newGroup.fieldsInGroup.add(new Point(x,y));
			newGroup.fieldsToKillThisGroup.remove(new Point(x,y));
			whiteGroups.add(newGroup);
			boardFields.put(new Point(x,y), BoardFieldOwnership.WHITE);
			foo4(foo3(new Point(x,y)));

			for(int i=0; i< blackGroups.size(); i++){
				Point p = new Point(x,y);
				if(blackGroups.get(i).fieldsToKillThisGroup.contains(p)){
					blackGroups.get(i).fieldsToKillThisGroup.remove(p);
				}
				if(blackGroups.get(i).fieldsToKillThisGroup.size() == 0){
					for(Point o: blackGroups.get(i).fieldsInGroup){
						boardFields.put(o,BoardFieldOwnership.FREE);
						for(int k = 0; k < whiteGroups.size(); k++){
							if(whiteGroups.get(k).fieldsInGroup.contains(new Point(o.x - 1, o.y)) || whiteGroups.get(k).fieldsInGroup.contains(new Point(o.x, o.y - 1)) || whiteGroups.get(k).fieldsInGroup.contains(new Point(o.x + 1, o.y)) || whiteGroups.get(k).fieldsInGroup.contains(new Point(o.x, o.y + 1))){
								whiteGroups.get(k).fieldsToKillThisGroup.add(o);
							}
						}
					}
					blackGroups.remove(i);
				}
			}
		}
		return true;
	}
	
	private void clearFields(FieldGroup grp) {
		HashSet<Point> points;
		points = grp.getPoints();
		for (Point pt : points) {
			this.boardFields.put(pt, BoardFieldOwnership.FREE);
		}
		
	}

	/**
	 * Returns current board size
	 * @return board size as Integer
	 */
	public int getBoardSize() {
		return boardFields.size();
	}
	
	/**
	 * Checks if field with coordinates x and y is empty
	 * @param x x axis coordinate
	 * @param y y axis coordinate 
	 * @return true if is empty, false if not
	 */
	public boolean isEmpty(int x, int y) {
		Point point;
		point = new Point(x, y);
		if (boardFields.get(point) == BoardFieldOwnership.FREE)
			return true;
		else
			return false;
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
	
	private ArrayList<FieldGroup> getNearbyWhiteGroups(Point point) {
		ArrayList<FieldGroup> nearbyGroups;
		nearbyGroups = new ArrayList<FieldGroup>();
		for (FieldGroup gr : whiteGroups) {
			if (gr.isNextTo(point))
				nearbyGroups.add(gr);
		}
		return nearbyGroups;
	}
	
	private ArrayList<FieldGroup> getNearbyBlackGroups(Point point) {
		ArrayList<FieldGroup> nearbyGroups;
		nearbyGroups = new ArrayList<FieldGroup>();
		for (FieldGroup gr : blackGroups) {
			if (gr.isNextTo(point))
				nearbyGroups.add(gr);
		}
		return nearbyGroups;
	}

	private ArrayList<Integer> foo1(Point point) {
		ArrayList<Integer> nearbyGroups = new ArrayList<>();
		for(int i = 0; i < blackGroups.size(); i++){
			if (blackGroups.get(i).isNextTo(point))
				nearbyGroups.add(i);
		}
		return nearbyGroups;
	}

	private void foo2(ArrayList<Integer> arr){
		Collections.sort(arr, Collections.reverseOrder());
		for (int i : arr)
			blackGroups.remove(i);
	}

	private ArrayList<Integer> foo3(Point point) {
		ArrayList<Integer> nearbyGroups = new ArrayList<>();
		for(int i = 0; i < whiteGroups.size(); i++){
			if (whiteGroups.get(i).isNextTo(point))
				nearbyGroups.add(i);
		}
		return nearbyGroups;
	}

	private void foo4(ArrayList<Integer> arr){
		Collections.sort(arr, Collections.reverseOrder());
		for (int i : arr)
			whiteGroups.remove(i);
	}

	public HashMap<Point, BoardFieldOwnership> getBoardFields(){
		return boardFields;
	}
}
