package view.game.boardpanel;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import controller.resources.Resources;
import view.game.boardpanel.cellsgraphics.CellGraphics;

public final class PlayerGraphics extends EntityGraphics {

    private final BoardPanel boardPanel;

    private final int playerID;

    private Image playerImage;


    public PlayerGraphics(final int playerID, final BoardPanel boardPanel, final int xPos, final int yPos) {
        super(boardPanel, xPos, yPos);
        this.boardPanel = boardPanel;
        this.playerID = playerID;
        playerImage = Resources.getImage("j" + playerID);
        setHorizontalAlignment(JLabel.CENTER);
        setVerticalAlignment(JLabel.CENTER);
        setVisible(true);
    }

    public void updateSize() {
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

}
