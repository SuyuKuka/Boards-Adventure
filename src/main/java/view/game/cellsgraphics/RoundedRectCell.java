package view.game.cellsgraphics;

import java.util.ArrayList;

import controller.resources.Resources;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import model.config.cells.Property;
import view.game.GamePanel;

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
        COLORS.add(Color.MAGENTA);
    }

    private static final int ARC_WIDTH = 5;
    private static final int ARC_HEIGHT = 5;

    public RoundedRectCell(final GamePanel gamePanel, final int cellID, final int xPos, final int yPos) {
        super(gamePanel, cellID, xPos, yPos);
    }

    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2d = (Graphics2D) g;
        final int cellID = getCellID();
        final Property cell = (Property) getController().getCell(cellID);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(COLORS.get(cell.getGroup() - 1));
        final double zoom = getGamePanel().getZoom();
        g2d.fillRoundRect(0, 0, getCellDiameter(), getCellDiameter(), ARC_WIDTH, ARC_HEIGHT);
        final int inside = 3;
        if (cell.getOwner() == null) {
            g2d.setColor(Color.WHITE);
        } else {
            g2d.setColor(COLORS.get(cell.getOwner().getTeam().getTeamID()));
        }
        g2d.fillRoundRect(inside, inside, getCellDiameter() - inside * 2,
            getCellDiameter() - inside * 2, ARC_WIDTH, ARC_HEIGHT);
        final int icon = 8;
        g2d.drawImage(getCellImage(), icon, icon, getCellDiameter() - icon * 2, getCellDiameter() - icon * 2, this);
        g2d.setColor(new Color(663399));
        g2d.setFont(Resources.getFont("RifficFree", 12));
        g2d.drawString(String.valueOf(cellID), getCellDiameter() / 2 - 5, getCellDiameter() / 2 - 5);
    }

}
