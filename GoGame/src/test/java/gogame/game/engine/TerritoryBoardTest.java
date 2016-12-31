package gogame.game.engine;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.HashMap;

import static org.junit.Assert.*;


public class TerritoryBoardTest {

    private TerritoryBoard testTerritoryBoard;
    private GameBoard testGameBoard;
    private BoardFieldOwnership
            black = BoardFieldOwnership.BLACK,
            white = BoardFieldOwnership.WHITE,
            free = BoardFieldOwnership.FREE;

    @Before
    public void setUp() throws Exception {
        testGameBoard = new GameBoard();
        this.testTerritoryBoard = new TerritoryBoard(testGameBoard.getBoardFields(), black);
    }

    @Test
    public void testWrongPlayerChoose() {
        assertFalse(testTerritoryBoard.placeTerritory(new Point(3, 4), BoardFieldOwnership.WHITE));
    }

    @Test
    public void testChooseOneTerritoryField() {
        assertTrue(testTerritoryBoard.placeTerritory(new Point(3, 4), BoardFieldOwnership.BLACK));
        assertFalse(testTerritoryBoard.placeTerritory(new Point(3, 4), BoardFieldOwnership.BLACK));
        assertTrue(testTerritoryBoard.getBoardFields().get(new Point(3, 4)) == BoardFieldOwnership.BLACK_TERRITORY);
    }

    @Test
    public void testChooseSmallGroupTerritoryField() {
        testTerritoryBoard.getBoardFields().put(new Point(10,3), BoardFieldOwnership.WHITE);
        testTerritoryBoard.getBoardFields().put(new Point(10,4), BoardFieldOwnership.WHITE);



        assertTrue(testTerritoryBoard.placeTerritory(new Point(10, 3), BoardFieldOwnership.BLACK));

        assertTrue(testTerritoryBoard.getBoardFields().get(new Point(10, 3)) == BoardFieldOwnership.WHITE_PIECE_NOT_ALIVE);
        assertTrue(testTerritoryBoard.getBoardFields().get(new Point(10, 4)) == BoardFieldOwnership.WHITE_PIECE_NOT_ALIVE);
    }


    @Test
    public void testChooseBigGroupTerritoryField() {
        testTerritoryBoard.getBoardFields().put(new Point(3,5), BoardFieldOwnership.BLACK);
        testTerritoryBoard.getBoardFields().put(new Point(2,4), BoardFieldOwnership.BLACK);
        testTerritoryBoard.getBoardFields().put(new Point(3,4), BoardFieldOwnership.BLACK);
        testTerritoryBoard.getBoardFields().put(new Point(3,3), BoardFieldOwnership.BLACK);
        testTerritoryBoard.getBoardFields().put(new Point(3,6), BoardFieldOwnership.BLACK);
        testTerritoryBoard.getBoardFields().put(new Point(3,7), BoardFieldOwnership.BLACK);
        testTerritoryBoard.getBoardFields().put(new Point(4,7), BoardFieldOwnership.BLACK);
        testTerritoryBoard.getBoardFields().put(new Point(2,7), BoardFieldOwnership.BLACK);
        testTerritoryBoard.getBoardFields().put(new Point(2,3), BoardFieldOwnership.WHITE);

        testTerritoryBoard.getBoardFields().put(new Point(1,17), BoardFieldOwnership.BLACK);
        testTerritoryBoard.getBoardFields().put(new Point(2,13), BoardFieldOwnership.BLACK);

        testTerritoryBoard.getBoardFields().put(new Point(10,3), BoardFieldOwnership.WHITE);
        testTerritoryBoard.getBoardFields().put(new Point(10,4), BoardFieldOwnership.WHITE);
        testTerritoryBoard.getBoardFields().put(new Point(10,5), BoardFieldOwnership.WHITE);
        testTerritoryBoard.getBoardFields().put(new Point(10,6), BoardFieldOwnership.WHITE);
        testTerritoryBoard.getBoardFields().put(new Point(10,7), BoardFieldOwnership.WHITE);

        testTerritoryBoard.getBoardFields().put(new Point(16,11), BoardFieldOwnership.WHITE);
        testTerritoryBoard.getBoardFields().put(new Point(10,12), BoardFieldOwnership.WHITE);
        testTerritoryBoard.getBoardFields().put(new Point(10,15), BoardFieldOwnership.WHITE);

        assertTrue(testTerritoryBoard.placeTerritory(new Point(3, 3), BoardFieldOwnership.BLACK));

        assertTrue(testTerritoryBoard.getBoardFields().get(new Point(3, 5)) == BoardFieldOwnership.BLACK_PIECE_NOT_ALIVE);
        assertTrue(testTerritoryBoard.getBoardFields().get(new Point(2, 4)) == BoardFieldOwnership.BLACK_PIECE_NOT_ALIVE);
        assertTrue(testTerritoryBoard.getBoardFields().get(new Point(3, 4)) == BoardFieldOwnership.BLACK_PIECE_NOT_ALIVE);
        assertTrue(testTerritoryBoard.getBoardFields().get(new Point(3, 3)) == BoardFieldOwnership.BLACK_PIECE_NOT_ALIVE);
        assertTrue(testTerritoryBoard.getBoardFields().get(new Point(3, 6)) == BoardFieldOwnership.BLACK_PIECE_NOT_ALIVE);
        assertTrue(testTerritoryBoard.getBoardFields().get(new Point(3, 7)) == BoardFieldOwnership.BLACK_PIECE_NOT_ALIVE);
        assertTrue(testTerritoryBoard.getBoardFields().get(new Point(4, 7)) == BoardFieldOwnership.BLACK_PIECE_NOT_ALIVE);
        assertTrue(testTerritoryBoard.getBoardFields().get(new Point(2, 7)) == BoardFieldOwnership.BLACK_PIECE_NOT_ALIVE);
    }

