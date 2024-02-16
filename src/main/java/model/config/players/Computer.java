package model.config.players;

import model.config.role.Role;

public class Computer extends Players {

    public Computer(final int playerID, final String username, final Role role) {
        super(playerID, username, role);
    }
}
