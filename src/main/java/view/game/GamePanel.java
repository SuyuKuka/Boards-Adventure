package view.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import org.json.JSONArray;
import org.json.JSONObject;

import controller.Controller;
import controller.resources.Resources;
import view.JLayeredManager;
import view.View;
import view.game.boardpanel.entitygraphics.PlayerGraphics;
import view.game.boardpanel.entitygraphics.cellsgraphics.CellGraphics;
import view.game.overlaypanel.OverlayPanel;

/**
 * Classe mère de la {@link View} EN JEU.
 * C'est elle qui fait le lien entre le {@link Controller} et tous les éléments graphiques que voient les joueurs.
 * <p> Elle étend {@link JLayeredManager} et contient 3 couches (en partant du bas) :
 * <p> - un {@link MainPanel} qui s'occupe de 99% du jeu
 * <p> - un {@link JPanel} "OverlayPanel" qui contient le bouton pause
 * <p> - un {@link JPanel} "PausePanel" qui contient le menu du pause si activé.
 */
public final class GamePanel extends JLayeredManager {

    private final View frame;
    private final Controller controller;

    // NUMEROS DES COUCHES
    private static final int MAIN_PANEL = 0;
    private static final int OVERLAY_PANEL = 1;
    private static final int PAUSE_PANEL = 2;

    // COUCHES
    private final MainPanel mainPanel;
    private final OverlayPanel overlayPanel;
    private final JPanel pausePanel;

    private HashMap<Integer, CellGraphics> cellsGraphics;
    private ArrayList<PlayerGraphics> playersGraphics;
    private HashMap<String, ImageIcon> itemsGraphics;

    public GamePanel(final View frame, final Controller controller, final JSONObject mapConfig) {
        super(frame.getContentPane().getWidth(), frame.getContentPane().getHeight());
        this.frame = frame;
        this.controller = controller;

        // cellsGraphics et playersGraphics sont load par boardPanel
        itemsGraphics = loadItemsGraphics(mapConfig);

        mainPanel = new MainPanel(this, mapConfig);
        overlayPanel = new OverlayPanel(this);
        pausePanel = createPausePanel();

        addComp(mainPanel, MAIN_PANEL);
        addComp(overlayPanel, OVERLAY_PANEL);
        addComp(pausePanel, PAUSE_PANEL);

        addResizeListener();

        setVisible(true);
    }

    @Override
    public void addResizeListener() {
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                final Dimension dim = getSize();
                setPreferredSize(dim);
                for (final Component comp : getLayersMap().values()) {
                    comp.setSize(dim);
                }
                revalidate();
                repaint();
            }
        });
    }

    //#region update

    //#endregion

    //#region gameState

    public void gameStart() {
        mainPanel.gameStart();
    }

    public void newTurn() {
        mainPanel.newTurn();
    }

    public void inJail() {
        mainPanel.inJail();
    }

    public void playerAction() {
        mainPanel.playerAction();
    }

    public void rollDice() {
        mainPanel.rollDice();
    }

    public void playerMove(final int startCellID) {
        mainPanel.playerMove(startCellID);
    }

    public void playerArrived() {
        mainPanel.playerArrived();
    }

    public void endTurn() {
        mainPanel.endTurn();
        repaint();
    }

    public void gameEnd() {
        mainPanel.gameEnd();
    }

    //#endregion

    public View getFrame() {
        return frame;
    }

    public Controller getController() {
        return controller;
    }

    //#region cellsGraphics

    public void setCellsGraphics(final HashMap<Integer, CellGraphics> cellsGraphics) {
        this.cellsGraphics = cellsGraphics;
    }

    public HashMap<Integer, CellGraphics> getCellsGraphics() {
        return cellsGraphics;
    }

    public CellGraphics getCellGraphics(final int cellID) {
        return cellsGraphics.get(cellID);
    }
    //#endregion

    //#region playersGraphics

    public void setPlayersGraphics(final ArrayList<PlayerGraphics> playersGraphics) {
        this.playersGraphics = playersGraphics;
    }

    public ArrayList<PlayerGraphics> getPlayersGraphics() {
        return playersGraphics;
    }

    public PlayerGraphics getPlayerGraphics(final int playerID) {
        return playersGraphics.get(playerID);
    }
    //#endregion

    //#region itemsGraphics

    private HashMap<String, ImageIcon> loadItemsGraphics(final JSONObject mapConfig) {
        final HashMap<String, ImageIcon> items = new HashMap<>();
        final JSONArray shopItems = mapConfig.getJSONArray("shop_items");
        for (int i = 0; i < shopItems.length(); i++) {
            final String itemName = shopItems.getJSONObject(i).getString("name").toLowerCase();
            items.put(itemName, new ImageIcon(Resources.getImage(itemName)));
        }
        final JSONArray startItems = mapConfig.getJSONArray("start_items");
        for (int i = 0; i < startItems.length(); i++) {
            final String itemName = shopItems.getJSONObject(i).getString("name").toLowerCase();
            if (!items.containsKey(itemName)) {
                items.put(itemName, new ImageIcon(Resources.getImage(itemName)));
            }
        }
        return items;
    }

    public HashMap<String, ImageIcon> getItemsGraphics() {
        return itemsGraphics;
    }

    public ImageIcon getItemGraphics(final String itemName) {
        return itemsGraphics.get(itemName);
    }

    //#endregion

    //#region Pause

    private JPanel createPausePanel() {
        final JPanel panel = new JPanel();
        panel.setOpaque(false);
        return panel;
    }

    //#endregion
}
