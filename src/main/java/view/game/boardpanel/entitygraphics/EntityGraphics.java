package view.game.boardpanel.entitygraphics;

import java.awt.Dimension;

import javax.swing.JLabel;

import view.game.boardpanel.BoardPanel;

public abstract class EntityGraphics extends JLabel {

    private final BoardPanel boardPanel;
    private double xPos;
    private double yPos;

    public EntityGraphics(final BoardPanel boardPanel, final double xPos, final double yPos) {
        this.boardPanel = boardPanel;
        this.xPos = xPos;
        this.yPos = yPos;
    }

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
        final int playerCamX = (int) (xPos - (int) boardPanel.getCameraX() + boardPanel.getCameraWidth() / 2);
        final int centerOffsetX = size.width / 2;
        final int finalX = (int) (playerCamX * boardPanel.getWidthRatio()) - centerOffsetX;

        final int playerCamY = (int) (yPos - (int) boardPanel.getCameraY() + boardPanel.getCameraHeight() / 2);
        final int centerOffsetY = size.height / 2;
        final int finalY = (int) (playerCamY * boardPanel.getHeightRatio()) - centerOffsetY;

        setLocation(finalX, finalY);
    }

    public final void updateCoordinates(final int nextXPos, final int nextYPos) {
        xPos = nextXPos;
        yPos = nextYPos;
        update();
    }

    public final BoardPanel getBoardPanel() {
        return boardPanel;
    }

    public final void setXPos(final int nextXPos) {
        this.xPos = nextXPos;
    }

    public final void setYPos(final int nextYPos) {
        this.yPos = nextYPos;
    }

    public final int getXPos() {
        return (int) xPos;
    }

    public final int getYPos() {
        return (int) yPos;
    }

    public final void setXPos(final double nextXPos) {
        this.xPos = nextXPos;
    }

    public final void setYPos(final double nextYPos) {
        this.yPos = nextYPos;
    }

    public final double getDXPos() {
        return xPos;
    }

    public final double getDYPos() {
        return yPos;
    }

}
