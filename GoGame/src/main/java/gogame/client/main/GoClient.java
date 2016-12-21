package gogame.client.main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import javax.swing.*;

import gogame.game.engine.BoardFieldOwnership;
import gogame.server.main.*;
import static javax.swing.JOptionPane.*;

public class GoClient {

    private BufferedReader in;
    private PrintWriter out;
    private JFrame frame = new JFrame("Game Go Menu");
    private JLabel currentPlayerLabel = new JLabel("Currently Logged In As");
    private String playerName = "";
    private JLabel playerNameLabel = new JLabel();
    private JLabel currnetlyOnlineLabel = new JLabel("Currently Onilne");
    private JList<String> onlinePlayersList = new JList<String>();
    private JScrollPane onlinePlayersScrollPane = new JScrollPane(onlinePlayersList, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
    private JButton challangeButton = new JButton("Challange");
    private HashSet<String> onlinePlayers = new HashSet<String>();
    private BoardFrame boardFrame;
    private String playerColor = "";


    public GoClient() {

        currnetlyOnlineLabel.setHorizontalAlignment(SwingConstants.CENTER);
        playerNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        currentPlayerLabel.setForeground(Color.LIGHT_GRAY);
        currentPlayerLabel.setBounds(300, 10, 170, 20);
        playerNameLabel.setForeground(Color.LIGHT_GRAY);
        playerNameLabel.setBounds(300, 30, 170, 20);
        currnetlyOnlineLabel.setForeground(Color.LIGHT_GRAY);
        currnetlyOnlineLabel.setBounds(300, 60, 170, 20);
        onlinePlayersScrollPane.setBounds(300, 80, 170 ,100);
        challangeButton.setBounds(100, 150, 120, 20);
        onlinePlayersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        try {
            frame.setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File(System.getProperty("user.dir") + "/src/resources/menu-background.jpg")))));
        } catch (IOException e) {
            e.printStackTrace();
        }

        frame.setResizable(false);
        frame.add(playerNameLabel);
        frame.add(currentPlayerLabel);
        frame.add(currnetlyOnlineLabel);
        frame.add(onlinePlayersScrollPane);
        frame.add(challangeButton);
        frame.setSize(500,250);
        frame.setLayout(null);
        frame.getContentPane().setBackground(Color.lightGray);


        // Add Listeners
        challangeButton.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                String challangeUser = onlinePlayersList.getSelectedValue();
                if (challangeUser == null){
                    JOptionPane.showConfirmDialog(
                            frame,
                            "Please select opponent!",
                            "Challange info",
                            DEFAULT_OPTION,
                            INFORMATION_MESSAGE
                    );
                }
                else{
                    out.println("CHALLANGE " + playerName + " " + challangeUser);
                }
            }
        });
    }

    private String getName() {
        playerName = null;
        while(playerName == null || playerName.equals("")){
            playerName = JOptionPane.showInputDialog(
                    frame,
                    "Please enter your nick:",
                    "Screen name selection",
                    JOptionPane.PLAIN_MESSAGE);
        }
        playerNameLabel.setText(playerName);

        System.out.println(playerName);

        return playerName;
    }

    /**
     * Connects to the server then enters the processing loop.
     */
    private void run() throws IOException, URISyntaxException, ClassNotFoundException {


        // Make connection and initialize streams
        Socket socket = new Socket("localhost", 8080);

        in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        while (true) {
            String line = in.readLine();
            switch(getFirstWordOfString(line)) {
                case "UPDATE_NAMES":
                    onlinePlayers.addAll(Arrays.asList(line.substring(13).split("\\s*,\\s*")));
                    onlinePlayersList.setListData(getOnlinePlayers());
                    break;
                case "PLAYER_COLOR":
                    playerColor = line.substring(13);
                    System.out.println(playerColor);
                    break;
                case "SUBMITNAME":
                    out.println(getName());
                    break;
                case "OK":
                    boardFrame.updateBoard(stringToBoardFiels(line.substring(3)));
                    break;
                case "NOT_OK":
                    System.out.println("NOT GOOD MOVE");
                    break;
                case "REMOVE_NAME":
                    onlinePlayers.remove(line.substring(12));
                    onlinePlayersList.setListData(getOnlinePlayers());
                    break;
                case "CHALLANGE":
                    String challanger = line.substring(10);
                    if (JOptionPane.showConfirmDialog(
                            frame,
                            "You got a challange from " + challanger +
                                    ". Do you take up?",
                            "Challange suggestion",
                            YES_NO_OPTION) == YES_OPTION) {
                        frame.setVisible(false);
                        boardFrame = new BoardFrame(this);
                        out.println("CHALLANGE_ACCEPTED " + challanger +
                                " " + playerName);

                    }
                    else{
                        out.println("CHALLANGE_REJECTED " + challanger + " " + playerName);
                    }
                    break;
                case "CHALLANGE_ACCEPTED":
                    JOptionPane.showConfirmDialog(
                            frame,
                            "Player " + getFirstWordOfString(line.substring(19)) +
                            " accepted your challange. Let's start!",
                            "Challange status info",
                            DEFAULT_OPTION,
                            INFORMATION_MESSAGE
                    );
                    frame.setVisible(false);
                    boardFrame = new BoardFrame(this);
                    break;
                case "CHALLANGE_REJECTED":
                    JOptionPane.showConfirmDialog(
                            frame,
                            "Player " + getFirstWordOfString(line.substring(19)) +
                            " rejected your challange.",
                            "Challange status info",
                            DEFAULT_OPTION,
                            INFORMATION_MESSAGE
                    );
                    break;
            }
        }
    }

        private String getFirstWordOfString(String str){
            return str.split("\\s+").length == 1 ?  str : str.substring(0, str.indexOf(' '));
        }
    private String[] getOnlinePlayers(){
        String[] playersOnline = onlinePlayers.stream().
                filter(map -> !map.equals(playerName))
                .collect(Collectors.toSet())
                .toArray(new String[onlinePlayers.size()]);

        if(playersOnline.length == 1){
            onlinePlayersList.setEnabled(false);
            return new String[]{"Nobody else online"};
        }
        else{
            onlinePlayersList.setEnabled(true);
            return playersOnline;
        }
    }
    public void sendMove(Point p) {
        out.println("MOVE " + (int)p.getX() + " " + (int)p.getY());
        System.out.println("player clicked at: " + p);
    }
    public HashMap<Point, BoardFieldOwnership> stringToBoardFiels(String str){
        HashMap<Point, BoardFieldOwnership> boardFields = new HashMap<Point, BoardFieldOwnership>();
        for (int i=1; i<=19; i++) {
            for (int j=1; j<=19; j++) {
                boardFields.put(new Point(i, j), BoardFieldOwnership.FREE);
            }
        }
        ArrayList<String> s = new ArrayList<String>(Arrays.asList(str.split("\\s* \\s*")));
        int k = 1;
        while(!s.get(k).equals("BLACK")){
            int x = Integer.parseInt(s.get(k));
            int y = Integer.parseInt(s.get(k + 1));

            Point p = new Point(x, y);
            boardFields.put(p, BoardFieldOwnership.WHITE);
            k++;
            k++;
        }
        k++;

        while(k < s.size()){
            int x1 = Integer.parseInt(s.get(k));
            int y1 = Integer.parseInt(s.get(k + 1));

            Point p = new Point(x1, y1);
            boardFields.put(p, BoardFieldOwnership.BLACK);
            k++;
            k++;
        }

        return boardFields;
    }

    public static void main(String[] args) throws Exception {
        GoClient client = new GoClient();
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.run();
    }
}