package model.config;

public class Property {
    private static int lastId=0;
    private Player owner;
    private int id;
    private final String name;
    private final int price;

    public Property(final String name, final int price) {
        this.name = name;
        this.price = price;
        lastId++;
        this.id=lastId;
    }
    public void setOwner(Player p){this.owner=p;}
    public Player getOwner() {return owner;}
    public boolean isAtHome(Player p){return p==owner;}
    public boolean isVoid(){return this.owner==null;}
}
