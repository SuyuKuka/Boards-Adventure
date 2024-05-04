package view.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import controller.Controller;
import controller.resources.Resources;
import model.config.cells.CellType;
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

    // CAMERA ET ZOOM
    private static final int CAMERA_MAX_WIDTH = 720;
    private static final int CAMERA_MAX_HEIGHT = 480;
    private static final double MIN_ZOOM = 0.5;
    private static final double MAX_ZOOM = 5;

    private double zoom = 1;
    private int cameraWidth = (int) (CAMERA_MAX_WIDTH / zoom);
    private int cameraHeight = (int) (CAMERA_MAX_HEIGHT / zoom);
    private int cameraX = 500;
    private int cameraY = 500;
    private Point cameraPanStartPos = null;
    private Point cameraPan = null;

    private Image boardImage;

    private JButton playButton;

    public GamePanel(final View frame, final Controller controller) {
        super(frame);
        this.controller = controller;
        boardImage = Resources.getImage("board");
        this.cellsGraphics = Resources.loadCellsGraphics(this, controller.getMapName());
        this.playerGraphicsMap = new HashMap<>();
        initValues();
        addPanel(createBackground(), BACKGROUND);
        addPanel(createCellsGraphics(), CELLS);
        addPanel(createPlayersGraphics(), PLAYERS);
        addPanel(createGUI(), GUI);
        addPanel(createPause(), PAUSE);
        setVisible(true);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    cameraPanStartPos = e.getPoint();
                    cameraPan = e.getPoint();
                }
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON2) {
                    // Quand le joueur clique molette sans bouger le curseur, téléporte la caméra
                    // sur le joueur courant.
                    // if (e.getPoint().equals(cameraPanStartPos)) {
                    //     final PlayerGraphics playerGraphics = playerGraphicsMap.get(controller.getCurrentPlayer());
                    //     updateCameraPosition(1, playerGraphics.getXPos(), playerGraphics.getYPos());
                    //     redraw();
                    // }
                    final PlayerGraphics playerGraphics = playerGraphicsMap.get(controller.getCurrentPlayer());
                    updateCameraPosition(1, playerGraphics.getXPos(), playerGraphics.getYPos());
                    redraw();
                    cameraPanStartPos = null;
                    cameraPan = null;
                }
            }
        });
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(final MouseWheelEvent e) {
                if (!e.isControlDown()) {
                    if (e.getWheelRotation() < 0) {
                        if (zoom <= MAX_ZOOM - 0.1) {
                            updateCameraPosition(zoom + 0.1, cameraX, cameraY);
                        }
                    } else {
                        if (zoom >= MIN_ZOOM + 0.1) {
                            updateCameraPosition(zoom - 0.1, cameraX, cameraY);
                        }
                    }
                    redraw();
                }
            }
        });
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e) && cameraPanStartPos != null) {
                    int deltaX = e.getX() - cameraPan.x;
                    int deltaY = e.getY() - cameraPan.y;
                    updateCameraPosition(zoom, cameraX + deltaX, cameraY + deltaY);
                    cameraPan = e.getPoint();
                    redraw();
                }
            }
        });
    }

    public void redraw() {
        repaint();
        for (CellGraphics cellGraphics : cellsGraphics.values()) {
            cellGraphics.update();
        }
        for (PlayerGraphics playerGraphics : playerGraphicsMap.values()) {
            playerGraphics.update();
        }
    }

    public void updateCameraPosition(final double nextZoom, final int nextCameraX, final int nextCameraY) {
        zoom = nextZoom;
        cameraWidth = (int) (CAMERA_MAX_WIDTH / zoom);
        cameraHeight = (int) (CAMERA_MAX_HEIGHT / zoom);
        cameraX = nextCameraX;
        cameraY = nextCameraY;
        if (cameraX < cameraWidth / 2) {
            cameraX = cameraWidth / 2;
        } else if (cameraX > boardImage.getWidth(null) - cameraWidth / 2) {
            cameraX = boardImage.getWidth(null) - cameraWidth / 2;
        }
        if (cameraY < cameraHeight / 2) {
            cameraY = cameraHeight / 2;
        } else if (cameraY > boardImage.getHeight(null) - cameraHeight / 2) {
            cameraY = boardImage.getHeight(null) - cameraHeight / 2;
        }
    }

    private void initValues() {
        for (Player player : controller.getPlayers()) {
            playerGraphicsMap.put(player, new PlayerGraphics(player, this));
        }
    }

    //#region gameState

    public void gameStart() {
        playButton.setText("Début du jeu !");
        startSingleActionTimer(1500, e -> controller.gameUpdate());
    }

    public void newTurn() {
        final Player currentPlayer = controller.getCurrentPlayer();
        playButton.setText("Tour " + controller.getCurrentTurn() + " de "
            + currentPlayer.getUsername());
        startSingleActionTimer(1500, e -> controller.gameUpdate());
    }

    public void inJail() {
        final Player currentPlayer = controller.getCurrentPlayer();
        playButton.setText(currentPlayer.getUsername() + " est en Prison.");
        startSingleActionTimer(1000, e -> controller.gameUpdate());
    }

    public void playerAction() {
        playButton.setText("Cliquez pour lancer le dé :");
        playButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                controller.rollDice();
                playButton.removeMouseListener(this);
            }
        });
    }

    public void rollDice() {
        // faire tourner le dé.
    }

    public void playerMove() {
        updateCurrentPlayerPosition();
        if (controller.getCurrentPlayerCellType() == CellType.INTERSECTION) {
            addPanel(createIntersectionPanel(), INTERACTION);
        } else {
            controller.gameUpdate();
        }
    }

    public void playerArrived() {
        updateCurrentPlayerPosition();
        final CellType cellType = controller.getCurrentPlayerCellType();
        playButton.setText(cellType.name().toUpperCase());
        // Faire les animations pour chaque cellType
        switch (cellType) {
            case GAIN:
                addPanel(createGainPanel(), INTERACTION);
                break;
            case LOSS:
                addPanel(createLossPanel(), INTERACTION);
                break;
            case KINGDOM:
                addPanel(createKingdomPanel(), INTERACTION);
                break;
            case PROPERTY:
                addPanel(createPropertyPanel(), INTERACTION);
                break;
            case SHOP:
                addPanel(createShopPanel(), INTERACTION);
                break;
            case CARD:
                System.out.println("AAAAAAAAAAAAAAA");
                addPanel(createCardPanel(), INTERACTION);
                break;
            default:
                startSingleActionTimer(500, e -> controller.gameUpdate());
                break;
        }
    }


    public void endTurn() {
        // animation de fin de tour
        if (controller.getTotalTurns() == controller.getCurrentTurn()) {
            playButton.setText("fin du jeu");
        } else {
            playButton.setText("fin du tour");
        }
        startSingleActionTimer(1000, e -> controller.gameUpdate());
    }

    public void gameEnd() {
        playButton.setText("fin du jeu.");
        // afficher un panel avec les points de chaque joueurs et bouton revenir au menu.
        return;
    }

    //#endregion

    public void updateCurrentPlayerPosition() {
        final PlayerGraphics playerGraphics = playerGraphicsMap.get(controller.getCurrentPlayer());
        playerGraphics.updateCoordinates();
        playerGraphics.update();
    }

    // #region Background 0

    private JPanel createBackground() {
        final JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(final Graphics g) {
                super.paintComponent(g);
                final int halfWidth = cameraWidth / 2;
                final int halfHeight = cameraHeight / 2;
                final int sx1 = cameraX - halfWidth;
                final int sy1 = cameraY - halfHeight;
                final int sx2 = cameraX + halfWidth;
                final int sy2 = cameraY + halfHeight;
                g.drawImage(boardImage, 0, 0, getWidth(), getHeight(), sx1, sy1, sx2, sy2, this);
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
        button.setSize(cellGraphics.getSize());
        button.setBackground(Color.GREEN);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(final MouseEvent e) {
                removePanel(INTERACTION);
                controller.setPlayerPosition(targetCellID);
                controller.gameUpdate();
            }
        });
        button.setVisible(true);
        return button;
    }

    // #endregion

    // #region Gain

    private JPanel createGainPanel() {
        // System.out.println(controller.getCurrentPlayer().getTeam().getMoney()); //Verification de sa money
        JPanel resizePanel = SwingFactory.createContainerPanel(this, 20, 20, 20, 20);
        resizePanel.setVisible(true);

        JPanel background = SwingFactory.createGamePanelCells(this, 60, 60);
        background.setBackground(Color.CYAN);
        background.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));

        controller.setCurrentCell();
        controller.playerGainMoney();
        int gain = controller.getGainedMoney();

        JLabel gainLabel = SwingFactory.createTextLabel("Tu as gagne " + gain + " $ !\n",
            "RifficFree", Color.BLACK, 32);
        gainLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        background.add(gainLabel);

        createCloseTimerLabel(background);
        resizePanel.add(background);
        return resizePanel;
    }

    // #endregion

    //#region Loss

    private JPanel createLossPanel() {
        // System.out.println(controller.getCurrentPlayer().getTeam().getMoney()); //Verification de sa money
        JPanel resizePanel = SwingFactory.createContainerPanel(this, 20, 20, 20, 20);
        resizePanel.setVisible(true);

        JPanel background = SwingFactory.createGamePanelCells(this, 60, 60);
        background.setBackground(Color.CYAN);
        background.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));

        controller.setCurrentCell();
        controller.playerLossMoney();
        int loss = controller.getLossMoney();

        JLabel lossLabel = SwingFactory.createTextLabel("Tu as perdu " + loss + " $ !\n",
            "RifficFree", Color.BLACK, 32);
            lossLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            background.add(lossLabel);
        // System.out.println(controller.getCurrentPlayer().getTeam().getMoney());

        createCloseTimerLabel(background);
        resizePanel.add(background);
        return resizePanel;
    }

    //#endregion

    // #region Kingdom

    private JPanel createKingdomPanel() {
        JPanel resizePanel = SwingFactory.createContainerPanel(this, 20, 20, 20, 20);
        resizePanel.setVisible(true);

        JPanel background = SwingFactory.createGamePanelCells(this, 60, 60);
        background.setBackground(Color.CYAN);
        background.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));

        controller.setCurrentCell();

        JLabel kingdomLabel = SwingFactory.createTextLabel("Tu viens de gagner un point en plus !\n",
            "RifficFree", Color.BLACK, 32);
        kingdomLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        background.add(kingdomLabel);

        JLabel pointsLabel = SwingFactory.createTextLabel("Tu as maintenant " + controller.getGamePoint()
        + " points !\n", "RifficFree", Color.BLACK, 26);
        pointsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        background.add(pointsLabel);

        createCloseTimerLabel(background);
        resizePanel.add(background);
        return resizePanel;
    }

    // #endregion

    // #region Property

    private JPanel createPropertyPanel() {
        JPanel resizePanel = SwingFactory.createContainerPanel(this, 20, 20, 20, 20);
        resizePanel.setVisible(true);

        JPanel background = SwingFactory.createGamePanelCells(this, 60, 60);
        background.setBackground(Color.CYAN);
        background.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));

        controller.setCurrentCell();

        if (!controller.propertyHasOwner()) {
            createBuyAndUpgradePanel(background);
        } else if (!controller.playerIsAtHome() && controller.propertyHasOwner()) {
            createRentPanel(background);
        }

        resizePanel.add(background);
        return resizePanel;
    }

    private void createBuyAndUpgradePanel(JPanel panel) {
        int price = controller.getPropertyPrice();
        String name = "\"Nom de la propriete\"";
        JLabel propertyLabel = SwingFactory.createTextLabel("Veux tu ameliorer cette propriete ?\n",
                "RifficFree", Color.BLACK, 32);
        propertyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        if (controller.playerIsAtHome()) {
            propertyLabel.setText("Veux tu ameliorer cette propriete ?\n");
        } else {
            propertyLabel.setText("Veux tu acheter cette propriete ?\n");
        }
        panel.add(propertyLabel);

        JLabel propertyPrice = SwingFactory.createTextLabel(name + " " + price + "$\n",
            "RifficFree", Color.BLACK, 18);
        propertyPrice.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(propertyPrice);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton yes = new JButton("Oui");
        yes.setFocusPainted(false);
        yes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                if (!controller.propertyHasOwner()) {
                    controller.playerBuyProperty();
                } else if (controller.playerIsAtHome() && controller.propertyHasOwner()) {
                    controller.playerUpgradeProperty();
                }
                controller.gameUpdate();
                removePanel(INTERACTION);
                revalidate();
            }
        });

        JButton no = new JButton("Non");
        no.setFocusPainted(false);
        no.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                controller.gameUpdate();
                removePanel(INTERACTION);
                revalidate();
            }
        });
        buttonPanel.add(yes);
        buttonPanel.add(no);

        panel.add(buttonPanel);
    }

    private void createRentPanel(JPanel panel) {
        JTextPane rentTextPane = new JTextPane();
        rentTextPane.setText("Oops, tu as atterri sur la propriété de quelqu'un d'autre. Tu dois payer "
        + controller.getRentPrice() + "$.\n");
        rentTextPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        rentTextPane.setFont(Resources.getFont("RifficFree", 32));
        rentTextPane.setForeground(Color.BLACK);
        rentTextPane.setEditable(false);
        rentTextPane.setFocusable(false);
        rentTextPane.setPreferredSize(new Dimension(WIDTH, 50));

        //Center le texte du rentTextPane
        StyledDocument doc = rentTextPane.getStyledDocument();
        Style centerStyle = rentTextPane.addStyle("CenterStyle", null);
        StyleConstants.setAlignment(centerStyle, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), centerStyle, false);

        panel.add(rentTextPane);

        JLabel moneyLabel = SwingFactory.createTextLabel("Il te reste " + controller.getCurrentMoney() + "$.\n",
        "RifficFree", Color.BLACK, 26);
        moneyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(moneyLabel);

        createCloseTimerLabel(panel);

        JLabel voidLabel = new JLabel();
        voidLabel.setBorder(BorderFactory.createEmptyBorder((int) (panel.getHeight() / 1.7), 0, 0, 0));
        panel.add(voidLabel);
    }

    // #endregion

    // #region Shop

    private JPanel createShopPanel() {
        JPanel resizePanel = SwingFactory.createContainerPanel(this, 20, 20, 20, 20);
        resizePanel.setVisible(true);

        JPanel background = SwingFactory.createGamePanelCells(this, 60, 60);
        background.setLayout(new BorderLayout());
        background.setBackground(Color.CYAN);
        background.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));

        controller.setCurrentCell();

        JPanel top = new JPanel();
        top.setLayout(new BorderLayout());

        JButton close = new JButton("X");
        close.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                controller.gameUpdate();
                removePanel(INTERACTION);
                revalidate();
            }
        });
        top.add(close, BorderLayout.EAST);

        JLabel shop = SwingFactory.createTextLabel("Boutique", "RifficFree", Color.BLACK, 32);
        top.add(shop, BorderLayout.CENTER);

        //LISTE ITEMS

        background.add(top, BorderLayout.NORTH);

        resizePanel.add(background);
        return resizePanel;
    }

    // #endregion

    // #region Card

    public JPanel createCardPanel() {
        JPanel resizePanel = SwingFactory.createContainerPanel(this, 20, 20, 20, 20);
        resizePanel.setVisible(true);

        JPanel background = SwingFactory.createGamePanelCells(this, 60, 60);
        background.setLayout(new BorderLayout());
        background.setBackground(Color.CYAN);
        background.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));

        controller.setCurrentCell();

        JButton open = new JButton("X");
        open.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                controller.playShowCard();
                removePanel(INTERACTION);
                revalidate();
                String test = controller.getCardEffect();

            System.out.println(test);
            }
        });
        background.add(open);

        resizePanel.add(background);
        return resizePanel;
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
        playButton.setFocusPainted(false);
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

    public void createCloseTimerLabel(JPanel panel) {
        final int[] remainingSeconds = {2};

        JLabel closeLabel = SwingFactory.createTextLabel("Fermeture automatique dans " + remainingSeconds[0] + "s",
        "RifficFree", Color.BLACK, 16);
        closeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(closeLabel);

        Timer timer = new Timer(1000, e -> {
            remainingSeconds[0]--;
            if (remainingSeconds[0] > 0) {
                closeLabel.setText("Fermeture automatique dans " + remainingSeconds[0] + "s");
            } else {
                removePanel(INTERACTION);
                panel.revalidate();
                controller.gameUpdate();
                ((Timer) e.getSource()).stop();
            }
        });
        timer.start();
    }

    /**
     * Joue une action après un délai.
     * @param delay délai en millisecondes.
     * @param action action réalisée APRES le délai.
     */
    private void startSingleActionTimer(final int delay, final ActionListener action) {
        final Timer timer = new Timer(delay, action);
        timer.setRepeats(false);
        timer.start();
    }

    public HashMap<Integer, CellGraphics> getCellsGraphics() {
        return cellsGraphics;
    }

    public double getZoom() {
        return zoom;
    }

    public int getCameraX() {
        return cameraX;
    }

    public int getCameraY() {
        return cameraY;
    }

    public int getCameraHeight() {
        return cameraHeight;
    }

    public int getCameraWidth() {
        return cameraWidth;
    }

    public static int getCameraMaxWidth() {
        return CAMERA_MAX_WIDTH;
    }

    public static int getCameraMaxHeight() {
        return CAMERA_MAX_HEIGHT;
    }

    public Controller getController() {
        return controller;
    }

    public Image getBoardImage() {
        return boardImage;
    }

}
