package view.game;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.Controller;

public final class DataPanel extends JPanel {

    private final GamePanel gamePanel;
    private final Controller controller;

    public DataPanel(final GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.controller = gamePanel.getController();
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);
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
        final JLabel label = new JLabel();
        label.setBackground(Color.BLACK);
        return label;
    }

    // #endregion

    // #region scoreboard

    private JPanel createScoreboardPanel() {
        final JPanel panel = new JPanel();
        panel.setBackground(Color.BLACK);
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
