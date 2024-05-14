package view.game.boardpanel;

import java.awt.Dimension;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import controller.Controller;
import controller.resources.Resources;
import view.game.boardpanel.cellsgraphics.CellGraphics;

public final class PlayerGraphics extends JLabel {

    private final BoardPanel boardPanel;
    private final Controller controller;

    private final int playerID;
    private int xPos;
    private int yPos;

    private Image playerImage;


    public PlayerGraphics(final int playerID, final BoardPanel boardPanel, final int xPos, final int yPos) {
        this.boardPanel = boardPanel;
        this.controller = boardPanel.getController();
        this.playerID = playerID;
        this.xPos = xPos;
        this.yPos = yPos;
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
        final int playerCamX = xPos - (int) boardPanel.getCameraX() + boardPanel.getCameraWidth() / 2;
        final int centerOffsetX = size.width / 2;
        final int finalX = (int) (playerCamX * boardPanel.getWidthRatio()) - centerOffsetX;

        final int playerCamY = (yPos - (int) boardPanel.getCameraY() + boardPanel.getCameraHeight() / 2);
        final int centerOffsetY = size.height / 2;
        final int finalY = (int) (playerCamY * boardPanel.getHeightRatio()) - centerOffsetY;

        setLocation(finalX, finalY);
    }

    public void updateCoordinates(final int nextXPos, final int nextYPos) {
        xPos = nextXPos;
        yPos = nextYPos;
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
