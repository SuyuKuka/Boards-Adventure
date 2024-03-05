package view.game;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import controller.Resources;
import model.config.players.Player;
import view.GameMapGraphics;
import view.game.cellsgraphics.CellGraphics;

public final class PlayerGraphics extends JLabel {

    private static final int PLAYER_TOKEN_WIDTH = 14;
    private static final int PLAYER_TOKEN_HEIGHT = 14;

    private final GameMapGraphics gameMapGraphics;

    private Player player;
    private Image playerImage;


    public PlayerGraphics(final Player player, final GameMapGraphics gameMapGraphics) {
        this.player = player;
        this.gameMapGraphics = gameMapGraphics;
        initPlayerImage(player.getPlayerID());
        setHorizontalAlignment(JLabel.CENTER);
        setVerticalAlignment(JLabel.CENTER);
        setBounds(0, 0, 14, 14);
        setIcon(new ImageIcon(playerImage.getScaledInstance(14, 14, Image.SCALE_SMOOTH)));
        final CellGraphics cellGraphics = gameMapGraphics.getCellGraphics(player.getPosition());
        setLocation(cellGraphics.getXPos() - CellGraphics.getCellWidth() / 4,
            cellGraphics.getYPos() - CellGraphics.getCellHeight() / 4);
        setVisible(true);
    }

    public void render() {

    }

    public void updatePosition() {
        final CellGraphics cellGraphics = gameMapGraphics.getCellGraphics(player.getPosition());
        setLocation(cellGraphics.getXPos() - CellGraphics.getCellWidth() / 4,
            cellGraphics.getYPos() - CellGraphics.getCellHeight() / 4);
    }

    private void initPlayerImage(final int id) {
        final String playerID = "j" + id;
        if (gameMapGraphics.getImages().containsKey(playerID)) {
            playerImage = gameMapGraphics.getImage(playerID);
        } else {
            playerImage = Resources.getGameImage(playerID);
        }
    }
}
