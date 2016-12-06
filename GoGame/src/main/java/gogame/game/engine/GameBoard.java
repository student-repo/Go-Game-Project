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
		if (player == BoardFieldOwnership.BLACK) {
            placeConcreteStone(blackGroups, whiteGroups, point, "blackMove");
		}
		else {
            placeConcreteStone(whiteGroups, blackGroups, point, "whiteMove");
		}
		return true;
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

	private Boolean setContainsNearbyPoint(HashSet<Point> set, Point point){

       return set.contains(new Point(point.x - 1, point.y)) || set.contains(new Point(point.x, point.y - 1)) ||
               set.contains(new Point(point.x + 1, point.y)) || set.contains(new Point(point.x, point.y + 1));
    }

    private void mergeGroups(ArrayList<Integer> groupsToMergeIndexes, ArrayList<FieldGroup> groups, String moveColor, Point point){
        FieldGroup newGroup = new FieldGroup(this);
        newGroup.addToGroup(point);
        for(Integer i: groupsToMergeIndexes){
            newGroup.fieldsInGroup.addAll(groups.get(i).fieldsInGroup);
            newGroup.fieldsToKillThisGroup.addAll(groups.get(i).fieldsToKillThisGroup);
        }
        newGroup.fieldsInGroup.add(point);
        newGroup.fieldsToKillThisGroup.remove(point);
        groups.add(newGroup);
        if(moveColor.equals("blackMove")){
            boardFields.put(point, BoardFieldOwnership.BLACK);
            removeAllBlackGroups(groupsToMergeIndexes);
        }
        else{
            boardFields.put(point, BoardFieldOwnership.WHITE);
            removeAllWhiteGroups(groupsToMergeIndexes);
        }
    }

    private void updateGroupAfterKillOpponent(ArrayList<FieldGroup> ownGroup,ArrayList<FieldGroup> foreignGroup, int i){
        for(Point o: foreignGroup.get(i).fieldsInGroup){
            boardFields.put(o,BoardFieldOwnership.FREE);
            for(int k = 0; k < ownGroup.size(); k++){
                if(setContainsNearbyPoint(ownGroup.get(k).fieldsInGroup, o )){
                    ownGroup.get(k).fieldsToKillThisGroup.add(o);
                }
            }
        }
    }

    //this function uses references
private void placeConcreteStone(ArrayList<FieldGroup> ownGroup,ArrayList<FieldGroup> foreignGroup, Point point, String art){

    ArrayList<Integer> nearbyGroupsIndexes = getNearbyGroupsIndexes(point, ownGroup);
    if(art.equals("blackMove")){
        mergeGroups(nearbyGroupsIndexes, ownGroup, "blackMove", point);
    }
    else{
        mergeGroups(nearbyGroupsIndexes, ownGroup, "whiteMove", point);
    }

            ArrayList<Integer> toRemoveGroupsIndexes = new ArrayList<>();
            for(int i = 0; i < foreignGroup.size(); i++){
                if(foreignGroup.get(i).fieldsToKillThisGroup.contains(point)){
                    foreignGroup.get(i).fieldsToKillThisGroup.remove(point);
                }
                if(foreignGroup.get(i).fieldsToKillThisGroup.size() == 0){
                    toRemoveGroupsIndexes.add(i);
                    updateGroupAfterKillOpponent(ownGroup, foreignGroup, i);
                }
            }
            if(art.equals("blackMove")){
                removeAllWhiteGroups(toRemoveGroupsIndexes);
            }
            else{
                removeAllBlackGroups(toRemoveGroupsIndexes);
            }
    }
}