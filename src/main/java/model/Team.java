package model;

import java.util.ArrayList;

public final class Team {

    private final String hexTeamColor;
    private final ArrayList<Integer> playerIDs;

    public Team(final String hexTeamColor, final ArrayList<Integer> playerIDs) {
        this.hexTeamColor = hexTeamColor;
        this.playerIDs = playerIDs;
    }

    public void addPlayer(final int playerID) {
        playerIDs.add(playerID);
    }

    // region getters/setters

    public String getHexTeamColor() {
        return hexTeamColor;
    }

    // endregion
}
