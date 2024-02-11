package model.config.cell;

public abstract class Cell {

    final String name;
    final int price;

    public Cell(final String name, final int price) {
        this.name = name;
        this.price = price;
    }
}
