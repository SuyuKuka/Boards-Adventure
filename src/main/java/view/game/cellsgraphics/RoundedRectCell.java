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

    private static final int INSIDE_COLOR = 3;
    private static final int ICON = 8;
    private static final int ARC_WIDTH = 5;
    private static final int ARC_HEIGHT = 5;

    public RoundedRectCell(final GamePanel gamePanel, final int cellID, final int xPos, final int yPos) {
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
        final int startInside = (int) (INSIDE_COLOR * zoom);
        final int endInside = (int) Math.ceil(((diam - INSIDE_COLOR * 2) * zoom));
        g2d.fillRoundRect(startInside, startInside, endInside, endInside, ARC_WIDTH, ARC_HEIGHT);
        // icône
        final int startIcon = (int) (ICON * zoom);
        final int endIcon = (int) Math.ceil((diam - ICON * 2) * zoom);
        g2d.drawImage(getCellImage(), startIcon, startIcon, endIcon, endIcon, this);
        // id cellule
        g2d.setColor(new Color(663399));
        g2d.setFont(Resources.getFont("RifficFree", 12)); //TODO <- size dynamique (ici =12)
        g2d.drawString(String.valueOf(cellID), getWidth() / 4, getHeight() / 2);
    }

}
