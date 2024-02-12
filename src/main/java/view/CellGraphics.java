package view;

import model.config.cell.Cell;

public class CellGraphics {
    private final Cell cell;
    private final int xPos;
    private final int yPos;

    public CellGraphics(final Cell cell, final int xPos, final int yPos) {
        this.cell = cell;
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }
    
}
