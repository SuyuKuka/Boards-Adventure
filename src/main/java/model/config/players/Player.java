package model.config.players;

import java.util.HashMap;
import java.util.Random;

import model.config.Dice;
import model.config.Team;

public abstract class Player {
    private static final Random RAND = new Random();

    private int playerID;
    private String username;
    private Team team;
    private HashMap<String, Dice> dices = new HashMap<>();
    private int jailTurn = 0;
    private int mapLap = 0;
    private int position = 0;
    private int previousPosition = 0;

    public Player(final int playerID, final String username, final Team team) {
        this.playerID = playerID;
        this.username = username;
        this.team = team;
        dices.put("normal", new Dice());
    }

    public final int rollDice() {
        return dices.get("normal").rollDice();
    }

    public final void incrementPosition(final int increment) {
        position += increment;
    }

    public final void incrementJailTurn(final int increment) {
        jailTurn += increment;
    }

    public final void incrementMapLap(final int increment) {
        mapLap += increment;
    }

    // region getters/setters

    public final Team getTeam() {
        return team;
    }

    public final String getUsername() {
        return username;
    }


    public final int getJailTurn() {
        return jailTurn;
    }

    public final void setPosition(final int position) {
        this.position = position;
    }

    public final int getPosition() {
        return position;
    }

    public final int getMapLap() {
        return mapLap;
    }

    public final void setPreviousPosition(final int previousPosition) {
        this.previousPosition = previousPosition;
    }

    public final int getPreviousPosition() {
        return previousPosition;
    }

    public final boolean isJailed() {
        return jailTurn > 0;
    }

    public final int getPlayerID() {
        return playerID;
    }

    // endregion

}
