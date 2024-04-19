package model.config.cells;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public final class CellFactory {

    public static Cell createCell(final JSONObject jObject) {
        final CellType type = CellType.valueOf(jObject.getString("type").toUpperCase());
        final Integer id = jObject.getInt("id");
        final boolean playerStop = jObject.optBoolean("playerStop", false);
        final Integer nextCellID = jObject.optInt("nextCellID", id + 1);
        switch (type) {
            case PROPERTY: {
                final JSONArray buyPricesJArray = jObject.getJSONArray("prices");
                final ArrayList<Integer> buyPrices = new ArrayList<>();
                for (int i = 0; i < buyPricesJArray.length(); i++) {
                    buyPrices.add(buyPricesJArray.getInt(i));
                }
                final int group = jObject.getInt("group");
                return new Property(id, playerStop, type, nextCellID, buyPrices, group);
            }
            case KINGDOM:
                return new Kingdom(id, playerStop, type, nextCellID, jObject.optInt("throneCell", id));
            case START:
                return new Start(id, playerStop, type, nextCellID,  jObject.getInt("lapGain"));
            case CARD:
                return new Card(id, playerStop, type, nextCellID);
            case INTERSECTION: {
                final JSONArray nextCellIDsJArray = jObject.getJSONArray("nextCellID");
                final ArrayList<Integer> nextCellIDs = new ArrayList<>();
                for (int i = 0; i < nextCellIDsJArray.length(); i++) {
                    nextCellIDs.add(nextCellIDsJArray.getInt(i));
                }
                return new Intersection(id, false, type, nextCellIDs);
            }
            case SHOP:
                return new Shop(id, playerStop, type, nextCellID);
            case TELEPORTATION:
                return new Teleportation(id, playerStop, type, nextCellID, jObject.getInt("targetCell"));
            case JAIL:
                return new Jail(id, playerStop, type, nextCellID, jObject.optInt("targetCell", id));
            case LOSS:
                return new Loss(id, playerStop, type, nextCellID, jObject.getInt("loss"));
            case GAIN:
                return new Gain(id, playerStop, type, nextCellID, jObject.getInt("gain"));
            case DANGER:
                return new Danger(id, playerStop, type, nextCellID);
            case VOID:
                return new Void(id, playerStop, type, nextCellID);
            default:
                break;
        }
        throw new IllegalArgumentException();
    }
}
