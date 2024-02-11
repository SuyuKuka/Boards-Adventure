package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import game.Resources;

/**
 * Classe MenuPanel qui s'occupe de toute la vue des menus du jeu.
 * Elle contient : le menu principal, la sélection de niveau, les paramètres, le manuel de jeu.
 * Chaque menu est un panel et tout en bas se trouve un panel pour le background.
 */
public class MenuPanel extends JLayeredPane implements MenuPanelInterface {

    private MonopolyView frame;

    private enum MenuState {
        MAINMENU, HELPMANUAL, SELECTMAP, WAITINGLOBBY, SETTINGSMENU;
    }

    private MenuState state = MenuState.MAINMENU;

    public MenuPanel(final MonopolyView frame) {
        this.frame = frame;
        setBounds(0, 0, frame.getContentPaneWidth(), frame.getContentPaneHeight());
        add(createBackground(), Integer.valueOf(0));
        add(createPanel(), Integer.valueOf(1));
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

    public void updateMenu() {
        for (Component comp : getComponents()) {
            if (getLayer(comp) == 1) {
                remove(comp);
            }
        }
        add(createPanel(), Integer.valueOf(1));
        revalidate();
        repaint();
    }

    public JPanel createPanel() {
        System.out.println(state);
        switch (state) {
            case MAINMENU:
                return createMainMenu();
            case HELPMANUAL:
                return createHelpManual();
            case SELECTMAP:
                return createSelectMap();
            case WAITINGLOBBY:
                return createWaitingLobby();
            case SETTINGSMENU:
                return createSettingsMenu();
            default:
                break;
        }
        JPanel error = new JPanel(new BorderLayout());
        error.setBackground(Color.RED);
        return error;
    }

    public void updateSize() {
        setBounds(0, 0, frame.getContentPaneWidth(), frame.getContentPaneHeight());
    }

    public JPanel createBackground() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0, 0, 255, 50));
        panel.setBounds(0, 0, frame.getContentPaneWidth(), frame.getContentPaneHeight());
        panel.setVisible(true);
        return panel;
    }

    public JPanel createMainMenu() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(50, 200, 100, 200));
        panel.setBounds(0, 0, frame.getContentPaneWidth(), frame.getContentPaneHeight());
        Image logoImg = Resources.getImage("logo");
        JLabel logoLabel = new JLabel();
        logoLabel.setIcon(new ImageIcon(logoImg.getScaledInstance(300, 100, Image.SCALE_FAST)));
        logoLabel.setHorizontalAlignment(JLabel.CENTER);
        panel.add(logoLabel, BorderLayout.NORTH);
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        JButton playButton = new JButton("Jouer !");
        playButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                state = MenuState.SELECTMAP;
                updateMenu();
            }
        });
        buttonsPanel.add(playButton);
        JButton settingsButton = new JButton("Options");
        settingsButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                state = MenuState.SETTINGSMENU;
                updateMenu();
            }
        });
        buttonsPanel.add(settingsButton);
        JButton leaveButton = new JButton("Quitter");
        leaveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                frame.dispose();
            }
        });
        buttonsPanel.add(leaveButton);
        panel.add(buttonsPanel);
        panel.setVisible(true);
        return panel;
    }

    public JPanel createHelpManual() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBounds(0, 0, frame.getContentPaneWidth(), frame.getContentPaneHeight());
        panel.setBackground(Color.BLACK);
        return panel;
    }

    public JPanel createSettingsMenu() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBounds(0, 0, frame.getContentPaneWidth(), frame.getContentPaneHeight());
        panel.setBackground(Color.PINK);
        return panel;
    }

    public JPanel createSelectMap() {
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
}
