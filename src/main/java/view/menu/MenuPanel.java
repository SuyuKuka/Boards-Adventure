package view.menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;

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
            getPanels().get(menuState.getLayerIndex()).setVisible(false);
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
        final JPanel panel = SwingFactory.createGifPanel(this, "ab", 100, 100);
        panel.setBounds(0, 0, getFrame().getWidth(), getFrame().getHeight());
        panel.setVisible(true);
        return panel;
    }

    // #endregion

    // #region mainmenu 1

    private JPanel createMainMenu() {
        final JPanel panel = SwingFactory.createContainerPanel(this, 0, 0, 0, 0);

        panel.add(SwingFactory.createTransparentPanel(panel, 45, 45));

        final JButton play = SwingFactory.createTextButton(panel, "Commencer une aventure", 20, 10, true);
        createSwitchStateButton(play, MenuState.CREATEJOIN);
        panel.add(play);
        final JButton options = SwingFactory.createTextButton(panel, "Préparer l'expédition", 20, 10, true);
        createSwitchStateButton(options, MenuState.SETTINGSMENU);
        panel.add(options);
        final JButton leaveGame = SwingFactory.createTextButton(panel, "Retourner au foyer", 20, 10, true);
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
        final JPanel panel = SwingFactory.createContainerPanel(this, 10, 15, 0, 15);
        return panel;
    }

    private JPanel createSettings(final JPanel parent) {
        final JPanel panel = SwingFactory.createImagePanel(parent, "frame", 70, 60);

        final int widthMargin = getFrame().getWidth() / 20;
        final int heightMargin = getFrame().getHeight() / 20;
        // Musique
        final JPanel musicPanel = SwingFactory.createBoxLayoutPanel();
        musicPanel.setBorder(BorderFactory.createEmptyBorder(heightMargin, widthMargin, heightMargin, widthMargin));
        final JLabel musique = SwingFactory.createTextLabel("Musique : -", "Komigo3D", Color.BLACK, 20);
        final JSlider musicSlider = SwingFactory.createMusicSlider();
        musicPanel.add(musique);
        musicPanel.add(musicSlider);
        // Son
        final JPanel soundPanel = SwingFactory.createBoxLayoutPanel();
        soundPanel.setBorder(BorderFactory.createEmptyBorder(heightMargin, widthMargin, heightMargin, widthMargin));
        final JLabel sound = SwingFactory.createTextLabel("Son : -", "Komigo3D", Color.BLACK, 20);
        final JSlider soundSlider = SwingFactory.createSoundSlider();
        soundPanel.add(sound);
        soundPanel.add(soundSlider);

        panel.add(musicPanel);
        panel.add(soundPanel);
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                panel.setPreferredSize(new Dimension(panel.getWidth(), panel.getHeight()));
            }
        });
        panel.setVisible(true);
        return panel;
    }

    // #endregion

    // #region createjoin 3

    private JPanel createCreateJoinMenu() {
        final JPanel panel = SwingFactory.createContainerPanel(this, 0, 0, 0, 0);

        panel.add(SwingFactory.createTransparentPanel(panel, 45, 45));

        final JButton create = SwingFactory.createTextButton(panel, "Créer une nouvelle aventure", 15, 8, true);
        createSwitchStateButton(create, MenuState.GAMELOBBY);
        panel.add(create);

        final JButton join = SwingFactory.createTextButton(panel, "Rejoindre une escouade", 20, 10, true);
        createSwitchStateButton(join, MenuState.JOINCODE);
        panel.add(join);

        final JButton back = SwingFactory.createTextButton(panel, "Rebrousser chemin", 20, 10, true);
        createSwitchStateButton(back, MenuState.MAINMENU);
        panel.add(back);
        return panel;
    }

    // #endregion

    // #region joincode 4

    private JPanel createJoinCodeMenu() {
        final JPanel panel = SwingFactory.createContainerPanel(this, 30, 20, 10, 20);
        return panel;
    }

    private JPanel createJoinCode(final JPanel parent) {
        final JPanel panel = SwingFactory.createImagePanel(parent, "frame", 60, 30);
        // TODO CODE ONLINE
        return panel;
    }
    // #endregion

    // #region gamelobby 5

    private JPanel createGameLobbyMenu() {
        final JPanel panel = SwingFactory.createContainerPanel(this, 5, 5, 0, 5);
        return panel;
    }

    private JPanel createLobbyCode(final Component parent) {
        final JPanel panel = SwingFactory.createTransparentPanel(parent, 100, 5);
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 0));
        final int defaultSize = 20;
        final JLabel lobbyCode = SwingFactory.createTextLabel("Code : " + ip.getHostAddress() + ":" + port,
            "RifficFree", new Color(175, 0, 0), defaultSize);
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                lobbyCode.setFont(getFont().deriveFont(Font.BOLD, defaultSize + getFrame().getHeight() / 50));
                lobbyCode.setPreferredSize(new Dimension(panel.getWidth(), panel.getHeight()));
            }
        });
        lobbyCode.setVerticalAlignment(JLabel.CENTER);
        panel.add(lobbyCode);
        return panel;
    }

    private JPanel createGameLobby(final JPanel parent) {
        final JPanel panel = SwingFactory.createImagePanel(parent, "frame", 90, 75);
        panel.setSize(panel.getPreferredSize());
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        panel.add(createMapSection(panel));
        panel.add(SwingFactory.createImageLabel(panel, "border", 5, 80, true));
        panel.add(createGameSettings(panel));
        return panel;
    }

    private JPanel createMapSection(final JPanel parent) {
        final JPanel panel = SwingFactory.createTransparentPanel(parent, 100, 100);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(createMapSelection(parent));
        panel.add(createMapDescription(parent));
        return panel;
    }

    private JPanel createMapSelection(final JPanel parent) {
        final JPanel panel = SwingFactory.createTransparentPanel(parent, 100, 50);
        panel.setOpaque(false);
        panel.setBackground(Color.BLACK);
        return panel;
    }

    private JPanel createMapDescription(final JPanel parent) {
        final JPanel panel = SwingFactory.createTransparentPanel(parent, 100, 50);
        panel.setOpaque(false);
        panel.setBackground(Color.ORANGE);
        return panel;
    }

    private JPanel createGameSettings(final Component parent) {
        final JPanel panel = SwingFactory.createTransparentPanel(parent, 100, 10);
        panel.setOpaque(false);
        panel.setBackground(Color.ORANGE);
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
