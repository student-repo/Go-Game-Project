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

	/**
	 * 
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
	
	/*public static void main(String[] args){
		GameBoard g = new GameBoard();

	}*/

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
			/*if(!noSuicide(blackGroups, point) && !testingKoRule(point)){
				return false;
			}
            placeConcreteStone(blackGroups, whiteGroups, point, "blackMove");*/
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
					//gr.updateBreaths(point);
						
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
			/*if(!noSuicide(whiteGroups, point) && !testingKoRule(point)){
				return false;
			}
            placeConcreteStone(whiteGroups, blackGroups, point, "whiteMove");*/
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
					//gr.updateBreaths(point);
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
	 * 
	 * @param point
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

	public int getCapturedWhiteStones() {
		return capturedWhiteStones;
	}

	public int getCapturedBlackStones() {
		return capturedBlackStones;
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

	/**
	 * Checks if move will not kill friendly group
	 * @param group An ArrayList of groups to be tested
	 * @param point Point where stone will be placed
	 * @return 
	 */
    private boolean noSuicide(ArrayList<FieldGroup> group, Point point){
		for(FieldGroup f: group){
			if(f.fieldsToKillThisGroup.contains(point)
					&& f.fieldsToKillThisGroup.size() == 1){
				return false;
			}
		}
		return  true;
	}
    
    /**
     * Testing if Ko rule was validated
     * @param testedPoint Point to be tested
     * @return True if rule was validated, false otherwise
     */
    private boolean testingKoRule(Point testedPoint) {
    	return false;
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
