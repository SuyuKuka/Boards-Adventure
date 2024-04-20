package model.config.players;

import model.config.Team;
import model.config.items.Item;

public class ComputerPlayer extends Player {

    public ComputerPlayer(final int playerID, final String username, final Team team) {
        super(playerID, username, team, null);
    }
}
