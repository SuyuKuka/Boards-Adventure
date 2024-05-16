package model.config.players;

import static game.Game.RAND;

import java.util.ArrayList;

import model.Model;
import model.config.cardseffect.CardEffect;
import model.config.cells.Cell;
import model.config.cells.Property;
import model.config.items.Item;

public abstract class Player {

    private static final int MAX_ITEM_SHOP = 3;
    private static int itemShopNb;
    private static int playerIDIterator = 0;

    private final int id;
    private final String name;
    private final String hexTeamColor;
    private final Item startItem;
    private final ArrayList<CardEffect> cards = new ArrayList<>();
    private final Item[] shopItem;
    private final String className;

    private final ArrayList<Property> properties = new ArrayList<>();
    private int money = 500;
    private int gamePoint = 0;
    private int rank = 1;
    private int jailTurn = 0;
    private int mapLap = 0;
    private int position;
    private int previousPosition = 0;

    private int startItemCooldown = 0;

    private int diceRollValue = 0;

    public Player(final String name, final String className, final String hexTeamColor,
            final Item startItem, final int startPos) {
        this.id = playerIDIterator;
        playerIDIterator++;
        shopItem = new Item[itemShopNb];
        this.className = className;
        this.name = name;
        this.hexTeamColor = hexTeamColor;
        this.startItem = startItem;
        this.position = startPos;
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

    public final Cell getPlayerCell(final Model model) {
        return model.getGameMap().getCell(position);
    }

    public final void setDiceRollValue(final int diceRollValue) {
        this.diceRollValue = diceRollValue;
    }

    public final int getDiceRollValue() {
        return diceRollValue;
    }

    public final String getHexTeamColor() {
        return hexTeamColor;
    }

    public final String getName() {
        return name;
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

    public final String getClassName() {
        return className;
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

    public final int getID() {
        return id;
    }

    public final int getMoney() {
        return money;
    }

    public final void setMoney(final int money) {
        this.money = money;
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

    public static final void resetPlayerIDIterator() {
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

    public final boolean canUseStartItem() {
        return startItemCooldown == 0;
    }

    public final void setStartItemCooldown(final int startItemCooldown) {
        this.startItemCooldown = startItemCooldown;
    }

    public final void decrementStartItemCooldown() {
        if (startItemCooldown > 0) {
            startItemCooldown--;
        }
    }

    public final int getStartItemCooldown() {
        return startItemCooldown;
    }

    // endregion

}
