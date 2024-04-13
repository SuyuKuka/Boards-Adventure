package model.config.cards;

import model.Model;
import model.config.players.Player;

public class MoneyEffect extends CardEffect {
    private int amount;

    public MoneyEffect(int amount) {
        this.amount = amount;
    }

    /**
     * Gagne ou perds de l'argent.
     *
     * @param  player  Le joueur au quel l'effet s'applique
     * @param  model   Le model qui contient toutes les informations du modeles
     */
    @Override
    public void applyEffect(Player player, Model model) {
        player.getTeam().incrementMoney(amount);
    }
}
