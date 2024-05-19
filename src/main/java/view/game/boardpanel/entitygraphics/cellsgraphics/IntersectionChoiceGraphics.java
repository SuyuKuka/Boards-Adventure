package view.game.boardpanel.entitygraphics.cellsgraphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import controller.Controller;
import view.game.boardpanel.BoardPanel;

public final class IntersectionChoiceGraphics extends CellGraphics {

    public IntersectionChoiceGraphics(final BoardPanel boardPanel, final int cellID,
            final int xPos, final int yPos) {
        super(boardPanel, cellID, xPos, yPos);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                final Controller controller = boardPanel.getController();
                if (e.getButton() == MouseEvent.BUTTON1) {
                    boardPanel.removeComp(BoardPanel.getInteraction());
                    boardPanel.cleanIntersectionEntities();
                    controller.getCurrentPlayer().setNextCellID(cellID);
                    controller.gameUpdate();
                }
            }
        });
        update();
    }

    @Override
    public void paintComponent(final Graphics g) {
        final Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        final Dimension dim = getSize();
        final int width = dim.width / 2;
        final int height = dim.height / 2;
        final int x = (dim.width - width) / 2;
        final int y = (dim.height - height) / 2;
        // Coutour
        g2d.fillOval(x, y, width, height);
        // Couleur intérieure
        g2d.setColor(Color.GREEN);
        final double colorMult = CellGraphics.getStartColorPaintMultiplier();
        final int width2 = (int) (width * colorMult);
        final int height2 = (int) (height * colorMult);
        final int x2 = (dim.width - width2) / 2;
        final int y2 = (dim.height - height2) / 2;
        g2d.fillOval(x2, y2, width2, height2);

    }

}
