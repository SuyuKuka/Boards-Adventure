package model.config.items;
import java.util.Random;

import model.Model;
import model.Player;

public final class GoldenRoad extends Item {

    public GoldenRoad(final ItemName itemName, final boolean isShopItem, final int value) {
        super(itemName, isShopItem, value);
    }

    @Override
    public void playItemEffect(final Player player, final Model model) {
        player.incrementCoins(new Random().nextInt(1000) + 1);
    }

    @Override
    public boolean canItemBeActivated(final Player player, final Model model) {
        return true;
    }
}
