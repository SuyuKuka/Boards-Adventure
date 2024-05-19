package model.config.cardseffect;

import java.util.ArrayList;
import java.util.Random;

import model.Model;
import model.Player;
import model.config.cells.Property;

/**
 * Fait perdre une propriété au joueur courant si possible.
 */
public class LossPropertyEffect extends CardEffect {

    public LossPropertyEffect(int quantity) {
        super(quantity);
    }

    /**
     * Le joueur perd une de ces propriétés de manière aléatoire.
     *
     * @param  player  Le joueur au quel l'effet s'applique
     * @param  model   Le model qui contient toutes les informations du modeles
     */
    @Override
    public void applyEffect(final Player player, final Model model) {
        final ArrayList<Property> properties = player.getProperties();
        if (properties.size() == 0) {
            return;
        }
        player.removeProperty(properties.get(new Random().nextInt(properties.size())));
    }

    @Override
    public final String toString() {
        return "Fait perdre une propriété au joueur courant si possible.";
    }

}
