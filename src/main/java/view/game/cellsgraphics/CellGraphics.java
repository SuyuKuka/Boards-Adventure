package view.game.cellsgraphics;

import java.awt.Image;
import java.awt.BorderLayout;

import javax.swing.JPanel;

import controller.Controller;
import controller.resources.Resources;

public abstract class CellGraphics extends JPanel {
    private static final int CELL_HEIGHT = 65;
    private static final int CELL_WIDTH = 65;

    private final Controller controller;
    private final int cellID;
    private final int xPos;
    private final int yPos;

    private Image image;

    public CellGraphics(final Controller controller, final int cellID, final int xPos, final int yPos) {
        this.controller = controller;
        this.cellID = cellID;
        this.xPos = xPos;
        this.yPos = yPos;
        image = Resources.getImage(controller.getCell(cellID).getCellType().name().toLowerCase());
        setSize(CELL_WIDTH, CELL_HEIGHT);
        setBounds(xPos - CELL_WIDTH / 2, yPos - CELL_HEIGHT / 2, CELL_WIDTH, CELL_HEIGHT);
        setLayout(new BorderLayout());
        setOpaque(false);
        setVisible(true);
    }

    public static int getCellWidth() {
        return CELL_WIDTH;
    }

    public static int getCellHeight() {
        return CELL_HEIGHT;
    }

    public final Controller getController() {
        return controller;
    }

    public final int getCellID() {
        return cellID;
    }

    public final Image getImage() {
        return image;
    }

    public final int getXPos() {
        return xPos;
    }

    public final int getYPos() {
        return yPos;
    }
}
