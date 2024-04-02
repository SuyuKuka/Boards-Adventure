package view.menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import controller.resources.Audio;
import controller.resources.Resources;
import game.Game;
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
        songSliderVolume = 50;
        soundSliderVolume = 50;
        map = "map4";
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        }
    }

    private void createPanels() {
        addPanel(createBackground(), BACKGROUND_INDEX);
        addPanel(createManualIcon(), MANUALICON_INDEX);
        addPanel(createManualBook(), MANUALBOOK_INDEX);
    }

    public void changeState(final MenuState newMenuState) {
        if (menuState != null) {
            removePanel(menuState.getLayerIndex());
        }
        this.menuState = newMenuState;
        addPanel(createPanel(), menuState.getLayerIndex());
        final JPanel newStatePanel = getPanels().get(menuState.getLayerIndex());
        newStatePanel.setBounds(0, 0, getWidth(), getHeight());
        newStatePanel.setVisible(true);
        revalidate();
    }

    public JPanel createPanel() {
        final JPanel panel;
        switch (menuState.getLayerIndex()) {
            case 2:
                panel = createSettingsMenu();
                break;
            case 3:
                panel = createCreateJoinMenu();
                break;
            case 4:
                panel = createJoinCodeMenu();
                break;
            case 5:
                panel = createGameLobbyMenu();
                break;
            default:
                panel = createMainMenu();
                break;
        }
        panel.setBounds(0, 0, getWidth(), getHeight());
        panel.setVisible(true);
        return panel;
    }

    private JButton createSwitchStateButton(final String img, final MenuState newMenuState,
        final int widthPer, final int heightPer) {
        final JButton button = SwingFactory.createImageButton(this, img, widthPer, heightPer, false);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                Audio.playSound("button.wav");
                changeState(newMenuState);
            }
        });
        return button;
    }

    // #region background 0

    private JPanel createBackground() {
        final JPanel panel = SwingFactory.createImagePanel(this, "background", 100, 100);
        panel.setBounds(0, 0, getFrame().getWidth(), getFrame().getHeight());
        panel.setVisible(true);
        return panel;
    }

    // #endregion

    // #region mainmenu 1

    private JPanel createMainMenu() {
        final JPanel panel = SwingFactory.createContainerPanel(this, 10, 0, 0, 0);

        panel.add(SwingFactory.createImageLabel(panel, "logo", 90, 15, false));
        panel.add(SwingFactory.createTransparentPanel(panel, 100, 10));
        panel.add(createSwitchStateButton("play", MenuState.CREATEJOIN, 60, 10));
        panel.add(SwingFactory.createTransparentPanel(panel, 100, 5));
        panel.add(createSwitchStateButton("settings", MenuState.SETTINGSMENU, 60, 10));
        panel.add(SwingFactory.createTransparentPanel(panel, 100, 5));
        final JButton leaveGame = SwingFactory.createImageButton(this, "quit", 60, 10, false);
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
        panel.add(createSettings(panel));
        panel.add(SwingFactory.createTransparentPanel(panel, 100, 10));
        panel.add(createSwitchStateButton("back", MenuState.MAINMENU, 40, 10));
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
        panel.add(createCreateJoin(panel));
        panel.add(createSwitchStateButton("back", MenuState.MAINMENU, 40, 10));
        panel.setVisible(false);
        return panel;
    }

    private JPanel createCreateJoin(final Component parent) {
        final JPanel panel = SwingFactory.createContainerPanel(parent, 40, 0, 30, 0);
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setVisible(true);
        panel.setOpaque(false);
        final JButton createButton = createSwitchStateButton("host", MenuState.GAMELOBBY, 40, 10);
        final JButton joinButton = createSwitchStateButton("join", MenuState.JOINCODE, 40, 10);
        panel.add(createButton);
        panel.add(joinButton);
        return panel;
    }

    // #endregion

    // #region joincode 4

    private JPanel createJoinCodeMenu() {
        final JPanel panel = SwingFactory.createContainerPanel(this, 30, 20, 10, 20);
        panel.add(createJoinCode(panel));
        panel.add(SwingFactory.createTransparentPanel(panel, 100, 20));
        panel.add(createSwitchStateButton("back", MenuState.CREATEJOIN, 40, 10));
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
        final JPanel buttonsPanel = SwingFactory.createTransparentPanel(panel, 100, 11);
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, (int) (panel.getWidth() * 0.2), 0));
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                final FlowLayout layout = (FlowLayout) buttonsPanel.getLayout();
                layout.setHgap((int) (panel.getWidth() * 0.2));
            }
        });

        buttonsPanel.add(createSwitchStateButton("back", MenuState.CREATEJOIN, 40, 10));
        final JButton startGame = SwingFactory.createImageButton(this, "play", 40, 10, false);
        startGame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Audio.stopMusic();
                final Game game = getFrame().getGame();
                game.createModel("map4", gameConfig, playersConfig);
                game.setSwitchMode(true);
            }
        });
        buttonsPanel.add(startGame);

        panel.add(createLobbyCode(panel));
        panel.add(SwingFactory.createTransparentPanel(panel, 100, 2));
        panel.add(createGameLobby(panel));
        panel.add(SwingFactory.createTransparentPanel(panel, 100, 2));
        panel.add(buttonsPanel);
        return panel;
    }

    private JPanel createLobbyCode(final Component parent) {
        final JPanel panel = SwingFactory.createTransparentPanel(parent, 100, 5);
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 0));
        final int defaultSize = 16;
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
        panel.add(createGameSettings(panel));
        return panel;
    }

    private JPanel createMapSection(final JPanel parent) {
        final JPanel panel = SwingFactory.createTransparentPanel(parent, 50, 100);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(createMapSelection(panel));
        panel.add(createMapDescription(panel));
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
        final JPanel panel = SwingFactory.createContainerPanel(this, 1, 1, 1, 1);
        panel.setOpaque(true);
        panel.setBackground(Color.ORANGE);

        // faire sorte que les deux forment bien bandelette du haut
        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Manuel", SwingConstants.CENTER);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.setBackground(Color.GREEN);
        panel.add(titlePanel, BorderLayout.NORTH);

        JButton closeButton = new JButton("X");
        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                getPanels().get(MANUALBOOK_INDEX).setVisible(false);
            }
        });
        titlePanel.add(closeButton, BorderLayout.EAST);

        JTextArea explanationTextArea = new JTextArea("   Bienvenue sur Board's Adventure !\n\n Si vous avez besoin d'informations sur les différents aspects de ce jeu, veuillez jeter un oeil aux rubriques d'aides correspondantes ci-contre.");
        explanationTextArea.setLineWrap(true);
        explanationTextArea.setWrapStyleWord(true);
        explanationTextArea.setEditable(false);
        explanationTextArea.setBackground(Color.CYAN);
        //explanationTextArea.setFont(Resources.loadFonts(14f, "RifficFree.ttf")); // CHANGER LA MANIERE POUR FONTS ?

        JScrollPane scrollPane = new JScrollPane(explanationTextArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        //JPanel navPanel = new JPanel(new GridLayout(3, 1));
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));

        JButton howToPlayButton = new JButton("Prise en main du jeu");
        howToPlayButton.setBackground(Color.YELLOW);
        //howToPlayButton.setFont(Resources.loadFonts(16f, "RifficFree.ttf"));
        JButton gameFlowButton = new JButton("Déroulement de la partie");
        gameFlowButton.setBackground(Color.YELLOW);
        //gameFlowButton.setFont(Resources.loadFonts(16f, "RifficFree.ttf"));
        JButton mapSpecialtiesButton = new JButton("Spécialité des cartes");
        mapSpecialtiesButton.setBackground(Color.YELLOW);
        //mapSpecialtiesButton.setFont(Resources.loadFonts(16f, "RifficFree.ttf"));

        navPanel.add(howToPlayButton);
        navPanel.add(gameFlowButton);
        navPanel.add(mapSpecialtiesButton);
        panel.add(navPanel, BorderLayout.WEST);

        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        titlePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        navPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

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


    /*ANCIEN MANUEL
        private JPanel createHelpManual() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBounds(frame.getContentPaneWidth() / 8, frame.getContentPaneHeight() / 8,
            frame.getContentPaneWidth() - frame.getContentPaneWidth() / 4,
            frame.getContentPaneHeight() - frame.getContentPaneHeight() / 4);
        panel.setBackground(Color.ORANGE);

        explanationTextArea.setFont(Resources.loadFont(14f, "RifficFree.ttf"));

        JScrollPane scrollPane = new JScrollPane(explanationTextArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        mapSpecialtiesButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                explanationTextArea.setText("     - Les différentes cases sur le plateau de jeu -\n ");

                ADAPTER AUX CASES DISPOS DU JEU (x nb de cases), BREVE EXPLICATION ET IMAGE SI POSSIBLE
                //JPanel mapPanel = new JPanel(new GridLayout(x, 1));
                
                //mapPanel.add(createCaseExplanationPanel("event_case_icon.png", "Case événement"));
                //mapPanel.add(createCaseExplanationPanel("property_case_icon.png", "Case propriété"));
                //mapPanel.add(createCaseExplanationPanel("bonus_case_icon.png", "Case bonus"));
                //mapPanel.add(createCaseExplanationPanel("kingdom_case_icon.png", "Case 'kingdom'"));
                //mapPanel.add(createCaseExplanationPanel("intersection_case_icon.png", "Case intersection"));

                //explanationTextArea.setVisible(false); // Cache texte ?
                //panel.add(mapPanel, BorderLayout.CENTER); // Ajouter le panneau des icônes à la place du texte
            }
        });
        return panel;
    }
     * 
     */



    // #endregion

    // private JPanel createJoin() {
    //     add(createBackground("background_menu"), Integer.valueOf(0));
    //     JPanel panel = SwingFactory.createPanel(getFrame(), 14);
    //     JPanel buttonPanel = SwingFactory.createBoxLayoutPanel();
    //     buttonPanel.setBorder(BorderFactory.createEmptyBorder(getFrame().getHeight() / 3, 0, 0, 0));
    //     JButton hostButton = SwingFactory.createImageButton("host", getFrame(), MenuState.CREATEJOIN, this);

    //     JButton joinButton = SwingFactory.createImageButton("join", getFrame(), MenuState.GAMELOBBY, this);
    //     buttonPanel.add(hostButton, BorderLayout.CENTER);
    //     buttonPanel.add(joinButton, BorderLayout.SOUTH);

    //     JPanel botPanel = SwingFactory.createBorderLayoutPanel();

    //     JButton backButton = SwingFactory.createImageButton("back", getFrame(), MenuState.MAINMENU, this);

    //     JButton manual = createManuelButton();
    //     botPanel.add(backButton, BorderLayout.WEST);
    //     botPanel.add(manual, BorderLayout.EAST);

    //     panel.add(buttonPanel, BorderLayout.CENTER);
    //     panel.add(botPanel, BorderLayout.SOUTH);
    //     return panel;
    // }

    // private JPanel createLobby() {
    //     try {
    //         ip = InetAddress.getLocalHost();
    //     } catch (UnknownHostException e1) {
    //         e1.printStackTrace();
    //     }
    //     JPanel panel = SwingFactory.createPanel(getFrame(), 0);

    //     JPanel lobbyNamePanel = SwingFactory.createFlowLayoutPanel(FlowLayout.LEFT);

    //     JLabel invitationLabel = SwingFactory.createLabel("Code d'invitation : ", 12, "RifficFree");
    //     lobbyNamePanel.add(invitationLabel);

    //     JLabel codeLabel = SwingFactory.createLabel(ip.getHostAddress() + ":" + port, 12, "RifficFree");
    //     codeLabel.setForeground(new Color(175, 0, 0));
    //     lobbyNamePanel.add(codeLabel);

    //     JPanel lobbyPanel = SwingFactory.createImagePanel("frame", 20, 0, getWidth() - 40, getHeight());
    //     lobbyPanel.setLayout(new GridLayout(1, 2));
    //     lobbyPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));

    //     JPanel leftPanel = createLeftSide();

    //     rightSidePanel = createRightSide();

    //     JPanel botPanel = SwingFactory.createBorderLayoutPanel();

    //     JButton backButton = SwingFactory.createImageButton("back", getFrame(), MenuState.CREATEJOIN, this);
    //     backButton.addMouseListener(new MouseAdapter() {
    //         @Override
    //         public void mouseClicked(final MouseEvent e) {
    //             nbPlayers = 0;
    //             players.clear();
    //             configGame.clear();
    //             configPlayer.clear();
    //         }
    //     });

    //     JButton startButton = SwingFactory.createImageButton("play", getFrame(), null, this);
    //     startButton.addMouseListener(new MouseAdapter() {
    //         @Override
    //         public void mouseClicked(final MouseEvent e) {
    //             System.out.println(select);
    //             controller.startGame(Resources.loadGameMapsGraphics("map4"), gameConfig, playersConfig);
    //             System.out.println(gameConfig);
    //             System.out.println(playersConfig);
    //             System.out.println("START GAME");
    //             System.out.println("NOMBRE DE JOUEUR " + players.size());
    //         }
    //     });

    //     botPanel.add(backButton, BorderLayout.WEST);
    //     botPanel.add(startButton, BorderLayout.CENTER);

    //     lobbyPanel.add(leftPanel, BorderLayout.WEST);
    //     lobbyPanel.add(rightSidePanel, BorderLayout.EAST);

    //     panel.add(lobbyNamePanel, BorderLayout.NORTH);
    //     panel.add(lobbyPanel, BorderLayout.CENTER);
    //     panel.add(botPanel, BorderLayout.SOUTH);
    //     return panel;
    // }

    // private JPanel createLeftSide() {
    //     JPanel leftSidePanel = SwingFactory.createPanel(getFrame(), 0);

    //     JPanel namePanel = SwingFactory.createFlowLayoutPanel(FlowLayout.CENTER);

    //     JLabel nameLabel = new JLabel("Royaume de Teranor");
    //     nameLabel.setFont(Resources.loadFont(30f, "RifficFree"));
    //     namePanel.add(nameLabel, BorderLayout.CENTER);

    //     JPanel mapSelectionPanel = SwingFactory.createBorderLayoutPanel();

    //     JLabel mapSelect = SwingFactory.createImageLabel("preview" + String.valueOf(select));
    //     mapSelect.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    //     mapSelect.setPreferredSize(new Dimension(200, 250));

    //     JTextArea descMap = new JTextArea();
    //     descMap.setOpaque(false);

    //     JButton leftSelectButton = SwingFactory.createImageButton("select_left", getFrame(), null, this);
    //     if (host) {
    //         leftSelectButton.addMouseListener(new MouseAdapter() {
    //             @Override
    //             public void mouseClicked(final MouseEvent e) {
    //                 if (select > 1) {
    //                     select--;
    //                 } else {
    //                     select = nbMaps;
    //                 }
    //                 mapSelect.setIcon(new ImageIcon(Resources.getMenuImage("preview" + String.valueOf(select))));
    //                 changeContent(nameLabel, descMap);
    //             }
    //         });
    //     } else {
    //         leftSelectButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    //     }
    //     leftSelectButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));

    //     JButton rightSelectButton = SwingFactory.createImageButton("select_right", getFrame(), null, this);
    //     rightSelectButton.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
    //     if (host) {
    //         rightSelectButton.addMouseListener(new MouseAdapter() {
    //             @Override
    //             public void mouseClicked(final MouseEvent e) {
    //                 if (select < nbMaps) {
    //                     select++;
    //                 } else {
    //                     select = 1;
    //                 }
    //                 mapSelect.setIcon(new ImageIcon(Resources.getMenuImage("preview" + String.valueOf(select))));
    //                 changeContent(nameLabel, descMap);
    //             }
    //         });
    //     } else {
    //         rightSelectButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    //     }
    //     mapSelectionPanel.add(leftSelectButton, BorderLayout.WEST);
    //     mapSelectionPanel.add(mapSelect, BorderLayout.CENTER);
    //     mapSelectionPanel.add(rightSelectButton, BorderLayout.EAST);

    //     JPanel descPanel = SwingFactory.createBorderLayoutPanel();
    //     descMap.setEditable(false);
    //     descMap.setLineWrap(true);
    //     descMap.setWrapStyleWord(true);
    //     descMap.setPreferredSize(new Dimension(200, 100));
    //     descMap.setFont(Resources.loadFont(12f, "RifficFree"));
    //     descMap.setBorder(BorderFactory.createEmptyBorder(0, 35, 0, 35));
    //     descPanel.add(descMap);

    //     leftSidePanel.add(namePanel, BorderLayout.NORTH);
    //     leftSidePanel.add(mapSelectionPanel, BorderLayout.CENTER);
    //     leftSidePanel.add(descPanel, BorderLayout.SOUTH);
    //     changeContent(nameLabel, descMap);
    //     return leftSidePanel;
    // }

    // private JPanel createRightSide() {
    //     rightSidePanel = SwingFactory.createPanel(getFrame(), 0);

    //     JPanel configPanel = createConfig();

    //     JPanel midPanel = SwingFactory.createBorderLayoutPanel();

    //     //TODO
    //     JLabel gameMode = new JLabel("Mode de jeu : 1v1");
    //     gameMode.setFont(Resources.loadFont(16, "RifficFree"));
    //     gameMode.setForeground(Color.BLACK);

    //     JPanel buttonPanel = SwingFactory.createBorderLayoutPanel();

    //     JPanel playerList = SwingFactory.createBoxLayoutPanel();
    //     playerList.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
    //     for (int i = 0; i < nbPlayers; i++) {
    //         JPanel reloadPlayer;
    //         if (players.size() == 0) {
    //             nbPlayers++;
    //             reloadPlayer = createPlayerList(nbPlayers, configPlayer);
    //         } else {
    //             rightSidePanel.removeAll();
    //             reloadPlayer = createPlayerList(i, players.get(i));
    //         }
    //         playerList.add(reloadPlayer);
    //         playerList.setName("playerList");
    //         repaint();
    //         revalidate();
    //     }
    //     resizing = false;
    //     if (players.size() == 0) {
    //         nbPlayers++;
    //         playerList.add(createPlayerList(nbPlayers, configPlayer));
    //         playerList.setName("playerList");
    //     }
    //     JLabel playerButton = SwingFactory.createImageLabel("player");
    //     if (host) {
    //         playerButton.addMouseListener(new MouseAdapter() {
    //             @Override
    //             public void mouseClicked(final MouseEvent e) {
    //                 if (nbPlayers < 4) {
    //                     nbPlayers++;
    //                     JPanel newPlayer = createPlayerList(nbPlayers, configPlayer);
    //                     playerList.add(newPlayer);
    //                     playerList.setName("playerList");
    //                     repaint();
    //                     revalidate();
    //                 }
    //             }
    //         });
    //     } else {
    //         playerButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    //     }

    //     JLabel botButton = SwingFactory.createImageLabel("bot");
    //     if (host) {
    //         botButton.addMouseListener(new MouseAdapter() {
    //             @Override
    //             public void mouseClicked(final MouseEvent e) {
    //                 System.out.println("Adding new bot...");
    //             }
    //         });
    //     } else {
    //         botButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    //     }
    //     botButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

    //     JLabel randButton = SwingFactory.createImageLabel("random");
    //     if (host) {
    //         randButton.addMouseListener(new MouseAdapter() {
    //             @Override
    //             public void mouseClicked(final MouseEvent e) {
    //                 System.out.println("Randomizing team...");
    //             }
    //         });
    //     } else {
    //         randButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    //     }
    //     buttonPanel.add(playerButton, BorderLayout.WEST);
    //     buttonPanel.add(botButton, BorderLayout.CENTER);
    //     buttonPanel.add(randButton, BorderLayout.EAST);
    //     midPanel.add(gameMode, BorderLayout.WEST);
    //     midPanel.add(buttonPanel, BorderLayout.EAST);

    //     rightSidePanel.add(configPanel, BorderLayout.NORTH);
    //     rightSidePanel.add(midPanel, BorderLayout.CENTER);
    //     rightSidePanel.add(playerList, BorderLayout.SOUTH);
    //     return rightSidePanel;
    // }

    // private JPanel createConfig() {
    //     if (!resizing) {
    //         initConfig();
    //     }
    //     JPanel panel = SwingFactory.createBorderLayoutPanel();

    //     JPanel titlePanel = createTitlePanel();
    //     panel.add(titlePanel, BorderLayout.NORTH);

    //     JPanel optionsPanel = SwingFactory.createBoxLayoutPanel();
    //     optionsPanel.add(createOptionPanel("Nombre de tour :", new String[]{"30", "40", "50"}, "round"));
    //     optionsPanel.add(createOptionPanel("Double dé :", new String[]{"Activé", "Désactivé"}, "dice"));
    //     optionsPanel.add(createOptionPanel("Objet de départ :", new String[]{"Activé", "Désactivé"}, "item"));
    //     optionsPanel.add(createOptionPanel("Plusieurs gagnants :", new String[]{"Activé", "Désactivé"}, "multiWin"));
    //     panel.add(optionsPanel, BorderLayout.WEST);
    //     return panel;
    // }

    // private JPanel createTitlePanel() {
    //     JPanel titlePanel = SwingFactory.createFlowLayoutPanel(FlowLayout.CENTER);
    //     JLabel title = SwingFactory.createLabel("Paramètre de la partie", 22, "RifficFree");
    //     titlePanel.add(title);
    //     return titlePanel;
    // }

    // private JPanel createOptionPanel(final String labelName, final String[] buttonLabels, final String groupName) {
    //     int size = 16;
    //     int tmp = 0;
    //     JPanel optionPanel = SwingFactory.createNormalPanel();

    //     JLabel optionLabel = SwingFactory.createLabel(labelName, size, "RifficFree");
    //     optionPanel.add(optionLabel);

    //     ButtonGroup optionGroup = new ButtonGroup();
    //     for (String buttonLabel : buttonLabels) {
    //         AbstractButton button;
    //         if (buttonLabels.length > 2) {
    //             button = SwingFactory.createRadioButton(buttonLabel, size, configGame, groupName);
    //             if (tmp == 1) {
    //                 tmp = 0;
    //             }
    //         } else {
    //             button = SwingFactory.createJCheckBox(buttonLabel, size, configGame, groupName);
    //             if (tmp == 2) {
    //                 tmp = 0;
    //             }
    //         }
    //         button.setForeground(Color.black);
    //         if (!host) {
    //             button.setEnabled(false);
    //         }
    //         if (tmp == 0) {
    //             button.setSelected(true);
    //         }
    //         tmp++;
    //         optionGroup.add(button);
    //         optionPanel.add(button);
    //     }
    //     return optionPanel;
    // }

    // private void initConfig() {
    //     configGame.put("map", 1);
    //     configGame.put("round", 30);
    //     configGame.put("dice", 1);
    //     configGame.put("item", 1);
    //     configGame.put("multiWin", 1);
    // }

    // private JPanel createPlayerList(final int y, final HashMap<String, String> config) {
    //     if (!resizing) {
    //         initPlayers();
    //     }
    //     int playerId = Integer.valueOf(config.get("id"));

    //     JPanel panel = new JPanel(new GridBagLayout());
    //     panel.setOpaque(false);
    //     panel.setName("panel_" + playerId);
    //     if (playerId == 1) {
    //         panel.setBorder(BorderFactory.createMatteBorder(2, 2, 1, 2, Color.black));
    //     } else if (playerId == 2 || playerId == 3) {
    //         panel.setBorder(BorderFactory.createMatteBorder(1, 2, 1, 2, Color.black));
    //     } else if (playerId == 4) {
    //         panel.setBorder(BorderFactory.createMatteBorder(1, 2, 2, 2, Color.black));
    //     }

    //     GridBagConstraints gbc = new GridBagConstraints();
    //     gbc.anchor = GridBagConstraints.WEST;
    //     gbc.fill = GridBagConstraints.HORIZONTAL;
    //     gbc.insets = new Insets(0, 5, 0, 5);
    //     gbc.gridy = y;
    //     gbc.weightx = 1;
    //     gbc.weighty = 1;

    //     JLabel manageLabel = new JLabel();
    //     if (host && playerId == 1) {
    //         manageLabel = SwingFactory.createImageLabel("crown");
    //         manageLabel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    //         gbc.gridx = 0;
    //         panel.add(manageLabel, gbc);
    //     } else if (host && playerId != 1) {
    //         manageLabel = SwingFactory.createImageLabel("remove");
    //         manageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 3));
    //         manageLabel.addMouseListener(new MouseAdapter() {
    //             @Override
    //             public void mouseClicked(final MouseEvent e) {
    //                 if (playerId > 1) {
    //                     int playerId = Integer.parseInt(((JLabel) e.getSource()).getName().split("_")[1]);
    //                     players.remove(playerId - 1);
    //                     nbPlayers--;
    //                     updatePlayerIds(playerId, ((JLabel) e.getSource()));

    //                     rightSidePanel.revalidate();
    //                     rightSidePanel.repaint();
    //                 }
    //             }
    //         });
    //     } else {
    //         manageLabel = SwingFactory.createImageLabel("nothing");
    //         manageLabel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    //     }
    //     manageLabel.setName("manageLabel_" + playerId);
    //     gbc.gridx = 0;
    //     panel.add(manageLabel, gbc);

    //     JLabel pseudoLabel = SwingFactory.createLabel(config.get("pseudo"), 12, "RifficFree");
    //     gbc.gridx = 1;
    //     panel.add(pseudoLabel, gbc);

    //     JPanel objectPanel = SwingFactory.createBorderLayoutPanel();

    //     JLabel leftSelection = SwingFactory.createImageLabel("object_left");
    //     leftSelection.addMouseListener(new MouseAdapter() {
    //         @Override
    //         public void mouseClicked(final MouseEvent e) {
    //             if (object > 1) {
    //                 object--;
    //             } else {
    //                 object = 4;
    //             }
    //             int newItem = changeItem();
    //             int playerClickedId = Integer.parseInt(((JLabel) e.getSource()).getName().split("_")[1]);
    //             players.get(playerClickedId - 1).replace("item", String.valueOf(newItem));
    //         }
    //     });
    //     leftSelection.setName("leftSeletion_" + playerId);

    //     JPanel objectContent = SwingFactory.createBorderLayoutPanel();
    //     objectContent.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, -20));

    //     JLabel objectImageLabel = SwingFactory.createImageLabel("sword");
    //     objectImageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
    //     objectImageLabel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

    //     JLabel objectNameLabel = SwingFactory.createLabel(config.get("item"), 12, "RifficFree");
    //     objectContent.add(objectImageLabel, BorderLayout.WEST);
    //     objectContent.add(objectNameLabel, BorderLayout.CENTER);

    //     JLabel rightSelection = SwingFactory.createImageLabel("object_right");
    //     rightSelection.addMouseListener(new MouseAdapter() {
    //         @Override
    //         public void mouseClicked(final MouseEvent e) {
    //             if (object < 4) {
    //                 object++;
    //             } else {
    //                 object = 1;
    //             }
    //             int newItem = changeItem();
    //             int playerClickedId = Integer.parseInt(((JLabel) e.getSource()).getName().split("_")[1]);
    //             players.get(playerClickedId - 1).replace("item", String.valueOf(newItem));
    //         }
    //     });
    //     rightSelection.setName("leftSeletion_" + playerId);
    //     objectPanel.add(leftSelection, BorderLayout.WEST);
    //     objectPanel.add(objectContent, BorderLayout.CENTER);
    //     objectPanel.add(rightSelection, BorderLayout.EAST);
    //     gbc.gridx = 2;
    //     panel.add(objectPanel, gbc);

    //     JLabel status;
    //     if (host) {
    //         status = SwingFactory.createImageLabel("ready");
    //     } else {
    //         status = SwingFactory.createImageLabel("notready");
    //     }
    //     status.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    //     gbc.gridx = 3;
    //     panel.add(status, gbc);

    //     JLabel changeTeam;
    //     if (host) {
    //         changeTeam = SwingFactory.createImageLabel("switch");
    //     } else {
    //         changeTeam = SwingFactory.createImageLabel("nothing");
    //         changeTeam.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    //     }
    //     gbc.gridx = 4;
    //     gbc.gridwidth = GridBagConstraints.REMAINDER;
    //     panel.add(changeTeam, gbc);
    //     if (!resizing) {
    //         players.add(configPlayer);
    //     }
    //     return panel;
    // }

    // private void initPlayers() {
    //     configPlayer.put("id", String.valueOf(nbPlayers));
    //     int playerId = Integer.valueOf(configPlayer.get("id"));
    //     if (host && playerId == 1) {
    //         configPlayer.put("manage", "crown");
    //     } else if (host && playerId != 1) {
    //         configPlayer.put("manage", "remove");
    //     } else {
    //         configPlayer.put("manage", "nothing");
    //     }
    //     configPlayer.put("pseudo", "Joueur" + playerId);
    //     configPlayer.put("item", "EPEE DE TEST");
    //     if (host) {
    //         configPlayer.put("status", "ready");
    //     } else {
    //         configPlayer.put("status", "notready");
    //     }
    //     configPlayer.put("team", "1");
    // }

    // private void updatePlayerIds(final int removedPlayerId, final JLabel removePlayerLabel) {
    //     //Change les données des joueurs en hauts
    //     for (Map<String, String> player : players) {
    //         int playerId = Integer.parseInt(player.get("id"));
    //         if (playerId > removedPlayerId) {
    //             player.put("id", String.valueOf(playerId - 1));
    //         }
    //     }
    //     //Change le nom des panels et labels
    //     for (Component comp : rightSidePanel.getComponents()) {
    //         if (comp.getName() != null) {
    //             for (Component comp2 : ((JPanel) comp).getComponents()) {
    //                 if (comp2.getName().equals("panel_" + removedPlayerId)) {
    //                     ((JPanel) comp).remove(comp2);
    //                 }
    //                 if (removedPlayerId < Integer.parseInt(comp2.getName().split("_")[1])) {
    //                     comp2.setName("panel_" + (Integer.parseInt(comp2.getName().split("_")[1]) - 1));
    //                     for (Component comp3 : ((JPanel) comp2).getComponents()) {
    //                         if (comp3.getName() != null) {
    //                             comp3.setName("manageLabel_" + (Integer.parseInt(comp3.getName()
    //                                  .split("_")[1]) - 1));
    //                         }
    //                     }
    //                 }
    //             }
    //         }
    //     }
    // }

    // private JPanel createWaitingLobby() {
    //     add(createBackground("background_lobby"), Integer.valueOf(0));
    //     JPanel panel = SwingFactory.createImagePanel("frame2", 100, 100, getWidth() - 200, getHeight() - 200);
    //     panel.setLayout(new BorderLayout());
    //     panel.setBounds(0, 0, getFrame().getWidth(), getFrame().getHeight());

    //     JPanel codePanel = SwingFactory.createBoxLayoutPanel();
    //     codePanel.setBorder(BorderFactory.createEmptyBorder(getFrame().getHeight() / 4, 0, 0, 0));

    //     JLabel codeLabel = SwingFactory.createLabel(
    //          "Veuillez saisir votre code d'invitation :", 24, "RifficFree");
    //     codeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

    //     JPanel textFieldPanel = SwingFactory.createFlowLayoutPanel(FlowLayout.CENTER);
    //     textFieldPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

    //     JTextField codeField = new JTextField();
    //     codeField.setHorizontalAlignment(JTextField.CENTER);
    //     codeField.setPreferredSize(new Dimension(400, 30));
    //     codeField.setFont(Resources.loadFont(16, "RifficFree"));
    //     codeField.setText("Saisir ici");
    //     codeField.setForeground(Color.lightGray);
    //     codeField.addMouseListener(new MouseAdapter() {
    //         @Override
    //         public void mousePressed(final MouseEvent e) {
    //             if (codeField.getText().contains("")) {
    //                 codeField.setText("");
    //                 codeField.setForeground(Color.BLACK);
    //             }
    //         }
    //     });
    //     textFieldPanel.add(codeField);

    //     JPanel buttonPanel = SwingFactory.createFlowLayoutPanel(FlowLayout.CENTER);

    //     JButton joinButton = SwingFactory.createImageButton("join", getFrame(), MenuState.GAMELOBBY, this);
    //     joinButton.addMouseListener(new MouseAdapter() {
    //         @Override
    //         public void mouseClicked(final MouseEvent e) {
    //             //Verification de l'ip + port via une fonction ou autre
    //             code = codeField.getText();
    //         }
    //     });
    //     buttonPanel.add(joinButton);
    //     codePanel.add(codeLabel);
    //     codePanel.add(textFieldPanel);
    //     codePanel.add(buttonPanel);

    //     JPanel botPanel = SwingFactory.createBorderLayoutPanel();

    //     JButton backButton = SwingFactory.createImageButton("back", getFrame(), MenuState.CREATEJOIN, this);

    //     JButton manual = createManuelButton();
    //     botPanel.add(backButton, BorderLayout.WEST);
    //     botPanel.add(manual, BorderLayout.EAST);

    //     panel.add(codePanel, BorderLayout.CENTER);
    //     panel.add(botPanel, BorderLayout.SOUTH);
    //     return panel;
    // }

    // private JButton createManuelButton() {
    //     JButton manual = SwingFactory.createImageButton("manual_close", getFrame(), null, this);
    //     manual.addMouseListener(new MouseAdapter() {
    //         @Override
    //         public void mouseEntered(final MouseEvent e) {
    //             manual.setIcon(new ImageIcon(Resources.getMenuImage("manual_bright")));
    //         }

    //         @Override
    //         public void mouseExited(final MouseEvent e) {
    //             manual.setIcon(new ImageIcon(Resources.getMenuImage("manual_close")));
    //         }

    //         @Override
    //         public void mouseClicked(final MouseEvent e) {
    //             add(createHelpManual(), Integer.valueOf(2));
    //         }
    //     });
    //     return manual;
    // }

    // private void changeContent(final JLabel nameMap, final JTextArea textArea) {
    //     switch (select) {
    //         case 1:
    //             nameMap.setText("Royaume de Teranor");
    //             nameMap.setForeground(new Color(120, 60, 0));
    //             textArea.setText("Royaume mediaval incroyable");
    //             textArea.setForeground(new Color(120, 60, 0));
    //             break;
    //         case 2:
    //             nameMap.setText("Stellaria");
    //             nameMap.setForeground(new Color(0, 0, 100));
    //             textArea.setText("Map escape avec un alien qui fait peur :c");
    //             textArea.setForeground(new Color(0, 0, 100));
    //             break;
    //         case 3:
    //             nameMap.setText("Frostoria");
    //             nameMap.setForeground(new Color(136, 248, 248));
    //             textArea.setText("Brrrr il fait giga froid ici");
    //             textArea.setForeground(new Color(136, 248, 248));
    //             break;
    //         case 4:
    //             nameMap.setText("Terre du Crepuscule");
    //             nameMap.setForeground(new Color(180, 0, 0));
    //             textArea.setText("C'est l'heure du du duel !");
    //             textArea.setForeground(new Color(180, 0, 0));
    //             break;
    //         default:
    //             break;
    //     }
    // }

    // private int changeItem() {
    //     //replacer les chiffres des return par leurs objets
    //     switch (object) {
    //         case 1:
    //             return 1;
    //         case 2:
    //             return 2;
    //         case 3:
    //             return 3;
    //         case 4:
    //             return 4;
    //         case 5:
    //             return 5;
    //         default:
    //             return 1;
    //     }
    // }

}
