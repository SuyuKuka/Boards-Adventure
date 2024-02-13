package model.config.players;

import java.util.ArrayList;

import model.config.cell.Property;
import model.config.role.Role;

public class Players {
    private int playerID;
    private Role role;
    private ArrayList<Property> properties = new ArrayList<>();
    private int money = 0;
    private int position;

    public Players(final int playerID) {
        this.playerID = playerID;
    }
}
