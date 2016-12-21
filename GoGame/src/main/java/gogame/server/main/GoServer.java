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
    private static HashMap<String, Socket> players2 = new HashMap<String, Socket>();


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
                            players2.put(name, socket);
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

                            sendMessageToPlayer("CHALLANGE",playerSendChallange, playerGetChallange );
                            break;

                        case "CHALLANGE_ACCEPTED":
                            ArrayList<String> challangeAcceptedPlayers = new ArrayList<String>(Arrays.asList(input.substring(19).split("\\s* \\s*")));
                            sendMessageToPlayer("CHALLANGE_ACCEPTED", challangeAcceptedPlayers.get(1), challangeAcceptedPlayers.get(0));
//                            new Game(players2.get(challangeAcceptedPlayers.get(1)), players2.get(challangeAcceptedPlayers.get(0))).run();
//                            new GamePlayer(players2.get(challangeAcceptedPlayers.get(1), "WHITE")).run();
                            GameBoard g = new GameBoard();
                            new GamePlayer(players2.get(challangeAcceptedPlayers.get(1)), BoardFieldOwnership.WHITE, g, players2.get(challangeAcceptedPlayers.get(0))).start();
                            new GamePlayer(players2.get(challangeAcceptedPlayers.get(0)), BoardFieldOwnership.BLACK, g, players2.get(challangeAcceptedPlayers.get(1))).start();
                            break;
                        case "CHALLANGE_REJECTED":
                            ArrayList<String> challangeRejectedPlayers = new ArrayList<String>(Arrays.asList(input.substring(19).split("\\s* \\s*")));
                            sendMessageToPlayer("CHALLANGE_REJECTED", challangeRejectedPlayers.get(1), challangeRejectedPlayers.get(0));
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