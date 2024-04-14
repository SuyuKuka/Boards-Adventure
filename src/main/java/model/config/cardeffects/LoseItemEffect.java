package model.config.cardeffects;

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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'applyEffect'");
    }
}
