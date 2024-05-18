package model.config.items;

import java.util.HashMap;

public final class ItemFactory {

    public static Item createItem(final HashMap<String, String> itemConfig, final boolean isShopItem) {
        final ItemName itemName = ItemName.valueOf(itemConfig.get("name").toUpperCase());
        final int value;
        if (isShopItem) {
            value = Integer.valueOf(itemConfig.get("buyPrice"));
        } else {
            value = Integer.valueOf(itemConfig.get("turnCooldown"));
        }
        switch (itemName) {
            case HOE:
                return new FarmerHoe(itemName, isShopItem, value);
            case LITTLE_DICE:
                return new LittleDice(itemName, isShopItem, value);
            case SACRED_BOOK:
                return new SacredBook(itemName, isShopItem, value);
            case MAGIC_WAND:
                return new MagicWand(itemName, isShopItem, value);
            case LUCKY_POTION:
                return new LuckyPotion(itemName, isShopItem, value);
            default:
                break;
        }
        return null;
    }

}
