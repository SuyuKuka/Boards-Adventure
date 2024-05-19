package view.game.overlaypanel;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.BorderLayout;

import controller.resources.Resources;
import view.SwingFactory;
import view.game.GamePanel;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;

public final class OverlayPanel extends JPanel {

    private final GamePanel gamePanel;

    private final JLabel turnsLabel;
    private final JLabel playerActionLabel;

    private final JPanel scoreboardPanel;
    private PlayerScore playerScorePanel;
    private final ArrayList<PlayerScore> playersScore = new ArrayList<>();

    private static final Image SETTINGS_IMAGE = Resources.getImage("settings");
    private final JButton settingsButton;

    public OverlayPanel(final GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        final Dimension dim = gamePanel.getPreferredSize();
        setSize(dim.width, dim.height);
        setLayout(null);
        setOpaque(false);

        turnsLabel = new JLabel();
        playerActionLabel = new JLabel();
        final JPanel turnsActionsPanel = createTurnsActionsPanel((int) (dim.width * 0.03), (int) (dim.height * 0.03),
            (int) (dim.width * 0.2), (int) (dim.height * 0.1));
        scoreboardPanel = createScoreboardPanel((int) (dim.width * 0.03), (int) (dim.height * 0.14),
            (int) (dim.width * 0.2), (int) (dim.height * 0.25));
        settingsButton = createSettingsButton((int) (dim.width * 0.94), (int) (dim.height * 0.04),
            (int) (dim.width * 0.04), (int) (dim.height * 0.07));

        add(turnsActionsPanel);
        add(scoreboardPanel);
        add(settingsButton);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                final Dimension dim = gamePanel.getPreferredSize();
                turnsActionsPanel.setBounds((int) (dim.width * 0.03), (int) (dim.height * 0.03),
                    (int) (dim.width * 0.2), (int) (dim.height * 0.1));
                final Dimension panelDim = turnsActionsPanel.getSize();
                turnsLabel.setPreferredSize(new Dimension(panelDim.width, (int) (panelDim.height * 0.6)));
                playerActionLabel.setPreferredSize(new Dimension(panelDim.width, (int) (panelDim.height * 0.4)));
                scoreboardPanel.setBounds((int) (dim.width * 0.03), (int) (dim.height * 0.14),
                    (int) (dim.width * 0.2), (int) (dim.height * 0.25));
                settingsButton.setBounds((int) (dim.width * 0.94), (int) (dim.height * 0.04),
                    (int) (dim.width * 0.04), (int) (dim.height * 0.07));
            }
        });
        setVisible(true);
    }

    public void updatePlayersScoreText() {
        for (final PlayerScore playerScore : playersScore) {
            playerScore.updatePlayerScoreText();
        }
    }

    public void updatePlayersRanksGraphics(final ArrayList<Integer> rankList) {
        for (int i = 0; i < rankList.size(); i++) {
            playersScore.get(i).updateRankImage(Resources.getImage("rank" + rankList.get(i)));
        }
        revalidate();
        repaint();
    }


    //#region turns + playersAction

    private JPanel createTurnsActionsPanel(final int x, final int y, final int width, final int height) {
        final JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(final Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(0, 0, 0, 100));
                final Dimension dim = getSize();
                g.fillRoundRect(0, 0, dim.width, dim.height, 10, 10);
            }
        };
        panel.setOpaque(false);
        panel.setBounds(x, y, width, height);
        final Dimension dim = panel.getSize();
        initTurnsLabel(dim.width, (int) (dim.height * 0.6));
        initPlayerActionLabel(dim.width, (int) (dim.height * 0.3));
        panel.add(turnsLabel, BorderLayout.CENTER);
        panel.add(playerActionLabel, BorderLayout.SOUTH);
        return panel;
    }

    private void initTurnsLabel(final int width, final int height) {
        turnsLabel.setPreferredSize(new Dimension(width, height));
        turnsLabel.setText("Tour 1 / 45");

        // Font
        final Font font = Resources.getFont("NoitaBlackletter", 40);
        turnsLabel.setFont(SwingFactory.getResizedFont(turnsLabel, turnsLabel.getText(), font));
        turnsLabel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                turnsLabel.setFont(SwingFactory.getResizedFont(turnsLabel, turnsLabel.getText(), font));
            }
        });

        turnsLabel.setForeground(new Color(199, 45, 45));
        turnsLabel.setHorizontalAlignment(JLabel.CENTER);
    }

    private void initPlayerActionLabel(final int width, final int height) {
        playerActionLabel.setPreferredSize(new Dimension(width, height));
        playerActionLabel.setText("hello");
        // Font
        final Font font = Resources.getFont("NoitaBlackletter", 22);
        playerActionLabel.setFont(SwingFactory.getResizedFont(playerActionLabel, playerActionLabel.getText(), font));
        playerActionLabel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                playerActionLabel.setFont(SwingFactory.getResizedFont(playerActionLabel,
                    playerActionLabel.getText(), font));
            }
        });
        playerActionLabel.setForeground(new Color(255, 222, 109));
        playerActionLabel.setHorizontalAlignment(JLabel.CENTER);
    }

    //#endregion

    private JPanel createScoreboardPanel(final int x, final int y, final int width, final int height) {
        final JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setBounds(x, y, width, height);
        final int playersNb = gamePanel.getPlayersGraphics().size();
        final int scoreboardLines = playersNb >= 4 ? playersNb : 4;
        panel.setLayout(new GridLayout(scoreboardLines, 1));

        gamePanel.getPlayersGraphics().forEach((playerGraphics) -> {
            playerScorePanel =
                new PlayerScore(gamePanel, width, height / scoreboardLines, playerGraphics.getPlayerID());
            playersScore.add(playerScorePanel);
            panel.add(playerScorePanel);
        });
        panel.setVisible(true);
        return panel;
    }

    private JButton createSettingsButton(final int x, final int y, final int width, final int height) {
        final JButton button = new JButton() {
            @Override
            protected void paintComponent(final Graphics g) {
                super.paintComponent(g);
                final Dimension dim = getSize();
                g.drawImage(SETTINGS_IMAGE, 0, 0, dim.width, dim.height, this);
            }
        };
        button.setBounds(x, y, width, height);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                gamePanel.getPausePanel().setVisible(true);
            }
        });
        return button;
    }

    public PlayerScore getplayerScorePanel() {
        return playerScorePanel;
    }

    public ArrayList<PlayerScore> getPlayersScore() {
        return playersScore;
    }

}
