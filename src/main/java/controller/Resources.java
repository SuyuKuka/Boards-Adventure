package controller;

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
import view.CellGraphics;
import view.GameMapGraphics;

/**
 * La classe {@link Resources} contient toutes les ressources du jeu.
 */
public final class Resources {
    private static final String PATH = "src/main/resources/";

    private static HashMap<String, GameMapGraphics> gameMaps = new HashMap<>();
    private static HashMap<String, Image> images = new HashMap<>();
    private static HashMap<String, Image> menuImages = new HashMap<>();
    private static HashMap<String, Image> mapImages = new HashMap<>();

    public static void loadResources() {
        loadImages(new File(PATH), images);
        loadImages(new File(PATH + "menu/"), menuImages);
        loadImages(new File(PATH + "maps/"), mapImages);
        loadGameMapsGraphics();
    }

    private static String getExtension(final File file) {
        return file.getName().substring(file.getName().lastIndexOf("."));
    }

    // region gamemap/cells

    private static void loadGameMapsGraphics() {
        try {
            File maps = new File(PATH + "maps/");
            for (File map : maps.listFiles()) {
                if (isACorrectMap(map)) {
                    final String mapName = map.getName();
                    HashMap<Integer, CellGraphics> cellsGraphics = readCells(new File(map.getPath() + "/cells.json"));
                    HashMap<Integer, Cell> cells = getCells(cellsGraphics);
                    HashMap<String, Image> gamemapImages = new HashMap<>();
                    loadImages(map, gamemapImages);
                    gameMaps.put(mapName,
                        new GameMapGraphics(new GameMap(mapName, cells), gamemapImages, cellsGraphics));
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.out.println("Resource/maps directory not found.");
        }
    }

    private static boolean isACorrectMap(final File file) {
        return file.isDirectory() && new File(file.getPath() + "/cells.json").exists()
            && new File(file.getPath() + "/board.png").exists();
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
                cellsGraphics.put(id, new CellGraphics(cell, xPos, yPos));
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

    private static void loadImages(final File folder, final HashMap<String, Image> hashMap) {
        try {
            for (File file : folder.listFiles()) {
                if (file.getName().contains(".") && getExtension(file).equals(".png")) {
                    addImage(file, hashMap);
                }
            }
        } catch (NullPointerException e) {
            System.out.println("Resource directory not found.");
        }
    }

    private static void addImage(final File file, final HashMap<String, Image> hashMap) {
        int nameLength = file.getName().indexOf(".png");
        String imageName = file.getName().substring(0, nameLength);
        try {
            hashMap.put(imageName, ImageIO.read(file));
        } catch (IOException e) {
            System.out.println("File not found.");
        }
    }

    // endregion

    // region font

    public static Font loadFont(final float size, final String font) {
        try {
            Font cFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/police/" + font));
            cFont = cFont.deriveFont(Font.BOLD, size);
            return cFont;
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
        if (images.containsKey(key)) {
            return images.get(key);
        }
        return images.get("placeholder32_32");
    }

    public static Image getMenuImage(final String key) {
        if (menuImages.containsKey(key)) {
            return menuImages.get(key);
        }
        return images.get("placeholder32_32");
    }

    public static Image getMapImage(final String key) {
        if (mapImages.containsKey(key)) {
            return mapImages.get(key);
        }
        return images.get("placeholder32_32");
    }

    public static GameMapGraphics getGameMapGraphics(final String key) {
        return gameMaps.get(key);
    }

    public static String getPath() {
        return PATH;
    }

    // endregion

    public static void main(final String[] args) {
        loadResources();
    }
}
