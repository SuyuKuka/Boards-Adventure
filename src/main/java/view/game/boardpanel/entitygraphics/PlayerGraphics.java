package view.game.boardpanel.entitygraphics;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import controller.resources.Resources;
import view.game.boardpanel.BoardPanel;
import view.game.boardpanel.entitygraphics.cellsgraphics.CellGraphics;

public final class PlayerGraphics extends EntityGraphics {
    private static final int PLAYER_MOVEMENT_SPEED = 4; // >= 4 pour éviter les bugs !

    private final int playerID;

    private Image playerImage;

    public PlayerGraphics(final int playerID, final BoardPanel boardPanel, final int xPos, final int yPos) {
        super(boardPanel, xPos, yPos);
        this.playerID = playerID;
        playerImage = Resources.getImage(getBoardPanel().getController().getPlayer(playerID).getClassName());
        setHorizontalAlignment(JLabel.CENTER);
        setVerticalAlignment(JLabel.CENTER);
        setVisible(true);
    }

    public void updateSize() {
        final BoardPanel boardPanel = getBoardPanel();
        final int cellDiam = CellGraphics.getCellDiameter();
        final int sizeX = (int) (cellDiam * boardPanel.getWidthRatio()) / 2;
        final int sizeY = (int) (cellDiam * boardPanel.getHeightRatio()) / 2;
        setSize(sizeX, sizeY);
        setIcon(new ImageIcon(playerImage.getScaledInstance(sizeX, sizeY, Image.SCALE_SMOOTH)));
    }

    public void updateMoving(final CellGraphics cellTarget) {
        final int xPos = getXPos();
        final int yPos = getYPos();

        final int cellXPos = cellTarget.getXPos();
        final int cellYPos = cellTarget.getYPos();

        final int distanceX = xPos - cellXPos;
        final int distanceY = yPos - cellYPos;

        final double distanceTot = Math.abs(distanceX) + Math.abs(distanceY);

        final int travelX = (int) (PLAYER_MOVEMENT_SPEED * (distanceX / distanceTot));
        final int travelY = (int) (PLAYER_MOVEMENT_SPEED * (distanceY / distanceTot));

        if (xPos != cellXPos) {
            setXPos(Math.abs(distanceX) <= Math.abs(travelX) ? cellXPos : xPos - travelX);
        }
        if (yPos != cellYPos) {
            setYPos(Math.abs(distanceY) <= Math.abs(travelY) ? cellYPos : yPos - travelY);
        }
    }

    public int getPlayerID() {
        return playerID;
    }

    public Image getPlayerImage() {
        return playerImage;
    }

}
