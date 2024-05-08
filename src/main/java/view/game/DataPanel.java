package view.game;

import java.util.ArrayList;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.Controller;
import view.game.cardGraphics.CardGraphics;
import view.game.cardGraphics.CardGraphicsFactory;

/**
 * {@link DataPanel} est une classe qui extends {@link JPanel} et occupe de la partie gauche de {@link GamePanel}.
 * <p> {@link DataPanel} (layout BorderLayout) contient 3 parties majeures du haut vers le bas :
 * <p> - Tours restants (NORTH)
 * <p> - Scoreboard -> Liste de {@link PlayerScore} (CENTER)
 * <p> - Info supplémentaire (sous forme de carte) -> {@link CardGraphics} (SOUTH)
 */
public final class DataPanel extends JPanel {
    // Taille de DataPanel en fonction de GamePanel
    private static final int WIDTH_PER = 20;
    private static final int HEIGHT_PER = 80;

    private final GamePanel gamePanel;
    private final Controller controller;

    // Tours restants (NORTH)
    private static final int NORTH_PANEL_HEIGHT_PER = 10;
    private final JPanel turnsPanel;
    private JLabel remainingTurns;
    // Scoreboard (CENTER)
    private final JPanel scoreBoard;
    private final ArrayList<PlayerScore> playerScoreList = new ArrayList<>();
    // CardGraphics (SOUTH)
    private static final int SOUTH_PANEL_HEIGHT_PER = 60;
    private final JPanel cardGraphicsPanel;
    private CardGraphics cardGraphics;

    public DataPanel(final GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.controller = gamePanel.getController();
        setOpaque(false);
        setPreferredSize(new Dimension(gamePanel.getWidth() * WIDTH_PER / 100,
            gamePanel.getHeight() * HEIGHT_PER / 100));
        setLayout(new BorderLayout());
        turnsPanel = createRemainingTurnsPanel();
        cardGraphicsPanel = createCardGraphicsPanel();
        scoreBoard = createScoreboardPanel(); // taille de scoreboard dépend des 2 autres
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                final int height = getHeight();
                final int width = getWidth();
                final int turnsPanelHeight = height * NORTH_PANEL_HEIGHT_PER / 100;
                final int cardGraphicsPanelHeight = height * SOUTH_PANEL_HEIGHT_PER / 100;
                turnsPanel.setPreferredSize(new Dimension(width, turnsPanelHeight));
                cardGraphicsPanel.setPreferredSize(new Dimension(width, cardGraphicsPanelHeight));
                scoreBoard.setPreferredSize(new Dimension(width, height - turnsPanelHeight - cardGraphicsPanelHeight));
                revalidate();
                repaint();
            }
        });
        add(turnsPanel, BorderLayout.NORTH);
        add(scoreBoard, BorderLayout.CENTER);
        add(cardGraphicsPanel, BorderLayout.SOUTH);
        setVisible(true);
    }
    // #region gameState
    public void endTurn() {
        remainingTurns.setText(controller.getCurrentTurn() + " / " + controller.getTotalTurns());
    }

    // #endregion

    // #region turns

    private JPanel createRemainingTurnsPanel() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(getWidth(), getHeight() * NORTH_PANEL_HEIGHT_PER / 100));
        panel.setBackground(Color.GRAY);
        panel.setOpaque(true);
        remainingTurns = new JLabel();
        remainingTurns.setText(controller.getCurrentTurn() + " / " + controller.getTotalTurns());
        remainingTurns.setHorizontalAlignment(JLabel.CENTER);
        remainingTurns.setVerticalAlignment(JLabel.CENTER);
        panel.add(remainingTurns);
        return panel;
    }

    // #endregion

    // #region scoreboard

    private JPanel createScoreboardPanel() {
        final JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(getWidth(), getHeight() - turnsPanel.getHeight()
            - cardGraphicsPanel.getHeight()));
        final int playersNb = controller.getPlayers().size();
        panel.setLayout(new GridLayout(playersNb >= 4 ? playersNb : 4, 1));
        panel.setBackground(Color.GREEN);

        gamePanel.getPlayerGraphicsList().forEach((playerGraphics) -> {
            final PlayerScore playerScore = new PlayerScore(gamePanel, playerGraphics.getPlayerID());
            playerScoreList.add(playerScore);
            panel.add(playerScore);
        });

        return panel;
    }

    // #endregion

    // #region cardGraphics

    private JPanel createCardGraphicsPanel() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(getWidth(), getHeight() * SOUTH_PANEL_HEIGHT_PER / 100));
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                if (cardGraphics != null) {
                    cardGraphics.updatePaintArea();
                }
            }
        });
        cardGraphics = CardGraphicsFactory.createEmptyCardGraphics(this, panel);
        panel.add(cardGraphics, BorderLayout.CENTER);
        return panel;
    }

    // #endregion

    @Override
    public int getWidth() {
        return getPreferredSize().width;
    }

    @Override
    public int getHeight() {
        return getPreferredSize().height;
    }

    public static int getWidthPer() {
        return WIDTH_PER;
    }

    public static int getHeightPer() {
        return HEIGHT_PER;
    }

    public Controller getController() {
        return controller;
    }

}
