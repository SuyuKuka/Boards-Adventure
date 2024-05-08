package view.game.cellsgraphics;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;
import javax.swing.JLabel;

import controller.Controller;
import controller.resources.Resources;
import view.game.BoardPanel;

/**
 * Classe qui s'occupe de la vue des {@link model.config.cells}. <p>
 * Par défaut, les cellules utilisent cette classe pour être affichée (de forme circulaire).
 * <p> Certaines cellules ont leurs propres classes graphiques.
 */
public class CellGraphics extends JLabel {
    // PAINT
    // Graphiquement, une cellule contient plusieurs couches dans l'ordre :
    // - Le contour
    // - la couleur intérieure -> COLOR_RANGE
    // - l'icône/image -> ICON_RANGE
    private static final int CELL_DIAMETER = 45;
    private static final int COLOR_RANGE = 3;
    private static final Rectangle COLOR_RANGE_DIM = new Rectangle();

    private static final int ICON_RANGE = 8;
    private static final Rectangle ICON_RANGE_DIM = new Rectangle();

    private final BoardPanel boardPanel;
    private final Controller controller;
    private final int cellID;
    private final int xPos;
    private final int yPos;

    public CellGraphics(final BoardPanel boardPanel, final int cellID, final int xPos, final int yPos) {
        this.boardPanel = boardPanel;
        this.controller = boardPanel.getController();
        this.cellID = cellID;
        this.xPos = xPos;
        this.yPos = yPos;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(final MouseEvent e) {
                boardPanel.getFrame().setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(final MouseEvent e) {
                boardPanel.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
            @Override
            public void mousePressed(final MouseEvent e) {
                final int button = e.getButton();
                if (button == MouseEvent.BUTTON3) {
                    boardPanel.dispatchEvent(SwingUtilities.convertMouseEvent(CellGraphics.this, e, boardPanel));
                } else if (button == MouseEvent.BUTTON1) {
                    System.out.println("clique gauche cellule !!!");
                }
            }
            @Override
            public void mouseReleased(final MouseEvent e) {
                boardPanel.getFrame().setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(final MouseEvent e) {
                boardPanel.dispatchEvent(SwingUtilities.convertMouseEvent(CellGraphics.this, e, boardPanel));
            }
        });
        setVisible(true);
    }

    public static final void updatePaintVariables(final double widthRatio, final double heightRatio) {
        COLOR_RANGE_DIM.x = (int) (COLOR_RANGE * widthRatio);
        COLOR_RANGE_DIM.y = (int) (COLOR_RANGE * heightRatio);
        COLOR_RANGE_DIM.width = (int) Math.ceil((CELL_DIAMETER - COLOR_RANGE * 2) * widthRatio);
        COLOR_RANGE_DIM.height = (int) Math.ceil((CELL_DIAMETER - COLOR_RANGE * 2) * heightRatio);

        ICON_RANGE_DIM.x = (int) (ICON_RANGE * widthRatio);
        ICON_RANGE_DIM.y = (int) (ICON_RANGE * heightRatio);
        ICON_RANGE_DIM.width = (int) Math.ceil((CELL_DIAMETER - ICON_RANGE * 2) * widthRatio);
        ICON_RANGE_DIM.height  = (int) Math.ceil((CELL_DIAMETER - ICON_RANGE * 2) * heightRatio);
    }

    public final void update() {
        updateSize();
        updateLocation();
    }

    private void updateSize() {
        setSize((int) (CELL_DIAMETER * boardPanel.getWidthRatio()),
            (int) (CELL_DIAMETER * boardPanel.getHeightRatio()));
    }


    private void updateLocation() {
        final Dimension size = getSize();
        final int cellCamX = xPos - (int) boardPanel.getCameraX() + boardPanel.getCameraWidth() / 2;
        final int centerOffsetX = size.width / 2;
        final int finalX = (int) (cellCamX * boardPanel.getWidthRatio()) - centerOffsetX;

        final int cellCamY = (yPos - (int) boardPanel.getCameraY() + boardPanel.getCameraHeight() / 2);
        final int centerOffsetY = size.height / 2;
        final int finalY = (int) (cellCamY * boardPanel.getHeightRatio()) - centerOffsetY;

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
        // Coutour
        g2d.fillOval(0, 0, getWidth(), getHeight());
        // Couleur intérieure
        g2d.setColor(Color.WHITE);
        g2d.fillOval(COLOR_RANGE_DIM.x, COLOR_RANGE_DIM.y, COLOR_RANGE_DIM.width, COLOR_RANGE_DIM.height);
        // icône
        g2d.drawImage(loadCellImage(), ICON_RANGE_DIM.x, ICON_RANGE_DIM.y,
            ICON_RANGE_DIM.width, ICON_RANGE_DIM.height, this);
        // id cellule
        g2d.setColor(new Color(663399));
        g2d.setFont(Resources.getFont("RifficFree", 12)); //TODO <- size dynamique (ici = 12)
        g2d.drawString(String.valueOf(cellID), getWidth() / 4, getHeight() / 2);
    }

    // region getters/setters

    public final BoardPanel getBoardPanel() {
        return boardPanel;
    }

    public static int getCellDiameter() {
        return CELL_DIAMETER;
    }

    public static Rectangle getColorRangeDim() {
        return COLOR_RANGE_DIM;
    }

    public static Rectangle getIconRangeDim() {
        return ICON_RANGE_DIM;
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
