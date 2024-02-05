package game;

import java.io.File;
import java.io.IOException;

import java.util.HashMap;

import java.awt.Image;

import javax.imageio.ImageIO;

public class Resources {
    private static final String PATH = "src/main/resources/";

    private static HashMap<String, Image> images = new HashMap<>();

    public static void loadResources() {
        loadImages();
    }

    public static void loadImages() {
        try {
            File resources = new File(PATH);
            for (File file : resources.listFiles()) {
                if (!file.isDirectory() && getExtension(file).equals(".png")) {
                    int test = file.getName().indexOf(".png");
                    if (test == -1) {
                        test = file.getName().length();
                    }
                    String imageName = file.getName().substring(0, test);
                    try {
                        images.put(imageName, ImageIO.read(file));
                    } catch (IOException e) {
                        System.out.println("File not found.");
                    }
                }
            }
        } catch (NullPointerException e) {
            System.out.println("Resource directory not found.");
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
}