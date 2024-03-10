package view;

import java.util.HashMap;

import controller.Resources;

import java.awt.Image;

import model.config.GameMap;
import view.game.cellsgraphics.CellGraphics;

public final class GameMapGraphics {

    private static final int CELL_HEIGHT = 25;
    private static final int CELL_WIDTH = 25;

    private final GameMap gameMap;
    private final HashMap<String, Image> images;
    private final HashMap<Integer, CellGraphics> cellsGraphics;

    public GameMapGraphics(final GameMap gameMap, final HashMap<String, Image> images,
        final HashMap<Integer, CellGraphics> cellsGraphics) {
        this.gameMap = gameMap;
        this.images = images;
        this.cellsGraphics = cellsGraphics;
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public HashMap<String, Image> getImages() {
        return images;
    }

    public Image getBoardImage() {
        return getImage("board");
    }

    public Image getImage(final String key) {
        if (images.containsKey(key)) {
            return images.get(key);
        }
        return Resources.getPlaceHolderImg();
    }

    public HashMap<Integer, CellGraphics> getCellsGraphics() {
        return cellsGraphics;
    }

    public CellGraphics getCellGraphics(final int cellID) {
        return cellsGraphics.get(cellID);
    }
}
