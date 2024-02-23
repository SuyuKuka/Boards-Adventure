package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.*;

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
public class MenuPanel extends JLayeredPane {

    private MonopolyView frame;

    private enum MenuState {
        MAINMENU, HELPMANUAL, SELECTMOD, LOBBY, WAITINGLOBBY, SETTINGSMENU;
    }

    private MenuState state = MenuState.MAINMENU;

    public MenuPanel(final MonopolyView frame) {
        this.frame = frame;
        setBounds(0, 0, frame.getContentPaneWidth(), frame.getContentPaneHeight());
        add(createBackground("background"), Integer.valueOf(0));
        add(createPanel(), Integer.valueOf(1));
        Music.playMusic("music/MenuTheme.wav");
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                for (Component comp : getComponents()) {
                    comp.setBounds(0, 0, frame.getContentPaneWidth(), frame.getContentPaneHeight());
                }
                revalidate();
            }
        });
        setVisible(true);
    }

    public void updateBackground() {
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

    public void removeMunual() {
        for (Component comp : getComponents()) {
            if (getLayer(comp) == 2) {
                remove(comp);
            }
        }
        revalidate();
        repaint();
    } 

    public JPanel createPanel() {
        switch (state) {
            case MAINMENU:
                return createMainMenu();
            case HELPMANUAL:
                return createHelpManual();
            case SELECTMOD:
                return createSelectMod();
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

    public JPanel createBackground(String st) {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(new ImageIcon(Resources.getMenuImage(st)).getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setBounds(0, 0, frame.getContentPaneWidth(), frame.getContentPaneHeight());
        panel.setVisible(true);
        return panel;
    }

    public JPanel createMainMenu() {
        JPanel panel = SwingFactory.createPanel(frame);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setOpaque(false);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(frame.getContentPaneHeight()/4, 0, 0, 0));

        JButton playButton = SwingFactory.createImageButton("play");
        playButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                state = MenuState.SELECTMOD;
                updateMenu();
                //-> LOCAL / ONLINE
                //LOCAL : SELECTMAP
                //ONLINE : HOST
            }
        });
        JButton settingsButton = SwingFactory.createImageButton("settings");
        settingsButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                state = MenuState.SETTINGSMENU;
                updateMenu();
            }
        });
        JButton leaveButton = SwingFactory.createImageButton("quit");
        leaveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.dispose();
                System.exit(0);
            }
        });
        buttonsPanel.add(playButton);
        buttonsPanel.add(settingsButton);
        buttonsPanel.add(leaveButton);

        JPanel manualPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        manualPanel.setOpaque(false);

        JLabel manual = SwingFactory.createImageLabel("manual_close");
        manual.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        manual.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                manual.setIcon(new ImageIcon(Resources.getMenuImage("manual_bright")));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                manual.setIcon(new ImageIcon(Resources.getMenuImage("manual_close")));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                add(createHelpManual(), Integer.valueOf(2));
            }
        });
        manualPanel.add(manual);

        panel.add(buttonsPanel, BorderLayout.CENTER, 0);
        panel.add(manualPanel, BorderLayout.SOUTH,1);
        return panel;
    }

    //TODO
    public JPanel createHelpManual() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBounds(frame.getContentPaneWidth()/8, frame.getContentPaneHeight()/8, frame.getContentPaneWidth() - frame.getContentPaneWidth()/4, frame.getContentPaneHeight() - frame.getContentPaneHeight()/4);
        panel.setBackground(Color.CYAN);
        return panel;
    }

    //TODO
    public JPanel createSelectMod() {
        JPanel panel = new JPanel(new BorderLayout());

        return null;
    }

    //TODO
    public JPanel createLobby() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBounds(0, 0, frame.getContentPaneWidth(), frame.getContentPaneHeight());
        panel.setBackground(Color.CYAN);
        return panel;
    }

    public JPanel createWaitingLobby() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBounds(0, 0, frame.getContentPaneWidth(), frame.getContentPaneHeight());
        panel.setBackground(Color.GREEN);
        return panel;
    }

    public JPanel createSettingsMenu() {
        add(createBackground("background_options"), Integer.valueOf(0));
        JPanel panel = SwingFactory.createPanel(frame);

        JPanel volumePanel = createVolumePanel();
        volumePanel.setBorder(BorderFactory.createEmptyBorder(frame.getContentPaneHeight()/4, 0, 0, 0));

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setOpaque(false);

        JButton backButton = SwingFactory.createImageButton("back");
        backButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                state = MenuState.MAINMENU;
                updateMenu();
            }
        });
        buttonsPanel.add(backButton);

        panel.add(volumePanel, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.SOUTH);
        return panel;
    }

    public JPanel createVolumePanel() {
        JPanel panel = SwingFactory.createPanel(frame);

        JPanel musicPanel = new JPanel();
        musicPanel.setLayout(new BoxLayout(musicPanel, BoxLayout.Y_AXIS));
        musicPanel.setOpaque(false);

        JLabel musicLabel = new JLabel("Musique :  -");
        musicLabel.setFont(Resources.loadFont(30f, "Komigo3D.ttf"));
        musicLabel.setForeground(Color.BLACK);
        musicLabel.setBorder(BorderFactory.createEmptyBorder(0, frame.getContentPaneWidth()/5, 0, 0));

        JSlider musicSlider = SwingFactory.createMusicJSlider();
        musicPanel.setLayout(new BoxLayout(musicPanel, BoxLayout.X_AXIS));
        musicPanel.add(musicLabel);
        musicPanel.add(Box.createHorizontalStrut(10));
        musicPanel.add(musicSlider);

        JLabel plusMusicLabel = new JLabel(" +");
        plusMusicLabel.setFont(Resources.loadFont(30f, "Komigo3D.ttf"));
        plusMusicLabel.setForeground(Color.BLACK);
        musicPanel.add(plusMusicLabel);


        JPanel songPanel = new JPanel();
        songPanel.setLayout(new BoxLayout(musicPanel, BoxLayout.Y_AXIS));
        songPanel.setOpaque(false);

        JLabel songLabel = new JLabel("Son :  -");
        songLabel.setFont(Resources.loadFont(30f, "Komigo3D.ttf"));
        songLabel.setForeground(Color.BLACK);
        songLabel.setBorder(BorderFactory.createEmptyBorder(0, frame.getContentPaneWidth()/5, 0, 0));

        JSlider songJSlider = SwingFactory.createSongJSlider();
        songPanel.setLayout(new BoxLayout(songPanel, BoxLayout.X_AXIS));
        songPanel.add(songLabel);
        songPanel.add(Box.createHorizontalStrut(10));
        songPanel.add(songJSlider);
        
        JLabel plusSongLabel = new JLabel(" +");
        plusSongLabel.setFont(Resources.loadFont(30f, "Komigo3D.ttf"));
        plusSongLabel.setForeground(Color.BLACK);
        songPanel.add(plusSongLabel);

        panel.add(musicPanel, BorderLayout.NORTH);
        panel.add(songPanel, BorderLayout.CENTER);
        return panel;
    }

    public JPanel createError(){
        JPanel error = new JPanel(new BorderLayout());
        error.setBackground(Color.RED);
        return error;
    }
}
