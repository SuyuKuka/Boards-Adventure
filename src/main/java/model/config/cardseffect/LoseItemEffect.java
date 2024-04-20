package model.config.cardseffect;

import java.util.Random;

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
        if (player.getItems().size() == 0) {
            return;
        } else {
            Random rand = new Random();
            player.getItems().remove(rand.nextInt(player.getItems().size()));
        }

    }
}
