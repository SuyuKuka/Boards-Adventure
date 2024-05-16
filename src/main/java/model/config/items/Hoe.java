package model.config.items;

import model.Model;
import model.config.players.Player;

public final class Hoe extends Item {

    public Hoe(final ItemName itemName, final boolean isShopItem, final int value) {
        super(itemName, isShopItem, value);
    }

    @Override
    public void playItemEffect(final Player player, final Model model) {
        player.incrementMoney(150);
        player.setStartItemCooldown(getTurnCooldown());
    }

    @Override
    public boolean canItemBeActivated(final Player player, final Model model) {
        return true;
    }

}
