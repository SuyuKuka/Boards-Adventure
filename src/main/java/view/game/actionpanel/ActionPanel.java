package view.game.actionpanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.FlowLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import controller.Controller;
import controller.resources.Resources;
import model.config.cells.Property;
import view.SwingFactory;
import view.game.GamePanel;

import java.util.ArrayList;
import java.util.HashMap;

public final class ActionPanel extends JPanel {

    private static final Image PROPERTY_IMAGE = Resources.getImage("property");

    private final GamePanel gamePanel;
    private final Controller controller;

    private final ChatPanel chatPanel;

    private final JPanel infoPanel;
    private JPanel infoPanelInside = null;
    private final JLabel defaultInfo;
    private JPanel buttonsPanel;
    private final JPanel inventoryPanel;
    private final JPanel buttonsInventoryPanel;

    private JComboBox<Object> otherActionBox;
    private ImageIcon rollDiceIcon = new ImageIcon(Resources.getImage("dice4"));
    private ImageIcon startItem;
    private HashMap<ImageIcon, Integer> shopItem = new HashMap<>();
    private JButton rollDiceButton;
    private final int widthButton;
    private final int heightButton;

    private boolean dice = true;

    public ActionPanel(final GamePanel gamePanel, final Controller controller, final int width, final int height) {
        this.gamePanel = gamePanel;
        this.controller = controller;
        setPreferredSize(new Dimension(width, height));
        setLayout(new BorderLayout());
        setOpaque(false);

        final int nbPropertiesGroup = gamePanel.getPropertiesInfo().size();

        final int width1 = (int) (width * 0.25);
        final int width2 = (int) (width * 0.3);
        int width4 = (int) (width * 0.3);
        width4 -= width4 % nbPropertiesGroup;
        this.widthButton = width - width1 - width2 - width4;
        this.heightButton = height;

        this.chatPanel = new ChatPanel(gamePanel, width1, height);
        add(chatPanel, BorderLayout.WEST);

        defaultInfo = createDefaultInfo();
        infoPanel = createInfoPanel(width2, height);
        add(infoPanel, BorderLayout.CENTER);

        buttonsPanel = createButtonsPanel();
        inventoryPanel = createInventoryPanel(width4, height);
        buttonsInventoryPanel = createButtonsInventoryPanel();
        add(buttonsInventoryPanel, BorderLayout.EAST);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                final Dimension dim = getSize();
                final int width1 = (int) (dim.width * 0.25);
                final int width2 = (int) (dim.width * 0.3);
                int width4 = (int) (dim.width * 0.3);
                width4 -= width4 % nbPropertiesGroup;
                final int width3 = dim.width - width1 - width2 - width4;
                chatPanel.setPreferredSize(new Dimension(width1, dim.height));
                infoPanel.setPreferredSize(new Dimension(width2, dim.height));
                buttonsInventoryPanel.setPreferredSize(new Dimension(width3 + width4, dim.height));
                buttonsPanel.setPreferredSize(new Dimension(width3, dim.height));
                inventoryPanel.setPreferredSize(new Dimension(width4, dim.height));
                revalidate();
                repaint();
            }
        });
        setVisible(true);
    }

    //#region gamestate

    public void gameStart() {
        chatPanel.gameStart();
    }

    public void newTurn() {
        chatPanel.newTurn();
        removeActionList();
        buttonsPanel = createButtonsPanel();
        buttonsInventoryPanel.add(buttonsPanel, BorderLayout.WEST);
        buttonsInventoryPanel.revalidate();
    }

    public void inJail() {
        chatPanel.inJail();
    }

    public void playerAction() {
        rollDiceButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(final MouseEvent e) {
                if (dice) {
                    removeActionList();
                    gamePanel.getController().rollDice();
                    rollDiceButton.removeMouseListener(this);
                } else {
                    if (otherActionBox.getSelectedItem() == startItem) {
                        controller.playerApplyStartItem();
                    } else {
                        controller.playerApplyShopItem(shopItem.get((ImageIcon) otherActionBox.getSelectedItem()));
                    }
                    gamePanel.updatePlayersScoreGraphics();
                }
            }
        });
    }

    public void rollDice() {
        chatPanel.rollDice();
    }

    public void playerArrived(final boolean isStarCell) {
        chatPanel.playerArrived(isStarCell);
    }


    public void endTurn() {
        chatPanel.endTurn();
    }

    public void gameEnd() {
        chatPanel.gameEnd();
    }

    //#endregion

    // #region Info

    private JPanel createInfoPanel(final int width, final int height) {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.add(defaultInfo, BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(width, height));
        panel.setBackground(Color.GRAY);
        return panel;
    }

    private JLabel createDefaultInfo() {
        final JLabel label = SwingFactory.createJLabel();
        label.setText("Rien a signaler...");
        final Font font = Resources.getFont(40);
        label.setFont(font);
        return label;
    }

    private void removeInfoPanelInside() {
        if (infoPanelInside != null) {
            infoPanel.remove(infoPanelInside);
            infoPanelInside = null;
            repaint();
            revalidate();
        } else {
            infoPanel.remove(defaultInfo);
        }
    }

    public void addDefaultInfo() {
        if (infoPanelInside != null) {
            infoPanel.remove(infoPanelInside);
            infoPanelInside = null;
            repaint();
            revalidate();
        }
        infoPanel.add(defaultInfo);
    }

    public void addPropertyInfo(final int cellID) {
        removeInfoPanelInside();
        final JPanel panel = new JPanel(new BorderLayout());
        final Property cell = (Property) controller.getCell(cellID);
        panel.setBackground(Color.decode(cell.getGroupHexColor()));
        // Owner
        final JPanel ownerPanel = new JPanel(new FlowLayout());
        final Font font = Resources.getFont(16);
        ownerPanel.setOpaque(false);
        final JLabel ownerLabel = new JLabel("Propriétaire : ");
        ownerLabel.setFont(font);
        final JLabel playerLabel = new JLabel();
        playerLabel.setFont(font);
        final Property property = ((Property) controller.getCell(cellID));
        final boolean hasOwner = property.hasOwner();
        if (hasOwner) {
            final String ownerName = property.getOwner().getName() + "";
            playerLabel.setText(ownerName);
            playerLabel.setIcon(new ImageIcon(Resources.getImage(property
                .getOwner().getClassName()).getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
        } else {
            playerLabel.setText(" Personne :(");
        }
        ownerPanel.add(ownerLabel);
        ownerPanel.add(playerLabel);
        panel.add(ownerPanel, BorderLayout.NORTH);
        // Prix
        final JPanel pricePanel = new JPanel(new BorderLayout());
        pricePanel.setBorder(BorderFactory.createEmptyBorder(10, 00, 10, 00));
        pricePanel.setOpaque(false);
        final JLabel buyLabel = SwingFactory.createJLabel();
        buyLabel.setFont(font);
        final JLabel rentLabel = SwingFactory.createJLabel();
        rentLabel.setFont(font);
        if (hasOwner) {
            final int buyPrice = property.getBuyPrices().get(property.getPropertyState() + 1);
            final JLabel currentPriceLabel = SwingFactory.createJLabel();
            currentPriceLabel.setFont(font);
            currentPriceLabel.setText("Prix actuel : " + property.getBuyPrices().get(property.getPropertyState()));
            pricePanel.add(currentPriceLabel, BorderLayout.NORTH);
            buyLabel.setText("Prix d'amélioration : " + buyPrice);
            rentLabel.setText("Loyer a payer : " + (int) (buyPrice * 0.70));
        } else {
            final int buyPrice = property.getBuyPrices().get(0);
            buyLabel.setText("Prix d'achat : " + buyPrice);
            rentLabel.setText("Loyer a payer : " + (int) (buyPrice * 0.70));
        }
        pricePanel.add(buyLabel, BorderLayout.CENTER);
        pricePanel.add(rentLabel, BorderLayout.SOUTH);
        panel.add(pricePanel, BorderLayout.CENTER);
        infoPanelInside = panel;
        infoPanel.add(infoPanelInside, BorderLayout.CENTER);
    }

    // #endregion

    private JPanel createButtonsInventoryPanel() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.add(buttonsPanel, BorderLayout.WEST);
        panel.add(inventoryPanel, BorderLayout.CENTER);
        panel.setVisible(true);
        return panel;
    }

    // #region Buttons

    private JPanel createButtonsPanel() {
        final JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.setPreferredSize(new Dimension(widthButton, heightButton));
        panel.setBackground(Color.PINK);

        final ArrayList<Object> objects = new ArrayList<>();
        objects.add(rollDiceIcon);
        this.startItem = new ImageIcon(Resources.getImage(controller.getCurrentPlayer().
            getStartItem().getItemName().name().toLowerCase()).getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        objects.add(startItem);

        for (int i = 0; i < controller.getCurrentPlayer().getShopItem().length; i++) {
            ImageIcon tmp = new ImageIcon(Resources.getImage(
                controller.getCurrentPlayer().getShopItem()[i].getItemName().name().toLowerCase())
                .getScaledInstance(50, 50, Image.SCALE_SMOOTH));
            objects.add(tmp);
            shopItem.put(tmp, i);
        }

        otherActionBox = new JComboBox<Object>(objects.toArray());
        rollDiceButton = new JButton();
        rollDiceButton.setIcon(Resources.getGif("rolling_dice"));

        ((JLabel) otherActionBox.getRenderer()).setHorizontalAlignment(JLabel.CENTER);

        otherActionBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    ImageIcon selectedIcon = (ImageIcon) otherActionBox.getSelectedItem();
                    if (selectedIcon == rollDiceIcon) {
                        dice = true;
                        rollDiceButton.setIcon(Resources.getGif("rolling_dice"));
                    } else {
                        dice = false;
                        rollDiceButton.setIcon(selectedIcon);
                    }
                }
            }
        });
        panel.add(otherActionBox);

        rollDiceButton.setBorderPainted(false);
        rollDiceButton.setOpaque(false);
        rollDiceButton.setContentAreaFilled(false);
        rollDiceButton.setFocusPainted(false);

        panel.add(rollDiceButton);
        return panel;
    }

    public void removeActionList() {
        buttonsInventoryPanel.remove(buttonsPanel);
        buttonsInventoryPanel.revalidate();
    }

    // #endregion

    // #region Inventaire

    private JPanel createInventoryPanel(final int width, final int height) {
        final HashMap<String, ArrayList<Integer>> propertiesInfo = gamePanel.getPropertiesInfo();
        final int groupPropertiesNb = propertiesInfo.size();
        int maxProperties = 0;
        for (String key : propertiesInfo.keySet()) {
            final int size = propertiesInfo.get(key).size();
            if (size > maxProperties) {
                maxProperties = size;
            }
        }
        final JPanel containerPanel = new JPanel(new GridLayout(1, groupPropertiesNb));
        containerPanel.setPreferredSize(new Dimension(width, height));
        containerPanel.setOpaque(false);
        for (String hexColor : propertiesInfo.keySet()) {
            final JPanel groupPanel = new JPanel(new GridLayout(maxProperties, 1));
            initGroupPropertyPanel(groupPanel, hexColor, propertiesInfo.get(hexColor),
                width / groupPropertiesNb, height / maxProperties);
            containerPanel.add(groupPanel);
        }
        return containerPanel;
    }

    private void initGroupPropertyPanel(final JPanel panel, final String hexColor,
            final ArrayList<Integer> propertiesID, final int width, final int height) {
        for (int i = 0; i < propertiesID.size(); i++) {
            final int propertyCellID = propertiesID.get(i);
            final JButton propertyButton = new JButton() {
                @Override
                protected void paintComponent(final Graphics g) {
                    final Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    final Dimension dim = getSize();
                    g2d.setColor(Color.decode(hexColor));
                    g2d.fillRect(0, 0, dim.width, dim.height);
                    final Property property = ((Property) gamePanel.getController().getCell(propertyCellID));
                    if (property.getOwner() == null) {
                        g2d.setColor(Color.WHITE);
                    } else {
                        g2d.setColor(
                            Color.decode(property.getOwner().getHexTeamColor()));
                    }
                    // Couleur intérieur
                    final int widthInside = (int) (dim.width * 0.9);
                    final int heightInside = (int) (dim.height * 0.9);
                    final int xInside = (dim.width - widthInside) / 2;
                    final int yInside = (dim.height - heightInside) / 2;
                    g2d.fillRoundRect(xInside, yInside, widthInside, heightInside, 5, 5);
                    // Icône
                    final int widthIcon = (int) (dim.width * 0.7);
                    final int heightIcon = (int) (dim.height * 0.7);
                    final int xIcon = (dim.width - widthIcon) / 2;
                    final int yIcon = (dim.height - heightIcon) / 2;
                    g2d.drawImage(PROPERTY_IMAGE, xIcon, yIcon, widthIcon, heightIcon, this);
                }
            };

            propertyButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(final MouseEvent e) {
                    addPropertyInfo(propertyCellID);
                    revalidate();
                    repaint();
                }
            });
            propertyButton.setSize(new Dimension(width, height));
            panel.add(propertyButton);
        }
    }

    //#endregion

    public JPanel getButtonsPanel() {
        return buttonsPanel;
    }

    public ChatPanel getChatPanel() {
        return chatPanel;
    }

    public JPanel getButtonsInventoryPanel() {
        return buttonsInventoryPanel;
    }

}
