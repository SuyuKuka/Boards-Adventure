package view.game;

import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.Controller;

public final class PlayerScore extends JPanel {

    private GamePanel gamePanel;
    private Controller controller;

    private int playerId;
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
        updateComp();

    }

    public void initComp() {
        this.rank = new JLabel(String.valueOf(controller.getPlayer(playerId).getTeam().getRank()));
        this.skin = new JLabel(controller.getPlayer(playerId).getSkin());
        this.pseudo = new JLabel(controller.getPlayer(playerId).getUsername());
        this.money = new JLabel(String.valueOf(controller.getPlayer(playerId).getTeam().getMoney()));
        this.points = new JLabel(String.valueOf(controller.getPlayer(playerId).getTeam().getGamePoint()));
        this.property = new JLabel(String.valueOf(controller.getPlayer(playerId).getTeam().getProperties().size()));
        this.items = new JLabel(String.valueOf(controller.getPlayer(playerId).getShopItem().size()));
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
        items.setText(String.valueOf(controller.getCurrentPlayer().getShopItem().size()));
    }

}
