package model.config.cardeffects;

import model.Model;
import model.config.players.Player;

/**
 * Donne 1 point à l'équipe du joueur courant.
 */
public final class GainGamePointEffect extends CardEffect {

    public GainGamePointEffect(int quantity) {
        super(quantity);
    }

    @Override
    public void applyEffect(Player player, Model model) {
        player.getTeam().incrementGamePoint(1);
    }

}
