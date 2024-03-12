package model.config.players;

import java.util.HashMap;

import model.config.Dice;
import model.config.GameMap;
import model.config.Team;
import model.config.cells.Cell;

public abstract class Player {

    private final int playerID;
    private final String username;
    private final Team team;
    private HashMap<String, Dice> dices;
    private int jailTurn;
    private int mapLap;
    private int position;
    private int previousPosition;
    private int intersectionCellID;

    private int diceRollValue;

    public Player(final int playerID, final String username, final Team team) {
        this.playerID = playerID;
        this.username = username;
        this.team = team;
        initValues();
    }

    private void initValues() {
        dices = new HashMap<>();
        dices.put("normal", new Dice());
        intersectionCellID = 0;
        position = 0;
        mapLap = 0;
        jailTurn = 0;
        previousPosition = 0;
        diceRollValue = 0;
    }

    // region model methods

    public final void rollDice(final String diceName) {
        // dices.get(diceName).rollDice();
        diceRollValue = 1;
        System.out.println(diceRollValue);
    }

    // endregion

    // region increment

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

    // endregion

    // region getters/setters

    public final void setIntersectionCellID(final int intersectionCellID) {
        this.intersectionCellID = intersectionCellID;
    }

    public final int getIntersectionCellID() {
        return intersectionCellID;
    }

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
