package view.menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.Image;
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
import javax.swing.ScrollPaneConstants;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

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
    private static final int PANEL_INDEX = 1;
    private static final int MANUALICON_INDEX = 2;
    private static final int MANUALPANEL_INDEX = 3;

    private final View frame;

    private HashMap<String, String> gameConfig = new HashMap<>();
    private ArrayList<HashMap<String, String>> playersConfig = new ArrayList<>();
    private HashMap<String, String> player;
    private HashMap<String, ButtonGroup> buttonGroups = new HashMap<>();

    private ImageIcon backgroundImage;

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

    private int defaultFont = 64;

    public MenuPanel(final View frame) {
        super(frame.getContentPane().getWidth(), frame.getContentPane().getHeight());
        this.frame = frame;
        this.height = getPreferredSize().width;
        this.width = getPreferredSize().height;
        // Audio.playMusic("MenuTheme.wav");
        initDefaultValues();
        backgroundImage = Resources.getGif("backgroundMoving");
        addComp(createBackground(), BACKGROUND_INDEX);
        addComp(createMainMenu(), PANEL_INDEX);
        addComp(createManualIcon(), MANUALICON_INDEX);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                final Dimension dim = getSize();
                setPreferredSize(dim);
                for (Component comp : getLayersMap().values()) {
                    comp.setSize(dim);
                }
                revalidate();
                repaint();
            }
        });
        setVisible(true);
    }

    private void initDefaultValues() {
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        }
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

    // #region background

    private JPanel createBackground() {
        final JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(final Graphics g) {
                super.paintComponent(g);
                final Dimension dim = getSize();
                g.drawImage(backgroundImage.getImage(), 0, 0, dim.width, dim.height, this);
            }
        };
        panel.setSize(getPreferredSize());
        panel.setVisible(true);
        return panel;
    }

    // #endregion

    // #region mainmenu 1

    private JPanel createMainMenu() {
        final JPanel panel = SwingFactory.createContainerPanel(this, 0, 0, 0, 5);
        panel.add(SwingFactory.createTransparentPanel(panel, 45, 45));

        final JButton play = SwingFactory.createLobbyButton(panel, "Commencer une aventure", 20, 10, 64, true);
        handCursor(play);
        changeState(play, MenuState.SELECTMODE, PANEL_INDEX);
        panel.add(play);

        final JButton options = SwingFactory.createLobbyButton(panel, "Préparer l'expédition", 20, 10, 64, true);
        handCursor(options);
        changeState(options, MenuState.SETTINGS, PANEL_INDEX);
        panel.add(options);

        final JButton leaveGame = SwingFactory.createLobbyButton(panel, "Retourner au foyer", 20, 10, 64, true);
        handCursor(leaveGame);
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

    //#region panels

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

        final JButton back = SwingFactory.createLobbyButton(panel, "Rebrousser chemin", 20, 10, 64, true);
        changeState(back, MenuState.MAINMENU, PANEL_INDEX);
        handCursor(back);
        panel.add(back);
        return panel;
    }

    private JPanel createSelectModeMenu() {
        final JPanel panel = SwingFactory.createContainerPanel(this, 0, 0, 0, 5);
        panel.add(SwingFactory.createTransparentPanel(panel, 45, 45));

        final JButton create = SwingFactory.createLobbyButton(panel, "Créer une nouvelle aventure", 20, 10, 64, true);
        handCursor(create);
        create.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                host = true;
                backgroundImage = Resources.getGif("good");
                SwingFactory.startSingleActionTimer(2900, action -> {
                    backgroundImage = new ImageIcon(Resources.getImage("blackscreen"));
                    SwingFactory.startSingleActionTimer(300, action2 -> {
                        backgroundImage = new ImageIcon(Resources.getImage("sign"));
                        removeComp(PANEL_INDEX);
                        addComp(createGameLobbyMenu(), PANEL_INDEX);
                    });
                });
            }
        });
        panel.add(create);

        final JButton join = SwingFactory.createLobbyButton(panel, "Rejoindre une escouade", 20, 10, 64, true);
        handCursor(join);
        changeState(join, MenuState.JOINGAME, PANEL_INDEX);
        panel.add(join);

        final JButton back = SwingFactory.createLobbyButton(panel, "Rebrousser chemin", 20, 10, 64, true);
        handCursor(back);
        changeState(back, MenuState.MAINMENU, PANEL_INDEX);
        panel.add(back);
        return panel;
    }

    private JPanel createJoinCodeMenu() {
        final JPanel panel = SwingFactory.createContainerPanel(this, 0, 0, 0, 5);
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
                    backgroundImage = Resources.getGif("good");
                    SwingFactory.startSingleActionTimer(2900, action -> {
                        backgroundImage = Resources.getGif("blackscreen");
                        SwingFactory.startSingleActionTimer(300, action2 -> {
                            backgroundImage = new ImageIcon(Resources.getImage("village"));
                            changeState(join, MenuState.PLAYERCONFIG, PANEL_INDEX);
                        });
                    });
                }
            }
        });
        codePanel.add(join, BorderLayout.EAST);
        handCursor(join);
        panel.add(codePanel);

        JButton back = SwingFactory.createLobbyButton(panel, "Rebrousser chemin", 20, 10, 64, true);
        handCursor(back);
        changeState(back, MenuState.SELECTMODE, PANEL_INDEX);
        panel.add(back);
        return panel;
    }

    //#endregion

    // #region gamelobby 5

    // #endregion

    // IL DOIT Y AVOIR UN INIT HOST
    // QUAND JE CREER UN JOUEUR YA UN INIT PLAYER
    // FAUT POSSIBILIER DE SUPPRIMER UN JOUEUR
    // QUAND TU AJOUTER UN PLAYER, LA FLECHE DISPARAIT MAIS UNE NOUVELLE APPARAIT

    //#region character 6

    private JPanel createSelectionCharacterMenu() {
        final JPanel panel = SwingFactory.createContainerPanel(this, 10, 0, 0, 0);
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

        selectSkin(parent, skinImg, character, playerID, 0);
        skinListener(left, parent, skinImg, character, playerID, -1);
        skinListener(right, parent, skinImg, character, playerID, 1);
        return panel;
    }

    private void skinListener(JButton button, JPanel skinSelection, ImageName mapImg,
        JLabel skinLabel, int playerID, int i) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectSkin(skinSelection, mapImg, skinLabel, playerID, i);
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
        // back.addMouseListener(new MouseAdapter() {
        //     @Override
        //     public void mouseClicked(MouseEvent e) {
        //         System.out.println(host);
        //         if (host) {
        //             changeState(MenuState.MAPCONFIG);
        //             changeBackgroundImg("sign");
        //         } else {
        //             changeState(MenuState.SELECTMOD);
        //             changeBackgroundGif("backgroundMoving");
        //         }
        //     }
        // });
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
        handCursor(startGame);
        panel.add(startGame, BorderLayout.EAST);
        return panel;
    }

    //#endregion

    // #region manualIcon 7
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
                addComp(new ManualPanel(MenuPanel.this, MANUALPANEL_INDEX), MANUALPANEL_INDEX);
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

    private void selectSkin(JPanel parent, ImageName skinImg, JLabel skin, int playerID, int i) {
        int nb = Integer.valueOf(playersConfig.get(playerID).get("skin"));
        nb += i;
        if (nb >= 6) {
            nb = 1;
        } else if (nb <= 0) {
            nb = 5;
        }

        final Dimension size = SwingFactory.calcDimension(skinImg.getImage(), parent, 60, 40, false);
        // skin.setPreferredSize(new Dimension((int) size.getWidth() * 40 / 100, (int) (size.getHeight() * 60 / 100)));

        String skinName = "";
        switch (nb) {
            case 1 :
                skinName = "frame-1";
                break;
            case 2 :
                skinName = "frame-2";
                break;
            case 3 :
                skinName = "frame-3";

                break;
            case 4 :
                skinName = "frame-4";

                break;
            case 5 :
                skinName = "frame-5";
                break;
            default:
                break;
        }
        skin.setIcon(new ImageIcon(Resources.getImage(skinName).getScaledInstance(
            (int) size.getWidth(), (int) size.getHeight(), Image.SCALE_SMOOTH)));
        playersConfig.get(playerID).replace("skin", String.valueOf(nb));

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

    private void changeState(final Component comp, final MenuState menuState, final int layer1) {
        comp.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(final MouseEvent e) {
                removeComp(layer1);
                final Component comp1;
                switch (menuState) {
                    case MAINMENU:
                        comp1 = createMainMenu();
                        break;
                    case SELECTMODE:
                        comp1 = createSelectModeMenu();
                        break;
                    case SETTINGS:
                        comp1 = createSettingsMenu();
                        break;
                    case JOINGAME:
                        comp1 = createJoinCodeMenu();
                        break;
                    case GAMECONFIG:
                        comp1 = new GameConfigPanel(MenuPanel.this);
                        break;
                    case PLAYERCONFIG:
                        comp1 = createSelectionCharacterMenu();
                        break;
                    default:
                        comp1 = null;
                        break;
                }
                addComp(comp1, layer1);
            }
        });
    }

    public InetAddress getIp() {
        return ip;
    }

}
