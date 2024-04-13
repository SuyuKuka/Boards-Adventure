package view.game.cellsgraphics;

import java.util.ArrayList;

import controller.resources.Resources;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import model.config.cells.Cell;
import model.config.cells.Property;

public final class RoundedRectCell extends CellGraphics {
    private static final ArrayList<Color> COLORS = new ArrayList<>();

    static {
        COLORS.add(Color.PINK);
        COLORS.add(Color.RED);
        COLORS.add(Color.BLUE);
        COLORS.add(Color.GREEN);
        COLORS.add(Color.YELLOW);
        COLORS.add(Color.ORANGE);
        COLORS.add(Color.LIGHT_GRAY);
    }

    private static final int ARC_WIDTH = 5;
    private static final int ARC_HEIGHT = 5;

    public RoundedRectCell(final Cell cell, final int xPos, final int yPos) {
        super(cell, xPos, yPos);
    }

    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2d = (Graphics2D) g;
        final Property cell = (Property) getCell();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(COLORS.get(cell.getGroup() - 1));
        g2d.fillRoundRect(0, 0, getCellWidth(), getCellHeight(), ARC_WIDTH, ARC_HEIGHT);
        final int inside = 3;
        if (cell.getOwner() == null) {
            g2d.setColor(Color.WHITE);
        } else {
            g2d.setColor(COLORS.get(cell.getOwner().getTeam().getTeamID()));
        }
        g2d.fillRoundRect(inside, inside, getCellWidth() - inside * 2,
            getCellHeight() - inside * 2, ARC_WIDTH, ARC_HEIGHT);
        final int icon = 8;
        g2d.drawImage(getImage(), icon, icon, getCellWidth() - icon * 2, getCellHeight() - icon * 2, null);
        g2d.setColor(new Color(663399));
        g2d.setFont(Resources.getFont("RifficFree", 12));
        g2d.drawString(String.valueOf(getCell().getId()), getCellWidth() / 2 - 5, getCellHeight() / 2 - 5);
    }

}
