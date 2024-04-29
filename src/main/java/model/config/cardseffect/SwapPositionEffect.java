package model.config.cardseffect;

import java.util.Random;

import model.Model;
import model.config.players.Player;

/**
 * Echange la position du joueur courant avec un autre joueur choisi aléatoirement.
 */
public final class SwapPositionEffect extends CardEffect {

    public SwapPositionEffect(final int quantity) {
        super(quantity);
    }

    @Override
    public void applyEffect(final Player player, final Model model) {
        Random rand = new Random();
        Player swaped = model.getPlayers().get(rand.nextInt(model.getPlayers().size()));

        int tmp = player.getPosition();
        player.setPosition(swaped.getPosition());
        swaped.setPosition(tmp);
    }

    @Override
    public String toString() {
        return "Echange la position du joueur courant avec un autre joueur choisi aléatoirement.";
    }

}
