package view.game;

import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.resources.Resources;

public final class PlayerScore extends JPanel {

    private final GamePanel gamePanel;
    private final int playerId;

    private final JLabel rankLabel;
    private Image rankImage;

    private final JLabel classLabel;
    private final Image classImage;

    private final JLabel nameLabel;
    private final JLabel moneyLabel;

    private final JLabel pointsLabel;
    private final JLabel propertyLabel;

    private final JLabel itemsLabel;

    private final JComboBox<Object> playerBox;
    private final JButton tradeButton;

    public PlayerScore(final GamePanel gamePanel, final int width, final int height, final int playerId) {
        this.gamePanel = gamePanel;
        this.playerId = playerId;
        setSize(width, height);
        setBackground(Color.decode(gamePanel.getController().getPlayer(playerId).getHexTeamColor()));
        this.rankImage = Resources.getImage("1st");
        this.classImage = Resources.getImage(gamePanel.getController().getCurrentPlayer().getClassName());

        setLayout(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        gbc.weightx = 0.1;
        gbc.weighty = 1;
        rankLabel = createRankLabel((int) (width * gbc.weightx), height);
        add(rankLabel, gbc);

        classLabel = createClassLabel((int) (width * gbc.weightx), height);
        add(classLabel, gbc);

        gbc.weightx = 0.4;
        nameLabel = new JLabel();
        moneyLabel = new JLabel();
        final JPanel panel1 = createNameMoneyPanel((int) (width * gbc.weightx), height);
        add(panel1, gbc);

        gbc.weightx = 0.3;
        pointsLabel = new JLabel();
        propertyLabel = new JLabel();
        final JPanel panel2 = createPointsPropertyPanel((int) (width * gbc.weightx), height);
        add(panel2, gbc);

        gbc.weightx = 0.1;
        itemsLabel = createItemsLabel((int) (width * gbc.weightx), height);

        tradeButton = new JButton();
        playerBox = createComboBox((int) (width * gbc.weightx), height);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                final Dimension dim = getSize();
                rankLabel.setIcon(new ImageIcon(rankImage.getScaledInstance((int) (dim.width * 0.1),
                    dim.height, Image.SCALE_FAST)));
                classLabel.setIcon(new ImageIcon(classImage.getScaledInstance((int) (dim.width * 0.1),
                    dim.height, Image.SCALE_FAST)));
                revalidate();
                repaint();
            }
        });

        setVisible(true);
    }

    private JLabel createRankLabel(final int width, final int height) {
        final JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(0, 0));
        label.setSize(width, height);
        label.setIcon(new ImageIcon(rankImage.getScaledInstance(width, height, Image.SCALE_FAST)));
        return label;
    }

    private JLabel createClassLabel(final int width, final int height) {
        final JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(0, 0));
        label.setSize(width, height);
        label.setIcon(new ImageIcon(classImage.getScaledInstance(width, height, Image.SCALE_FAST)));
        return label;
    }

    private JPanel createNameMoneyPanel(final int width, final int height) {
        final JPanel panel = new JPanel();
        panel.setOpaque(false);
        return panel;
    }


    private JPanel createPointsPropertyPanel(final int width, final int height) {
        final JPanel panel = new JPanel();
        panel.setOpaque(false);
        return panel;
    }

    private JLabel createItemsLabel(final int width, final int height) {
        final JLabel label = new JLabel();
        return label;
    }

    private JComboBox<Object> createComboBox(final int width, final int height) {
        final JComboBox<Object> comboBox = new JComboBox<>();
        return comboBox;
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
    }


}
