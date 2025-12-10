/*
 * 	Author: Ryan Jackson & Ryan Buck
 * 	Created: 12/10/2025
 * 	Final Project - Console Customer Relationship Management Tool
 */

package customer;

import crm.observer.event.EventType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Interaction {
    private final LocalDateTime timestamp;
    private final EventType eventType;
    private final String details;

    public Interaction(EventType eventType, String details) {
        this.timestamp = LocalDateTime.now();
        this.eventType = eventType;
        this.details = details;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public EventType getEventType() {
        return eventType;
    }

    public String getDetails() {
        return details;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("[%s] %s - %s",
            timestamp.format(formatter),
            eventType.toString(),
            details);
    }
}
