package view.menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.SwingConstants;

import controller.resources.Resources;
import view.SwingFactory;

public final class ManualPanel extends JPanel {

    private final MenuPanel menuPanel;
    private final int layer;

    public ManualPanel(final MenuPanel menuPanel, final int layer) {
        this.menuPanel = menuPanel;
        this.layer = layer;
        setSize(menuPanel.getPreferredSize());
        setLayout(new BorderLayout());
        setOpaque(false);
        add(createManuelPanel());
    }

    private JPanel createManuelPanel() {
        final JPanel panel = SwingFactory.createContainerPanel(this, 5, 5, 5, 5);
        panel.setOpaque(false);
        panel.setBackground(new Color(205, 173, 142));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.ORANGE);
        mainPanel.setBorder(BorderFactory.createLineBorder(new Color(205, 173, 142), 4));

        // HAUT, CHANGER FONTS
        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("  Manuel", SwingConstants.CENTER);
        titleLabel.setFont(Resources.getFont("NoitaBlackletter", 22));
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.setBackground(new Color(205, 173, 142));

        JButton closeButton = SwingFactory.createImageButton(this, "delete", 4, 4, false);

        titlePanel.add(closeButton, BorderLayout.EAST);
        int titleHeight = (int) (panel.getHeight() * 0.05);
        titlePanel.setPreferredSize(new Dimension(panel.getWidth(), titleHeight));
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // BAS, CHANGER FONTS
        JPanel contentPanel = new JPanel(new GridLayout(1, 2));
        contentPanel.setBackground(new Color(230, 205, 175));

        JPanel navPanel = new JPanel(new GridLayout(3, 1));
        JButton howToPlayButton = new JButton("Prise en main du jeu");
        howToPlayButton.setBackground(new Color(230, 205, 175));
        howToPlayButton.setFont(Resources.getFont("NoitaBlackletter", 64));
        JButton gameFlowButton = new JButton("Déroulement de la partie");
        gameFlowButton.setBackground(new Color(230, 205, 175));
        gameFlowButton.setFont(Resources.getFont("NoitaBlackletter", 64));
        JButton mapSpecialtiesButton = new JButton("Spécialité des cartes");
        mapSpecialtiesButton.setBackground(new Color(230, 205, 175));
        mapSpecialtiesButton.setFont(Resources.getFont("NoitaBlackletter", 64));
        navPanel.add(howToPlayButton, BorderLayout.NORTH);
        navPanel.add(gameFlowButton, BorderLayout.CENTER);
        navPanel.add(mapSpecialtiesButton, BorderLayout.SOUTH);

        int navPanelWidth = (int) (contentPanel.getWidth() * 0.3);      // Soucis pour 30%
        navPanel.setPreferredSize(new Dimension(navPanelWidth, contentPanel.getHeight()));
        contentPanel.add(navPanel, BorderLayout.WEST);

