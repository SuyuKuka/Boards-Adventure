package view.game.cellsgraphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import controller.resources.Resources;
import view.game.BoardPanel;

public final class DefaultCellGraphics extends CellGraphics {

    public DefaultCellGraphics(final BoardPanel boardPanel,
            final int cellID, final int xPos, final int yPos) {
        super(boardPanel, cellID, xPos, yPos);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent e) {
                final int button = e.getButton();
                if (button == MouseEvent.BUTTON3) {
                    boardPanel.dispatchEvent(SwingUtilities.convertMouseEvent(DefaultCellGraphics.this, e, boardPanel));
                } else if (button == MouseEvent.BUTTON1) {
                    System.out.println("clique gauche cellule !!!");
                }
            }
        });

    }

    public void paintComponent(final Graphics g) {
        final Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // Coutour
        g2d.fillOval(0, 0, getWidth(), getHeight());
        // Couleur intérieure
        g2d.setColor(Color.WHITE);
        final Rectangle colorRangeDim = CellGraphics.getColorRangeDim();
        g2d.fillOval(colorRangeDim.x, colorRangeDim.y, colorRangeDim.width, colorRangeDim.height);
        // icône
        final Rectangle iconRangeDim = CellGraphics.getIconRangeDim();
        g2d.drawImage(getCellImage(), iconRangeDim.x, iconRangeDim.y,
            iconRangeDim.width, iconRangeDim.height, this);
        // id cellule
        g2d.setColor(new Color(663399));
        g2d.setFont(Resources.getFont("RifficFree", 12)); //TODO <- size dynamique (ici = 12)
        g2d.drawString(String.valueOf(getCellID()), getWidth() / 4, getHeight() / 2);
    }

}
