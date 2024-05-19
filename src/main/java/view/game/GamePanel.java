package view.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

import org.json.JSONObject;

import controller.Controller;
import controller.resources.Resources;
import view.JLayeredManager;
import view.SwingFactory;
import view.View;
import view.game.boardpanel.entitygraphics.PlayerGraphics;
import view.game.boardpanel.entitygraphics.cellsgraphics.CellGraphics;
import view.game.boardpanel.entitygraphics.cellsgraphics.PropertyGraphics;
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
    private JPanel pausePanel;

    public GamePanel(final View frame, final Controller controller, final JSONObject mapConfig) {
        super(frame.getContentPane().getWidth(), frame.getContentPane().getHeight());
        this.frame = frame;
        this.controller = controller;

        mainPanel = new MainPanel(this, mapConfig);
        overlayPanel = new OverlayPanel(this);
        pausePanel = createPausePanel();

        addComp(mainPanel, MAIN_PANEL);
        addComp(overlayPanel, OVERLAY_PANEL);
        addComp(pausePanel, PAUSE_PANEL);
        pausePanel.setVisible(false);

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
        // gérer le cas ou c'est un ordi
        mainPanel.playerAction();
    }

    public void rollDice() {
        mainPanel.rollDice();
    }

    public void playerMove(final int diceRollValue) {
        mainPanel.playerMove(diceRollValue);
    }

    public void playerArrived(final boolean isStarCell) {
        mainPanel.playerArrived(isStarCell);
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

    public OverlayPanel getOverlayPanel() {
        return overlayPanel;
    }

    //#region cellsGraphics


    public HashMap<Integer, CellGraphics> getCellsGraphics() {
        return controller.getCellsGraphics();
    }

    public PropertyGraphics getPropertyGraphics(final int cellID) {
        return (PropertyGraphics) getCellGraphics(cellID);
    }

    public HashMap<String, ArrayList<Integer>> getPropertiesInfo() {
        return controller.getPropertiesInfo();
    }

    public CellGraphics getCellGraphics(final int cellID) {
        return controller.getCellGraphics(cellID);
    }

    public JPanel getPausePanel() {
        return pausePanel;
    }

    //#endregion

    //#region playersGraphics

    public ArrayList<PlayerGraphics> getPlayersGraphics() {
        return controller.getPlayersGraphics();
    }

    public PlayerGraphics getPlayerGraphics(final int playerID) {
        return controller.getPlayerGraphics(playerID);
    }
    //#endregion

    //#region Pause

    public JPanel createPausePanel() {
        JPanel background = SwingFactory.createGamePanelCells(frame.getContentPane(), 100, 100);
        background.setLayout(new BorderLayout());
        background.setBackground(Color.CYAN);
        background.setBorder(BorderFactory.createLineBorder(Color.GRAY, 4));

        JPanel top = new JPanel(new BorderLayout());
        JLabel shop = new JLabel("Settings", SwingConstants.CENTER);
        shop.setFont(Resources.getFont("NoitaBlackletter", 64));
        top.setBackground(Color.LIGHT_GRAY);
        top.add(shop, BorderLayout.CENTER);

        JButton close = SwingFactory.createImageButton(frame.getContentPane(), "delete", 5, 5, false);
        close.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent e) {
                pausePanel.setVisible(false);
                revalidate();
            }
        });
        top.add(close, BorderLayout.EAST);
        background.add(top, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(SwingFactory.createTransparentPanel(panel, 100, 35));

        final JPanel musicPanel = new JPanel();
        musicPanel.setLayout(new BoxLayout(musicPanel, BoxLayout.X_AXIS));
        musicPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        musicPanel.setOpaque(false);
        musicPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        final JLabel musique = SwingFactory.createTextLabel("Musique : - ", Color.BLACK, 64);
        final JSlider musicSlider = SwingFactory.createMusicSlider();
        final JLabel plusMusic = SwingFactory.createTextLabel(" +", Color.BLACK, 64);
        musicPanel.add(musique);
        musicPanel.add(musicSlider);
        musicPanel.add(plusMusic);
        panel.add(musicPanel);

        final JPanel soundPanel = new JPanel();
        soundPanel.setLayout(new BoxLayout(soundPanel, BoxLayout.X_AXIS));
        soundPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        soundPanel.setOpaque(false);
        soundPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        final JLabel sound = SwingFactory.createTextLabel("Son : - ", Color.BLACK, 64);
        final JSlider soundSlider = SwingFactory.createSoundSlider();
        final JLabel plusSound = SwingFactory.createTextLabel(" +", Color.BLACK, 64);
        soundPanel.add(sound);
        soundPanel.add(soundSlider);
        soundPanel.add(plusSound);
        panel.add(soundPanel);
        background.add(panel, BorderLayout.CENTER);

        JLabel back = SwingFactory.createTextLabel("Retourner au menu principal", Color.BLACK, 64);
        back.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent e) {
                frame.getGame().startMenu();
            }
        });
        background.add(back, BorderLayout.SOUTH);
        return background;
    }

    //#endregion

    public void updatePlayersScoreGraphics() {
        overlayPanel.updatePlayersRanksGraphics(controller.getPlayersRankList());
        overlayPanel.updatePlayersScoreText();
    }
}
