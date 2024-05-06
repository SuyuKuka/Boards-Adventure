package view.game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.Controller;

public final class DataPanel extends JPanel {

    private static final int WIDTH_PER = 20;
    private static final int HEIGHT_PER = 80;

    private final GamePanel gamePanel;
    private final Controller controller;

    private final JLabel turn = new JLabel();

    private final ArrayList<PlayerScore> playerScoreList = new ArrayList<>();

    public DataPanel(final GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.controller = gamePanel.getController();
        setLayout(new GridBagLayout());
        setOpaque(false);
        setPreferredSize(new Dimension(gamePanel.getWidth() * WIDTH_PER / 100,
            gamePanel.getHeight() * HEIGHT_PER / 100));
        initComponents();
        setBackground(Color.RED);
        setVisible(true);
    }

    private void initComponents() {
        // Ajouts des composants de haut en bas :
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 0.10;
        add(createTurnsPanel(), gbc); // tours restants
        gbc.gridy = 1;
        gbc.weighty = 0.40;
        add(createScoreboardPanel(), gbc); // scoreboard
        gbc.gridy = 2;
        gbc.weighty = 0.50;
        add(createInfoPanel(), gbc); // info de l'objet sélectionné, sinon vide
    }

    // #region turn

    private JPanel createTurnsPanel() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        turn.setText("text");
        turn.setHorizontalAlignment(JLabel.CENTER);
        turn.setVerticalAlignment(JLabel.CENTER);
        panel.add(turn);
        return panel;
    }

    // #endregion

    // #region scoreboard

    private JPanel createScoreboardPanel() {
        final JPanel panel = new JPanel();
        panel.setBackground(Color.BLACK);
        panel.setOpaque(false);
        panel.setLayout(new GridLayout(4, 1));

        gamePanel.getPlayerGraphicsList().forEach((playerGraphics) -> {
            final PlayerScore playerScore = new PlayerScore(gamePanel, playerGraphics.getPlayerID());
            playerScoreList.add(playerScore);
            panel.add(playerScore);
        });

        return panel;
    }

    // #endregion

    // #region info

    private JPanel createInfoPanel() {
        final JPanel panel = new JPanel();
        panel.setBackground(Color.YELLOW);
        return panel;
    }


    // #endregion

    public static int getWidthPer() {
        return WIDTH_PER;
    }

    public static int getHeightPer() {
        return HEIGHT_PER;
    }

}
