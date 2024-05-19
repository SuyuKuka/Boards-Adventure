package model.config.cells;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public final class CellFactory {

    public static Cell createCell(final JSONObject jObject) {
        final CellType type = CellType.valueOf(jObject.getString("type").toUpperCase());
        final Integer id = jObject.getInt("id");
        final boolean playerStopBy = jObject.optBoolean("playerStopBy", false);
        final Integer nextCellID = jObject.optInt("nextCellID", id + 1);
        switch (type) {
            case PROPERTY: {
                final JSONArray buyPricesJArray = jObject.getJSONArray("prices");
                final ArrayList<Integer> buyPrices = new ArrayList<>();
                for (int i = 0; i < buyPricesJArray.length(); i++) {
                    buyPrices.add(buyPricesJArray.getInt(i));
                }
                final String groupHexColor = jObject.getString("groupHexColor");
                return new Property(id, playerStopBy, type, nextCellID, buyPrices, groupHexColor);
            }
            case KINGDOM:
                return new Kingdom(id, playerStopBy, type, nextCellID, jObject.optInt("throneCell", id));
            case START:
                return new Start(id, playerStopBy, type, nextCellID,  jObject.getInt("lapGain"));
            case CARD:
                return new Card(id, playerStopBy, type, nextCellID);
            case INTERSECTION: {
                final JSONArray nextCellIDsJArray = jObject.getJSONArray("nextCellID");
                final ArrayList<Integer> nextCellIDs = new ArrayList<>();
                for (int i = 0; i < nextCellIDsJArray.length(); i++) {
                    nextCellIDs.add(nextCellIDsJArray.getInt(i));
                }
                return new Intersection(id, true, type, nextCellIDs);
            }
            case SHOP:
                return new Shop(id, jObject.optBoolean("playerStopBy", true), type, nextCellID);
            case TELEPORTATION:
                return new Teleportation(id, playerStopBy, type, nextCellID, jObject.getInt("targetCell"));
            case JAIL:
                return new Jail(id, playerStopBy, type, nextCellID, jObject.optInt("targetCell", id));
            case COINS:
                return new Coins(id, playerStopBy, type, nextCellID, jObject.getInt("value"));
            case DANGER:
                return new Danger(id, playerStopBy, type, nextCellID);
            case VOID:
                return new Void(id, playerStopBy, type, nextCellID);
            case NONE:
                return new None(id, playerStopBy, type, nextCellID);
            default:
                break;
        }
        throw new IllegalArgumentException();
    }

}
