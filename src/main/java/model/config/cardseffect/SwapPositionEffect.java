package model.config.cardseffect;

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
        throw new UnsupportedOperationException("Unimplemented method 'applyEffect'");
    }

}
