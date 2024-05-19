package view.menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.HashMap;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import controller.resources.Resources;
import view.SwingFactory;

public final class GameConfigPanel extends JPanel {

    private final MenuPanel menuPanel;
    private int height;
    private int width;

    private JLabel titleMap = new JLabel();

    private HashMap<String, String> gameConfig = new HashMap<>();
    private HashMap<String, ButtonGroup> buttonGroups = new HashMap<>();

    private int defaultFont;
    private int nbMaps = 4;
    private int selectMap = 1;

    public GameConfigPanel(final MenuPanel menuPanel) {
        setOpaque(false);
        this.menuPanel = menuPanel;
        height = menuPanel.getHeight();
        width = menuPanel.getWidth();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setSize(new Dimension(width, height));
        this.defaultFont = menuPanel.getDefaultFont();
        add(SwingFactory.createTransparentPanel(this, 100, 10));
        add(createGameLobby(this));
        add(createBottomButton(this));
        setVisible(true);
    }

    // #region Create GameLobby

    private JPanel createGameLobby(final JPanel parent) {
        final JPanel panel = SwingFactory.createImagePanel(parent, "sign", 100, 85);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        panel.add(SwingFactory.createTransparentPanel(parent, 2, 0));
        panel.add(createMapSection(parent));
        panel.add(createGameSettings(parent));
        return panel;
    }

    // #endregion

    // #region Create MapSection

    private JPanel createMapSection(final JPanel parent) {
        final JPanel panel = SwingFactory.createImagePanel(parent, "paper", 50, 90);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        final JPanel mapSelection = SwingFactory.createTransparentPanel(parent, 100, 50);
        mapSelection.setLayout(new BorderLayout());

        int widthPer = 70;
        int heightPer = 40;
        Image mapImg = Resources.getImage("preview" + selectMap);

        JButton left = SwingFactory.createImageButton(parent, "left", 20, 10, false);
        mapSelection.add(left, BorderLayout.WEST);
        JLabel mapLabel = SwingFactory.createImageImageLabel(parent, mapImg, widthPer, heightPer, false);
        mapSelection.add(mapLabel, BorderLayout.CENTER);
        JButton right = SwingFactory.createImageButton(parent, "right", 20, 10, false);
        mapSelection.add(right, BorderLayout.EAST);
        panel.add(mapSelection);

        final JPanel mapDescription = SwingFactory.createTransparentPanel(parent, 100, 50);
        mapDescription.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        mapDescription.setLayout(new BorderLayout());

        JTextArea desc = new JTextArea();
        desc.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        desc.setFont(Resources.getFont("NoitaBlackletter", defaultFont / 2));
        desc.setForeground(Color.BLACK);
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);
        desc.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(desc, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        mapDescription.add(scrollPane, BorderLayout.CENTER);
        panel.add(mapDescription);

        selectMap(parent, mapLabel, desc, 0, widthPer, heightPer);
        mapListerner(left, parent, mapLabel, desc, -1, widthPer, heightPer);
        mapListerner(right, parent, mapLabel, desc, 1, widthPer, heightPer);
        return panel;
    }

