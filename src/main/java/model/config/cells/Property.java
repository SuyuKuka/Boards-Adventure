package model.config.cells;

import java.util.ArrayList;

import model.GameData;
import model.config.players.Player;

public final class Property extends Cell {

    private final ArrayList<Integer> buyPrices;
    private final int group;
    private int propertyState = 0;
    private Player owner = null;

    public Property(final int id, final ArrayList<Integer> buyPrices, final int group) {
        super(id);
        this.buyPrices = buyPrices;
        this.group = group;
    }

    @Override
    public void playEffect(final Player player, final GameData gameState) {
        if (owner == null) {
            if (player.getTeam().getMoney() < buyPrices.get(0)) {
                System.out.println("Désolé, vous n'avez pas assez d'argents pour acheter cette propriété ("
                + buyPrices.get(0) + "$).");
            } else {
                String str = System.console().readLine("Cette maison n'est pas à vendre. Voulez vous l'acheter ? "
                + buyPrices.get(0) + " : ");
                if (str.toLowerCase().equals("oui")) {
                    owner = player;
                    player.getTeam().addProperty(this);
                    player.getTeam().incrementMoney(-buyPrices.get(propertyState));
                    propertyState++;
                }
            }
        } else if (owner == player) {
            if (propertyState + 1 > buyPrices.size()) {
                System.out.println("Votre propriété est déjà au max.");
            } else if (player.getTeam().getMoney() < buyPrices.get(propertyState + 1)) {
                System.out.println("Désolé, vous n'avez pas assez d'argents pour améliorer votre propriété : ("
                + buyPrices.get(propertyState + 1) + "$).");
            } else {
                String str = System.console().readLine("Voulez vous améliorer votre propriété ? "
                + buyPrices.get(propertyState + 1) + " : ");
                if (str.toLowerCase().equals("oui")) {
                    owner = player;
                    player.getTeam().incrementMoney(-buyPrices.get(propertyState));
                    propertyState++;
                }
            }
        } else {
            final int price = buyPrices.get(propertyState - 1);
            if (player.getTeam().getMoney() < price) {
                //gameState.removePlayer(player);
                System.out.println(player + " a pas assez d'argents.");
            } else {
                player.getTeam().incrementMoney(-price);
                owner.getTeam().incrementMoney(price);
            }
        }
    }

    public int getGroup() {
        return group;
    }

}
