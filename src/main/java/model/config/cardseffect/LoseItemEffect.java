package model.config.cardseffect;

import model.Model;
import model.config.items.Item;
import model.config.players.Player;

/**
 * Fait perdre l'objet de shop du joueur si il en possède un.
 */
public final class LoseItemEffect extends CardEffect {

    public LoseItemEffect(int quantity) {
        super(quantity);
    }

    @Override
    public void applyEffect(Player player, Model model) {
        final Item[] itemArr = player.getShopItem();
        for (int i = 0; i < itemArr.length; i++) {
            if (itemArr[i] != null) {
                itemArr[i] = null;
                return;
            }
        }
    }

}
