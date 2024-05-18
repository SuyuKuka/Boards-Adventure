package view.menu;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.util.Enumeration;

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
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import org.w3c.dom.events.MouseEvent;

import controller.resources.Resources;
import view.SwingFactory;
import view.menu.MenuPanel.ImageName;

public final class GameConfigPanel extends JPanel {

    private final MenuPanel menuPanel;

    public GameConfigPanel(final MenuPanel menuPanel) {
        this.menuPanel = menuPanel;
    }
    private JPanel createGameLobbyMenu() {
        final JPanel panel = SwingFactory.createContainerPanel(this, 2, 0, 0, 0);
        panel.add(createLobbyCode(panel));
        panel.add(createGameLobby(panel));
        panel.add(createBottomButton(panel));
        return panel;
    }

    private JPanel createLobbyCode(final Component parent) {
        final JPanel panel = SwingFactory.createTransparentPanel(parent, 100, 5);
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 0));
        final JLabel lobbyCode = SwingFactory.createTextLabel("Code : " + ip.getHostAddress() + ":" + port,
            "NoitaBlackletter", Color.BLACK, 12 / 2);
        lobbyCode.setVerticalAlignment(JLabel.CENTER);
        panel.add(lobbyCode);
        return panel;
    }

    private JPanel createGameLobby(final JPanel parent) {
        final JPanel panel = SwingFactory.createImagePanel(parent, "sign", 100, 85);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        panel.setSize(panel.getPreferredSize());
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        panel.add(SwingFactory.createTransparentPanel(parent, 2, 0));
        panel.add(createMapSection(panel));
        panel.add(createGameSettings(panel));
        return panel;
    }

    private JPanel createMapSection(final JPanel parent) {
        final JPanel panel = SwingFactory.createImagePanel(parent, "paper", 50, 90);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        final JPanel mapSelection = SwingFactory.createTransparentPanel(parent, 100, 50);
        mapSelection.setLayout(new BorderLayout());

        ImageName mapImg = new ImageName(1, Resources.getImage("preview1"));

        JButton left = SwingFactory.createImageButton(parent, "left", 20, 10, false);
        mapSelection.add(left, BorderLayout.WEST);
        JLabel mapLabel = createImageImageLabel(parent, mapImg, 60, 60, false);
        mapSelection.add(mapLabel, BorderLayout.CENTER);
        JButton right = SwingFactory.createImageButton(parent, "right", 20, 10, false);
        mapSelection.add(right, BorderLayout.EAST);
        panel.add(mapSelection);

        final JPanel mapDescription = SwingFactory.createTransparentPanel(parent, 100, 50);
        mapDescription.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        mapDescription.setLayout(new BorderLayout());

        JTextArea desc = new JTextArea();
        desc.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        desc.setFont(Resources.getFont("NoitaBlackletter", defaultFont / 2));
        desc.setForeground(Color.BLACK);
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);
        desc.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(desc, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        mapDescription.add(scrollPane, BorderLayout.CENTER);
        panel.add(mapDescription);

        selectMap(parent, mapImg, mapLabel, desc, 0);
        mapListerner(left, parent, mapImg, mapLabel, desc, -1);
        mapListerner(right, parent, mapImg, mapLabel, desc, 1);
        return panel;
    }

    private void mapListerner(JButton button, JPanel mapSelection, ImageName mapImg,
        JLabel mapLabel, JTextArea desc, int i) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectMap(mapSelection, mapImg, mapLabel, desc, i);
            }
        });
        handCursor(button);
    }

    private JPanel createGameSettings(final Component parent) {
        final JPanel panel = SwingFactory.createTransparentPanel(parent, 50, 90);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(createTitleMap(parent));
        panel.add(SwingFactory.createTransparentPanel(parent, 40, 10));
        panel.add(createMapConfig(parent));
        return panel;
    }

    private JPanel createTitleMap(final Component parent) {
        final JPanel panel = SwingFactory.createImagePanel(parent, "paper", 40, 25);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(Box.createVerticalGlue());

        titleMap = SwingFactory.createTextLabel("Royaume Médieval", "NoitaBlackletter", Color.BLACK, defaultFont);
        titleMap.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleMap);
        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private JPanel createMapConfig(final Component parent) {
        final JPanel panel = SwingFactory.createImagePanel(parent, "paper", 40, 65);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel turnsPanel = SwingFactory.createTransparentPanel(parent, 40, 10);
        turnsPanel.setLayout(new BoxLayout(turnsPanel, BoxLayout.X_AXIS));
        turnsPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 0, 0));
        JLabel turns = SwingFactory.createTextLabel("Nombre de tours :", "NoitaBlackletter",
            Color.BLACK, defaultFont / 2);

        ButtonGroup turnsGroup = new ButtonGroup();
        JRadioButton turnsBox1 = createRadioButton("25", turnsGroup, true);
        JRadioButton turnsBox2 = createRadioButton("35", turnsGroup, false);
        JRadioButton turnsBox3 = createRadioButton("45", turnsGroup, false);
        turnsPanel.add(turns);
        turnsPanel.add(turnsBox1);
        turnsPanel.add(turnsBox2);
        turnsPanel.add(turnsBox3);

        JPanel doubleDicePanel = SwingFactory.createTransparentPanel(parent, 40, 10);
        doubleDicePanel.setLayout(new BoxLayout(doubleDicePanel, BoxLayout.X_AXIS));
        doubleDicePanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 0, 0));
        JLabel doubleDice = SwingFactory.createTextLabel("Double dé :", "NoitaBlackletter",
            Color.BLACK, defaultFont / 2);

        ButtonGroup doubleDiceGroup = new ButtonGroup();
        JRadioButton doubleDiceBox1 = createRadioButton("Désactivé", doubleDiceGroup, true);
        JRadioButton doubleDiceBox2 = createRadioButton("Activé", doubleDiceGroup, false);

        doubleDicePanel.add(doubleDice);
        doubleDicePanel.add(doubleDiceBox1);
        doubleDicePanel.add(doubleDiceBox2);

        JPanel modePanel = SwingFactory.createTransparentPanel(parent, 40, 10);
        modePanel.setLayout(new BoxLayout(modePanel, BoxLayout.X_AXIS));
        modePanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 0, 0));
        JLabel mode = SwingFactory.createTextLabel("Mode de jeu :", "NoitaBlackletter", Color.BLACK, defaultFont / 2);

        ButtonGroup modeGroup = new ButtonGroup();
        JRadioButton modeBox1 = createRadioButton("FFA", modeGroup, true);
        JRadioButton modeBox2 = createRadioButton("2v2", modeGroup, false);

        modePanel.add(mode);
        modePanel.add(modeBox1);
        modePanel.add(modeBox2);

        buttonGroups.put("turns", turnsGroup);
        buttonGroups.put("doubleDice", doubleDiceGroup);
        buttonGroups.put("mode", modeGroup);

        panel.add(turnsPanel);
        panel.add(doubleDicePanel);
        panel.add(modePanel);
        return panel;
    }

    private JRadioButton createRadioButton(String text, ButtonGroup group, boolean selected) {
        JRadioButton radioButton = new JRadioButton(text, selected);
        radioButton.setOpaque(false);
        radioButton.setForeground(Color.BLACK);
        radioButton.setBorderPainted(false);
        radioButton.setContentAreaFilled(false);
        radioButton.setFocusPainted(false);
        radioButton.setFont(Resources.getFont("NoitaBlackletter", 32));
        group.add(radioButton);
        return radioButton;
    }

    private JPanel createBottomButton(final JPanel parent) {
        final JPanel panel = SwingFactory.createTransparentPanel(parent, 100, 10);
        panel.setLayout(new BorderLayout());
        final JButton back = new JButton("Retourner à la base");
        handCursor(back);
        back.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(final MouseEvent e) {
                backgroundImage = Resources.getGif("backgroundMoving");
            }
        });
        back.setBorderPainted(false);
        back.setOpaque(false);
        back.setContentAreaFilled(false);
        back.setFocusPainted(false);
        back.setForeground(Color.BLACK);
        back.setAlignmentX(Component.LEFT_ALIGNMENT);
        back.setFont(Resources.getFont("NoitaBlackletter", 64));
        back.setHorizontalTextPosition(JButton.LEFT);
        back.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                host = false;
                parent.setVisible(false);
                // removeComp(BACKGROUND_INDEX);
                // addComp(createBackground(), BACKGROUND_INDEX);
            }
        });
        // createSwitchStateButton(back, MenuState.SELECTMOD);
        handCursor(back);
        panel.add(back, BorderLayout.WEST);

        JButton selectCharacter = new JButton("Selectionner cette aventure");
        selectCharacter.setBorderPainted(false);
        selectCharacter.setOpaque(false);
        selectCharacter.setContentAreaFilled(false);
        selectCharacter.setFocusPainted(false);
        selectCharacter.setForeground(Color.BLACK);
        selectCharacter.setAlignmentX(Component.RIGHT_ALIGNMENT);
        selectCharacter.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 100));
        selectCharacter.setFont(Resources.getFont("NoitaBlackletter", 64));
        selectCharacter.setHorizontalTextPosition(JButton.LEFT);
        selectCharacter.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (String key : buttonGroups.keySet()) {
                    ButtonGroup bg = buttonGroups.get(key);
                    for (Enumeration<AbstractButton> buttons = bg.getElements(); buttons.hasMoreElements();) {
                        AbstractButton button = buttons.nextElement();
                        if (button.isSelected()) {
                            gameConfig.put(key, button.getText());
                            break;
                        }
                    }
                }
                System.out.println(gameConfig);
            }
        });
        handCursor(selectCharacter);
        panel.add(selectCharacter, BorderLayout.EAST);
        return panel;
    }
}
