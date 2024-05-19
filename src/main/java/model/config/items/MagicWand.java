package model.config.items;

import model.Model;
import model.Player;

public class MagicWand extends Item {

    public MagicWand(ItemName itemName, boolean isShopItem, int value) {
        super(itemName, isShopItem, value);
    }

    @Override
    public final void playItemEffect(Player player, Model model) {
        player.setStartItemCooldown(getTurnCooldown());
    }

    @Override
    public final boolean canItemBeActivated(Player player, Model model) {
        return true;
    }

}
