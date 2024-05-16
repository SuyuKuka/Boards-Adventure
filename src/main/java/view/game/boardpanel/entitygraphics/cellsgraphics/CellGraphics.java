package view.game.boardpanel.entitygraphics.cellsgraphics;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import controller.resources.Resources;
import view.View;
import view.game.boardpanel.BoardPanel;
import view.game.boardpanel.entitygraphics.EntityGraphics;

/**
 * Classe ABSTRAIRE qui s'occupe de la vue des {@link model.config.cells}. <p>
 * Par défaut, les cellules du jeu sont invoquées par la classe {@link DefaultCellGraphics}.
 */
public abstract class CellGraphics extends EntityGraphics {
    // PAINT
    // Graphiquement, une cellule contient plusieurs couches dans l'ordre :
    // - Le contour
    // - la couleur intérieure -> colorRange
    // - l'icône/image -> iconRange
    private static int cellDiameter;
    private static final double START_COLOR_PAINT_MULTIPLIER = 0.85;
    private static int colorRange;
    private static final Rectangle COLOR_RANGE_DIM = new Rectangle();

    private static final double START_ICON_PAINT_MULTIPLIER = 0.65;
    private static int iconRange;
    private static final Rectangle ICON_RANGE_DIM = new Rectangle();

    private final int cellID;
    private final Image cellImage;

    public CellGraphics(final BoardPanel boardPanel, final int cellID, final int xPos, final int yPos) {
        super(boardPanel, xPos, yPos);
        this.cellID = cellID;
        this.cellImage = Resources.getImage(boardPanel.getController()
            .getCell(cellID).getCellType().name().toLowerCase());
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
        COLOR_RANGE_DIM.x = (int) (colorRange * widthRatio);
        COLOR_RANGE_DIM.y = (int) (colorRange * heightRatio);
        COLOR_RANGE_DIM.width = (int) Math.ceil((cellDiameter - colorRange * 2) * widthRatio);
        COLOR_RANGE_DIM.height = (int) Math.ceil((cellDiameter - colorRange * 2) * heightRatio);

        ICON_RANGE_DIM.x = (int) (iconRange * widthRatio);
        ICON_RANGE_DIM.y = (int) (iconRange * heightRatio);
        ICON_RANGE_DIM.width = (int) Math.ceil((cellDiameter - iconRange * 2) * widthRatio);
        ICON_RANGE_DIM.height  = (int) Math.ceil((cellDiameter - iconRange * 2) * heightRatio);
    }

    public final void updateSize() {
        final BoardPanel boardPanel = getBoardPanel();
        setSize((int) (cellDiameter * boardPanel.getWidthRatio()),
            (int) (cellDiameter * boardPanel.getHeightRatio()));
    }

    public abstract void paintComponent(Graphics g);

    public static void updateCellSize(final int cellDia) {
        cellDiameter = cellDia;
        if (cellDiameter % 2 == 0) {
            cellDiameter--;
        }
        colorRange = (int) (cellDiameter * START_COLOR_PAINT_MULTIPLIER);
        if (colorRange % 2 == 0) {
            colorRange++;
        }
        colorRange = (cellDiameter - colorRange) / 2;
        iconRange = (int) (cellDiameter * START_ICON_PAINT_MULTIPLIER);
        if (iconRange % 2 == 0) {
            iconRange++;
        }
        iconRange = (cellDiameter - iconRange) / 2;
    }

    // region getters/setters

    public static int getCellDiameter() {
        return cellDiameter;
    }

    public static Rectangle getColorRangeDim() {
        return COLOR_RANGE_DIM;
    }

    public static Rectangle getIconRangeDim() {
        return ICON_RANGE_DIM;
    }

    public static int getColorRange() {
        return colorRange;
    }

    public static int getIconRange() {
        return iconRange;
    }

    public final int getCellID() {
        return cellID;
    }

    public final Image getCellImage() {
        return cellImage;
    }

    // endregion
}
