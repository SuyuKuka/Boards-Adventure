package model.config.cardseffect;

import model.Model;
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
        player.setShopItem(null);
    }

    @Override
    public String toString() {
        return "Fait perdre l'objet de shop du joueur si il en possède un.";
    }

}
