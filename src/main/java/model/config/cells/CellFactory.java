package model.config.cells;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import model.config.Card;

public final class CellFactory {

    public static Cell createCell(final JSONObject jObject) {
        final CellType type = CellType.valueOf(jObject.getString("type").toUpperCase());
        final Integer id = jObject.getInt("id");
        switch (type) {
            case PROPERTY: {
                final JSONArray buyPricesJArray = jObject.getJSONArray("prices");
                final ArrayList<Integer> buyPrices = new ArrayList<>();
                for (int i = 0; i < buyPricesJArray.length(); i++) {
                    buyPrices.add(buyPricesJArray.getInt(i));
                }
                final int group = jObject.getInt("group");
                return new Property(id, type, jObject.optInt("nextCellID", id + 1), buyPrices, group);
            }
            case KINGDOM:
                return new Kingdom(id, type, jObject.optInt("nextCellID", id + 1), jObject.optInt("throneCell", id));
            case START:
                return new Start(id, type, jObject.optInt("nextCellID", id + 1),  jObject.getInt("lapGain"));
            case CARD:
                return new Card(id, type, jObject.optInt("nextCellID", id + 1));
            case INTERSECTION: {
                final JSONArray nextCellIDsJArray = jObject.getJSONArray("nextCellID");
                final ArrayList<Integer> nextCellIDs = new ArrayList<>();
                for (int i = 0; i < nextCellIDsJArray.length(); i++) {
                    nextCellIDs.add(nextCellIDsJArray.getInt(i));
                }
                return new Intersection(id, type, nextCellIDs);
            }
            case SHOP:
                return new Shop(id, type, jObject.optInt("nextCellID", id + 1));
            case TELEPORTATION:
                return new Teleportation(id, type, jObject.optInt("nextCellID", id + 1), jObject.getInt("targetCell"));
            case JAIL:
                return new Jail(id, type, jObject.optInt("nextCellID", id + 1), jObject.optInt("targetCell", id));
            case LOSS:
                return new Loss(id, type, jObject.optInt("nextCellID", id + 1), jObject.getInt("loss"));
            case GAIN:
                return new Gain(id, type, jObject.optInt("nextCellID", id + 1), jObject.getInt("gain"));
            case DANGER:
                return new Danger(id, type, jObject.optInt("nextCellID", id + 1));
            case VOID:
                return new Void(id, type, jObject.optInt("nextCellID", id + 1));
            default:
                break;
        }
        throw new IllegalArgumentException();
    }
}
