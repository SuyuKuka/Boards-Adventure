package view.game;

import java.util.HashMap;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import controller.resources.Resources;
import model.config.players.Player;
import view.game.cellsgraphics.CellGraphics;

public final class PlayerGraphics extends JLabel {

    private static final int PLAYER_DIAMETER = 32;

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
        final int cellDia = CellGraphics.getCellDiameter();
        final double zoom = gamePanel.getZoom();
        final double configToScreenRatioW = 1.0 * gamePanel.getWidth() / gamePanel.getCameraWidth();
        final double configToScreenRatioH = 1.0 * gamePanel.getHeight() / gamePanel.getCameraHeight();

        final int cameraX = (int) (gamePanel.getCameraX() - GamePanel.getCameraMaxWidth() / 2 * zoom);
        final int cameraY = (int) (gamePanel.getCameraY() - GamePanel.getCameraMaxHeight() / 2 * zoom);

        final int posCamX = (int) ((configToScreenRatioW * (xPos - cameraX)) - cellDia / 2 * zoom);
        final int posCamY = (int) ((configToScreenRatioH * (yPos - cameraY)) - cellDia / 2 * zoom);

        setLocation(posCamX + cellDia / 4, posCamY + cellDia / 4);
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
