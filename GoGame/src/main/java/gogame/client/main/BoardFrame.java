package gogame.client.main;

import gogame.game.engine.BoardFieldOwnership;
import gogame.game.engine.GameBoard;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;

import static javax.swing.JOptionPane.DEFAULT_OPTION;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;

public class BoardFrame {

    private final int BOARD_SIZE = 19;
    private boolean onClose = true;
    private final JPanel gui = new JPanel(new BorderLayout(3, 3));
    private JFrame frame = new JFrame("GoGame");
    private ImagePanel[][] fields = new ImagePanel[BOARD_SIZE][BOARD_SIZE];
    private JPanel board;
    public GameBoard g = new GameBoard();
    private GoClient player;
    private JButton resignButton = new JButton("Resign");
    private JButton passButton = new JButton("Pass");
    private JButton suggestTerritoryButton = new JButton("Suggest territory");
    private JLabel playerNick;
    private JLabel playerColor;
    private boolean territoryMode = false;
    private JToolBar tools = new JToolBar();
    private JLabel capturedStones = new JLabel("Captured Stones: 0");

    BoardFrame(GoClient client, String nick) throws IOException, URISyntaxException {
        playerNick = new JLabel("NICK: " + nick);
        initializeGui();
        player = client;
    }

    public final void initializeGui() throws IOException, URISyntaxException {
        gui.setBorder(new EmptyBorder(5, 5, 5, 5));
        tools.setFloatable(false);
        resignButton.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                player.handleResignGame();
            }
        });
        passButton.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                player.sendPass();
            }
        });
        suggestTerritoryButton.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                player.suggestTerritory();
            }
        });

        gui.add(tools, BorderLayout.PAGE_START);
        tools.add(passButton); // TODO - add functionality

        tools.addSeparator();
        tools.add(resignButton);
        tools.addSeparator();
        tools.addSeparator();
        tools.add(playerNick);
        tools.addSeparator();
        tools.add(capturedStones);
        tools.addSeparator();
        tools.addSeparator();

        board = new JPanel(new GridLayout(0, BOARD_SIZE));
        board.setBorder(new LineBorder(Color.BLACK));
        gui.add(board);

        createBoard();

        frame.add(gui);
