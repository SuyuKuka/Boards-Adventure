package model.config.items;

import model.Model;

public abstract class Item {

    private final ItemName itemName;

    private final boolean isShopItem;
    // Shop Item
    private final int buyPrice;
    // Starter Item
    private final int turnCooldown;

    public Item(final ItemName itemName, final boolean isShopItem, final int value) {
        this.itemName = itemName;
        this.isShopItem = isShopItem;
        if (isShopItem) {
            buyPrice = value;
            turnCooldown = -1;
        } else {
            buyPrice = -1;
            turnCooldown = value;
        }
    }

    public abstract void playItemEffect(Model model);

    // #region setter / getter

    public final ItemName getItemName() {
        return itemName;
    }

    public final boolean isShopItem() {
        return isShopItem;
    }

    public final boolean isStarterItem() {
        return !isShopItem;
    }

    public final int getBuyPrice() {
        return buyPrice;
    }

    public final int getTurnCooldown() {
        return turnCooldown;
    }

    // #endregion

}
