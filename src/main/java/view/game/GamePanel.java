package view.game;

import static game.Game.NANO_S;

import java.util.ArrayList;
import java.util.HashMap;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;

import javax.swing.SwingUtilities;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.GameController;
import model.GameState;
import model.config.cells.Intersection;
import model.config.players.Player;
import view.CorePanel;
import view.View;
import view.game.cellsgraphics.CellGraphics;

public final class GamePanel extends CorePanel {
    // LAYERS
    private static final int BACKGROUND = 0;
    private static final int CELLS = 1;
    private static final int PLAYERS = 2;
    private static final int BOARDBARRIER = 3;
    private static final int INTERACTION = 4;
    private static final int GUI = 5;
    private static final int GAMEBARRIER = 6;
    private static final int PAUSE = 7;

    private final HashMap<Integer, JPanel> panels;

    private final GameController controller;
    private final GameMapGraphics gameMapGraphics;
    private final HashMap<Integer, CellGraphics> cellsGraphics;
    private final HashMap<Player, PlayerGraphics> playerGraphicsMap;

    private double zoomScale;
    private boolean hasRendered;
    private long lastGameUpdateTime;
    private double elaspedTime;

    private JButton playButton;

    public GamePanel(final View frame, final GameController controller, final GameMapGraphics gameMapGraphics) {
        super(frame);
        this.panels = new HashMap<>();
        this.controller = controller;
        this.gameMapGraphics = gameMapGraphics;
        this.cellsGraphics = gameMapGraphics.getCellsGraphics();
        this.playerGraphicsMap = new HashMap<>();
        initValues();
        add(createBackground(), Integer.valueOf(BACKGROUND));
        add(createCellsGraphics(), Integer.valueOf(CELLS));
        add(createPlayersGraphics(), Integer.valueOf(PLAYERS));
        add(createBarrier(BOARDBARRIER), Integer.valueOf(BOARDBARRIER));
        add(createGUI(), Integer.valueOf(GUI));
        add(createBarrier(GAMEBARRIER), Integer.valueOf(GAMEBARRIER));
        add(createPause(), Integer.valueOf(PAUSE));
        setVisible(true);
    }

    private void initValues() {
        zoomScale = 1;
        elaspedTime = 0;
        hasRendered = false;
        lastGameUpdateTime = System.nanoTime();
        for (Player player : controller.getPlayers()) {
            playerGraphicsMap.put(player, new PlayerGraphics(player, gameMapGraphics));
        }
    }

    private void animate() {
        // rien pour l'instant
    }

