package view.game.cardGraphics;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import controller.resources.Resources;
import view.game.DataPanel;

public final class EmptyCardGraphics extends CardGraphics {

    private static final int STRING_PER_WIDTH = 65;
    private final Image image = Resources.getImage("soul_knight1");
    private static final int DEFAULT_FONT_SIZE = 24;
    private final Font font;

    public EmptyCardGraphics(final DataPanel dataPanel, final JPanel parent) {
        super(dataPanel, parent);
        this.font = Resources.getFont("NoitaBlackletter-Regular", DEFAULT_FONT_SIZE);
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        final Rectangle bounds = getBoundsArea();
        final double targetWidth = bounds.width * STRING_PER_WIDTH / 100;
        // @see https://stackoverflow.com/a/27740330
        g.setFont(font);
        final String text = "Rien à signaler...";
        FontMetrics metrics = g.getFontMetrics(font);
        float currentFontSize = font.getSize();
        while (metrics.stringWidth(text) < targetWidth) {
            currentFontSize++;
            g.setFont(font.deriveFont(currentFontSize));
            metrics = g.getFontMetrics(g.getFont());
        }
        final int x = bounds.x + (bounds.width - metrics.stringWidth(text)) / 2;
        final int y = bounds.y + ((bounds.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g.drawString(text, x, y);
    }

}
