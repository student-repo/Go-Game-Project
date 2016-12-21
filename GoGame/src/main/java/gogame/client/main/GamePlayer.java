package gogame.client.main;

import gogame.game.engine.BoardFieldOwnership;
import gogame.game.engine.GameBoard;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class GamePlayer extends Thread {
    private BufferedReader in;
    private PrintWriter out;
    private PrintWriter oponentOut;
    private BoardFieldOwnership playerColor = null;
    GameBoard g;
    private boolean myMove;


    public GamePlayer(Socket socket, BoardFieldOwnership playerColor, GameBoard g, Socket oponentSocket) throws IOException {
        in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        oponentOut = new PrintWriter(oponentSocket.getOutputStream(), true);
        this.playerColor = playerColor;
        this.g = g;
    }
    public void run(){
        try{

            out.println("PLAYER_COLOR " + playerColor);
//            while (true) {
//                out.println("PLAYER_COLOR " + playerColor);
//                playerColor = in.readLine();
//                if (playerColor == null) {
//                    return;
//                }
//                else {
//                    break;
//                }
//            }

            while (true) {
                String line = null;
                line = in.readLine();
                switch (getFirstWordOfString(line)) {
                    case "YYY":
                        System.out.println("@@@@ START @@@@");
                        break;
                    case "MOVE":
                        ArrayList<String> sss = new ArrayList<String>(Arrays.asList(line.substring(5).split("\\s* \\s*")));
                        int x = Integer.parseInt(sss.get(0));
                        int y = Integer.parseInt(sss.get(1));
                        if(playerColor == BoardFieldOwnership.WHITE && g.whiteMove() && g.placeStone(new Point(x, y), playerColor)){
                            String s = "OK " + boardFieldsToString(g.getBoardFields());
                            out.println(s);
                            oponentOut.println(s);
                            g.changeMove();

                        }
                        else if(playerColor == BoardFieldOwnership.BLACK && !g.whiteMove() && g.placeStone(new Point(x, y), playerColor)){
                            String s = "OK " + boardFieldsToString(g.getBoardFields());
                            out.println(s);
                            oponentOut.println(s);
                            g.changeMove();
                        }
                        else{
                            out.println("NOT_OK move");
                        }
                        break;

                }
            }
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }
    private String getFirstWordOfString(String str){
        return str.split("\\s+").length == 1 ?  str : str.substring(0, str.indexOf(' '));
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
