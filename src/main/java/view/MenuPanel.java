package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.awt.GridBagLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import controller.MonopolyController;
import controller.Resources;
import controller.Music;

/**
 * Classe MenuPanel qui s'occupe de toute la vue des menus du jeu.
 * Elle contient : le menu principal, la sélection de niveau, les paramètres, le manuel de jeu.
 * Chaque menu est un panel et tout en bas se trouve un panel pour le background.
 */
public final class MenuPanel extends JLayeredPane {

    private MonopolyView frame;
    private MonopolyController controller;
    private HashMap<String, Integer> configGame = new HashMap<>();
    private HashMap<String, String> configPlayer;
    private ArrayList<HashMap<String, String>> players = new ArrayList<>();

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

    public enum MenuState {
        MAINMENU, HELPMANUAL, CREATEJOIN, LOBBY, WAITINGLOBBY, SETTINGSMENU;
    }

    private MenuState state = MenuState.MAINMENU;

    public MenuPanel(final MonopolyView frame) {
        this.frame = frame;
        this.controller = frame.getController();
        setBounds(0, 0, frame.getContentPaneWidth(), frame.getContentPaneHeight());
        add(createBackground("background_menu"), Integer.valueOf(0));
        add(createPanel(), Integer.valueOf(1));
        Music.playMusic("music/MenuTheme.wav");
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                for (Component comp : getComponents()) {
                    comp.setBounds(0, 0, frame.getContentPaneWidth(), frame.getContentPaneHeight());
                }
                revalidate();
            }
        });
        setVisible(true);
    }

    private void updateBackground() {
        for (Component comp : getComponents()) {
            if (getLayer(comp) == 0) {
                remove(comp);
            }
        }
        revalidate();
        repaint();
    }

    public void updateMenu() {
        for (Component comp : getComponents()) {
            if (getLayer(comp) == 1) {
                remove(comp);
            }
        }
        updateBackground();
        removeMunual();
        add(createPanel(), Integer.valueOf(1));
        revalidate();
        repaint();
    }

    private void removeMunual() {
        for (Component comp : getComponents()) {
            if (getLayer(comp) == 2) {
                remove(comp);
            }
        }
        revalidate();
        repaint();
    }

    private JPanel createPanel() {
        switch (state) {
            case MAINMENU:
                return createMainMenu();
            case HELPMANUAL:
                return createHelpManual();
            case CREATEJOIN:
                return createJoin();
            case LOBBY:
                return createLobby();
            case WAITINGLOBBY:
                return createWaitingLobby();
            case SETTINGSMENU:
                return createSettingsMenu();
            default:
                return createMainMenu();
        }
    }

    public void updateSize() {
        setBounds(0, 0, frame.getContentPaneWidth(), frame.getContentPaneHeight());
        resizing = true;
        updateMenu();
    }

    private JPanel createBackground(final String st) {
        JPanel panel = SwingFactory.createImagePanel(st, 0, 0, getWidth(), getHeight());
        panel.setBounds(0, 0, frame.getContentPaneWidth(), frame.getContentPaneHeight());
        return panel;
    }

    private JPanel createMainMenu() {
        add(createBackground("background_menu"), Integer.valueOf(0));
        JPanel panel = SwingFactory.createPanel(frame, 14);

        JPanel buttonsPanel = SwingFactory.createBoxLayoutPanel();
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(frame.getContentPaneHeight() / 5, 0, 0, 0));

        JButton playButton = SwingFactory.createImageButton("play", frame, MenuState.CREATEJOIN, this);

        JButton settingsButton = SwingFactory.createImageButton("settings", frame, MenuState.SETTINGSMENU, this);

        JButton leaveButton = SwingFactory.createImageButton("quit", frame, null, this);
        leaveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                frame.dispose();
                System.exit(0);
            }
        });
        buttonsPanel.add(playButton);
        buttonsPanel.add(settingsButton);
        buttonsPanel.add(leaveButton);

        JPanel manualPanel = SwingFactory.createFlowLayoutPanel(FlowLayout.RIGHT);

        JButton manual = createManuelButton();
        manualPanel.add(manual);

        panel.add(buttonsPanel, BorderLayout.CENTER);
        panel.add(manualPanel, BorderLayout.SOUTH);
        return panel;
    }

    //TODO?
    private JPanel createHelpManual() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBounds(frame.getContentPaneWidth() / 8, frame.getContentPaneHeight() / 8,
            frame.getContentPaneWidth() - frame.getContentPaneWidth() / 4,
            frame.getContentPaneHeight() - frame.getContentPaneHeight() / 4);
        panel.setBackground(Color.CYAN);
        return panel;
    }

    private JPanel createJoin() {
        add(createBackground("background_menu"), Integer.valueOf(0));
        JPanel panel = SwingFactory.createPanel(frame, 14);
        JPanel buttonPanel = SwingFactory.createBoxLayoutPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(frame.getContentPaneHeight() / 3, 0, 0, 0));
        JButton hostButton = SwingFactory.createImageButton("host", frame, MenuState.LOBBY, this);

        JButton joinButton = SwingFactory.createImageButton("join", frame, MenuState.WAITINGLOBBY, this);
        buttonPanel.add(hostButton, BorderLayout.CENTER);
        buttonPanel.add(joinButton, BorderLayout.SOUTH);

        JPanel botPanel = SwingFactory.createBorderLayoutPanel();

        JButton backButton = SwingFactory.createImageButton("back", frame, MenuState.MAINMENU, this);

        JButton manual = createManuelButton();
        botPanel.add(backButton, BorderLayout.WEST);
        botPanel.add(manual, BorderLayout.EAST);

        panel.add(buttonPanel, BorderLayout.CENTER);
        panel.add(botPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createLobby() {
        add(createBackground("background_lobby"), Integer.valueOf(0));
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        }
        JPanel panel = SwingFactory.createPanel(frame, 0);

        JPanel lobbyNamePanel = SwingFactory.createFlowLayoutPanel(FlowLayout.LEFT);

        JLabel invitationLabel = SwingFactory.createLabel("Code d'invitation : ", 12, "RifficFree.ttf");
        lobbyNamePanel.add(invitationLabel);

        JLabel codeLabel = SwingFactory.createLabel(ip.getHostAddress() + ":" + port, 12, "RifficFree.ttf");
        codeLabel.setForeground(new Color(175, 0, 0));
        lobbyNamePanel.add(codeLabel);

        JPanel lobbyPanel = SwingFactory.createImagePanel("frame", 20, 0, getWidth() - 40, getHeight());
        lobbyPanel.setLayout(new GridLayout(1, 2));
        lobbyPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));

        JPanel leftPanel = createLeftSide();

        rightSidePanel = createRightSide();

        JPanel botPanel = SwingFactory.createBorderLayoutPanel();

        JButton backButton = SwingFactory.createImageButton("back", frame, MenuState.CREATEJOIN, this);
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                nbPlayers = 0;
                players.clear();
                configGame.clear();
                configPlayer.clear();
            }
        });

        JButton startButton = SwingFactory.createImageButton("play", frame, null, this);
        startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                System.out.println(select);
                configGame.replace("map", select);
                controller.startGame(configGame);
                System.out.println(configGame);
                System.out.println(players);
                System.out.println("START GAME");
                System.out.println("NOMBRE DE JOUEUR " + players.size());
            }
        });

        JButton manual = createManuelButton();
        botPanel.add(backButton, BorderLayout.WEST);
        botPanel.add(startButton, BorderLayout.CENTER);
        botPanel.add(manual, BorderLayout.EAST);

        lobbyPanel.add(leftPanel, BorderLayout.WEST);
        lobbyPanel.add(rightSidePanel, BorderLayout.EAST);

        panel.add(lobbyNamePanel, BorderLayout.NORTH);
        panel.add(lobbyPanel, BorderLayout.CENTER);
        panel.add(botPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createLeftSide() {
        JPanel leftSidePanel = SwingFactory.createPanel(frame, 0);

        JPanel namePanel = SwingFactory.createFlowLayoutPanel(FlowLayout.CENTER);

        JLabel nameLabel = new JLabel("Royaume de Teranor");
        nameLabel.setFont(Resources.loadFont(30f, "RifficFree.ttf"));
        namePanel.add(nameLabel, BorderLayout.CENTER);

        JPanel mapSelectionPanel = SwingFactory.createBorderLayoutPanel();

        JLabel mapSelect = SwingFactory.createImageLabel("preview" + String.valueOf(select));
        mapSelect.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        mapSelect.setPreferredSize(new Dimension(200, 250));

        JTextArea descMap = new JTextArea();
        descMap.setOpaque(false);

        JButton leftSelectButton = SwingFactory.createImageButton("select_left", frame, null, this);
        if (host) {
            leftSelectButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(final MouseEvent e) {
                    if (select > 1) {
                        select--;
                    } else {
                        select = nbMaps;
                    }
                    mapSelect.setIcon(new ImageIcon(Resources.getMenuImage("preview" + String.valueOf(select))));
                    changeContent(nameLabel, descMap);
                }
            });
        } else {
            leftSelectButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
        leftSelectButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));

        JButton rightSelectButton = SwingFactory.createImageButton("select_right", frame, null, this);
        rightSelectButton.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        if (host) {
            rightSelectButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(final MouseEvent e) {
                    if (select < nbMaps) {
                        select++;
                    } else {
                        select = 1;
                    }
                    mapSelect.setIcon(new ImageIcon(Resources.getMenuImage("preview" + String.valueOf(select))));
                    changeContent(nameLabel, descMap);
                }
            });
        } else {
            rightSelectButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
        mapSelectionPanel.add(leftSelectButton, BorderLayout.WEST);
        mapSelectionPanel.add(mapSelect, BorderLayout.CENTER);
        mapSelectionPanel.add(rightSelectButton, BorderLayout.EAST);

        JPanel descPanel = SwingFactory.createBorderLayoutPanel();
        descMap.setEditable(false);
        descMap.setLineWrap(true);
        descMap.setWrapStyleWord(true);
        descMap.setPreferredSize(new Dimension(200, 100));
        descMap.setFont(Resources.loadFont(12f, "RifficFree.ttf"));
        descMap.setBorder(BorderFactory.createEmptyBorder(0, 35, 0, 35));
        descPanel.add(descMap);

        leftSidePanel.add(namePanel, BorderLayout.NORTH);
        leftSidePanel.add(mapSelectionPanel, BorderLayout.CENTER);
        leftSidePanel.add(descPanel, BorderLayout.SOUTH);
        changeContent(nameLabel, descMap);
        return leftSidePanel;
    }

    private JPanel createRightSide() {
        rightSidePanel = SwingFactory.createPanel(frame, 0);

        JPanel configPanel = createConfig();

        JPanel midPanel = SwingFactory.createBorderLayoutPanel();

        //TODO
        JLabel gameMode = new JLabel("Mode de jeu : 1v1");
        gameMode.setFont(Resources.loadFont(16, "RifficFree.ttf"));
        gameMode.setForeground(Color.BLACK);

        JPanel buttonPanel = SwingFactory.createBorderLayoutPanel();

        JPanel playerList = SwingFactory.createBoxLayoutPanel();
        playerList.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        for (int i = 0; i < nbPlayers; i++) {
            JPanel reloadPlayer;
            if (players.size() == 0) {
                nbPlayers++;
                reloadPlayer = createPlayerList(nbPlayers, configPlayer = new HashMap<>());
            } else {
                rightSidePanel.removeAll();
                reloadPlayer = createPlayerList(i, players.get(i));
            }
            playerList.add(reloadPlayer);
            playerList.setName("playerList");
            repaint();
            revalidate();
        }
        resizing = false;
        if (players.size() == 0) {
            nbPlayers++;
            playerList.add(createPlayerList(nbPlayers, configPlayer = new HashMap<>()));
            playerList.setName("playerList");
        }
        JLabel playerButton = SwingFactory.createImageLabel("player");
        if (host) {
            playerButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(final MouseEvent e) {
                    if (nbPlayers < 4) {
                        nbPlayers++;
                        JPanel newPlayer = createPlayerList(nbPlayers, configPlayer = new HashMap<>());
                        playerList.add(newPlayer);
                        playerList.setName("playerList");
                        repaint();
                        revalidate();
                    }
                }
            });
        } else {
            playerButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }

        JLabel botButton = SwingFactory.createImageLabel("bot");
        if (host) {
            botButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(final MouseEvent e) {
                    System.out.println("Adding new bot...");
                }
            });
        } else {
            botButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
        botButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

        JLabel randButton = SwingFactory.createImageLabel("random");
        if (host) {
            randButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(final MouseEvent e) {
                    System.out.println("Randomizing team...");
                }
            });
        } else {
            randButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
        buttonPanel.add(playerButton, BorderLayout.WEST);
        buttonPanel.add(botButton, BorderLayout.CENTER);
        buttonPanel.add(randButton, BorderLayout.EAST);
        midPanel.add(gameMode, BorderLayout.WEST);
        midPanel.add(buttonPanel, BorderLayout.EAST);

        rightSidePanel.add(configPanel, BorderLayout.NORTH);
        rightSidePanel.add(midPanel, BorderLayout.CENTER);
        rightSidePanel.add(playerList, BorderLayout.SOUTH);
        return rightSidePanel;
    }

    private JPanel createConfig() {
        if (!resizing) {
            initConfig();
        }
        JPanel panel = SwingFactory.createBorderLayoutPanel();

        JPanel titlePanel = createTitlePanel();
        panel.add(titlePanel, BorderLayout.NORTH);

        JPanel optionsPanel = SwingFactory.createBoxLayoutPanel();
        optionsPanel.add(createOptionPanel("Nombre de tour :", new String[]{"30", "40", "50"}, "round"));
        optionsPanel.add(createOptionPanel("Double dé :", new String[]{"Activé", "Désactivé"}, "dice"));
        optionsPanel.add(createOptionPanel("Objet de départ :", new String[]{"Activé", "Désactivé"}, "item"));
        optionsPanel.add(createOptionPanel("Plusieurs gagnants :", new String[]{"Activé", "Désactivé"}, "multiWin"));
        panel.add(optionsPanel, BorderLayout.WEST);
        return panel;
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = SwingFactory.createFlowLayoutPanel(FlowLayout.CENTER);
        JLabel title = SwingFactory.createLabel("Paramètre de la partie", 22, "RifficFree.ttf");
        titlePanel.add(title);
        return titlePanel;
    }

    private JPanel createOptionPanel(final String labelName, final String[] buttonLabels, final String groupName) {
        int size = 16;
        int tmp = 0;
        JPanel optionPanel = SwingFactory.createNormalPanel();

        JLabel optionLabel = SwingFactory.createLabel(labelName, size, "RifficFree.ttf");
        optionPanel.add(optionLabel);

        ButtonGroup optionGroup = new ButtonGroup();
        for (String buttonLabel : buttonLabels) {
            AbstractButton button;
            if (buttonLabels.length > 2) {
                button = SwingFactory.createRadioButton(buttonLabel, size, configGame, groupName);
                if (tmp == 1) {
                    tmp = 0;
                }
            } else {
                button = SwingFactory.createJCheckBox(buttonLabel, size, configGame, groupName);
                if (tmp == 2) {
                    tmp = 0;
                }
            }
            button.setForeground(Color.black);
            if (!host) {
                button.setEnabled(false);
            }
            if (tmp == 0) {
                button.setSelected(true);
            }
            tmp++;
            optionGroup.add(button);
            optionPanel.add(button);
        }
        return optionPanel;
    }

    private void initConfig() {
        configGame.put("map", 1);
        configGame.put("round", 30);
        configGame.put("dice", 1);
        configGame.put("item", 1);
        configGame.put("multiWin", 1);
    }

    private JPanel createPlayerList(final int y, final HashMap<String, String> config) {
        if (!resizing) {
            initPlayers();
        }
        int playerId = Integer.valueOf(config.get("id"));

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setName("panel_" + playerId);
        if (playerId == 1) {
            panel.setBorder(BorderFactory.createMatteBorder(2, 2, 1, 2, Color.black));
        } else if (playerId == 2 || playerId == 3) {
            panel.setBorder(BorderFactory.createMatteBorder(1, 2, 1, 2, Color.black));
        } else if (playerId == 4) {
            panel.setBorder(BorderFactory.createMatteBorder(1, 2, 2, 2, Color.black));
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 5, 0, 5);
        gbc.gridy = y;
        gbc.weightx = 1;
        gbc.weighty = 1;

        JLabel manageLabel = new JLabel();
        if (host && playerId == 1) {
            manageLabel = SwingFactory.createImageLabel("crown");
            manageLabel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            gbc.gridx = 0;
            panel.add(manageLabel, gbc);
        } else if (host && playerId != 1) {
            manageLabel = SwingFactory.createImageLabel("remove");
            manageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 3));
            manageLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(final MouseEvent e) {
                    if (playerId > 1) {
                        int playerId = Integer.parseInt(((JLabel) e.getSource()).getName().split("_")[1]);
                        players.remove(playerId - 1);
                        nbPlayers--;
                        updatePlayerIds(playerId, ((JLabel) e.getSource()));

                        rightSidePanel.revalidate();
                        rightSidePanel.repaint();
                    }
                }
            });
        } else {
            manageLabel = SwingFactory.createImageLabel("nothing");
            manageLabel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
        manageLabel.setName("manageLabel_" + playerId);
        gbc.gridx = 0;
        panel.add(manageLabel, gbc);

        JLabel pseudoLabel = SwingFactory.createLabel(config.get("pseudo"), 12, "RifficFree.ttf");
        gbc.gridx = 1;
        panel.add(pseudoLabel, gbc);

        JPanel objectPanel = SwingFactory.createBorderLayoutPanel();

        JLabel leftSelection = SwingFactory.createImageLabel("object_left");
        leftSelection.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                if (object > 1) {
                    object--;
                } else {
                    object = 4;
                }
                int newItem = changeItem();
                int playerClickedId = Integer.parseInt(((JLabel) e.getSource()).getName().split("_")[1]);
                players.get(playerClickedId - 1).replace("item", String.valueOf(newItem));
            }
        });
        leftSelection.setName("leftSeletion_" + playerId);

        JPanel objectContent = SwingFactory.createBorderLayoutPanel();
        objectContent.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, -20));

        JLabel objectImageLabel = SwingFactory.createImageLabel("sword");
        objectImageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        objectImageLabel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

        JLabel objectNameLabel = SwingFactory.createLabel(config.get("item"), 12, "RifficFree.ttf");
        objectContent.add(objectImageLabel, BorderLayout.WEST);
        objectContent.add(objectNameLabel, BorderLayout.CENTER);

        JLabel rightSelection = SwingFactory.createImageLabel("object_right");
        rightSelection.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                if (object < 4) {
                    object++;
                } else {
                    object = 1;
                }
                int newItem = changeItem();
                int playerClickedId = Integer.parseInt(((JLabel) e.getSource()).getName().split("_")[1]);
                players.get(playerClickedId - 1).replace("item", String.valueOf(newItem));
            }
        });
        rightSelection.setName("leftSeletion_" + playerId);
        objectPanel.add(leftSelection, BorderLayout.WEST);
        objectPanel.add(objectContent, BorderLayout.CENTER);
        objectPanel.add(rightSelection, BorderLayout.EAST);
        gbc.gridx = 2;
        panel.add(objectPanel, gbc);

        JLabel status;
        if (host) {
            status = SwingFactory.createImageLabel("ready");
        } else {
            status = SwingFactory.createImageLabel("notready");
        }
        status.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        gbc.gridx = 3;
        panel.add(status, gbc);

        JLabel changeTeam;
        if (host) {
            changeTeam = SwingFactory.createImageLabel("switch");
        } else {
            changeTeam = SwingFactory.createImageLabel("nothing");
            changeTeam.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
        gbc.gridx = 4;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        panel.add(changeTeam, gbc);
        if (!resizing) {
            players.add(configPlayer);
        }
        return panel;
    }

    private void initPlayers() {
        configPlayer.put("id", String.valueOf(nbPlayers));
        int playerId = Integer.valueOf(configPlayer.get("id"));
        if (host && playerId == 1) {
            configPlayer.put("manage", "crown");
        } else if (host && playerId != 1) {
            configPlayer.put("manage", "remove");
        } else {
            configPlayer.put("manage", "nothing");
        }
        configPlayer.put("pseudo", "Joueur" + playerId);
        configPlayer.put("item", "EPEE DE TEST");
        if (host) {
            configPlayer.put("status", "ready");
        } else {
            configPlayer.put("status", "notready");
        }
        configPlayer.put("team", "1");
    }

    private void updatePlayerIds(final int removedPlayerId, final JLabel removePlayerLabel) {
        //Change les données des joueurs en hauts
        for (Map<String, String> player : players) {
            int playerId = Integer.parseInt(player.get("id"));
            if (playerId > removedPlayerId) {
                player.put("id", String.valueOf(playerId - 1));
            }
        }
        //Change le nom des panels et labels
        for (Component comp : rightSidePanel.getComponents()) {
            if (comp.getName() != null) {
                for (Component comp2 : ((JPanel) comp).getComponents()) {
                    if (comp2.getName().equals("panel_" + removedPlayerId)) {
                        ((JPanel) comp).remove(comp2);
                    }
                    if (removedPlayerId < Integer.parseInt(comp2.getName().split("_")[1])) {
                        comp2.setName("panel_" + (Integer.parseInt(comp2.getName().split("_")[1]) - 1));
                        for (Component comp3 : ((JPanel) comp2).getComponents()) {
                            if (comp3.getName() != null) {
                                comp3.setName("manageLabel_" + (Integer.parseInt(comp3.getName().split("_")[1]) - 1));
                            }
                        }
                    }
                }
            }
        }
    }

    private JPanel createWaitingLobby() {
        add(createBackground("background_lobby"), Integer.valueOf(0));
        JPanel panel = SwingFactory.createImagePanel("frame2", 100, 100, getWidth() - 200, getHeight() - 200);
        panel.setLayout(new BorderLayout());
        panel.setBounds(0, 0, frame.getContentPaneWidth(), frame.getContentPaneHeight());

        JPanel codePanel = SwingFactory.createBoxLayoutPanel();
        codePanel.setBorder(BorderFactory.createEmptyBorder(frame.getHeight() / 4, 0, 0, 0));

        JLabel codeLabel = SwingFactory.createLabel("Veuillez saisir votre code d'invitation :", 24, "RifficFree.ttf");
        codeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel textFieldPanel = SwingFactory.createFlowLayoutPanel(FlowLayout.CENTER);
        textFieldPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JTextField codeField = new JTextField();
        codeField.setHorizontalAlignment(JTextField.CENTER);
        codeField.setPreferredSize(new Dimension(400, 30));
        codeField.setFont(Resources.loadFont(16, "RifficFree.ttf"));
        codeField.setText("Saisir ici");
        codeField.setForeground(Color.lightGray);
        codeField.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent e) {
                if (codeField.getText().contains("")) {
                    codeField.setText("");
                    codeField.setForeground(Color.BLACK);
                }
            }
        });
        textFieldPanel.add(codeField);

        JPanel buttonPanel = SwingFactory.createFlowLayoutPanel(FlowLayout.CENTER);

        JButton joinButton = SwingFactory.createImageButton("join", frame, MenuState.LOBBY, this);
        joinButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                //Verification de l'ip + port via une fonction ou autre
                code = codeField.getText();
            }
        });
        buttonPanel.add(joinButton);
        codePanel.add(codeLabel);
        codePanel.add(textFieldPanel);
        codePanel.add(buttonPanel);

        JPanel botPanel = SwingFactory.createBorderLayoutPanel();

        JButton backButton = SwingFactory.createImageButton("back", frame, MenuState.CREATEJOIN, this);

        JButton manual = createManuelButton();
        botPanel.add(backButton, BorderLayout.WEST);
        botPanel.add(manual, BorderLayout.EAST);

        panel.add(codePanel, BorderLayout.CENTER);
        panel.add(botPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createSettingsMenu() {
        add(createBackground("background_settings"), Integer.valueOf(0));
        JPanel panel = SwingFactory.createPanel(frame, 14);

        JPanel volumePanel = createVolumePanel();
        volumePanel.setBorder(BorderFactory.createEmptyBorder(frame.getContentPaneHeight() / 4, 0, 0, 0));

        JPanel botPanel = SwingFactory.createBorderLayoutPanel();

        JButton backButton = SwingFactory.createImageButton("back", frame, MenuState.MAINMENU, this);

        JButton manual = createManuelButton();
        botPanel.add(backButton, BorderLayout.WEST);
        botPanel.add(manual, BorderLayout.EAST);

        panel.add(volumePanel, BorderLayout.CENTER);
        panel.add(botPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createVolumePanel() {
        int size = 30;
        JPanel panel = SwingFactory.createPanel(frame, 14);

        JPanel musicPanel = SwingFactory.createBoxLayoutPanel();
        musicPanel.setLayout(new BoxLayout(musicPanel, BoxLayout.X_AXIS));

        JLabel musicLabel = SwingFactory.createLabel("Musique :  -", size, "Komigo3D.ttf");
        musicLabel.setBorder(BorderFactory.createEmptyBorder(0, frame.getContentPaneWidth() / 5, 0, 0));

        JSlider musicSlider = SwingFactory.createMusicJSlider();

        JLabel plusMusicLabel = SwingFactory.createLabel(" +", 30, "Komigo3D.ttf");
        musicPanel.add(musicLabel);
        musicPanel.add(Box.createHorizontalStrut(10));
        musicPanel.add(musicSlider);
        musicPanel.add(plusMusicLabel);

        JPanel songPanel = SwingFactory.createBoxLayoutPanel();
        songPanel.setLayout(new BoxLayout(songPanel, BoxLayout.X_AXIS));

        JLabel songLabel = SwingFactory.createLabel("Son :  -", size, "Komigo3D.ttf");
        songLabel.setBorder(BorderFactory.createEmptyBorder(0, frame.getContentPaneWidth() / 5, 0, 0));

        JSlider songJSlider = SwingFactory.createSongJSlider();

        JLabel plusSongLabel = SwingFactory.createLabel(" +", 30, "Komigo3D.ttf");
        songPanel.add(songLabel);
        songPanel.add(Box.createHorizontalStrut(10));
        songPanel.add(songJSlider);
        songPanel.add(plusSongLabel);

        panel.add(musicPanel, BorderLayout.NORTH);
        panel.add(songPanel, BorderLayout.CENTER);
        return panel;
    }

    private JButton createManuelButton() {
        JButton manual = SwingFactory.createImageButton("manual_close", frame, null, this);
        manual.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(final MouseEvent e) {
                manual.setIcon(new ImageIcon(Resources.getMenuImage("manual_bright")));
            }

            @Override
            public void mouseExited(final MouseEvent e) {
                manual.setIcon(new ImageIcon(Resources.getMenuImage("manual_close")));
            }

            @Override
            public void mouseClicked(final MouseEvent e) {
                add(createHelpManual(), Integer.valueOf(2));
            }
        });
        return manual;
    }

    private void changeContent(final JLabel nameMap, final JTextArea textArea) {
        switch (select) {
            case 1:
                nameMap.setText("Royaume de Teranor");
                nameMap.setForeground(new Color(120, 60, 0));
                textArea.setText("Royaume mediaval incroyable");
                textArea.setForeground(new Color(120, 60, 0));
                break;
            case 2:
                nameMap.setText("Stellaria");
                nameMap.setForeground(new Color(0, 0, 100));
                textArea.setText("Map escape avec un alien qui fait peur :c");
                textArea.setForeground(new Color(0, 0, 100));
                break;
            case 3:
                nameMap.setText("Frostoria");
                nameMap.setForeground(new Color(136, 248, 248));
                textArea.setText("Brrrr il fait giga froid ici");
                textArea.setForeground(new Color(136, 248, 248));
                break;
            case 4:
                nameMap.setText("Terre du Crepuscule");
                nameMap.setForeground(new Color(180, 0, 0));
                textArea.setText("C'est l'heure du du duel !");
                textArea.setForeground(new Color(180, 0, 0));
                break;
            default:
                break;
        }
    }

    private int changeItem() {
        //replacer les chiffres des return par leurs objets
        switch (object) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            default:
                return 1;
        }
    }

    public HashMap<String, Integer> getHashMap() {
        return configGame;
    }

    public MenuState getState() {
        return state;
    }

    public void setState(final MenuState state) {
        this.state = state;
    }

    public void setHost(final boolean host) {
        this.host = host;
    }

    public ArrayList<HashMap<String, String>> getPlayers() {
        return players;
    }

    public String getCode() {
        return code;
    }
}
