package model.config.items;

import org.json.JSONObject;

public final class ItemFactory {
    public static Item createItem(final JSONObject jObject, final boolean isShopItem) {
        final ItemName itemName = ItemName.valueOf(jObject.getString("name").toUpperCase());
        final int value = isShopItem ? jObject.getInt("buyPrice") : jObject.getInt("turnCooldown");
        switch (itemName) {
            case HOE:
                return new Hoe(itemName, isShopItem, value);
            case LITTLE_DICE:
                return new LittleDice(itemName, isShopItem, value);
            default:
                break;
        }
        return null;
    }

}
