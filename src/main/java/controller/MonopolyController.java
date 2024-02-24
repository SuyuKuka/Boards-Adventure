package controller;

import model.MonopolyModel;
import view.MonopolyView;

public final class MonopolyController {

    public enum Mode {
        MENU, GAME;
    }

    private MonopolyModel model;
    private MonopolyView view;

    private Mode mode = Mode.MENU;

    public MonopolyController() {

    }

    public Mode getMode() {
        return mode;
    }

    public void setModel(final MonopolyModel model) {
        this.model = model;
    }

    public void setView(final MonopolyView view) {
        this.view = view;
    }

}
