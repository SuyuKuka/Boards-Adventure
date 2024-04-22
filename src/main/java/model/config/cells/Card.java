package model.config.cells;

import java.util.Random;

import model.Model;
import model.config.cardseffect.CardEffect;
import model.config.players.Player;

public class Card extends Cell {

    private CardEffect cardEffect;

    public Card(final int id, final boolean shouldPlayerStop, final CellType cellType, final int nextCellID) {
        super(id, shouldPlayerStop, cellType, nextCellID);
    }

    /**
     * Selectionne aléatoirement une carte dans cards et l'utilise.
     *
     * @param  player  the player to apply the effect to
     * @param  model   the game model
     */
    @Override
    public void playCellEffect(Player player, Model model) {
    }

    public final void showCard(Player player, Model model) {
        System.out.println(model.getGameMap().getCardEffects().size());
        Random rand = new Random();
        CardEffect card = model.getGameMap().getCardEffects().get(
            rand.nextInt(model.getGameMap().getCardEffects().size()));
        model.getGameMap().getCardEffects().remove(card);
        card.applyEffect(player, model);
        this.cardEffect = card;
        System.out.println(card.toString());
        System.out.println(model.getGameMap().getCardEffects().size());
    }

    public final CardEffect getCardEffect() {
        return cardEffect;
    }

}
