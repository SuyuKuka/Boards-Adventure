package view.game.boardpanel.entitygraphics.cellsgraphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
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
                    controller.getCurrentPlayerGraphics().updateCoordinates(xPos, yPos);
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

        // Coutour
        g2d.fillOval(0, 0, getWidth(), getHeight());
        // Couleur intérieure
        g2d.setColor(Color.GREEN);
        final Rectangle colorRangeDim = CellGraphics.getColorRangeDim();
        g2d.fillOval(colorRangeDim.x, colorRangeDim.y, colorRangeDim.width, colorRangeDim.height);

    }

}
