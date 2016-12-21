package gogame.client.main;

import gogame.game.engine.BoardFieldOwnership;
import gogame.game.engine.GameBoard;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;


public class GamePlayer extends Thread {
    private BufferedReader in;
    private PrintWriter out;
    private BoardFieldOwnership playerColor = null;
    GameBoard g;


    public GamePlayer(Socket socket, BoardFieldOwnership playerColor, GameBoard g) throws IOException {
        in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
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
                        System.out.println("player " + playerColor + " moved " + x + " " + y);
                        if(g.placeStone(new Point(x, y), playerColor)){
                            out.println("OK " + playerColor + " " + x + " " + y);

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
}
