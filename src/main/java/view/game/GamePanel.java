package view.game;

import java.util.ArrayList;
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
import controller.GameState;
import model.config.players.Player;
import view.CorePanel;
import view.GameMapGraphics;
import view.View;
import view.game.cellsgraphics.CellGraphics;

public final class GamePanel extends CorePanel {

    private GameController controller;

    private GameMapGraphics gameMapGraphics;
    private ArrayList<CellGraphics> cellsGraphicsList = new ArrayList<>();
    private HashMap<Player, PlayerGraphics> playerGraphicsMap = new HashMap<>();

    private double viewTime = 0;
    private double viewDuration = 0;
    private boolean hasUpdated = false;
    private Boolean playerHasMadeChoice = false;

    private JButton playButton;

    public GamePanel(final View frame, final GameController controller, final GameMapGraphics gameMapGraphics) {
        super(frame);
        this.controller = controller;
        this.gameMapGraphics = gameMapGraphics;
        initPlayButton();
        add(createBackground(), Integer.valueOf(0));
        add(createCellsGraphics(), Integer.valueOf(1));
        add(createPlayersGraphics(), Integer.valueOf(2));
        add(playButton, Integer.valueOf(3));
        setVisible(true);
    }

    public void render(final double lastUpdate) {
        final Player player = controller.getCurrentPlayer();
        final GameState gameState = controller.getGameState();
        switch (gameState) {
            case GAME_START:
                if (!hasUpdated) {
                    playButton.setText("Début du jeu !");
                    hasUpdated = true;
                    viewTime = 0.3;
                } else {
                    viewDuration += lastUpdate;
                    if (viewDuration > viewTime) {
                        updateGameState();
                    }
                }
            case NEW_TURN:
                if (!hasUpdated) {
                    playButton.setText("Tour " + controller.getCurrentTurn()
                        + " de " + player.getUsername());
                    hasUpdated = true;
                    viewTime = 0.1;
                } else {
                    viewDuration += lastUpdate;
                    if (viewDuration > viewTime) {
                        updateGameState();
                    }
                }
                break;
            case IN_JAIL:
                if (!hasUpdated) {
                    playButton.setText(player.getJailTurn() + " tour(s) en prison restant.");
                    hasUpdated = true;
                    viewTime = 0.1;
                } else {
                    viewDuration += lastUpdate;
                    if (viewDuration > viewTime) {
                        updateGameState();
                    }
                }
                break;
            case PLAY_DICE:
                if (!hasUpdated) {
                    playButton.setText("Cliquez pour lancer votre dé.");
                    hasUpdated = true;
                    playButton.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mousePressed(final MouseEvent e) {
                            controller.playDice();
                            updateGameState();
                            playButton.removeMouseListener(playButton.getMouseListeners()[1]);
                        }
                    });
                }
                break;
            case NORMAL_CELL:
                if (!hasUpdated) {
                    playButton.setText(controller.getCurrentPlayerCell().getCellType().name());
                    hasUpdated = true;
                    viewTime = 0.5;
                } else {
                    viewDuration += lastUpdate;
                    if (viewDuration > viewTime) {
                        updateGameState();
                    }
                }
                break;
            case ASK_INTERSECTION:
            case ASK_PROPERTY:
            case ASK_SHOP:
                if (!hasUpdated) {
                    playButton.setText(controller.getCurrentPlayerCell().getCellType().name());
                    hasUpdated = true;
                    playButton.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mousePressed(final MouseEvent e) {
                            updateGameState();
                            playButton.removeMouseListener(playButton.getMouseListeners()[1]);
                        }
                    });
                }
                break;
            case END_TURN:
                if (!hasUpdated) {
                    playButton.setText("Fin du tour de " + player.getUsername() + ".");
                    hasUpdated = true;
                    viewTime = 0.1;
                } else {
                    viewDuration += lastUpdate;
                    if (viewDuration > viewTime) {
                        updateGameState();
                    }
                }
                break;
            default:
                break;
        }
    }

    public void playDice() {
        playerGraphicsMap.get(controller.getCurrentPlayer()).updatePosition();
    }

    private void updateGameState() {
        controller.updateGameState();
        viewTime = 0;
        viewDuration = 0;
        hasUpdated = false;
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
        final HashMap<Integer, CellGraphics> cellsGraphics = gameMapGraphics.getCellsGraphics();
        for (Integer key : cellsGraphics.keySet()) {
            CellGraphics cellGraphics = cellsGraphics.get(key);
            cellsGraphics.put(key, cellGraphics);
            panel.add(cellGraphics);
        }
        panel.setVisible(true);
        return panel;
    }

    private JPanel createPlayersGraphics() {
        JPanel panel = new JPanel(null);
        panel.setOpaque(false);
        panel.setBounds(0, 0, getFrame().getWidth(), getFrame().getHeight());
        for (Player player : controller.getPlayers()) {
            PlayerGraphics playerGraphics = new PlayerGraphics(player, gameMapGraphics);
            playerGraphicsMap.put(player, playerGraphics);
            panel.add(playerGraphics);
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

}
