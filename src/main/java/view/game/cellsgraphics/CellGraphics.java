package view.game.cellsgraphics;

import java.awt.Image;

import javax.swing.JLabel;

import controller.Controller;
import controller.resources.Resources;
import view.game.GamePanel;

public abstract class CellGraphics extends JLabel {
    private static final int CELL_DIAMETER = 65;

    private final GamePanel gamePanel;
    private final Controller controller;
    private final int cellID;
    private final int xPos;
    private final int yPos;

    private Image cellImage;

    public CellGraphics(final GamePanel gamePanel, final int cellID, final int xPos, final int yPos) {
        this.gamePanel = gamePanel;
        this.controller = gamePanel.getController();
        this.cellID = cellID;
        this.xPos = xPos;
        this.yPos = yPos;
        cellImage = Resources.getImage(controller.getCell(cellID).getCellType().name().toLowerCase());
        update();
        setVisible(true);
    }

    public final void update() {
        updateSize();
        updateLocation();
    }

    private void updateSize() {
        final double zoom = gamePanel.getZoom();
        setSize((int) (gamePanel.getWidth() / GamePanel.getCameraMaxWidth() * CELL_DIAMETER * zoom),
            (int) (gamePanel.getHeight() / GamePanel.getCameraMaxHeight() * CELL_DIAMETER * zoom));
        // System.out.println(getSize());
    }


    private void updateLocation() {
        final int camWidth = gamePanel.getCameraWidth();
        final int cellCamX = xPos - gamePanel.getCameraX() + camWidth / 2;
        final double screenRatioX = 1.0 * gamePanel.getWidth() / camWidth;
        final int centerOffsetX = CELL_DIAMETER / 2 * GamePanel.getCameraMaxWidth() / camWidth;
        final int finalX = (int) (cellCamX * screenRatioX) - centerOffsetX;

        final int camHeight = gamePanel.getCameraHeight();
        final int cellCamY = (yPos - gamePanel.getCameraY() + camHeight / 2);
        final double screenRatioY = 1.0 * gamePanel.getHeight() / camHeight;
        final int centerOffsetY = CELL_DIAMETER / 2 * GamePanel.getCameraMaxHeight() / camHeight;
        final int finalY = (int) (cellCamY * screenRatioY) - centerOffsetY;

        setLocation(finalX, finalY);
    }

    public final GamePanel getGamePanel() {
        return gamePanel;
    }

    public static int getCellDiameter() {
        return CELL_DIAMETER;
    }

    public final Controller getController() {
        return controller;
    }

    public final int getCellID() {
        return cellID;
    }

    public final Image getCellImage() {
        return cellImage;
    }

    public final int getXPos() {
        return xPos;
    }

    public final int getYPos() {
        return yPos;
    }
}
