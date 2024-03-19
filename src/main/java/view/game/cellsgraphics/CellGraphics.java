package view.game.cellsgraphics;

import model.config.cells.Cell;

import java.awt.Image;
import java.awt.BorderLayout;

import javax.swing.JPanel;

import controller.resources.Resources;

public abstract class CellGraphics extends JPanel {
    private static final int CELL_DEFAULT_HEIGHT = 24;
    private static final int CELL_DEFAULT_WIDTH = 24;

    private final int defaultXPos;
    private final int defaultYPos;

    private Cell cell;

    private int cellWidth;
    private int cellHeight;
    private int xPos;
    private int yPos;

    private Image image;

    public CellGraphics(final Cell cell, final int defaultXPos, final int defaultYPos) {
        this.cell = cell;
        this.defaultXPos = defaultXPos;
        this.defaultYPos = defaultYPos;
        image = Resources.getImage(cell.getCellType().name().toLowerCase());
        update(1, 1);
        setLayout(new BorderLayout());
        setOpaque(false);
        setVisible(true);
    }

    public final void update(final double zoomScaleX, final double zoomScaleY) {
        cellWidth = (int) (CELL_DEFAULT_WIDTH * zoomScaleX);
        cellHeight = (int) (CELL_DEFAULT_HEIGHT * zoomScaleY);
        xPos = (int) (defaultXPos * zoomScaleX);
        yPos = (int) (defaultYPos * zoomScaleY);
        setSize(cellWidth, cellHeight);
        setBounds(xPos - cellWidth / 2, yPos - cellHeight / 2, cellWidth, cellHeight);
    }

    public final Cell getCell() {
        return cell;
    }

    public final Image getImage() {
        return image;
    }

    public final int getXPos() {
        return xPos;
    }

    public final int getYPos() {
        return yPos;
    }

    public final int getCellWidth() {
        return cellWidth;
    }

    public final int getCellHeight() {
        return cellHeight;
    }
}
