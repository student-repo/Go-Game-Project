package gogame.game.engine;

import java.awt.*;
import java.util.HashMap;

public class TerritoryBoard {
    private HashMap<Point, BoardFieldOwnership> boardFields;

    public TerritoryBoard(HashMap<Point, BoardFieldOwnership> boardFields){
        this.boardFields = boardFields;
    }

    public boolean chooseTerritory(Point p, BoardFieldOwnership colorTerritory){
        BoardFieldOwnership territory = colorTerritory == BoardFieldOwnership.BLACK ? BoardFieldOwnership.BLACK_TERRITORY : BoardFieldOwnership.WHITE_TERRITORY;
        if(boardFields.get(p) == BoardFieldOwnership.FREE){
            boardFields.put(p, territory);
            return true;
        }
        else{
            return false;
        }
    }

    public HashMap<Point, BoardFieldOwnership> getBoardFields(){
        return boardFields;
    }

}
