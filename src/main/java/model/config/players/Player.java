package model.config.players;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import model.config.Dice;
import model.config.cells.Property;

public abstract class Player {
    private static final Random RAND = new Random();

    private int playerID;
    private String username;
    private HashMap<Integer, ArrayList<Property>> properties = new HashMap<>();
    private ArrayList<Dice> dices = new ArrayList<>();
    private int jailTurn = 0;
    private int mapTurn = 0;
    private int money = 500;
    private int position = 0;

    private PlayerState playerState = PlayerState.NORMAL;

    public Player(final int playerID, final String username) {
        this.playerID = playerID;
        this.username = username;
        dices.add(new Dice());
    }

    public final void addProperty(final Property property) {
        final int propertyID = property.getId();
        if (!properties.containsKey(propertyID)) {
            properties.put(propertyID, new ArrayList<>());
        }
        properties.get(propertyID).add(property);
    }

    public final void playDice() {
        ArrayList<Integer> values = dices.get(0).getValues();
        position += values.get(RAND.nextInt(values.size()));
    }

    public final void incrementPosition(final int increment) {
        position += increment;
    }

    public final void incrementMoney(final int increment) {
        money += increment;
    }

    public final String getUsername() {
        return username;
    }

    public final void incrementJailTurn(final int increment) {
        jailTurn += increment;
    }

    public void setJailTurn(final int jailTurn) {
        this.jailTurn = jailTurn;
    }

    public final int getJailTurn() {
        return jailTurn;
    }

    public final int getMoney() {
        return money;
    }

    public final void setPosition(final int position) {
        this.position = position;
    }

    public final int getPosition() {
        return position;
    }

    public final void incrementMapTurn(final int increment) {
        mapTurn += increment;
    }

    public final int getMapTurn() {
        return mapTurn;
    }

    public void setPlayerState(final PlayerState playerState) {
        this.playerState = playerState;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public final String toString() {
        return username + " : " + money + "$, position : " + position;
    }

}
