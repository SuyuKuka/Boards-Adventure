package view.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;
import javax.swing.Timer;

import org.json.JSONArray;

import controller.Controller;
import view.JLayeredManager;
import view.View;
import view.game.boardpanel.PlayerGraphics;
import view.game.boardpanel.cellsgraphics.CellGraphics;

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
    private final JPanel overlayPanel;
    private final JPanel pausePanel;

    private final HashMap<Integer, CellGraphics> cellsGraphics;
    private final ArrayList<PlayerGraphics> playersGraphics;

    public GamePanel(final View frame, final Controller controller, final JSONArray jCells) {
        super(frame.getContentPane().getWidth(), frame.getContentPane().getHeight());
        this.frame = frame;
        this.controller = controller;

        cellsGraphics = new HashMap<>();
        playersGraphics = new ArrayList<>();

        mainPanel = new MainPanel(this, cellsGraphics, jCells, playersGraphics);
        overlayPanel = createOverlayPanel();
        pausePanel = createPausePanel();

        addComp(mainPanel, MAIN_PANEL);
        // addComp(overlayPanel, OVERLAY_PANEL);
        // addComp(pausePanel, PAUSE_PANEL);

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

        setVisible(true);
    }

    //#region Panels

    private JPanel createOverlayPanel() {
        final JPanel panel = new JPanel();
        panel.setBounds(getBounds());
        panel.setOpaque(false);
        return panel;
    }

    private JPanel createPausePanel() {
        final JPanel panel = new JPanel();
        panel.setOpaque(false);
        return panel;
    }

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

    public void playerMove() {
        mainPanel.playerMove();
    }

    public void playerArrived() {
        mainPanel.playerArrived();
    }


    public void endTurn() {
        mainPanel.endTurn();
    }

    public void gameEnd() {
        mainPanel.gameEnd();
    }

    /**
     * Joue une action après un délai.
     * @param delay délai en millisecondes.
     * @param action action réalisée APRES le délai.
     */
    public void startSingleActionTimer(final int delay, final ActionListener action) {
        final Timer timer = new Timer(delay, action);
        timer.setRepeats(false);
        timer.start();
    }

    //#endregion

    public View getFrame() {
        return frame;
    }

    public Controller getController() {
        return controller;
    }

    public HashMap<Integer, CellGraphics> getCellsGraphics() {
        return cellsGraphics;
    }

    public CellGraphics getCellGraphics(final int cellID) {
        return cellsGraphics.get(cellID);
    }

    public ArrayList<PlayerGraphics> getPlayersGraphics() {
        return playersGraphics;
    }

    public PlayerGraphics getPlayerGraphics(final int playerID) {
        return playersGraphics.get(playerID);
    }

}
