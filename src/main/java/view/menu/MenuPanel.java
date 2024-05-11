package view.menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.Image;
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
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;

import controller.resources.Audio;
import controller.resources.Resources;
import game.Game;
import game.Mode;
import view.CorePanel;
import view.SwingFactory;
import view.View;

/**
 * Classe MenuPanel qui s'occupe de toute la vue des menus du jeu.
 * Elle contient : le menu principal, la sélection de niveau, les paramètres, le manuel de jeu.
 * Chaque menu est un panel et tout en bas se trouve un panel pour le background.
 */
public final class MenuPanel extends CorePanel {

    private static final int BACKGROUND_INDEX = 0;
    private static final int ANIMATION_BACKGROUND = 1;
    private static final int MANUALICON_INDEX = 6;
    private static final int MANUALBOOK_INDEX = 7;

    private MenuState menuState;

    private final HashMap<String, Integer> gameConfig;
    private final ArrayList<HashMap<String, String>> playersConfig;

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
        super(frame);
        this.height = frame.getHeight();
        this.width = frame.getWidth();
        gameConfig = new HashMap<>();
        gameConfig.put("turns", 45);
        playersConfig = new ArrayList<>();
        Audio.playMusic("MenuTheme.wav");
        initDefaultValues();
        createPanels();
        changeState(MenuState.MAINMENU);
        setVisible(true);
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
        addPanel(createBackground(), BACKGROUND_INDEX);
        addPanel(createMainMenu(), MenuState.MAINMENU.getLayerIndex());
        addPanel(createSettingsMenu(), MenuState.SETTINGSMENU.getLayerIndex());
        addPanel(createCreateJoinMenu(), MenuState.CREATEJOIN.getLayerIndex());
        addPanel(createJoinCodeMenu(), MenuState.JOINCODE.getLayerIndex());
        addPanel(createGameLobbyMenu(), MenuState.GAMELOBBY.getLayerIndex());
        addPanel(createManualIcon(), MANUALICON_INDEX);
        addPanel(createManualBook(), MANUALBOOK_INDEX);
    }

    public void changeState(final MenuState newMenuState) {
        if (menuState != null) {
            // getPanels().values().forEach(panel -> panel.setVisible(false));
            getPanels().get(menuState.getLayerIndex()).setVisible(false);
            System.out.println("menuState : " + menuState);
            System.out.println("newMenuState : " + newMenuState);
        }
        this.menuState = newMenuState;
        final JPanel newStatePanel = getPanels().get(menuState.getLayerIndex());
        newStatePanel.setBounds(0, 0, getWidth(), getHeight());
        newStatePanel.setVisible(true);
        revalidate();
    }

    private void createSwitchStateButton(final JButton button, final MenuState newMenuState) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                Audio.playSound("button.wav");
                changeState(newMenuState);
            }
        });
    }

    // #region background 0

    private JPanel createBackground() {
        background = SwingFactory.createGifPanel(this, "backgroundMoving", 100, 100);
        background.setBounds(0, 0, getFrame().getWidth(), getFrame().getHeight());
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
        final JPanel panel = SwingFactory.createContainerPanel(this, 0, 0, 0, 5);

        panel.add(SwingFactory.createTransparentPanel(panel, 45, 45));

        final JButton play = SwingFactory.createTextButton(panel, "Commencer une aventure", 20, 10, 64, true);
        createSwitchStateButton(play, MenuState.CREATEJOIN);
        panel.add(play);
        final JButton options = SwingFactory.createTextButton(panel, "Préparer l'expédition", 20, 10, 64, true);
        createSwitchStateButton(options, MenuState.SETTINGSMENU);
        panel.add(options);
        final JButton leaveGame = SwingFactory.createTextButton(panel, "Retourner au foyer", 20, 10, 64, true);
        leaveGame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                getFrame().dispose();
                System.exit(0);
            }
        });
        panel.add(leaveGame);

        return panel;
    }

    // #endregion

    // #region settings 2

    private JPanel createSettingsMenu() {
        final JPanel panel = SwingFactory.createContainerPanel(this, 45, 0, 0, 6);

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
        final JPanel panel = SwingFactory.createContainerPanel(this, 0, 0, 0, 5);

        panel.add(SwingFactory.createTransparentPanel(panel, 45, 45));

        final JButton create = SwingFactory.createTextButton(panel, "Créer une nouvelle aventure", 15, 8, 64, true);
        panel.add(create);
        create.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                animation(create, panel, MenuState.GAMELOBBY);
                createSwitchStateButton(create, MenuState.MAINMENU);
            }
        });

        final JButton join = SwingFactory.createTextButton(panel, "Rejoindre une escouade", 20, 10, 64, true);
        panel.add(join);
        join.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                animation(join, panel, MenuState.JOINCODE);
                createSwitchStateButton(join, MenuState.MAINMENU);
            }
        });

        final JButton back = SwingFactory.createTextButton(panel, "Rebrousser chemin", 20, 10, 64,true);
        createSwitchStateButton(back, MenuState.MAINMENU);
        panel.add(back);
        return panel;
    }

    private void animation(JButton button, JPanel panel, MenuState state) {
        if (!animation) {
            animation = true;
            changeBackgroundGif("good");

            Timer timer = new Timer(2900, f -> {
                createSwitchStateButton(button, state);
                changeBackgroundImg("blackscreen");
            });
            timer.start();
            timer.setRepeats(false);

            Timer tmp = new Timer(3200, f -> {
                animation = false;
                changeBackgroundImg("sign");
                changeState(state);
            });
            tmp.start();
            tmp.setRepeats(false);
        }
    }

    // #endregion

    // #region animation

    private void changeBackgroundGif(String gif) {
        removePanel(BACKGROUND_INDEX);

        JPanel animate = SwingFactory.createGifPanel(this, gif, 100, 100);
        animate.setBounds(0, 0, getFrame().getWidth(), getFrame().getHeight());
        background.add(animate);
        addPanel(background, BACKGROUND_INDEX);
    }

    private void changeBackgroundImg(String img) {
        removePanel(BACKGROUND_INDEX);

        JPanel animate = SwingFactory.createImagePanel(this, img, 100, 100);
        animate.setBounds(0, 0, getFrame().getWidth(), getFrame().getHeight());
        background.add(animate);
        addPanel(background, BACKGROUND_INDEX);
    }


    // #endregion

    // #region gamelobby 4

    private JPanel createGameLobbyMenu() {
        final JPanel panel = SwingFactory.createContainerPanel(this, 2, 0, 0, 0);
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
        final JPanel panel = SwingFactory.createImagePanel(parent, "sign", 100, 85);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        panel.setSize(panel.getPreferredSize());
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        panel.add(SwingFactory.createTransparentPanel(parent, 2, 0));
        panel.add(createMapSection(panel));
        //transparant?
        panel.add(createGameSettings(panel));
        return panel;
    }

    private JPanel createMapSection(final JPanel parent) {
        final JPanel panel = SwingFactory.createImagePanel(parent, "paper", 50, 90);
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
            font = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/NoitaBlackletter.ttf")).deriveFont(40f);
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
        panel.add(createMapDescription(parent));

        return panel;
    }

    private JPanel createTitleMap(final Component parent) {
        final JPanel panel = SwingFactory.createImagePanel(parent, "paper", 40, 25);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(Box.createVerticalGlue());

        JLabel title = SwingFactory.createTextLabel("Royaume Médieval", "NoitaBlackletter", Color.BLACK, defaultFont);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private JPanel createMapDescription(final Component parent) {
        final JPanel panel = SwingFactory.createImagePanel(parent, "paper", 40, 65);
        return panel;
    }

    public JPanel createBackButton(final JPanel parent) {
        final JPanel panel = SwingFactory.createTransparentPanel(parent, 100, 10);
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 0));
        //temporaire
        final JButton back = new JButton("Retourner à la base");
        back.setBorderPainted(false);
        back.setOpaque(false);
        back.setContentAreaFilled(false);
        back.setFocusPainted(false);
        back.setForeground(Color.BLACK);
        back.setFont(Resources.getFont("NoitaBlackletter", 64));
        back.setHorizontalTextPosition(JButton.LEFT);
        back.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                removePanel(BACKGROUND_INDEX);
                addPanel(createBackground(), BACKGROUND_INDEX);
                revalidate();
                repaint();
            }
        });
        createSwitchStateButton(back, MenuState.CREATEJOIN);
        panel.add(back);
        return panel;
    }

    // #endregion

    // #region joincode 5

    private JPanel createJoinCodeMenu() {
        final JPanel panel = SwingFactory.createContainerPanel(this, 0, 0, 0, 0);
        return panel;
    }

    // #endregion

    // #region manualIcon 6
    private JPanel createManualIcon() {
        final JPanel panel = SwingFactory.createContainerPanel(this, 93, 0, 0, 95);
        panel.add(createBook());
        panel.setVisible(true);
        return panel;
    }

    private JButton createBook() {
        final JButton button = SwingFactory.createImageButton(this, "manual_close", 3, 5, false);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                Audio.playSound("button.wav");
                System.out.println("sur manuel là");
                getPanels().get(MANUALBOOK_INDEX).setVisible(true);
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

    // #region manualBook 7

    private JPanel createManualBook() {
        final JPanel panel = SwingFactory.createContainerPanel(this, 0, 0, 0, 0);
        panel.setOpaque(true);
        panel.setBackground(Color.ORANGE);

        JTextArea explanationTextArea = new JTextArea("   Bienvenue sur Board's Adventure !\n\n Si vous avez besoin d'informations sur les différents aspects de ce jeu, veuillez jeter un oeil aux rubriques d'aides correspondantes ci-contre.");
        explanationTextArea.setLineWrap(true);
        explanationTextArea.setWrapStyleWord(true);
        explanationTextArea.setEditable(false);
        explanationTextArea.setBackground(Color.CYAN);
        //explanationTextArea.setFont(Resources.loadFonts(14f, "RifficFree.ttf")); // CHANGER LA MANIERE POUR FONTS ?

        JScrollPane scrollPane = new JScrollPane(explanationTextArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel navPanel = new JPanel(new GridLayout(3, 1));
        JButton howToPlayButton = new JButton("Prise en main du jeu");
        howToPlayButton.setBackground(Color.YELLOW);
        //howToPlayButton.setFont(Resources.loadFonts(16f, "RifficFree.ttf"));
        JButton gameFlowButton = new JButton("Déroulement de la partie");
        gameFlowButton.setBackground(Color.YELLOW);
        //gameFlowButton.setFont(Resources.loadFonts(16f, "RifficFree.ttf"));
        JButton mapSpecialtiesButton = new JButton("Spécialité des cartes");
        mapSpecialtiesButton.setBackground(Color.YELLOW);
        //mapSpecialtiesButton.setFont(Resources.loadFonts(16f, "RifficFree.ttf"));

        howToPlayButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                explanationTextArea.setText("     - PRISE EN MAIN DU LOGICIEL -\n\n Sur le menu du jeu, vous pouvez modifier les réglages sonores en cliquant sur \"Options\" ou vous pouvez vous lancer directement dans une partie en appuyant sur \"Jouer\".\nUne fois dans ce menu, vous pouvez choisir de rejoindre une partie déjà créée ou d'en être l'hôte. Vous pourrez alors configurer la partie comme cela vous convient le mieux !");
            }
        });
        gameFlowButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                explanationTextArea.setText("           > Comment se déroule une partie de Board's Adventure ? <\n\n 1 - Début de tour: Faites constat des évènements qui prennent effet avant votre tour. \n\n 2 - Lancer de dé: Sélectionnez et lancez le dé que vous allez jouer à ce tour. Différents types de dés peuvent être obtenus au cours de la partie.\n\n 3 - Déplacement: Déplacez votre pion à la hauteur du résultat, et choisissez quelle direction emprunter si le chemin vous l'impose. \n\n 4 - Action: L'effet de la case sur laquelle vous atterrissez prend effet. Laissez la chance décider de votre sort avec les évènements, ou faites l'acquisition des propriétés du plateau afin d'affirmer votre monopole sur les autres.");
            }
        });
        mapSpecialtiesButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                explanationTextArea.setText("     - Les différentes cases sur le plateau de jeu -\n ");
            }
        });

        navPanel.add(howToPlayButton);
        navPanel.add(gameFlowButton);
        navPanel.add(mapSpecialtiesButton);
        panel.add(navPanel, BorderLayout.WEST);

        // JButton backButton = createSwitchStateButton("back", MenuState.MAINMENU, 40, 10); // nécessaire avec l'event click ?
        // backButton.addMouseListener(new MouseAdapter() {
        //     @Override
        //     public void mouseClicked(MouseEvent e) {    // Ferme bien le manuel avec l'event (le rend non-visible)
        //         getPanels().get(MANUALBOOK_INDEX).setVisible(false);
        //     }
        // });
        // panel.add(backButton, BorderLayout.SOUTH);

        // panel.addMouseListener(new MouseAdapter() {
        //     @Override
        //     public void mouseEntered(MouseEvent e) {
        //         setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        //     }

        //     @Override
        //     public void mouseClicked(MouseEvent e) {
        //         e.consume();
        //         System.out.println("NAAN CLIQUE PAS ICI Y'A R");
        //     }
        // });
        return panel;
    }

}
