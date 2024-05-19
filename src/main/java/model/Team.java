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

    public int getPlayersStars(final ArrayList<Player> players) {
        int stars = 0;
        for (int i = 0; i < playerIDs.size(); i++) {
            stars += players.get(playerIDs.get(i)).getStars();
        }
        return stars;
    }

    public int getPlayersPropertiesNb(final ArrayList<Player> players) {
        int propertiesNb = 0;
        for (int i = 0; i < playerIDs.size(); i++) {
            propertiesNb += players.get(playerIDs.get(i)).getProperties().size();
        }
        return propertiesNb;
    }

    public int getPlayersCoins(final ArrayList<Player> players) {
        int coins = 0;
        for (int i = 0; i < playerIDs.size(); i++) {
            coins += players.get(playerIDs.get(i)).getCoins();
        }
        return coins;
    }

    public ArrayList<Integer> getPlayerIDs() {
        return playerIDs;
    }

    // endregion
}
