package model.config.items;

import java.util.Random;

import model.Model;
import model.Player;

public final class LittleDice extends Item {

    public LittleDice(final ItemName itemName, final boolean isShopItem, final int value) {
        super(itemName, isShopItem, value);
    }

    @Override
    public void playItemEffect(final Player player, final Model model) {
        model.getCurrentPlayer().setDiceRollValue(new Random().nextInt(3) + 1);
        player.setStartItemCooldown(getTurnCooldown());
    }

    @Override
    public boolean canItemBeActivated(final Player player, final Model model) {
        return true;
    }

}
