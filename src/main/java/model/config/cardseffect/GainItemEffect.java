package model.config.cardseffect;

import java.util.Random;

import model.Model;
import model.config.items.Item;
import model.config.players.Player;

/**
 * Fait gagner au joueur courant l'objet de nom {@link #itemName} si rien ne le contraint.
 */
public final class GainItemEffect extends CardEffect {

    public GainItemEffect(final int quantity) {
        super(quantity);
    }

    @Override
    public void applyEffect(final Player player, final Model model) {
        Random rand = new Random();
        Item item = model.getGameMap().getShopItems().get(rand.nextInt(model.getGameMap().getShopItems().size()));
        if (player.hasShopItem()) {
            player.tooMuchShopItem(item);
        } else {
            player.setShopItem(item);
        }
    }

    @Override
    public String toString() {
        return "Fait gagner au joueur courant l'objet de nom si rien ne le contraint.";
    }
}
