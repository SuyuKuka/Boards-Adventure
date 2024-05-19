package model.config.cardseffect;

import model.Model;
import model.Player;

/**
 * Objet pour carte à effet.
 * Contient une variable {@link #quantity} indiquant le nombre d'exemplaire disponibles pour la carte durant la partie.
 */
public abstract class CardEffect {

    private int quantity;

    public CardEffect(final int quantity) {
        this.quantity = quantity <= 1 ? 1 : quantity;
    }

    public abstract void applyEffect(Player player, Model model);

    public final int getQuantity() {
        return quantity;
    }

    public final void decreaseQuantity() {
        quantity--;
    }
}
