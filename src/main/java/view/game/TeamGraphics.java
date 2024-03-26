package view.game;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JPanel;

import model.config.Team;

public class TeamGraphics extends JPanel {
    private static final ArrayList<Color> COLORS = new ArrayList<>();

    private Team team;


    static {
        COLORS.add(Color.RED);
        COLORS.add(Color.BLUE);
        COLORS.add(Color.GREEN);
        COLORS.add(Color.YELLOW);
    }
}
