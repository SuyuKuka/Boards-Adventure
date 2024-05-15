package model.config.items;

import model.Model;

public abstract class Item {

    private final ItemName itemName;

    private final boolean isShopItem;
    // Shop Item
    private final int buyPrice;
    // Starter Item
    private int turnCooldown;
    
    private final int baseCooldown;

    public Item(final ItemName itemName, final boolean isShopItem, final int value) {
        this.itemName = itemName;
        this.isShopItem = isShopItem;
        if (isShopItem) {
            buyPrice = value;
            turnCooldown = -1;
        } else {
            buyPrice = -1;
            turnCooldown = 0;
            baseCooldown = value;
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

    public final int getBaseCooldown() {
        return baseCooldown;
    }

    public void resetTurnCooldown(){
        turnCooldown=baseCooldown;
    }


    // #endregion

    public abstract Item getCopy();

}
