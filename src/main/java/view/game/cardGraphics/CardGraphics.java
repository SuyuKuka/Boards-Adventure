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

    private static final int WIDTH_PER = 76;
    private static final int HEIGHT_PER = 84;
    private static final Rectangle BOUNDS_AREA = new Rectangle();

    private final DataPanel dataPanel;
    private final Controller controller;

    private final Image backgroundImage = Resources.getImage("cardGraphicsFrame");
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
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(),
            0, 0, backgroundImage.getWidth(this), backgroundImage.getHeight(this), this);
    }

    public final void updatePaintArea() {
        final Dimension parentSize = cellGraphicsPanel.getPreferredSize();
        final int areaWidth = parentSize.width * WIDTH_PER / 100;
        final int areaHeight = parentSize.height * HEIGHT_PER / 100;
        BOUNDS_AREA.x = (parentSize.width - areaWidth) / 2;
        BOUNDS_AREA.y = (parentSize.height - areaHeight) / 2;
        BOUNDS_AREA.width = areaWidth;
        BOUNDS_AREA.height = areaHeight;
    }

    public static Rectangle getBoundsArea() {
        return BOUNDS_AREA;
    }
}
