package model.config.items;

import java.util.Random;

import model.Model;
import model.Player;

public final class BigDice extends Item {

    public BigDice(final ItemName itemName, final boolean isShopItem, final int value) {
        super(itemName, isShopItem, value);
    }

    @Override
    public void playItemEffect(final Player player, final Model model) {
        player.incrementDiceRollValue(new Random().nextInt(10) + 1);
    }

    @Override
    public boolean canItemBeActivated(final Player player, final Model model) {
        return true;
    }
}
