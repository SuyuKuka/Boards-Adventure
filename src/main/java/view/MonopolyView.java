package view;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import controller.MonopolyController;

public final class MonopolyView extends JFrame {

    private MonopolyController controller;

    private int width;
    private int height;

    public MonopolyView() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        int largeur = (int) size.getWidth();
        int hauteur = (int) size.getHeight();
        setLocation(largeur / 4 - getWidth() / 2, hauteur / 4 - getHeight() / 2);
        setSize(largeur / 2 - getWidth() / 2, hauteur / 2 - getHeight() / 2);
        width = getWidth();
        height = getHeight();
    }

    public void init() {
        add(new MenuPanel(this));
        setVisible(true);
    }

    public void render() {
        // System.out.println("view render !");
    }

    public void setController(final MonopolyController controller) {
        this.controller = controller;
    }

    public int getFrameWidth() {
        return width;
    }

    public int getFrameHeight() {
        return height;
    }
}
