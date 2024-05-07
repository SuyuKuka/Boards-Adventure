package model.config.players;

import model.config.Team;
import model.config.items.Item;

public class ComputerPlayer extends Player {

    public ComputerPlayer(final String username, final String className, final Team team, final Item startItem) {
        super(username, className, team, startItem);
    }

}
