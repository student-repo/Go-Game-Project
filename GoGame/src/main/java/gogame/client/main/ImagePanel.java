package gogame.client.main;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {

    Image image;

    public void setBackground(Image image) {
        this.image = image;
    }

    @Override
    public void paintComponent(Graphics G) {
        super.paintComponent(G);
        G.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);
        repaint();
    }

}
