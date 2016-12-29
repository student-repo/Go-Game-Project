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
            HashSet<Point> aaa = getGroup((int)p.getX(), (int)p.getY(), new HashSet<Point>());
                    aaa.add(p);
            for(Point c: aaa){
                boardFields.put(c, BoardFieldOwnership.BLACK_PIECE_NOT_ALIVE);
            }
            return true;
        }
        else if(boardFields.get(p) == BoardFieldOwnership.WHITE){
            HashSet<Point> bbb = getGroup((int)p.getX(), (int)p.getY(), new HashSet<Point>());
            bbb.add(p);
            for(Point c: bbb){
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

    public static void main(String[] args){

        int boardSize = 19;
        HashMap<Point, BoardFieldOwnership> boardFields = new HashMap<Point, BoardFieldOwnership>();

        for (int i=0; i<boardSize; i++) {
            for (int j=0; j<boardSize; j++) {
                boardFields.put(new Point(i, j), BoardFieldOwnership.FREE);
            }
        }


        boardFields.put(new Point(3,5), BoardFieldOwnership.BLACK);
        boardFields.put(new Point(2,4), BoardFieldOwnership.BLACK);
        boardFields.put(new Point(3,4), BoardFieldOwnership.BLACK);
        boardFields.put(new Point(3,3), BoardFieldOwnership.BLACK);
        boardFields.put(new Point(3,6), BoardFieldOwnership.BLACK);
        boardFields.put(new Point(3,7), BoardFieldOwnership.BLACK);
        boardFields.put(new Point(4,7), BoardFieldOwnership.BLACK);
        boardFields.put(new Point(2,7), BoardFieldOwnership.BLACK);
        boardFields.put(new Point(2,3), BoardFieldOwnership.WHITE);

        boardFields.put(new Point(1,17), BoardFieldOwnership.BLACK);
        boardFields.put(new Point(2,13), BoardFieldOwnership.BLACK);

        boardFields.put(new Point(10,3), BoardFieldOwnership.WHITE);
        boardFields.put(new Point(10,4), BoardFieldOwnership.WHITE);
        boardFields.put(new Point(10,5), BoardFieldOwnership.WHITE);
        boardFields.put(new Point(10,6), BoardFieldOwnership.WHITE);
        boardFields.put(new Point(10,7), BoardFieldOwnership.WHITE);

        boardFields.put(new Point(16,11), BoardFieldOwnership.WHITE);
        boardFields.put(new Point(10,12), BoardFieldOwnership.WHITE);
        boardFields.put(new Point(10,15), BoardFieldOwnership.WHITE);

        TerritoryBoard t = new TerritoryBoard(boardFields,BoardFieldOwnership.BLACK);

        t.chooseTerritory(new Point(2,3), BoardFieldOwnership.BLACK);

        for(Point p: boardFields.keySet()){
            if(boardFields.get(p) != BoardFieldOwnership.FREE){
                System.out.println("key: " + p + " value: " + boardFields.get(p));
            }
        }

    }

}
