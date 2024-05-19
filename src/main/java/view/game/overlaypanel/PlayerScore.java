package view.game.overlaypanel;

import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.Controller;
import controller.resources.Resources;
import view.game.GamePanel;

public final class PlayerScore extends JPanel {

    private static final Color START_ITEM_COLOR_ICON = new Color(173, 50, 31);

    private final GamePanel gamePanel;
    private final Controller controller;
    private final int playerId;

    private final JLabel rankLabel;
    private Image rankImage;

    private final JLabel classLabel;
    private final Image classImage;

    private final JLabel nameLabel;
    private final String name;
    private final JLabel coinsLabel;

    private final JLabel gamePointsLabel;
    private final JLabel propertiesLabel;

    private final JPanel itemsPanel;
    private final ArrayList<JLabel> itemsLabel;

    private final JComboBox<Object> playerBox;

    public PlayerScore(final GamePanel gamePanel, final int width, final int height, final int playerId) {
        this.gamePanel = gamePanel;
        this.controller = gamePanel.getController();
        this.playerId = playerId;
        setSize(width, height);
        setBackground(Color.decode(gamePanel.getController().getPlayer(playerId).getHexTeamColor()));
        this.rankImage = Resources.getImage("1st");
        this.classImage = Resources.getImage(controller.getCurrentPlayer().getClassName());

        setLayout(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        gbc.weightx = 0.15;
        gbc.weighty = 1;
        rankLabel = createRankLabel((int) (width * gbc.weightx), height);
        add(rankLabel, gbc);

        classLabel = createClassLabel((int) (width * gbc.weightx), height);
        add(classLabel, gbc);

        gbc.weightx = 0.3;
        nameLabel = new JLabel();
        name = controller.getPlayer(playerId).getName();
        coinsLabel = new JLabel();
        final JPanel panel1 = createNameCoinsPanel((int) (width * gbc.weightx), height);
        add(panel1, gbc);

        gbc.weightx = 0.2;
        gamePointsLabel = new JLabel();
        propertiesLabel = new JLabel();
        final JPanel panel2 = createPointsPropertiesPanel((int) (width * gbc.weightx), height);
        add(panel2, gbc);

        gbc.weightx = 0.15;
        itemsLabel = new ArrayList<>();
        itemsPanel = createItemsLabel((int) (width * gbc.weightx), height);
        add(itemsPanel, gbc);

        gbc.weightx = 0.05;
        playerBox = createComboBox((int) (width * gbc.weightx), height);
        add(playerBox, gbc);

        addResizeListener();

        setVisible(true);
    }

    //#region update

    public void updateRankImage(final Image newRankImage) {
        if (!rankImage.equals(newRankImage)) {
            this.rankImage = newRankImage;
            final Dimension dim = getSize();
            rankLabel.setIcon(new ImageIcon(rankImage.getScaledInstance(
                (int) (dim.width * 0.15), dim.height, Image.SCALE_SMOOTH)));
        }
    }

    public void updatePlayerScoreText() {
        updateCoinsText();
        updatePropertiesText();
        updateGamePointsText();
    }

    private void updateCoinsText() {
        coinsLabel.setText(controller.getPlayer(playerId).getCoins() + "");
    }

    private void updatePropertiesText() {
        propertiesLabel.setText(controller.getPlayer(playerId).getProperties().size() + "");
    }

    private void updateGamePointsText() {
        gamePointsLabel.setText(controller.getPlayer(playerId).getGamePoint() + "");
    }

    //#endregion

    //#region label/panel creation

    private void addResizeListener() {
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                final Dimension dim = getSize();
                rankLabel.setIcon(new ImageIcon(rankImage.getScaledInstance((int) (dim.width * 0.15),
                    dim.height, Image.SCALE_SMOOTH)));
                classLabel.setIcon(new ImageIcon(classImage.getScaledInstance((int) (dim.width * 0.15),
                    dim.height, Image.SCALE_SMOOTH)));
                coinsLabel.setIcon(new ImageIcon(Resources.getImage("coins").getScaledInstance(
                    (int) (dim.width * 0.35 * 0.2), (int) (dim.height / 2 * 0.8), Image.SCALE_SMOOTH)));
                gamePointsLabel.setIcon(new ImageIcon(Resources.getImage("points").getScaledInstance(
                    (int) (dim.width * 0.25 * 0.2), (int) (dim.height / 2 * 0.8), Image.SCALE_SMOOTH)));
                propertiesLabel.setIcon(new ImageIcon(Resources.getImage("properties").getScaledInstance(
                    (int) (dim.width * 0.25 * 0.2), (int) (dim.height / 2 * 0.8), Image.SCALE_SMOOTH)));
                revalidate();
                repaint();
            }
        });
    }

    private JLabel createRankLabel(final int width, final int height) {
        final JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(0, 0));
        label.setSize(width, height);
        label.setIcon(new ImageIcon(rankImage.getScaledInstance(width, height, Image.SCALE_SMOOTH)));
        return label;
    }

    private JLabel createClassLabel(final int width, final int height) {
        final JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(0, 0));
        label.setSize(width, height);
        label.setIcon(new ImageIcon(classImage.getScaledInstance(width, height, Image.SCALE_SMOOTH)));
        return label;
    }

    private JPanel createNameCoinsPanel(final int width, final int height) {
        final JPanel panel = new JPanel(new GridLayout(2, 0));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        panel.setPreferredSize(new Dimension(0, 0));
        nameLabel.setText(name);
        nameLabel.setHorizontalAlignment(JLabel.CENTER);
        panel.add(nameLabel);
        coinsLabel.setIcon(new ImageIcon(Resources.getImage("coins")
        .getScaledInstance((int) (width * 0.2), (int) (height / 2 * 0.8), Image.SCALE_SMOOTH)));
        coinsLabel.setText(controller.getPlayer(playerId).getCoins() + "");
        panel.add(coinsLabel);
        return panel;
    }


    private JPanel createPointsPropertiesPanel(final int width, final int height) {
        final JPanel panel = new JPanel(new GridLayout(2, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        panel.setPreferredSize(new Dimension(0, 0));
        panel.setOpaque(false);
        gamePointsLabel.setText("0");
        gamePointsLabel.setIcon(new ImageIcon(Resources.getImage("points")
            .getScaledInstance((int) (width * 0.2), (int) (height / 2 * 0.8), Image.SCALE_SMOOTH)));
        panel.add(gamePointsLabel);
        propertiesLabel.setText("0");
        propertiesLabel.setIcon(new ImageIcon(Resources.getImage("properties")
            .getScaledInstance((int) (width * 0.2), (int) (height / 2 * 0.8), Image.SCALE_SMOOTH)));
        panel.add(propertiesLabel);
        return panel;
    }

    private JPanel createItemsLabel(final int width, final int height) {
        final JPanel panel = new JPanel();
        panel.setOpaque(false);
        final int playerItemShopNb = Controller.getPlayerItemShopNb() + 1;
        final int gridWidth = playerItemShopNb <= 2 ? 1 : 2;
        panel.setLayout(new GridLayout(2, gridWidth));
        for (int i = 0; i < playerItemShopNb; i++) {
            final Color color = i == 0 ? START_ITEM_COLOR_ICON : Color.BLACK;
            final JLabel label = new JLabel() {
                @Override
                protected void paintComponent(final Graphics g) {
                    super.paintComponent(g);
                    final Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    final Dimension size = getSize();
                    final int paint = size.width > size.height ? (int) (size.height * 0.9) : (int) (size.width * 0.9);
                    final int x1 = (size.width - paint) / 2;
                    final int y1 = (size.height - paint) / 2;
                    final int x2 = (paint - 1) % 2 == 0 ? paint - 1 : paint;
                    final int y2 = (paint - 1) % 2 == 0 ? paint - 1 : paint;
                    g2d.setColor(color);
                    g2d.fillOval(x1, y1, x2, y2);
                }
            };
            label.setPreferredSize(new Dimension(0, 0));
            itemsLabel.add(label);
            panel.add(label);
        }
        return panel;
    }

    private JComboBox<Object> createComboBox(final int width, final int height) {
        final JComboBox<Object> comboBox = new JComboBox<>();
        comboBox.setPreferredSize(new Dimension(0, 0));
        return comboBox;
    }

    //#endregion label/panel creation

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        if (controller.getCurrentPlayerID() != playerId) {
            final Dimension dim = getSize();
            g.setColor(new Color(0, 0, 0, 100));
            g.fillRect(0, 0, dim.width, dim.height);
        }
    }

}
