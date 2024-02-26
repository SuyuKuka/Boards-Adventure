package view.game;

import java.awt.Image;

import javax.swing.JLabel;

public class PlayerGraphics extends JLabel {

    private Image playerImage;

    public PlayerGraphics(final Image playerImage) {
        this.playerImage = playerImage;
    }
}
