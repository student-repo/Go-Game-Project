package gogame.client.main;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;

public class BoardFrame {

    private final int BOARD_SIZE = 19;
    private final JPanel gui = new JPanel(new BorderLayout(3, 3));
    private JFrame frame = new JFrame("GoGame");
    private ImagePanel[][] fields = new ImagePanel[BOARD_SIZE][BOARD_SIZE];
    private JPanel board;


    BoardFrame() throws IOException, URISyntaxException {
        initializeGui();
    }

    public final void initializeGui() throws IOException, URISyntaxException {
        gui.setBorder(new EmptyBorder(5, 5, 5, 5));
        JToolBar tools = new JToolBar();
        tools.setFloatable(false);
        gui.add(tools, BorderLayout.PAGE_START);
        tools.add(new JButton("Pass")); // TODO - add functionality
        tools.addSeparator();
        tools.add(new JButton("Resign")); // TODO - add functionality

        board = new JPanel(new GridLayout(0, BOARD_SIZE));
        board.setBorder(new LineBorder(Color.BLACK));
        gui.add(board);

        createBoard();

        frame.add(gui);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationByPlatform(true);
        frame.pack();
        frame.setMinimumSize(frame.getSize());
        frame.setVisible(true);
        frame.setSize(700,720);
    }
    private void clearFields(ArrayList<Point> arr) throws URISyntaxException, IOException {
        for(Point p : arr){
            fields[p.x][p.y].setBackground(ImageIO.read(new File(System.getProperty("user.dir") + "/src/resources/fieldEmpty.png")));
        }

    }
    private void setFieldBackground(String image, Point field) throws URISyntaxException, IOException {
        fields[field.x][field.y].
                setBackground(ImageIO.read(new File(System.getProperty("user.dir") + "/src/resources/" + image)));
    }
    private void createBoard() throws IOException, URISyntaxException {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                fields[j][i] = new ImagePanel();
                setFieldBackground("fieldEmpty.png", new Point(j,i));
                int finali = i;
                int finalj = j;

                fields[j][i].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        try {
                            if(Math.random() <0.5){
                                setFieldBackground("whitePiece.png", new Point(finalj,finali));
                            }
                            else{
                                setFieldBackground("blackPiece.png", new Point(finalj,finali));
                            }

                        } catch (IOException e1) {
                            e1.printStackTrace();
                        } catch (URISyntaxException e1) {
                            e1.printStackTrace();
                        }
                        System.out.println("(" + finalj + "," + finali + ")" );
                    }
                });
                board.add(fields[j][i]);
            }
        }
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        new BoardFrame();
    }
}
