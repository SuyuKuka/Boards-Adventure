package view.game.cellsgraphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import model.config.cells.Cell;

public final class RoundedRectCell extends CellGraphics {
    private static final int ARC_WIDTH = 20;
    private static final int ARC_HEIGHT = 20;

    public RoundedRectCell(final Cell cell, final int xPos, final int yPos) {
        super(cell, xPos, yPos);
    }

    @Override
    public void addContent() {
        JPanel test = new JPanel();
        test.setBackground(Color.GREEN);
        test.setBounds(0, 0, 50, 50);
        test.setPreferredSize(new Dimension(50, 50));
        test.setVisible(true);
        super.add(test, BorderLayout.CENTER);
    }

    @Override
    public void paint(final Graphics g) {
        super.paint(g);
        final Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.fillRoundRect(0, 0, getCellWidth(), getCellHeight(), ARC_WIDTH, ARC_HEIGHT);
        final int inside = 3;
        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect(inside, inside, getCellWidth() - inside * 2,
            getCellHeight() - inside * 2, ARC_WIDTH, ARC_HEIGHT);
    }

}
