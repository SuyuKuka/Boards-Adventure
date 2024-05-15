package view.game.boardpanel;

import controller.Controller;
import controller.resources.Resources;
import view.game.GamePanel;
import view.game.boardpanel.cellsgraphics.CellGraphics;
import view.game.boardpanel.cellsgraphics.CellGraphicsFactory;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;

import javax.swing.Timer;

import java.util.ArrayList;
import java.util.HashMap;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.json.JSONArray;
import org.json.JSONObject;

import model.config.cells.CellType;
import model.config.cells.Intersection;
import view.JLayeredManager;
import view.SwingFactory;
import view.View;

public final class BoardPanel extends JLayeredManager {

    private final GamePanel gamePanel;
    private final Controller controller;

    // NUMERO DES COUCHES
    private static final int BACKGROUND = 0;
    private static final int CELLS = 1;
    private static final int PLAYERS = 2;
    private static final int INTERACTION = 3;

    // COUCHES
    private final JPanel backgroundPanel;
    private final JPanel cellsPanel;
    private final JPanel playersPanel;

    private final HashMap<Integer, CellGraphics> cellsGraphics;
    private final ArrayList<PlayerGraphics> playersGraphics;
    private final ArrayList<EntityGraphics> entitiesGraphics;

    // CAMERA ET ZOOM
    private static final int WIDE_ANGLE_SAFETY = 3;
    private static final int CAMERA_MAX_WIDTH = 720;
    private static final int CAMERA_MAX_HEIGHT = 480;
    private static final double MIN_ZOOM = 0.5;
    private static final double MAX_ZOOM = 5;

    private double zoom = 1;
    private int cameraWidth = (int) (CAMERA_MAX_WIDTH / zoom);
    private int cameraHeight = (int) (CAMERA_MAX_HEIGHT / zoom);
    private double cameraX;
    private double cameraY;
    private Point cameraPan = null;
    private int wideAngleAccumulator = 0;
    private boolean wideAngle = false;

    private final Image boardImage;

    public BoardPanel(final GamePanel gamePanel, final JSONObject mapConfig,
            final int width, final int height) {
        super(width, height);
        this.gamePanel = gamePanel;
        this.controller = gamePanel.getController();
        this.boardImage = Resources.getImage("board");
        setOpaque(true);
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(width, height));

        CellGraphics.updateCellSize(mapConfig.getInt("cell_diameter"));
        this.cellsGraphics = loadCellsGraphics(mapConfig.getJSONArray("cells"));
        gamePanel.setCellsGraphics(cellsGraphics);
        entitiesGraphics = new ArrayList<>();
        this.playersGraphics = loadPlayerGraphics();
        gamePanel.setPlayersGraphics(playersGraphics);

        final PlayerGraphics currentPlayerGraphics = playersGraphics.get(controller.getCurrentPlayerID());
        updateCameraPosition(zoom, currentPlayerGraphics.getXPos(), currentPlayerGraphics.getYPos());

        backgroundPanel = createBackgroundPanel();
        cellsPanel = createCellsPanel();
        playersPanel = createPlayersPanel();

