package model.config.cardseffect;

import model.Model;
import model.Player;

/**
 * Donne 1 point à l'équipe du joueur courant.
 */
public final class GainGamePointEffect extends CardEffect {

    public GainGamePointEffect(int quantity) {
        super(quantity);
    }

    @Override
    public void applyEffect(Player player, Model model) {
        player.incrementGamePoint(1);
    }

    @Override
    public String toString() {
        return "Donne 1 point à l'équipe du joueur courant.";
    }

}
