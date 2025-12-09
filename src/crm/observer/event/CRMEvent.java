package crm.observer.event;

import java.util.Date;

public class CRMEvent {
    private final EventType eventType;
    private final Date timestamp;
    private final Integer customerId;      // Optional - for events involving a specific customer
    private final String additionalInfo;   // Optional - flexible field for extra details

    // Constructor without additional data (backwards compatible)
    public CRMEvent(EventType eventType) {
        this(eventType, null, null);
    }

    // Constructor with customer ID only
    public CRMEvent(EventType eventType, Integer customerId) {
        this(eventType, customerId, null);
    }

    // Full constructor with all optional fields
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
