package view;

import javax.swing.JLayeredPane;

public class GamePanel extends JLayeredPane {

    private MonopolyView frame;
    public GamePanel(final MonopolyView frame) {
        this.frame = frame;
        setBounds(0, 0, frame.getWidth(), frame.getHeight());
        repaint();
        setVisible(true);
    }
}