    @Test
    public void testFinishBoardFields() {
        testTerritoryBoard.getBoardFields().put(new Point(10,3), BoardFieldOwnership.WHITE_PIECE_NOT_ALIVE);
        testTerritoryBoard.getBoardFields().put(new Point(10,4), BoardFieldOwnership.WHITE_PIECE_NOT_ALIVE);

        testTerritoryBoard.getBoardFields().put(new Point(11,13), BoardFieldOwnership.BLACK_PIECE_NOT_ALIVE);
        testTerritoryBoard.getBoardFields().put(new Point(4,1), BoardFieldOwnership.BLACK_PIECE_NOT_ALIVE);

        testTerritoryBoard.getBoardFields().put(new Point(16,11), BoardFieldOwnership.WHITE);
        testTerritoryBoard.getBoardFields().put(new Point(10,12), BoardFieldOwnership.WHITE);

        testTerritoryBoard.getBoardFields().put(new Point(1,17), BoardFieldOwnership.BLACK);
        testTerritoryBoard.getBoardFields().put(new Point(2,13), BoardFieldOwnership.BLACK);

        testTerritoryBoard.getBoardFields().put(new Point(2,15), BoardFieldOwnership.BLACK_TERRITORY);
        testTerritoryBoard.getBoardFields().put(new Point(5,13), BoardFieldOwnership.BLACK_TERRITORY);
        testTerritoryBoard.getBoardFields().put(new Point(6,13), BoardFieldOwnership.BLACK_TERRITORY);

        HashMap<Point, BoardFieldOwnership> finishBoard = testTerritoryBoard.getFinishBoardFields(BoardFieldOwnership.BLACK);


        for(Point p: finishBoard.keySet()){
            if(!p.equals(new Point(10,3)) && !p.equals(new Point(10,4)) && !p.equals(new Point(11,13)) && !p.equals(new Point(4,1)) &&
                    !p.equals(new Point(16, 11)) && !p.equals(new Point(10,12)) && !p.equals(new Point(1,17)) && !p.equals(new Point(2,13))
                    && !p.equals(new Point(2,15)) && !p.equals(new Point(5,13)) && !p.equals(new Point(6,13))){
                assertTrue(finishBoard.get(p) == BoardFieldOwnership.WHITE_TERRITORY);
            }
        }

        assertTrue(testTerritoryBoard.getBoardFields().get(new Point(10, 3)) == BoardFieldOwnership.WHITE_PIECE_NOT_ALIVE);
        assertTrue(testTerritoryBoard.getBoardFields().get(new Point(10, 4)) == BoardFieldOwnership.WHITE_PIECE_NOT_ALIVE);

        assertTrue(testTerritoryBoard.getBoardFields().get(new Point(11, 13)) == BoardFieldOwnership.BLACK_PIECE_NOT_ALIVE);
        assertTrue(testTerritoryBoard.getBoardFields().get(new Point(4, 1)) == BoardFieldOwnership.BLACK_PIECE_NOT_ALIVE);

        assertTrue(testTerritoryBoard.getBoardFields().get(new Point(16,11)) == BoardFieldOwnership.WHITE);
        assertTrue(testTerritoryBoard.getBoardFields().get(new Point(10,12)) == BoardFieldOwnership.WHITE);

        assertTrue(testTerritoryBoard.getBoardFields().get(new Point(1, 17)) == BoardFieldOwnership.BLACK);
        assertTrue(testTerritoryBoard.getBoardFields().get(new Point(2, 13)) == BoardFieldOwnership.BLACK);

        assertTrue(testTerritoryBoard.getBoardFields().get(new Point(2, 15)) == BoardFieldOwnership.BLACK_TERRITORY);
        assertTrue(testTerritoryBoard.getBoardFields().get(new Point(5, 13)) == BoardFieldOwnership.BLACK_TERRITORY);
        assertTrue(testTerritoryBoard.getBoardFields().get(new Point(6, 13)) == BoardFieldOwnership.BLACK_TERRITORY);
    }



}