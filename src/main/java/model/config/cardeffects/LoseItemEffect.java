package model.config.cardeffects;

import java.util.Random;

import model.Model;
import model.config.players.Player;

/**
 * Fait perdre un objet au joueur courant choisi aléatoirement si possible.
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
