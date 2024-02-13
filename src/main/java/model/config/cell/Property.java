package model.config.cell;

public class Property extends Cell {

    private final int group;

    public Property(final int id, final int price, final int group) {
        super(id, price);
        this.group = group;
    }
}
