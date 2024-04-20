package model.config.cardseffect;

import model.Model;
import model.config.players.Player;

/**
 * Téléporte le joueur courant sur la cellule {@link #cellID}.
 */
public final class TeleportationEffect extends CardEffect {

    private final int cellID;

    public TeleportationEffect(final int quantity, final int cellID) {
        super(quantity);
        this.cellID = cellID;
    }

    @Override
    public void applyEffect(final Player player, final Model model) {

    }

    public int getCellID() {
        return cellID;
    }

}
