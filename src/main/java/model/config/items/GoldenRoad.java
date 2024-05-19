package model.config.items;

import model.Model;
import model.Player;

public final class GoldenRoad extends Item {

    public GoldenRoad(final ItemName itemName, final boolean isShopItem, final int value) {
        super(itemName, isShopItem, value);
    }

    @Override
    public void playItemEffect(final Player player, final Model model) {
        player.incrementStars(1);
    }

    @Override
    public boolean canItemBeActivated(final Player player, final Model model) {
        return true;
    }

}
