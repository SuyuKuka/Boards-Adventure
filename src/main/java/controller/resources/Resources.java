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

import model.config.cardseffect.CardEffect;
import model.config.cardseffect.CardEffectFactory;
import model.config.cells.Cell;
import model.config.cells.CellFactory;

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
        loadImages(new File(PATH + "images/common"), COMMON_IMAGES, true);
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
    }

    /**
     * Charge les ressources du jeu et de la map choisie. <p>
     * Les ressources du jeu comprend : les images, les cellules, les items et les cartes.
     * @param mapName
     * @param cells
     * @param cardEffects
     * @param startItems
     * @param shopItems
     * @return {@link JSONArray} associé au cellules.
     */
    public static JSONArray loadGameResources(final String mapName, final HashMap<Integer, Cell> cells,
            final ArrayList<CardEffect> cardEffects, final HashMap<String, HashMap<String, String>> startItems,
            final HashMap<String, HashMap<String, String>> shopItems) {
        // IMAGES
        MODE_IMAGES.clear();
        MODE_GIFS.clear();
        final File gameFolder = new File(PATH + "images/game/");
        loadImages(gameFolder, MODE_IMAGES, true);
        loadGifs(gameFolder, MODE_GIFS, true);
        final File mapFolder = new File(PATH + "maps/" + mapName);
        loadImages(mapFolder, MODE_IMAGES, true);
        loadGifs(mapFolder, MODE_GIFS, true);
        try {
            final File file = new File(PATH + "maps/" + mapName + "/config.json");
            final JSONObject configJObject = new JSONObject(FileUtils.readFileToString(file, StandardCharsets.UTF_8));
            final JSONArray jCells = configJObject.getJSONArray("cells");
            loadCells(cells, jCells);
            loadCardEffects(cardEffects, configJObject.getJSONArray("cards_effect"));
            loadAvailableStartItems(startItems, configJObject.getJSONArray("start_items"));
            loadAvailableShopItems(shopItems, configJObject.getJSONArray("shop_items"));
            return jCells;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // region MODEL

    private static void loadCells(final HashMap<Integer, Cell> cellsMap, final JSONArray jCells) {
        for (int i = 0; i < jCells.length(); i++) {
            final JSONObject jCell = jCells.getJSONObject(i);
            cellsMap.put(jCell.getInt("id"), CellFactory.createCell(jCell));
        }
    }

    private static void loadCardEffects(final ArrayList<CardEffect> cardEffects, final JSONArray jCardsEffect) {
        for (int i = 0; i < jCardsEffect.length(); i++) {
            cardEffects.addAll(CardEffectFactory.createCardEffect(jCardsEffect.getJSONObject(i)));
        }
    }

    private static void loadAvailableStartItems(final HashMap<String, HashMap<String, String>> startItems,
            final JSONArray jStartItems) {
        for (int i = 0; i < jStartItems.length(); i++) {
            final JSONObject jStartItem = jStartItems.getJSONObject(i);
            final HashMap<String, String> itemConfig = new HashMap<>();
            final String itemName = jStartItem.getString("name");
            itemConfig.put("name", itemName);
            itemConfig.put("turnCooldown", jStartItem.getString("turnCooldown"));
            startItems.put(itemName, itemConfig);
        }
    }

    private static void loadAvailableShopItems(final HashMap<String, HashMap<String, String>> shopItems,
            final JSONArray jShopItems) {
        for (int i = 0; i < jShopItems.length(); i++) {
            final JSONObject jStartItem = jShopItems.getJSONObject(i);
            final HashMap<String, String> itemConfig = new HashMap<>();
            final String itemName = jStartItem.getString("name");
            itemConfig.put("name", itemName);
            itemConfig.put("buyPrice", jStartItem.getString("buyPrice"));
            shopItems.put(itemName, itemConfig);
        }
    }

    // endregion

    // region images

    private static void loadImages(final File folder, final HashMap<String, Image> hashMap, final boolean recursive) {
        try {
            for (File file : folder.listFiles()) {
                if (file.isDirectory() && recursive) {
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

    private static void loadFonts(final File folder, final HashMap<String, Font> hashMap) {
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
