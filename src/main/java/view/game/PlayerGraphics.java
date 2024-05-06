package view.game;

import java.util.HashMap;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import controller.Controller;
import controller.resources.Resources;
import model.config.players.Player;
import view.game.cellsgraphics.CellGraphics;

public final class PlayerGraphics extends JLabel {

    private static final int PLAYER_DIAMETER = CellGraphics.getCellDiameter() / 2;

    private final BoardPanel boardPanel;
    private final Controller controller;
    private final HashMap<Integer, CellGraphics> cellsGraphics;

    private final int playerID;
    private int xPos;
    private int yPos;

    private Player playedr;
    // TODO : RETIRER player de PlayersGraphics
    private Image playerImage;


    public PlayerGraphics(final int playerID, final BoardPanel boardPanel) {
        this.boardPanel = boardPanel;
        this.controller = boardPanel.getController();
        this.playerID = playerID;
        this.cellsGraphics = boardPanel.getCellsGraphics();
        final CellGraphics cellGraphics = cellsGraphics.get(controller.getPlayer(playerID).getPosition());
        xPos = cellGraphics.getXPos();
        yPos = cellGraphics.getYPos();
        playerImage = Resources.getImage("j" + playerID);
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
        final double zoom = boardPanel.getZoom();
        final int sizeX = (int) (PLAYER_DIAMETER * zoom);
        final int sizeY = (int) (PLAYER_DIAMETER * zoom);
        setSize(sizeX, sizeY);
        setIcon(new ImageIcon(playerImage.getScaledInstance(sizeX, sizeY, Image.SCALE_SMOOTH)));
    }

    private void updatePosition() {
        final int camWidth = boardPanel.getCameraWidth();
        final int cellCamX = xPos - boardPanel.getCameraX() + camWidth / 2;
        final double screenRatioX = 1.0 * boardPanel.getWidth() / camWidth;
        final int centerOffsetX = PLAYER_DIAMETER / 2 * BoardPanel.getCameraMaxWidth() / camWidth;
        final int finalX = (int) (cellCamX * screenRatioX) - centerOffsetX;

        final int camHeight = boardPanel.getCameraHeight();
        final int cellCamY = (yPos - boardPanel.getCameraY() + camHeight / 2);
        final double screenRatioY = 1.0 * boardPanel.getHeight() / camHeight;
        final int centerOffsetY = PLAYER_DIAMETER / 2 * BoardPanel.getCameraMaxHeight() / camHeight;
        final int finalY = (int) (cellCamY * screenRatioY) - centerOffsetY;

        setLocation(finalX, finalY);
    }

    public void updateCoordinates() {
        final CellGraphics cellGraphics = cellsGraphics.get(controller.getPlayer(playerID).getPosition());
        xPos = cellGraphics.getXPos();
        yPos = cellGraphics.getYPos();
    }

    public int getPlayerID() {
        return playerID;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

}
