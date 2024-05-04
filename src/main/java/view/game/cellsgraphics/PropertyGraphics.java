package view.game.cellsgraphics;

import java.util.ArrayList;

import controller.resources.Resources;
import model.config.cells.Property;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import java.awt.Color;

import view.game.GamePanel;

public final class PropertyGraphics extends CellGraphics {
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

    public PropertyGraphics(GamePanel gamePanel, int cellID, int xPos, int yPos) {
        super(gamePanel, cellID, xPos, yPos);
    }

    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        final int cellID = getCellID();
        final Property cell = (Property) getController().getCell(cellID);
        final int diam = getCellDiameter();
        final double zoom = getGamePanel().getZoom();
        // Contour
        g2d.setColor(COLORS.get(cell.getGroup() - 1));
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), ARC_WIDTH, ARC_HEIGHT);
        // Couleur intérieure
        if (cell.getOwner() == null) {
            g2d.setColor(Color.WHITE);
        } else {
            g2d.setColor(COLORS.get(cell.getOwner().getTeam().getTeamID()));
        }
        final int startInside = (int) (getColorRange() * zoom);
        final int endInside = (int) Math.ceil(((diam - getColorRange() * 2) * zoom));
        g2d.fillRoundRect(startInside, startInside, endInside, endInside, ARC_WIDTH, ARC_HEIGHT);
        // icône
        final int startIcon = (int) (getIconRange() * zoom);
        final int endIcon = (int) Math.ceil((diam - getIconRange() * 2) * zoom);
        g2d.drawImage(loadCellImage(), startIcon, startIcon, endIcon, endIcon, this);
        // id cellule
        g2d.setColor(new Color(663399));
        g2d.setFont(Resources.getFont("RifficFree", 12)); //TODO <- size dynamique (ici =12)
        g2d.drawString(String.valueOf(cellID), getWidth() / 4, getHeight() / 2);
    }

}
