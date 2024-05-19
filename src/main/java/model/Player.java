package model;

import static game.Game.RAND;

import java.util.ArrayList;

import model.config.cardseffect.CardEffect;
import model.config.cells.Cell;
import model.config.cells.Property;
import model.config.items.Item;

public final class Player {

    private static final int MAX_ITEM_SHOP = 3;
    private static int itemShopNb;
    private static int playerIDIterator = 0;

    private final int id;
    private final String name;
    private final boolean isHuman;
    private final String hexTeamColor;
    private final Item startItem;
    private final ArrayList<CardEffect> cards = new ArrayList<>();
    private final ArrayList<Property> properties = new ArrayList<>();
    private final Item[] shopItem;
    private final String className;

    private int coins = 500;
    private int gamePoint = 0;
    private int rank = 1;
    private int jailTurn = 0;
    private int mapLap = 0;
    private int position;
    private int previousPosition = 0;
    private int nextCellID = -1;

    private int startItemCooldown = 0;

    private int diceRollValue = 0;

    public Player(final String name, final boolean isHuman, final String className, final String hexTeamColor,
            final Item startItem, final int startPos) {
        this.id = playerIDIterator;
        playerIDIterator++;
        shopItem = new Item[itemShopNb];
        this.className = className;
        this.name = name;
        this.isHuman = isHuman;
        this.hexTeamColor = hexTeamColor;
        this.startItem = startItem;
        this.position = startPos;
    }

    // region model methods

    public void rollDice() {
        diceRollValue = RAND.nextInt(6) + 1;
        diceRollValue = 4;
    }

    public void addProperty(final Property property) {
        properties.add(property);
    }

    public void removeProperty(final Property property) {
        properties.remove(property);
    }

    // endregion

    // region increment

    public void incrementDiceRollValue(final int increment) {
        diceRollValue += increment;
    }

    public void incrementPosition(final int increment) {
        position += increment;
    }

    public void incrementMapLap(final int increment) {
        mapLap += increment;
    }

    public void incrementGamePoint(final int increment) {
        this.gamePoint += increment;
    }

    public void incrementCoins(final int increment) {
        this.coins += increment;
    }

    // endregion

    // region getters/setters

    public boolean hasEnoughCoins(final int price) {
        return this.coins >= price;
    }

    public Cell getPlayerCell(final Model model) {
        return model.getGameMap().getCell(position);
    }

    public void setDiceRollValue(final int diceRollValue) {
        this.diceRollValue = diceRollValue;
    }

    public int getDiceRollValue() {
        return diceRollValue;
    }

    public String getHexTeamColor() {
        return hexTeamColor;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Property> getProperties() {
        return properties;
    }

    public void setJailTurn(final int jailTurn) {
        this.jailTurn = jailTurn;
    }

    public int getRank() {
        return rank;
    }

    public int getJailTurn() {
        return jailTurn;
    }

    public Item getStartItem() {
        return startItem;
    }

    public Item[] getShopItem() {
        return shopItem;
    }

    public String getClassName() {
        return className;
    }

    public void setPosition(final int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public int getMapLap() {
        return mapLap;
    }

    public void setPreviousPosition(final int previousPosition) {
        this.previousPosition = previousPosition;
    }

    public int getPreviousPosition() {
        return previousPosition;
    }

    public boolean isJailed() {
        return jailTurn > 0;
    }

    public int getID() {
        return id;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getGamePoint() {
        return gamePoint;
    }

    public ArrayList<CardEffect> getCards() {
        return cards;
    }

    public void addShopItem(final Item item) {
        for (int i = 0; i < shopItem.length; i++) {
            if (shopItem[i] == null) {
                shopItem[i] = item;
            }
        }
    }

    public boolean hasShopItem() {
        return shopItem != null;
    }

    public static void resetPlayerIDIterator() {
        playerIDIterator = 0;
    }

    public static void setItemShopNb(final int nbItems) {
        if (nbItems > MAX_ITEM_SHOP) {
            itemShopNb = MAX_ITEM_SHOP;
        } else {
            itemShopNb = nbItems;
        }
    }

    public static int getItemShopNb() {
        return itemShopNb;
    }

    public static int getMaxItemShop() {
        return MAX_ITEM_SHOP;
    }

    public boolean canUseStartItem() {
        return startItemCooldown == 0;
    }

    public void setStartItemCooldown(final int startItemCooldown) {
        this.startItemCooldown = startItemCooldown;
    }

    public void decrementStartItemCooldown() {
        if (startItemCooldown > 0) {
            startItemCooldown--;
        }
    }

    public boolean isHuman() {
        return isHuman;
    }

    public int getStartItemCooldown() {
        return startItemCooldown;
    }

    public int getNextCellID() {
        return nextCellID;
    }

    public void setNextCellID(final int nextCellID) {
        this.nextCellID = nextCellID;
    }

    // endregion

}
