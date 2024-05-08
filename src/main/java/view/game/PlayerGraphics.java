package view.game;

import java.util.HashMap;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import controller.Controller;
import controller.resources.Resources;
import view.game.cellsgraphics.CellGraphics;

public final class PlayerGraphics extends JLabel {

    private static final int PLAYER_DIAMETER = CellGraphics.getCellDiameter() / 2;

    private final BoardPanel boardPanel;
    private final Controller controller;
    private final HashMap<Integer, CellGraphics> cellsGraphics;

    private final int playerID;
    private int xPos;
    private int yPos;

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
        setVisible(true);
    }

    public void update() {
        updateSize();
        updatePosition();
    }

    private void updateSize() {
        final int cellDiam = CellGraphics.getCellDiameter();
        final int sizeX = (int) (cellDiam * boardPanel.getWidthRatio()) / 2;
        final int sizeY = (int) (cellDiam * boardPanel.getHeightRatio()) / 2;
        setSize(sizeX, sizeY);
        setIcon(new ImageIcon(playerImage.getScaledInstance(sizeX, sizeY, Image.SCALE_SMOOTH)));
    }

    private void updatePosition() {
        final Dimension size = getSize();
        final int playerCamX = xPos - boardPanel.getCameraX() + boardPanel.getCameraWidth() / 2;
        final int centerOffsetX = size.width / 2;
        final int finalX = (int) (playerCamX * boardPanel.getWidthRatio()) - centerOffsetX;

        final int playerCamY = (yPos - boardPanel.getCameraY() + boardPanel.getCameraHeight() / 2);
        final int centerOffsetY = size.height / 2;
        final int finalY = (int) (playerCamY * boardPanel.getHeightRatio()) - centerOffsetY;

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

    public Image getPlayerImage() {
        return playerImage;
    }

}
