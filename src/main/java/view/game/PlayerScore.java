package view.game;

import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.Controller;
import controller.resources.Resources;
import view.SwingFactory;

public final class PlayerScore extends JPanel {

    private final DataPanel dataPanel;
    private final Controller controller;
    private final int playerId;

    // RankSkinPanel (WEST) 30% W
    private static final int RANK_SKIN_PANEL_PER_WIDTH = 30;
    private final JPanel rankSkinPanel = new JPanel();
    private static final int RANK_PER_WIDTH = 50;
    private final JLabel rankLabel = SwingFactory.createJLabel();
    private final JLabel skinLabel = SwingFactory.createJLabel();
    private Image rankImage;
    private final Image skinImage;

    // PlayerInfoPanel (CENTER) 100% - 30% - 10% W
    private final JPanel playerInfoPanel = new JPanel();
    // Username + Money (+WEST) 60% W
    private static final int NAME_MONEY_PANEL_PER_WIDTH = 60;
    private final JPanel usernameMoneyPanel = new JPanel();
    private final JLabel usernameLabel = SwingFactory.createJLabel();
    private final JLabel moneyLabel = SwingFactory.createJLabel();
    // Points + Properties (+CENTER) 100% - 60% - 10% W
    private final JPanel pointsPropertyPanel = new JPanel();
    private final JLabel pointsLabel = SwingFactory.createJLabel();
    private final JLabel propertyLabel = SwingFactory.createJLabel();
    // Items (+EAST) 10% W
    private static final int ITEM_PANEL_PER_WIDTH = 10;
    private final JPanel itemsPanel = new JPanel();
    private final JLabel itemsLabel = SwingFactory.createJLabel();

    // ButtonsPanel (EAST) 10% W
    private static final int BUTTONS_PANEL_PER_WIDTH = 10;
    private final JPanel buttonsPanel = new JPanel();
    private final JButton tradeButton = new JButton();

    public PlayerScore(final DataPanel dataPanel, final JPanel scoreboardPanel, final int playerId) {
        this.dataPanel = dataPanel;
        this.controller = dataPanel.getController();
        this.playerId = playerId;
        this.rankImage = Resources.getImage("1st");
        final ArrayList<PlayerGraphics> playerList = dataPanel.getGamePanel().getPlayerGraphicsList();
        this.skinImage = playerList.get(playerId).getPlayerImage();
        setLayout(new BorderLayout());

        final Dimension scoreboardSize = scoreboardPanel.getPreferredSize();
        final int playerNb = playerList.size() < 4 ? 4 : playerList.size();
        setPreferredSize(new Dimension(scoreboardSize.width, scoreboardSize.height / playerNb));
        initComp();
        add(rankSkinPanel, BorderLayout.WEST);
        add(playerInfoPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.EAST);
    }

    public void initComp() {
        final int height = getHeight();
        final int width = getWidth();
        final int rankSkinPanelWidth = width * RANK_SKIN_PANEL_PER_WIDTH / 100;
        final int buttonsPanelWidth = width * BUTTONS_PANEL_PER_WIDTH / 100;
        rankSkinPanel.setPreferredSize(new Dimension(rankSkinPanelWidth, height));
        playerInfoPanel.setPreferredSize(new Dimension(width - rankSkinPanelWidth - buttonsPanelWidth, height));
        buttonsPanel.setPreferredSize(new Dimension(buttonsPanelWidth, height));
        initRankSkinPanel();
        initPlayerInfoPanel();
        initButtonsPanel();
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                final int height = getHeight();
                final int width = getWidth();
                final int rankSkinPanelWidth = width * RANK_SKIN_PANEL_PER_WIDTH / 100;
                final int buttonsPanelWidth = width * BUTTONS_PANEL_PER_WIDTH / 100;
                rankSkinPanel.setPreferredSize(new Dimension(rankSkinPanelWidth, height));
                playerInfoPanel.setPreferredSize(new Dimension(width - rankSkinPanelWidth - buttonsPanelWidth, height));
                buttonsPanel.setPreferredSize(new Dimension(buttonsPanelWidth, height));
            }
        });
    }

    //#region RankSkinPanel

    private void initRankSkinPanel() {
        rankSkinPanel.setLayout(new BorderLayout());
        initRankSkinComp();
        final Dimension rankSize = rankLabel.getPreferredSize();
        rankLabel.setIcon(new ImageIcon(rankImage.getScaledInstance(rankSize.width,
            rankSize.height, Image.SCALE_FAST)));
        final Dimension skinSize = skinLabel.getPreferredSize();
        skinLabel.setIcon(new ImageIcon(skinImage.getScaledInstance(skinSize.width,
            skinSize.height, Image.SCALE_FAST)));
        rankSkinPanel.add(rankLabel, BorderLayout.WEST);
        rankSkinPanel.add(skinLabel, BorderLayout.CENTER);

        rankSkinPanel.setVisible(true);
    }

    private void initRankSkinComp() {
        final Dimension size = rankSkinPanel.getPreferredSize();
        final int height = size.height;
        final int width = size.width;
        final int rankWidth = width * RANK_PER_WIDTH / 100;
        rankLabel.setPreferredSize(new Dimension(rankWidth, height));
        skinLabel.setPreferredSize(new Dimension(width - rankWidth, height));
        rankSkinPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                final Dimension size = rankSkinPanel.getPreferredSize();
                final int height = size.height;
                final int width = size.width;
                final int rankWidth = width * RANK_PER_WIDTH / 100;
                rankLabel.setPreferredSize(new Dimension(rankWidth, height));
                skinLabel.setPreferredSize(new Dimension(width - rankWidth, height));
                final Dimension rankSize = rankLabel.getPreferredSize();
                rankLabel.setIcon(new ImageIcon(rankImage.getScaledInstance(rankSize.width,
                    rankSize.height, Image.SCALE_FAST)));
                final Dimension skinSize = skinLabel.getPreferredSize();
                skinLabel.setIcon(new ImageIcon(skinImage.getScaledInstance(skinSize.width,
                    skinSize.height, Image.SCALE_FAST)));
            }
        });
    }

    //#endregion

    //#region PlayerInfoPanel

    private void initPlayerInfoPanel() {
        playerInfoPanel.setBackground(Color.GRAY);
    }

    //#endregion

    //#region ButtonsPanel

    private void initButtonsPanel() {
    }

    //#endregion

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

    @Override
    public int getWidth() {
        return getPreferredSize().width;
    }

    @Override
    public int getHeight() {
        return getPreferredSize().height;
    }

}
