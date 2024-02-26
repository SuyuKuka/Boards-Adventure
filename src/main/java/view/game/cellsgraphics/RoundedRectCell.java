package view.game.cellsgraphics;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JLabel;
import javax.swing.JTextArea;

import model.config.cells.Cell;

public class RoundedRectCell extends CellGraphics {
    private static final int ARC_WIDTH = 20;
    private static final int ARC_HEIGHT = 20;

    private JLabel propertyLevelLabel;
    private Image propertyLevelImg;
    private int propertyLevel;

    private JTextArea propertyTextArea;
    private String propertyName;

    private JLabel propertyRentLabel;
    private int propertyRent;

    public RoundedRectCell(final Cell cell, final int xPos, final int yPos) {
        super(cell, xPos, yPos);
        setVisible(true);
    }

    @Override
    public void addContent() {
        initPropertyLevelLabel();
        super.add(propertyLevelLabel, BorderLayout.NORTH);
        initPropertyTextArea();
        super.add(propertyTextArea, BorderLayout.CENTER);
        initPropertyRentLabel();
        super.add(propertyRentLabel, BorderLayout.SOUTH);
    }

    public void initPropertyLevelLabel() {
        propertyLevelLabel = new JLabel();
        propertyLevelLabel.setVerticalAlignment(JLabel.CENTER);
        propertyLevelLabel.setHorizontalAlignment(JLabel.CENTER);
    }

    private void initPropertyTextArea() {
        propertyTextArea = new JTextArea();
        propertyTextArea.setLineWrap(true);
    }

    private void initPropertyRentLabel() {
        propertyRentLabel = new JLabel();
        propertyRentLabel.setVerticalAlignment(JLabel.CENTER);
        propertyRentLabel.setHorizontalAlignment(JLabel.CENTER);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(getBorderThickness()));
        g2d.drawRoundRect(1, 1, getCellWidth() - 2, getCellHeight() - 2, ARC_WIDTH, ARC_HEIGHT);
    }

}
