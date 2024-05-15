package view.game.datapanel;

import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.GridBagLayout;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.resources.Resources;
import view.game.GamePanel;

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

        gbc.weightx = 0.15;
        gbc.weighty = 1;
        rankLabel = createRankLabel((int) (width * gbc.weightx), height);
        add(rankLabel, gbc);

        classLabel = createClassLabel((int) (width * gbc.weightx), height);
        add(classLabel, gbc);

        gbc.weightx = 0.35;
        nameLabel = new JLabel();
        moneyLabel = new JLabel();
        final JPanel panel1 = createNameMoneyPanel((int) (width * gbc.weightx), height);
        add(panel1, gbc);

        gbc.weightx = 0.30;
        pointsLabel = new JLabel();
        propertyLabel = new JLabel();
        final JPanel panel2 = createPointsPropertyPanel((int) (width * gbc.weightx), height);
        add(panel2, gbc);

        gbc.weightx = 0.05;
        itemsLabel = createItemsLabel((int) (width * gbc.weightx), height);
        add(itemsLabel, gbc);

        playerBox = createComboBox((int) (width * gbc.weightx), height);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                final Dimension dim = getSize();
                rankLabel.setIcon(new ImageIcon(rankImage.getScaledInstance((int) (dim.width * 0.15),
                    dim.height, Image.SCALE_FAST)));
                classLabel.setIcon(new ImageIcon(classImage.getScaledInstance((int) (dim.width * 0.15),
                    dim.height, Image.SCALE_FAST)));
                revalidate();
                repaint();
            }
        });

        setVisible(true);
    }

    private JLabel createRankLabel(final int width, final int height) {
        final JLabel label = new JLabel();
        label.setOpaque(true);
        label.setBackground(Color.BLACK);
        label.setPreferredSize(new Dimension(0, 0));
        label.setSize(width, height);
        label.setIcon(new ImageIcon(rankImage.getScaledInstance(width, height, Image.SCALE_FAST)));
        return label;
    }

    private JLabel createClassLabel(final int width, final int height) {
        final JLabel label = new JLabel();
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        label.setPreferredSize(new Dimension(0, 0));
        label.setSize(width, height);
        label.setIcon(new ImageIcon(classImage.getScaledInstance(width, height, Image.SCALE_FAST)));
        return label;
    }

    private JPanel createNameMoneyPanel(final int width, final int height) {
        final JPanel panel = new JPanel(new GridLayout(2, 0));
        panel.setPreferredSize(new Dimension(0, 0));
        panel.setBackground(Color.ORANGE);
        panel.add(nameLabel);
        nameLabel.setText("TESTPSEUDO");
        panel.add(moneyLabel);
        moneyLabel.setText("0");
        return panel;
    }


    private JPanel createPointsPropertyPanel(final int width, final int height) {
        final JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(0, 0));
        panel.setBackground(Color.CYAN);
        return panel;
    }

    private JLabel createItemsLabel(final int width, final int height) {
        final JLabel label = new JLabel();
        label.setBackground(Color.BLUE);
        label.setOpaque(true);
        label.setPreferredSize(new Dimension(0, 0));
        label.setSize(width, height);
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
