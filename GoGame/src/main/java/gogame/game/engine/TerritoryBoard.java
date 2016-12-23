package gogame.game.engine;

import java.awt.*;
import java.util.HashMap;

public class TerritoryBoard {
    private HashMap<Point, BoardFieldOwnership> boardFields;
    private boolean whiteMove;

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

    public HashMap<Point, BoardFieldOwnership> getFinishBoardFields(String b){
        BoardFieldOwnership a;
        if(b.equals("BLACK")){
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

    public void setMove(String s){
        if(s.equals("WHITE")){
            whiteMove = true;
        }
        else{
            whiteMove = false;
        }
    }

    public boolean whiteMove(){
        return whiteMove;
    }

}
