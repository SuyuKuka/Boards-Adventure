package model.config.cells.cards;

import model.Model;
import model.config.players.Player;

public abstract class CardEffect {
    public abstract void applyEffect(Player player, Model model);
// Teleportation sur une certaine case ?
// Gain d'argent                                    ✔
// Perte d'argent                                   ✔
// Tombe sur une propriété (soit achat soit payer)
// Perte d'une propriété                            ✔
// Echanger de position avec un autre joueur
// Gain objet (du shop)
// Perte objet (items de départ non compris)
// Gain d'un point ?
// Prison
// Case départ? (donc gagne 1 point + argent)

}
