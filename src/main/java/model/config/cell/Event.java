package model.config.cell;

public class Event extends Cell {

    private final EventType eventType;

    public Event(final EventType eventType, final int id, final int price) {
        super(id, price);
        this.eventType = eventType;
    }

    public final EventType getEventType() {
        return eventType;
    }

}
