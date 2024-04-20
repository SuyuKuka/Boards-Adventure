package model.config.cardseffect;

import java.util.Random;

import model.Model;
import model.config.items.ItemName;
import model.config.players.Player;

/**
 * Fait gagner au joueur courant l'objet de nom {@link #itemName} si rien ne le contraint.
 */
public final class GainItemEffect extends CardEffect {

    private final ItemName itemName;

    public GainItemEffect(final int quantity) {
        super(quantity);
    }

    @Override
    public void applyEffect(final Player player, final Model model) {
        Random rand = new Random();
        if (player.hasExceedItems()) {
            player.removeItem(0);
        } else {
            player.getItems().add(model.getGameMap().getItems().get(
                rand.nextInt(model.getGameMap().getItems().size())));
        }
    }
}
