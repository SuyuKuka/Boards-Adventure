package model.config.cell;

public class Property extends Cell {

    private int rent;
    private int ownerID; // Voir comment on s'organisera une fois Player plus concret

    public Property(final String name, final int price, final int rent) {
        super(name, price);
        this.rent = rent;
        this.ownerID = 0;
    }

    public int getRent(){
        return rent;
    }
    public int ownerID(){
        return ownerID;
    }

    public void setOwner(int ownerID){
        this.ownerID = ownerID;
    }
}
