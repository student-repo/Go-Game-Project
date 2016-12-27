package gogame.client.main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import javax.swing.*;

import gogame.game.engine.BoardFieldOwnership;
import gogame.game.engine.GameBoard;
import gogame.game.engine.TerritoryBoard;
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
    private BoardFieldOwnership playerColor, opponentColor;
    private String opponentName;
    private GameBoard game;
    private TerritoryBoard territoryMode;



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
                    opponentName = challangeUser;
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
            ArrayList<String> inputArguments;
            String line = in.readLine();
            switch(getFirstWordOfString(line)) {
                case "UPDATE_NAMES":
                    onlinePlayers.addAll(Arrays.asList(line.substring(13).split("\\s*,\\s*")));
                    onlinePlayersList.setListData(getOnlinePlayers());
                    break;
                case "PLAYER_COLOR":
                    if(line.substring(13).equals("WHITE")){
                        playerColor = BoardFieldOwnership.WHITE;
                        opponentColor = BoardFieldOwnership.BLACK;
                    }
                    else{
                        opponentColor = BoardFieldOwnership.WHITE;
                        playerColor = BoardFieldOwnership.BLACK;
                    }
                    game = new GameBoard();
                    System.out.println(playerColor);
                    boardFrame.setPlayerColor(playerColor.toString());
                    break;
                case "SUBMITNAME":
                    out.println(getName());
                    break;
                case "MOVE_OK1":
                    boardFrame.updateBoard(game.getBoardFields());
                    break;
                case "MOVE_OK":
                    inputArguments = new ArrayList<String>(Arrays.asList(line.substring(8).split("\\s* \\s*")));
                    int xx1 = Integer.parseInt(inputArguments.get(0));
                    int yy1 = Integer.parseInt(inputArguments.get(1));
                    game.placeStone(new Point(xx1, yy1), playerColor);
                    boardFrame.updateBoard(game.getBoardFields());
                    if(playerColor == BoardFieldOwnership.BLACK){
                        boardFrame.changeCapturesStones(game.getCapturedWhiteStones());
                    }
                    else{
                        boardFrame.changeCapturesStones(game.getCapturedBlackStones());
                    }
                    break;
                case "TERRITORY_CHOOSE_OK":
                    inputArguments= new ArrayList<String>(Arrays.asList(line.substring(20).split("\\s* \\s*")));
                    int xxx1 = Integer.parseInt(inputArguments.get(0));
                    int yyy1 = Integer.parseInt(inputArguments.get(1));
                    territoryMode.chooseTerritory(new Point(xxx1, yyy1), playerColor);
                    boardFrame.updateBoard(territoryMode.getBoardFields());
                    break;
                case "OPPONENT_TERRITORY_CHOOSE_OK1":
                    inputArguments = new ArrayList<String>(Arrays.asList(line.substring(30).split("\\s* \\s*")));
                    int xxxx1 = Integer.parseInt(inputArguments.get(0));
                    int yyyy1 = Integer.parseInt(inputArguments.get(1));
                    territoryMode.chooseTerritory(new Point(xxxx1, yyyy1), opponentColor);
                    boardFrame.updateBoard(territoryMode.getBoardFields());
                    break;
                case "TERRITORY_CHOOSE_NOT_OK":
                    boardFrame.moveNotAllowed();
                    break;
                case "OPPONENT_SUGGEST_TERRITORY":
                    boardFrame.updateBoard(territoryMode.getFinishBoardFields(opponentColor));
                    boardFrame.showTerritorySuggestDialog();
                    break;
                case "MOVE_NOT_OK":
                    boardFrame.moveNotAllowed();
                    break;
                case "RESUME_GAME":
                    game.restoreGameBoard();
                    boardFrame.resGame();
                    boardFrame.updateBoard(game.getBoardFields());
                    out.println("OPPONENT_RESUME_GAME OPPONENT_RESUME_GAME");
                    break;
                case "SHOW_RESULT":
                    boardFrame.finishDialog(line.substring(12));
                    break;
                case "PASS_NOT_YOUR_MOVE":
                    boardFrame.passImpossiblyDialog();
                    break;
                case "REMOVE_NAME":
                    onlinePlayers.remove(line.substring(12));
                    onlinePlayersList.setListData(getOnlinePlayers());
                    break;
                case "OPPONENT_TERRITORY_CHOOSE_OK":
                    inputArguments = new ArrayList<String>(Arrays.asList(line.substring(29).split("\\s* \\s*")));
                    int xxx = Integer.parseInt(inputArguments.get(0));
                    int yyy = Integer.parseInt(inputArguments.get(1));
                    out.println("OPPONENT_TERRITORY_CHOOSE " + xxx + " " + yyy);
                    break;
                case "OPPONENT_MOVE":
                    inputArguments = new ArrayList<String>(Arrays.asList(line.substring(14).split("\\s* \\s*")));
                    int xx = Integer.parseInt(inputArguments.get(0));
                    int yy = Integer.parseInt(inputArguments.get(1));
                    if(playerColor.toString().equals("WHITE")){
                        game.placeStone(new Point(xx, yy), BoardFieldOwnership.BLACK);
                    }
                    else{
                        game.placeStone(new Point(xx, yy), BoardFieldOwnership.WHITE);
                    }
                    out.println("OPPONENT_MOVE " + xx + " " + yy);
                    break;
                case "CHALLANGE":
                    String challanger = line.substring(10);
                    if (JOptionPane.showConfirmDialog(
                            frame,
                            "You got a challange from " + challanger +
                                    ". Do you take up?",
                            "Challange suggestion",
                            YES_NO_OPTION) == YES_OPTION) {
                        opponentName = challanger;
                        frame.setVisible(false);
                        boardFrame = new BoardFrame(this, playerName);
                        out.println("CHALLANGE_ACCEPTED " + challanger +
                                " " + playerName);

                    }
                    else{
                        out.println("CHALLANGE_REJECTED " + challanger + " " + playerName);
                    }
                    break;
                case "CHALLANGE_ACCEPTED":
                    out.println("YOUR_CHALLANGE_ACCEPTED " + opponentName);
                    JOptionPane.showConfirmDialog(
                            frame,
                            "Player " + getFirstWordOfString(line.substring(19)) +
                            " accepted your challange. Let's start!",
                            "Challange status info",
                            DEFAULT_OPTION,
                            INFORMATION_MESSAGE
                    );

                    opponentName = getFirstWordOfString(line.substring(19));
                    frame.setVisible(false);
                    boardFrame = new BoardFrame(this, playerName);
                    break;
                case "OPPONENT_PASS":
                    out.println("OPPONENT_PASS_CHANGE_MOVE OPPONENT_PASS");
                    boardFrame.showOpponentPassDialog();
                    break;
                case "OPPONENT_INIT_TERRITORY_MODE":
                    game.restoreGameBoard();
                    territoryMode = new TerritoryBoard(game.getBoardFields(), opponentColor);
//                    territoryMode.setMove(opponentColor.toString());
                    boardFrame.setTerritoryMode(true);
                    boardFrame.updateBoard(game.getBoardFields());
                    out.println("OPPONENT_INIT_TERRITORY_MODE OPPONENT_INIT_TERRITORY_MODE");
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
                case "OPPONENT_RESIGN":
                    JOptionPane.showConfirmDialog(
                            frame,
                            "Your oponent resigned",
                            "Challange status info",
                            DEFAULT_OPTION,
                            INFORMATION_MESSAGE
                    );
                    boardFrame.closeFrame();
                    frame.setVisible(true);

                    break;
                case "INACCESSIBLE":
                    JOptionPane.showConfirmDialog(
                            frame,
                            "Player " + getFirstWordOfString(line.substring(12)) +
                                    " is already playing.",
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
        System.out.println("player clicked: " + (int)p.getX() + " " + (int)p.getY());
    }

    public void sendTerritoryField(Point p) {
        out.println("TERRITORY_FIELD " + (int)p.getX() + " " + (int)p.getY());
        System.out.println("player clicked territory: " + p);
    }

    public void initTerritoryMode() throws IOException, URISyntaxException {
        territoryMode = new TerritoryBoard(game.getBoardFields(), playerColor);
        game.restoreGameBoard();
        boardFrame.updateBoard(game.getBoardFields());
        out.println("INIT_TERRITORY_MODE INIT_TERRITORY_MODE");
    }

    public void suggestTerritory() throws IOException, URISyntaxException {
        out.println("SUGGEST_TERRITORY " + playerColor);
        boardFrame.updateBoard(territoryMode.getFinishBoardFields(playerColor));
    }

    public void resumeGame() throws IOException, URISyntaxException {
        game.restoreGameBoard();
        boardFrame.updateBoard(game.getBoardFields());
        out.println("RESUME_GAME RESUME_GAME");
    }


    public void sendPass(){
        out.println("PASS PASS PASS");
    }


    public void handleResignGame(){
        boardFrame.closeFrame();
        frame.setVisible(true);
        out.println("HANDLE_OPPONENT_RESIGN " + opponentName + " " + playerName);
    }

    public void showResult(){
        out.println("SHOW_RESULT SHOW_RESULT");
    }

    public static void main(String[] args) throws Exception {
        GoClient client = new GoClient();
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.run();
    }
}