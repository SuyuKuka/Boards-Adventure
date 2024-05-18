package view.menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import controller.resources.Audio;
import controller.resources.Resources;
import view.JLayeredManager;
import view.SwingFactory;
import view.View;

/**
 * Classe MenuPanel qui s'occupe de toute la vue des menus du jeu.
 * Elle contient : le menu principal, la sélection de niveau, les paramètres, le manuel de jeu.
 * Chaque menu est un panel et tout en bas se trouve un panel pour le background.
 */
public final class MenuPanel extends JLayeredManager {

    private static final int BACKGROUND_INDEX = 0;
    private static final int MANUALICON_INDEX = 7;
    private static final int MANUALBOOK_INDEX = 8;

    private MenuState menuState;

    private final View frame;
    private final Container pane;
    private HashMap<String, String> gameConfig = new HashMap<>();
    private ArrayList<HashMap<String, String>> playersConfig = new ArrayList<>();
    private HashMap<String, String> player;
    private HashMap<String, ButtonGroup> buttonGroups = new HashMap<>();

    private int height;
    private int width;

    private boolean host = false;
    private boolean resizing = false;
    private int nbPlayers = 0;
    private int select = 1;
    private int nbMaps = 4;
    private int object = 1;

    private String code = "";
    private InetAddress ip;
    private int port = 55000;

    private JLabel titleMap = new JLabel();

    private JPanel background = null;
    private boolean animation = false;

    private int defaultFont = 64;

