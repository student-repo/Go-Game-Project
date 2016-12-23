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
import java.util.Map;

import gogame.game.engine.BoardFieldOwnership;
import gogame.game.engine.TerritoryBoard;
import gogame.game.engine.GameBoard;

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
        private GameBoard g;
        private TerritoryBoard t;
        private String playerColor;
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
                    String input = in.readLine();
                    switch(getFirstWordOfString(input)) {
                        case "CHALLANGE":
                            ArrayList<String> challangePlayers = new ArrayList<String>(Arrays.asList(input.substring(10).split("\\s* \\s*")));
                            String playerSendChallange = challangePlayers.get(0);
                            String playerGetChallange = challangePlayers.get(1);
                            if(temporarilyInaccessible.contains(playerSendChallange) || temporarilyInaccessible.contains(playerGetChallange )){
                                sendMessageToPlayer("INACCESSIBLE",  playerGetChallange, playerSendChallange);
                            }
                            else{
                                sendMessageToPlayer("CHALLANGE",playerSendChallange, playerGetChallange );
                            }
                            break;

                        case "CHALLANGE_ACCEPTED":
                            ArrayList<String> challangeAcceptedPlayers = new ArrayList<String>(Arrays.asList(input.substring(19).split("\\s* \\s*")));
                            sendMessageToPlayer("CHALLANGE_ACCEPTED", challangeAcceptedPlayers.get(1), challangeAcceptedPlayers.get(0));
                            g = new GameBoard();
                            playerColor = BoardFieldOwnership.WHITE.toString();
                            oponentName = challangeAcceptedPlayers.get(0);

                            temporarilyInaccessible.add(challangeAcceptedPlayers.get(1));
                            temporarilyInaccessible.add(challangeAcceptedPlayers.get(0));
                            out.println("PLAYER_COLOR " + BoardFieldOwnership.WHITE);
                            break;
                        case "YOUR_CHALLANGE_ACCEPTED":
                            ArrayList<String> challangeAcceptedPlayers2 = new ArrayList<String>(Arrays.asList(input.substring(24).split("\\s* \\s*")));
                            g = new GameBoard();
                            playerColor = BoardFieldOwnership.BLACK.toString();
                            oponentName = challangeAcceptedPlayers2.get(0);

                            out.println("PLAYER_COLOR " + BoardFieldOwnership.BLACK);

                            break;
                        case "CHALLANGE_REJECTED":
                            ArrayList<String> challangeRejectedPlayers = new ArrayList<String>(Arrays.asList(input.substring(19).split("\\s* \\s*")));
                            sendMessageToPlayer("CHALLANGE_REJECTED", challangeRejectedPlayers.get(1), challangeRejectedPlayers.get(0));
                            break;
                        case "HANDLE_OPPONENT_RESIGN":
                            g = null;
                            ArrayList<String> resignGamePlayers = new ArrayList<String>(Arrays.asList(input.substring(23).split("\\s* \\s*")));
                            temporarilyInaccessible.remove(resignGamePlayers.get(0));
                            temporarilyInaccessible.remove(resignGamePlayers.get(1));
                            sendMessageToPlayer("OPPONENT_RESIGN", "Player", getFirstWordOfString(input.substring(23)));
                            break;
                        case "PASS":
                            if(playerColor.equals(BoardFieldOwnership.WHITE.toString()) && g.whiteMove()){
                                g.changeMove();
                                players.get(oponentName).println("OPPONENT_PASS OPPONENT_PASS");
                            }
                            else if(playerColor.equals(BoardFieldOwnership.BLACK.toString())&& !g.whiteMove()){
                                g.changeMove();
                                players.get(oponentName).println("OPPONENT_PASS OPPONENT_PASS");
                            }
                            else{
                                out.println("PASS_NOT_YOUR_MOVE PASS_NOT_YOUR_MOVE");
                            }
                            break;
                        case "OPPONENT_PASS_CHANGE_MOVE":
                            g.changeMove();
                            break;
                        case "INIT_TERRITORY_MODE":
                            t = new TerritoryBoard(g.getBoardFields());
                            players.get(oponentName).println("OPPONENT_INIT_TERRITORY_MODE OPPONENT_INIT_TERRITORY_MODE");
                            break;
                        case "OPPONENT_INIT_TERRITORY_MODE":
                            t = new TerritoryBoard(g.getBoardFields());
                            break;
                        case "MOVE":
                            ArrayList<String> moveCoordinate = new ArrayList<String>(Arrays.asList(input.substring(5).split("\\s* \\s*")));
                            int x = Integer.parseInt(moveCoordinate.get(0)) + 1;
                            int y = Integer.parseInt(moveCoordinate.get(1)) + 1;
                            if(playerColor.equals(BoardFieldOwnership.WHITE.toString()) && g.whiteMove() && g.placeStone(new Point(x, y), BoardFieldOwnership.WHITE)){
                                String s = "MOVE_OK " + boardFieldsToString(g.getBoardFields());
                                out.println(s);
                                players.get(oponentName).println("OPPONENT_MOVE " + x + " " + y);
                                g.changeMove();

                            }
                            else if(playerColor.equals(BoardFieldOwnership.BLACK.toString())&& !g.whiteMove() && g.placeStone(new Point(x, y), BoardFieldOwnership.BLACK)){
                                String s = "MOVE_OK " + boardFieldsToString(g.getBoardFields());
                                out.println(s);
                                players.get(oponentName).println("OPPONENT_MOVE " + x + " " + y);
                                g.changeMove();
                            }
                            else{
                                out.println("MOVE_NOT_OK move");
                            }
                            break;
                        case "TERRITORY_FIELD":
                            ArrayList<String> territoryCoordinate = new ArrayList<String>(Arrays.asList(input.substring(16).split("\\s* \\s*")));
                            int x2 = Integer.parseInt(territoryCoordinate.get(0)) + 1;
                            int y2 = Integer.parseInt(territoryCoordinate.get(1)) + 1;
                            if(playerColor.equals(BoardFieldOwnership.WHITE.toString()) && g.whiteMove() && t.chooseTerritory(new Point(x2, y2), BoardFieldOwnership.WHITE)){
                                String s = "TERRITORY_CHOOSE_OK " + boardFieldsToString(t.getBoardFields());
                                out.println(s);
                                players.get(oponentName).println("OPPONENT_TERRITORY_CHOOSE_OK " + x2 + " " + y2);

                            }
                            else if(playerColor.equals(BoardFieldOwnership.BLACK.toString())&& !g.whiteMove() && t.chooseTerritory(new Point(x2, y2), BoardFieldOwnership.BLACK)){
                                String s = "TERRITORY_CHOOSE_OK " + boardFieldsToString(t.getBoardFields());
                                out.println(s);
                                players.get(oponentName).println("OPPONENT_TERRITORY_CHOOSE_OK " + x2 + " " + y2);
                            }
                            else{
                                out.println("TERRITORY_CHOOSE_NOT_OK TERRITORY_CHOOSE_NOT_OK");
                            }
                            break;
                        case "OPPONENT_MOVE":
                            ArrayList<String> ss = new ArrayList<String>(Arrays.asList(input.substring(14).split("\\s* \\s*")));
                            int x1 = Integer.parseInt(ss.get(0));
                            int y1 = Integer.parseInt(ss.get(1));
                            if(!playerColor.equals(BoardFieldOwnership.WHITE.toString()) && g.whiteMove() && g.placeStone(new Point(x1, y1), BoardFieldOwnership.WHITE)){
                                String s = "MOVE_OK " + boardFieldsToString(g.getBoardFields());
                                out.println(s);
                                g.changeMove();

                            }
                            else if(!playerColor.equals(BoardFieldOwnership.BLACK.toString())&& !g.whiteMove() && g.placeStone(new Point(x1, y1), BoardFieldOwnership.BLACK)){
                                String s = "MOVE_OK " + boardFieldsToString(g.getBoardFields());
                                out.println(s);
                                g.changeMove();
                            }
                            else{
                                out.println("MOVE_NOT_OK move");
                            }
                            break;
                        case "OPPONENT_TERRITORY_CHOOSE":
                            ArrayList<String> oponentTerritoryCoordinate = new ArrayList<String>(Arrays.asList(input.substring(26).split("\\s* \\s*")));
                            int x22 = Integer.parseInt(oponentTerritoryCoordinate.get(0));
                            int y22 = Integer.parseInt(oponentTerritoryCoordinate.get(1));
                            if(!playerColor.equals(BoardFieldOwnership.WHITE.toString()) && g.whiteMove() && t.chooseTerritory(new Point(x22, y22), BoardFieldOwnership.WHITE)){
                                String s = "TERRITORY_CHOOSE_OK " + boardFieldsToString(t.getBoardFields());
                                out.println(s);

                            }
                            else if(!playerColor.equals(BoardFieldOwnership.BLACK.toString())&& !g.whiteMove() && t.chooseTerritory(new Point(x22, y22), BoardFieldOwnership.BLACK)){
                                String s = "TERRITORY_CHOOSE_OK " + boardFieldsToString(t.getBoardFields());
                                out.println(s);
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

        public static String boardFieldsToString(HashMap<Point, BoardFieldOwnership> boardFields){
            String white = "WHITE ";
            String black = "BLACK ";
            String whiteTerritory = "WHITE_TERRITORY ";
            String blackTerritory = "BLACK_TERRITORY ";

            for (Map.Entry<Point, BoardFieldOwnership> entry : boardFields.entrySet()) {
                Point p = entry.getKey();
                BoardFieldOwnership value = entry.getValue();
                if(value == BoardFieldOwnership.BLACK){
                    black += (int)p.getX() + " " + (int)p.getY() + " ";
                }
                else if (value == BoardFieldOwnership.WHITE){
                    white += (int)p.getX() + " " + (int)p.getY() + " ";
                }
                else if (value == BoardFieldOwnership.WHITE_TERRITORY){
                    whiteTerritory += (int)p.getX() + " " + (int)p.getY() + " ";
                }
                else if (value == BoardFieldOwnership.BLACK_TERRITORY){
                    blackTerritory += (int)p.getX() + " " + (int)p.getY() + " ";
                }
            }
            return white + " " + black + " " + whiteTerritory + " " + blackTerritory;

        }
    }
}