package view.game.cellsgraphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import controller.Resources;
import model.config.cells.Cell;

public final class OvalCell extends CellGraphics {

    private JLabel iconLabel;
    private Image icon = Resources.getImage("placeholder");

    public OvalCell(final Cell cell, final int xPos, final int yPos) {
        super(cell, xPos, yPos);
        setVisible(true);
    }

    public void initIconPanel() {
        iconLabel = new JLabel(new ImageIcon(icon));
        iconLabel.setVerticalAlignment(JLabel.CENTER);
        iconLabel.setHorizontalAlignment(JLabel.CENTER);
        iconLabel.setVisible(true);
    }

    @Override
    public void paint(final Graphics g) {
        super.paint(g);
        final Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.fillOval(0, 0, getCellWidth(), getCellHeight());
        final int inside = 3;
        g2d.setColor(Color.WHITE);
        g2d.fillOval(inside, inside, getCellWidth() - inside * 2, getCellHeight() - inside * 2);
    }

}
