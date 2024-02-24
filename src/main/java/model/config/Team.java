package model.config;

import java.util.ArrayList;
import java.util.HashMap;

import model.config.cells.Property;

public final class Team {

    private int teamID;
    private String teamName;

    private int gamePoint = 0;
    private HashMap<Integer, ArrayList<Property>> properties = new HashMap<>();
    private int money = 500;

    public Team(final int teamID, final String teamName) {
        this.teamID = teamID;
        this.teamName = teamName;
    }

    public void addProperty(final Property property) {
        final int propertyGroup = property.getGroup();
        if (!properties.containsKey(propertyGroup)) {
            properties.put(propertyGroup, new ArrayList<>());
        }
        properties.get(propertyGroup).add(property);
    }

    public void removeProperty(final Property property) {
        final int propertyGroup = property.getGroup();
        final ArrayList<Property> groupPropertiesArray = properties.get(propertyGroup);
        if (groupPropertiesArray.size() == 1) {
            properties.remove(propertyGroup);
        } else {
            groupPropertiesArray.remove(property);
        }
    }

    public boolean hasEnoughMoney(final int price) {
        return this.money >= price;
    }

    public void incrementGamePoint(final int increment) {
        this.gamePoint += increment;
    }

    public void incrementMoney(final int increment) {
        this.money += increment;
    }

    // region getters/setters

    public int getMoney() {
        return money;
    }

    public HashMap<Integer, ArrayList<Property>> getProperties() {
        return properties;
    }

    public String getTeamName() {
        return teamName;
    }

    public int getGamePoint() {
        return gamePoint;
    }

    // endregion
}
