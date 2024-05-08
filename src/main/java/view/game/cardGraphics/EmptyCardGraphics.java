package view.game.cardGraphics;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import controller.resources.Resources;
import view.game.DataPanel;

public final class EmptyCardGraphics extends CardGraphics {

    public EmptyCardGraphics(final DataPanel dataPanel, final JPanel parent) {
        super(dataPanel, parent);
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2d = (Graphics2D) g;
        final Image image = Resources.getImage("soul_knight1");
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        final Rectangle inside = getBoundsArea();
        g.fillRect(inside.x, inside.y, inside.width, inside.height);
        g.drawImage(image, inside.x, inside.y, inside.width, inside.height,
            0, 0, image.getWidth(this), image.getHeight(this), this);
    }

}
