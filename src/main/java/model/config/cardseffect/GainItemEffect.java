package model.config.cardseffect;

import model.Model;
import model.config.GameMap;
import model.config.players.Player;

/**
 * Fait gagner au joueur courant un {@link Item} choisi aléatoirement de type {@link Shop} si rien ne le contraint.
 */
public final class GainItemEffect extends CardEffect {

    public GainItemEffect(final int quantity) {
        super(quantity);
    }

    @Override
    public void applyEffect(final Player player, final Model model) {
        final GameMap gameMap = model.getGameMap();
        if (player.getShopItem().length < Player.getMaxItemShop() && gameMap.hasAvailableShopItem()) {
            player.addShopItem(gameMap.generateRandomItem(true));
        }
    }
}
