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

    private static final Font FONT = Resources.getFont("NoitaBlackletter", 22);

    private final EntityGraphics targetEntity;
    private final Image image;
    private String text;
    private final double xRatio;
    private final int xOffset;
    private final double yRatio;
    private final int yOffset;

    public TargetGraphics(final BoardPanel boardPanel, final EntityGraphics targetEntity, final Image image,
            final String text, final int xOffset, final int yOffset, final double xRatio, final double yRatio) {
        super(boardPanel, targetEntity.getXPos() + xOffset, targetEntity.getYPos() + yOffset);
        this.targetEntity = targetEntity;
        this.image = image;
        this.text = text;
        this.xRatio = xRatio;
        this.xOffset = xOffset;
        this.yRatio = yRatio;
        this.yOffset = yOffset;
        setHorizontalAlignment(JLabel.CENTER);
        setVerticalAlignment(JLabel.CENTER);
        setVisible(true);
    }

    public TargetGraphics(final BoardPanel boardPanel, final PlayerGraphics player,
            final String currentPlayerDiceRollValue) {
        this(boardPanel, player, Resources.getImage("placeholder"),
            currentPlayerDiceRollValue, 0, -30, 0.6, 0.6);
    }

    public void updateMoving(final String diceRollValue) {
        setXPos(targetEntity.getXPos() + xOffset);
        setYPos(targetEntity.getYPos() + yOffset);
        if (text != diceRollValue) {
            text = diceRollValue;
        }
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
        // g.drawImage(image, 0, 0, dim.width, dim.height, this);
        final Font font = SwingFactory.getResizedFont(this, text, FONT);
        final FontMetrics metrics = g.getFontMetrics(font);
        final int x = (dim.width - metrics.stringWidth(text)) / 2;
        final int y = ((dim.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g.setFont(font);
        g.drawString(text, x, y);
    }

}
