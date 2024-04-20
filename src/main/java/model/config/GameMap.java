package model.config;

import java.util.ArrayList;
import java.util.HashMap;

import model.config.cardeffects.CardEffect;
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

    /** Liste des cartes disponibles pour cette map. */
    private final ArrayList<CardEffect> cardEffects;
    /** Liste des cellules du jeu. */
    private final HashMap<Integer, Cell> cells;
    /** Liste des items du jeu. */
    private final ArrayList<Item> items;

    public GameMap(final String mapName, final ArrayList<CardEffect> cardEffects, final HashMap<Integer, Cell> cells,
        final ArrayList<Item> items) {
        this.mapName = mapName;
        this.cardEffects = cardEffects;
        this.cells = cells;
        this.items = items;
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

    public ArrayList<Item> getItems() {
        return items;
    }

}
