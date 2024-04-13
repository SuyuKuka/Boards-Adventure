package view.game;

import static game.Game.NANO_S;

import java.util.ArrayList;
import java.util.HashMap;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import controller.Controller;
import controller.resources.Resources;
import model.GameState;
import model.config.cells.Intersection;
import model.config.players.Player;
import view.CorePanel;
import view.SwingFactory;
import view.View;
import view.game.cellsgraphics.CellGraphics;

public final class GamePanel extends CorePanel {
    // LAYERS
    private static final int BACKGROUND = 0;
    private static final int CELLS = 1;
    private static final int PLAYERS = 2;
    private static final int INTERACTION = 3;
    private static final int GUI = 4;
    private static final int PAUSE = 5;

    private final Controller controller;

    private final HashMap<Integer, CellGraphics> cellsGraphics;
    private final HashMap<Player, PlayerGraphics> playerGraphicsMap;

    /** zoom sur la map. */
    private double zoom;
    /** zoom sur les cellules x. (TEMPORAIRE). */
    private double zoomX;
    /** zoom sur les cellules y. (TEMPORAIRE). */
    private double zoomY;

    private boolean hasRendered;
    private long lastGameUpdateTime;
    private double elaspedTime;

    private JButton playButton;

    public GamePanel(final View frame, final Controller controller,
        final HashMap<Integer, CellGraphics> cellsGraphics) {
        super(frame);
        this.controller = controller;
        this.cellsGraphics = cellsGraphics;
        this.playerGraphicsMap = new HashMap<>();
        initValues();
        addPanel(createBackground(), BACKGROUND);
        addPanel(createCellsGraphics(), CELLS);
        // addPanel(createPlayersGraphics(), PLAYERS);
        addPanel(createGUI(), GUI);
        setVisible(true);
    }

