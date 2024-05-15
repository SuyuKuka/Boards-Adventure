package view.game.datapanel;

import java.util.ArrayList;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.Controller;
import view.game.GamePanel;
import view.game.datapanel.cardgraphics.CardGraphics;
import view.game.datapanel.cardgraphics.CardGraphicsFactory;

/**
 * {@link DataPanel} est une classe qui extends {@link JPanel} et occupe de la partie gauche de {@link GamePanel}.
 */
public final class DataPanel extends JPanel {

    private final GamePanel gamePanel;

    private final JPanel turnsPanel;
    private JLabel remainingTurns;

    private final JPanel scoreBoardPanel;
    private final ArrayList<PlayerScore> playerScoreList = new ArrayList<>();

    private final JPanel cardGraphicsPanel;
    private CardGraphics cardGraphics;

    public DataPanel(final GamePanel gamePanel, final int width, final int height) {
        this.gamePanel = gamePanel;
        setPreferredSize(new Dimension(width, height));
        setLayout(new GridBagLayout());
        setOpaque(false);

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;

        gbc.weighty = 0.1;
        turnsPanel = createRemainingTurnsPanel(width, (int) (gbc.weighty * height));
        add(turnsPanel, gbc);

        gbc.weighty = 0.3;
        gbc.gridy = 1;
        scoreBoardPanel = createScoreboardPanel(width, (int) (gbc.weighty * height));
        add(scoreBoardPanel, gbc);

        gbc.weighty = 0.6;
        gbc.gridy = 2;
        cardGraphicsPanel = createCardGraphicsPanel(width, (int) (gbc.weighty * height));
        add(cardGraphicsPanel, gbc);

        setVisible(true);
    }
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
        panel.setBackground(Color.GREEN);

        gamePanel.getPlayersGraphics().forEach((playerGraphics) -> {
            final PlayerScore playerScore = new PlayerScore(gamePanel, width, height / scoreboardLines,
                playerGraphics.getPlayerID());
            playerScoreList.add(playerScore);
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
