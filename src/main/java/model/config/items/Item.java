package model.config.items;

import model.Model;
import model.Player;

/**
 * Deux types d'Item sont disponibles dans le jeu :
 * <p> Les items venant du shop (qui ont un prix et à usage unique)
 * et les items start (ont un cooldown et sont à usage illimité).
 * Certains items ont des conditions d'activations et les items ne peuvent être activés
 * que durant le tour du joueur (au moment de jouer son dé).
 */
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

    /**
     * Joue l'effet de l'{@link Item} après avoir rempli ses conditions d'activations et que
     * {@link Player#getStartItemCooldown()} soit égal à 0.
     * @param player
     * @param model
     */
    public abstract void playItemEffect(Player player, Model model);

    /**
     * Donne les conditions d'activations de l'{@link Item}.
     * @param player
     * @param model
     * @return {@link boolean}
     */
    public abstract boolean canItemBeActivated(Player player, Model model);

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
