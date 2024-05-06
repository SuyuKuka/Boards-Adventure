package view.game.cellsgraphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import controller.Controller;
import view.game.BoardPanel;

public final class IntersectionChoiceGraphics extends CellGraphics {

    public IntersectionChoiceGraphics(final BoardPanel boardPanel, final int cellID, final int xPos, final int yPos) {
        super(boardPanel, cellID, xPos, yPos);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                boardPanel.removeComp(BoardPanel.getInteraction());
                final Controller controller = boardPanel.getController();
                controller.setPlayerPosition(cellID);
                controller.gameUpdate();
            }
        });
    }

    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        final double zoom = getBoardPanel().getZoom();

        // Coutour
        g2d.fillOval(0, 0, getWidth(), getHeight());
        // Couleur intérieure
        g2d.setColor(Color.GREEN);
        final int startInside = (int) (getColorRange() * zoom);
        final int endInside = (int) Math.ceil(((getCellDiameter() - getColorRange() * 2) * zoom));
        g2d.fillOval(startInside, startInside, endInside, endInside);
    }

}
