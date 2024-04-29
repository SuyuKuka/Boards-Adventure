package model.config.items;

import model.Model;

public final class Hoe extends Item {

    public Hoe(final ItemName itemName, final boolean isShopItem, final int value) {
        super(itemName, isShopItem, value);
    }

    @Override
    public void playItemEffect(Model model) {
        throw new UnsupportedOperationException("Unimplemented method 'playItemEffect'");
    }

}
