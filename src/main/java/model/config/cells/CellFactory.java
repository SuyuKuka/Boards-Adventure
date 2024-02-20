package model.config.cells;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public final class CellFactory {

    public static Cell createCell(final JSONObject jObject) {
        final CellType type = CellType.valueOf(jObject.getString("type").toUpperCase());
        final Integer id = jObject.getInt("id");
        switch (type) {
            case PROPERTY : {
                final JSONArray buyPricesJArray = jObject.getJSONArray("values");
                final ArrayList<Integer> buyPrices = new ArrayList<>();
                for (int i = 0; i < buyPricesJArray.length(); i++) {
                    buyPrices.add(buyPricesJArray.getInt(i));
                }
                final int group = jObject.getInt("group");
                return new Property(id, buyPrices, group);
            }
            case KINGDOM:
                return new Kingdom(id, jObject.getInt("throneCell"));
            case START:
                return new Start(id, jObject.getInt("lapGain"));
            case CARD_ATTACK:
                return new Card(id, CellType.CARD_ATTACK);
            case CARD_DEFENSE:
                return new Card(id, CellType.CARD_DEFENSE);
            case TELEPORTATION:
                return new Teleportation(id, jObject.getInt("targetCell"));
            case TAX:
                return new Tax(id, jObject.getInt("value"));
            case JAIL:
                return new Jail(id, jObject.getInt("targetCell"));
            case VOID:
                return new Void(id);
            default:
                break;
        }
        throw new IllegalArgumentException();
    }
}
