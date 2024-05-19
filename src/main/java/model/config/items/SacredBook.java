package model.config.items;

import model.Model;
import model.Player;
import model.config.cells.Cell;
import model.config.cells.Property;

public final class SacredBook extends Item {

    public SacredBook(final ItemName itemName, final boolean isShopItem, final int value) {
        super(itemName, isShopItem, value);
    }

    @Override
    public void playItemEffect(final Player player, final Model model) {
        final Property property = (Property) player.getPlayerCell(model);
        property.getOwner().removeProperty(property);
        property.setOwner(null);
        player.setCoins(player.getCoins() / 4);
        player.setStartItemCooldown(getTurnCooldown());
    }

    @Override
    public boolean canItemBeActivated(Player player, Model model) {
        final Cell currentCell = player.getPlayerCell(model);
        return currentCell instanceof Property && !((Property) currentCell).isAtHome(player);
    }
}
