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
                        case "MOVE":
                            ArrayList<String> sss = new ArrayList<String>(Arrays.asList(input.substring(5).split("\\s* \\s*")));
                            int x = Integer.parseInt(sss.get(0));
                            int y = Integer.parseInt(sss.get(1));
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

        public String boardFieldsToString(HashMap<Point, BoardFieldOwnership> boardFields){
            String white = "WHITE ";
            String black = "BLACK ";

            for (Map.Entry<Point, BoardFieldOwnership> entry : boardFields.entrySet()) {
                Point p = entry.getKey();
                BoardFieldOwnership value = entry.getValue();
                if(value == BoardFieldOwnership.BLACK){
                    black += (int)p.getX() + " " + (int)p.getY() + " ";
                }
                else if (value == BoardFieldOwnership.WHITE){
                    white += (int)p.getX() + " " + (int)p.getY() + " ";
                }
            }
            return white + " " + black;

        }
    }
}