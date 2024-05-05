package view.game;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.Controller;
public final class DataPanel extends JPanel {

    private final GamePanel gamePanel;
    private final Controller controller;

    private JLabel turn = new JLabel();

    private ArrayList<PlayerScore> playerScoreList = new ArrayList<>();

    public DataPanel(final GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.controller = gamePanel.getController();
        setBackground(Color.CYAN);
        setLayout(new GridBagLayout());
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        // Ajouts des composants de haut en bas :
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 0.10;
        add(createTurnsLabel(), gbc); // tours restants
        gbc.gridy = 1;
        gbc.weighty = 0.40;
        add(createScoreboardPanel(), gbc); // scoreboard
        gbc.gridy = 2;
        gbc.weighty = 0.50;
        add(createInfoPanel(), gbc); // info de l'objet sélectionné, sinon vide
    }

    // #region turn

    private JLabel createTurnsLabel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);

        updateTurnsLabel();

        panel.add(turn);
        return turn;
    }

    public void updateTurnsLabel() {
        turn.setText("Tour : " + controller.getCurrentTurn() + " / " + controller.getTotalTurns());
    }

    // #endregion

    // #region scoreboard

    //Pseudo, argent, points, classement, nb de propriété
    private JPanel createScoreboardPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new GridLayout(4, 1));

        // for (PlayerGraphics player : gamePanel.getPlayerGraphicsMap().values()) {
        //     PlayerScore playerScore = new PlayerScore(gamePanel, player.getId());
        //     playerScoreList.add(playerScore);
        //     panel.add(playerScore);
        // }

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

}
