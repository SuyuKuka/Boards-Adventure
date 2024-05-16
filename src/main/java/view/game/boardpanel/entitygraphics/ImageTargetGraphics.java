package view.game.boardpanel.entitygraphics;

import java.awt.Dimension;
import java.awt.Image;

import javax.swing.JLabel;

import view.game.boardpanel.BoardPanel;

public final class ImageTargetGraphics extends EntityGraphics {

    private final Image image;
    private final EntityGraphics targetEntity;
    private final int xSize;
    private final int ySize;

    public ImageTargetGraphics(final BoardPanel boardPanel, final EntityGraphics targetEntity,
            final int xOffset, final int yOffset, final int xSize, final int ySize, final Image image) {
        super(boardPanel, targetEntity.getXPos() + xOffset, targetEntity.getYPos() + yOffset);
        this.targetEntity = targetEntity;
        this.image = image;
        this.xSize = xSize;
        this.ySize = ySize;
        setHorizontalAlignment(JLabel.CENTER);
        setVerticalAlignment(JLabel.CENTER);
        setVisible(true);
    }

    @Override
    public void updateSize() {
        final BoardPanel boardPanel = getBoardPanel();
        setSize(new Dimension((int) (xSize * boardPanel.getWidthRatio()), ySize * (int) (boardPanel.getHeightRatio())));
    }

}
