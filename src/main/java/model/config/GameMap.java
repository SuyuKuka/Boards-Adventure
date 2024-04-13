package model.config;

import java.util.HashMap;

import model.config.cells.Cell;
import model.config.cells.Start;

/**
 * Classe qui contient les données (non graphiques) d'une map.
 * Un {@link GameMap} contient :
 * <p> - Une liste de {@link model.config.cells.Cell} qui composent le plateau du jeu.
 * <p> - Liste de cartes disponibles (pour les {@link model.config.cells.Card} {A IMPLEMENTER}).
 */
public final class GameMap {

    private final String mapName;
    /** Map qui contient toutes les cellules accessibles à pied par les joueurs. */
    private final HashMap<Integer, Cell> walkableCells = new HashMap<>();
    /** Map qui contient toutes les cellules inaccessibles sauf par moyen spécial (trône, prison, ...). */
    private final HashMap<Integer, Cell> specialCells = new HashMap<>();

    public GameMap(final String mapName, final HashMap<Integer, Cell> cells) {
        this.mapName = mapName;
        for (Integer cellId : cells.keySet()) {
            if (cellId < 0) {
                specialCells.put(cellId, cells.get(cellId));
            } else {
                walkableCells.put(cellId, cells.get(cellId));
            }
        }
    }

    public int getLapGain() {
        return ((Start) walkableCells.get(0)).getLapGain();
    }

    public String getMapName() {
        return mapName;
    }

    public Cell getCell(final int pos) {
        if (pos < 0) {
            return specialCells.get(pos);
        }
        return walkableCells.get(pos);
    }

    public HashMap<Integer, Cell> getWalkableCells() {
        return walkableCells;
    }
    public String toString() {
        return "mapName : " + mapName + ", Nombre de Cells accessibles : " + walkableCells.size();
    }
}
