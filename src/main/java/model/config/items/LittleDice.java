package model.config.items;

import model.Model;

public final class LittleDice extends Item {

    public LittleDice(final ItemName itemName, final boolean isShopItem, final int value) {
        super(itemName, isShopItem, value);
    }

    @Override
    public void playItemEffect(final Model model) {
        model.getCurrentPlayer().diceRollValue=RAND.nextInt(3)+1;
        model.getCurrentPlayer().getStartItem().resetTurnCooldown();
    }
}
