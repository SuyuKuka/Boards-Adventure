package model.config.cell;

public class CellFactory {
    public static Cell createCell(final int id, final String type, final Integer value, final Integer group) {
        switch (type) {
            case "PROPERTY":
                return new Property(id, value, group);
            case "KINGDOM":
                return new Kingdom(id);
            case "START":
                return new Event(EventType.START, id, value);
            case "CHANCE":
                return new Event(EventType.CHANCE, id, 0);
            case "CHEST":
                return new Event(EventType.CHEST, id, 0);
            case "TELEPORTATION":
                return new Event(EventType.TELEPORTATION, id, 0);
            case "TAX":
                return new Event(EventType.TAX, id, value);
            case "VOID":
                return new Event(EventType.VOID, id, 0);
            default:
                break;
        }
        throw new IllegalArgumentException();
    }
}
