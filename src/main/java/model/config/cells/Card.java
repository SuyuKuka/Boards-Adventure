package model.config.cells;

import model.Model;
import model.Player;
import model.config.cardseffect.CardEffect;

public class Card extends Cell {

    public Card(final int id, final boolean shouldPlayerStop, final CellType cellType, final int nextCellID) {
        super(id, shouldPlayerStop, cellType, nextCellID);
    }

    /**
     * Selectionne aléatoirement une carte dans cards et l'utilise.
     *
     * @param  player
     * @param  model
     */
    @Override
    public void playCellEffect(final Player player, final Model model) {
        System.out.println("sefs");
        final CardEffect cardEffect = model.getGameMap().loadRandomCardEffect();
        cardEffect.applyEffect(player, model);
    }

}
