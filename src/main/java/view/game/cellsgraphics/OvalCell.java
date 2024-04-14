package view.game.cellsgraphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import controller.Controller;
import controller.resources.Resources;

public final class OvalCell extends CellGraphics {

    private JLabel iconLabel;

    public OvalCell(final Controller controller, final int cellID, final int xPos, final int yPos) {
        super(controller, cellID, xPos, yPos);
    }

    public void initIconPanel() {
        iconLabel = new JLabel(new ImageIcon(getImage()));
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
        final int icon = 8;
        g2d.drawImage(getImage(), icon, icon, getCellWidth() - icon * 2, getCellHeight() - icon * 2, null);
        g2d.setColor(new Color(663399));
        g2d.setFont(Resources.getFont("RifficFree", 12));
        g2d.drawString(String.valueOf(getCellID()), getCellWidth() / 2 - 5, getCellHeight() / 2 - 5);
    }

}