    private void mapListerner(JButton button, JPanel mapSelection, JLabel mapLabel, JTextArea desc,
    int i, int widthPer, int heightPer) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                selectMap(mapSelection, mapLabel, desc, i, widthPer, heightPer);
            }
        });
        menuPanel.handCursor(button);
    }

    // #endregion

    // #region Create GameSettings

    private JPanel createGameSettings(final Component parent) {
        final JPanel panel = SwingFactory.createTransparentPanel(parent, 50, 90);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(createTitleMap(parent));
        panel.add(SwingFactory.createTransparentPanel(parent, 40, 10));
        panel.add(createMapConfig(parent));
        return panel;
    }

    private JPanel createTitleMap(final Component parent) {
        final JPanel panel = SwingFactory.createImagePanel(parent, "paper", 40, 25);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(Box.createVerticalGlue());

        titleMap = SwingFactory.createTextLabel("Royaume Médieval", Color.BLACK, defaultFont);
        titleMap.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleMap);
        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private JPanel createMapConfig(final Component parent) {
        final JPanel panel = SwingFactory.createImagePanel(parent, "paper", 40, 65);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel turnsPanel = SwingFactory.createTransparentPanel(parent, 40, 10);
        turnsPanel.setLayout(new BoxLayout(turnsPanel, BoxLayout.X_AXIS));
        turnsPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 0, 0));
        JLabel turns = SwingFactory.createTextLabel("Nombre de tours :", Color.BLACK, defaultFont / 2);

        ButtonGroup turnsGroup = new ButtonGroup();
        JRadioButton turnsBox1 = createRadioButton("25", turnsGroup, true);
        JRadioButton turnsBox2 = createRadioButton("35", turnsGroup, false);
        JRadioButton turnsBox3 = createRadioButton("45", turnsGroup, false);
        turnsPanel.add(turns);
        turnsPanel.add(turnsBox1);
        turnsPanel.add(turnsBox2);
        turnsPanel.add(turnsBox3);

        JPanel doubleDicePanel = SwingFactory.createTransparentPanel(parent, 40, 10);
        doubleDicePanel.setLayout(new BoxLayout(doubleDicePanel, BoxLayout.X_AXIS));
        doubleDicePanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 0, 0));
        JLabel doubleDice = SwingFactory.createTextLabel("Double dé :", Color.BLACK, defaultFont / 2);

        ButtonGroup doubleDiceGroup = new ButtonGroup();
        JRadioButton doubleDiceBox1 = createRadioButton("Désactivé", doubleDiceGroup, true);
        JRadioButton doubleDiceBox2 = createRadioButton("Activé", doubleDiceGroup, false);

        doubleDicePanel.add(doubleDice);
        doubleDicePanel.add(doubleDiceBox1);
        doubleDicePanel.add(doubleDiceBox2);

        JPanel modePanel = SwingFactory.createTransparentPanel(parent, 40, 10);
        modePanel.setLayout(new BoxLayout(modePanel, BoxLayout.X_AXIS));
        modePanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 0, 0));
        JLabel mode = SwingFactory.createTextLabel("Mode de jeu :", Color.BLACK, defaultFont / 2);

        ButtonGroup modeGroup = new ButtonGroup();
        JRadioButton modeBox1 = createRadioButton("FFA", modeGroup, true);
        JRadioButton modeBox2 = createRadioButton("2v2", modeGroup, false);

        modePanel.add(mode);
        modePanel.add(modeBox1);
        modePanel.add(modeBox2);

        buttonGroups.put("totalTurns", turnsGroup);
        buttonGroups.put("doubleDice", doubleDiceGroup);
        buttonGroups.put("mode", modeGroup);

        panel.add(turnsPanel);
        panel.add(doubleDicePanel);
        panel.add(modePanel);
        return panel;
    }

    private JRadioButton createRadioButton(String text, ButtonGroup group, boolean selected) {
        JRadioButton radioButton = new JRadioButton(text, selected);
        radioButton.setOpaque(false);
        radioButton.setForeground(Color.BLACK);
        radioButton.setBorderPainted(false);
        radioButton.setContentAreaFilled(false);
        radioButton.setFocusPainted(false);
        radioButton.setFont(Resources.getFont("NoitaBlackletter", 32));
        group.add(radioButton);
        return radioButton;
    }

    // #endregion

    // #region BottomButton

    private JPanel createBottomButton(final JPanel parent) {
        final JPanel panel = SwingFactory.createTransparentPanel(parent, 100, 10);
        panel.setLayout(new BorderLayout());
        final JButton back = SwingFactory.createTextButton(parent, "Retourner à la base", 10, 10, 64, false);
        menuPanel.handCursor(back);
        back.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(final MouseEvent e) {
                GameConfigPanel.this.setVisible(false);
                parent.setVisible(false);
                menuPanel.setBackgroundImage(Resources.getGif("back"));
                SwingFactory.startSingleActionTimer(1800, action -> {
                    menuPanel.removeComp(MenuPanel.getPanelIndex());
                    menuPanel.addComp(menuPanel.createSelectModeMenu(), MenuPanel.getPanelIndex());
                    menuPanel.setBackgroundImage(Resources.getGif("backgroundMoving"));
                });
            }
        });
        menuPanel.handCursor(back);
        panel.add(back, BorderLayout.WEST);

        JButton selectCharacter = SwingFactory.createTextButton(parent,
            "Selectionner cette aventure", 10, 10, 64, false);
        selectCharacter.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 100));
        selectCharacter.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                for (String key : buttonGroups.keySet()) {
                    ButtonGroup bg = buttonGroups.get(key);
                    for (Enumeration<AbstractButton> buttons = bg.getElements(); buttons.hasMoreElements();) {
                        AbstractButton button = buttons.nextElement();
                        if (button.isSelected()) {
                            if (button.getText().equals("Désactivé")) {
                                gameConfig.put(key, "false");
                            } else if (button.getText().equals("Activé")) {
                                gameConfig.put(key, "true");
                            } else {
                                gameConfig.put(key, button.getText());
                            }
                            break;
                        }
                    }
                }
                String mapName = "";
                switch (selectMap) {
                    case 1:
                        mapName = "kingdom";
                        break;
                    case 2:
                        mapName = "spaceship";
                        break;
                    case 3:
                        mapName = "villanova";
                        break;
                    case 4:
                        mapName = "frostharbor";
                        break;
                    default:
                        break;
                }
                gameConfig.put("mapName", mapName);
                menuPanel.removeComp(MenuPanel.getPanelIndex());
                menuPanel.addComp(new PlayerConfigPanel(menuPanel, gameConfig), MenuPanel.getPanelIndex());
                menuPanel.setBackgroundImage(new ImageIcon(Resources.getImage("village")));
            }
        });
        menuPanel.handCursor(selectCharacter);
        panel.add(selectCharacter, BorderLayout.EAST);
        return panel;
    }

    // #endregion

    // #region Selector

    private void selectMap(JPanel parent, JLabel map, JTextArea desc, int i, int widthPer, int heightPer) {
        selectMap += i;
        if (selectMap > nbMaps) {
            selectMap = 1;
        } else if (selectMap <= 0) {
            selectMap = nbMaps;
        }
        Image mapImg = Resources.getImage("preview" + selectMap);
        switch (selectMap) {
            case 1 :
                titleMap.setText("Royaume médieval");
                desc.setText("Votre aventure a pour but d'accumuler autant de couronnes que possible "
                    + "dans un nombre de tours restreint, aspirant ainsi au titre de souverain du royaume.\n"
                    + "Vous endosserez le rôle d'un intrépide aventurier animé par l'ambition de régner en "
                    + "maître absolu sur tous les domaines et autres joueurs.");
                break;
            case 2 :
                titleMap.setText("Station Andromeda");
                desc.setText("Cette fois aventuriers astronautes, embarquez dans Andromeda et explorez les "
                    + "moindres recoins de la station spatiale. Emparez-vous du maximum de ressources oubliées "
                    + "en ces lieux et servez-vous en pour vous réclamer votre monopole sur la station et "
                    + "débuter votre domination de l'espace.");
                break;
            case 3 :
                titleMap.setText("Villa Nova");
                desc.setText("Villa Nova et ses nombreuses propriétés n'attendent que votre vous pour les "
                    + "saisir et les collectionner. Dans votre aventure, vous pourrez récolter de précieuses "
                    + "sommes d'argent qui vous servirons à acquérir vos biens. Attention toutefois à ne pas "
                    + "perdre ce précieux argent ou à ne pas vous retrouver derrière les barreaux.");
                break;
            case 4 :
                titleMap.setText("Frost Arbor");
                desc.setText("Vous êtes piégé sur le vieux port de Frost Arbor, un lieu où le froid glacial  "
                    + "rencontre les eaux sombres et mystérieuses. Les bâtiments délabrés et les quais abandonnés "
                    + "racontent des histoires de marins disparus et de trésors perdus. Seule la richesse "
                    + "peut acheter votre liberté. ");
            default:
                break;
        }

        final Dimension size = SwingFactory.calcDimension(mapImg, parent, widthPer, heightPer, false);
        map.setPreferredSize(new Dimension((int) size.getWidth() * widthPer / 100,
            (int) (size.getHeight() * heightPer / 100)));
        map.setIcon(new ImageIcon(mapImg.getScaledInstance(
            (int) size.getWidth(), (int) size.getHeight(), Image.SCALE_SMOOTH)));

        parent.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                final Dimension size = SwingFactory.calcDimension(mapImg, parent, widthPer, heightPer, false);
                map.setPreferredSize(new Dimension((int) size.getWidth() * widthPer / 100,
                    (int) (size.getWidth() * heightPer / 100)));
                map.setIcon(new ImageIcon(mapImg.getScaledInstance((int) size.getWidth(),
                    (int) size.getHeight(), Image.SCALE_SMOOTH)));
            }
        });
        map.repaint();
        map.revalidate();
    }

    // #endregion

}
