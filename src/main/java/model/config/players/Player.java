package model.config.players;

import static game.Game.RAND;

import java.util.ArrayList;

import model.config.GameMap;
import model.config.Team;
import model.config.cardseffect.CardEffect;
import model.config.cells.Cell;
import model.config.cells.Property;
import model.config.items.Item;

public abstract class Player {

    private static int playerIDIterator = 0;

    private final int playerID;
    private final String username;
    private final Team team;
    private final Item startItem;
    private final ArrayList<CardEffect> cards = new ArrayList<>();
    private final Item[] shopItem = new Item[3];
    private final String className;

    private final ArrayList<Property> properties = new ArrayList<>();
    private int money = 500;
    private int gamePoint = 0;
    private int rank = 1;
    private int jailTurn = 0;
    private int mapLap = 0;
    private int position = 0;
    private int previousPosition = 0;

    private int diceRollValue = 0;

    public Player(final String username, final String className, final Team team, final Item startItem) {
        this.playerID = playerIDIterator;
        playerIDIterator++;
        this.className = className;
        this.username = username;
        this.team = team;
        this.startItem = startItem;
    }

    // region model methods

    public final void rollDice() {
        diceRollValue = RAND.nextInt(6) + 1;
    }

    public final void addProperty(final Property property) {
        properties.add(property);
    }

    public final void removeProperty(final Property property) {
        properties.remove(property);
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

    public final void incrementGamePoint(final int increment) {
        this.gamePoint += increment;
    }

    public final void incrementMoney(final int increment) {
        this.money += increment;
    }

    // endregion

    // region getters/setters

    public final boolean hasEnoughMoney(final int price) {
        return this.money >= price;
    }

    public final Cell getPlayerCell(final GameMap gameMap) {
        return gameMap.getCell(position);
    }

    public final void setDiceRollValue(final int diceRollValue) {
        this.diceRollValue = diceRollValue;
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

    public final ArrayList<Property> getProperties() {
        return properties;
    }

    public final void setJailTurn(final int jailTurn) {
        this.jailTurn = jailTurn;
    }

    public final int getRank() {
        return rank;
    }

    public final int getJailTurn() {
        return jailTurn;
    }

    public final Item getStartItem() {
        return startItem;
    }

    public final Item[] getShopItem() {
        return shopItem;
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

    public final int getMoney() {
        return money;
    }

    public final int getGamePoint() {
        return gamePoint;
    }

    public final ArrayList<CardEffect> getCards() {
        return cards;
    }

    public final void addShopItem(final Item item) {
        for (int i = 0; i < shopItem.length; i++) {
            if (shopItem[i] == null) {
                shopItem[i] = item;
            }
        }
    }

    public final boolean hasShopItem() {
        return shopItem != null;
    }

    public final void resetPlayerIDIterator() {
        playerIDIterator = 0;
    }

    // endregion

}
