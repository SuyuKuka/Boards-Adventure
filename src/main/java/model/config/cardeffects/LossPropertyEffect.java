package model.config.cardeffects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import model.Model;
import model.config.cells.Property;
import model.config.players.Player;

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
    public void applyEffect(Player player, Model model) {
        HashMap<Integer, ArrayList<Property>> tmp = player.getTeam().getProperties();
        Random i = new Random();
        ArrayList<Property> list = tmp.get(new ArrayList<>(tmp.keySet()).get(i.nextInt(tmp.keySet().size())));
        list.remove(i.nextInt(list.size()));
    }

}
