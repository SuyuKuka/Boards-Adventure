package model.config.items;

import java.util.Random;

import model.Model;
import model.Player;

public class MagicWand extends Item {

    public MagicWand(ItemName itemName, boolean isShopItem, int value) {
        super(itemName, isShopItem, value);
    }

    @Override
    public final void playItemEffect(Player player, Model model) {
        int cell = player.getPosition();
        Player target = null;
        while (target != null && target == player) {
            Random rand = new Random();
            target = model.getPlayers().get(rand.nextInt(model.getPlayers().size()));
        }

        int tmp = target.getPosition();
        player.setPosition(tmp);
        target.setPosition(cell);
        player.setStartItemCooldown(getTurnCooldown());
    }

    @Override
    public final boolean canItemBeActivated(Player player, Model model) {
        return true;
    }

}
