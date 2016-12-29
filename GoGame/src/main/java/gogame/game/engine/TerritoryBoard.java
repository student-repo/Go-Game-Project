package gogame.game.engine;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class TerritoryBoard {
    private HashMap<Point, BoardFieldOwnership> boardFields;
    private BoardFieldOwnership whoChooseTerritory;

    public TerritoryBoard(HashMap<Point, BoardFieldOwnership> boardFields, BoardFieldOwnership whoChooseTerritory){
        this.boardFields = boardFields;
        this.whoChooseTerritory = whoChooseTerritory;
    }

    public boolean chooseTerritory(Point p, BoardFieldOwnership colorTerritory){
        BoardFieldOwnership territory = colorTerritory == BoardFieldOwnership.BLACK ? BoardFieldOwnership.BLACK_TERRITORY : BoardFieldOwnership.WHITE_TERRITORY;
        if(whoChooseTerritory != colorTerritory){
            return false;
        }
        else if(boardFields.get(p) == BoardFieldOwnership.FREE){
            boardFields.put(p, territory);
            return true;
        }
        else if(boardFields.get(p) == BoardFieldOwnership.BLACK){
            HashSet<Point> blackGroupPoints = getGroup((int)p.getX(), (int)p.getY(), new HashSet<Point>());
            blackGroupPoints.add(p);
            for(Point c: blackGroupPoints){
                boardFields.put(c, BoardFieldOwnership.BLACK_PIECE_NOT_ALIVE);
            }
            return true;
        }
        else if(boardFields.get(p) == BoardFieldOwnership.WHITE){
            HashSet<Point> whiteGroupPoints = getGroup((int)p.getX(), (int)p.getY(), new HashSet<Point>());
            whiteGroupPoints.add(p);
            for(Point c: whiteGroupPoints){
                boardFields.put(c, BoardFieldOwnership.WHITE_PIECE_NOT_ALIVE);
            }
            return true;
        }
        else{
            return false;
        }
    }

    private HashSet<Point> getGroup(int x, int y, HashSet<Point> group){
        if(pointHasNeighborSameColor(new Point(x, y), new Point(x - 1, y), group)){
            group.add(new Point(x - 1, y));
            getGroup(x - 1, y, group);
        }
        if(pointHasNeighborSameColor(new Point(x, y), new Point(x , y - 1), group)){
            group.add(new Point(x, y - 1));
            getGroup(x , y - 1, group);
        }
        if(pointHasNeighborSameColor(new Point(x, y), new Point(x , y + 1), group)){
            group.add(new Point(x, y + 1));
            getGroup(x , y + 1, group);
        }
        if(pointHasNeighborSameColor(new Point(x, y), new Point(x + 1, y), group)){
            group.add(new Point(x + 1, y));
            getGroup(x + 1, y, group);
        }
        return group;
    }

    private Boolean pointHasNeighborSameColor(Point point, Point neighbor, HashSet<Point> group){
        if(boardFields.keySet().contains(neighbor) &&
                boardFields.get(point) == boardFields.get(neighbor) && !group.contains(neighbor)){
            return true;
        }
        return false;
    }

    public HashMap<Point, BoardFieldOwnership> getBoardFields(){
        return boardFields;
    }

    public HashMap<Point, BoardFieldOwnership> getFinishBoardFields(BoardFieldOwnership b){
        BoardFieldOwnership a;
        if(b == BoardFieldOwnership.BLACK){
            a = BoardFieldOwnership.WHITE_TERRITORY;
        }
        else{
            a = BoardFieldOwnership.BLACK_TERRITORY;
        }
        for(Point p: boardFields.keySet()){
            if(boardFields.get(p) == BoardFieldOwnership.FREE){
                boardFields.put(p, a);
            }
        }
        return boardFields;
    }

}
