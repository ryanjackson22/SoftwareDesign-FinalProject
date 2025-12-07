package crm.observer.event;

public enum EventType {
    CONTACT_CREATED("Contact Created"),
    CONTACT_UPDATED("Contact Updated"),
    CONTACT_DELETED("Contact Deleted"),
    SALE_MADE("Sale Made"),
    NOTIFICATION_SENT("Notification Sent"),
    COMMAND_UNDONE("Command Undone"),
    NO_EVENT("No Event");

    private final String eventTypeStr;

    EventType(String str) {
        eventTypeStr = str;
    }

    public String toString() {
        return eventTypeStr;
    }
}
