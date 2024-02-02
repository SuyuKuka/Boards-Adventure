package view;

import controller.MonopolyController;

public class MonopolyView {

    private MonopolyController controller;

    public MonopolyView() {

    }

    public void render() {
        // System.out.println("view render !");
    }

    public void setController(final MonopolyController controller) {
        this.controller = controller;
    }
}
