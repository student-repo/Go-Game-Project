package gogame.server.main;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import gogame.bot.alphabot.AlphaBot;
import gogame.client.main.GoPlayer;
import gogame.game.engine.*;

public class GoServer {

    private static final int PORT = 8080;

    private static HashMap<String, PrintWriter> players = new HashMap<String, PrintWriter>();
    private static ArrayList<String> temporarilyInaccessible = new ArrayList<String>();
    ServerSocket listener;

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
    public GoServer() {
        try {
            listener = new ServerSocket(PORT);
            listener.setSoTimeout(100);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void connectClient() throws IOException {
        new Handler(listener.accept()).start();
    }

    private static class Handler extends Thread {
        private String name;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private GoPlayer player, opponentPlayer;
        private AlphaBot alphaBot;
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
                    String input = in.readLine();
                    ArrayList<String> inputArguments = null;
                    int x = 0,y = 0;
                    String messageFirstWord = getFirstWordOfString(input);
                    if(wordsInString(input) > 1){
                        inputArguments = new ArrayList<String>(Arrays.asList(input.substring(messageFirstWord.length() + 1)
                                .split("\\s* \\s*")));
                        if(wordsInString(input) > 2){
                            try{
                                x = Integer.parseInt(inputArguments.get(0));
                                y = Integer.parseInt(inputArguments.get(1));
                            }
                            catch (Exception e){

                            }
                        }
                    }
                    switch(messageFirstWord) {
                        case "CHALLANGE":
                            String playerSendChallange = inputArguments.get(0);
                            String playerGetChallange = inputArguments.get(1);
                            if(temporarilyInaccessible.contains(playerSendChallange) || temporarilyInaccessible.contains(playerGetChallange )){
                                players.get(playerSendChallange).println("INACCESSIBLE" + " " + playerGetChallange);
                            }
                            else{
                                players.get(playerGetChallange).println("CHALLANGE" + " " + name);
                            }
                            break;

                        case "CHALLANGE_ACCEPTED":
                            players.get(inputArguments.get(0)).println("CHALLANGE_ACCEPTED " + inputArguments.get(1));
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
                            player = new GoPlayer(BoardFieldOwnership.BLACK, GameEngineStatus.GAME);
                            opponentPlayer = new GoPlayer(BoardFieldOwnership.WHITE, GameEngineStatus.GAME);
                            game = new GameEngine(opponentPlayer, player);
                            playerColor = BoardFieldOwnership.BLACK;
                            opponentColor = BoardFieldOwnership.WHITE;
                            oponentName = inputArguments.get(0);

                            out.println("PLAYER_COLOR " + BoardFieldOwnership.BLACK);

                            break;
                        case "CHALLANGE_REJECTED":
                            players.get(inputArguments.get(0)).println("CHALLANGE_REJECTED " + inputArguments.get(1));
                            break;
                        case "SHOW_RESULT":
                            String sasl;
                            if(playerColor == BoardFieldOwnership.WHITE){
                                territoryMode.getFinishBoardFields(BoardFieldOwnership.BLACK);
                                sasl = "SHOW_RESULT " + game.getWinnerMessage(name, oponentName);
                                out.println(sasl);
                            }
                            else{
                                territoryMode.getFinishBoardFields(BoardFieldOwnership.WHITE);
                                sasl = "SHOW_RESULT " + game.getWinnerMessage(oponentName, name);
                                out.println(sasl);
                            }
                            players.get(oponentName).println(sasl);
                            break;
                        case "HANDLE_OPPONENT_RESIGN":
                            game = null;
                            temporarilyInaccessible.remove(inputArguments.get(0));
                            temporarilyInaccessible.remove(inputArguments.get(1));
                            players.get(inputArguments.get(0)).println("OPPONENT_RESIGN Player");

                            break;
                        case "PASS":
                            if(game.passTurn(player)){
                                players.get(oponentName).println("OPPONENT_PASS");
                            }
                            else{
                                out.println("PASS_NOT_YOUR_MOVE");
                            }
                            break;
                        case "OPPONENT_PASS_CHANGE_MOVE":
                            game.passTurn(opponentPlayer);
                            break;
                        case "INIT_TERRITORY_MODE":
                            if(game.passTurn(player)){
                                game.restoreGameBoard();
                                territoryMode = new TerritoryBoard(game.getBoardFields(), playerColor);
                                players.get(oponentName).println("OPPONENT_INIT_TERRITORY_MODE");
                            }
                            break;
                        case "RESUME_GAME":
                            game.restoreGameBoard();
                            game.resumeGame(player);
                            players.get(oponentName).println("RESUME_GAME");
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
                            players.get(oponentName).println("OPPONENT_SUGGEST_TERRITORY");
                            break;
                        case "MOVE":
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
                            if(territoryMode.chooseTerritory(new Point(x, y), playerColor)){
                                out.println("TERRITORY_CHOOSE_OK " + x + " " + y);
                                players.get(oponentName).println("OPPONENT_TERRITORY_CHOOSE " + x + " " + y);
                            }
                            else{
                                out.println("TERRITORY_CHOOSE_NOT_OK");
                            }
                            break;
                        case "OPPONENT_MOVE":
                            try{
                                game.makeMove(x, y, opponentPlayer);
                                out.println("OPPONENT_MOVE_OK " + x + " " + y);
                            }catch (Exception e){
                                System.out.println("Exception OPPONENT MOVE");
                            }
                            break;
                        case "OPPONENT_TERRITORY_CHOOSE":
                            if(territoryMode.chooseTerritory(new Point(x, y), opponentColor)){
                                out.println("OPPONENT_TERRITORY_CHOOSE_OK " + x + " " + y);
                            }
                            else{
                                out.println("TERRITORY_CHOOSE_NOT_OK");
                            }
                            break;
//                        HANDLE SINGLE PLAYER
                        case "SINGLEPLAYER_MODE":
                            name = inputArguments.get(0);

                            player = new GoPlayer(BoardFieldOwnership.BLACK, GameEngineStatus.GAME);
//                            opponentPlayer = new GoPlayer(BoardFieldOwnership.WHITE, GameEngineStatus.GAME);
                            alphaBot = new AlphaBot(BoardFieldOwnership.WHITE, GameEngineStatus.GAME);
                            game = new GameEngine(alphaBot ,player);
                            alphaBot.setGameEngine(game);

                            playerColor = BoardFieldOwnership.BLACK;

                            temporarilyInaccessible.add(name);
                            out.println("SINGLEPLAYER_PLAYER_COLOR " + BoardFieldOwnership.BLACK);
                            break;
                        case "SINGLEPLAYER_MOVE":
                            try{
                                game.makeMove(x, y, player);
                                out.println("SINGLEPLAYER_MOVE_OK " + x + " " + y + " " + alphaBot.makeMove());
                            }
                            catch(Exception e){
                                System.out.println(game.getCurrentPlayer());
                                out.println("MOVE_NOT_OK move");
                            }
                            break;
                        case "SINGLEPLAYER_INIT_TERRITORY_MODE":
//                            if(game.passTurn(player)){
                                game.restoreGameBoard();
                                territoryMode = new TerritoryBoard(game.getBoardFields(), playerColor);
//                            }
                            break;
                        case "SINGLEPLAYER_TERRITORY_FIELD":
                            if(territoryMode.chooseTerritory(new Point(x, y), playerColor)){
                                out.println("TERRITORY_CHOOSE_OK " + x + " " + y);
                            }
                            else{
                                out.println("TERRITORY_CHOOSE_NOT_OK");
                            }
                            break;
                        case "SINGLEPLAYER_SUGGEST_TERRITORY":
                                territoryMode.getFinishBoardFields(playerColor);
                                out.println("SHOW_RESULT " + game.getWinnerMessage(alphaBot.getName(), name));
                            break;
                        case "SINGLEPLAYER_PASS":
                            if(game.passTurn(player)){
                                game.restoreGameBoard();
                                alphaBot.suggestTerritory();
                                out.println("SINGLEPLAYER_SUGGEST_TERRITORY");
                            }
                            break;
                        case "SINGLEPLAYER_RESUME_GAME":
                            game.restoreGameBoard();
                            game.resumeGame(player);
                            alphaBot.notifyGameStateChanged(GameEngineStatus.GAME);
                            String botMove = alphaBot.makeMove();
                            out.println("SINGLEPLAYER_BOT_MOVE " + botMove);
                            System.out.println("single Player resume : " + botMove);
                            break;
                        case "SINGLEPLAYER_SHOW_RESULT":
                                out.println("SHOW_RESULT " + game.getWinnerMessage(alphaBot.getName(), name));
                            break;
                        case "FOFO":
                            out.println("SHOW_RESULT");
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
//            if(str.indexOf(' ') == -1){
//                return str;
//            }
            return str.split("\\s+").length == 1 ?  str : str.substring(0, str.indexOf(' '));
        }

        private int wordsInString(String str){
            String trim = str.trim();
            if (trim.isEmpty()){
                return 0;
            }
            return trim.split("\\s+").length;
        }

    }
}