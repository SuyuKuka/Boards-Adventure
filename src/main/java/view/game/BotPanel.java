package view.game;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class BotPanel extends JPanel {

    public BotPanel() {
        initComponents();
        setOpaque(false);
        setLayout(null);
        setVisible(true);
    }

    private void initComponents() {
        add(createChatPanel());

    }

    // #region Chat

    private JTabbedPane createChatPanel() {
        JTabbedPane tabs = new JTabbedPane();
        //Définir la position de conteneur d'onglets
        tabs.setBounds(0, 0, 300, 300);


        tabs.add("Global", createGlobalChatPanel());
        tabs.add("Equipe", createTeamChatPanel());
        tabs.add("Systeme", createSystemChatPanel());
        return tabs;
    }

    private JPanel createGlobalChatPanel() {

    }

    private JPanel createTeamChatPanel() {

    }

    private JPanel createSystemChatPanel() {

    }

    // #endregion

    // #region Inventory


    // #endregion

    // #region Action

    // #endregion

    // #region MiniMap

    //#endregion
}
