package model.config.cell;

public class Event extends Cell {

    private enum EventType {
        START, CHANCE, CHEST, SPECIAL;
    }

    private EventType eventType;

    public Event(final String name, final int price) {
        super(name, price);
    }

    public EventType getEventType() {
        return eventType;
    }
    
}
