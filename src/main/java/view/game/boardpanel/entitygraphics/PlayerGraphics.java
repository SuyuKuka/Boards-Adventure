package view.game.boardpanel.entitygraphics;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import controller.resources.Resources;
import view.game.boardpanel.BoardPanel;
import view.game.boardpanel.entitygraphics.cellsgraphics.CellGraphics;

public final class PlayerGraphics extends EntityGraphics {

    private final int playerID;

    private Image playerImage;
    private int nextCellID;

    public PlayerGraphics(final int playerID, final BoardPanel boardPanel, final int xPos, final int yPos) {
        super(boardPanel, xPos, yPos);
        this.playerID = playerID;
        playerImage = Resources.getImage("j" + playerID);
        nextCellID = boardPanel.getController().getPlayerCell(playerID).getNextCellID();
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

    public int getPlayerID() {
        return playerID;
    }

    public Image getPlayerImage() {
        return playerImage;
    }

    public int getNextCellID() {
        return nextCellID;
    }

    public void setNextCellID(final int nextCellID) {
        this.nextCellID = nextCellID;
    }

}
