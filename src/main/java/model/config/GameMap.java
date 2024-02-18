package model.config;

import java.util.ArrayList;
import java.util.HashMap;

import model.config.cells.Cell;
import model.config.cells.Start;

public final class GameMap {
    private final String mapName;
    /** Liste qui contient toutes les cellules accessibles à pied par les joueurs (la case 0 est toujours le START). */
    private final ArrayList<Cell> walkableCells = new ArrayList<>();
    /** Map qui contient toutes les cellules inaccessibles sauf par moyen spécial (téléportation, trône, prison, ..). */
    private final HashMap<Integer, Cell> specialCells = new HashMap<>();

    public GameMap(final String mapName, final HashMap<Integer, Cell> cells) {
        this.mapName = mapName;
        for (Integer cellId : cells.keySet()) {
            if (cellId < 0) {
                specialCells.put(cellId, cells.get(cellId));
            } else {
                walkableCells.add(cells.get(cellId));
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

    public ArrayList<Cell> getWalkableCells() {
        return walkableCells;
    }
    public String toString() {
        return "mapName : " + mapName + ", Nombre de Cells accessibles : " + walkableCells.size();
    }
}
