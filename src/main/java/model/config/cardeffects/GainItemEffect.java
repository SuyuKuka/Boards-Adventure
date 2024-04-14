package model.config.cardeffects;

import model.Model;
import model.config.players.Player;

/**
 * Fait gagner au joueur courant l'objet de nom {@link #itemName} si rien ne le contraint.
 */
public final class GainItemEffect extends CardEffect {

    private final String itemName;

    public GainItemEffect(int quantity, final String itemName) {
        super(quantity);
        this.itemName = itemName;
    }

    @Override
    public void applyEffect(Player player, Model model) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'applyEffect'");
    }

    public String getItemName() {
        return itemName;
    }
}
