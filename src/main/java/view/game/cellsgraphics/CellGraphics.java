package view.game.cellsgraphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.JLabel;

import controller.Controller;
import controller.resources.Resources;
import view.game.GamePanel;

/**
 * Classe qui s'occupe de la vue des {@link model.config.cells}. <p>
 * Par défaut, les cellules utilisent cette classe pour être affichée (de forme circulaire).
 * <p> Certaines cellules ont leurs propres classes graphiques.
 */
public class CellGraphics extends JLabel {
    private static final int CELL_DIAMETER = 65;
    // Graphiquement, une cellule contient plusieurs couches dans l'ordre :
    // - Le contour
    // - la couleur intérieure -> COLOR_RANGE
    // - l'icône/image -> ICON_RANGE
    private static final int COLOR_RANGE = 3;
    private static final int ICON_RANGE = 8;

    private final GamePanel gamePanel;
    private final Controller controller;
    private final int cellID;
    private final int xPos;
    private final int yPos;

    public CellGraphics(final GamePanel gamePanel, final int cellID, final int xPos, final int yPos) {
        this.gamePanel = gamePanel;
        this.controller = gamePanel.getController();
        this.cellID = cellID;
        this.xPos = xPos;
        this.yPos = yPos;
        update();
        setVisible(true);
    }

    public final void update() {
        updateSize();
        updateLocation();
    }

    private void updateSize() {
        final double zoom = gamePanel.getZoom();
        setSize((int) (CELL_DIAMETER * zoom), (int) (CELL_DIAMETER * zoom));
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

    /**
     * Méthode qui peint les cellules.
     */
    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        final double zoom = getGamePanel().getZoom();

        // Coutour
        g2d.fillOval(0, 0, getWidth(), getHeight());
        // Couleur intérieure
        g2d.setColor(Color.WHITE);
        final int startInside = (int) (COLOR_RANGE * zoom);
        final int endInside = (int) Math.ceil(((CELL_DIAMETER - COLOR_RANGE * 2) * zoom));
        g2d.fillOval(startInside, startInside, endInside, endInside);
        // icône
        final int startIcon = (int) (ICON_RANGE * zoom);
        final int endIcon = (int) Math.ceil((CELL_DIAMETER - ICON_RANGE * 2) * zoom);
        g2d.drawImage(loadCellImage(), startIcon, startIcon, endIcon, endIcon, this);
        // id cellule
        g2d.setColor(new Color(663399));
        g2d.setFont(Resources.getFont("RifficFree", 12)); //TODO <- size dynamique (ici =12)
        g2d.drawString(String.valueOf(cellID), getWidth() / 4, getHeight() / 2);
    }

    // region getters/setters

    public final GamePanel getGamePanel() {
        return gamePanel;
    }

    public static int getCellDiameter() {
        return CELL_DIAMETER;
    }

    public static int getColorRange() {
        return COLOR_RANGE;
    }

    public static int getIconRange() {
        return ICON_RANGE;
    }

    public final Controller getController() {
        return controller;
    }

    public final int getCellID() {
        return cellID;
    }

    public final Image loadCellImage() {
        return Resources.getImage(controller.getCell(cellID).getCellType().name().toLowerCase());
    }

    public final int getXPos() {
        return xPos;
    }

    public final int getYPos() {
        return yPos;
    }

    // endregion
}
