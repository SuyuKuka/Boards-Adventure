package view.game.boardpanel.entitygraphics;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JLabel;

import controller.resources.Resources;
import view.game.boardpanel.BoardPanel;

public final class TargetGraphics extends EntityGraphics {

    private final Image image;
    private final EntityGraphics targetEntity;
    private final int xSize;
    private final int xOffset;
    private final int ySize;
    private final int yOffset;

    public TargetGraphics(final BoardPanel boardPanel, final EntityGraphics targetEntity,
            final int xOffset, final int yOffset, final int xSize, final int ySize, final Image image) {
        super(boardPanel, targetEntity.getXPos() + xOffset, targetEntity.getYPos() + yOffset);
        this.targetEntity = targetEntity;
        this.image = image;
        this.xSize = xSize;
        this.xOffset = xOffset;
        this.ySize = ySize;
        this.yOffset = yOffset;
        setHorizontalAlignment(JLabel.CENTER);
        setVerticalAlignment(JLabel.CENTER);
        setVisible(true);
    }

    public TargetGraphics(final BoardPanel boardPanel, final PlayerGraphics player) {
        this(boardPanel, player, 0, -30,
            player.getWidth() / 2, player.getHeight() / 2, Resources.getImage("placeholder"));
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
        setSize(new Dimension((int) (xSize * boardPanel.getWidthRatio()), (int) (ySize * boardPanel.getHeightRatio())));
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Dimension dim = getSize();
        g.drawImage(image, 0, 0, dim.width, dim.height, this);
        System.out.println(dim);
    }

}
