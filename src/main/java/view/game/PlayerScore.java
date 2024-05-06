package view.game;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.Controller;
import view.SwingFactory;

public final class PlayerScore extends JPanel {

    private final GamePanel gamePanel;
    private final Controller controller;

    private final int playerId;
    private JLabel rank;
    private JLabel skin;
    private JLabel pseudo;
    private JLabel money;
    private JLabel points;
    private JLabel property;
    private JLabel items;

    public PlayerScore(final GamePanel gamePanel, int playerId) {
        this.gamePanel = gamePanel;
        this.controller = gamePanel.getController();
        this.playerId = playerId;

        initComp();
        setBackground(Color.BLACK);
        updateComp();
        setLayout(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        add(rank, gbc);
        add(skin, gbc);
        add(pseudo, gbc);
        add(money, gbc);
        add(points, gbc);
        add(property, gbc);
        add(items, gbc);
    }

    public void initComp() {
        this.rank = SwingFactory.createJLabel(String.valueOf(controller.getPlayer(playerId).getTeam().getRank()));
        this.skin = SwingFactory.createJLabel(controller.getPlayer(playerId).getSkin());
        this.pseudo = SwingFactory.createJLabel(controller.getPlayer(playerId).getUsername());
        this.money = SwingFactory.createJLabel(String.valueOf(controller.getPlayer(playerId).getTeam().getMoney()));
        this.points = SwingFactory.createJLabel(String.valueOf(controller.getPlayer(playerId).getTeam()
            .getGamePoint()));
        this.property = SwingFactory.createJLabel(String.valueOf(controller.getPlayer(playerId).getTeam()
            .getProperties().size()));
        this.items = SwingFactory.createJLabel(String.valueOf(controller.getPlayer(playerId).getShopItem().length));
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
