package controller.resources;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import java.awt.Image;
import java.awt.Font;
import java.awt.FontFormatException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import model.config.GameMap;
import model.config.cells.Cell;
import model.config.cells.CellFactory;
import view.game.GameMapGraphics;
import view.game.cellsgraphics.CellGraphics;
import view.game.cellsgraphics.CellGraphicsFactory;

/**
 * La classe {@link Resources} contient toutes les ressources du jeu.
 */
public final class Resources {
    private static final String PATH = "src/main/resources/";

    private static HashMap<String, Image> commonImages = new HashMap<>();
    private static HashMap<String, Image> modeImages = new HashMap<>();
    private static GameMapGraphics gameMapGraphics = null;

    static {
        loadImages(new File(PATH + "images/"), commonImages, false);
    }

    public static void loadMenuResources() {
        modeImages.clear();
        loadImages(new File(PATH + "images/menu/"), modeImages, true);
    }

    public static void loadGameResources(final String map) {
        modeImages.clear();
        System.out.println(modeImages.keySet());
        loadImages(new File(PATH + "images/game/"), modeImages, true);
        loadImages(new File(PATH + "maps/" + map), modeImages, true);
        loadGameMapGraphics(map);
        System.out.println(modeImages.keySet());
    }

    // region gamemap/cells

    private static void loadGameMapGraphics(final String mapName) {
        try {
            File map = new File(PATH + "maps/" + mapName);
            HashMap<Integer, CellGraphics> cellsGraphics = readCells(new File(map.getPath() + "/cells.json"));
            HashMap<Integer, Cell> cells = getCells(cellsGraphics);
            gameMapGraphics = new GameMapGraphics(new GameMap(mapName, cells), cellsGraphics);
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.out.println("Resource/maps directory not found.");
        }
    }

    private static HashMap<Integer, CellGraphics> readCells(final File cellsFile) {
        HashMap<Integer, CellGraphics> cellsGraphics = new HashMap<>();
        try {
            final String cellsString = FileUtils.readFileToString(cellsFile, StandardCharsets.UTF_8);
            final JSONArray jArrayCells = new JSONArray(cellsString);
            for (int i = 0; i < jArrayCells.length(); i++) {
                final JSONObject jCell = jArrayCells.getJSONObject(i);
                final int id = jCell.getInt("id");
                final Cell cell = CellFactory.createCell(jCell);
                final int xPos = jCell.getInt("xPos");
                final int yPos = jCell.getInt("yPos");
                cellsGraphics.put(id, CellGraphicsFactory.createCellGraphics(cell, yPos, xPos));
            }
        } catch (IOException e) {
            System.out.println("Fichier cells.json non trouvé.");
            e.printStackTrace();
        }
        return cellsGraphics;
    }

    private static HashMap<Integer, Cell> getCells(final HashMap<Integer, CellGraphics> cellsGraphics) {
        HashMap<Integer, Cell> cells = new HashMap<>();
        for (int id : cellsGraphics.keySet()) {
            cells.put(id, cellsGraphics.get(id).getCell());
        }
        return cells;
    }

    // endregion

    // region images

    private static void loadImages(final File folder, final HashMap<String, Image> hashMap, final boolean recursive) {
        try {
            for (File file : folder.listFiles()) {
                if (recursive && file.isDirectory()) {
                    loadImages(file, hashMap, recursive);
                } else {
                    final String fileName = file.getName();
                    if (fileName.endsWith(".png") || fileName.endsWith(".jpg")) {
                        addImage(file, hashMap);
                    }
                }
            }
        } catch (NullPointerException e) {
            System.out.println("Resource directory not found.");
        }
    }

    private static String removeExtension(String str) {
        if (str == null || str.isEmpty()) {
          return str;
        }
        final int index = str.indexOf('.');
        return (index == -1) ? str : str.substring(0, index);
    }

    private static void addImage(final File file, final HashMap<String, Image> hashMap) {
        try {
            hashMap.put(removeExtension(file.getName()), ImageIO.read(file));
        } catch (IOException e) {
            System.out.println("File not found.");
        }
    }

    // endregion

    // region font

    public static Font setFont(final String font, final float size) {
        try {
            return Font.createFont(Font.TRUETYPE_FONT, new File(PATH + "fonts/" + font)).deriveFont(Font.BOLD, size);
        } catch (FontFormatException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    // endregion

    // region getter/setters

    public static Image getImage(final String key) {
        if (modeImages.containsKey(key)) {
            return modeImages.get(key);
        } else if (commonImages.containsKey(key)) {
            return commonImages.get(key);
        }
        return commonImages.get("placeholder32_32");
    }

    public static void setGameMapGraphics(GameMapGraphics gameMapGraphics) {
        Resources.gameMapGraphics = gameMapGraphics;
    }

    public static GameMapGraphics getGameMapGraphics() {
        return gameMapGraphics;
    }

    // endregion
}
