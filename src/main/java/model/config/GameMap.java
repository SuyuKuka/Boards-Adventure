package model.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import model.config.cardseffect.CardEffect;
import model.config.cells.Cell;
import model.config.cells.Start;
import model.config.items.Item;

/**
 * Classe qui contient toutes les données "logiques" d'une map sous forme de fichier "config.json"
 * trouvable dans "src/resources/maps/{@link #mapName}".
 * <p> Une map contient :
 * <p> - Les {@link Cell} qui composent le plateau de la map.
 * <p> - Une liste de cartes ayant des effets {@link CardEffect}.
 */
public final class GameMap {

    private final String mapName;

    /** Liste des items des shops de la map disponibles. */
    private final ArrayList<Item> shopItems;
    /** Liste des cartes disponibles pour cette map. */
    private final ArrayList<CardEffect> cardEffects;
    /** Liste des cellules du jeu. */
    private final HashMap<Integer, Cell> cells;

    private int cardEffectsCounter;

    public GameMap(final String mapName, final ArrayList<CardEffect> cardEffects, final HashMap<Integer, Cell> cells,
        final ArrayList<Item> shopItems) {
        this.mapName = mapName;
        this.cardEffects = cardEffects;
        this.cells = cells;
        this.shopItems = shopItems;
        cardEffectsCounter = countCardEffectsQuantity();
    }

    private int countCardEffectsQuantity() {
        int counter = 0;
        for (int i = 0; i < cardEffects.size(); i++) {
            counter += cardEffects.get(i).getQuantity();
        }
        return counter;
    }

    public CardEffect loadRandomCardEffect() {
        final Random rand = new Random();
        int randInt = rand.nextInt(cardEffectsCounter);
        CardEffect cardEffect = null;
        for (int i = 0; i < cardEffects.size(); i++) {
            cardEffect = cardEffects.get(i);
            if (randInt <= 0) {
                break;
            }
            randInt -= cardEffect.getQuantity();
        }
        cardEffects.remove(cardEffect);
        cardEffectsCounter--;
        return cardEffect;
    }

    public int getLapGain() {
        return ((Start) cells.get(0)).getLapGain();
    }

    public String getMapName() {
        return mapName;
    }

    public Cell getCell(final int pos) {
        return cells.get(pos);
    }

    public ArrayList<CardEffect> getCardEffects() {
        return cardEffects;
    }

    public ArrayList<Item> getShopItems() {
        return shopItems;
    }

}
