package gogame.server.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import gogame.client.main.*;
import gogame.game.engine.BoardFieldOwnership;
import gogame.game.engine.GameBoard;

public class GoServer {

    private static final int PORT = 8080;

    private static HashMap<String, PrintWriter> players = new HashMap<String, PrintWriter>();
    private static HashMap<String, Socket> playersSockets = new HashMap<String, Socket>();
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
                            playersSockets.put(name, socket);
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
                            GameBoard g = new GameBoard();
                            new GamePlayer(playersSockets.get(challangeAcceptedPlayers.get(1)), BoardFieldOwnership.WHITE, g, playersSockets.get(challangeAcceptedPlayers.get(0))).start();
                            new GamePlayer(playersSockets.get(challangeAcceptedPlayers.get(0)), BoardFieldOwnership.BLACK, g, playersSockets.get(challangeAcceptedPlayers.get(1))).start();
                            temporarilyInaccessible.add(challangeAcceptedPlayers.get(1));
                            temporarilyInaccessible.add(challangeAcceptedPlayers.get(0));
                            break;
                        case "CHALLANGE_REJECTED":
                            ArrayList<String> challangeRejectedPlayers = new ArrayList<String>(Arrays.asList(input.substring(19).split("\\s* \\s*")));
                            sendMessageToPlayer("CHALLANGE_REJECTED", challangeRejectedPlayers.get(1), challangeRejectedPlayers.get(0));
                            break;
                        case "HANDLE_OPONENT_RESIGN":
                            ArrayList<String> resignGamePlayers = new ArrayList<String>(Arrays.asList(input.substring(22).split("\\s* \\s*")));
                            temporarilyInaccessible.remove(resignGamePlayers.get(0));
                            temporarilyInaccessible.remove(resignGamePlayers.get(1));
                            sendMessageToPlayer("OPONENT_RESIGN", "Player", getFirstWordOfString(input.substring(22)));
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
            System.out.println("@@@@ inside send Message");
            for (String player : players.keySet()) {
                if(player.equals(toPlayer)){
                    players.get(player).println(messageType + " " + fromPlayer);
//                    players.get(player).println(messageType + " " + fromPlayer);
                    System.out.println("sending message to: " + toPlayer + " : " + messageType + " " + fromPlayer);
                }
            }
        }
    }
}