        addComp(backgroundPanel, BACKGROUND);
        addComp(cellsPanel, CELLS);
        addComp(playersPanel, PLAYERS);
        redraw();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                final Dimension dim = getSize();
                for (final Component comp : getLayersMap().values()) {
                    comp.setSize(dim);
                }
                redraw();
                revalidate();
                repaint();
            }
        });

        addMouseListeners();

        setVisible(true);
    }
    //#region MouseListeners
    private void addMouseListeners() {
        addMouseListener();
        addMouseWheelListener();
        addMouseMotionListener();
    }

    private void addMouseListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3 && !wideAngle) {
                    cameraPan = e.getPoint();
                }
            }
            @Override
            public void mouseReleased(final MouseEvent e) {
                final int button = e.getButton();
                if (button == MouseEvent.BUTTON3 && !wideAngle) {
                    cameraPan = null;
                    getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
                if (button == MouseEvent.BUTTON2 && cameraPan == null) {
                    final PlayerGraphics playerGraphics =
                        gamePanel.getPlayerGraphics(gamePanel.getController().getCurrentPlayerID());
                    updateCameraPosition(1, playerGraphics.getXPos(), playerGraphics.getYPos());
                    redraw();
                    cameraPan = null;
                }
            }
        });
    }

    private void addMouseWheelListener() {
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(final MouseWheelEvent e) {
                if (!e.isControlDown()) {
                    if (e.getWheelRotation() < 0) {
                        // molette haut/zoom
                        if (wideAngle) {
                            wideAngle = false;
                        }
                        if (zoom <= MAX_ZOOM - 0.1) {
                            updateCameraPosition(zoom + 0.1, cameraX, cameraY);
                        }
                    } else {
                        // molette bas/dezoom
                        if (zoom >= MIN_ZOOM + 0.1) {
                            updateCameraPosition(zoom - 0.1, cameraX, cameraY);
                        } else if (!wideAngle && cameraPan == null) {
                            if (wideAngleAccumulator < WIDE_ANGLE_SAFETY) {
                                wideAngleAccumulator++;
                            } else {
                                wideAngle = true;
                                updateWideAngleCamera();
                            }
                        }
                    }
                    redraw();
                }
            }
        });
    }

    private void addMouseMotionListener() {
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(final MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e) && cameraPan != null) {
                    getFrame().setCursor(new Cursor(Cursor.MOVE_CURSOR));
                    final double deltaX = (e.getX() - cameraPan.x) / getWidthRatio();
                    final double deltaY = (e.getY() - cameraPan.y) / getHeightRatio();
                    updateCameraPosition(zoom, cameraX - deltaX, cameraY - deltaY);
                    cameraPan = e.getPoint();
                    redraw();
                }
            }
        });
    }
    //#endregion MouseListeners

    //#region Loading entities

    private HashMap<Integer, CellGraphics> loadCellsGraphics(final JSONArray jCells) {
        final HashMap<Integer, CellGraphics> cells = new HashMap<>();
        for (int i = 0; i < jCells.length(); i++) {
            final JSONObject jCell = jCells.getJSONObject(i);
            cells.put(jCell.getInt("id"), CellGraphicsFactory.createCellGraphics(this, jCell));
        }
        return cells;
    }

    private ArrayList<PlayerGraphics> loadPlayerGraphics() {
        final ArrayList<PlayerGraphics> players = new ArrayList<>();
        gamePanel.getController().getPlayers().forEach((player) -> {
            final CellGraphics cell = cellsGraphics.get(controller.getStartCell().getId());
            players.add(new PlayerGraphics(player.getID(), this, cell.getXPos(), cell.getYPos()));
        });
        return players;
    }

    //#endregion Loading entities

    //#region Camera

    public void redraw() {
        CellGraphics.updatePaintVariables(getWidthRatio(), getHeightRatio());
        for (CellGraphics cellGraphics : cellsGraphics.values()) {
            cellGraphics.update();
        }
        entitiesGraphics.forEach(entityGraphics -> entityGraphics.update());
        gamePanel.getPlayersGraphics().forEach(playerGraphics -> playerGraphics.update());
        revalidate();
        repaint();
    }

    public void updateCameraPosition(final double nextZoom, final double nextCameraX, final double nextCameraY) {
        zoom = nextZoom;
        cameraWidth = (int) (CAMERA_MAX_WIDTH / zoom);
        cameraHeight = (int) (CAMERA_MAX_HEIGHT / zoom);
        cameraX = nextCameraX;
        cameraY = nextCameraY;
        repositionCamera();
    }

    private void updateWideAngleCamera() {
        cameraWidth = boardImage.getWidth(null);
        cameraHeight = boardImage.getHeight(null);
        cameraX = cameraWidth / 2;
        cameraY = cameraHeight / 2;
    }

    private void repositionCamera() {
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

    //#endregion Camera

    //#region GameState
    public void gameStart() {
        addComp(createGameStartBanner(), INTERACTION);
        gamePanel.startSingleActionTimer(1500, action -> {
            removeComp(INTERACTION);
            controller.gameUpdate();
        });
    }

    public void newTurn() {
        addComp(createNewTurnBanner(), INTERACTION);
        gamePanel.startSingleActionTimer(1500, action -> {
            removeComp(INTERACTION);
            controller.gameUpdate();
        });
    }

    public void inJail() {
        addComp(createInJailBanner(), INTERACTION);
        gamePanel.startSingleActionTimer(1500, action -> {
            removeComp(INTERACTION);
            controller.gameUpdate();
        });
    }

    public void playerMove() {
        updateCurrentPlayerGraphicsPosition();
        if (controller.getCurrentPlayerCellType() == CellType.INTERSECTION) {
            addComp(createIntersectionPanel(), INTERACTION);
        } else {
            controller.gameUpdate();
        }
    }

    public void playerArrived() {
        updateCurrentPlayerGraphicsPosition();
        final CellType cellType = controller.getCurrentPlayerCellType();
        switch (cellType) {
            case GAIN:
                addComp(createGainPanel(), INTERACTION);
                break;
            case LOSS:
                addComp(createLossPanel(), INTERACTION);
                break;
            case KINGDOM:
                addComp(createKingdomPanel(), INTERACTION);
                break;
            case PROPERTY:
                addComp(createPropertyPanel(), INTERACTION);
                break;
            case SHOP:
                addComp(createShopPanel(), INTERACTION);
                break;
            case CARD:
                addComp(createCardPanel(), INTERACTION);
                break;
            default:
                gamePanel.startSingleActionTimer(500, e -> controller.gameUpdate());
                break;
        }
    }

    public void endTurn() {
        addComp(createEndTurnBanner(), INTERACTION);
        gamePanel.startSingleActionTimer(1000, action -> {
            removeComp(INTERACTION);
            controller.gameUpdate();
        });
    }

    public void gameEnd() {
        System.out.println("gameend !!");
    }

    private void updateCurrentPlayerGraphicsPosition() {
        final PlayerGraphics playerGraphics = playersGraphics.get(controller.getCurrentPlayerID());
        final CellGraphics cell = cellsGraphics.get(controller.getCurrentPlayer().getPosition());
        playerGraphics.updateCoordinates(cell.getXPos(), cell.getYPos());
        playerGraphics.update();
    }

    //#endregion gameState

    // #region Background 0

    private JPanel createBackgroundPanel() {
        final JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(final Graphics g) {
                super.paintComponent(g);
                final Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                final int halfWidth = cameraWidth / 2;
                final int halfHeight = cameraHeight / 2;
                final int sx1 = (int) (cameraX - halfWidth);
                final int sy1 = (int) (cameraY - halfHeight);
                final int sx2 = (int) (cameraX + halfWidth);
                final int sy2 = (int) (cameraY + halfHeight);
                g.drawImage(boardImage, 0, 0, getWidth(), getHeight(), sx1, sy1, sx2, sy2, this);
            }
        };
        panel.setSize(getSize());
        panel.setVisible(true);
        return panel;
    }

    // #endregion

    // #region CellsGraphics 1

    private JPanel createCellsPanel() {
        final JPanel panel = new JPanel(null);
        panel.setSize(getSize());
        panel.setOpaque(false);
        for (Integer key : cellsGraphics.keySet()) {
            panel.add(cellsGraphics.get(key));
        }
        panel.setVisible(true);
        return panel;
    }

    // #endregion

    // #region PlayersGraphics 2

    private JPanel createPlayersPanel() {
        final JPanel panel = new JPanel(null);
        panel.setSize(getSize());
        panel.setOpaque(false);
        playersGraphics.forEach((playerGraphics) -> {
            panel.add(playerGraphics);
        });
        panel.setVisible(true);
        return panel;
    }

    // #endregion

    // #region Interaction 3

    // #region Banner

    private JPanel createGameStartBanner() {
        final JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(final Graphics g) {
                super.paintComponent(g);
                final Color backgroundColor = new Color(0, 0, 0, 150);
                g.setColor(backgroundColor);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setOpaque(false);
        panel.setBounds(0, 0, getWidth(), getHeight());
        final String labelString = "Début du jeu !";
        final JLabel label = SwingFactory.createJLabel();
        label.setText(labelString);
        panel.add(label, BorderLayout.CENTER);
        panel.setVisible(true);
        return panel;
    }

    private JPanel createNewTurnBanner() {
        final JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(final Graphics g) {
                super.paintComponent(g);
                final Color backgroundColor = new Color(0, 0, 0, 150);
                g.setColor(backgroundColor);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setOpaque(false);
        panel.setBounds(0, 0, getWidth(), getHeight());
        final String labelString = "Tour de " + controller.getCurrentPlayer().getName();
        final JLabel label = SwingFactory.createJLabel();
        label.setText(labelString);
        panel.add(label, BorderLayout.CENTER);
        panel.setVisible(true);
        return panel;
    }

    private JPanel createInJailBanner() {
        final JPanel panel = new JPanel();
        panel.setOpaque(false);
        return panel;
    }

    private JPanel createEndTurnBanner() {
        final JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(final Graphics g) {
                super.paintComponent(g);
                final Color backgroundColor = new Color(0, 0, 0, 150);
                g.setColor(backgroundColor);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setBounds(0, 0, getWidth(), getHeight());
        panel.setOpaque(false);
        final String labelString = "FIN DU TOUR !";
        final JLabel label = SwingFactory.createJLabel();
        label.setText(labelString);
        panel.add(label, BorderLayout.CENTER);
        panel.setVisible(true);
        return panel;
    }

    // #endregion

    // #region Intersection
    public JPanel createIntersectionPanel() {
        final JPanel panel = new JPanel(null);
        panel.setBounds(0, 0, getWidth(), getHeight());
        panel.setOpaque(false);
        final ArrayList<Integer> intersectIDs = ((Intersection) controller.getCurrentPlayerCell()).getNextCellIDs();
        intersectIDs.forEach(cellID -> {
            final CellGraphics cell = CellGraphicsFactory.createIntersectionChoice(this, cellsGraphics.get(cellID));
            entitiesGraphics.add(cell);
            panel.add(cell);
        });
        panel.setVisible(true);
        return panel;
    }

    // #endregion

    // #region Gain

    public JPanel createGainPanel() {
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

    public JPanel createLossPanel() {
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

    public JPanel createKingdomPanel() {
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

    public JPanel createPropertyPanel() {
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
                removeComp(INTERACTION);
                if (!controller.propertyHasOwner()) {
                    controller.playerBuyProperty();
                } else if (controller.playerIsAtHome() && controller.propertyHasOwner()) {
                    controller.playerUpgradeProperty();
                }
                controller.gameUpdate();
                revalidate();
            }
        });

        JButton no = new JButton("Non");
        no.setFocusPainted(false);
        no.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                removeComp(INTERACTION);
                controller.gameUpdate();
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

    public JPanel createShopPanel() {
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
                removeComp(INTERACTION);
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
                removeComp(INTERACTION);
                controller.gameUpdate();
                revalidate();
            }
        });
        background.add(open);

        resizePanel.add(background);
        return resizePanel;
    }

    // #endregion

    private void createCloseTimerLabel(JPanel panel) {
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
                removeComp(INTERACTION);
                panel.revalidate();
                controller.gameUpdate();
                ((Timer) e.getSource()).stop();
            }
        });
        timer.start();
    }

    // #endregion INTERACTION

    public int getPanelWidth() {
        final Rectangle bounds = getBounds();
        return bounds.width - bounds.x;
    }

    public int getPanelHeight() {
        final Rectangle bounds = getBounds();
        return bounds.height - bounds.y;
    }

    public View getFrame() {
        return gamePanel.getFrame();
    }

    public Controller getController() {
        return gamePanel.getController();
    }

    public double getZoom() {
        return zoom;
    }

    public double getCameraX() {
        return cameraX;
    }

    public double getCameraY() {
        return cameraY;
    }

    public double getWidthRatio() {
        return getPreferredSize().getWidth() / cameraWidth;
    }

    public double getHeightRatio() {
        return getPreferredSize().getHeight() / cameraHeight;
    }

    public int getCameraHeight() {
        return cameraHeight;
    }

    public int getCameraWidth() {
        return cameraWidth;
    }

    public static int getCameraMaxHeight() {
        return CAMERA_MAX_HEIGHT;
    }

    public static int getCameraMaxWidth() {
        return CAMERA_MAX_WIDTH;
    }

    public Image getBoardImage() {
        return boardImage;
    }

    public static int getInteraction() {
        return INTERACTION;
    }

}
