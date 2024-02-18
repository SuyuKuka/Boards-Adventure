package view;

import model.config.cells.Cell;

public final class CellGraphics {
    private Cell cell;
    private int xPos;
    private int yPos;

    public CellGraphics(final Cell cell, final int xPos, final int yPos) {
        this.cell = cell;
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public Cell getCell() {
        return cell;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

}
