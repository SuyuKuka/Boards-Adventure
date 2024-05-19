package view.menu;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.Image;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

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

    private ImageIcon backgroundImage;

    private int defaultFont = 64;

    public MenuPanel(final View frame) {
        super(frame.getContentPane().getWidth(), frame.getContentPane().getHeight());
        this.frame = frame;
        backgroundImage = Resources.getGif("backgroundMoving");
        addComp(createBackground(), BACKGROUND_INDEX);
        addComp(createMainMenu(), PANEL_INDEX);
        addComp(createManualIcon(), MANUALICON_INDEX);
        addResizeListener();
        setVisible(true);
    }

    public void addResizeListener() {
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                final Dimension dim = getSize();
                setPreferredSize(dim);
                for (final Component comp : getLayersMap().values()) {
                    comp.setSize(dim);
                    comp.setPreferredSize(dim);
                }
                revalidate();
                repaint();
            }
        });
    }

    public void handCursor(JButton button) {
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
        final JPanel panel = new JPanel() {
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
        final JPanel panel = SwingFactory.createContainerPanel(frame.getContentPane(), 0, 0, 0, 5);
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
            public void mousePressed(MouseEvent e) {
                frame.dispose();
                System.exit(0);
            }
        });
        panel.add(leaveGame);
        return panel;
    }

    // #endregion

    // #region Settings

    private JPanel createSettingsMenu() {
        final JPanel panel = SwingFactory.createContainerPanel(frame.getContentPane(), 45, 0, 0, 6);

        // Musique
        final JPanel musicPanel = new JPanel();
        musicPanel.setLayout(new BoxLayout(musicPanel, BoxLayout.X_AXIS));
        musicPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        musicPanel.setOpaque(false);
        musicPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        final JLabel musique = SwingFactory.createTextLabel("Musique : - ", Color.BLACK, defaultFont);
        final JSlider musicSlider = SwingFactory.createMusicSlider();
        final JLabel plusMusic = SwingFactory.createTextLabel(" +", Color.BLACK, defaultFont);
        musicPanel.add(musique);
        musicPanel.add(musicSlider);
        musicPanel.add(plusMusic);
        // Son
        final JPanel soundPanel = new JPanel();
        soundPanel.setLayout(new BoxLayout(soundPanel, BoxLayout.X_AXIS));
        soundPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        soundPanel.setOpaque(false);
        soundPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        final JLabel sound = SwingFactory.createTextLabel("Son : - ", Color.BLACK, defaultFont);
        final JSlider soundSlider = SwingFactory.createSoundSlider();
        final JLabel plusSound = SwingFactory.createTextLabel(" +", Color.BLACK, defaultFont);
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

    // #endregion

    // #region SelectMode

    public JPanel createSelectModeMenu() {
        final JPanel panel = SwingFactory.createContainerPanel(frame.getContentPane(), 0, 0, 0, 5);
        panel.add(SwingFactory.createTransparentPanel(panel, 45, 45));

        final JButton create = SwingFactory.createLobbyButton(panel, "Créer une nouvelle aventure", 20, 10, 64, true);
        handCursor(create);
        create.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                panel.setVisible(false);
                backgroundImage = Resources.getGif("good");
                SwingFactory.startSingleActionTimer(2900, action -> {
                    backgroundImage = new ImageIcon(Resources.getImage("blackscreen"));
                    SwingFactory.startSingleActionTimer(300, action2 -> {
                        backgroundImage = new ImageIcon(Resources.getImage("sign"));
                        removeComp(PANEL_INDEX);
                        addComp(new GameConfigPanel(MenuPanel.this), PANEL_INDEX);
                    });
                });
            }
        });
        panel.add(create);

        final JButton back = SwingFactory.createLobbyButton(panel, "Rebrousser chemin", 20, 10, 64, true);
        handCursor(back);
        changeState(back, MenuState.MAINMENU, PANEL_INDEX);
        panel.add(back);
        return panel;
    }

    // #endregion

    // #endregion

    // #region manualIcon 3
    private JPanel createManualIcon() {
        final JPanel panel = SwingFactory.createContainerPanel(frame.getContentPane(), 93, 0, 0, 95);
        final JButton button = createBook(panel);
        panel.add(button);
        panel.setVisible(true);
        return panel;
    }

    private JButton createBook(final JPanel parent) {
        final JButton button = SwingFactory.createImageButton(parent, "manual_close", 3, 5, false);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent e) {
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

    public void changeState(final Component comp, final MenuState menuState, final int layer1) {
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
                    default:
                        comp1 = null;
                        break;
                }
                addComp(comp1, layer1);
            }
        });
    }

    public int getDefaultFont() {
        return this.defaultFont;
    }

    public ImageIcon getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(ImageIcon backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public View getFrame() {
        return frame;
    }

    public static int getPanelIndex() {
        return PANEL_INDEX;
    }

}
