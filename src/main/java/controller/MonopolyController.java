package controller;

import model.MonopolyModel;
import view.MonopolyView;

public class MonopolyController {

    private MonopolyModel model;
    private MonopolyView view;

    public MonopolyController() {

    }

    public void setModel(final MonopolyModel model) {
        this.model = model;
    }

    public void setView(final MonopolyView view) {
        this.view = view;
    }

}
