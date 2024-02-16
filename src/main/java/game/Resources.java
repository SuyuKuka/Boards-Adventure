package game;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import java.awt.Image;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import model.config.GameMap;
import model.config.cell.Cell;
import model.config.cell.CellFactory;
import view.CellGraphics;
import view.GameMapGraphics;

/**
 * La classe {@link Resources} contient toutes les ressources du jeu.
 */
public final class Resources {
    private static final String PATH = "src/main/resources/";

    private static HashMap<String, GameMapGraphics> gameMaps = new HashMap<>();
    private static HashMap<String, Image> images = new HashMap<>();

    public static void loadResources() {
        loadImages(new File(PATH), images);
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
                    HashMap<String, Image> mapImages = new HashMap<>();
                    loadImages(map, mapImages);
                    gameMaps.put(mapName, new GameMapGraphics(new GameMap(mapName, cells), mapImages, cellsGraphics));
                }
            }
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
                final String type = jCell.getString("type");
                final Integer value = jCell.has("value") ? jCell.getInt("value") : null;
                final Integer group = jCell.has("group") ? jCell.getInt("group") : null;
                final Cell cell = CellFactory.createCell(id, type, value, group);
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

    private static boolean isACorrectMap(final File file) {
        return file.isDirectory() && new File(file.getPath() + "/cells.json").exists()
            && new File(file.getPath() + "/board.png").exists();
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

    // region getter/setters

    public static Image getImage(final String key) {
        if (images.containsKey(key)) {
            return images.get(key);
        }
        return images.get("placeholder32_32");
    }

    public static GameMapGraphics getGameMapGraphics(final String key) {
        return gameMaps.get(key);
    }

    // endregion

    public static void main(final String[] args) {
        loadResources();
    }
}