        JTextArea explanationTextArea = new JTextArea("   Bienvenue sur Board's Adventure !\n\n"
            + "Si vous avez besoin d'informations sur les différents aspects de ce jeu"
            + ", veuillez jeter un oeil aux rubriques d'aides correspondantes ci-contre.");
        explanationTextArea.setLineWrap(true);
        explanationTextArea.setWrapStyleWord(true);
        explanationTextArea.setEditable(false);
        explanationTextArea.setBackground(new Color(255, 240, 200));
        explanationTextArea.setFont(Resources.getFont("NoitaBlackletter", 32));
        JScrollPane scrollPane = new JScrollPane(explanationTextArea);

        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                scrollPane.setViewportView(explanationTextArea);
                menuPanel.removeComp(layer);
            }
        });

        int scrollPaneWidth = (int) (contentPanel.getWidth() * 0.7);    // Soucis pour 70%
        scrollPane.setPreferredSize(new Dimension(scrollPaneWidth, contentPanel.getHeight()));
        scrollPane.setViewportBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        scrollPane.setBackground(new Color(230, 205, 175));
        contentPanel.add(scrollPane, BorderLayout.EAST);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        panel.add(mainPanel);

        // LISTENERS, voir adaption texte/rapport au resize
        howToPlayButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                scrollPane.setViewportView(explanationTextArea);
                explanationTextArea.setText("     - PRISE EN MAIN DU LOGICIEL -\n\n"
                + "Sur le menu du jeu, vous pouvez modifier les réglages sonores en cliquant sur \"Options\" "
                + "ou vous pouvez vous lancer directement dans une partie en appuyant sur \"Jouer\".\n"
                + "Une fois dans ce menu, vous pouvez choisir de rejoindre une partie déjà créée ou d'en être l'hôte."
                + "Vous pourrez alors configurer la partie comme cela vous convient le mieux !");
            }
        });
        gameFlowButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                scrollPane.setViewportView(explanationTextArea);
                explanationTextArea.setText("   Comment se déroule une partie de Board's Adventure ?\n\n"
                + "1 - Début de tour: Faites constat des évènements qui prennent effet avant votre tour. \n\n"
                + "2 - Lancer de dé: Sélectionnez et lancez le dé que vous allez jouer à ce tour. "
                + "Différents types de dés peuvent être obtenus au cours de la partie.\n\n"
                + "3 - Déplacement: Déplacez votre pion à la hauteur du résultat,"
                + "et choisissez quelle direction emprunter si le chemin vous l'impose. \n\n "
                + "4 - Action: L'effet de la case sur laquelle vous atterrissez prend effet. "
                + "Laissez la chance décider de votre sort avec les évènements, "
                + "ou faites l'acquisition des propriétés du plateau afin d'affirmer votre monopole sur les autres.");
            }
        });
        mapSpecialtiesButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                explanationTextArea.setText("     - Les différentes cases sur le plateau de jeu -\n "
                + "Veuillez recharger la fenêtre du manuel afin d'afficher la liste des différentes cases du jeu.");
                JPanel mapPanel = new JPanel(new GridLayout(8, 1));

                JPanel gainCase = createCaseExplanationPanel("gain", "Bonus: Vous fait gagner X or.", 10);
                gainCase.setBackground(new Color(255, 240, 200));
                mapPanel.add(gainCase);  // gain
                JPanel lossCase = createCaseExplanationPanel("loss", "Malus: Vous fait perdre X or.", 10);
                lossCase.setBackground(new Color(255, 240, 200));
                mapPanel.add(lossCase);  // loss
                JPanel dangerCase = createCaseExplanationPanel("danger",
                    "Danger: Un événement dangereux pourrait arriver oulala...", 10);
                    dangerCase.setBackground(new Color(255, 240, 200));
                mapPanel.add(dangerCase);    // danger
                JPanel intersectionCase = createCaseExplanationPanel("intersection",
                    "Intersection: Choissisez entre deux chemins à emprunter afin de continuer votre route.", 10);
                    intersectionCase.setBackground(new Color(255, 240, 200));
                mapPanel.add(intersectionCase);    // intersection
                JPanel kingdomCase = createCaseExplanationPanel("kingdom",
                    "Kingdom: Un événement spécial, battez-vous pour prendre possession du trône et de la partie !",
                        10);
                        kingdomCase.setBackground(new Color(255, 240, 200));
                mapPanel.add(kingdomCase);    // kingdom
                JPanel propertyCase = createCaseExplanationPanel("property",
                    "Propriété: Achetez une propriété afin d'avoir le contrôle de la case et gagner un revenu.", 10);
                propertyCase.setBackground(new Color(255, 240, 200));
                mapPanel.add(propertyCase);    // property
                JPanel shopCase = createCaseExplanationPanel("shop",
                    "Boutique: Vous permet d'acheter des objets ou des dés spéciaux en échange de votre or.", 10);
                shopCase.setBackground(new Color(255, 240, 200));
                mapPanel.add(shopCase);    // shop
                JPanel voidCase = createCaseExplanationPanel("void",
                    "Vide: Une case vide, sur laquelle rien ne se passe.", 10);
                voidCase.setBackground(new Color(255, 240, 200));
                mapPanel.add(voidCase);    // void
                JPanel cardCase = createCaseExplanationPanel("card",
                "Carte: Faites parler votre chance et tirez une carte"
                    + "ce qui pourrait vous avantager (ou non) au cours de votre périple.", 10);
                cardCase.setBackground(new Color(255, 240, 200));
                mapPanel.add(cardCase);

                JViewport viewport = scrollPane.getViewport();
                viewport.setView(mapPanel);
            }
        });

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                e.consume();
            }
        });
        panel.setVisible(true);
        return panel;
    }

    private JPanel createCaseExplanationPanel(String img, String description, int imageSize) {
        JPanel casePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 3, 5, 3);

        JLabel iconLabel = new JLabel(new ImageIcon(Resources.getImage(img)));
        casePanel.add(iconLabel, gbc);
        gbc.gridx++;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel descriptionLabel = new JLabel(description);
        descriptionLabel.setFont(Resources.getFont("NoitaBlackletter", 32));
        casePanel.add(descriptionLabel, gbc);
        return casePanel;
    }

}
