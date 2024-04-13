package view.game;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

import controller.resources.Resources;
import model.config.players.Player;
import view.game.cellsgraphics.CellGraphics;

public final class PlayerGraphics extends JPanel {

    private static final int PLAYER_DEFAULT_WIDTH = 14;
    private static final int PLAYER_DEFAULT_HEIGHT = 14;

    private final GamePanel gamePanel;

    private int playerWidth;
    private int playerHeight;
    private int xPos;
    private int yPos;

    private Player player;
    private Image playerImage;


    public PlayerGraphics(final Player player, final GamePanel gamePanel) {
        this.player = player;
        this.gamePanel = gamePanel;
        playerWidth = (int) (PLAYER_DEFAULT_WIDTH * gamePanel.getZoom());
        playerHeight = (int) (PLAYER_DEFAULT_HEIGHT * gamePanel.getZoom());
        System.out.println(playerWidth);
        final CellGraphics cellGraphics = gamePanel.getGameMapGraphics().getCellGraphics(player.getPosition());
        xPos = cellGraphics.getXPos();
        yPos = cellGraphics.getYPos();
        playerImage = Resources.getImage("j" + player.getPlayerID());
        setBounds(0, 0, gamePanel.getWidth(), gamePanel.getHeight());
        setLocation(xPos - cellGraphics.getCellWidth() / 4, yPos - cellGraphics.getCellHeight() / 4);
        setOpaque(false);
        setVisible(true);
    }

    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform at = new AffineTransform();
        final double zoom = gamePanel.getZoom();
        // at.translate(gamePanel.getWidth() / 2, gamePanel.getHeight() / 2);
        // at.scale(zoom, zoom);
        // at.translate(-588, -263);
        g2d.transform(at);
        g2d.drawImage(playerImage, 0, 0, playerWidth, playerHeight, this);
    }

    public void update(final double zoomScaleX, final double zoomScaleY) {
        playerWidth = (int) (PLAYER_DEFAULT_WIDTH * zoomScaleX);
        playerHeight = (int) (PLAYER_DEFAULT_HEIGHT * zoomScaleY);
        final CellGraphics cellGraphics = gamePanel.getGameMapGraphics().getCellGraphics(player.getPosition());
        xPos = cellGraphics.getXPos();
        yPos = cellGraphics.getYPos();
        setBounds(0, 0, gamePanel.getWidth(), gamePanel.getHeight());
        setLocation(xPos - cellGraphics.getCellWidth() / 4, yPos - cellGraphics.getCellHeight() / 4);
    }

    public void updatePosition() {
        final CellGraphics cellGraphics = gamePanel.getGameMapGraphics().getCellGraphics(player.getPosition());
        setLocation(cellGraphics.getXPos() - cellGraphics.getCellWidth() / 4,
            cellGraphics.getYPos() - cellGraphics.getCellHeight() / 4);
    }
}