    public void render(final GameState gameState) {
        animate();
        final double animationTime = gameState.getAnimationTime();
        if (hasRendered) {
            if (animationTime == -1) { // <- Actions où on attend l'input du joueur
                return;
            } else if (elaspedTime >= animationTime) { // Si l'action a dépassé le temps voulue, update le jeu.
                controller.setShouldStateUpdate(true);
                return;
            }
        } else {
            System.out.println(gameState + " " + animationTime);
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
                    System.out.println("here");
                    playButton.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mousePressed(final MouseEvent e) {
                            controller.setShouldStateUpdate(true);
                            controller.playerRollDice("normal");
                            System.out.println("pressed");
                            playButton.removeMouseListener(playButton.getMouseListeners()[1]);
                        }
                    });
                    break;
                case NORMAL_CELL:
                    playButton.setText(controller.getCurrentPlayerCell().getCellType().name());
                    playerGraphicsMap.get(controller.getCurrentPlayer()).updatePosition();
                    break;
                case ASK_INTERSECTION:
                    playButton.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mousePressed(final MouseEvent e) {
                            e.consume();
                        }
                    });
                    removeLayer(INTERACTION);
                    add(createIntersectionPanel(), Integer.valueOf(INTERACTION));
                    break;
                case ASK_PROPERTY:
                    removeLayer(INTERACTION);
                    add(createPropertyPanel(), Integer.valueOf(4));
                    break;
                case ASK_SHOP:
                    playerGraphicsMap.get(controller.getCurrentPlayer()).updatePosition();
                    playButton.setText(controller.getCurrentPlayerCell().getCellType().name());
                    playButton.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mousePressed(final MouseEvent e) {
                            resetState();
                            playButton.removeMouseListener(playButton.getMouseListeners()[1]);
                        }
                    });
                    removeLayer(INTERACTION);
                    break;
                case END_TURN:
                    playButton.setText("Fin du tour de " + player.getUsername() + ".");
                    break;
                default:
                    break;
            }
            hasRendered = true;
        }
        elaspedTime = (double) (System.nanoTime() - lastGameUpdateTime) / NANO_S;
    }

    public void updateCurrentPlayerPosition() {
        playerGraphicsMap.get(controller.getCurrentPlayer()).updatePosition();
    }


    public void resetState() {
        hasRendered = false;
        elaspedTime = 0;
        lastGameUpdateTime = System.nanoTime();
    }

    // #region Background 0

    private JPanel createBackground() {
        final JPanel panel = new JPanel(null);
        panel.setBounds(0, 0, getFrame().getWidth(), getFrame().getHeight());
        panel.setBackground(Color.MAGENTA);
        Image boardImg = gameMapGraphics.getImage("board");
        JLabel backgroundLabel = new JLabel(new ImageIcon(boardImg));
        backgroundLabel.setSize(new Dimension(boardImg.getWidth(null), boardImg.getHeight(null)));
        panel.add(backgroundLabel);
        panel.setVisible(true);
        panels.put(BACKGROUND, panel);
        return panel;
    }

    // #endregion

    // #region CellsGraphics 1

    private JPanel createCellsGraphics() {
        final JPanel panel = new JPanel(null);
        panel.setBounds(0, 0, getFrame().getWidth(), getFrame().getHeight());
        panel.setOpaque(false);
        for (Integer key : cellsGraphics.keySet()) {
            panel.add(cellsGraphics.get(key));
        }
        panel.setVisible(true);
        panels.put(CELLS, panel);
        return panel;
    }

    // #endregion

    // #region PlayersGraphics 2

    private JPanel createPlayersGraphics() {
        final JPanel panel = new JPanel(null);
        panel.setOpaque(false);
        panel.setBounds(0, 0, getFrame().getWidth(), getFrame().getHeight());
        for (Player player : playerGraphicsMap.keySet()) {
            panel.add(playerGraphicsMap.get(player));
        }
        panel.setVisible(true);
        panels.put(PLAYERS, panel);
        return panel;
    }

    // #endregion

    // #region Barrière 3 et 6

    private JPanel createBarrier(final int index) {
        final JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(final Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(0, 0, 0, 80));
                g.fillRect(0, 0, getWidth(), getHeight());
            }

        };
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent e) {
                e.consume();
            }
        });
        panel.setOpaque(false);
        panel.setVisible(false);
        panels.put(index, panel);
        return panel;
    }

    // #endregion

    // #region Interaction Player/Game 4

    // #region Cellule Intersection
    private JPanel createIntersectionPanel() {
        final JPanel panel = new JPanel(null);
        final ArrayList<Integer> intersectIDs = ((Intersection) controller.getCurrentPlayerCell()).getNextCellIDs();
        for (int i = 0; i < intersectIDs.size(); i++) {
            panel.add(createIntersectChoiceButton(intersectIDs.get(i)));
        }
        panel.setOpaque(false);
        panel.setBounds(0, 0, getFrame().getWidth(), getFrame().getHeight());
        panel.setVisible(true);
        panels.put(INTERACTION, panel);
        return panel;
    }
    private JButton createIntersectChoiceButton(final int targetCellID) {
        final CellGraphics cellGraphics = cellsGraphics.get(targetCellID);
        JButton button = new JButton();
        button.setLocation(cellGraphics.getLocation());
        button.setBounds(cellGraphics.getLocation().x, cellGraphics.getLocation().y,
            CellGraphics.getCellWidth(), CellGraphics.getCellHeight());
        button.setBackground(Color.GREEN);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(final MouseEvent e) {
                resetState();
                controller.playerChooseIntersection(targetCellID);
                panels.get(INTERACTION).setVisible(false);
                removeLayer(INTERACTION);
            }
        });
        button.setVisible(true);
        return button;
    }

    // #endregion

    // #region Propriété

    private JPanel createPropertyPanel() {
        final JPanel panel = new JPanel();
        panel.setBounds(0, 0, getFrame().getWidth(), getFrame().getHeight());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));
        return panel;
    }

    // #endregion

    // #endregion

    // #region GUI 5

    private JPanel createGUI() {
        final JPanel panel = new JPanel(null);
        panel.setBounds(0, 0, getFrame().getWidth(), getFrame().getHeight());
        panel.setOpaque(false);
        playButton = new JButton();
        playButton.setBounds(0, 0, 400, 100);
        playButton.setVerticalAlignment(JButton.CENTER);
        playButton.setHorizontalAlignment(JButton.CENTER);
        panel.add(playButton);
        panel.setVisible(true);
        panels.put(GUI, panel);
        return panel;
    }

    // #endregion

    // #region Pause 7

    private JPanel createPause() {
        final JPanel panel = new JPanel(null);
        panel.setBounds(0, 0, getFrame().getWidth(), getFrame().getHeight());
        panel.setOpaque(false);
        panel.setVisible(false);
        panels.put(PAUSE, panel);
        return panel;
    }

    // #endregion

    private void removeLayer(final int layer) {
        SwingUtilities.invokeLater(() -> {
            if (panels.containsKey(layer)) {
                final JPanel panel = panels.get(layer);
                if (getObjectForLayer(layer) == layer) {
                    remove(panel);
                    panels.remove(layer);
                }
            }
            revalidate();
            repaint();
        });
    }

    @Override
    public void resized() {
        setBounds(0, 0, getFrame().getWidth(), getFrame().getHeight());
        for (int i : panels.keySet()) {
            final JPanel panel = panels.get(i);
            if (panel.isVisible()) {
                panel.setBounds(0, 0, getFrame().getWidth(), getFrame().getHeight());
            }
        }
        revalidate();
    }

    public boolean hasRendered() {
        return hasRendered;
    }

}
