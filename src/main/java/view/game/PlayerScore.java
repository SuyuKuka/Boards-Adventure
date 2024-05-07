package view.game;

import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.GridBagConstraints;
import java.awt.Color;

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
    private final JLabel rank = SwingFactory.createJLabel();
    private Image rankImage;
    private final JLabel skin = SwingFactory.createJLabel();
    private final Image skinImage;

    private final JPanel playerInfoPanel = new JPanel();
    private final JLabel pseudo = SwingFactory.createJLabel();
    private final JLabel money = SwingFactory.createJLabel();
    private final JLabel points = SwingFactory.createJLabel();
    private final JLabel property = SwingFactory.createJLabel();
    private final JLabel items = SwingFactory.createJLabel();

    private JButton tradeButton;

    public PlayerScore(final GamePanel gamePanel, final int playerId) {
        this.gamePanel = gamePanel;
        this.controller = gamePanel.getController();
        this.playerId = playerId;
        this.skinImage = Resources.getImage("");
        this.rankImage = Resources.getImage("placeholder");

        initComp();
        setBackground(Color.BLACK);
        updateComp();
        setLayout(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;
        gbc.weightx = 0.2;
        add(rank, gbc);
        gbc.weightx = 0.2;
        add(skin, gbc);
        gbc.weightx = 0.5;
        add(playerInfoPanel, gbc);
        add(pseudo, gbc);
        add(money, gbc);
        add(points, gbc);
        add(property, gbc);
        add(items, gbc);
        gbc.weightx = 0.1;
        add(tradeButton, gbc);
    }

    public void initComp() {
        rank.setText(String.valueOf(controller.getPlayer(playerId).getTeam().getRank()));
        skin.setText(controller.getPlayer(playerId).getSkin());
        pseudo.setText(controller.getPlayer(playerId).getUsername());
        money.setText(String.valueOf(controller.getPlayer(playerId).getTeam().getMoney()));
        points.setText(String.valueOf(controller.getPlayer(playerId).getTeam().getGamePoint()));
        property.setText(String.valueOf(controller.getPlayer(playerId).getTeam().getProperties().size()));
        items.setText(String.valueOf(controller.getPlayer(playerId).getShopItem().length));
        this.tradeButton = new JButton();
    }

    public void updateComp() {
        updateRank();
        updateMoney();
        updatePoints();
        updateProperty();
        updateItems();
    }

    private void updateRank() {
        rank.setText(String.valueOf(controller.getCurrentPlayer().getTeam().getRank()));
    }

    private void updateMoney() {
        money.setText(String.valueOf(controller.getCurrentPlayer().getTeam().getMoney()));
    }

    private void updatePoints() {
        points.setText(String.valueOf(controller.getCurrentPlayer().getTeam().getGamePoint()));
    }

    private void updateProperty() {
        property.setText(String.valueOf(controller.getCurrentPlayer().getTeam().getProperties().size()));
    }

    private void updateItems() {
        items.setText(String.valueOf(controller.getCurrentPlayer().getShopItem().length));
    }

}
