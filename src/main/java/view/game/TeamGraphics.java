package view.game;

import java.awt.Color;
import java.util.ArrayList;

public final class TeamGraphics {

    private final Color teamColor;
    private final ArrayList<PlayerGraphics> players;

    public TeamGraphics(final Color teamColor, final ArrayList<PlayerGraphics> players) {
        this.teamColor = teamColor;
        this.players = players;
    }

    public Color getTeamColor() {
        return teamColor;
    }

    public ArrayList<PlayerGraphics> getPlayers() {
        return players;
    }
}
