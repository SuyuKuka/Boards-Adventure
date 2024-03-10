package model.config.players;

import java.util.HashMap;

import model.config.Dice;
import model.config.GameMap;
import model.config.Team;
import model.config.cells.Cell;

public abstract class Player {

    private int playerID;
    private String username;
    private Team team;
    private HashMap<String, Dice> dices = new HashMap<>();
    private int jailTurn = 0;
    private int mapLap = 0;
    private int position = 0;
    private int previousPosition = 0;

    private int diceRollValue = 0;

    public Player(final int playerID, final String username, final Team team) {
        this.playerID = playerID;
        this.username = username;
        this.team = team;
        dices.put("normal", new Dice());
    }

    public final void rollDice() {
        diceRollValue = dices.get("normal").rollDice();
    }

    public final void incrementDiceRollValue(final int increment) {
        diceRollValue += increment;
    }

    public final void incrementPosition(final int increment) {
        position += increment;
    }

    public final void incrementMapLap(final int increment) {
        mapLap += increment;
    }

    public final Cell getPlayerCell(final GameMap gameMap) {
        return gameMap.getCell(position);
    }

    // region getters/setters

    public final int getDiceRollValue() {
        return diceRollValue;
    }

    public final Team getTeam() {
        return team;
    }

    public final String getUsername() {
        return username;
    }

    public final void setJailTurn(final int jailTurn) {
        this.jailTurn = jailTurn;
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
