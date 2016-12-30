package gogame.client.main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import javax.swing.*;
import gogame.game.engine.BoardFieldOwnership;
import gogame.game.engine.GameBoard;
import gogame.game.engine.TerritoryBoard;
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
    private Boolean singleplayerMode = false;



    public GoClient() {

        onlinePlayers.add("AlphaBot");

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
                    if(opponentName.equals("AlphaBot")){
                        singleplayerMode = true;
                        out.println("SINGLEPLAYER_MODE " + playerName);
                    }
                    else{
                        singleplayerMode = false;
                        out.println("CHALLANGE " + playerName + " " + challangeUser);
                    }
                }
            }
        });
    }

    /**
     * Show dialog for client and take nick
     * @return String with nick
     */
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
    public void connect() throws IOException {
        Socket socket = new Socket("localhost", 8080);

        in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }
    /**
     * Send command to server and take responses, show course of
     * the game, realize command from server
     */
    private void run() throws IOException, URISyntaxException, ClassNotFoundException {

        connect();

        while (true) {
            String line = in.readLine();
            ArrayList<String> inputArguments = null;
            int x = 0,y = 0;
            String messageFirstWord = getFirstWordOfString(line);
            if(wordsInString(line) > 1){
                inputArguments = new ArrayList<String>(Arrays.asList(line.substring(messageFirstWord.length() + 1)
                        .split("\\s* \\s*")));
                if(wordsInString(line) > 2){
                    try{
                        x = Integer.parseInt(inputArguments.get(0));
                        y = Integer.parseInt(inputArguments.get(1));
                    }
                    catch (Exception e){

                    }
                }
            }
            switch(messageFirstWord) {
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
                case "OPPONENT_MOVE_OK":
                        game.placeStone(new Point(x, y), opponentColor);
                    boardFrame.updateBoard(game.getBoardFields());
                    break;
                case "MOVE_OK":
                    game.placeStone(new Point(x, y), playerColor);
                    boardFrame.updateBoard(game.getBoardFields());
                    boardFrame.changeCapturesStones(game.getCapturedColorStones(opponentColor));
                    break;
                case "TERRITORY_CHOOSE_OK":
                    territoryMode.placeTerritory(new Point(x, y), playerColor);
                    boardFrame.updateBoard(territoryMode.getBoardFields());
                    break;
                case "OPPONENT_TERRITORY_CHOOSE_OK":
                    territoryMode.placeTerritory(new Point(x, y), opponentColor);
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
                    out.println("OPPONENT_RESUME_GAME");
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
                case "OPPONENT_TERRITORY_CHOOSE":
                    out.println("OPPONENT_TERRITORY_CHOOSE " + x + " " + y);
                    break;
                case "OPPONENT_MOVE":
                    out.println("OPPONENT_MOVE " + x + " " + y);
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
                    out.println("OPPONENT_PASS_CHANGE_MOVE");
                    boardFrame.showOpponentPassDialog();
                    break;
                case "OPPONENT_INIT_TERRITORY_MODE":
                    game.restoreGameBoard();
                    territoryMode = new TerritoryBoard(game.getBoardFields(), opponentColor);
                    boardFrame.setTerritoryMode(true);
                    boardFrame.updateBoard(game.getBoardFields());
                    out.println("OPPONENT_INIT_TERRITORY_MODE");
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
//                HANDLE SINGLE PLAYER MODE
                case "SINGLEPLAYER_PLAYER_COLOR":
                    if(line.substring(26).equals("WHITE")){
                        playerColor = BoardFieldOwnership.WHITE;
                        opponentColor = BoardFieldOwnership.BLACK;
                    }
                    else{
                        opponentColor = BoardFieldOwnership.WHITE;
                        playerColor = BoardFieldOwnership.BLACK;
                    }
                    frame.setVisible(false);
                    boardFrame = new BoardFrame(this, playerName);
                    game = new GameBoard();
                    System.out.println(playerColor);
                    boardFrame.setPlayerColor(playerColor.toString());
                    break;
                case "SINGLEPLAYER_MOVE_OK":
                    int xbot;
                    int ybot;
                    game.placeStone(new Point(x, y), playerColor);
                    if(inputArguments.size() == 4){
                        xbot = Integer.parseInt(inputArguments.get(2));
                        ybot = Integer.parseInt(inputArguments.get(3));
                        game.placeStone(new Point(xbot, ybot), opponentColor);
                        boardFrame.updateBoard(game.getBoardFields());
                    }
                    else{
                        boardFrame.updateBoard(game.getBoardFields());
                        boardFrame.showOpponentPassDialog();
                    }
                    boardFrame.changeCapturesStones(game.getCapturedColorStones(opponentColor));
                    break;
                case "SINGLEPLAYER_SUGGEST_TERRITORY":
                    boardFrame.showTerritorySuggestDialog();
                    break;
                case "SINGLEPLAYER_BOT_MOVE":
                    int xxbot;
                    int yybot;
                    if(inputArguments.size() == 2){
                        xxbot = Integer.parseInt(inputArguments.get(0));
                        yybot = Integer.parseInt(inputArguments.get(1));
                        game.placeStone(new Point(xxbot, yybot), opponentColor);
                        boardFrame.updateBoard(game.getBoardFields());
                    }
                    else{
                        boardFrame.updateBoard(game.getBoardFields());
                        boardFrame.showOpponentPassDialog();
                    }
                    boardFrame.changeCapturesStones(game.getCapturedColorStones(opponentColor));
                    break;
            }
        }
    }

    /**
     * Get first word of string
     * @param str String
     * @return String with first word
     */
        private String getFirstWordOfString(String str){
            return str.split("\\s+").length == 1 ?  str : str.substring(0, str.indexOf(' '));
        }
    /**
     * Get currently online players
     * @return String with all online players
     */
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
    /**
     * Answer for each move. Send move coordinate to server
     * @param p move coordinate
     */
    public void sendMove(Point p) {
        if(singleplayerMode){
            out.println("SINGLEPLAYER_MOVE " + (int)p.getX() + " " + (int)p.getY());
        }
        else{
            out.println("MOVE " + (int)p.getX() + " " + (int)p.getY());
        }
        System.out.println("player clicked: " + (int)p.getX() + " " + (int)p.getY());
    }
    /**
     * Send territory field to server
     * @param p territory coordinate
     */
    public void sendTerritoryField(Point p) {
        if(singleplayerMode){
            out.println("SINGLEPLAYER_TERRITORY_FIELD " + (int)p.getX() + " " + (int)p.getY());
        }
        else{
            out.println("TERRITORY_FIELD " + (int)p.getX() + " " + (int)p.getY());
        }
        System.out.println("player clicked territory: " + p);
    }

    /**
     * Send information to server, that client want choose territory
     */
    public void initTerritoryMode() throws IOException, URISyntaxException {
        territoryMode = new TerritoryBoard(game.getBoardFields(), playerColor);
        game.restoreGameBoard();
        boardFrame.updateBoard(game.getBoardFields());
        if(singleplayerMode){
            out.println("SINGLEPLAYER_INIT_TERRITORY_MODE");
        }
        else{
            out.println("INIT_TERRITORY_MODE");
        }
    }

    /**
     * Send information to server, that client want suggest territory
     */
    public void suggestTerritory() throws IOException, URISyntaxException {
        if(singleplayerMode){
            out.println("SINGLEPLAYER_SUGGEST_TERRITORY");
        }
        else{
            out.println("SUGGEST_TERRITORY " + playerColor);
        }
        boardFrame.updateBoard(territoryMode.getFinishBoardFields(playerColor));
    }

    /**
     * Send information to server, that client want resume game
     */
    public void resumeGame() throws IOException, URISyntaxException {
        game.restoreGameBoard();
        boardFrame.updateBoard(game.getBoardFields());
        if(singleplayerMode){
            out.println("SINGLEPLAYER_RESUME_GAME");
        }
        else{
            out.println("RESUME_GAME");
        }
    }

    /**
     * Send information to server, that client want pass
     */
    public void sendPass() throws IOException, URISyntaxException {
        if(singleplayerMode){
            territoryMode = new TerritoryBoard(game.getBoardFields(), opponentColor);
            game.restoreGameBoard();
            boardFrame.updateBoard(territoryMode.getFinishBoardFields(opponentColor));
            out.println("SINGLEPLAYER_PASS");
        }
        else{
            out.println("PASS");
        }
    }
    /**
     * Send information to server, that client want resign
     */
    public void handleResignGame(){
        boardFrame.closeFrame();
        frame.setVisible(true);
        out.println("HANDLE_OPPONENT_RESIGN " + opponentName + " " + playerName);
    }
    /**
     * Send information to server, that client finsh game and show result
     */
    public void showResult(){
        if(singleplayerMode){
            out.println("SINGLEPLAYER_SHOW_RESULT");
        }
        else{
            out.println("SHOW_RESULT");
        }
    }

    /**
     * Calculate quantity of words in string
     * @param str String
     * @return int with quantity of words
     */
    private int wordsInString(String str){
        String trim = str.trim();
        if (trim.isEmpty()){
            return 0;
        }
        return trim.split("\\s+").length;
    }

    public static void main(String[] args) throws Exception {
        GoClient client = new GoClient();
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.run();
    }
}