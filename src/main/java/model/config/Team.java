package model.config;

import java.util.ArrayList;

import model.config.players.Player;

public final class Team {

    private static int teamIDIterator = 0;

    private final int teamID;
    private final String teamName;
    private final ArrayList<Player> players;

    public Team(final String teamName) {
        this.teamID = teamIDIterator;
        teamIDIterator++;
        this.teamName = teamName;
        this.players = new ArrayList<>();
    }

    public void addPlayer(final Player player) {
        players.add(player);
    }

    // region getters/setters

    public int getTeamID() {
        return teamID;
    }

    public String getTeamName() {
        return teamName;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void resetTeamIDIterator() {
        teamIDIterator = 0;
    }

    // endregion
}
