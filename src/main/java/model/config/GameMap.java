package model.config;

import java.util.ArrayList;
import java.util.HashMap;

import model.config.cardeffects.CardEffect;
import model.config.cells.Cell;
import model.config.cells.Start;

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

    public GameMap(final String mapName, final ArrayList<CardEffect> cardEffects, final HashMap<Integer, Cell> cells) {
        this.mapName = mapName;
        this.cardEffects = cardEffects;
        this.cells = cells;
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

}
