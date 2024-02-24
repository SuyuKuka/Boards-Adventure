package controller;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;

import javax.imageio.ImageIO;

public class Resources {
    private static final String PATH = "src/main/resources/";
    private static Font cFont;

    private static HashMap<String, Image> images = new HashMap<>();
    private static HashMap<String, Image> menuImages = new HashMap<>();
    private static HashMap<String, Image> mapImages = new HashMap<>();
    
    public static void loadResources() {
        loadImages(new File(PATH), images);
        loadImages(new File(PATH + "menu/"), menuImages);
        loadImages(new File(PATH + "maps/"), mapImages);
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

    public static String getExtension(final File file) {
        return file.getName().substring(file.getName().lastIndexOf("."));
    }

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

    public static Font loadFont(float size, String font){
        try {
            cFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/police/" + font));
            cFont = cFont.deriveFont(Font.BOLD, size);
        } catch (FontFormatException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return cFont;
    }
 
    public static String getPath() {
        return PATH;
    }

}