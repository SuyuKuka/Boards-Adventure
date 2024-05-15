package model.config.items;

import model.Model;

public final class BigDice extends Item {

    public BigDice(final ItemName itemName, final boolean isShopItem, final int value) {
        super(itemName, isShopItem, value);
    }

    @Override
    public void playItemEffect(final Model model) {
        model.getCurrentPlayer.diceRollValue=RAND.nextInt(10)+1;
        model.getCurrentPlayer.getStartItem.resetTurnCooldown;
    }
}
