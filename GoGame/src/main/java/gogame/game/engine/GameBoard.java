package gogame.game.engine;

import java.awt.Point;
import java.util.*;


public class GameBoard {

	private static HashMap<Point, BoardFieldOwnership> boardFields;
	private static ArrayList<FieldGroup> blackGroups;
	private static ArrayList<FieldGroup> whiteGroups;

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
		g.placeStone(new Point(1,3),BoardFieldOwnership.BLACK);
		g.placeStone(new Point(3,2),BoardFieldOwnership.WHITE);
        g.placeStone(new Point(2,2),BoardFieldOwnership.BLACK);
        g.placeStone(new Point(2,3),BoardFieldOwnership.WHITE);
        g.placeStone(new Point(3,1),BoardFieldOwnership.BLACK);
        g.placeStone(new Point(3,4),BoardFieldOwnership.WHITE);
        g.placeStone(new Point(4,2),BoardFieldOwnership.BLACK);
        g.placeStone(new Point(4,4),BoardFieldOwnership.WHITE);
        g.placeStone(new Point(4,3),BoardFieldOwnership.BLACK);
        g.placeStone(new Point(5,4),BoardFieldOwnership.BLACK);
        g.placeStone(new Point(4,5),BoardFieldOwnership.BLACK);
        g.placeStone(new Point(3,5),BoardFieldOwnership.BLACK);
        g.placeStone(new Point(4,2),BoardFieldOwnership.BLACK);
        g.placeStone(new Point(2,4),BoardFieldOwnership.BLACK);
        g.placeStone(new Point(3,3),BoardFieldOwnership.BLACK);



		System.out.println("amount white groups: " + whiteGroups.size());
		System.out.println("amount black groups: " + blackGroups.size());
		for(FieldGroup f: blackGroups){
			System.out.println(f.fieldsInGroup.toString().replaceAll("java.awt.Point", "") + " amount group: " + f.fieldsInGroup.size());
			System.out.println(f.fieldsToKillThisGroup.toString().replaceAll("java.awt.Point", "") + " amount to kill: " + f.fieldsToKillThisGroup.size());
		}

		System.out.println();
		System.out.println();
		System.out.println();
		for(FieldGroup f: whiteGroups){
			System.out.println(f.fieldsInGroup.toString().replaceAll("java.awt.Point", "") + " amount group: " + f.fieldsInGroup.size());
			System.out.println(f.fieldsToKillThisGroup.toString().replaceAll("java.awt.Point", "") + " amount to kill: " + f.fieldsToKillThisGroup.size());
		}

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
	 * @param point coordinate
	 * @param player enum value describing which player placed it
	 * @return true if move was successfully executed, false otherwise
	 */
	public boolean placeStone(Point point, BoardFieldOwnership player) {
		if (!(this.isEmpty(point))) {
			return false;
		}
		FieldGroup newGroup = new FieldGroup(this);
		newGroup.addToGroup(point);
		ArrayList<Integer> nearbyGroupsIndexes;
		if (player == BoardFieldOwnership.BLACK) {

			nearbyGroupsIndexes = getNearbyGroupsIndexes(point, blackGroups);

			for(Integer i: nearbyGroupsIndexes){
				newGroup.fieldsInGroup.addAll(blackGroups.get(i).fieldsInGroup);
				newGroup.fieldsToKillThisGroup.addAll(blackGroups.get(i).fieldsToKillThisGroup);
			}
			newGroup.fieldsInGroup.add(point);
			newGroup.fieldsToKillThisGroup.remove(point);
			blackGroups.add(newGroup);
			boardFields.put(point, BoardFieldOwnership.BLACK);
            removeAllBlackGroups(nearbyGroupsIndexes);

            ArrayList<Integer> toRemoveGroupsIndexes1 = new ArrayList<>();
			for(int i=0; i< whiteGroups.size(); i++){
				if(whiteGroups.get(i).fieldsToKillThisGroup.contains(point)){
					whiteGroups.get(i).fieldsToKillThisGroup.remove(point);
				}
				if(whiteGroups.get(i).fieldsToKillThisGroup.size() == 0){
                    toRemoveGroupsIndexes1.add(i);
					for(Point o: whiteGroups.get(i).fieldsInGroup){
						boardFields.put(o,BoardFieldOwnership.FREE);
						for(int k = 0; k < blackGroups.size(); k++){
							if(blackGroups.get(k).fieldsInGroup.contains(new Point(o.x - 1, o.y)) || blackGroups.get(k).fieldsInGroup.contains(new Point(o.x, o.y - 1)) || blackGroups.get(k).fieldsInGroup.contains(new Point(o.x + 1, o.y)) || blackGroups.get(k).fieldsInGroup.contains(new Point(o.x, o.y + 1))){
								blackGroups.get(k).fieldsToKillThisGroup.add(o);
							}
						}
					}
				}
			}
            removeAllWhiteGroups(toRemoveGroupsIndexes1);

		}
		else {
			nearbyGroupsIndexes = getNearbyGroupsIndexes(point, whiteGroups);
			for(Integer i: nearbyGroupsIndexes){
				newGroup.fieldsInGroup.addAll(whiteGroups.get(i).fieldsInGroup);
				newGroup.fieldsToKillThisGroup.addAll(whiteGroups.get(i).fieldsToKillThisGroup);
			}
			newGroup.fieldsInGroup.add(point);
			newGroup.fieldsToKillThisGroup.remove(point);
			whiteGroups.add(newGroup);
			boardFields.put(point, BoardFieldOwnership.WHITE);
            removeAllWhiteGroups(nearbyGroupsIndexes);

            ArrayList<Integer> toRemoveGroupsIndexes2 = new ArrayList<>();
			for(int i=0; i< blackGroups.size(); i++){
				if(blackGroups.get(i).fieldsToKillThisGroup.contains(point)){
					blackGroups.get(i).fieldsToKillThisGroup.remove(point);
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
				}
			}
            removeAllBlackGroups(toRemoveGroupsIndexes2);
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

	private ArrayList<Integer> getNearbyGroupsIndexes(Point point, ArrayList<FieldGroup> groups) {
		ArrayList<Integer> nearbyGroupsIndexes = new ArrayList<>();
		for(int i = 0; i < groups.size(); i++){
			if (groups.get(i).isNextTo(point))
				nearbyGroupsIndexes.add(i);
		}
		return nearbyGroupsIndexes;
	}

	private void removeAllBlackGroups(ArrayList<Integer> arr){
		Collections.sort(arr, Collections.reverseOrder());
		for (int i : arr)
			blackGroups.remove(i);
	}

	private void removeAllWhiteGroups(ArrayList<Integer> arr){
		Collections.sort(arr, Collections.reverseOrder());
		for (int i : arr)
			whiteGroups.remove(i);
	}

	public HashMap<Point, BoardFieldOwnership> getBoardFields(){
		return boardFields;
	}
}