package model.config.items;

import model.Model;

public final class SacredBook extends Item {

    public SacredBook(final ItemName itemName, final boolean isShopItem, final int value) {
        super(itemName, isShopItem, value);
    }

    @Override
    public void playItemEffect(Model model) {
        if(model.getCurrentPlayer.getStartItem().turnCooldown==0){
            Player p = model.getCurrentPlayer;
            if(model.getCurrentPlayerCelltype()==PROPERTY && !p.getCurrentCell().isAtHome(p)){
                p.getCurrentCell().getOwner.removeProperty(p.getCurrentCell());
                p.getCurrentCell().setOwner(null);
                p.setMoney(p.getMoney/4);
                p.getStartItem().resetTurnCooldown();
            }
            else {
                System.out.println("Vous ne pouvez pas faire ça chez vous !");
            }
        }
        else{
            System.out.println("Votre objet est disponible dans "+model.getCurrentPlayer.getStartItem.getTurnCooldown()+" tours.");
        }
    }
}
