package view.game;

import java.util.HashMap;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import controller.resources.Resources;
import model.config.players.Player;
import view.game.cellsgraphics.CellGraphics;

public final class PlayerGraphics extends JLabel {

    private static final int PLAYER_DEFAULT_WIDTH = 14;
    private static final int PLAYER_DEFAULT_HEIGHT = 14;

    private final HashMap<Integer, CellGraphics> cellsGraphics;

    private int playerWidth;
    private int playerHeight;
    private int xPos;
    private int yPos;

    private Player player;
    // TODO : RETIRER player de PlayersGraphics
    private Image playerImage;


    public PlayerGraphics(final Player player, final HashMap<Integer, CellGraphics> cellsGraphics) {
        this.player = player;
        this.cellsGraphics = cellsGraphics;
        playerWidth = PLAYER_DEFAULT_WIDTH;
        playerHeight = PLAYER_DEFAULT_HEIGHT;
        final CellGraphics cellGraphics = cellsGraphics.get(player.getPosition());
        xPos = cellGraphics.getXPos();
        yPos = cellGraphics.getYPos();
        playerImage = Resources.getImage("j" + player.getPlayerID());
        setHorizontalAlignment(JLabel.CENTER);
        setVerticalAlignment(JLabel.CENTER);
        setBounds(0, 0, playerWidth, playerHeight);
        setIcon(new ImageIcon(playerImage.getScaledInstance(playerWidth, playerHeight, Image.SCALE_SMOOTH)));
        setLocation(xPos - cellGraphics.getCellWidth() / 4, yPos - cellGraphics.getCellHeight() / 4);
        setVisible(true);
    }

    public void update(final double zoomScaleX, final double zoomScaleY) {
        playerWidth = (int) (PLAYER_DEFAULT_WIDTH * zoomScaleX);
        playerHeight = (int) (PLAYER_DEFAULT_HEIGHT * zoomScaleY);
        final CellGraphics cellGraphics = cellsGraphics.get(player.getPosition());
        xPos = cellGraphics.getXPos();
        yPos = cellGraphics.getYPos();
        setBounds(0, 0, playerWidth, playerHeight);
        setLocation(xPos - cellGraphics.getCellWidth() / 4, yPos - cellGraphics.getCellHeight() / 4);
        setIcon(new ImageIcon(playerImage.getScaledInstance(playerWidth, playerHeight, Image.SCALE_SMOOTH)));
    }

    public void updatePosition() {
        final CellGraphics cellGraphics = cellsGraphics.get(player.getPosition());
        setLocation(cellGraphics.getXPos() - cellGraphics.getCellWidth() / 4,
            cellGraphics.getYPos() - cellGraphics.getCellHeight() / 4);
    }
}
