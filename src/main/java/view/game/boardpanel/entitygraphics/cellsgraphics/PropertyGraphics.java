package view.game.boardpanel.entitygraphics.cellsgraphics;

import model.config.cells.Property;
import view.game.boardpanel.BoardPanel;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import java.awt.Color;

public final class PropertyGraphics extends CellGraphics {

    private static final int ARC_WIDTH = 5;
    private static final int ARC_HEIGHT = 5;
    private final Color color;

    public PropertyGraphics(final BoardPanel boardPanel, final int cellID, final int xPos, final int yPos) {
        super(boardPanel, cellID, xPos, yPos);
        this.color = Color.decode(((Property) getBoardPanel().getController().getCell(cellID)).getGroupHexColor());
    }

    @Override
    public void paintComponent(final Graphics g) {
        final Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        final int cellID = getCellID();
        final Property cell = (Property) getBoardPanel().getController().getCell(cellID);

        // Contour
        g2d.setColor(color);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), ARC_WIDTH, ARC_HEIGHT);
        // Couleur intérieure
        if (cell.getOwner() == null) {
            g2d.setColor(Color.WHITE);
        } else {
            g2d.setColor(Color.decode(cell.getOwner().getHexTeamColor()));
        }
        final Rectangle colorRangeDim = CellGraphics.getColorRangeDim();
        g2d.fillRoundRect(colorRangeDim.x, colorRangeDim.y, colorRangeDim.width, colorRangeDim.height,
            ARC_WIDTH, ARC_HEIGHT);
        // icône
        final Rectangle iconRangeDim = CellGraphics.getIconRangeDim();
        g2d.drawImage(getCellImage(), iconRangeDim.x, iconRangeDim.y, iconRangeDim.width, iconRangeDim.height, this);
    }

    public Color getColor() {
        return color;
    }

}
