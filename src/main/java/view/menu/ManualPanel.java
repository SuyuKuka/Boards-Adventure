package view.menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
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
        Dimension size = panel.getSize();
        int imgSize = 40;
        panel.setOpaque(false);
        panel.setBackground(new Color(205, 173, 142));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.ORANGE);
        mainPanel.setBorder(BorderFactory.createLineBorder(new Color(205, 173, 142), 4));

        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("  Manuel", SwingConstants.CENTER);
        titleLabel.setFont(Resources.getFont(64));
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

        JPanel navPanel = new JPanel(new GridLayout(4, 1));
        navPanel.setOpaque(false);
        JButton playButton = SwingFactory.createTextButtonManual(this, "Prise en main du jeu", 100, 100, 64, false);
        menuPanel.handCursor(playButton);
        navPanel.add(playButton);

        JButton gameButton = SwingFactory.createTextButtonManual(this, "Déroulement de la partie", 100, 100, 64, false);
        menuPanel.handCursor(gameButton);
        navPanel.add(gameButton);

        JButton cellButton = SwingFactory.createTextButtonManual(this, "Spécificité des cellules",
            100, 100, 64, false);
        menuPanel.handCursor(cellButton);
        navPanel.add(cellButton);

        JButton itemButton = SwingFactory.createTextButtonManual(this, "Spécificité des objets", 100, 100, 64, false);
        menuPanel.handCursor(itemButton);
        navPanel.add(itemButton);

        int navPanelWidth = (int) (contentPanel.getWidth() * 0.3);
        navPanel.setPreferredSize(new Dimension(navPanelWidth, contentPanel.getHeight()));
        contentPanel.add(navPanel, BorderLayout.WEST);

        JTextArea explanationTextArea = new JTextArea("   Bienvenue sur Board's Adventure !\n\n"
            + "Si vous avez besoin d'informations sur les différents aspects de ce jeu"
            + ", veuillez jeter un oeil aux rubriques d'aides correspondantes ci-contre.");
        explanationTextArea.setLineWrap(true);
        explanationTextArea.setWrapStyleWord(true);
        explanationTextArea.setEditable(false);
        explanationTextArea.setBackground(new Color(255, 240, 200));
        explanationTextArea.setFont(Resources.getFont(32));
        JScrollPane scrollPane = new JScrollPane(explanationTextArea);

        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                scrollPane.setViewportView(explanationTextArea);
                menuPanel.removeComp(layer);
            }
        });

        int scrollPaneWidth = (int) (contentPanel.getWidth() * 0.7);
        scrollPane.setPreferredSize(new Dimension(scrollPaneWidth, contentPanel.getHeight()));
        scrollPane.setViewportBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        scrollPane.setBackground(new Color(230, 205, 175));
        contentPanel.add(scrollPane, BorderLayout.EAST);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        panel.add(mainPanel);

        // LISTENERS, voir adaption texte/rapport au resize
        playButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                scrollPane.setViewportView(explanationTextArea);
                explanationTextArea.setText("   PRISE EN MAIN DU LOGICIEL\n\n"
                + "Sur le menu du jeu, vous pouvez modifier les réglages sonores en cliquant sur \"Options\" "
                + "ou vous pouvez vous lancer directement dans une partie en appuyant sur \"Jouer\".\n"
                + "Une fois dans ce menu, vous pouvez commencer une partie. Vous pourrez alors configurer la "
                + "partie comme cela vous convient le mieux et choisir votre personnage et votre objet de départ!");
            }
        });

        gameButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                scrollPane.setViewportView(explanationTextArea);
                explanationTextArea.setText("   Comment se déroule une partie de Board's Adventure ?\n\n"
                + "1 - Début de tour: Faites constat des évènements qui prennent effet avant votre tour. \n\n"
                + "2 - Action du joueur: Vous avez la possitbiliter de lancer le dé ou d'utiliser vos objets.\n\n"
                + "3 - Déplacement: Déplacez votre pion à la hauteur du résultat,"
                + "et choisissez quelle direction emprunter si le chemin vous l'impose. \n\n "
                + "4 - Action: L'effet de la case sur laquelle vous atterrissez prend effet. "
                + "Laissez la chance décider de votre sort avec les évènements, "
                + "ou faites l'acquisition des propriétés du plateau afin d'affirmer votre monopole sur les autres.");
            }
        });

        cellButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                explanationTextArea.setText("     - Les différentes cases sur le plateau de jeu -\n "
                    + "Veuillez recharger la fenêtre du manuel afin d'afficher la liste des différentes cases du jeu.");
                JPanel mapPanel = new JPanel(new GridLayout(10, 1));

                JPanel starsCase = createImageDesc("star", "Etoile : Vous obtenez 1 point en plus.", imgSize, size);
                starsCase.setBackground(new Color(255, 240, 200));
                mapPanel.add(starsCase);

                JPanel gainCase = createImageDesc("gain", "Bonus: Vous fait gagner X coins.", imgSize, size);
                gainCase.setBackground(new Color(255, 240, 200));
                mapPanel.add(gainCase);

                JPanel lossCase = createImageDesc("loss", "Malus: Vous fait perdre X coins.", imgSize, size);
                lossCase.setBackground(new Color(255, 240, 200));
                mapPanel.add(lossCase);

                JPanel dangerCase = createImageDesc("danger", "Danger: Un événement dangereux "
                    + "pourrait arriver oulala... (Regardez votre argent...)", imgSize, size);
                dangerCase.setBackground(new Color(255, 240, 200));
                mapPanel.add(dangerCase);

                JPanel intersectionCase = createImageDesc("intersection", "Intersection: Choissisez entre deux chemins "
                    + "à emprunter afin de continuer votre route.", imgSize, size);
                 intersectionCase.setBackground(new Color(255, 240, 200));
                mapPanel.add(intersectionCase);

                JPanel propertyCase = createImageDesc("property", "Propriété: Achetez une propriété afin d'avoir "
                    + "le contrôle de la case et gagner un revenu.", imgSize, size);
                propertyCase.setBackground(new Color(255, 240, 200));
                mapPanel.add(propertyCase);

                JPanel shopCase = createImageDesc("shop", "Boutique: Vous permet d'acheter parmi "
                    + "3 objets spéciaux et aléatoire en échange de vos coins.", imgSize, size);
                shopCase.setBackground(new Color(255, 240, 200));
                mapPanel.add(shopCase);

                JPanel voidCase = createImageDesc("void",
                    "Vide: Une case vide, sur laquelle rien ne se passe.", imgSize, size);
                voidCase.setBackground(new Color(255, 240, 200));
                mapPanel.add(voidCase);

                JPanel cardCase = createImageDesc("card",
                    "Carte: Faites parler votre chance et tirez une carte"
                    + "ce qui pourrait vous avantager (ou non) au cours de votre périple.", imgSize, size);
                cardCase.setBackground(new Color(255, 240, 200));
                mapPanel.add(cardCase);

                JPanel jailCase = createImageDesc("jail",
                    "Prison: Vous pouvez entrer dans la prison, quel dommage....", imgSize, size);
                jailCase.setBackground(new Color(255, 240, 200));
                mapPanel.add(jailCase);

                JViewport viewport = scrollPane.getViewport();
                viewport.setView(mapPanel);
            }
        });

        itemButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                explanationTextArea.setText("     - Les différentes objets du jeu -\n "
                    + "Veuillez recharger la fenêtre du manuel afin d'afficher la liste des différentes cases du jeu.");
                JPanel mapPanel = new JPanel(new GridLayout(7, 1));

                JPanel bigDice = createImageDesc("big_dice", "Big Dice: Incrémente la "
                    + "valeur de votre dé par le big dice, un chiffre compris entre 1 et 12.", imgSize, size);
                bigDice.setBackground(new Color(255, 240, 200));
                mapPanel.add(bigDice);

                JPanel farmerHoe = createImageDesc("farmer_hoe",
                    "Farmer Hoe: Vous allez cultivez du blé, ce qui vous rapporte de l'argent.", imgSize, size);
                farmerHoe.setBackground(new Color(255, 240, 200));
                mapPanel.add(farmerHoe);

                JPanel goldenRoad = createImageDesc("golden_road", "Golden Road: Vous avez acheté un bloc mystère ! "
                + "Vous gagnez un point supplémentaire.", imgSize, size);
                goldenRoad.setBackground(new Color(255, 240, 200));
                mapPanel.add(goldenRoad);

                JPanel littleDice = createImageDesc("little_dice",
                    "Little Dice: Incrémente la valeur de votre dé par le little dice, "
                    + "un chiffre compris entre 1 et 3.    ", imgSize, size);
                littleDice.setBackground(new Color(255, 240, 200));
                mapPanel.add(littleDice);

                JPanel luckyPotion = createImageDesc("lucky_potion",
                    "Lucky Potion: Vous avez 50 pourcent de chances de faire perdre un certain montent à un "
                    + "joueur au hasard y compris vous et 30 pourcent de chance de gagner de l'argent.", imgSize, size);
                luckyPotion.setBackground(new Color(255, 240, 200));
                mapPanel.add(luckyPotion);

                JPanel magicWand = createImageDesc("magic_wand",
                    "Magic Wand: Echange votre position avec un autre joueur de manière aléatoire.", imgSize, size);
                magicWand.setBackground(new Color(255, 240, 200));
                mapPanel.add(magicWand);

                JPanel sacredBook = createImageDesc("sacred_book",
                    "Sacred Book: Si vous êtes sur une case propriété, vous pouvez la détruire, "
                    + "mais vous perdez 25% de vos coins.", imgSize, size);
                sacredBook.setBackground(new Color(255, 240, 200));
                mapPanel.add(sacredBook);

                JViewport viewport = scrollPane.getViewport();
                viewport.setView(mapPanel);
            }
        });

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                e.consume();
            }
        });
        panel.setVisible(true);
        return panel;
    }

    private JPanel createImageDesc(String img, String description, int imageSize, Dimension size) {
        JPanel casePanel = new JPanel();
        casePanel.setSize(new Dimension(size.width, size.height / 8));
        casePanel.setPreferredSize(new Dimension(size.width, size.height / 6));
        casePanel.setLayout(new BoxLayout(casePanel, BoxLayout.X_AXIS));

        JLabel iconLabel = SwingFactory.createImageLabel(casePanel, img, imageSize, imageSize, false);
        casePanel.add(iconLabel);

        JLabel descriptionLabel = SwingFactory.createTextLabel(description, Color.BLACK, 32);
        casePanel.add(descriptionLabel);
        return casePanel;
    }

}