//        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationByPlatform(true);

        frame.pack();
        frame.setMinimumSize(frame.getSize());
        frame.setVisible(true);
        frame.setSize(700,720);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                if(onClose){
                    JOptionPane.showConfirmDialog(
                            frame,
                            "You can't close this window. To exit please resign first.",
                            "Exit info",
                            DEFAULT_OPTION,
                            INFORMATION_MESSAGE
                    );
                }
            }
        });

    }
    private void clearFields(ArrayList<Point> arr) throws URISyntaxException, IOException {
        for(Point p : arr){
            fields[p.x][p.y].setBackground(ImageIO.
                    read(new File(System.getProperty("user.dir") + "/src/resources/fieldEmpty.png")));
        }

    }
    private void setFieldBackground(String image, Point field) throws URISyntaxException, IOException {
        fields[field.x][field.y].
                setBackground(ImageIO.
                        read(new File(System.getProperty("user.dir") + "/src/resources/" + image)));
    }
    
    private void setEmptyFieldBackground (Point field)  throws IOException, URISyntaxException{
    	int i = field.y;
    	int j = field.x;
    	if ((i > 0 && i < 18) && (j > 0 && j < 18)) {
            setFieldBackground("fieldEmpty.png", new Point(j,i));
    	}
    	else if (j == 0 && (i > 0 && i < 18))
    		setFieldBackground("fieldEmptyBorderWest.png", new Point(j,i));
    	else if (j == 18 && (i > 0 && i < 18))
    		setFieldBackground("fieldEmptyBorderEast.png", new Point(j,i));
    	else if (i == 0 && (j > 0 && j < 18))
    		setFieldBackground("fieldEmptyBorderNorth.png", new Point(j,i));
    	else if (i == 18 && (j > 0 && j < 18))
    		setFieldBackground("fieldEmptyBorderSouth.png", new Point(j,i));
    	else if (j == 0 && i == 0)
    		setFieldBackground("fieldEmptyCornerNorthWest.png", new Point(j,i));
    	else if (j == 0 && i == 18)
    		setFieldBackground("fieldEmptyCornerSouthWest.png", new Point(j,i));
    	else if (j == 18 && i == 0)
    		setFieldBackground("fieldEmptyCornerNorthEast.png", new Point(j,i));
    	else if (j == 18 && i == 18)
    		setFieldBackground("fieldEmptyCornerSouthEast.png", new Point(j,i));
    	
    }
    private void createBoard() throws IOException, URISyntaxException {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
            	fields[j][i] = new ImagePanel();
            	setEmptyFieldBackground(new Point (j,i));
            	int finali = i;
                int finalj = j;
                fields[j][i].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if(territoryMode){
                            player.sendTerritoryField(new Point(finali, finalj));
                        }
                        else{
                            player.sendMove(new Point(finali, finalj));
                        }

                    }
                });
                board.add(fields[j][i]);
            }
        }
    }

    public void moveNotAllowed(){
        JOptionPane.showConfirmDialog(
                                        frame,
                                        "Move not allowed",
                                        "Move allowed info",
                                        DEFAULT_OPTION,
                                        INFORMATION_MESSAGE
                                );
    }

    public void updateBoard(HashMap<Point, BoardFieldOwnership> boardFields) throws IOException, URISyntaxException {
        for(Point h: boardFields.keySet()){
            if(boardFields.get(h).equals(BoardFieldOwnership.BLACK)){
                setFieldBackground("blackPiece.png", new Point(h.y -1, h.x -1));
            }
            else if(boardFields.get(h).equals(BoardFieldOwnership.WHITE)){
                setFieldBackground("whitePiece.png", new Point(h.y - 1, h.x - 1));
            }
            else if(boardFields.get(h).equals(BoardFieldOwnership.BLACK_TERRITORY)){
                setFieldBackground("fieldTerritoryBlack.png", new Point(h.y - 1, h.x - 1));
            }
            else if(boardFields.get(h).equals(BoardFieldOwnership.WHITE_TERRITORY)){
                setFieldBackground("fieldTerritoryWhite.png", new Point(h.y - 1, h.x - 1));
            }
            else{
                setEmptyFieldBackground(new Point(h.y - 1, h.x - 1));
            }
        }
    }

    public void closeFrame(){
        onClose = false;
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }

    public void setPlayerColor(String s){
        playerColor = new JLabel("COLOR: " + s);
        tools.add(playerColor);
    }

    public void showOpponentPassDialog(){
//        JOptionPane.showConfirmDialog(
//                frame,
//                "Your opponent click pass - your move",
//                "Opponent pass info",
//                DEFAULT_OPTION,
//                INFORMATION_MESSAGE
//        );
        if(JOptionPane.showOptionDialog(null,
                "Your opponent clicked pass, " +
                        "you can continue playing or " +
                        "pass and suggest your territory",
                "Opponent pass",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{"Continue playing", "Pass and suggest your territory"},
                "default") == 1){
            System.out.println("player want Pass and suggest your territory");
            territoryMode = true;
            tools.addSeparator();
            tools.addSeparator();
            tools.add(suggestTerritoryButton);
            player.initTerritoryMode();
        }
        else{
            System.out.println("player want Continue playing");
        }
    }

    public void passImpossiblyDialog(){
        JOptionPane.showConfirmDialog(
                frame,
                "Your can't pass - it's not your move!",
                "Opponent pass info",
                DEFAULT_OPTION,
                INFORMATION_MESSAGE
        );
    }

    public void showTerritorySuggestDialog(){
        int a = JOptionPane.showOptionDialog(null,
                "Your opponent clicked pass, " +
                        "you can continue playing or " +
                        "pass and suggest your territory",
                "Opponent pass",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{"Accept", "Suggest your territory", "Resume game"},
                "default");
        switch (a){
            case 0:
                break;
            case 1:
                territoryMode = true;
                tools.addSeparator();
                tools.addSeparator();
                tools.add(suggestTerritoryButton);
                player.initTerritoryMode();
                break;
            case 2:
                territoryMode = false;
                player.resumeGame();
                break;

        }
    }

    public void resGame(){
        territoryMode = false;
    }

    public void changeCapturesStones(int n){
        capturedStones.setText("Captured Stones: " + n);
    }
    

    public static void main(String[] args) throws IOException, URISyntaxException {
//        new BoardFrame();
    }
}