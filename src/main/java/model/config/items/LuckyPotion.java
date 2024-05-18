package model.config.items;

import java.util.Random;

import model.Model;
import model.config.players.Player;

public class LuckyPotion extends Item {

    public LuckyPotion(ItemName itemName, boolean isShopItem, int value) {
        super(itemName, isShopItem, value);
    }

    @Override
    public final void playItemEffect(Player player, Model model) {
        Random rand = new Random();
        int i = rand.nextInt(100);
        if (i < 50) {
            model.skipTurn();
        } else if (i > 50 && i < 80) {
            player.incrementCoins(player.getCoins() / 10);
        }
        player.setStartItemCooldown(getTurnCooldown());
    }

    @Override
    public final boolean canItemBeActivated(Player player, Model model) {
        return true;
    }

}
