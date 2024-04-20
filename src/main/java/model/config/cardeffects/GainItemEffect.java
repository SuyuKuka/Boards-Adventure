package model.config.cardeffects;

import java.util.Random;

import model.Model;
import model.config.players.Player;

/**
 * Fait gagner au joueur courant l'objet de nom {@link #itemName} si rien ne le contraint.
 */
public final class GainItemEffect extends CardEffect {

    public GainItemEffect(int quantity) {
        super(quantity);
    }

    @Override
    public void applyEffect(Player player, Model model) {
        Random rand = new Random();
        if (player.hasExceedItems()) {
            player.removeItem(0);
        } else {
            player.getItems().add(model.getGameMap().getItems().get(
                rand.nextInt(model.getGameMap().getItems().size())));
        }
    }
}
