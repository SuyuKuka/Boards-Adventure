package view.game;

import java.util.HashMap;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import controller.resources.Resources;
import model.config.players.Player;
import view.game.cellsgraphics.CellGraphics;

public final class PlayerGraphics extends JLabel {

    private static final int PLAYER_DIAMETER = CellGraphics.getCellDiameter() / 2;

    private final GamePanel gamePanel;
    private final HashMap<Integer, CellGraphics> cellsGraphics;

    private int xPos;
    private int yPos;

    private Player player;
    // TODO : RETIRER player de PlayersGraphics
    private Image playerImage;


    public PlayerGraphics(final Player player, final GamePanel gamePanel) {
        this.player = player;
        this.gamePanel = gamePanel;
        this.cellsGraphics = gamePanel.getCellsGraphics();
        final CellGraphics cellGraphics = cellsGraphics.get(player.getPosition());
        xPos = cellGraphics.getXPos();
        yPos = cellGraphics.getYPos();
        playerImage = Resources.getImage("j" + player.getPlayerID());
        setHorizontalAlignment(JLabel.CENTER);
        setVerticalAlignment(JLabel.CENTER);
        update();
        setVisible(true);
    }

    public void update() {
        updateSize();
        updatePosition();
    }

    private void updateSize() {
        final double zoom = gamePanel.getZoom();
        final int sizeX = (int) (gamePanel.getWidth() / GamePanel.getCameraMaxWidth() * PLAYER_DIAMETER * zoom);
        final int sizeY = (int) (gamePanel.getHeight() / GamePanel.getCameraMaxHeight() * PLAYER_DIAMETER * zoom);
        setSize(sizeX, sizeY);
        setIcon(new ImageIcon(playerImage.getScaledInstance(sizeX, sizeY, Image.SCALE_SMOOTH)));
    }

    private void updatePosition() {
        final int camWidth = gamePanel.getCameraWidth();
        final int cellCamX = xPos - gamePanel.getCameraX() + camWidth / 2;
        final double screenRatioX = 1.0 * gamePanel.getWidth() / camWidth;
        final int centerOffsetX = PLAYER_DIAMETER / 2 * GamePanel.getCameraMaxWidth() / camWidth;
        final int finalX = (int) (cellCamX * screenRatioX) - centerOffsetX;

        final int camHeight = gamePanel.getCameraHeight();
        final int cellCamY = (yPos - gamePanel.getCameraY() + camHeight / 2);
        final double screenRatioY = 1.0 * gamePanel.getHeight() / camHeight;
        final int centerOffsetY = PLAYER_DIAMETER / 2 * GamePanel.getCameraMaxHeight() / camHeight;
        final int finalY = (int) (cellCamY * screenRatioY) - centerOffsetY;

        setLocation(finalX, finalY);
    }

    public void updateCoordinates() {
        final CellGraphics cellGraphics = cellsGraphics.get(player.getPosition());
        xPos = cellGraphics.getXPos();
        yPos = cellGraphics.getYPos();
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

}