    private void initValues() {
        zoom = 5;
        zoomX = ((double) getWidth() / getMinimumSize().width);
        zoomY = ((double) getHeight() / getMinimumSize().height) * 0.9;
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                zoomX = ((double) getWidth() / getMinimumSize().width);
                zoomY = ((double) getHeight() / getMinimumSize().height) * 0.9;
            }
        });
        elaspedTime = 0;
        hasRendered = false;
        lastGameUpdateTime = System.nanoTime();
        for (Player player : controller.getPlayers()) {
            playerGraphicsMap.put(player, new PlayerGraphics(player, this));
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
            final Player player = controller.getCurrentPlayer();
            System.out.println(gameState);
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
                            controller.setShouldStateUpdate(true);
                            controller.playerRollDice("normal");
                            playButton.removeMouseListener(playButton.getMouseListeners()[1]);
                        }
                    });
                    break;
                case PLAYER_MOVE:
                    break;
                case PLAYER_ARRIVED:
                    playerGraphicsMap.get(controller.getCurrentPlayer()).updatePosition();
                    revalidate();
                    break;
                case NORMAL_CELL:
                    playButton.setText(controller.getCurrentPlayerCell().getCellType().name());
                    playerGraphicsMap.get(controller.getCurrentPlayer()).updatePosition();
                    break;
                case ASK_INTERSECTION:
                    playerGraphicsMap.get(player).updatePosition();
                    playButton.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mousePressed(final MouseEvent e) {
                            e.consume();
                        }
                    });
                    addPanel(createIntersectionPanel(), INTERACTION);
                    break;
                case ASK_PROPERTY:
                    addPanel(createPropertyPanel(), INTERACTION);
                    break;
                case ASK_SHOP:
                    addPanel(createShopPanel(), INTERACTION);
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
        final JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(final Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                AffineTransform at = new AffineTransform();
                Image image = Resources.getImage("board");
                final PlayerGraphics playerGraphics = playerGraphicsMap.get(controller.getCurrentPlayer());
                at.translate(getWidth() / 2, getHeight() / 2);
                at.scale(zoom, zoom);
                System.out.println(-playerGraphics.getX() * zoomX + " " + -playerGraphics.getY() * zoomY);
                at.translate(-playerGraphics.getX() * zoomX, -playerGraphics.getY() * zoomY);
                g2d.transform(at);
                g2d.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };
        return panel;
    }

    // #endregion

    // #region CellsGraphics 1

    private JPanel createCellsGraphics() {
        final JPanel panel = new JPanel(null);
        panel.setBounds(0, 0, getFrame().getWidth(), getFrame().getHeight());
        panel.setOpaque(false);
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                for (Integer key : cellsGraphics.keySet()) {
                    cellsGraphics.get(key).update(zoomX, zoomY);
                }
            }
        });
        for (Integer key : cellsGraphics.keySet()) {
            panel.add(cellsGraphics.get(key));
        }
        return panel;
    }

    // #endregion

    // #region PlayersGraphics 2

    private JPanel createPlayersGraphics() {
        final JPanel panel = new JPanel(null);
        panel.setOpaque(false);
        panel.setBounds(0, 0, getFrame().getWidth(), getFrame().getHeight());
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                for (PlayerGraphics playerGraphics : playerGraphicsMap.values()) {
                    playerGraphics.update(zoomX, zoomY);
                }
            }
        });
        for (Player player : playerGraphicsMap.keySet()) {
            panel.add(playerGraphicsMap.get(player));
        }
        panel.setVisible(true);
        return panel;
    }

    // #endregion

    // #region Interaction Player/Game 3

    // #region Cellule Intersection
    private JPanel createIntersectionPanel() {
        final JPanel panel = new JPanel(null);
        panel.setOpaque(false);
        panel.setBounds(0, 0, getFrame().getWidth(), getFrame().getHeight());
        final ArrayList<Integer> intersectIDs = ((Intersection) controller.getCurrentPlayerCell()).getNextCellIDs();
        for (int i = 0; i < intersectIDs.size(); i++) {
            panel.add(createIntersectChoiceButton(intersectIDs.get(i)));
        }
        panel.setVisible(true);
        return panel;
    }
    private JButton createIntersectChoiceButton(final int targetCellID) {
        final CellGraphics cellGraphics = cellsGraphics.get(targetCellID);
        JButton button = new JButton();
        button.setLocation(cellGraphics.getLocation());
        button.setBounds(cellGraphics.getLocation().x, cellGraphics.getLocation().y,
            cellGraphics.getCellWidth(), cellGraphics.getCellHeight());
        button.setBackground(Color.GREEN);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(final MouseEvent e) {
                removePanel(INTERACTION);
                controller.playerChooseIntersection(targetCellID);
                controller.setShouldStateUpdate(true);
            }
        });
        button.setVisible(true);
        return button;
    }

    // #endregion

    // #region Propriété

    private JPanel createPropertyPanel() {
        final JPanel container = SwingFactory.createContainerPanel(this, 0, 0, 0, 0);
        container.add(SwingFactory.createTransparentPanel(container, 100, 25));
        final JPanel panel = SwingFactory.createImagePanel(container, "button", 50, 50);
        container.add(panel);
        container.setVisible(true);
        return container;
    }

    // #endregion

    // #region Shop

    private JPanel createShopPanel() {
        final JPanel panel = new JPanel();
        panel.setBounds(0, 0, getFrame().getWidth(), getFrame().getHeight());
        // panel.setOpaque(false);
        panel.setBackground(Color.RED);
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                final int width = (int) (getFrame().getWidth() * 0.2);
                final int height = (int) (getFrame().getHeight() * 0.25);
                panel.setBorder(BorderFactory.createEmptyBorder(height, width, height, width));
            }
        });
        // panel.setVisible(true);
        return panel;
    }

    // #endregion

    // #endregion

    // #region GUI 4

    private JPanel createGUI() {
        final JPanel panel = new JPanel(null);
        panel.setBounds(0, 0, getFrame().getWidth(), getFrame().getHeight());
        panel.setOpaque(false);
        createPlayButton();
        panel.add(playButton);
        int height = 150;
        for (Player player : controller.getPlayers()) {
            add(createPlayerInfo(player, height));
            height += 100;
        }
        panel.setVisible(true);
        return panel;
    }

    private void createPlayButton() {
        playButton = new JButton();
        playButton.setBounds(0, 0, 200, 50);
        playButton.setVerticalAlignment(JButton.CENTER);
        playButton.setHorizontalAlignment(JButton.CENTER);
    }

    private JPanel createPlayerInfo(final Player player, final int startHeight) {
        final JPanel container = SwingFactory.createContainerPanel(this, 70, 70, 70, 70);
        final JPanel panel = SwingFactory.createImagePanel(container, "button", 100, 100);
        container.add(panel);
        return container;
    }

    // #endregion

    // #region Pause 5

    private JPanel createPause() {
        final JPanel panel = new JPanel(null);
        panel.setBounds(0, 0, getFrame().getWidth(), getFrame().getHeight());
        panel.setOpaque(false);
        panel.setVisible(false);
        return panel;
    }

    // #endregion

    private JPanel createBarrier() {
        final JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(final Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(0, 0, 0, 80));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setBounds(0, 0, getFrame().getWidth(), getFrame().getHeight());
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent e) {
                e.consume();
            }
        });
        panel.setOpaque(false);
        panel.setVisible(false);
        return panel;
    }

    public boolean hasRendered() {
        return hasRendered;
    }

    public double getZoom() {
        return zoom;
    }

}
