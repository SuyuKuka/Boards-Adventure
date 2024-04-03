package view.game;

import static game.Game.NANO_S;

import java.util.ArrayList;
import java.util.HashMap;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.Controller;
import controller.resources.Resources;
import model.GameState;
import model.config.cells.Intersection;
import model.config.cells.Property;
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
    private final GameMapGraphics gameMapGraphics;
    private final HashMap<Integer, CellGraphics> cellsGraphics;
    private final HashMap<Player, PlayerGraphics> playerGraphicsMap;

    private double zoomX;
    private double zoomY;

    private boolean hasRendered;
    private long lastGameUpdateTime;
    private double elaspedTime;

    private JButton playButton;

    public GamePanel(final View frame, final Controller controller, final GameMapGraphics gameMapGraphics) {
        super(frame);
        this.controller = controller;
        this.gameMapGraphics = gameMapGraphics;
        this.cellsGraphics = gameMapGraphics.getCellsGraphics();
        this.playerGraphicsMap = new HashMap<>();
        initValues();
        addPanel(createBackground(), BACKGROUND);
        addPanel(createCellsGraphics(), CELLS);
        addPanel(createPlayersGraphics(), PLAYERS);
        addPanel(createGUI(), GUI);
        addPanel(createPause(), PAUSE);
        setVisible(true);
    }

    private void initValues() {
        zoomX = ((double) getWidth() / getMinimumSize().width) * 1.897;
        zoomY = ((double) getHeight() / getMinimumSize().height) * 1.6;
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                zoomX = ((double) getWidth() / getMinimumSize().width) * 1.897;
                zoomY = ((double) getHeight() / getMinimumSize().height) * 1.6;
            }
        });
        elaspedTime = 0;
        hasRendered = false;
        lastGameUpdateTime = System.nanoTime();
        for (Player player : controller.getPlayers()) {
            playerGraphicsMap.put(player, new PlayerGraphics(player, gameMapGraphics));
        }
    }

    public void animate(final GameState gameState) {
        final double animationTime = gameState.getAnimationTime();
        if (animationTime != -1 && elaspedTime >= animationTime) {
            controller.setShouldStateUpdate(true);
            lastGameUpdateTime = System.nanoTime();
            elaspedTime = 0;
        } else {
            // animation des panels
        }
        elaspedTime = (double) (System.nanoTime() - lastGameUpdateTime) / NANO_S;
    }

    public void render(final GameState gameState) {
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
                        controller.setShouldStateUpdate(true);
                        controller.playerRollDice("normal");
                        playButton.removeMouseListener(playButton.getMouseListeners()[1]);
                    }
                });
                break;
            case PLAYER_MOVE:
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
            case PLAYER_ARRIVED:
                playerGraphicsMap.get(controller.getCurrentPlayer()).updatePosition();
                break;
            case START:
                break;
            case CARD:
                break;
            case DANGER:
                break;
            case GAIN:
                break;
            case LOSS:
                break;
            case KINGDOM:
                break;
            case VOID:
                break;
            case TELEPORTATION:
                break;
            case ASK_PROPERTY:
                addPanel(createPropertyPanel(), INTERACTION);
                revalidate();
                break;
            case ASK_SHOP:
                // addPanel(createShopPanel(), INTERACTION);
                break;
            case END_TURN:
                revalidate();
                playButton.setText("Fin du tour de " + player.getUsername() + ".");
                break;
            default:
                break;
        }
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
        final JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(final Graphics g) {
                super.paintComponent(g);
                Image image = Resources.getImage("board");
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
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

    public JPanel createPropertyPanel() {
        JPanel panel = SwingFactory.createBoxLayoutPanel();
        panel.setOpaque(true);
        panel.setBounds(getFrame().getWidth() / 4, getFrame().getHeight() / 4,
                getFrame().getWidth() - getFrame().getWidth() / 2, getFrame().getHeight() - getFrame().getHeight() / 2);

        Property property = (Property) controller.getCurrentPlayerCell();
        JLabel propertyName = SwingFactory.createTextLabel(
            "Veux tu acheter cette propriete ?\n",
            "RifficFree", Color.BLACK, 32);
        propertyName.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel propertyPrice = SwingFactory.createTextLabel("Nom de la propriete "
            + property.getBuyPrices().get(property.getPropertyState()) + "$\n",
            "RifficFree", Color.BLACK, 18
        );
        propertyPrice.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        JButton yes = new JButton("Oui");
        yes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                if (!property.hasOwner()) {
                    controller.playerBuyProperty(property);
                } else {
                    controller.playerUpgradeProperty(property);
                }
                controller.setShouldStateUpdate(true);
                removePanel(INTERACTION);
                revalidate();
            }
        });
        JButton no = new JButton("Non");
        no.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                controller.setShouldStateUpdate(true);
                removePanel(INTERACTION);
                revalidate();
            }
        });
        buttonPanel.add(yes);
        buttonPanel.add(no);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(propertyName);
        panel.add(propertyPrice);
        panel.add(buttonPanel);
        return panel;
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

}
