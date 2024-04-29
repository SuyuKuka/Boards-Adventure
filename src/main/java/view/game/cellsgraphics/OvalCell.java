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
        final double zoom = getGamePanel().getZoom();
        g2d.fillOval(0, 0, (int) (getCellDiameter() * zoom), (int) (getCellDiameter() * zoom));
        final int inside = 3;
        g2d.setColor(Color.WHITE);
        g2d.fillOval(inside, inside, getCellDiameter() - inside * 2, getCellDiameter() - inside * 2);
        final int icon = 8;
        g2d.drawImage(getCellImage(), icon, icon, getCellDiameter() - icon * 2, getCellDiameter() - icon * 2, this);
        g2d.setColor(new Color(663399));
        g2d.setFont(Resources.getFont("RifficFree", 12));
        g2d.drawString(String.valueOf(getCellID()), getCellDiameter() / 2 - 5, getCellDiameter() / 2 - 5);
    }

}
