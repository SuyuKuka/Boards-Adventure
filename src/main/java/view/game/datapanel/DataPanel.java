package view.game.datapanel;

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
import model.config.players.Player;
import view.game.GamePanel;
import view.game.datapanel.cardgraphics.CardGraphics;
import view.game.datapanel.cardgraphics.CardGraphicsFactory;
import view.game.overlaypanel.PlayerScore;

/**
 * {@link DataPanel} est une classe qui extends {@link JPanel} et occupe de la partie gauche de {@link GamePanel}.
 */
public final class DataPanel extends JPanel {

    private final GamePanel gamePanel;

    private final JPanel turnsPanel;
    private JLabel remainingTurns;

    private final JPanel scoreBoardPanel;
    private final ArrayList<PlayerScore> playersScore = new ArrayList<>();

    private final JPanel cardGraphicsPanel;
    private CardGraphics cardGraphics;

    public DataPanel(final GamePanel gamePanel, final int width, final int height) {
        this.gamePanel = gamePanel;
        setPreferredSize(new Dimension(width, height));
        setLayout(new BorderLayout());
        setOpaque(false);

        final int nbPlayers = gamePanel.getPlayersGraphics().size();
        final int scoreboardLines = nbPlayers < 4 ? 4 : nbPlayers;

        final int height1 = (int) (height * 0.05);
        int height2 = (int) (height * 0.4);
        height2 -= height2 % scoreboardLines;
        final int height3 = height - height1 - height2;
        turnsPanel = createRemainingTurnsPanel(width, height1);
        add(turnsPanel, BorderLayout.NORTH);

        scoreBoardPanel = createScoreboardPanel(width, height2);
        add(scoreBoardPanel, BorderLayout.CENTER);

        cardGraphicsPanel = createCardGraphicsPanel(width, height3);
        add(cardGraphicsPanel, BorderLayout.SOUTH);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                final Dimension dim = getSize();
                final int height1 = (int) (dim.height * 0.05);
                int height2 = (int) (height * 0.4);
                height2 -= height2 % scoreboardLines;
                final int height3 = dim.height - height1 - height2;
                turnsPanel.setPreferredSize(new Dimension(width, height1));
                scoreBoardPanel.setPreferredSize(new Dimension(width, height2));
                cardGraphicsPanel.setPreferredSize(new Dimension(width, height3));
                revalidate();
                repaint();
            }
        });

        setVisible(true);
    }

    //#region update
    public void updatePlayerScoreText() {
        final Player player = gamePanel.getController().getCurrentPlayer();
        playersScore.get(gamePanel.getController().getCurrentPlayerID()).updatePlayerScoreText();
    }

    //#endregion
    // #region gameState
    public void endTurn() {
        final Controller controller = gamePanel.getController();
        remainingTurns.setText(controller.getCurrentTurn() + " / " + controller.getTotalTurns());
    }

    // #endregion

    // #region turns

    private JPanel createRemainingTurnsPanel(final int width, final int height) {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(width, height));
        panel.setBackground(Color.GRAY);
        panel.setOpaque(true);
        remainingTurns = new JLabel();
        final Controller controller = gamePanel.getController();
        remainingTurns.setText(controller.getCurrentTurn() + " / " + controller.getTotalTurns());
        remainingTurns.setHorizontalAlignment(JLabel.CENTER);
        remainingTurns.setVerticalAlignment(JLabel.CENTER);
        panel.add(remainingTurns, BorderLayout.CENTER);
        panel.setVisible(true);
        return panel;
    }

    // #endregion

    // #region scoreboard

    private JPanel createScoreboardPanel(final int width, final int height) {
        final JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(width, height));
        final int playersNb = gamePanel.getController().getPlayers().size();
        final int scoreboardLines = playersNb >= 4 ? playersNb : 4;
        panel.setLayout(new GridLayout(scoreboardLines, 1));

        gamePanel.getPlayersGraphics().forEach((playerGraphics) -> {
            final PlayerScore playerScore = new PlayerScore(gamePanel, width, height / scoreboardLines,
                playerGraphics.getPlayerID());
            playersScore.add(playerScore);
            panel.add(playerScore);
        });
        panel.setVisible(true);
        return panel;
    }

    // #endregion

    // #region cardGraphics

    private JPanel createCardGraphicsPanel(final int width, final int height) {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(width, height));
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                if (cardGraphics != null) {
                    cardGraphics.updatePaintArea();
                }
                revalidate();
                repaint();
            }
        });
        cardGraphics = CardGraphicsFactory.createEmptyCardGraphics(gamePanel, panel.getWidth(), panel.getHeight());
        panel.add(cardGraphics, BorderLayout.CENTER);
        panel.setVisible(true);
        return panel;
    }

    // #endregion

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public Controller getController() {
        return gamePanel.getController();
    }

}
