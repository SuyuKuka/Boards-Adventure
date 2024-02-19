package model.config.cells;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public final class CellFactory {

    public static Cell createCell(final JSONObject jObject) {
        final String type = jObject.getString("type");
        final Integer id = jObject.getInt("id");
        switch (type) {
            case "PROPERTY" : {
                final JSONArray buyPricesJArray = jObject.getJSONArray("values");
                final ArrayList<Integer> buyPrices = new ArrayList<>();
                for (int i = 0; i < buyPricesJArray.length(); i++) {
                    buyPrices.add(buyPricesJArray.getInt(i));
                }
                final int group = jObject.getInt("group");
                return new Property(id, buyPrices, group);
            }
            case "KINGDOM":
                return new Kingdom(id, jObject.getInt("throneCell"));
            case "START":
                return new Start(id, jObject.getInt("lapGain"));
            case "CARD":
                return new Card(id, CardType.valueOf(jObject.getString("card")));
            case "TELEPORTATION":
                return new Teleportation(id, jObject.getInt("targetCell"));
            case "TAX":
                return new Tax(id, jObject.getInt("value"));
            case "JAIL":
                return new Jail(id, jObject.getInt("targetCell"));
            case "VOID":
                return new Void(id);
            default:
                break;
        }
        throw new IllegalArgumentException();
    }
}
