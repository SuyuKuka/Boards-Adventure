package view;

import java.util.HashMap;

import java.awt.Image;

import model.config.GameMap;

public final class GameMapGraphics {
    private final GameMap gameMap;
    private final HashMap<String, Image> images;
    private final HashMap<Integer, CellGraphics> cellsGraphics;

    public GameMapGraphics(final GameMap gameMap, final HashMap<String, Image> images,
        final HashMap<Integer, CellGraphics> cellsGraphics) {
        this.gameMap = gameMap;
        this.images = images;
        this.cellsGraphics = cellsGraphics;
    }

    public Image getImage(final String key) {
        return images.get(key);
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public String toString() {
        return "GameMap : " + gameMap + ",\nImages : " + images + ",\nNombre de CellGraphics : " + cellsGraphics.size();
    }
}
