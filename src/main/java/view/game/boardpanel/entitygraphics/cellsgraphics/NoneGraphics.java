package view.game.boardpanel.entitygraphics.cellsgraphics;

import java.awt.Graphics;

import view.game.boardpanel.BoardPanel;

public final class NoneGraphics extends CellGraphics {

    private final boolean shouldPlayerTeleport;

    public NoneGraphics(final BoardPanel boardPanel, final int cellID, final int xPos, final int yPos,
            final boolean shouldPlayerTeleport) {
        super(boardPanel, cellID, xPos, yPos);
        this.shouldPlayerTeleport = shouldPlayerTeleport;
    }

    @Override
    public void paintComponent(Graphics g) {
        return;
    }

    public boolean shouldPlayerTeleport() {
        return shouldPlayerTeleport;
    }

}
