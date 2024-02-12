package game;

import java.io.File;
import java.io.IOException;

import java.util.HashMap;

import java.awt.Image;

import javax.imageio.ImageIO;

import model.config.GameMap;
import model.config.cell.Cell;

/**
 * La classe {@link Resources} contient toutes les ressources du jeu.
 */
public final class Resources {
    private static final String PATH = "src/main/resources/";

    private static HashMap<String, GameMap> gameMaps = new HashMap<>();
    private static HashMap<String, HashMap<String, Image>> gameMapsImages = new HashMap<>();
    private static HashMap<String, Image> images = new HashMap<>();

    public static void loadResources() {
        loadImages();
        loadGameMaps();
    }

    private static void loadGameMaps() {
        try {
            File maps = new File(PATH + "maps/");
            for (File map : maps.listFiles()) {
                if (map.isDirectory() && new File(map.getPath() + "/map.json").exists()) {
                    final String mapName = map.getName();
                    GameMap gameMap = null;
                    HashMap<String, Image> gameMapImages = new HashMap<>();
                    for (File file : map.listFiles()) {
                        if (file.getName().equals("map.json")) {
                            gameMap = new GameMap(mapName, readCells(file));
                        } else if (getExtension(file).equals(".png")) {
                            addImage(gameMapImages, file);
                        }
                    }
                    gameMaps.put(mapName, gameMap);
                    gameMapsImages.put(mapName, gameMapImages);
                }
            }
        } catch (NullPointerException e) {
            System.out.println("Resource/maps directory not found.");
        }
    }

    private static void loadImages() {
        try {
            File resources = new File(PATH);
            for (File file : resources.listFiles()) {
                if (file.getName().contains(".") && getExtension(file).equals(".png")) {
                    addImage(images, file);
                }
            }
        } catch (NullPointerException e) {
            System.out.println("Resource directory not found.");
        }
    }

    private static String getExtension(final File file) {
        return file.getName().substring(file.getName().lastIndexOf("."));
    }

    private static Cell[] readCells(final File file) {
        return new Cell[10];
    }

    private static void addImage(final HashMap<String, Image> folder, final File file) {
        int nameLength = file.getName().indexOf(".png");
        String imageName = file.getName().substring(0, nameLength);
        try {
            folder.put(imageName, ImageIO.read(file));
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
        System.out.println(gameMaps);
        System.out.println(gameMapsImages);
        System.out.println(images);
    }
}