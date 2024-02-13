package view;

import java.util.HashMap;

import java.awt.Image;

import model.config.GameMap;

public class GameMapGraphics {
    private final GameMap gameMap;
    private final HashMap<String, Image> images;
    private final CellGraphics[] cellsGraphics;

    public GameMapGraphics(final GameMap gameMap, final HashMap<String, Image> images, final CellGraphics[] cellsGraphics) {
        this.gameMap = gameMap;
        this.images = images;
        this.cellsGraphics = cellsGraphics;
    }

    public Image getImage(final String key) {
        return images.get(key);
    }

    public String toString() {
        return "GameMap : " + gameMap + ",\nImages : " + images + ",\nNombre de CellsGraphics : " + cellsGraphics.length;
    }
}
