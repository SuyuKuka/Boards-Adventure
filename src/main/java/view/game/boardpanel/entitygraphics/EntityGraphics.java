package view.game.boardpanel.entitygraphics;

import java.awt.Dimension;

import javax.swing.JLabel;

import view.game.boardpanel.BoardPanel;

public abstract class EntityGraphics extends JLabel {

    private final BoardPanel boardPanel;
    private int xPos;
    private int yPos;

    public EntityGraphics(final BoardPanel boardPanel, final int xPos, final int yPos) {
        this.boardPanel = boardPanel;
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public final void update() {
        updateSize();
        updatePosition();
    }

    public abstract void updateSize();

    private void updatePosition() {
        final Dimension size = getSize();
        final int playerCamX = xPos - (int) boardPanel.getCameraX() + boardPanel.getCameraWidth() / 2;
        final int centerOffsetX = size.width / 2;
        final int finalX = (int) (playerCamX * boardPanel.getWidthRatio()) - centerOffsetX;

        final int playerCamY = (yPos - (int) boardPanel.getCameraY() + boardPanel.getCameraHeight() / 2);
        final int centerOffsetY = size.height / 2;
        final int finalY = (int) (playerCamY * boardPanel.getHeightRatio()) - centerOffsetY;

        setLocation(finalX, finalY);
    }

    public final BoardPanel getBoardPanel() {
        return boardPanel;
    }

    public final void updateCoordinates(final int nextXPos, final int nextYPos) {
        xPos = nextXPos;
        yPos = nextYPos;
    }

    public final int getXPos() {
        return xPos;
    }

    public final int getYPos() {
        return yPos;
    }

}
