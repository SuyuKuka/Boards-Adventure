package model.config.cardseffect;

import model.Model;
import model.config.players.Player;

/**
 * Ajoute ou diminue l'argent que possède l'équipe du joueur par {@link #amount}.
 */
public final class MoneyEffect extends CardEffect {

    private int amount;

    public MoneyEffect(final int quantity, final int amount) {
        super(quantity);
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

    public int getAmount() {
        return amount;
    }
}
