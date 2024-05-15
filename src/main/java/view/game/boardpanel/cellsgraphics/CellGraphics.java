package view.game.boardpanel.cellsgraphics;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;
import javax.swing.JLabel;

import controller.resources.Resources;
import view.View;
import view.game.boardpanel.BoardPanel;

/**
 * Classe ABSTRAIRE qui s'occupe de la vue des {@link model.config.cells}. <p>
 * Par défaut, les cellules du jeu sont invoquées par la classe {@link DefaultCellGraphics}.
 */
public abstract class CellGraphics extends JLabel {
    // PAINT
    // Graphiquement, une cellule contient plusieurs couches dans l'ordre :
    // - Le contour
    // - la couleur intérieure -> COLOR_RANGE
    // - l'icône/image -> ICON_RANGE
    private static final int CELL_DIAMETER = 45; // si pair faire - 1
    private static final int COLOR_RANGE = 3;
    // (CELL_DIAMETER - CELL_DIAMETER * START_PAINT_MULT) / 2 = START PAINT si pair faire + 1
    private static final double START_COLOR_PAINT_MULTIPLIER = 0.85;
    private static final Rectangle COLOR_RANGE_DIM = new Rectangle();

    private static final double START_ICON_PAINT_MULTIPLIER = 0.65; // si pair faire + 1
    private static final int ICON_RANGE = 8;
    private static final Rectangle ICON_RANGE_DIM = new Rectangle();

    private final BoardPanel boardPanel;
    private final int cellID;
    private final Image cellImage;
    private final int xPos;
    private final int yPos;

    public CellGraphics(final BoardPanel boardPanel, final int cellID, final int xPos, final int yPos) {
        this.boardPanel = boardPanel;
        this.cellID = cellID;
        this.cellImage = Resources.getImage(boardPanel.getController()
            .getCell(cellID).getCellType().name().toLowerCase());
        this.xPos = xPos;
        this.yPos = yPos;
        final View frame = boardPanel.getFrame();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(final MouseEvent e) {
                frame.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(final MouseEvent e) {
                frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
            @Override
            public void mouseReleased(final MouseEvent e) {
                frame.setCursor(new Cursor(Cursor.HAND_CURSOR));
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

    public abstract void paintComponent(Graphics g);

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

    // endregion
}
