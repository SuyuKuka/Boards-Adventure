package view.game.cellsgraphics;

import model.config.cells.Cell;
import model.config.cells.Property;

public final class CellGraphicsFactory {
    public static CellGraphics createCellGraphics(final Cell cell, final int yPos, final int xPos) {
        if (cell instanceof Property) {
            return createPropertyCell(cell, yPos, xPos);
        }
        return new OvalCell(cell, xPos, yPos);
    }

    private static CellGraphics createPropertyCell(final Cell cell, final int yPos, final int xPos) {
        final RoundedRectCell propertyCell = new RoundedRectCell(cell, xPos, yPos);
        return propertyCell;
    }
}
