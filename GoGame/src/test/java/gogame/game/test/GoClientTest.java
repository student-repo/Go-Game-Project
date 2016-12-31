package gogame.game.test;

import gogame.client.main.GoClient;
import gogame.game.engine.BoardFieldOwnership;
import org.junit.Test;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GoClientTest {
    GoClient goClient = null;

    @Test
    public void testShowResult() throws IOException {
        ServerSocket testServerSocket = new ServerSocket(8081);
        testServerSocket.setSoTimeout(100);
        goClient = new GoClient(8081);
        goClient.connect();
        Socket testSocket = testServerSocket.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(testSocket.getInputStream()));
        goClient.showResult();
        assertEquals("SHOW_RESULT", in.readLine());

    }

    @Test
    public void testPass() throws IOException, URISyntaxException {
        ServerSocket testServerSocket = new ServerSocket(8082);
        testServerSocket.setSoTimeout(100);
        goClient = new GoClient(8082);
        goClient.connect();
        Socket testSocket = testServerSocket.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(testSocket.getInputStream()));
        goClient.sendPass();
        assertEquals("PASS", in.readLine());

    }

    @Test
    public void testSendTerritoryField() throws IOException, URISyntaxException {
        ServerSocket testServerSocket = new ServerSocket(8083);
        testServerSocket.setSoTimeout(100);
        goClient = new GoClient(8083);
        goClient.connect();
        Socket testSocket = testServerSocket.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(testSocket.getInputStream()));
        goClient.sendTerritoryField(new Point(1, 2));
        assertEquals("TERRITORY_FIELD 1 2", in.readLine());

    }

    @Test
    public void testSendMove() throws IOException {
        ServerSocket testServerSocket = new ServerSocket(8084);
        testServerSocket.setSoTimeout(100);
        goClient = new GoClient(8084);
        goClient.connect();
        Socket testSocket = testServerSocket.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(testSocket.getInputStream()));
        goClient.sendMove(new Point(2, 3));
        assertEquals("MOVE 2 3", in.readLine());

    }

    @Test
    public void testHandleResignGame() throws IOException, URISyntaxException {
        ServerSocket testServerSocket = new ServerSocket(8085);
        testServerSocket.setSoTimeout(100);
        goClient = new GoClient(8085);
        goClient.connect();
        goClient.initGameBoard();
        goClient.initBoardFrame();
        goClient.setPlayerName("Player");
        goClient.setOpponentName("Opponent");
        Socket testSocket = testServerSocket.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(testSocket.getInputStream()));
        goClient.handleResignGame();
        assertEquals("HANDLE_OPPONENT_RESIGN Opponent Player", in.readLine());

    }

    @Test
    public void testResumeGame() throws IOException, URISyntaxException {
        ServerSocket testServerSocket = new ServerSocket(8086);
        testServerSocket.setSoTimeout(100);
        goClient = new GoClient(8086);
        goClient.connect();
        goClient.initGameBoard();
        goClient.initBoardFrame();
        Socket testSocket = testServerSocket.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(testSocket.getInputStream()));
        goClient.resumeGame();
        assertEquals("RESUME_GAME", in.readLine());

    }

    @Test
    public void testSuggestTerritory() throws IOException, URISyntaxException {
        ServerSocket testServerSocket = new ServerSocket(8087);
        testServerSocket.setSoTimeout(100);
        goClient = new GoClient(8087);
        goClient.connect();
        goClient.initGameBoard();
        goClient.initBoardFrame();
        goClient.initiateTerritoryMide();
        goClient.setPlayerColor(BoardFieldOwnership.BLACK);
        Socket testSocket = testServerSocket.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(testSocket.getInputStream()));
        goClient.suggestTerritory();
        assertEquals("SUGGEST_TERRITORY BLACK", in.readLine());

    }

    @Test
    public void testShowResultSingleplayerMode() throws IOException {
        ServerSocket testServerSocket = new ServerSocket(8088);
        testServerSocket.setSoTimeout(100);
        goClient = new GoClient(8088);
        goClient.connect();
        goClient.setSingleplayerMode(true);
        Socket testSocket = testServerSocket.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(testSocket.getInputStream()));
        goClient.showResult();
        assertEquals("SINGLEPLAYER_SHOW_RESULT", in.readLine());

    }

    @Test
    public void testPassSingleplayerMode() throws IOException, URISyntaxException {
        ServerSocket testServerSocket = new ServerSocket(8089);
        testServerSocket.setSoTimeout(100);
        goClient = new GoClient(8089);
        goClient.connect();
        goClient.setSingleplayerMode(true);
        goClient.initGameBoard();
        goClient.initBoardFrame();
        Socket testSocket = testServerSocket.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(testSocket.getInputStream()));
        goClient.sendPass();
        assertEquals("SINGLEPLAYER_PASS", in.readLine());

    }

    @Test
    public void testSendTerritoryFieldSingleplayerMode() throws IOException, URISyntaxException {
        ServerSocket testServerSocket = new ServerSocket(8090);
        testServerSocket.setSoTimeout(100);
        goClient = new GoClient(8090);
        goClient.connect();
        goClient.setSingleplayerMode(true);
        Socket testSocket = testServerSocket.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(testSocket.getInputStream()));
        goClient.sendTerritoryField(new Point(1, 2));
        assertEquals("SINGLEPLAYER_TERRITORY_FIELD 1 2", in.readLine());

    }

    @Test
    public void testSendMoveSingleplayerMode() throws IOException {
        ServerSocket testServerSocket = new ServerSocket(8091);
        testServerSocket.setSoTimeout(100);
        goClient = new GoClient(8091);
        goClient.connect();
        goClient.setSingleplayerMode(true);
        Socket testSocket = testServerSocket.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(testSocket.getInputStream()));
        goClient.sendMove(new Point(2, 3));
        assertEquals("SINGLEPLAYER_MOVE 2 3", in.readLine());

    }
    @Test
    public void testHandleResignGameSingleplayerMode() throws IOException, URISyntaxException {
        ServerSocket testServerSocket = new ServerSocket(8092);
        testServerSocket.setSoTimeout(100);
        goClient = new GoClient(8092);
        goClient.connect();
        goClient.setSingleplayerMode(true);
        goClient.initGameBoard();
        goClient.initBoardFrame();
        goClient.setPlayerName("Player");
        goClient.setOpponentName("Opponent");
        Socket testSocket = testServerSocket.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(testSocket.getInputStream()));
        goClient.handleResignGame();
        assertEquals("HANDLE_OPPONENT_RESIGN Opponent Player", in.readLine());

    }

    @Test
    public void testResumeGameSingleplayerMode() throws IOException, URISyntaxException {
        ServerSocket testServerSocket = new ServerSocket(8093);
        testServerSocket.setSoTimeout(100);
        goClient = new GoClient(8093);
        goClient.connect();
        goClient.setSingleplayerMode(true);
        goClient.initGameBoard();
        goClient.initBoardFrame();
        Socket testSocket = testServerSocket.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(testSocket.getInputStream()));
        goClient.resumeGame();
        assertEquals("SINGLEPLAYER_RESUME_GAME", in.readLine());

    }

    @Test
    public void testSuggestTerritorySingleplaterMode() throws IOException, URISyntaxException {
        ServerSocket testServerSocket = new ServerSocket(8094);
        testServerSocket.setSoTimeout(100);
        goClient = new GoClient(8094);
        goClient.connect();
        goClient.setSingleplayerMode(true);
        goClient.initGameBoard();
        goClient.initBoardFrame();
        goClient.initiateTerritoryMide();
        goClient.setPlayerColor(BoardFieldOwnership.BLACK);
        Socket testSocket = testServerSocket.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(testSocket.getInputStream()));
        goClient.suggestTerritory();
        assertEquals("SINGLEPLAYER_SUGGEST_TERRITORY", in.readLine());

    }


}
