package view.game.cellsgraphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import controller.resources.Resources;
import view.game.GamePanel;

public final class OvalCell extends CellGraphics {

    private static final int INSIDE_COLOR = 3;
    private static final int ICON = 8;

    private JLabel iconLabel;

    public OvalCell(final GamePanel gamePanel, final int cellID, final int xPos, final int yPos) {
        super(gamePanel, cellID, xPos, yPos);
    }

    public void initIconPanel() {
        iconLabel = new JLabel(new ImageIcon(getCellImage()));
        iconLabel.setVerticalAlignment(JLabel.CENTER);
        iconLabel.setHorizontalAlignment(JLabel.CENTER);
        iconLabel.setVisible(true);
    }

    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // Variables utiles
        final int diam = getCellDiameter();
        final double zoom = getGamePanel().getZoom();
        // Coutour
        g2d.fillOval(0, 0, getWidth(), getHeight());
        // Couleur intérieure
        g2d.setColor(Color.WHITE);
        final int startInside = (int) (INSIDE_COLOR * zoom);
        final int endInside = (int) Math.ceil(((diam - INSIDE_COLOR * 2) * zoom));
        g2d.fillOval(startInside, startInside, endInside, endInside);
        // icône
        final int startIcon = (int) (ICON * zoom);
        final int endIcon = (int) Math.ceil((diam - ICON * 2) * zoom);
        g2d.drawImage(getCellImage(), startIcon, startIcon, endIcon, endIcon, this);
        // id cellule
        g2d.setColor(new Color(663399));
        g2d.setFont(Resources.getFont("RifficFree", 12)); //TODO <- size dynamique (ici =12)
        final int cellID = getCellID();
        g2d.drawString(String.valueOf(cellID), getWidth() / 4, getHeight() / 2);
    }

}
