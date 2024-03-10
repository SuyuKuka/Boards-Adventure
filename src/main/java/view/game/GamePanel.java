package view.game;

import static game.Game.NANO_S;

import java.util.HashMap;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.GameController;
import model.GameState;
import model.config.players.Player;
import view.CorePanel;
import view.GameMapGraphics;
import view.View;
import view.game.cellsgraphics.CellGraphics;

public final class GamePanel extends CorePanel {

    private final GameController controller;
    private final GameMapGraphics gameMapGraphics;
    private final HashMap<Integer, CellGraphics> cellsGraphics;
    private HashMap<Player, PlayerGraphics> playerGraphicsMap;

    private double zoomScale;
    private boolean hasRendered;
    private long lastGameUpdateTime;
    private double elaspedTime;

    private JButton playButton;

    public GamePanel(final View frame, final GameController controller, final GameMapGraphics gameMapGraphics) {
        super(frame);
        this.controller = controller;
        this.gameMapGraphics = gameMapGraphics;
        this.cellsGraphics = gameMapGraphics.getCellsGraphics();
        initValues();
        initPlayButton();
        add(createBackground(), Integer.valueOf(0));
        add(createCellsGraphics(), Integer.valueOf(1));
        add(createPlayersGraphics(), Integer.valueOf(2));
        add(playButton, Integer.valueOf(3));
        setVisible(true);
    }

    private void initValues() {
        createPlayersGraphicsMap();
        zoomScale = 1;
        elaspedTime = 0;
        hasRendered = false;
        lastGameUpdateTime = System.nanoTime();
    }

    private void createPlayersGraphicsMap() {
        playerGraphicsMap = new HashMap<>();
        for (Player player : controller.getPlayers()) {
            playerGraphicsMap.put(player, new PlayerGraphics(player, gameMapGraphics));
        }
    }

    private void animate() {
        // rien pour l'instant
    }

    public void render(final GameState gameState) {
        final double animationTime = gameState.getAnimationTime();
        if (hasRendered) {
            if (animationTime == -1) { // <- Actions où on attend l'input du joueur
                return;
            } else if (elaspedTime >= animationTime) { // Si l'action a dépassé le temps voulue, update le jeu.
                hasRendered = false;
                elaspedTime = 0;
                lastGameUpdateTime = System.nanoTime();
                controller.setShouldGameUpdate(true);
            } else { // Temps d'animations
                animate();
                elaspedTime = (double) (System.nanoTime() - lastGameUpdateTime) / NANO_S;
            }
            return;
        }
        final Player player = controller.getCurrentPlayer();
        switch (gameState) {
            case GAME_START:
                playButton.setText("Début du jeu !");
                break;
            case NEW_TURN:
                playButton.setText("Tour " + controller.getCurrentTurn() + " de " + player.getUsername());
                break;
            case IN_JAIL:
                playButton.setText(player.getJailTurn() + " tour(s) en prison restant.");
                break;
            case ROLL_DICE:
                playButton.setText("Cliquez pour lancer votre dé.");
                playButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(final MouseEvent e) {
                        hasRendered = false;
                        elaspedTime = 0;
                        controller.setShouldGameUpdate(true);
                        lastGameUpdateTime = System.nanoTime();
                        playButton.removeMouseListener(playButton.getMouseListeners()[1]);
                    }
                });
                break;
            case NORMAL_CELL:
                playButton.setText(controller.getCurrentPlayerCell().getCellType().name());
                playerGraphicsMap.get(controller.getCurrentPlayer()).updatePosition();
                break;
            case ASK_INTERSECTION:
            case ASK_PROPERTY:
            case ASK_SHOP:
                playerGraphicsMap.get(controller.getCurrentPlayer()).updatePosition();
                playButton.setText(controller.getCurrentPlayerCell().getCellType().name());
                playButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(final MouseEvent e) {
                        hasRendered = false;
                        elaspedTime = 0;
                        controller.setShouldGameUpdate(true);
                        lastGameUpdateTime = System.nanoTime();
                        playButton.removeMouseListener(playButton.getMouseListeners()[1]);
                    }
                });
                break;
            case END_TURN:
                playButton.setText("Fin du tour de " + player.getUsername() + ".");
                break;
            default:
                break;
        }
        hasRendered = true;
        elaspedTime = (double) (System.nanoTime() - lastGameUpdateTime) / NANO_S;
    }

    public void updateCurrentPlayerPosition() {
        playerGraphicsMap.get(controller.getCurrentPlayer()).updatePosition();
    }

    public void playCellEffect() {
        System.out.println("effet");
    }

    private void removeLayer(final int layerIndex) {
        final Component layer = getComponent(layerIndex);
        if (layer != null) {
            remove(layer);
            revalidate();
            repaint();
        }
    }

    private JPanel createBackground() {
        JPanel panel = new JPanel(null);
        panel.setBounds(0, 0, getFrame().getWidth(), getFrame().getHeight());
        panel.setBackground(Color.MAGENTA);
        Image boardImg = gameMapGraphics.getImage("board");
        JLabel backgroundLabel = new JLabel(new ImageIcon(boardImg));
        backgroundLabel.setSize(new Dimension(boardImg.getWidth(null), boardImg.getHeight(null)));
        panel.add(backgroundLabel);
        panel.setVisible(true);
        return panel;
    }

    private JPanel createCellsGraphics() {
        JPanel panel = new JPanel(null);
        panel.setOpaque(false);
        panel.setBounds(0, 0, getFrame().getWidth(), getFrame().getHeight());
        for (Integer key : cellsGraphics.keySet()) {
            panel.add(cellsGraphics.get(key));
        }
        panel.setVisible(true);
        return panel;
    }

    private JPanel createPlayersGraphics() {
        JPanel panel = new JPanel(null);
        panel.setOpaque(false);
        panel.setBounds(0, 0, getFrame().getWidth(), getFrame().getHeight());
        for (Player player : playerGraphicsMap.keySet()) {
            panel.add(playerGraphicsMap.get(player));
        }
        panel.setVisible(true);
        return panel;
    }

    public void initPlayButton() {
        playButton = new JButton();
        playButton.setBounds(0, 0, 400, 100);
        playButton.setVerticalAlignment(JButton.CENTER);
        playButton.setHorizontalAlignment(JButton.CENTER);
    }

    @Override
    public void resized() {
        setBounds(0, 0, getFrame().getWidth(), getFrame().getHeight());
        for (Component comp : getComponents()) {
            if (comp instanceof JButton) {
                continue;
            }
            comp.setBounds(0, 0, getFrame().getWidth(), getFrame().getHeight());
        }
        revalidate();
    }

    public boolean hasRendered() {
        return hasRendered;
    }

}
