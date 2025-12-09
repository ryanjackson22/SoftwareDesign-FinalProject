package crm.observer.event;

import java.util.Date;

public class CRMEvent {
    private final EventType eventType;
    private final Date timestamp;

    public CRMEvent(EventType eventType) {
        this.eventType = eventType;
        this.timestamp = new Date();
    }

    public EventType getEventType() {
        return eventType;
    }

    public String toString() {
        String eventTypeStr = eventType.toString();
        String timestampStr = timestamp.toString();
        return String.format("Event Type: %s, Event Time: %s", eventTypeStr, timestampStr);
    }
}
