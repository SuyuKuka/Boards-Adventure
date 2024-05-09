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

    // Tours restants (NORTH) 10% H
    private static final int TURNS_PANEL_HEIGHT_PER = 10;
    private final JPanel turnsPanel = new JPanel();
    private JLabel remainingTurns;
    // Scoreboard (CENTER) 100% - 10% - 60% H
    private final JPanel scoreBoardPanel = new JPanel();
    private final ArrayList<PlayerScore> playerScoreList = new ArrayList<>();
    // CardGraphics (SOUTH) 60% H
    private static final int CARD_PANEL_HEIGHT_PER = 60;
    private final JPanel cardGraphicsPanel = new JPanel();
    private CardGraphics cardGraphics;

    public DataPanel(final GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.controller = gamePanel.getController();
        setOpaque(false);
        setPreferredSize(new Dimension(gamePanel.getWidth() * WIDTH_PER / 100,
            gamePanel.getHeight() * HEIGHT_PER / 100));
        setLayout(new BorderLayout());
        initComp();
        add(turnsPanel, BorderLayout.NORTH);
        add(scoreBoardPanel, BorderLayout.CENTER);
        add(cardGraphicsPanel, BorderLayout.SOUTH);
        setVisible(true);
    }
    // #region gameState
    public void endTurn() {
        remainingTurns.setText(controller.getCurrentTurn() + " / " + controller.getTotalTurns());
    }

    // #endregion

    private void initComp() {
        final int width = getWidth();
        final int height = getHeight();
        final int turnsPanelHeight = height * TURNS_PANEL_HEIGHT_PER / 100;
        final int cardPanelHeight = height * CARD_PANEL_HEIGHT_PER / 100;
        turnsPanel.setPreferredSize(new Dimension(width, turnsPanelHeight));
        scoreBoardPanel.setPreferredSize(new Dimension(width, height - turnsPanelHeight - cardPanelHeight));
        cardGraphicsPanel.setPreferredSize(new Dimension(width, cardPanelHeight));
        initRemainingTurnsPanel();
        initScoreboardPanel();
        initCardGraphicsPanel();
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                final int width = getWidth();
                final int height = getHeight();
                final int turnsPanelHeight = height * TURNS_PANEL_HEIGHT_PER / 100;
                final int cardPanelHeight = height * CARD_PANEL_HEIGHT_PER / 100;
                turnsPanel.setPreferredSize(new Dimension(width, turnsPanelHeight));
                scoreBoardPanel.setPreferredSize(new Dimension(width, height - turnsPanelHeight - cardPanelHeight));
                cardGraphicsPanel.setPreferredSize(new Dimension(width, cardPanelHeight));
            }
        });
    }

    // #region turns

    private void initRemainingTurnsPanel() {
        turnsPanel.setBackground(Color.GRAY);
        turnsPanel.setOpaque(true);
        remainingTurns = new JLabel();
        remainingTurns.setText(controller.getCurrentTurn() + " / " + controller.getTotalTurns());
        remainingTurns.setHorizontalAlignment(JLabel.CENTER);
        remainingTurns.setVerticalAlignment(JLabel.CENTER);
        turnsPanel.add(remainingTurns);
        turnsPanel.setVisible(true);
    }

    // #endregion

    // #region scoreboard

    private void initScoreboardPanel() {
        final int playersNb = controller.getPlayers().size();
        scoreBoardPanel.setLayout(new GridLayout(playersNb >= 4 ? playersNb : 4, 1));
        scoreBoardPanel.setBackground(Color.GREEN);

        gamePanel.getPlayerGraphicsList().forEach((playerGraphics) -> {
            final PlayerScore playerScore = new PlayerScore(this, scoreBoardPanel, playerGraphics.getPlayerID());
            playerScoreList.add(playerScore);
            scoreBoardPanel.add(playerScore);
        });
        scoreBoardPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                final int playerNb = playerScoreList.size() < 4 ? 4 : playerScoreList.size();
                final int height = scoreBoardPanel.getHeight() / playerNb;
                final int width = scoreBoardPanel.getWidth();
                playerScoreList.forEach(playerScore -> {
                    playerScore.setPreferredSize(new Dimension(width, height));
                });
                revalidate();
                repaint();
            }
        });
        scoreBoardPanel.setVisible(true);
    }

    // #endregion

    // #region cardGraphics

    private void initCardGraphicsPanel() {
        cardGraphicsPanel.setLayout(new BorderLayout());
        cardGraphicsPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                if (cardGraphics != null) {
                    cardGraphics.updatePaintArea();
                }
            }
        });
        cardGraphics = CardGraphicsFactory.createEmptyCardGraphics(this, cardGraphicsPanel);
        cardGraphicsPanel.add(cardGraphics, BorderLayout.CENTER);
        cardGraphicsPanel.setVisible(true);
    }

    // #endregion

    public GamePanel getGamePanel() {
        return gamePanel;
    }

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
