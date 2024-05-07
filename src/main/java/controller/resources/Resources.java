package controller.resources;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

import java.awt.Image;
import java.awt.Font;
import java.awt.FontFormatException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import model.config.GameMap;
import model.config.cardseffect.CardEffect;
import model.config.cardseffect.CardEffectFactory;
import model.config.cells.Cell;
import model.config.cells.CellFactory;
import model.config.items.Item;
import model.config.items.ItemFactory;
import view.game.GamePanel;
import view.game.cellsgraphics.CellGraphics;
import view.game.cellsgraphics.CellGraphicsFactory;

/**
 * La classe {@link Resources} contient toutes les ressources du jeu.
 * <p> === <{@link Image}> === <p>
 * Dans le menu, seules les images contenus dans "src/main/resources/" et "src/main/resources/menu/.." sont accessibles.
 * <p> En jeu, seules les images contenus dans "src/main/resources/" et "src/main/resources/game/.." sont accessibles.
 * <p> Pour obtenir un {@link Image}, utilisez {@link #getImage(String)} avec String le nom du fichier sans extension.
 * <p> (Par exemple, getImage("test") pour test.png ou test.jpg). Evitez d'avoir plusieurs fichiers du même nom!
 *
 * <p> === <{@link Font}> === <p>
 * Toutes les polices d'écritures sont chargées dès le lancement du programme.
 * <p> Pour obtenir un objet {@link Font}, utilisez {@link #getFont(String, float)} avec String le nom du fichier
 * sans extension et float la taille souhaitée.
 *
 */
public final class Resources {
    private static final float DEFAULT_FONT_SIZE = 12;
    private static final String PATH = "src/main/resources/";

    private static final HashMap<String, Font> FONTS = new HashMap<>();
    private static final HashMap<String, Image> COMMON_IMAGES = new HashMap<>();
    private static final HashMap<String, Image> MODE_IMAGES = new HashMap<>();
    private static final HashMap<String, ImageIcon> MODE_GIFS = new HashMap<>();

    /**
     * Charge les ressources communes au jeu et menu de l'application.
     */
    public static void loadCommonResources() {
        loadImages(new File(PATH + "images/"), COMMON_IMAGES, false);
        loadFonts(new File(PATH + "fonts/"), FONTS);
    }

    /**
     * Charge les ressources du menu.
     */
    public static void loadMenuResources() {
        MODE_IMAGES.clear();
        MODE_GIFS.clear();
        final File menuFolder = new File(PATH + "images/menu/");
        loadImages(menuFolder, MODE_IMAGES, true);
        loadGifs(menuFolder, MODE_GIFS, true);
        System.out.println(MODE_GIFS.keySet());
    }

    /**
     * Charge les ressources du jeu et de la map choisie.
     * @param mapName
     */
    public static void loadGameResources(final String mapName) {
        MODE_IMAGES.clear();
        MODE_GIFS.clear();
        final File gameFolder = new File(PATH + "images/game/");
        loadImages(gameFolder, MODE_IMAGES, true);
        loadGifs(gameFolder, MODE_GIFS, true);
        final File mapFolder = new File(PATH + "maps/" + mapName);
        loadImages(mapFolder, MODE_IMAGES, true);
        loadGifs(mapFolder, MODE_GIFS, true);
    }

    // region gameMap

    public static GameMap loadGameMap(final String mapName) {
        try {
            final File file = new File(PATH + "maps/" + mapName + "/config.json");
            final JSONObject configJObject = new JSONObject(FileUtils.readFileToString(file, StandardCharsets.UTF_8));
            final ArrayList<CardEffect> cardEffects = loadCardEffects(configJObject.getJSONArray("cards_effect"));
            final HashMap<Integer, Cell> cells = loadCells(configJObject.getJSONArray("cells"));
            final ArrayList<Item> shopItems = loadShopItems(configJObject.getJSONArray("shop_items"));
            return new GameMap(mapName, cardEffects, cells, shopItems);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static ArrayList<CardEffect> loadCardEffects(final JSONArray jCardEffect) {
        final ArrayList<CardEffect> cardEffects = new ArrayList<>();
        for (int i = 0; i < jCardEffect.length(); i++) {
            cardEffects.addAll(CardEffectFactory.createCardEffect(jCardEffect.getJSONObject(i)));
        }
        return cardEffects;
    }

    private static HashMap<Integer, Cell> loadCells(final JSONArray jCells) {
        final HashMap<Integer, Cell> cells = new HashMap<>();
        for (int i = 0; i < jCells.length(); i++) {
            final JSONObject jCell = jCells.getJSONObject(i);
            cells.put(jCell.getInt("id"), CellFactory.createCell(jCell));
        }
        return cells;
    }

    private static ArrayList<Item> loadShopItems(final JSONArray jShopItems) {
        final ArrayList<Item> shopItems = new ArrayList<>();
        for (int i = 0; i < jShopItems.length(); i++) {
            shopItems.add(ItemFactory.createItem(jShopItems.getJSONObject(i), true));
        }
        return shopItems;
    }

    // endregion

    // region gameMapGraphics

    public static HashMap<Integer, CellGraphics> loadCellsGraphics(final GamePanel gamePanel, final String mapName) {
        try {
            final File file = new File(PATH + "maps/" + mapName + "/config.json");
            final JSONArray jCellsArray = new JSONObject(FileUtils.readFileToString(file, StandardCharsets.UTF_8))
                .getJSONArray("cells");
            final HashMap<Integer, CellGraphics> cellsGraphics = new HashMap<>();
            for (int i = 0; i < jCellsArray.length(); i++) {
                final JSONObject jCell = jCellsArray.getJSONObject(i);
                cellsGraphics.put(jCell.getInt("id"), CellGraphicsFactory.createCellGraphics(gamePanel, jCell));
            }
            return cellsGraphics;
        } catch (Exception e) {
            System.out.println("Resource/maps directory not found.");
            e.printStackTrace();
        }
        return null;
    }

    // endregion

    // region images

    public static void loadImages(final File folder, final HashMap<String, Image> hashMap, final boolean recursive) {
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
            e.printStackTrace();
            System.out.println("Resource directory not found.");
        }
    }

    public static void loadGifs(final File folder, final HashMap<String, ImageIcon> hashMap, final boolean recursive) {
        try {
            for (File file : folder.listFiles()) {
                if (recursive && file.isDirectory()) {
                    loadGifs(file, hashMap, recursive);
                } else {
                    if (file.getName().endsWith(".gif")) {
                        addGif(file, hashMap);
                    }
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
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

    private static void addGif(final File file, final HashMap<String, ImageIcon> hashMap) {
        hashMap.put(removeExtension(file.getName()), new ImageIcon(file.getPath()));
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
        return FONTS.get(font).deriveFont(Font.BOLD, size);
    }

    // endregion

    // region getter/setters

    public static Image getImage(final String key) {
        if (MODE_IMAGES.containsKey(key)) {
            return MODE_IMAGES.get(key);
        } else if (COMMON_IMAGES.containsKey(key)) {
            return COMMON_IMAGES.get(key);
        }
        return COMMON_IMAGES.get("placeholder32_32");
    }

    public static ImageIcon getGif(final String key) {
        if (MODE_GIFS.containsKey(key)) {
            return MODE_GIFS.get(key);
        }
        return null;
    }

    // endregion
}
