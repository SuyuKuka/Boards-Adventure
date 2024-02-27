package view.game.cellsgraphics;

import model.config.cells.Cell;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public abstract class CellGraphics extends JPanel {
    private Cell cell;
    private int xPos;
    private int yPos;

    private static final int CELL_HEIGHT = 25;
    private static final int CELL_WIDTH = 25;

    public CellGraphics(final Cell cell, final int xPos, final int yPos) {
        this.cell = cell;
        this.xPos = xPos;
        this.yPos = yPos;
        setLayout(new BorderLayout());
        setOpaque(false);
        setSize(CELL_WIDTH, CELL_HEIGHT);
        setBounds(xPos - CELL_WIDTH / 2, yPos - CELL_HEIGHT / 2, CELL_WIDTH, CELL_HEIGHT);
        addContent();
        setVisible(true);
    }

    public abstract void addContent();

    public final Cell getCell() {
        return cell;
    }

    public final int getXPos() {
        return xPos;
    }

    public final int getYPos() {
        return yPos;
    }

    public static int getCellWidth() {
        return CELL_WIDTH;
    }

    public static int getCellHeight() {
        return CELL_HEIGHT;
    }

}
