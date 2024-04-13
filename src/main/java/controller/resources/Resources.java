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
    private static final float DEFAULT_FONT_SIZE = 12;
    private static final String PATH = "src/main/resources/";

    private static HashMap<String, Font> fonts = new HashMap<>();
    private static HashMap<String, Image> commonImages = new HashMap<>();
    private static HashMap<String, Image> modeImages = new HashMap<>();

    /**
     * Charge les ressources communes au mode de jeu {@link game.Mode#GAME} et {@link game.Mode#MENU}.
     * <p> Ces ressources sont constamment chargées par l'application.
     */
    public static void loadCommonResources() {
        loadImages(new File(PATH + "images/"), commonImages, false);
        loadFonts(new File(PATH + "fonts/"), fonts);
    }

    /**
     * Charge les ressources du menu.
     * <p> Ces ressources sont déchargées lorsque le joueur est en jeu.
     */
    public static void loadMenuResources() {
        modeImages.clear();
        loadImages(new File(PATH + "images/menu/"), modeImages, true);
    }

    /**
     * Charge les ressources du jeu ainsi que ceux de la map choisie.
     * <p> Ces ressources sont déchargées lorsque le joueur est dans le menu.
     * @param mapName nom du dossier de la map (dans resources\maps\).
     * @return {@link GameMapGraphics}
     */
    public static GameMapGraphics loadGameResources(final String mapName) {
        modeImages.clear();
        loadImages(new File(PATH + "images/game/"), modeImages, true);
        loadImages(new File(PATH + "maps/" + mapName), modeImages, true);
        return loadGameMapGraphics(mapName);
    }

    // region gamemap/cells

    public static GameMapGraphics loadGameMapGraphics(final String mapName) {
        try {
            File map = new File(PATH + "maps/" + mapName);
            HashMap<Integer, CellGraphics> cellsGraphics = readCells(new File(map.getPath() + "/cells.json"));
            HashMap<Integer, Cell> cells = getCells(cellsGraphics);
            return new GameMapGraphics(new GameMap(mapName, cells), cellsGraphics);
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.out.println("Resource/maps directory not found.");
        }
        return null;
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

    public static void loadFonts(final File folder, final HashMap<String, Font> hashMap) {
        try {
            for (File file : folder.listFiles()) {
                hashMap.put(removeExtension(file.getName()),
                    Font.createFont(Font.TRUETYPE_FONT, file).deriveFont(Font.BOLD, DEFAULT_FONT_SIZE));
            }
        } catch (FontFormatException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static Font getFont(final String font, final float size) {
        return fonts.get(font).deriveFont(Font.BOLD, size);
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

    // endregion
}
