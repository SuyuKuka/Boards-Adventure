package view.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;
import javax.swing.Timer;

import controller.Controller;
import view.JLayeredManager;
import view.View;
import view.game.cellsgraphics.CellGraphics;

public final class GamePanel extends JLayeredManager {
    // LAYERS
    private static final int MAIN_PANEL = 0;
    private static final int OVERLAY_PANEL = 1;
    private static final int PAUSE_GAME = 2;

    private final View frame;
    private final Controller controller;

    private BoardPanel boardPanel;
    private ActionPanel actionPanel;
    private DataPanel dataPanel;

    private JPanel overlayPanel;

    private HashMap<Integer, CellGraphics> cellsGraphics;
    private ArrayList<PlayerGraphics> playerGraphicsList;

    public GamePanel(final View frame, final Controller controller) {
        super();
        setBounds(0, 0, frame.getWidth(), frame.getHeight());
        setMinimumSize(frame.getMinimumSize());
        setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight()));
        this.frame = frame;
        this.controller = controller;
        initPanels();
        addComp(createMainPanel(), MAIN_PANEL);
        addComp(overlayPanel, OVERLAY_PANEL);
        setVisible(true);
    }
    //#region Panels

    private void initPanels() {
        overlayPanel = createOverlayPanel();
        boardPanel = new BoardPanel(this);
        dataPanel = new DataPanel(this);
        actionPanel = new ActionPanel(this);
    }

    private JPanel createMainPanel() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.add(dataPanel, BorderLayout.WEST);
        panel.add(boardPanel, BorderLayout.CENTER);
        panel.add(actionPanel, BorderLayout.SOUTH);
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                final int dataPanelWidth = getWidth() * DataPanel.getWidthPer() / 100;
                final int dataPanelHeight = getHeight() * DataPanel.getHeightPer() / 100;
                dataPanel.setPreferredSize(new Dimension(dataPanelWidth, dataPanelHeight));

                final int actionPanelHeight = getHeight() - dataPanelHeight;
                actionPanel.setPreferredSize(new Dimension(getWidth(), actionPanelHeight));

                final int boardPanelWidth = getWidth() - dataPanelWidth;
                final int boardPanelHeight = getHeight() - actionPanelHeight;
                boardPanel.setPreferredSize(new Dimension(boardPanelWidth, boardPanelHeight));
                boardPanel.redraw();
            }
        });
        panel.setVisible(true);
        return panel;
    }

    private JPanel createOverlayPanel() {
        final JPanel panel = new JPanel();
        panel.setBounds(getBounds());
        panel.setOpaque(false);
        return panel;
    }

    private JPanel createPausePanel() {
        final JPanel panel = new JPanel();
        return panel;
    }

    //#endregion

    //#region gameState

    public void gameStart() {
        startSingleActionTimer(50, action -> boardPanel.gameStart());
    }

    public void newTurn() {
        boardPanel.newTurn();
    }

    public void inJail() {
        boardPanel.inJail();
    }

    public void playerAction() {
        actionPanel.playerAction();
    }

    public void playerMove() {
        boardPanel.playerMove();
    }

    public void playerArrived() {
        boardPanel.playerArrived();
    }


    public void endTurn() {
        boardPanel.endTurn();
        dataPanel.endTurn();
    }

    public void gameEnd() {
        // afficher un panel avec les points de chaque joueurs et bouton revenir au menu.
        return;
    }

    //#endregion

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

    public HashMap<Integer, CellGraphics> getCellsGraphics() {
        return cellsGraphics;
    }

    public Controller getController() {
        return controller;
    }

    public View getFrame() {
        return frame;
    }

    public ArrayList<PlayerGraphics> getPlayerGraphicsList() {
        return playerGraphicsList;
    }

    public PlayerGraphics getPlayerGraphics(final int playerID) {
        return playerGraphicsList.get(playerID);
    }

    public void setPlayerGraphicsList(final ArrayList<PlayerGraphics> playerGraphicsList) {
        this.playerGraphicsList = playerGraphicsList;
    }

    public void setCellsGraphics(final HashMap<Integer, CellGraphics> cellsGraphics) {
        this.cellsGraphics = cellsGraphics;
    }

}
