package crm.observer.event;

import java.util.Date;

public class CRMEvent {
    private final EventType eventType;
    private final Date timestamp;
    private final Integer customerId;
    private final String additionalInfo;

    public CRMEvent(EventType eventType) {
        this(eventType, null, null);
    }

    public CRMEvent(EventType eventType, Integer customerId) {
        this(eventType, customerId, null);
    }

    public CRMEvent(EventType eventType, Integer customerId, String additionalInfo) {
        this.eventType = eventType;
        this.timestamp = new Date();
        this.customerId = customerId;
        this.additionalInfo = additionalInfo;
    }

    public EventType getEventType() {
        return eventType;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Type: %s, Time: %s", eventType.toString(), timestamp.toString()));

        if (customerId != null) {
            sb.append(String.format(", ID: %d", customerId));
        }

        if (additionalInfo != null && !additionalInfo.isEmpty()) {
            sb.append(String.format(", Info: %s", additionalInfo));
        }

        return sb.toString();
    }
}
