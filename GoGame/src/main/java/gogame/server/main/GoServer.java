package gogame.server.main;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import gogame.client.main.GoPlayer;
import gogame.game.engine.*;

public class GoServer {

    private static final int PORT = 8080;

    private static HashMap<String, PrintWriter> players = new HashMap<String, PrintWriter>();
    private static ArrayList<String> temporarilyInaccessible = new ArrayList<String>();


    public static void main(String[] args) throws Exception {
        System.out.println("The server is running.");
        ServerSocket listener = new ServerSocket(PORT);
        try {
            while (true) {
                new Handler(listener.accept()).start();
            }
        } finally {
            listener.close();
        }
    }

    private static class Handler extends Thread {
        private String name;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private GoPlayer player, opponentPlayer;
        private GameEngine game;
        private TerritoryBoard territoryMode;
        private BoardFieldOwnership playerColor;
        private BoardFieldOwnership opponentColor;
        private String oponentName;


        public Handler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {

                // Create character streams for the socket.
                in = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                while (true) {
                    out.println("SUBMITNAME");
                    name = in.readLine();
                    if (name == null) {
                        return;
                    }
                    synchronized (players) {
                        if (!players.keySet().contains(name)) {
                            players.put(name, out);
                            break;
                        }
                    }
                }

                for (PrintWriter writer : players.values()) {
                    writer.println("UPDATE_NAMES " + players.keySet().toString().replace("[", "").replace("]", ""));
                }

                while (true) {
                    ArrayList<String> inputArguments;
                    String input = in.readLine();
                    switch(getFirstWordOfString(input)) {
                        case "CHALLANGE":
                            inputArguments = new ArrayList<String>(Arrays.asList(input.substring(10).split("\\s* \\s*")));
                            String playerSendChallange = inputArguments.get(0);
                            String playerGetChallange = inputArguments.get(1);
                            if(temporarilyInaccessible.contains(playerSendChallange) || temporarilyInaccessible.contains(playerGetChallange )){
                                sendMessageToPlayer("INACCESSIBLE",  playerGetChallange, playerSendChallange);
                            }
                            else{
                                sendMessageToPlayer("CHALLANGE",playerSendChallange, playerGetChallange );
                            }
                            break;

                        case "CHALLANGE_ACCEPTED":
                            inputArguments = new ArrayList<String>(Arrays.asList(input.substring(19).split("\\s* \\s*")));
                            sendMessageToPlayer("CHALLANGE_ACCEPTED", inputArguments.get(1), inputArguments.get(0));
                            player = new GoPlayer(BoardFieldOwnership.WHITE, GameEngineStatus.GAME);
                            opponentPlayer = new GoPlayer(BoardFieldOwnership.BLACK, GameEngineStatus.GAME);
                            game = new GameEngine(player, opponentPlayer);

                            playerColor = BoardFieldOwnership.WHITE;
                            opponentColor = BoardFieldOwnership.BLACK;
                            oponentName = inputArguments.get(0);

                            temporarilyInaccessible.add(inputArguments.get(1));
                            temporarilyInaccessible.add(inputArguments.get(0));
                            out.println("PLAYER_COLOR " + BoardFieldOwnership.WHITE);
                            break;
                        case "YOUR_CHALLANGE_ACCEPTED":
                            inputArguments = new ArrayList<String>(Arrays.asList(input.substring(24).split("\\s* \\s*")));
                            player = new GoPlayer(BoardFieldOwnership.BLACK, GameEngineStatus.GAME);
                            opponentPlayer = new GoPlayer(BoardFieldOwnership.WHITE, GameEngineStatus.GAME);
                            game = new GameEngine(opponentPlayer, player);
                            playerColor = BoardFieldOwnership.BLACK;
                            opponentColor = BoardFieldOwnership.WHITE;
                            oponentName = inputArguments.get(0);

                            out.println("PLAYER_COLOR " + BoardFieldOwnership.BLACK);

                            break;
                        case "CHALLANGE_REJECTED":
                            inputArguments = new ArrayList<String>(Arrays.asList(input.substring(19).split("\\s* \\s*")));
                            sendMessageToPlayer("CHALLANGE_REJECTED", inputArguments.get(1), inputArguments.get(0));
                            break;
                        case "SHOW_RESULT":
                            String sasl;
                            if(playerColor == BoardFieldOwnership.WHITE){
                                territoryMode.getFinishBoardFields(BoardFieldOwnership.BLACK);
                                sasl = "SHOW_RESULT " + game.getWinnerMessage(territoryMode.getWhiteTerritoryPoints(), territoryMode.getBlackTerritoryPoints(), name, oponentName);
                                out.println(sasl);
                            }
                            else{
                                territoryMode.getFinishBoardFields(BoardFieldOwnership.WHITE);
                                sasl = "SHOW_RESULT " + game.getWinnerMessage(territoryMode.getWhiteTerritoryPoints(), territoryMode.getBlackTerritoryPoints(), oponentName, name);
                                out.println(sasl);
                            }
                            players.get(oponentName).println(sasl);
                            break;
                        case "HANDLE_OPPONENT_RESIGN":
                            game = null;
                            inputArguments = new ArrayList<String>(Arrays.asList(input.substring(23).split("\\s* \\s*")));
                            temporarilyInaccessible.remove(inputArguments.get(0));
                            temporarilyInaccessible.remove(inputArguments.get(1));
                            sendMessageToPlayer("OPPONENT_RESIGN", "Player", getFirstWordOfString(input.substring(23)));
                            break;
                        case "PASS":
                            if(game.passTurn(player)){
                                players.get(oponentName).println("OPPONENT_PASS OPPONENT_PASS");
                            }
                            else{
                                out.println("PASS_NOT_YOUR_MOVE PASS_NOT_YOUR_MOVE");
                            }
                            break;
                        case "OPPONENT_PASS_CHANGE_MOVE":
                            game.passTurn(opponentPlayer);
                            break;
                        case "INIT_TERRITORY_MODE":
                            if(game.passTurn(player)){
                                game.restoreGameBoard();
                                territoryMode = new TerritoryBoard(game.getBoardFields(), playerColor);
                                players.get(oponentName).println("OPPONENT_INIT_TERRITORY_MODE OPPONENT_INIT_TERRITORY_MODE");
                            }
                            break;
                        case "RESUME_GAME":
                            game.restoreGameBoard();
                            game.resumeGame(player);
                            players.get(oponentName).println("RESUME_GAME RESUME_GAME");
                            break;
                        case "OPPONENT_RESUME_GAME":
                            game.restoreGameBoard();
                            game.resumeGame(player);
                            break;
                        case "OPPONENT_INIT_TERRITORY_MODE":
                            game.passTurn(opponentPlayer);
                            game.restoreGameBoard();
                            territoryMode = new TerritoryBoard(game.getBoardFields(), opponentColor);
                            break;
                        case "SUGGEST_TERRITORY":
                            players.get(oponentName).println("OPPONENT_SUGGEST_TERRITORY OPPONENT_SUGGEST_TERRITORY");
                            break;
                        case "MOVE":
                            inputArguments = new ArrayList<String>(Arrays.asList(input.substring(5).split("\\s* \\s*")));
                            int x = Integer.parseInt(inputArguments.get(0));
                            int y = Integer.parseInt(inputArguments.get(1));
                            try{
                                game.makeMove(x, y, player);
                                String s = "MOVE_OK " + x + " " + y;
                                out.println(s);
                                players.get(oponentName).println("OPPONENT_MOVE " + x + " " + y);
                            }
                            catch(Exception e){
                                out.println("MOVE_NOT_OK move");
                            }
                            break;
                        case "TERRITORY_FIELD":
                            inputArguments = new ArrayList<String>(Arrays.asList(input.substring(16).split("\\s* \\s*")));
                            int x2 = Integer.parseInt(inputArguments.get(0));
                            int y2 = Integer.parseInt(inputArguments.get(1));
                            if(territoryMode.chooseTerritory(new Point(x2, y2), playerColor)){
                                out.println("TERRITORY_CHOOSE_OK " + x2 + " " + y2);
                                players.get(oponentName).println("OPPONENT_TERRITORY_CHOOSE_OK " + x2 + " " + y2);
                            }
                            else{
                                out.println("TERRITORY_CHOOSE_NOT_OK TERRITORY_CHOOSE_NOT_OK");
                            }
                            break;
                        case "OPPONENT_MOVE":
                            inputArguments = new ArrayList<String>(Arrays.asList(input.substring(14).split("\\s* \\s*")));
                            int x1 = Integer.parseInt(inputArguments.get(0));
                            int y1 = Integer.parseInt(inputArguments.get(1));
                            try{
                                game.makeMove(x1, y1, opponentPlayer);
                                String s = "MOVE_OK1 MOVE_OK1";
                                out.println(s);
                            }catch (Exception e){
                                System.out.println("Exception OPPONENT MOVE");
                            }
                            break;
                        case "OPPONENT_TERRITORY_CHOOSE":
                            inputArguments = new ArrayList<String>(Arrays.asList(input.substring(26).split("\\s* \\s*")));
                            int x22 = Integer.parseInt(inputArguments.get(0));
                            int y22 = Integer.parseInt(inputArguments.get(1));

                            if(territoryMode.chooseTerritory(new Point(x22, y22), opponentColor)){
                                out.println("OPPONENT_TERRITORY_CHOOSE_OK1 " + x22 + " " + y22);
                            }
                            else{
                                out.println("TERRITORY_CHOOSE_NOT_OK TERRITORY_CHOOSE_NOT_OK");
                            }
                            break;
                    }
                    if (input == null) {
                        return;
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            } finally {
                if(name != null || out != null){
                    players.remove(name);
                }
                for (PrintWriter writer : players.values()) {
                    writer.println("REMOVE_NAME " +name);
                }
                try {

                    socket.close();
                } catch (IOException e) {
                }
            }
        }
        private String getFirstWordOfString(String str){
            return str.split("\\s+").length == 1 ?  str : str.substring(0, str.indexOf(' '));
        }

        private void sendMessageToPlayer(String messageType, String fromPlayer, String toPlayer){
            for (String player : players.keySet()) {
                if(player.equals(toPlayer)){
                    players.get(player).println(messageType + " " + fromPlayer);
                }
            }
        }

    }
}