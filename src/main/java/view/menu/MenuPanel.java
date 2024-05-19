package view.menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GridLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JEditorPane;
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
    private HashMap<String, String> gameConfig;
    private ArrayList<HashMap<String, String>> playersConfig;

    private int soundSliderVolume;
    private int songSliderVolume;

    private String map;
    private JPanel rightSidePanel;

    private int height;
    private int width;

    private boolean host = false;
    private boolean resizing = false;
    private int nbPlayers = 0;
    private int select = 1;
    private int nbMaps = 4;
    private int object = 1;
    private String code = " ";

    private InetAddress ip;
    private int port = 55000;

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
        gameConfig = new HashMap<>();
        gameConfig.put("turns", "45");
        playersConfig = new ArrayList<>();
        Audio.playMusic("MenuTheme.wav");
        initDefaultValues();
        createPanels();
        changeState(MenuState.MAINMENU);

        addResizeListener();

        setVisible(true);
    }

    @Override
    public void addResizeListener() {
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
        songSliderVolume = 100;
        soundSliderVolume = 100;
        map = "map4";
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
        newStatePanel.setBounds(0, 0, getWidth(), getHeight());
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

    // #region background 0

    private JPanel createBackground() {
        background = SwingFactory.createGifPanel(frame.getContentPane(), "backgroundMoving", 100, 100);
        background.setBounds(0, 0, frame.getContentPane().getWidth(), frame.getContentPane().getHeight());
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
        final JPanel panel = SwingFactory.createContainerPanel(frame.getContentPane(), 0, 0, 0, 5);

        panel.add(SwingFactory.createTransparentPanel(panel, 45, 45));

        final JButton play = SwingFactory.createTextButton(panel, "Commencer une aventure", 20, 10, 64, true);
        createSwitchStateButton(play, MenuState.SELECTMOD);
        panel.add(play);
        final JButton options = SwingFactory.createTextButton(panel, "Préparer l'expédition", 20, 10, 64, true);
        createSwitchStateButton(options, MenuState.SETTINGS);
        panel.add(options);
        final JButton leaveGame = SwingFactory.createTextButton(panel, "Retourner au foyer", 20, 10, 64, true);
        leaveGame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.dispose();
                System.exit(0);
            }
        });
        panel.add(leaveGame);

        return panel;
    }

    // #endregion

    // #region settings 2

    private JPanel createSettingsMenu() {
        final JPanel panel = SwingFactory.createContainerPanel(frame.getContentPane(), 45, 0, 0, 6);

        final int widthMargin = frame.getWidth() / 20;
        final int heightMargin = frame.getHeight() / 20;
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
        final JButton back = SwingFactory.createTextButton(panel, "Rebrousser chemin", 20, 10, 64, true);
        createSwitchStateButton(back, MenuState.MAINMENU);
        panel.add(back);
        return panel;
    }
    // #endregion

    // #region createjoin 3

    private JPanel createCreateJoinMenu() {
        final JPanel panel = SwingFactory.createContainerPanel(frame.getContentPane(), 0, 0, 0, 5);

        panel.add(SwingFactory.createTransparentPanel(panel, 45, 45));

        final JButton create = SwingFactory.createTextButton(panel, "Créer une nouvelle aventure", 15, 8, 64, true);
        panel.add(create);

        final JButton join = SwingFactory.createTextButton(panel, "Rejoindre une escouade", 20, 10, 64, true);
        panel.add(join);

        final JButton back = SwingFactory.createTextButton(panel, "Rebrousser chemin", 20, 10, 64, true);
        createSwitchStateButton(back, MenuState.MAINMENU);
        panel.add(back);

        create.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                animation(create, panel, MenuState.MAPCONFIG);
            }
        });

        join.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                animation(join, panel, MenuState.JOINGAME);
            }
        });
        return panel;
    }

    private void animation(JButton button, JPanel panel, MenuState state) {
        if (!animation) {
            animation = true;
            changeBackgroundGif("good");

            Timer timer = new Timer(2900, f -> {
                changeBackgroundImg("blackscreen");
            });
            timer.start();
            timer.setRepeats(false);

            Timer tmp = new Timer(3200, f -> {
                changeBackgroundImg("sign");
                changeState(state);
                animation = false;
            });
            tmp.start();
            tmp.setRepeats(false);
        }
    }

    // #endregion

     // #region joincode 4

     private JPanel createJoinCodeMenu() {
        final JPanel panel = SwingFactory.createContainerPanel(frame.getContentPane(), 0, 0, 0, 0);
        return panel;
    }

    // #endregion

    // #region animation

    private void changeBackgroundGif(String gif) {
        removeComp(BACKGROUND_INDEX);

        JPanel animate = SwingFactory.createGifPanel(frame.getContentPane(), gif, 100, 100);
        animate.setBounds(0, 0, frame.getContentPane().getWidth(), frame.getContentPane().getHeight());
        background.add(animate);
        addComp(background, BACKGROUND_INDEX);
    }

    private void changeBackgroundImg(String img) {
        removeComp(BACKGROUND_INDEX);

        JPanel animate = SwingFactory.createImagePanel(frame.getContentPane(), img, 100, 100);
        animate.setBounds(0, 0, frame.getContentPane().getWidth(), frame.getContentPane().getHeight());
        background.add(animate);
        addComp(background, BACKGROUND_INDEX);
    }


    // #endregion

    // #region gamelobby 5

    private JPanel createGameLobbyMenu() {
        final JPanel panel = SwingFactory.createContainerPanel(frame.getContentPane(), 2, 0, 0, 0);
        panel.add(createLobbyCode(panel));
        panel.add(createGameLobby(panel));
        panel.add(createBackButton(panel));
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
        final JPanel panel = SwingFactory.createImagePanel(parent, "sign", 100, 85); // SOURCE IMG
        panel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        panel.setSize(panel.getPreferredSize());
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        panel.add(SwingFactory.createTransparentPanel(parent, 2, 0));
        panel.add(createMapSection(panel));
        panel.add(createGameSettings(panel));
        return panel;
    }

    private JPanel createMapSection(final JPanel parent) {
        final JPanel panel = SwingFactory.createImagePanel(parent, "paper", 50, 90); // SOURCE IMG
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        panel.add(createMapSelection(parent));
        panel.add(createMapDescription(parent));
        return panel;
    }

    private JPanel createMapSelection(final JPanel parent) {
        final JPanel panel = SwingFactory.createTransparentPanel(parent, 100, 50);
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // TODO selection de map
        JPanel center = SwingFactory.createTransparentPanel(parent, 1, 50);
        panel.add(center);
        JButton left = SwingFactory.createImageButton(parent, "left", 5, 10, false);
        panel.add(left);
        JLabel mapLabel = SwingFactory.createImageLabel(parent, "test", 90, 60, false);
        panel.add(mapLabel);
        JButton right = SwingFactory.createImageButton(parent, "right", 5, 10, false);
        panel.add(right);
        return panel;
    }

    private JPanel createMapDescription(final JPanel parent) {
        final JPanel panel = SwingFactory.createTransparentPanel(parent, 100, 50);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setLayout(new BorderLayout());
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextPane desc = SwingFactory.createTextPane();
        desc.setOpaque(false);
        desc.setEditable(true);
        desc.setForeground(Color.BLACK);
        desc.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
        desc.setFont(Resources.getFont("NoitaBlackletter", 32));
        desc.setText("Bonjour, binevenueafiajgag egfaenigopjnpgqz .G\n Aogjpjzqpogjqg .gfeapigja ");
        Font font;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT,
            new File("src/main/resources/fonts/NoitaBlackletter.ttf")).deriveFont(40f);
            desc.setFont(font);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JScrollPane tmp = new JScrollPane(desc, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        panel.add(tmp);

        return panel;
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
        final JPanel panel = SwingFactory.createImagePanel(parent, "paper", 40, 25); // SOURCE IMG?
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(Box.createVerticalGlue());

        JLabel title = SwingFactory.createTextLabel("Royaume Médieval", "NoitaBlackletter", Color.BLACK, defaultFont);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private JPanel createMapConfig(final Component parent) {
        final JPanel panel = SwingFactory.createImagePanel(parent, "paper", 40, 65);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel turnsPanel = SwingFactory.createTransparentPanel(parent, 40, 10);
        turnsPanel.setLayout(new BoxLayout(turnsPanel, BoxLayout.X_AXIS));
        turnsPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 0, 0));
        JLabel turns = SwingFactory.createTextLabel("Nombre de tours :",
        "NoitaBlackletter", Color.BLACK, defaultFont / 2);

        ButtonGroup group = new ButtonGroup();
        JRadioButton turnsBox1 = createRadioButton("25", group, true);
        JRadioButton turnsBox2 = createRadioButton("35", group, false);
        JRadioButton turnsBox3 = createRadioButton("45", group, false);
        turnsPanel.add(turns);
        turnsPanel.add(turnsBox1);
        turnsPanel.add(turnsBox2);
        turnsPanel.add(turnsBox3);

        panel.add(turnsPanel);
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

    public JPanel createBackButton(final JPanel parent) {
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
                removeComp(BACKGROUND_INDEX);
                addComp(createBackground(), BACKGROUND_INDEX);
            }
        });
        createSwitchStateButton(back, MenuState.SELECTMOD);
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
                changeBackgroundImg("village");
            }
        });
        createSwitchStateButton(selectCharacter, MenuState.CHARACTER);
        panel.add(selectCharacter, BorderLayout.EAST);
        return panel;
    }

    // #endregion

    //#region character 6

    private JPanel createSelectionCharacterMenu() {
        final JPanel panel = SwingFactory.createContainerPanel(frame.getContentPane(), 18, 0, 0, 0);
        panel.add(createConfigCharacter(panel));
        panel.add(SwingFactory.createTransparentPanel(panel, 100, 70));
        panel.add(createImageCharacter(panel));
        panel.add(SwingFactory.createTransparentPanel(panel, 100, 30));
        return panel;
    }

    private JPanel createConfigCharacter(final JPanel parent) {
        final JPanel panel = SwingFactory.createTransparentPanel(parent, 100, 80);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel tmp = SwingFactory.createTransparentPanel(parent, 200, 30);
        panel.setSize(new Dimension(200, 50));
        JTextArea pseudo = new JTextArea("Joueur " + (playersConfig.size() + 1));
        pseudo.setForeground(Color.BLACK);
        pseudo.setAlignmentX(JTextArea.CENTER_ALIGNMENT);
        pseudo.setAlignmentY(JTextArea.CENTER_ALIGNMENT);
        pseudo.setFont(Resources.getFont("NoitaBlackletter", 32));
        pseudo.setPreferredSize(new Dimension(200, 50));
        tmp.add(pseudo);
        panel.add(tmp);

        JPanel skin = selectCharacter(parent);
        panel.add(skin);

        panel.add(SwingFactory.createTransparentPanel(parent, 100, 10));

        JPanel item = selectItem(parent);
        panel.add(item);
        return panel;
    }

    public JPanel selectCharacter(final JPanel parent) {
        final JPanel panel = SwingFactory.createTransparentPanel(parent, 100, 20);
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        JPanel center = SwingFactory.createTransparentPanel(parent, 35, 0);
        panel.add(center);

        JButton left = SwingFactory.createImageButton(parent, "left", 5, 10, false);
        left.setBorder(BorderFactory.createEmptyBorder(70, 0, 0, 0));
        panel.add(left);
        JLabel character = SwingFactory.createImageLabel(parent, "frame-1", 28, 28, false);
        panel.add(character);
        JButton right = SwingFactory.createImageButton(parent, "right", 5, 10, false);
        right.setBorder(BorderFactory.createEmptyBorder(70, 0, 0, 0));
        panel.add(right);
        return panel;
    }

    public JPanel selectItem(final JPanel parent) {
        final JPanel panel = SwingFactory.createTransparentPanel(parent, 100, 25);
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        JPanel center = SwingFactory.createTransparentPanel(parent, 40, 0);
        panel.add(center);

        JButton left = SwingFactory.createImageButton(parent, "left", 5, 5, false);
        panel.add(left);
        JLabel character = SwingFactory.createImageLabel(parent, "sword", 20, 10, false);
        panel.add(character);
        JButton right = SwingFactory.createImageButton(parent, "right", 5, 5, false);
        panel.add(right);
        return panel;
    }

    public JPanel createImageCharacter(final JPanel parent) {
        final JPanel panel = SwingFactory.createTransparentPanel(parent, 100, 100);
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        JLabel character = SwingFactory.createImageLabel(parent, "frame-2", 20, 17, false);
        panel.add(character);
        return panel;
    }

    //#endregion

    // #region manualIcon 7
    private JPanel createManualIcon() {
        final JPanel panel = SwingFactory.createContainerPanel(frame.getContentPane(), 93, 0, 0, 95);
        panel.add(createBook());
        panel.setVisible(true);
        return panel;
    }

    private JButton createBook() {
        final JButton button = SwingFactory.createImageButton(frame.getContentPane(), "manual_close", 3, 5, false);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                Audio.playSound("button.wav");
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
        final JPanel panel = SwingFactory.createContainerPanel(frame.getContentPane(), 5, 5, 5, 5);
        panel.setOpaque(false);
        panel.setBackground(new Color(205, 173, 142));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(205, 173, 142));
        mainPanel.setBorder(BorderFactory.createLineBorder(new Color(205, 173, 142), 4));

        // HAUT
        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("  Manuel", SwingConstants.CENTER);
        titleLabel.setFont(Resources.getFont("NoitaBlackletter", 32));
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.setBackground(new Color(205, 173, 142));

        JButton closeButton = SwingFactory.createImageButton(frame.getContentPane(), "notready", 5, 5, false);

        titlePanel.add(closeButton, BorderLayout.EAST);
        int titleHeight = (int) (panel.getHeight() * 0.05);
        titlePanel.setPreferredSize(new Dimension(panel.getWidth(), titleHeight));
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // BAS
        JPanel contentPanel = new JPanel(new GridLayout(1, 2));
        contentPanel.setBackground(new Color(230, 205, 175));

        JPanel navPanel = new JPanel(new GridLayout(3, 1));
        JButton howToPlayButton = new JButton("Prise en main du jeu");
        howToPlayButton.setBackground(new Color(230, 205, 175));
        howToPlayButton.setFont(Resources.getFont("NoitaBlackletter", 28));
        JButton gameFlowButton = new JButton("Déroulement de la partie");
        gameFlowButton.setBackground(new Color(230, 205, 175));
        gameFlowButton.setFont(Resources.getFont("NoitaBlackletter", 28));
        JButton mapSpecialtiesButton = new JButton("Spécialité des cartes");
        mapSpecialtiesButton.setBackground(new Color(230, 205, 175));
        mapSpecialtiesButton.setFont(Resources.getFont("NoitaBlackletter", 28));
        navPanel.add(howToPlayButton, BorderLayout.NORTH);
        navPanel.add(gameFlowButton, BorderLayout.CENTER);
        navPanel.add(mapSpecialtiesButton, BorderLayout.SOUTH);

        int navPanelWidth = (int) (contentPanel.getWidth() * 0.3);
        navPanel.setPreferredSize(new Dimension(navPanelWidth, contentPanel.getHeight()));
        contentPanel.add(navPanel, BorderLayout.WEST);

        JTextArea explanationTextArea = new JTextArea("   Bienvenue sur Board's Adventure !\n\n Si vous avez besoin d'informations sur les différents aspects de ce jeu, veuillez jeter un oeil aux rubriques d'aides correspondantes ci-contre.");
        explanationTextArea.setLineWrap(true);
        explanationTextArea.setWrapStyleWord(true);
        explanationTextArea.setEditable(false);
        explanationTextArea.setBackground(new Color(255, 240, 200));
        explanationTextArea.setFont(Resources.getFont("NoitaBlackletter", 20));
        JScrollPane scrollPane = new JScrollPane(explanationTextArea);

        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                scrollPane.setViewportView(explanationTextArea);
                getLayerComp(MANUALBOOK_INDEX).setVisible(false);
            }
        });

        int scrollPaneWidth = (int) (contentPanel.getWidth() * 0.7);
        scrollPane.setPreferredSize(new Dimension(scrollPaneWidth, contentPanel.getHeight()));
        scrollPane.setViewportBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        scrollPane.setBackground(new Color(230, 205, 175));
        contentPanel.add(scrollPane, BorderLayout.EAST);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        panel.add(mainPanel);

        // LISTENERS
        howToPlayButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                scrollPane.setViewportView(explanationTextArea);
                explanationTextArea.setText(" - PRISE EN MAIN DU LOGICIEL -\n\n Sur le menu du jeu, vous pouvez modifier les réglages sonores en cliquant sur \"Préparer l'expédition\" ou vous lancer directement dans une partie en appuyant sur \"Commencer une aventure\".\nUne fois dans ce menu, vous pouvez choisir de rejoindre une partie déjà créée en cliquant sur \"Rejoindre une escouade\" ou en être l'hôte en cliquant sur \"Créer une nouvelle aventure\". Vous pourrez alors configurer la partie comme cela vous convient le mieux !");
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
                explanationTextArea.setText(" - LES DIFFÉRENTES CASES DU JEU - \n\n Veuillez recharger la fenêtre du manuel afin d'afficher la liste des différentes cases du jeu.");
                JPanel mapPanel = new JPanel(new GridLayout(9, 1));
                JPanel gainCase = createCaseExplanationPanel(
                    "gain", "Bonus: Vous fait gagner une certaine quantité d'or.", 10);
                gainCase.setBackground(new Color(255, 240, 200));
                mapPanel.add(gainCase);  // gain
                JPanel lossCase = createCaseExplanationPanel(
                    "loss", "Malus: Vous fait perdre une certaine quantité d'or.", 10);
                lossCase.setBackground(new Color(255, 240, 200));
                mapPanel.add(lossCase);  // loss
                JPanel dangerCase =
                createCaseExplanationPanel("danger", "Danger: Un événement dangereux pourrait arriver oulala...", 10);
                dangerCase.setBackground(new Color(255, 240, 200));
                mapPanel.add(dangerCase);    // danger
                JPanel intersectionCase =
                createCaseExplanationPanel("intersection",
                "Intersection: Choissisez quel chemin emprunter afin de continuer votre route.", 10);
                intersectionCase.setBackground(new Color(255, 240, 200));
                mapPanel.add(intersectionCase);    // intersection
                JPanel kingdomCase =
                createCaseExplanationPanel("kingdom",
                "Kingdom: Un événement spécial, battez-vous pour prendre possession du trône et de la partie !", 10);
                kingdomCase.setBackground(new Color(255, 240, 200));
                mapPanel.add(kingdomCase);    // kingdom
                JPanel propertyCase = createCaseExplanationPanel("property",
                "Propriété: Achetez une propriété afin de réclamer le contrôle de la case et gagner un revenu.", 10);
                propertyCase.setBackground(new Color(255, 240, 200));
                mapPanel.add(propertyCase);    // property
                JPanel shopCase = createCaseExplanationPanel("shop",
                "Boutique: Vous permet d'acheter diverses objets qui vous avantageront en échange d'or.", 10);
                shopCase.setBackground(new Color(255, 240, 200));
                mapPanel.add(shopCase);    // shop
                JPanel voidCase =
                createCaseExplanationPanel("void",
                "Vide: Une case vide, sur laquelle rien ne se passe mis à part un repos bien mérité.", 10);
                voidCase.setBackground(new Color(255, 240, 200));
                mapPanel.add(voidCase);    // void
                JPanel cardCase = createCaseExplanationPanel("cardResized",
                "Carte: Faites parler votre chance et tirez une carte, ce qui pourrait vous avantager (ou non) au cours de votre périple.", 10);
                cardCase.setBackground(new Color(255, 240, 200));
                mapPanel.add(cardCase);     // card
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
            }
        });
        return panel;
    }

    private JPanel createCaseExplanationPanel(String img, String description, int imageSize) {
        JPanel casePanel = new JPanel(new GridBagLayout());
        // Contraintes de placement
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 3, 5, 3);

        JLabel iconLabel = new JLabel(new ImageIcon(Resources.getImage(img)));
        casePanel.add(iconLabel, gbc);
        // Positionnement texte en dessous de l'image
        gbc.gridx++;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel descriptionLabel = new JLabel(description);
        descriptionLabel.setFont(Resources.getFont("NoitaBlackletter", 18));
        casePanel.add(descriptionLabel, gbc);

        return casePanel;
    }

}
