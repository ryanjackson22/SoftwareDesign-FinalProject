package crm.observer.event;

public enum EventType {
    CUSTOMER_CREATED("Customer Created"),
    CUSTOMER_UPDATED("Customer Updated"),
    CUSTOMER_DELETED("Customer Deleted"),
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
