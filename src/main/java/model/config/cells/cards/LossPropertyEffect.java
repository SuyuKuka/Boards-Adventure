package model.config.cells.cards;

import java.util.Random;

import model.Model;
import model.config.players.Player;

public class LossPropertyEffect extends CardEffect {
    /**
     * Le joueur perd une de ces propriétés de manière aléatoire.
     *
     * @param  player  Le joueur au quel l'effet s'applique
     * @param  model   Le model qui contient toutes les informations du modeles
     */
    @Override
    public void applyEffect(Player player, Model model) {
        Random rand = new Random();
        int i = rand.nextInt(player.getTeam().getProperties().size());
        player.getTeam().getProperties().remove(i);
    }
}
