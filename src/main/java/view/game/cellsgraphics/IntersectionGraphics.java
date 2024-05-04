package view.game.cellsgraphics;

import java.awt.Graphics;

import view.game.BoardPanel;

public final class IntersectionGraphics extends CellGraphics {

    public IntersectionGraphics(final BoardPanel boardPanel, final int cellID, final int xPos, final int yPos) {
        super(boardPanel, cellID, xPos, yPos);
    }

    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
    }

}
