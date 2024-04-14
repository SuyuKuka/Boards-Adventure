package model.config.cardeffects;

import model.Model;
import model.config.players.Player;

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
        // Teleportation sur une certaine case ?
        // Gain d'argent                                    ✔
        // Perte d'argent                                   ✔
        // Perte d'une propriété                            ✔
        // Echanger de position avec un autre joueur
        // Gain objet (du shop)
        // Perte objet (items de départ non compris)
        // Gain d'un point ?
        // Prison

        // TP exemple :
        // Tombe sur une propriété (soit achat soit payer)
        // Case départ? (donc gagne 1 point + argent)

}
