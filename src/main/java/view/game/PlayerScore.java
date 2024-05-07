package view.game;

import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.GridBagConstraints;
import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.Controller;
import controller.resources.Resources;
import view.SwingFactory;

public final class PlayerScore extends JPanel {

    private final GamePanel gamePanel;
    private final Controller controller;

    private final int playerId;
    private final JLabel rankLabel = SwingFactory.createJLabel();
    private final Image rankImage;
    private final JLabel skinLabel = SwingFactory.createJLabel();
    private final Image skinImage;

    private final JPanel playerInfoPanel = new JPanel();
    private final JLabel pseudoLabel = SwingFactory.createJLabel();
    private final JLabel moneyLabel = SwingFactory.createJLabel();
    private final JLabel pointsLabel = SwingFactory.createJLabel();
    private final JLabel propertyLabel = SwingFactory.createJLabel();
    private final JLabel itemsLabel = SwingFactory.createJLabel();

    private final JPanel buttonsPanel = new JPanel();
    private final JButton tradeButton = new JButton();

    public PlayerScore(final GamePanel gamePanel, final int playerId) {
        this.gamePanel = gamePanel;
        this.controller = gamePanel.getController();
        this.playerId = playerId;
        this.rankImage = Resources.getImage("placeholder");
        this.skinImage = gamePanel.getPlayerGraphics(playerId).getPlayerImage();
        initComp();
        setBackground(Color.BLACK);
        updateComp();
        setLayout(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;
        gbc.weightx = 0.2;
        add(rankLabel, gbc);
        gbc.weightx = 0.2;
        add(skinLabel, gbc);
        gbc.weightx = 0.5;
        add(playerInfoPanel, gbc);
        add(pseudoLabel, gbc);
        add(moneyLabel, gbc);
        add(pointsLabel, gbc);
        add(propertyLabel, gbc);
        add(itemsLabel, gbc);
        gbc.weightx = 0.1;
        add(buttonsPanel, gbc);
    }

    public void initComp() {
        rankLabel.setIcon(new ImageIcon(rankImage));
        skinLabel.setIcon(new ImageIcon(skinImage));
        pseudoLabel.setText(controller.getPlayer(playerId).getUsername());
        moneyLabel.setText(String.valueOf(controller.getPlayer(playerId).getMoney()));
        pointsLabel.setText(String.valueOf(controller.getPlayer(playerId).getGamePoint()));
        propertyLabel.setText(String.valueOf(controller.getPlayer(playerId).getProperties().size()));
        itemsLabel.setText(String.valueOf(controller.getPlayer(playerId).getShopItem().length));
    }

    public void updateComp() {
        updateRank();
        updateMoney();
        updatePoints();
        updateProperty();
        updateItems();
    }

    private void updateRank() {
        rankLabel.setText(String.valueOf(controller.getCurrentPlayer().getRank()));
    }

    private void updateMoney() {
        moneyLabel.setText(String.valueOf(controller.getCurrentPlayer().getMoney()));
    }

    private void updatePoints() {
        pointsLabel.setText(String.valueOf(controller.getCurrentPlayer().getGamePoint()));
    }

    private void updateProperty() {
        propertyLabel.setText(String.valueOf(controller.getCurrentPlayer().getProperties().size()));
    }

    private void updateItems() {
        itemsLabel.setText(String.valueOf(controller.getCurrentPlayer().getShopItem().length));
    }

}
