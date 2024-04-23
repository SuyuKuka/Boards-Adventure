package view.game;

import java.util.HashMap;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import controller.resources.Resources;
import model.config.players.Player;
import view.game.cellsgraphics.CellGraphics;

public final class PlayerGraphics extends JLabel {

    private static final int PLAYER_WIDTH = 14;
    private static final int PLAYER_HEIGHT = 14;

    private final HashMap<Integer, CellGraphics> cellsGraphics;

    private int xPos;
    private int yPos;

    private Player player;
    // TODO : RETIRER player de PlayersGraphics
    private Image playerImage;


    public PlayerGraphics(final Player player, final HashMap<Integer, CellGraphics> cellsGraphics) {
        this.player = player;
        this.cellsGraphics = cellsGraphics;
        final CellGraphics cellGraphics = cellsGraphics.get(player.getPosition());
        xPos = cellGraphics.getXPos();
        yPos = cellGraphics.getYPos();
        playerImage = Resources.getImage("j" + player.getPlayerID());
        setHorizontalAlignment(JLabel.CENTER);
        setVerticalAlignment(JLabel.CENTER);
        setBounds(0, 0, PLAYER_WIDTH, PLAYER_HEIGHT);
        setIcon(new ImageIcon(playerImage.getScaledInstance(PLAYER_WIDTH, PLAYER_HEIGHT, Image.SCALE_SMOOTH)));
        setLocation(xPos - CellGraphics.getCellWidth() / 4, yPos - CellGraphics.getCellHeight() / 4);
        setVisible(true);
    }

    public void updatePosition() {
        final CellGraphics cellGraphics = cellsGraphics.get(player.getPosition());
        setLocation(cellGraphics.getXPos() - CellGraphics.getCellWidth() / 4,
            cellGraphics.getYPos() - CellGraphics.getCellHeight() / 4);
    }
}
