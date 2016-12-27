package gogame.game.engine;

import java.awt.*;
import java.util.HashMap;

public class TerritoryBoard {
    private HashMap<Point, BoardFieldOwnership> boardFields;
    private BoardFieldOwnership whoChooseTerritory;

    public TerritoryBoard(HashMap<Point, BoardFieldOwnership> boardFields, BoardFieldOwnership whoChooseTerritory){
        this.boardFields = boardFields;
        this.whoChooseTerritory = whoChooseTerritory;
    }

    public boolean chooseTerritory(Point p, BoardFieldOwnership colorTerritory){
        BoardFieldOwnership territory = colorTerritory == BoardFieldOwnership.BLACK ? BoardFieldOwnership.BLACK_TERRITORY : BoardFieldOwnership.WHITE_TERRITORY;
        if(boardFields.get(p) == BoardFieldOwnership.FREE && whoChooseTerritory == colorTerritory){
            boardFields.put(p, territory);
            return true;
        }
        else{
            return false;
        }
    }

    public int getWhiteTerritoryPoints(){
        int i = 0;
        for(Point p : boardFields.keySet()){
            if(boardFields.get(p) == BoardFieldOwnership.WHITE_TERRITORY){
                i++;
            }
        }
        return i;
    }

    public int getBlackTerritoryPoints(){
        int i = 0;
        for(Point p : boardFields.keySet()){
            if(boardFields.get(p) == BoardFieldOwnership.BLACK_TERRITORY){
                i++;
            }
        }
        return i;
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
