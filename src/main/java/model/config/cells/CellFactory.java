package model.config.cells;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

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
                return new Property(id, type, buyPrices, group);
            }
            case KINGDOM:
                return new Kingdom(id, type, jObject.getInt("throneCell"));
            case START:
                return new Start(id, type, jObject.getInt("lapGain"));
            case CARD_ATTACK:
                return new Card(id, type);
            case CARD_DEFENSE:
                return new Card(id, type);
            case INTERSECTION: {
                final JSONArray intersectIDsJArray = jObject.getJSONArray("intersectIDs");
                final ArrayList<Integer> intersectIDs = new ArrayList<>();
                for (int i = 0; i < intersectIDsJArray.length(); i++) {
                    intersectIDs.add(intersectIDsJArray.getInt(i));
                }
                return new Intersection(id, type, intersectIDs);
            }
            case SHOP:
                return new Shop(id, type);
            case TELEPORTATION:
                return new Teleportation(id, type, jObject.getInt("targetCell"));
            case JAIL:
                return new Jail(id, type, jObject.getInt("targetCell"));
            case GAIN:
                return new Gain(id, type, jObject.getInt("gain"));
            case VOID:
                return new Void(id, type);
            default:
                break;
        }
        throw new IllegalArgumentException();
    }
}
