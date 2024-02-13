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
        System.out.println(gameMaps);
    }

    private static void loadGameMapsGraphics() {
        try {
            File maps = new File(PATH + "maps/");
            for (File map : maps.listFiles()) {
                if (isACorrectMap(map)) {
                    final String mapName = map.getName();
                    CellGraphics[] cellsGraphics = readCells(new File(map.getPath() + "/cells.json"));
                    Cell[] cells = new Cell[cellsGraphics.length];
                    for (int i = 0; i < cellsGraphics.length; i++) {
                        cells[i] = cellsGraphics[i].getCell();
                    };
                    HashMap<String, Image> gameMapImages = new HashMap<>();
                    for (File file : map.listFiles()) {
                        if (getExtension(file).equals(".png")) {
                            addImage(file, gameMapImages);
                        }
                    }
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

    /**
     * Renvoie true si le fichier donné en paramètres est bien un dossier pour une map
     * (= est un dossier qui contient un fichier cells.json et un fichier board.png).
     * @return
     */
    private static boolean isACorrectMap(final File file) {
        return file.isDirectory() && new File(file.getPath() + "/cells.json").exists() && new File(file.getPath() + "/board.png").exists();
    }

    private static String getExtension(final File file) {
        return file.getName().substring(file.getName().lastIndexOf("."));
    }

    /**
     * Méthode qui renvoie une ArrayList contenant 2 tableaux de CellGraphics[].
     * Le premier tableau contient toutes les cellules "normales" où les joueurs peuvent se déplacer de manière libres.
     * Le deuxième tableau contient toutes les cellules "spéciales" non accessibles normalement (e.g : trône, prison,...).
     * @param cellsFile
     * @return
     */
    private static CellGraphics[] readCells(final File cellsFile) {
        CellGraphics[] cellsGraphics = new CellGraphics[0];
        try {
            final String cellsString = FileUtils.readFileToString(cellsFile, StandardCharsets.UTF_8);
            final JSONArray jArrayCells = new JSONArray(cellsString);
            cellsGraphics = new CellGraphics[jArrayCells.length()];
            for (int i = 0; i < jArrayCells.length(); i++) {
                JSONObject jCell = jArrayCells.getJSONObject(i);
                int id = jCell.getInt("id");
                String type = jCell.getString("type");
                Integer value = jCell.has("value") ? jCell.getInt("value") : null;
                Integer group = jCell.has("group") ? jCell.getInt("group") : null;
                Cell cell = CellFactory.createCell(id, type, value, group);
                int xPos = jCell.getInt("xPos");
                int yPos = jCell.getInt("yPos");
                CellGraphics cellGraphics = new CellGraphics(cell, xPos, yPos);
                cellsGraphics[i] = cellGraphics;
            }
        } catch (IOException e) {
            System.out.println("Fichier cells.json non trouvé.");
            e.printStackTrace();
        }
        return cellsGraphics;
    }

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

    public static Image getImage(final String key) {
        if (images.containsKey(key)) {
            return images.get(key);
        }
        return images.get("placeholder32_32");
    }

    public static void main(String[] args) {
        loadResources();
    }
}