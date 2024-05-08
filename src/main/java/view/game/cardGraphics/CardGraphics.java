package view.game.cardGraphics;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.Controller;
import controller.resources.Resources;
import view.game.DataPanel;

public abstract class CardGraphics extends JLabel {

    // CardGraphics Border
    private final Image cardBackground = Resources.getImage("cardGraphicsFrame");
    private static final int BORDER_WIDTH = 96;
    private static final int BORDER_HEIGHT = 96;
    private static final Rectangle BOUNDS_AREA = new Rectangle();

    private final DataPanel dataPanel;
    private final Controller controller;

    private final JPanel cellGraphicsPanel;

    public CardGraphics(final DataPanel dataPanel, final JPanel cellGraphicsPanel) {
        this.dataPanel = dataPanel;
        this.controller = dataPanel.getController();
        this.cellGraphicsPanel = cellGraphicsPanel;
        updatePaintArea();
        setVisible(true);
    }

    /**
     * Méthode qui dessine le background de la carte. Si cette méthode est héritée, il faut toujours call
     * super.paintComponent(g);.
     */
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(cardBackground, 0, 0, getWidth(), getHeight(),
            0, 0, cardBackground.getWidth(this), cardBackground.getHeight(this), this);
    }

    public final void updatePaintArea() {
        final Dimension parentSize = cellGraphicsPanel.getPreferredSize();
        final int borderWidth = (int) (1.0 * parentSize.width / cardBackground.getWidth(this) * BORDER_WIDTH);
        final int borderHeight = (int) (1.0 * parentSize.height / cardBackground.getHeight(this) * BORDER_HEIGHT);
        BOUNDS_AREA.x = borderWidth + 1;
        BOUNDS_AREA.y = borderHeight + 1;
        BOUNDS_AREA.width = parentSize.width - (borderWidth + 1) * 2;
        BOUNDS_AREA.height = parentSize.height - (borderHeight + 1) * 2;
    }

    public static Rectangle getBoundsArea() {
        return BOUNDS_AREA;
    }
}
