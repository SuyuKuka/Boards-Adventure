package view.game.cellsgraphics;

import model.config.cells.Cell;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public abstract class CellGraphics extends JPanel {
    private static final int CELL_HEIGHT = 50;
    private static final int CELL_WIDTH = 50;
    private static final int BORDER_THICKNESS = 3;

    private Cell cell;
    private int xPos;
    private int yPos;

    public CellGraphics(final Cell cell, final int xPos, final int yPos) {
        this.cell = cell;
        this.xPos = xPos;
        this.yPos = yPos;
        setLayout(new BorderLayout());
        setBackground(Color.GREEN);
        setSize(CELL_WIDTH, CELL_HEIGHT);
        setBounds(xPos - CELL_WIDTH / 2, yPos - CELL_HEIGHT / 2, CELL_WIDTH, CELL_HEIGHT);
        setVisible(true);
    }

    public abstract void addContent();

    @Override
    public void paint(final Graphics g) {
        super.paint(g);
    }

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

    public static int getBorderThickness() {
        return BORDER_THICKNESS;
    }

}
