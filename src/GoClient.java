import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashSet;

import javax.swing.*;

import static javax.swing.JOptionPane.*;

public class GoClient {

    private BufferedReader in;
    private PrintWriter out;
    private JFrame frame = new JFrame("Game Go Menu");
    private JLabel currentPlayerLabel = new JLabel("Currently Logged In As");
    private String playerName = "";
    private JLabel playerNameLabel = new JLabel();
    private JLabel currnetlyOnlineLabel = new JLabel("Currently Onilne");
    private JTextArea onlinePlayersTextArea = new JTextArea();
    private JScrollPane onlinePlayersScrollPane = new JScrollPane(onlinePlayersTextArea);
    private JButton challangeButton = new JButton("Challange");
    private HashSet<String> onlinePlayers = new HashSet<String>();


    public GoClient() {

        currnetlyOnlineLabel.setHorizontalAlignment(SwingConstants.CENTER);
        playerNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        currentPlayerLabel.setBounds(300, 10, 170, 20);
        playerNameLabel.setBounds(300, 30, 170, 20);
        currnetlyOnlineLabel.setBounds(300, 60, 170, 20);
        onlinePlayersScrollPane.setBounds(300, 80, 170 ,100);
        challangeButton.setBounds(100, 150, 120, 20);
        frame.setResizable(false);
        frame.add(playerNameLabel);
        frame.add(currentPlayerLabel);
        frame.add(currnetlyOnlineLabel);
        frame.add(onlinePlayersScrollPane);
        frame.add(challangeButton);
        frame.setSize(500,250);
        frame.setLayout(null);
        onlinePlayersTextArea.setEditable(false);

        // Add Listeners
        challangeButton.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                String challangeUser= JOptionPane.showInputDialog(
                        frame,
                        "Challange player:",
                        "Chalange player selection",
                        JOptionPane.PLAIN_MESSAGE);
                if(onlinePlayers.contains(challangeUser) && !playerName.equals(challangeUser)){
                    out.println("CHALLANGE " + playerName + " " + challangeUser);
                }
                else if (!challangeUser.equals("")){
                    JOptionPane.showConfirmDialog(
                            frame,
                            "Player " + challangeUser + " isn't online",
                            "Challange info",
                            DEFAULT_OPTION,
                            INFORMATION_MESSAGE
                    );
                }
            }
        });
    }

    private String getName() {
        playerName = null;
        while(playerName == null || playerName.equals("")){
            playerName = JOptionPane.showInputDialog(
                    frame,
                    "Please enter your screen name:",
                    "Screen name selection",
                    JOptionPane.PLAIN_MESSAGE);
        }
        playerNameLabel.setText(playerName);

        return playerName;
    }

    /**
     * Connects to the server then enters the processing loop.
     */
    private void run() throws IOException {

        // Make connection and initialize streams
        Socket socket = new Socket(<IP ADDRESS>, 8080);
        in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        while (true) {
            String line = in.readLine();
            switch(getFirstWordOfString(line)) {
                case "UPDATE_NAMES":
                    onlinePlayers.addAll(Arrays.asList(line.substring(13).split("\\s*,\\s*")));
                    onlinePlayersTextArea.setText(onlinePlayers.toString());
                    break;
                case "SUBMITNAME":
                    out.println(getName());
                    break;
                case "REMOVE_NAME":
                    onlinePlayers.remove(line.substring(12));
                    onlinePlayersTextArea.setText(onlinePlayers.toString());
                    break;
                case "CHALLANGE":
                    String challanger = line.substring(10);
                    if (JOptionPane.showConfirmDialog(
                            frame,
                            "You got a challange from " + challanger +
                                    ". Do you take up?",
                            "Challange suggestion",
                            YES_NO_OPTION) == YES_OPTION){
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
    public static void main(String[] args) throws Exception {
        GoClient client = new GoClient();
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.run();
    }
}