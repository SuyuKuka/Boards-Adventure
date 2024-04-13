package model.config.players;

import java.util.HashMap;

import model.config.Dice;
import model.config.GameMap;
import model.config.Item;
import model.config.Team;
import model.config.cells.Cell;

public abstract class Player {

    private final int playerID;
    private final String username;
    private final Team team;
    private final Item startItem;
    private final HashMap<String, Dice> dices;

    private int jailTurn;
    private int mapLap;
    private int position;
    private int previousPosition;
    private int intersectionCellID;

    private int diceRollValue;

    public Player(final int playerID, final String username, final Team team, final Item startItem) {
        this.playerID = playerID;
        this.username = username;
        this.team = team;
        this.startItem = startItem;
        dices = new HashMap<>();
        initValues();
    }

    private void initValues() {
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
        // dices.get("normal").rollDice()
        diceRollValue = 28;
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
