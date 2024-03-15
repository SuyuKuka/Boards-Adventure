package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.sound.sampled.FloatControl;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JCheckBox;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSliderUI;

import java.util.HashMap;

import controller.resources.Music;
import controller.resources.Resources;
import view.menu.MenuPanel.MenuState;
import view.menu.MenuPanel;

public class SwingFactory {

    private static final String PATH = "src/main/resources/";

    public static JPanel createNormalPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        return panel;
    }

    public static JPanel createPanel(final View frame, final int borderSize) { //14 Normal
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setVisible(true);
        if (borderSize != 0) {
            panel.setBorder(BorderFactory.createEmptyBorder(frame.getHeight() / borderSize, 0, 0, 0));
        }
        panel.setBounds(0, 0, frame.getWidth(), frame.getHeight());
        return panel;
    }

    public static JPanel createImagePanel(final String key, final int x, final int y,
        final int width, final int height) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(final Graphics g) {
                super.paintComponent(g);
                g.drawImage(new ImageIcon(
                    Resources.getMenuImage(key)).getImage(), x, y, width, height, this);
            }
        };
        panel.setOpaque(false);
        return panel;
    }

    public static JPanel createBorderLayoutPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        return panel;
    }

    public static JPanel createBoxLayoutPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        return panel;
    }

    public static JPanel createFlowLayoutPanel(final int pos) {
        JPanel panel = new JPanel(new FlowLayout(pos));
        panel.setOpaque(false);
        return panel;
    }

    public static JLabel createImageLabel(final String img) {
        JLabel label = new JLabel(new ImageIcon(Resources.getMenuImage(img)));
        label.setOpaque(false);
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return label;
    }

    public static JLabel createLabel(final String st, final int size, final String font) {
        JLabel label = new JLabel(st);
        label.setForeground(Color.BLACK);
        label.setFont(Resources.loadFont(size, font));
        return label;
    }

    public static JButton createImageButton(
        final String img, final View frame, final MenuState state, final MenuPanel panel) {
        JButton button = new JButton(new ImageIcon(Resources.getMenuImage(img)));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(frame.getHeight() / 18, 0, 0, 0));
        if (state != null) {
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(final MouseEvent e) {
                    Music.playSong("song/button.wav");
                    panel.setState(state);
                    if (img.equals("back") || img.equals("join")) {
                        panel.setHost(false);
                    } else if (img.equals("host")) {
                        panel.setHost(true);
                    }
                    panel.updateMenu();
                }
            });
        }
        // TODO : Que les boutons a rendre resize puis c'est bon
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                int newSize = Math.min(frame.getWidth(), frame.getHeight());
                // button.setPreferredSize(new Dimension(newSize, newSize));
                button.setBorder(BorderFactory.createEmptyBorder(newSize / 18, 0, 0, 0));
            }
        });
        return button;
    }

    public static JRadioButton createRadioButton(final String content, final int size,
            final HashMap<String, Integer> configGame, final String key) {
        JRadioButton rButton = new JRadioButton(content);
        rButton.setOpaque(false);
        rButton.setFocusPainted(false);
        rButton.setFont(Resources.loadFont(size, "RifficFree.ttf"));
        rButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                configGame.replace(key, Integer.valueOf(content));
            }
        });
        return rButton;
    }

    public static JCheckBox createJCheckBox(final String content, final int size,
            final HashMap<String, Integer> configGame, final String key) {
        JCheckBox itemBox = new JCheckBox(content);
        itemBox.setOpaque(false);
        itemBox.setFont(Resources.loadFont(size, "RifficFree.ttf"));
        itemBox.setFocusPainted(false);
        itemBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                configGame.replace(key, content.equals("Activé") ? 1 : 0);
            }
        });
        return itemBox;
    }

    public static JSlider createJSlider() {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0) {
            @Override
            public void updateUI() {
                setUI(new BasicSliderUI(this) {
                    /*
                     * Créer la taille du rectangle  qui contiendra le curseur.
                    */
                    @Override
                    protected Dimension getThumbSize() {
                        return new Dimension(20, 20);
                    }

                    /*
                     * Supprimer l'effet de surbrillance en pointiller lorsqu'on clique dessus
                     */
                    @Override
                    protected Color getFocusColor() {
                        return new Color(0, 0, 0, 0);
                    }

                    /*
                     * Paint le fond du rectangle en gris
                     */
                    @Override
                    public void paintTrack(final Graphics g) {
                        Graphics2D g2d = (Graphics2D) g.create();
                        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        Rectangle trackBounds = trackRect;
                        g2d.setColor(Color.GRAY);
                        g2d.fillRect(trackBounds.x - 12, trackBounds.y, trackBounds.width + 24, trackBounds.height);
                        g2d.dispose();
                    }

                    /*
                     * Change la forme du curseur en rectangle et change sa couleur
                     */
                    @Override
                    public void paintThumb(final Graphics g) {
                        Graphics2D g2d = (Graphics2D) g.create();
                        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        Rectangle thumbBounds = thumbRect;
                        g2d.setColor(Color.lightGray);
                        g2d.fillRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height);
                        g2d.dispose();
                    }
                });
            }
        };
        slider.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        slider.setOpaque(false);
        slider.setPreferredSize(new Dimension(150, slider.getPreferredSize().height));
        slider.setMaximumSize(new Dimension(150, slider.getPreferredSize().height));
        slider.setAlignmentX(Component.LEFT_ALIGNMENT);
        return slider;
    }

    public static JSlider createMusicJSlider() {
        JSlider musicSlider = createJSlider();
        musicSlider.setLayout(new BoxLayout(musicSlider, BoxLayout.X_AXIS));
        musicSlider.setValue((int) (Music.getMusicSlider()));
        musicSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(final ChangeEvent e) {
                Music.setMusicVolume(Music.getMusicSlider() / 100.0f);
                Music.setMusicSlider(musicSlider.getValue());
                if (Music.getMusic() != null) {
                    FloatControl gainControl =
                        (FloatControl) Music.getMusic().getControl(FloatControl.Type.MASTER_GAIN);
                    gainControl.setValue(20f * (float) Math.log10(Music.getMusicVolume()));
                }
            }
        });
        return musicSlider;
    }

    public static JSlider createSongJSlider() {
        JSlider songSlider = createJSlider();
        songSlider.setLayout(new BoxLayout(songSlider, BoxLayout.X_AXIS));
        songSlider.setValue((int) (Music.getSongSlider()));
        songSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(final ChangeEvent e) {
                Music.setSongVolume(Music.getSongSlider() / 100.0f);
                Music.setSongSlider(songSlider.getValue());
                if (Music.getSong() != null) {
                    FloatControl gainControl = (FloatControl) Music.getSong().getControl(FloatControl.Type.MASTER_GAIN);
                    gainControl.setValue(20f * (float) Math.log10(Music.getSongVolume()));
                }
            }
        });
        return songSlider;
    }

    public static String getPath() {
        return PATH;
    }
}
