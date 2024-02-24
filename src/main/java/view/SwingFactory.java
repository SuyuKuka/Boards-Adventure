package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSliderUI;

import controller.Music;
import controller.Resources;

public class SwingFactory {

    private static final String PATH = "src/main/resources/";

    public static JPanel createPanel(final MonopolyView frame) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setVisible(true);
        panel.setBorder(BorderFactory.createEmptyBorder(frame.getContentPaneHeight() / 14, 0, 0, 0));
        panel.setBounds(0, 0, frame.getContentPaneWidth(), frame.getContentPaneHeight());
        return panel;
    }

    public static JPanel createSubPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        return panel;
    }

    public static JLabel createImageLabel(final String img) {
        JLabel label = new JLabel(new ImageIcon(Resources.getMapImage(img)));
        label.setOpaque(false);
        return label;
    }

    public static JButton createImageButton(final String img, final MonopolyView frame) {
        JButton button = new JButton(new ImageIcon(Resources.getMenuImage(img)));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(frame.getContentPaneHeight() / 18, 0, 0, 0));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                Music.playSong("song/button.wav");
            }
        });
        //Pour le resize mais pas trop fonctionnelle actuellement
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                int newSize = Math.min(frame.getWidth(), frame.getHeight());
                //button.setPreferredSize(new Dimension(newSize, newSize));
                button.setBorder(BorderFactory.createEmptyBorder(newSize / 18, 0, 0, 0));
            }
        });
        return button;
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
