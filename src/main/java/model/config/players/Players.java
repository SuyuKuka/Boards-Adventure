package model.config.players;

import java.util.ArrayList;

import model.config.cell.Property;
import model.config.role.Role;

public abstract class Players {
    private int playerID;
    private String username;
    private Role role;
    private ArrayList<Property> properties = new ArrayList<>();
    private int money = 500;
    private int position = 0;

    public Players(final int playerID, final String username, final Role role) {
        this.playerID = playerID;
        this.username = username;
        this.role = role;
    }

    public final void updatePosition(final int increment, final int max) {
        position = (position + increment) % max;
    }

    public final String getUsername() {
        return username;
    }

    public final int getMoney() {
        return money;
    }

    public final int getPosition() {
        return position;
    }

    public final String toString() {
        return username + " : " + money + "$, position : " + position + "\n";
    }

}