    public MenuPanel(final View frame) {
        super(frame.getContentPane().getWidth(), frame.getContentPane().getHeight());
        final Container contentPane = frame.getContentPane();
        setBounds(0, 0, contentPane.getWidth(), contentPane.getHeight());
        setMinimumSize(contentPane.getMinimumSize());
        setPreferredSize(contentPane.getSize());
        this.frame = frame;
        this.pane = frame.getContentPane();
        this.height = frame.getContentPane().getHeight();
        this.width = frame.getContentPane().getWidth();
        // Audio.playMusic("MenuTheme.wav");
        initDefaultValues();
        createPanels();
        changeState(MenuState.MAINMENU);
        setVisible(true);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                final Dimension dim = getSize();
                setPreferredSize(dim);
                for (Component comp : getLayersMap().values()) {
                    comp.setPreferredSize(dim);
                    comp.setSize(dim);
                }
                revalidate();
                repaint();
            }
        });
    }

    private void initDefaultValues() {
        menuState = null;
        // map = "map4";
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        }
    }

    private void createPanels() {
        addComp(createBackground(), BACKGROUND_INDEX);
        addComp(createMainMenu(), MenuState.MAINMENU.getLayerIndex());
        addComp(createSettingsMenu(), MenuState.SETTINGS.getLayerIndex());
        addComp(createCreateJoinMenu(), MenuState.SELECTMOD.getLayerIndex());
        addComp(createJoinCodeMenu(), MenuState.JOINGAME.getLayerIndex());
        addComp(createGameLobbyMenu(), MenuState.MAPCONFIG.getLayerIndex());
        addComp(createSelectionCharacterMenu(), MenuState.CHARACTER.getLayerIndex());
        addComp(createManualIcon(), MANUALICON_INDEX);
        addComp(createManualBook(), MANUALBOOK_INDEX);
    }

    public void changeState(final MenuState newMenuState) {
        if (menuState != null) {
            getLayerComp(menuState.getLayerIndex()).setVisible(false);
        }
        this.menuState = newMenuState;
        final JPanel newStatePanel = (JPanel) getLayerComp(menuState.getLayerIndex());
        newStatePanel.setBounds(0, 0, width, height);
        newStatePanel.setVisible(true);
        revalidate();
    }

    private void createSwitchStateButton(final JButton button, final MenuState newMenuState) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                changeState(newMenuState);
            }
        });
    }

    private void handCursor(JButton button) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                frame.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }

    // #region background 0

    private JPanel createBackground() {
        background = SwingFactory.createGifPanel(pane, "backgroundMoving", 100, 100);
        background.setBounds(0, 0, width, height);
        background.setVisible(true);
        background.revalidate();
        background.repaint();
        revalidate();
        repaint();
        return background;
    }

    // #endregion

    // #region mainmenu 1

    private JPanel createMainMenu() {
        final JPanel panel = SwingFactory.createContainerPanel(pane, 0, 0, 0, 5);

        panel.add(SwingFactory.createTransparentPanel(panel, 45, 45));

        final JButton play = SwingFactory.createLobbyButton(panel, "Commencer une aventure", 20, 10, 64, true);
        createSwitchStateButton(play, MenuState.SELECTMOD);
        handCursor(play);
        panel.add(play);
        final JButton options = SwingFactory.createLobbyButton(panel, "Préparer l'expédition", 20, 10, 64, true);
        createSwitchStateButton(options, MenuState.SETTINGS);
        handCursor(options);
        panel.add(options);
        final JButton leaveGame = SwingFactory.createLobbyButton(panel, "Retourner au foyer", 20, 10, 64, true);
        leaveGame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.dispose();
                System.exit(0);
            }
        });
        handCursor(leaveGame);
        panel.add(leaveGame);

        return panel;
    }

    // #endregion

    // #region settings 2

    private JPanel createSettingsMenu() {
        final JPanel panel = SwingFactory.createContainerPanel(pane, 45, 0, 0, 6);

        // final int widthMargin = frame.getWidth() / 20;
        // final int heightMargin = frame.getHeight() / 20;
        // Musique
        final JPanel musicPanel = new JPanel();
        musicPanel.setLayout(new BoxLayout(musicPanel, BoxLayout.X_AXIS));
        musicPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        musicPanel.setOpaque(false);
        musicPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        final JLabel musique = SwingFactory.createTextLabel("Musique : - ", "NoitaBlackletter",
            Color.BLACK, defaultFont);
        final JSlider musicSlider = SwingFactory.createMusicSlider();
        final JLabel plusMusic = SwingFactory.createTextLabel(" +", "NoitaBlackletter", Color.BLACK, defaultFont);
        musicPanel.add(musique);
        musicPanel.add(musicSlider);
        musicPanel.add(plusMusic);
        // Son
        final JPanel soundPanel = new JPanel();
        soundPanel.setLayout(new BoxLayout(soundPanel, BoxLayout.X_AXIS));
        soundPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        soundPanel.setOpaque(false);
        soundPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        final JLabel sound = SwingFactory.createTextLabel("Son : - ", "NoitaBlackletter", Color.BLACK, defaultFont);
        final JSlider soundSlider = SwingFactory.createSoundSlider();
        final JLabel plusSound = SwingFactory.createTextLabel(" +", "NoitaBlackletter", Color.BLACK, defaultFont);
        soundPanel.add(sound);
        soundPanel.add(soundSlider);
        soundPanel.add(plusSound);

        panel.add(musicPanel);
        panel.add(soundPanel);
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                panel.setPreferredSize(new Dimension(panel.getWidth(), panel.getHeight()));
            }
        });
        final JButton back = SwingFactory.createLobbyButton(panel, "Rebrousser chemin", 20, 10, 64, true);
        createSwitchStateButton(back, MenuState.MAINMENU);
        handCursor(back);
        panel.add(back);
        return panel;
    }
    // #endregion

    // #region createjoin 3

    private JPanel createCreateJoinMenu() {
        final JPanel panel = SwingFactory.createContainerPanel(pane, 0, 0, 0, 5);
        panel.add(SwingFactory.createTransparentPanel(panel, 45, 45));

        final JButton create = SwingFactory.createLobbyButton(panel, "Créer une nouvelle aventure", 20, 10, 64, true);
        create.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                host = true;
                panel.setVisible(false);
                animation1(panel, MenuState.MAPCONFIG);
            }
        });
        handCursor(create);
        panel.add(create);

        final JButton join = SwingFactory.createLobbyButton(panel, "Rejoindre une escouade", 20, 10, 64, true);
        createSwitchStateButton(join, MenuState.JOINGAME);
        handCursor(join);
        panel.add(join);

        final JButton back = SwingFactory.createLobbyButton(panel, "Rebrousser chemin", 20, 10, 64, true);
        createSwitchStateButton(back, MenuState.MAINMENU);
        handCursor(back);
        panel.add(back);
        return panel;
    }

    // #endregion

     // #region joincode 4

    private JPanel createJoinCodeMenu() {
        final JPanel panel = SwingFactory.createContainerPanel(pane, 0, 0, 0, 5);
        panel.add(SwingFactory.createTransparentPanel(panel, 45, 45));

        JLabel title = new JLabel();
        title.setBorder(BorderFactory.createEmptyBorder(0, 18, 0, 0));
        title.setText("Saisir les coordonnées de l'aventure : ");
        title.setFont(Resources.getFont("NoitaBlackletter", defaultFont));
        title.setForeground(Color.BLACK);
        panel.add(title);

        JPanel codePanel = SwingFactory.createTransparentPanel(panel, 40, 6);
        codePanel.setBorder(BorderFactory.createEmptyBorder(0, 100, 0, 0));
        codePanel.setLayout(new BorderLayout());
        codePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea joinCode = new JTextArea();
        joinCode.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        joinCode.setFont(Resources.getFont("NoitaBlackletter", defaultFont / 2));
        joinCode.setText("(AdresseIP:Port)");
        joinCode.setPreferredSize(new Dimension(250, 20));
        joinCode.setForeground(Color.LIGHT_GRAY);
        joinCode.setLineWrap(true);
        joinCode.setWrapStyleWord(true);

        joinCode.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (joinCode.getText().equals("(AdresseIP:Port)")) {
                    joinCode.setEditable(true);
                    joinCode.setText("");
                    joinCode.setForeground(Color.BLACK);
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (joinCode.getText().equals("(AdresseIP:Port)")) {
                    joinCode.setEditable(true);
                    joinCode.setText("");
                    joinCode.setForeground(Color.BLACK);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (joinCode.getText().equals("")) {
                    joinCode.setEditable(false);
                    joinCode.setText("(AdresseIP:Port)");
                    joinCode.setForeground(Color.LIGHT_GRAY);
                }
            }
        });

        codePanel.add(joinCode, BorderLayout.CENTER);

        JButton join = SwingFactory.createImageButton(panel, "next_arrow", 6, 6, false);
        join.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!joinCode.getText().equals("(AdresseIP:Port)") && !joinCode.getText().equals("")) {
                    host = false;
                    code = joinCode.getText();
                    System.out.println(code);
                    panel.setVisible(false);
                    animation1(panel, MenuState.CHARACTER);
                }
            }
        });
        codePanel.add(join, BorderLayout.EAST);
        handCursor(join);
        panel.add(codePanel);

        JButton back = SwingFactory.createLobbyButton(panel, "Rebrousser chemin", 20, 10, 64, true);
        createSwitchStateButton(back, MenuState.SELECTMOD);
        handCursor(back);
        panel.add(back);
        return panel;
    }

    // #endregion

    // #region animation

    private void animation1(JPanel panel, MenuState state) {
        if (!animation) {
            animation = true;
            changeBackgroundGif("good");

            Timer timer = new Timer(2900, f -> {
                changeBackgroundImg("blackscreen");
            });
            timer.start();
            timer.setRepeats(false);

            Timer tmp = new Timer(3200, f -> {
                changeState(state);
                if (state == MenuState.MAPCONFIG) {
                    changeBackgroundImg("sign");
                } else if (state == MenuState.CHARACTER) {
                    changeBackgroundImg("village");
                }
                animation = false;
            });
            tmp.start();
            tmp.setRepeats(false);
        }
    }

    private void animation2(JPanel panel, MenuState state, String gif) {
        if (!animation) {
            animation = true;
            changeBackgroundGif(gif);

            Timer tmp = new Timer(1600, f -> {
                changeState(MenuState.SELECTMOD);
                changeBackgroundGif("backgroundMoving");
                animation = false;
            });
            tmp.start();
            tmp.setRepeats(false);
        }
    }

    private void changeBackgroundGif(String gif) {
        removeComp(BACKGROUND_INDEX);

        JPanel animate = SwingFactory.createGifPanel(pane, gif, 100, 100);
        animate.setBounds(0, 0, width, height);
        background.add(animate);
        addComp(background, BACKGROUND_INDEX);
    }

    private void changeBackgroundImg(String img) {
        removeComp(BACKGROUND_INDEX);

        JPanel animate = SwingFactory.createImagePanel(pane, img, 100, 100);
        animate.setBounds(0, 0, width, height);
        background.add(animate);
        addComp(background, BACKGROUND_INDEX);
    }


    // #endregion

    // #region gamelobby 5

    private JPanel createGameLobbyMenu() {
        final JPanel panel = SwingFactory.createContainerPanel(pane, 2, 0, 0, 0);
        panel.add(createLobbyCode(panel));
        panel.add(createGameLobby(panel));
        panel.add(createBottomButton(panel));
        return panel;
    }

    private JPanel createLobbyCode(final Component parent) {
        final JPanel panel = SwingFactory.createTransparentPanel(parent, 100, 5);
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 0));
        final JLabel lobbyCode = SwingFactory.createTextLabel("Code : " + ip.getHostAddress() + ":" + port,
            "NoitaBlackletter", Color.BLACK, defaultFont / 2);
        lobbyCode.setVerticalAlignment(JLabel.CENTER);
        panel.add(lobbyCode);
        return panel;
    }

    private JPanel createGameLobby(final JPanel parent) {
        final JPanel panel = SwingFactory.createImagePanel(parent, "sign", 100, 85);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        panel.setSize(panel.getPreferredSize());
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        panel.add(SwingFactory.createTransparentPanel(parent, 2, 0));
        panel.add(createMapSection(panel));
        panel.add(createGameSettings(panel));
        return panel;
    }

    private JPanel createMapSection(final JPanel parent) {
        final JPanel panel = SwingFactory.createImagePanel(parent, "paper", 50, 90);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        final JPanel mapSelection = SwingFactory.createTransparentPanel(parent, 100, 50);
        mapSelection.setLayout(new BorderLayout());

        ImageName mapImg = new ImageName(1, Resources.getImage("preview1"));

        JButton left = SwingFactory.createImageButton(parent, "left", 20, 10, false);
        mapSelection.add(left, BorderLayout.WEST);
        JLabel mapLabel = createImageImageLabel(parent, mapImg, 60, 60, false);
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

        selectMap(parent, mapImg, mapLabel, desc, 0);
        mapListerner(left, parent, mapImg, mapLabel, desc, -1);
        mapListerner(right, parent, mapImg, mapLabel, desc, 1);
        return panel;
    }

    private void mapListerner(JButton button, JPanel mapSelection, ImageName mapImg,
        JLabel mapLabel, JTextArea desc, int i) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectMap(mapSelection, mapImg, mapLabel, desc, i);
            }
        });
        handCursor(button);
    }

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

        titleMap = SwingFactory.createTextLabel("Royaume Médieval", "NoitaBlackletter", Color.BLACK, defaultFont);
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
        JLabel turns = SwingFactory.createTextLabel("Nombre de tours :", "NoitaBlackletter",
            Color.BLACK, defaultFont / 2);

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
        JLabel doubleDice = SwingFactory.createTextLabel("Double dé :", "NoitaBlackletter",
            Color.BLACK, defaultFont / 2);

        ButtonGroup doubleDiceGroup = new ButtonGroup();
        JRadioButton doubleDiceBox1 = createRadioButton("Désactivé", doubleDiceGroup, true);
        JRadioButton doubleDiceBox2 = createRadioButton("Activé", doubleDiceGroup, false);

        doubleDicePanel.add(doubleDice);
        doubleDicePanel.add(doubleDiceBox1);
        doubleDicePanel.add(doubleDiceBox2);

        JPanel modePanel = SwingFactory.createTransparentPanel(parent, 40, 10);
        modePanel.setLayout(new BoxLayout(modePanel, BoxLayout.X_AXIS));
        modePanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 0, 0));
        JLabel mode = SwingFactory.createTextLabel("Mode de jeu :", "NoitaBlackletter", Color.BLACK, defaultFont / 2);

        ButtonGroup modeGroup = new ButtonGroup();
        JRadioButton modeBox1 = createRadioButton("FFA", modeGroup, true);
        JRadioButton modeBox2 = createRadioButton("2v2", modeGroup, false);

        modePanel.add(mode);
        modePanel.add(modeBox1);
        modePanel.add(modeBox2);

        buttonGroups.put("turns", turnsGroup);
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

    private JPanel createBottomButton(final JPanel parent) {
        final JPanel panel = SwingFactory.createTransparentPanel(parent, 100, 10);
        panel.setLayout(new BorderLayout());
        //temporaire
        final JButton back = new JButton("Retourner à la base");
        back.setBorderPainted(false);
        back.setOpaque(false);
        back.setContentAreaFilled(false);
        back.setFocusPainted(false);
        back.setForeground(Color.BLACK);
        back.setAlignmentX(Component.LEFT_ALIGNMENT);
        back.setFont(Resources.getFont("NoitaBlackletter", 64));
        back.setHorizontalTextPosition(JButton.LEFT);
        back.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                host = false;
                parent.setVisible(false);
                animation2(parent, MenuState.SELECTMOD, "back");
                // removeComp(BACKGROUND_INDEX);
                // addComp(createBackground(), BACKGROUND_INDEX);
            }
        });
        // createSwitchStateButton(back, MenuState.SELECTMOD);
        handCursor(back);
        panel.add(back, BorderLayout.WEST);

        JButton selectCharacter = new JButton("Selectionner cette aventure");
        selectCharacter.setBorderPainted(false);
        selectCharacter.setOpaque(false);
        selectCharacter.setContentAreaFilled(false);
        selectCharacter.setFocusPainted(false);
        selectCharacter.setForeground(Color.BLACK);
        selectCharacter.setAlignmentX(Component.RIGHT_ALIGNMENT);
        selectCharacter.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 100));
        selectCharacter.setFont(Resources.getFont("NoitaBlackletter", 64));
        selectCharacter.setHorizontalTextPosition(JButton.LEFT);
        selectCharacter.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (String key : buttonGroups.keySet()) {
                    ButtonGroup bg = buttonGroups.get(key);
                    for (Enumeration<AbstractButton> buttons = bg.getElements(); buttons.hasMoreElements();) {
                        AbstractButton button = buttons.nextElement();
                        if (button.isSelected()) {
                            gameConfig.put(key, button.getText());
                            break;
                        }
                    }
                }
                changeBackgroundImg("village");
                System.out.println(gameConfig);
            }
        });
        createSwitchStateButton(selectCharacter, MenuState.CHARACTER);
        handCursor(selectCharacter);
        panel.add(selectCharacter, BorderLayout.EAST);
        return panel;
    }

    // #endregion

    // IL DOIT Y AVOIR UN INIT HOST
    // QUAND JE CREER UN JOUEUR YA UN INIT PLAYER
    // FAUT POSSIBILIER DE SUPPRIMER UN JOUEUR
    // QUAND TU AJOUTER UN PLAYER, LA FLECHE DISPARAIT MAIS UNE NOUVELLE APPARAIT

    //#region character 6

    private JPanel createSelectionCharacterMenu() {
        final JPanel panel = SwingFactory.createContainerPanel(pane, 10, 0, 0, 0);
        panel.add(createConfigCharacter(panel));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(SwingFactory.createTransparentPanel(panel, 100, 70));
        panel.add(createBottomPanel(panel));
        return panel;
    }

    private JPanel createConfigCharacter(final JPanel parent) {
        final JPanel panel = SwingFactory.createTransparentPanel(parent, 100, 80);
        panel.setLayout(new GridLayout(0, 4));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel addPlayer4 = createAddPlayer(parent, 3, null);
        JPanel addPlayer3 = createAddPlayer(parent, 2, addPlayer4);
        JPanel addPlayer2 = createAddPlayer(parent, 1, addPlayer3);
        JPanel character1 = createCharacter(parent, 0);

        panel.add(character1);
        panel.add(addPlayer2);
        addPlayer3.setVisible(false);
        panel.add(addPlayer3);
        addPlayer4.setVisible(false);
        panel.add(addPlayer4);
        return panel;
    }

    private JPanel createCharacter(final JPanel parent, final int playerID) {
        final JPanel panel = SwingFactory.createTransparentPanel(parent, 100, 20);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        character(panel, parent, playerID);
        return panel;
    }

    private void character(JPanel panel, JPanel parent, int playerID) {
        initCharacterValue(playerID);

        JPanel team = createTeam(parent, playerID);
        panel.add(team);

        JPanel pseudo = createPseudo(parent, playerID);
        pseudo.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));
        panel.add(pseudo);

        JPanel skin = createSelectCharacter(parent, playerID);
        panel.add(skin);

        JPanel item = createSelectItem(parent, playerID);
        panel.add(item);

        JPanel status = createStatus(parent, playerID, (JTextPane) pseudo.getComponent(0));
        panel.add(status);
    }

    private void initCharacterValue(final int playerID) {
        HashMap<String, String> newPlayer = new HashMap<>();
        newPlayer.put("team", "none");
        newPlayer.put("pseudo", "Player" + (playersConfig.size() + 1));
        newPlayer.put("skin", "1"); //CHECK SI LE SKIN EST DISPONIBLE
        newPlayer.put("item", "1");
        newPlayer.put("host", host ? "true" : "false");
        newPlayer.put("status", "false");
        playersConfig.add(playerID, newPlayer);
    }

    private JPanel createTeam(final JPanel parent, int playerID) {
        final JPanel panel = SwingFactory.createTransparentPanel(parent, 100, 20);
        panel.setLayout(new GridLayout(1, 2));

        JButton blue = new JButton("BLEU");
        blue.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                //FAIRE LE CHECKING DE TEAM MAX 2
                playersConfig.get(playerID).replace("team", "blue");
                System.out.println("Team : " + playersConfig.get(playerID).get("team"));
            }
        });
        panel.add(blue);

        JButton red = new JButton("ROUGE");
        red.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                //FAIRE LE CHECKING DE TEAM MAX 2
                playersConfig.get(playerID).replace("team", "red");
                System.out.println("Team : " + playersConfig.get(playerID).get("team"));
            }
        });
        panel.add(red);
        return panel;
    }

    private JPanel createPseudo(final JPanel parent, int playerID) {
        final JPanel panel = SwingFactory.createTransparentPanel(parent, 100, 20);
        JTextPane pseudo = new JTextPane();
        pseudo.setText(playersConfig.get(playersConfig.size() - 1).get("pseudo"));
        pseudo.setForeground(Color.BLACK);
        pseudo.setFont(Resources.getFont("NoitaBlackletter", 32));
        pseudo.setPreferredSize(new Dimension(200, 50));

        ((AbstractDocument) pseudo.getDocument()).setDocumentFilter(new DocumentFilter() {
        private final int maxChars = 10;

        @Override
        public void insertString(FilterBypass fb, int offset, String string,
            AttributeSet attr) throws BadLocationException {
            if ((fb.getDocument().getLength() + string.length()) <= maxChars) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length,
            String text, AttributeSet attrs) throws BadLocationException {
            if ((fb.getDocument().getLength() + text.length() - length) <= maxChars) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
        });

        StyledDocument doc = pseudo.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        panel.add(pseudo, 0);
        return panel;
    }

    private JPanel createSelectCharacter(final JPanel parent, int playerID) {
        final JPanel panel = SwingFactory.createTransparentPanel(parent, 100, 80);
        // panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setLayout(new BorderLayout());
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        ImageName skinImg =  new ImageName(1, Resources.getImage("frame-1"));

        JButton left = SwingFactory.createImageButton(parent, "left", 5, 10, false);
        left.setBorder(BorderFactory.createEmptyBorder(70, 0, 0, 0));
        panel.add(left, BorderLayout.WEST);
        JLabel character = createImageImageLabel(parent, skinImg, 20, 20, false);
        panel.add(character, BorderLayout.CENTER);
        JButton right = SwingFactory.createImageButton(parent, "right", 5, 10, false);
        right.setBorder(BorderFactory.createEmptyBorder(70, 0, 0, 0));
        panel.add(right, BorderLayout.EAST);

        selectSkin(parent, skinImg, character, 0);
        skinListerner(left, panel, skinImg, character, -1);
        skinListerner(right, panel, skinImg, character, 1);
        return panel;
    }

    private void skinListerner(JButton button, JPanel mapSelection, ImageName mapImg,
        JLabel skinLabel, int i) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectSkin(mapSelection, mapImg, skinLabel, i);
            }
        });
        handCursor(button);
    }

    private JPanel createSelectItem(final JPanel parent, int playerID) {
        final JPanel panel = SwingFactory.createTransparentPanel(parent, 100, 20);
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createHorizontalGlue());
        JButton left = SwingFactory.createImageButton(parent, "left", 4, 4, false);
        panel.add(left);
        JLabel character = SwingFactory.createImageLabel(parent, "sword", 100, 10, false);
        panel.add(character);
        JButton right = SwingFactory.createImageButton(parent, "right", 4, 4, false);
        panel.add(right);
        panel.add(Box.createHorizontalGlue());
        return panel;
    }

    private JPanel createStatus(final JPanel parent, int playerID, JTextPane pseudoPane) {
        final JPanel panel = SwingFactory.createTransparentPanel(parent, 100, 20);
        panel.setLayout(new GridLayout(1, 2));

        JButton cancel = new JButton("Annuler");
        cancel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                playersConfig.get(playerID).replace("status", "false");
                System.out.println(playersConfig.get(playerID).get("pseudo"));
            }
        });
        panel.add(cancel);

        JButton confirm = new JButton("Confirmer");
        confirm.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                String updatedPseudo = pseudoPane.getText();
                playersConfig.get(playerID).put("pseudo", updatedPseudo);
                playersConfig.get(playerID).replace("status", "true");
            }
        });
        panel.add(confirm);
        return panel;
    }

    private JPanel createAddPlayer(final JPanel parent, final int playerID, final JPanel nextPlayerPanel) {
        final JPanel panel = SwingFactory.createTransparentPanel(parent, 100, 20);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel center = SwingFactory.createTransparentPanel(parent, 100, 28);
        panel.add(center);
        JButton addPlayer = SwingFactory.createImageButton(parent, "add", 10, 10, false);
        addPlayer.setAlignmentX(Component.CENTER_ALIGNMENT);
        addPlayer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                panel.remove(addPlayer);
                panel.remove(center);

                character(panel, parent, playerID);
                if (nextPlayerPanel != null) {
                    nextPlayerPanel.setVisible(true);
                }
            }
        });
        panel.add(addPlayer);
        return panel;
    }

    private JPanel createBottomPanel(final JPanel parent) {
        final JPanel panel = SwingFactory.createTransparentPanel(parent, 100, 10);
        panel.setLayout(new BorderLayout());
        //temporaire
        final JButton back = new JButton("Retourner au configuration");
        back.setBorderPainted(false);
        back.setOpaque(false);
        back.setContentAreaFilled(false);
        back.setFocusPainted(false);
        back.setForeground(Color.BLACK);
        back.setAlignmentX(Component.LEFT_ALIGNMENT);
        back.setFont(Resources.getFont("NoitaBlackletter", 64));
        back.setHorizontalTextPosition(JButton.LEFT);
        back.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println(host);
                if (host) {
                    changeState(MenuState.MAPCONFIG);
                    changeBackgroundImg("sign");
                } else {
                    changeState(MenuState.SELECTMOD);
                    changeBackgroundGif("backgroundMoving");
                }
            }
        });
        handCursor(back);
        panel.add(back, BorderLayout.WEST);

        JButton startGame = new JButton("Lancez l'aventure !");
        startGame.setBorderPainted(false);
        startGame.setOpaque(false);
        startGame.setContentAreaFilled(false);
        startGame.setFocusPainted(false);
        startGame.setForeground(Color.BLACK);
        startGame.setAlignmentX(Component.RIGHT_ALIGNMENT);
        startGame.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 100));
        startGame.setFont(Resources.getFont("NoitaBlackletter", 64));
        startGame.setHorizontalTextPosition(JButton.LEFT);
        startGame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (int i = 0; i < playersConfig.size(); i++) {
                    if (playersConfig.get(i).get("status").equals("false")) {
                        return;
                    }
                }
                System.out.println("start game");
                for (int i = 0; i < playersConfig.size(); i++) {
                    System.out.println(playersConfig.get(i));
                }
            }
        });
        createSwitchStateButton(startGame, MenuState.CHARACTER);
        handCursor(startGame);
        panel.add(startGame, BorderLayout.EAST);
        return panel;
    }

    //#endregion

    // #region manualIcon 7
    private JPanel createManualIcon() {
        final JPanel panel = SwingFactory.createContainerPanel(pane, 93, 0, 0, 95);
        panel.add(createBook());
        panel.setVisible(true);
        return panel;
    }

    private JButton createBook() {
        final JButton button = SwingFactory.createImageButton(pane, "manual_close", 3, 5, false);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                getLayerComp(MANUALBOOK_INDEX).setVisible(true);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                final Dimension size = button.getPreferredSize();
                button.setIcon(new ImageIcon(Resources.getImage("manual_bright")
                    .getScaledInstance((int) size.getWidth(), (int) size.getHeight(), Image.SCALE_SMOOTH)));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                final Dimension size = button.getPreferredSize();
                button.setIcon(new ImageIcon(Resources.getImage("manual_close")
                    .getScaledInstance((int) size.getWidth(), (int) size.getHeight(), Image.SCALE_SMOOTH)));
            }
        });
        return button;
    }

    // #endregion

    // #region manualBook 8

    private JPanel createManualBook() {
        final JPanel panel = SwingFactory.createContainerPanel(pane, 5, 5, 5, 5);
        panel.setOpaque(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.ORANGE);
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 4));

        // HAUT, CHANGER FONTS
        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("  Manuel", SwingConstants.CENTER);
        titleLabel.setFont(Resources.getFont("NoitaBlackletter", 22));
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.setBackground(Color.ORANGE);

        JButton closeButton = SwingFactory.createImageButton(pane, "notready", 4, 4, false);
        // MouseListener ici

        titlePanel.add(closeButton, BorderLayout.EAST);
        int titleHeight = (int) (panel.getHeight() * 0.05);
        titlePanel.setPreferredSize(new Dimension(panel.getWidth(), titleHeight));
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // BAS, CHANGER FONTS
        JPanel contentPanel = new JPanel(new GridLayout(1, 2));
        contentPanel.setBackground(Color.ORANGE);

        JPanel navPanel = new JPanel(new GridLayout(3, 1));
        JButton howToPlayButton = new JButton("Prise en main du jeu");
        howToPlayButton.setBackground(Color.YELLOW);
        howToPlayButton.setFont(Resources.getFont("NoitaBlackletter", 64));
        JButton gameFlowButton = new JButton("Déroulement de la partie");
        gameFlowButton.setBackground(Color.YELLOW);
        gameFlowButton.setFont(Resources.getFont("NoitaBlackletter", 64));
        JButton mapSpecialtiesButton = new JButton("Spécialité des cartes");
        mapSpecialtiesButton.setBackground(Color.YELLOW);
        mapSpecialtiesButton.setFont(Resources.getFont("NoitaBlackletter", 64));
        navPanel.add(howToPlayButton, BorderLayout.NORTH);
        navPanel.add(gameFlowButton, BorderLayout.CENTER);
        navPanel.add(mapSpecialtiesButton, BorderLayout.SOUTH);

        int navPanelWidth = (int) (contentPanel.getWidth() * 0.3);      // Soucis pour 30%
        navPanel.setPreferredSize(new Dimension(navPanelWidth, contentPanel.getHeight()));
        contentPanel.add(navPanel, BorderLayout.WEST);

        JTextArea explanationTextArea = new JTextArea("   Bienvenue sur Board's Adventure !\n\n Si vous avez besoin d'informations sur les différents aspects de ce jeu, veuillez jeter un oeil aux rubriques d'aides correspondantes ci-contre.");
        explanationTextArea.setLineWrap(true);
        explanationTextArea.setWrapStyleWord(true);
        explanationTextArea.setEditable(false);
        explanationTextArea.setBackground(Color.CYAN);
        explanationTextArea.setFont(Resources.getFont("NoitaBlackletter", 32));
        JScrollPane scrollPane = new JScrollPane(explanationTextArea);

        closeButton.addMouseListener(new MouseAdapter() {   // Déplacement du mouseListener du bouton close afin de bien actualiser la fenêtre au début /EDIT: ENFAIT CA MARCHE PAS MDRRRRR
            @Override
            public void mouseClicked(MouseEvent e) {
                scrollPane.setViewportView(explanationTextArea);
                getLayerComp(MANUALBOOK_INDEX).setVisible(false);
            }
        });

        int scrollPaneWidth = (int) (contentPanel.getWidth() * 0.7);    // Soucis pour 70%
        scrollPane.setPreferredSize(new Dimension(scrollPaneWidth, contentPanel.getHeight()));
        scrollPane.setViewportBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        scrollPane.setBackground(Color.CYAN);
        contentPanel.add(scrollPane, BorderLayout.EAST);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        panel.add(mainPanel);

        // LISTENERS, voir adaption texte/rapport au resize
        howToPlayButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                scrollPane.setViewportView(explanationTextArea);
                explanationTextArea.setText("     - PRISE EN MAIN DU LOGICIEL -\n\n Sur le menu du jeu, vous pouvez modifier les réglages sonores en cliquant sur \"Options\" ou vous pouvez vous lancer directement dans une partie en appuyant sur \"Jouer\".\nUne fois dans ce menu, vous pouvez choisir de rejoindre une partie déjà créée ou d'en être l'hôte. Vous pourrez alors configurer la partie comme cela vous convient le mieux !");
            }
        });
        gameFlowButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                scrollPane.setViewportView(explanationTextArea);
                explanationTextArea.setText("   Comment se déroule une partie de Board's Adventure ?\n\n 1 - Début de tour: Faites constat des évènements qui prennent effet avant votre tour. \n\n 2 - Lancer de dé: Sélectionnez et lancez le dé que vous allez jouer à ce tour. Différents types de dés peuvent être obtenus au cours de la partie.\n\n 3 - Déplacement: Déplacez votre pion à la hauteur du résultat, et choisissez quelle direction emprunter si le chemin vous l'impose. \n\n 4 - Action: L'effet de la case sur laquelle vous atterrissez prend effet. Laissez la chance décider de votre sort avec les évènements, ou faites l'acquisition des propriétés du plateau afin d'affirmer votre monopole sur les autres.");
            }
        });
        mapSpecialtiesButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                explanationTextArea.setText("     - Les différentes cases sur le plateau de jeu -\n Veuillez recharger la fenêtre du manuel afin d'afficher la liste des différentes cases du jeu.");
                JPanel mapPanel = new JPanel(new GridLayout(8, 1));

                JPanel gainCase = createCaseExplanationPanel("gain", "Bonus: Vous fait gagner X or.", 10);
                gainCase.setBackground(Color.CYAN);
                mapPanel.add(gainCase);  // gain
                JPanel lossCase = createCaseExplanationPanel("loss", "Malus: Vous fait perdre X or.", 10);
                lossCase.setBackground(Color.CYAN);
                mapPanel.add(lossCase);  // loss
                JPanel dangerCase = createCaseExplanationPanel("danger", "Danger: Un événement dangereux pourrait arriver oulala...", 10);
                dangerCase.setBackground(Color.CYAN);
                mapPanel.add(dangerCase);    // danger
                JPanel intersectionCase = createCaseExplanationPanel("intersection", "Intersection: Choissisez entre deux chemins à emprunter afin de continuer votre route.", 10);
                intersectionCase.setBackground(Color.CYAN);
                mapPanel.add(intersectionCase);    // intersection
                JPanel kingdomCase = createCaseExplanationPanel("kingdom", "Kingdom: Un événement spécial, battez-vous pour prendre possession du trône et de la partie !", 10);
                kingdomCase.setBackground(Color.CYAN);
                mapPanel.add(kingdomCase);    // kingdom
                JPanel propertyCase = createCaseExplanationPanel("property", "Propriété: Achetez une propriété afin d'avoir le contrôle de la case et gagner un revenu.", 10);
                propertyCase.setBackground(Color.CYAN);
                mapPanel.add(propertyCase);    // property
                JPanel shopCase = createCaseExplanationPanel("shop", "Boutique: Vous permet d'acheter des objets ou des dés spéciaux en échange de votre or.", 10);
                shopCase.setBackground(Color.CYAN);
                mapPanel.add(shopCase);    // shop
                JPanel voidCase = createCaseExplanationPanel("void", "Vide: Une case vide, sur laquelle rien ne se passe.", 10);
                voidCase.setBackground(Color.CYAN);
                mapPanel.add(voidCase);    // void

                JViewport viewport = scrollPane.getViewport();
                viewport.setView(mapPanel);

            }
        });

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                e.consume();
                System.out.println("NAAN CLIQUE PAS ICI Y'A R");
            }
        });
        return panel;
    }

    private JPanel createCaseExplanationPanel(String img, String description, int imageSize) {
        // BufferedImage caseImage = loadImage(img); // TODO Vraiment utile ?
        // caseImage = resizeImage(caseImage, imageSize, imageSize);

        // Création du JPanel avec un GridBagLayout
        JPanel casePanel = new JPanel(new GridBagLayout());
        // Création des contraintes pour le placement des composants
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 3, 5, 3);

        // Ajout de l'image de la case
        JLabel iconLabel = new JLabel(new ImageIcon(Resources.getImage(img)));
        casePanel.add(iconLabel, gbc);
        // Changement des contraintes pour positionner le texte en dessous de l'image
        gbc.gridx++;
        gbc.anchor = GridBagConstraints.WEST;
        // Ajout du texte de description
        JLabel descriptionLabel = new JLabel(description);
        descriptionLabel.setFont(Resources.getFont("NoitaBlackletter", 32));
        casePanel.add(descriptionLabel, gbc);

        return casePanel;
    }

    // // VOIR COMMENT MODIFIER POUR AJUSTER CORRECTEMENT IMG DEFENSE
    // private static BufferedImage loadImage(String filename) {
    //     try {
    //         return ImageIO.read(new File(filename));
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //         return null;
    //     }
    // }

    // private static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
    //     BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
    //     Graphics2D graphics2D = resizedImage.createGraphics();
    //     graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
    //     graphics2D.dispose();
    //     return resizedImage;
    // }

    // #region selector

    private void selectMap(JPanel parent, ImageName mapImg, JLabel map, JTextArea desc, int i) {
        int nb = mapImg.getImageSelection();
        nb += i;
        if (nb >= nbMaps) {
            nb = 1;
        } else if (nb <= 0) {
            nb = 3;
        }
        switch (nb) {
            case 1 :
                titleMap.setText("Royaume de Diviteria");
                mapImg.setImage(Resources.getImage("preview1"));
                mapImg.setImageSelection(1);
                desc.setText("Votre aventure a pour but d'accumuler autant de couronnes que possible "
                    + "dans un nombre de tours restreint, aspirant ainsi au titre de souverain du royaume.\n"
                    + "Vous endosserez le rôle d'un intrépide aventurier animé par l'ambition de régner en "
                    + "maître absolu sur tous les domaines et autres joueurs.");
                break;
            case 2 :
                titleMap.setText("Station Andromeda");
                mapImg.setImage(Resources.getImage("preview2"));
                mapImg.setImageSelection(2);
                desc.setText("Cette fois aventuriers astronautes, embarquez dans Andromeda et explorez les "
                    + "moindres recoins de la station spatiale. Emparez-vous du maximum de ressources oubliées "
                    + "en ces lieux et servez-vous en pour vous réclamer votre monopole sur la station et "
                    + "débuter votre domination de l'espace.");
                break;
            case 3 :
                titleMap.setText("Villa Nova");
                mapImg.setImage(Resources.getImage("preview3"));
                mapImg.setImageSelection(3);
                desc.setText("Villa Nova et ses nombreuses propriétés n'attendent que votre vous pour les "
                    + "saisir et les collectionner. Dans votre aventure, vous pourrez récolter de précieuses "
                    + "sommes d'argent qui vous servirons à acquérir vos biens. Attention toutefois à ne pas "
                    + "perdre ce précieux argent ou à ne pas vous retrouver derrière les barreaux.");
                break;
            default:
                break;
        }

        final Dimension size = SwingFactory.calcDimension(mapImg.getImage(), parent, 60, 40, false);
                map.setPreferredSize(new Dimension((int) size.getWidth() * 40 / 100,
                    (int) (size.getHeight() * 60 / 100)));
                map.setIcon(new ImageIcon(mapImg.getImage().getScaledInstance(
                    (int) size.getWidth(), (int) size.getHeight(), Image.SCALE_SMOOTH)));

        map.repaint();
        map.revalidate();
    }

    private void selectSkin(JPanel parent, ImageName skinImg, JLabel skin, int i) {
        int nb = skinImg.getImageSelection();
        nb += i;
        if (nb >= 6) {
            nb = 1;
        } else if (nb <= 0) {
            nb = 5;
        }

        final Dimension size = SwingFactory.calcDimension(skinImg.getImage(), parent, 60, 40, false);
        // skin.setPreferredSize(new Dimension((int) size.getWidth() * 40 / 100, (int) (size.getHeight() * 60 / 100)));

        switch (nb) {
            case 1 :
            skin.setIcon(new ImageIcon(Resources.getImage("frame-1").getScaledInstance(
                (int) size.getWidth(), (int) size.getHeight(), Image.SCALE_SMOOTH)));
                break;
            case 2 :
            skin.setIcon(new ImageIcon(Resources.getImage("frame-2").getScaledInstance(
                (int) size.getWidth(), (int) size.getHeight(), Image.SCALE_SMOOTH)));
                break;
            case 3 :
            skin.setIcon(new ImageIcon(Resources.getImage("frame-3").getScaledInstance(
                (int) size.getWidth(), (int) size.getHeight(), Image.SCALE_SMOOTH)));
                break;
            case 4 :
            skin.setIcon(new ImageIcon(Resources.getImage("frame-4").getScaledInstance(
                (int) size.getWidth(), (int) size.getHeight(), Image.SCALE_SMOOTH)));
                break;
            case 5 :
                skin.setIcon(new ImageIcon(Resources.getImage("frame-5").getScaledInstance(
            (int) size.getWidth(), (int) size.getHeight(), Image.SCALE_SMOOTH)));
                break;
            default:
                break;
        }

        skin.repaint();
        skin.revalidate();
    }

        private void selectItem(JPanel parent, ImageName itemImg, JLabel item, int i) {
            int nb = itemImg.getImageSelection();
            nb += i;
            if (nb >= 6) {
                nb = 1;
            } else if (nb <= 0) {
                nb = 5;
            }

            switch (nb) {
                case 1 :
                    itemImg.setImage(Resources.getImage("frame-1"));
                    itemImg.setImageSelection(1);
                    break;
                case 2 :
                    itemImg.setImage(Resources.getImage("frame-2"));
                    itemImg.setImageSelection(2);
                    break;
                case 3 :
                    itemImg.setImage(Resources.getImage("frame-3"));
                    itemImg.setImageSelection(3);
                    break;
                case 4 :
                    itemImg.setImage(Resources.getImage("frame-4"));
                    itemImg.setImageSelection(4);
                    break;
                case 5 :
                    itemImg.setImage(Resources.getImage("frame-5"));
                    itemImg.setImageSelection(5);
                    break;
                default:
                    break;
            }

        final Dimension size = SwingFactory.calcDimension(itemImg.getImage(), parent, 40, 40, false);
        item.setPreferredSize(new Dimension((int) size.getWidth() * 40 / 100,
            (int) (size.getHeight() * 40 / 100)));
            item.setIcon(new ImageIcon(itemImg.getImage().getScaledInstance(
            (int) size.getWidth(), (int) size.getHeight(), Image.SCALE_SMOOTH)));
        item.repaint();
        item.revalidate();
    }

    public JLabel createImageImageLabel(final Component parent, final ImageName imgName,
        final int widthPer, final int heightPer, final boolean resize) {
        final Dimension size = SwingFactory.calcDimension(imgName.getImage(), parent, widthPer, heightPer, resize);
        final JLabel label = new JLabel(new ImageIcon(imgName.getImage().getScaledInstance(
            (int) size.getWidth(), (int) size.getHeight(), Image.SCALE_SMOOTH)));
        label.setPreferredSize(new Dimension((int) size.getWidth() * widthPer / 100,
            (int) (size.getWidth() * heightPer / 100)));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setOpaque(false);

        parent.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                final Dimension size = SwingFactory.calcDimension(imgName.getImage(), parent, widthPer,
                    heightPer, false);
                label.setPreferredSize(new Dimension((int) size.getWidth() * widthPer / 100,
                    (int) (size.getWidth() * heightPer / 100)));
                label.setIcon(new ImageIcon(imgName.getImage().getScaledInstance(
                    (int) size.getWidth(), (int) size.getHeight(), Image.SCALE_SMOOTH)));
            }
        });

        return label;
    }

    public class ImageName {
        private int imageSelection;
        private Image image;

        public ImageName(int imageSelection, Image image) {
            this.imageSelection = imageSelection;
            this.image = image;
        }

        public final int getImageSelection() {
            return imageSelection;
        }

        public final Image getImage() {
            return image;
        }

        public final void setImage(Image image) {
            this.image = image;
        }

        public final void setImageSelection(int imageSelection) {
            this.imageSelection = imageSelection;
        }
    }

    // #endregion

}
