package view;

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
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JSlider;

import controller.Music;
import controller.Resources;

/**
 * Classe MenuPanel qui s'occupe de toute la vue des menus du jeu.
 * Elle contient : le menu principal, la sélection de niveau, les paramètres, le manuel de jeu.
 * Chaque menu est un panel et tout en bas se trouve un panel pour le background.
 */
public final class MenuPanel extends JLayeredPane {

    private MonopolyView frame;
    private boolean host = false;
    private int select = 1;
    private int nbMaps = 4; //faire en sorte qu'il compte lui meme??

    private enum MenuState {
        MAINMENU, HELPMANUAL, CREATEJOIN, LOBBY, WAITINGLOBBY, SETTINGSMENU;
    }

    private MenuState state = MenuState.MAINMENU;

    public MenuPanel(final MonopolyView frame) {
        this.frame = frame;
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

    private void updateMenu() {
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
            case WAITINGLOBBY: //est-il nécessaire?
                return createWaitingLobby();
            case SETTINGSMENU:
                return createSettingsMenu();
            default:
                return createError();
        }
    }

    public void updateSize() {
        setBounds(0, 0, frame.getContentPaneWidth(), frame.getContentPaneHeight());
    }

    private JPanel createBackground(final String st) {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(final Graphics g) {
                super.paintComponent(g);
                g.drawImage(new ImageIcon(Resources.getMenuImage(st)).getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setBounds(0, 0, frame.getContentPaneWidth(), frame.getContentPaneHeight());
        panel.setVisible(true);
        return panel;
    }

    private JPanel createMainMenu() {
        add(createBackground("background_menu"), Integer.valueOf(0));
        JPanel panel = SwingFactory.createPanel(frame);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setOpaque(false);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(frame.getContentPaneHeight() / 4, 0, 0, 0));

        JButton playButton = SwingFactory.createImageButton("play", frame);
        playButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                state = MenuState.CREATEJOIN;
                updateMenu();
                //-> LOCAL / ONLINE
                //LOCAL : LOBBY
                //ONLINE : LOBBY / WAITING LOBBY?
            }
        });
        JButton settingsButton = SwingFactory.createImageButton("settings", frame);
        settingsButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                state = MenuState.SETTINGSMENU;
                updateMenu();
            }
        });
        JButton leaveButton = SwingFactory.createImageButton("quit", frame);
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

        JPanel manualPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        manualPanel.setOpaque(false);

        JButton manual = createManuelButton();
        manualPanel.add(manual);

        panel.add(buttonsPanel, BorderLayout.CENTER);
        panel.add(manualPanel, BorderLayout.SOUTH);
        return panel;
    }

    //TODO
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
        JPanel panel = SwingFactory.createPanel(frame);
        JPanel buttonPanel = SwingFactory.createSubPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(frame.getContentPaneHeight() / 3, 0, 0, 0));
        JButton hostButton = SwingFactory.createImageButton("host", frame);
        hostButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                host = true;
                state = MenuState.LOBBY;
                updateMenu();
            }
        });

        JButton joinButton = SwingFactory.createImageButton("join", frame);
        joinButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                host = false;
                state = MenuState.WAITINGLOBBY;
                updateMenu();
            }
        });
        buttonPanel.add(hostButton, BorderLayout.CENTER);
        buttonPanel.add(joinButton, BorderLayout.SOUTH);

        JPanel botPanel = SwingFactory.createSubPanel();

        JButton backButton = SwingFactory.createImageButton("back", frame);
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                state = MenuState.MAINMENU;
                updateMenu();
            }
        });

        JButton manual = createManuelButton();

        botPanel.add(backButton, BorderLayout.WEST);
        botPanel.add(manual, BorderLayout.EAST);

        panel.add(buttonPanel, BorderLayout.CENTER);
        panel.add(botPanel, BorderLayout.SOUTH);
        return panel;
    }

    //TODO
    private JPanel createLobby() {
        add(createBackground("background_lobby"), Integer.valueOf(0));
        JPanel panel = SwingFactory.createPanel(frame);

        JPanel lobbyNamePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lobbyNamePanel.setOpaque(false);

        JLabel lobbyNameLabel = new JLabel("Room code : ");
        lobbyNameLabel.setFont(Resources.loadFont(12f, "RifficFree.ttf"));
        lobbyNamePanel.add(lobbyNameLabel);

        JPanel lobbyPanel = SwingFactory.createSubPanel();
        lobbyPanel.setLayout(new GridLayout(1, 2));

        JPanel leftSidePanel = createLeftSide();

        JPanel rightSidePanel = createRightSide();

        lobbyPanel.add(leftSidePanel);
        lobbyPanel.add(rightSidePanel);

        //AJOUT DU CODE EN NORTH???
        panel.add(lobbyNamePanel, BorderLayout.NORTH);
        panel.add(lobbyPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createLeftSide() {
        JPanel leftSidePanel = SwingFactory.createPanel(frame);

        JPanel mapSelectionPanel = SwingFactory.createSubPanel();

        JButton leftSelectButton = SwingFactory.createImageButton("select_left", frame);
        leftSelectButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                if (select > 1) { //4 map actuellement
                    select--;
                    //mapSelect.setIcon(new ImageIcon("map" + select));
                } else {
                    select = nbMaps;
                }
                System.out.println("Left : " + select);
            }
        });

        JLabel mapSelect = SwingFactory.createImageLabel("board");
        mapSelect.setPreferredSize(new Dimension(200, 250));

        JButton rightSelectButton = SwingFactory.createImageButton("select_right", frame);
        rightSelectButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                if (select < nbMaps) { //4 map actuellement
                    select++;
                    //mapSelect.setIcon(new ImageIcon("map" + select));
                } else {
                    select = 1;
                }
                System.out.println("Right : " + select);
            }
        });

        mapSelectionPanel.add(leftSelectButton, BorderLayout.WEST);
        mapSelectionPanel.add(mapSelect, BorderLayout.CENTER);
        mapSelectionPanel.add(rightSelectButton, BorderLayout.EAST);

        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        namePanel.setOpaque(false);

        JLabel nameLabel = new JLabel("Mario Party");
        nameLabel.setFont(Resources.loadFont(30f, "RifficFree.ttf"));
        namePanel.add(nameLabel);

        JPanel botPanel = SwingFactory.createSubPanel();

        JButton backButton = SwingFactory.createImageButton("back", frame);
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                state = MenuState.CREATEJOIN;
                updateMenu();
            }
        });

        botPanel.add(backButton, BorderLayout.WEST);

        leftSidePanel.add(mapSelectionPanel, BorderLayout.NORTH);
        leftSidePanel.add(namePanel, BorderLayout.CENTER);
        leftSidePanel.add(botPanel, BorderLayout.SOUTH);
        return leftSidePanel;
    }

    private JPanel createRightSide() {
        JPanel panel = SwingFactory.createPanel(frame);
        panel.setOpaque(true);
        panel.setBackground(Color.BLACK);
        return panel;
    }

    //TODO?
    private JPanel createWaitingLobby() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBounds(0, 0, frame.getContentPaneWidth(), frame.getContentPaneHeight());
        panel.setBackground(Color.GREEN);
        return panel;
    }

    private JPanel createSettingsMenu() {
        add(createBackground("background_settings"), Integer.valueOf(0));
        JPanel panel = SwingFactory.createPanel(frame);

        JPanel volumePanel = createVolumePanel();
        volumePanel.setBorder(BorderFactory.createEmptyBorder(frame.getContentPaneHeight() / 4, 0, 0, 0));

        JPanel botPanel = new JPanel(new BorderLayout());
        botPanel.setOpaque(false);

        JButton backButton = SwingFactory.createImageButton("back", frame);
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                state = MenuState.MAINMENU;
                updateMenu();
            }
        });

        JButton manual = createManuelButton();
        botPanel.add(backButton, BorderLayout.WEST);
        botPanel.add(manual, BorderLayout.EAST);

        panel.add(volumePanel, BorderLayout.CENTER);
        panel.add(botPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createVolumePanel() {
        JPanel panel = SwingFactory.createPanel(frame);

        JPanel musicPanel = new JPanel();
        musicPanel.setLayout(new BoxLayout(musicPanel, BoxLayout.Y_AXIS));
        musicPanel.setOpaque(false);

        JLabel musicLabel = new JLabel("Musique :  -");
        musicLabel.setFont(Resources.loadFont(30f, "Komigo3D.ttf"));
        musicLabel.setForeground(Color.BLACK);
        musicLabel.setBorder(BorderFactory.createEmptyBorder(0, frame.getContentPaneWidth() / 5, 0, 0));

        JSlider musicSlider = SwingFactory.createMusicJSlider();
        musicPanel.setLayout(new BoxLayout(musicPanel, BoxLayout.X_AXIS));

        JLabel plusMusicLabel = new JLabel(" +");
        plusMusicLabel.setFont(Resources.loadFont(30f, "Komigo3D.ttf"));
        plusMusicLabel.setForeground(Color.BLACK);
        musicPanel.add(musicLabel);
        musicPanel.add(Box.createHorizontalStrut(10));
        musicPanel.add(musicSlider);
        musicPanel.add(plusMusicLabel);


        JPanel songPanel = SwingFactory.createSubPanel();
        songPanel.setLayout(new BoxLayout(musicPanel, BoxLayout.Y_AXIS));

        JLabel songLabel = new JLabel("Son :  -");
        songLabel.setFont(Resources.loadFont(30f, "Komigo3D.ttf"));
        songLabel.setForeground(Color.BLACK);
        songLabel.setBorder(BorderFactory.createEmptyBorder(0, frame.getContentPaneWidth() / 5, 0, 0));

        JSlider songJSlider = SwingFactory.createSongJSlider();
        songPanel.setLayout(new BoxLayout(songPanel, BoxLayout.X_AXIS));

        JLabel plusSongLabel = new JLabel(" +");
        plusSongLabel.setFont(Resources.loadFont(30f, "Komigo3D.ttf"));
        plusSongLabel.setForeground(Color.BLACK);
        songPanel.add(songLabel);
        songPanel.add(Box.createHorizontalStrut(10));
        songPanel.add(songJSlider);
        songPanel.add(plusSongLabel);

        panel.add(musicPanel, BorderLayout.NORTH);
        panel.add(songPanel, BorderLayout.CENTER);
        return panel;
    }

    private JButton createManuelButton() {
        JButton manual = SwingFactory.createImageButton("manual_close", frame);
        manual.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
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

    private JPanel createError() {
        JPanel error = new JPanel(new BorderLayout());
        error.setBackground(Color.RED);
        return error;
    }
}
