package view.game.boardpanel.entitygraphics;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JLabel;

import controller.resources.Resources;
import view.SwingFactory;
import view.game.boardpanel.BoardPanel;

public final class TargetGraphics extends EntityGraphics {

    private static final Font FONT = Resources.getFont(32);

    private final EntityGraphics targetEntity;
    private final Image image;
    private String labelText;
    private final double xRatio;
    private final int xOffset;
    private final double yRatio;
    private final int yOffset;

    public TargetGraphics(final BoardPanel boardPanel, final EntityGraphics targetEntity, final Image image,
            final String labelText, final int xOffset, final int yOffset, final double xRatio, final double yRatio) {
        super(boardPanel, targetEntity.getXPos() + xOffset, targetEntity.getYPos() + yOffset);
        this.targetEntity = targetEntity;
        this.image = image;
        this.labelText = labelText;
        this.xRatio = xRatio;
        this.xOffset = xOffset;
        this.yRatio = yRatio;
        this.yOffset = yOffset;
        setHorizontalAlignment(JLabel.CENTER);
        setVerticalAlignment(JLabel.CENTER);
        setVisible(true);
    }

    /**
     * {@link TargetGraphics} avec des paramètres par défaut pour suivre le {@link Player} courant.
     * @param boardPanel
     * @param player
     * @param currentPlayerDiceRollValue
     */
    public TargetGraphics(final BoardPanel boardPanel, final PlayerGraphics player,
            final String currentPlayerDiceRollValue) {
        this(boardPanel, player, null,
            currentPlayerDiceRollValue, 0, -30, 0.6, 0.6);
    }

    public void updateMoving() {
        setXPos(targetEntity.getXPos() + xOffset);
        setYPos(targetEntity.getYPos() + yOffset);
        revalidate();
        repaint();
    }

    @Override
    public void updateSize() {
        final BoardPanel boardPanel = getBoardPanel();
        setSize(new Dimension((int) (targetEntity.getWidth() * xRatio * boardPanel.getWidthRatio()),
            (int) (targetEntity.getHeight() * yRatio * boardPanel.getHeightRatio())));
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Dimension dim = getSize();
        if (image != null) {
            g.drawImage(image, 0, 0, dim.width, dim.height, this);
        }
        final Font font = SwingFactory.getResizedFont(this, labelText, FONT);
        final FontMetrics metrics = g.getFontMetrics(font);
        final int x = (dim.width - metrics.stringWidth(labelText)) / 2;
        final int y = ((dim.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g.setFont(font);
        g.drawString(labelText, x, y);
    }

    public void setLabelText(final String labelText) {
        this.labelText = labelText;
    }

}
