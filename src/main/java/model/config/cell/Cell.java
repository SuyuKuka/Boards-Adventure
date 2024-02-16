package model.config.cell;

public abstract class Cell {

    private final int id;
    private final int price;

    public Cell(final int id, final int price) {
        this.id = id;
        this.price = price;
    }
}
