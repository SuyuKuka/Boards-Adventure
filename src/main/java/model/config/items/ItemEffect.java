package model.config.items;

import model.Model;
import model.config.players.Player;

public abstract class ItemEffect {

    public abstract void applyEffect(Player player, Model model);

}
