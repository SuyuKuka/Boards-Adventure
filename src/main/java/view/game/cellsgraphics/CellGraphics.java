package view.game.cellsgraphics;

import model.config.cells.Cell;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public abstract class CellGraphics extends JPanel {
    private static final int HEIGHT = 20;
    private static final int WIDTH = 20;

    private Cell cell;
    private int xPos;
    private int yPos;

    public CellGraphics(final Cell cell, final int xPos, final int yPos) {
        this.cell = cell;
        this.xPos = xPos;
        this.yPos = yPos;
        setLayout(new BorderLayout());
        setBackground(Color.GREEN);
        setSize(WIDTH, HEIGHT);
        setBounds(xPos - WIDTH / 2, yPos - HEIGHT / 2, WIDTH, HEIGHT);
        setVisible(true);
    }

    @Override
    public void paint(final Graphics g) {
        super.paint(g);
        g.drawOval(0, 0, WIDTH - 1, HEIGHT - 1);
    }

    public final Cell getCell() {
        return cell;
    }

    public final int getXPos() {
        return xPos;
    }

    public final int getYPos() {
        return yPos;
    }

}
