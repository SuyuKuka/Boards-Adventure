package model.config.cells.cards;

import model.Model;
import model.config.players.Player;

public final class CardManagement {

    private CardEffect effect;

    public CardManagement(CardEffect effect) {
        this.effect = effect;
    }

    public void applyEffect(Player player, Model model) {
        effect.applyEffect(player, model);
    }

}
