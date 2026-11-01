package model.config.items;

import java.util.Random;

import model.Model;
import model.Player;

public final class FarmerHoe extends Item {

    public FarmerHoe(final ItemName itemName, final boolean isShopItem, final int value) {
        super(itemName, isShopItem, value);
    }

    @Override
    public void playItemEffect(final Player player, final Model model) {
        Random rand = new Random();
        player.incrementCoins((int) (150 * rand.nextDouble() * 3.0));
        player.setStartItemCooldown(getTurnCooldown());
    }

    @Override
    public boolean canItemBeActivated(final Player player, final Model model) {
        return true;
    }

}
