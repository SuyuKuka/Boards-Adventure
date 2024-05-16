package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
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
import javax.swing.JSlider;
import javax.swing.JTextPane;
import javax.swing.SizeRequirements;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSliderUI;
import javax.swing.text.Element;
import javax.swing.text.ParagraphView;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.InlineView;

import controller.resources.Audio;
import controller.resources.Resources;

public class SwingFactory {

    private static final String PATH = "src/main/resources/";

    /**
     * Crée un {@link JPanel} transparent invisible avec pour layout {@link BoxLayout}
     * et ayant une bordure invisible qui prend X% d'espace associé.
     * @param parent {@link Component} parent.
     * @param topB (0 à 100) en %.
     * @param rightB (0 à 100) en %.
     * @param botB (0 à 100) en %.
     * @param leftB (0 à 100) en %.
     * @return {@link JPanel}.
     */
    public static JPanel createContainerPanel(final Component parent, final int topB,
            final int rightB, final int botB, final int leftB) {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        final int height = (int) parent.getHeight();
        final int width = (int) parent.getWidth();
        panel.setPreferredSize(new Dimension(width, height));
        panel.setBounds(0, 0, width, height);
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(height * topB / 100, width * leftB / 100,
             height * botB / 100, width * rightB / 100));
        parent.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                final int height = (int) parent.getHeight();
                final int width = (int) parent.getWidth();
                panel.setPreferredSize(new Dimension(width, height));
                panel.setBorder(BorderFactory.createEmptyBorder(height * topB / 100, width * leftB / 100,
                    height * botB / 100, width * rightB / 100));
                parent.revalidate();
            }
        });
        panel.setVisible(false);
        return panel;
    }

    public static JPanel createPanel(final Component parent, final int topB,
        final int rightB, final int botB, final int leftB) {
        final JPanel panel = new JPanel();
        panel.setOpaque(false);
        final int height = (int) parent.getHeight();
        final int width = (int) parent.getWidth();
        panel.setPreferredSize(new Dimension(width, height));
        panel.setBorder(BorderFactory.createEmptyBorder(height * topB / 100, width * rightB / 100,
             height * botB / 100, width * leftB / 100));
        parent.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                final int height = (int) parent.getHeight();
                final int width = (int) parent.getWidth();
                panel.setPreferredSize(new Dimension(width, height));
                panel.setBorder(BorderFactory.createEmptyBorder(height * topB / 100, width * rightB / 100,
                    height * botB / 100, width * leftB / 100));
                parent.revalidate();
            }
        });
        panel.setVisible(true);
        return panel;
    }

    /**
     * Crée un {@link JPanel} transparent dynamique sans layout ayant X% de la largeur
     * de son parent et X% de la hauteur de son parent.
     * Utile aussi pour créer de l'espace entre composants.
     * @param parent
     * @param widthPer (0 à 100) %
     * @param heightPer (0 à 100) en %.
     * @return {@link JPanel}.
     */
    public static JPanel createTransparentPanel(final Component parent, final int widthPer, final int heightPer) {
        final JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        final Dimension panelSize = new Dimension((int) (parent.getWidth() * widthPer / 100),
            (int) (parent.getHeight() * heightPer / 100));
        panel.setMaximumSize(panelSize);
        panel.setPreferredSize(panelSize);
        parent.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                final Dimension panelSize = new Dimension((int) (parent.getWidth() * widthPer / 100),
                    (int) (parent.getHeight() * heightPer / 100));
                panel.setMaximumSize(panelSize);
                panel.setPreferredSize(panelSize);
                panel.revalidate();
            }
        });
        return panel;
    }

    public static JPanel createImagePanel(final Component parent, final String img,
        final int widthPer, final int heightPer) {
        final JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                final int width = (int) (parent.getWidth() * widthPer / 100);
                final int height = (int) (parent.getHeight() * heightPer / 100);
                g.drawImage(Resources.getImage(img)
                    .getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, this);
            }
        };
        panel.setOpaque(false);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        final Dimension size = new Dimension(parent.getWidth() * widthPer / 100, parent.getHeight() * heightPer / 100);
        panel.setMaximumSize(size);
        panel.setPreferredSize(size);
        parent.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                final Dimension size = new Dimension((int) (parent.getWidth() * widthPer / 100),
                    (int) (parent.getHeight() * heightPer / 100));
                panel.setMaximumSize(size);
                panel.setPreferredSize(size);
            }
        });
        panel.setVisible(true);
        return panel;
    }

    public static JPanel createGifPanel(final Component parent, final String img,
    final int widthPer, final int heightPer) {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setOpaque(false);
        final Dimension size = new Dimension(parent.getWidth() * widthPer / 100, parent.getHeight() * heightPer / 100);
        panel.setMaximumSize(size);
        panel.setPreferredSize(size);
        final ImageIcon gif = new ImageIcon(Resources.getGif(img).getImage()
        .getScaledInstance(size.width, size.height, Image.SCALE_FAST));
        final JLabel label = new JLabel(gif);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setHorizontalAlignment(JLabel.CENTER);
        panel.add(label, BorderLayout.CENTER);
        parent.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                final Dimension size = new Dimension((int) (parent.getWidth() * widthPer / 100),
                    (int) (parent.getHeight() * heightPer / 100));
                panel.setMaximumSize(size);
                panel.setPreferredSize(size);
                label.setIcon(new ImageIcon(Resources.getGif(img).getImage()
                    .getScaledInstance(size.width, size.height, Image.SCALE_FAST)));
            }
        });
        panel.setVisible(true);
        return panel;
    }


    public static JPanel createGamePanelCells(final Component parent,
    final int widthPer, final int heightPer) {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        final Dimension size = new Dimension(parent.getWidth() * 60 / 100, parent.getHeight() * 60 / 100);
        panel.setMaximumSize(size);
        panel.setPreferredSize(size);
        parent.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                final Dimension size = new Dimension((int) (parent.getWidth() * 60 / 100),
                    (int) (parent.getHeight() * 60 / 100));
                panel.setMaximumSize(size);
                panel.setPreferredSize(size);
            }
        });
        panel.setVisible(true);
        return panel;
    }

    public static JLabel createImageLabel(final Component parent, final String img,
        final int widthPer, final int heightPer, final boolean resize) {
        final Image image = Resources.getImage(img);
        final Dimension size = calcDimension(image, parent, widthPer, heightPer, resize);
        final JLabel label = new JLabel(new ImageIcon(image.getScaledInstance(
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
                final Dimension size = calcDimension(image, parent, widthPer, heightPer, resize);
                label.setPreferredSize(new Dimension((int) size.getWidth() * widthPer / 100,
                    (int) (size.getWidth() * heightPer / 100)));
                label.setIcon(new ImageIcon(image.getScaledInstance(
                    (int) size.getWidth(), (int) size.getHeight(), Image.SCALE_SMOOTH)));
            }
        });
        return label;
    }

    private static Dimension calcDimension(final Image image, final Component parent,
        final int widthPer, final int heightPer, final boolean resize) {
        if (resize) {
            return new Dimension((int) (parent.getWidth() * widthPer / 100),
                (int) (parent.getHeight() * heightPer / 100));
        } else {
            final Dimension imageSize = new Dimension(image.getWidth(null), image.getHeight(null));
            final double widthRatio = parent.getWidth() * widthPer / 100 / imageSize.getWidth();
            final double heightRatio = parent.getHeight() * heightPer / 100 / imageSize.getHeight();
            final double ratio = Math.min(widthRatio, heightRatio);
            // System.out.println(parent.getWidth() * widthPer / 100 + " "
            //     + parent.getHeight() * heightPer / 100 + " " + ratio);

            return new Dimension((int) (imageSize.width  * ratio),
                (int) (imageSize.height * ratio));
        }
    }

    public static JButton createImageButton(final Component parent, final String img,
        final int widthPer, final int heightPer, final boolean resize) {
        final Image image = Resources.getImage(img);
        final Dimension size = calcDimension(image, parent, widthPer, heightPer, resize);
        final JButton button = new JButton(new ImageIcon(image.getScaledInstance(
            (int) size.getWidth(), (int) size.getHeight(), Image.SCALE_SMOOTH)));
            button.setPreferredSize(new Dimension((int) size.getWidth(), (int) size.getHeight()));
        button.setBorderPainted(false);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                parent.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                parent.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                parent.revalidate();
            }
        });
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        parent.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                final Dimension size = calcDimension(image, parent, widthPer, heightPer, resize);
                button.setPreferredSize(new Dimension((int) size.getWidth(), (int) size.getHeight()));
                button.setIcon(new ImageIcon(image.getScaledInstance(
                    (int) size.getWidth(), (int) size.getHeight(), Image.SCALE_SMOOTH)));
            }
        });
        return button;
    }

    public static JButton createTextButton(final Component parent, final String text,
        final int widthPer, final int heightPer, final int sizeFont, final boolean resize) {
        final Image image = Resources.getImage("emptysword_small");
        final Dimension size = calcDimension(image, parent, widthPer, heightPer, resize);
        final JButton button = new JButton();
        button.setPreferredSize(new Dimension((int) size.getWidth() + 20, (int) size.getHeight())); //ne fonctionne pas
        button.setBorderPainted(false);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);

        button.setText(text);
        button.setForeground(Color.BLACK);
        button.setFont(Resources.getFont("NoitaBlackletter", sizeFont));
        button.setHorizontalTextPosition(JButton.LEFT);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setIcon(new ImageIcon(image.getScaledInstance(
                    (int) size.getWidth() / 2, (int) size.getHeight() / 2, Image.SCALE_DEFAULT)));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setIcon(null);
                parent.revalidate();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                Audio.playSound("button.wav");
                parent.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        });
        parent.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // System.out.println(size);
                final Dimension size = calcDimension(image, parent, widthPer, heightPer, resize);
                button.setPreferredSize(new Dimension((int) size.getWidth(), (int) size.getHeight()));
            }
        });
        return button;
    }

    public static JPanel createBoxLayoutPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        return panel;
    }

    public static JLabel createJLabel() {
        final JLabel label = new JLabel();
        label.setVerticalAlignment(JLabel.CENTER);
        label.setHorizontalAlignment(JLabel.CENTER);
        return label;
    }

    public static JLabel createTextLabel(final String text, final String fontName, final Color foregroundColor,
        final int size) {
        final JLabel label = new JLabel(text);
        label.setForeground(foregroundColor);
        label.setFont(Resources.getFont(fontName, size));
        return label;
    }

    public static JSlider createJSlider() {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 100) {
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

    public static JSlider createMusicSlider() {
        JSlider musicSlider = createJSlider();
        musicSlider.setLayout(new BoxLayout(musicSlider, BoxLayout.X_AXIS));
        musicSlider.setValue(Audio.getMusicSliderValue());
        musicSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(final ChangeEvent e) {
                Audio.setMusicVolume(musicSlider.getValue() / 100.0f);
                if (Audio.getMusic() != null) {
                    FloatControl gainControl =
                        (FloatControl) Audio.getMusic().getControl(FloatControl.Type.MASTER_GAIN);
                    gainControl.setValue(20f * (float) Math.log10(Audio.getMusicVolume()));
                }
            }
        });
        return musicSlider;
    }

    public static JSlider createSoundSlider() {
        JSlider soundSlider = createJSlider();
        soundSlider.setLayout(new BoxLayout(soundSlider, BoxLayout.X_AXIS));
        soundSlider.setValue(Audio.getSoundSliderValue());
        soundSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(final ChangeEvent e) {
                Audio.setSoundVolume(soundSlider.getValue() / 100.0f);
                if (Audio.getSound() != null) {
                    FloatControl gainControl =
                        (FloatControl) Audio.getSound().getControl(FloatControl.Type.MASTER_GAIN);
                    gainControl.setValue(20f * (float) Math.log10(Audio.getSoundVolume()));
                }
            }
        });
        return soundSlider;
    }

    public static JTextPane createTextPane() {
        final JTextPane textPane = new JTextPane();
        textPane.setEditable(false);
        // @see https://stackoverflow.com/a/7036671 permet d'avoir du letter wrap dans le textPane
        textPane.setEditorKit(new HTMLEditorKit() {
            @Override
            public ViewFactory getViewFactory() {
                return new HTMLFactory() {
                    public javax.swing.text.View create(final Element e) {
                        javax.swing.text.View v = super.create(e);
                        if (v instanceof InlineView) {
                            return new InlineView(e) {
                                public int getBreakWeight(int axis, float pos, float len) {
                                    return GoodBreakWeight;
                                }
                                public javax.swing.text.View breakView(int axis, int p0, float pos, float len) {
                                    if (axis == javax.swing.text.View.X_AXIS) {
                                        checkPainter();
                                        int p1 = getGlyphPainter().getBoundedPosition(this, p0, pos, len);
                                        if (p0 == getStartOffset() && p1 == getEndOffset()) {
                                            return this;
                                        }
                                        return createFragment(p0, p1);
                                    }
                                    return this;
                                }
                            };
                        } else if (v instanceof ParagraphView) {
                            return new ParagraphView(e) {
                                protected SizeRequirements calculateMinorAxisRequirements(int axis,
                                    SizeRequirements r) {
                                    if (r == null) {
                                        r = new SizeRequirements();
                                    }
                                    float pref = layoutPool.getPreferredSpan(axis);
                                    float min = layoutPool.getMinimumSpan(axis);
                                    r.minimum = (int) min;
                                    r.preferred = Math.max(r.minimum, (int) pref);
                                    r.maximum = Integer.MAX_VALUE;
                                    r.alignment = 0.5f;
                                  return r;
                                }
                            };
                        }
                      return v;
                    }
                };
            }
        });
        return textPane;
    }

    public static String getPath() {
        return PATH;
    }
}
