package model.config.cell;

public abstract class Cell {

    final int id;
    final int price;

    public Cell(final int id, final int price) {
        this.id = id;
        this.price = price;
    }
}
