package model.config.players;

import model.config.Team;
import model.config.items.Item;

public class HumanPlayer extends Player {

    public HumanPlayer(final int playerID, final String username, final Team team) {
        super(playerID, username, team, new Item("test"));
    }

}
