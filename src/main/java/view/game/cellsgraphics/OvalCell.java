package view.game.cellsgraphics;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import controller.Resources;
import model.config.cells.Cell;

public class OvalCell extends CellGraphics {

    private JLabel iconLabel;
    private Image icon = Resources.getImage("placeholder");

    public OvalCell(final Cell cell, final int xPos, final int yPos) {
        super(cell, xPos, yPos);
        setVisible(true);
    }

    @Override
    public void addContent() {
        initIconPanel();
        super.add(iconLabel, BorderLayout.CENTER);
    }

    public void initIconPanel() {
        iconLabel = new JLabel(new ImageIcon(icon));
        iconLabel.setVerticalAlignment(JLabel.CENTER);
        iconLabel.setHorizontalAlignment(JLabel.CENTER);
        iconLabel.setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(getBorderThickness()));
        g2d.drawOval(1, 1, getCellWidth() - 2, getCellHeight() - 2);
    }

}
