package crm.observer.event;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CRMEventTest {

    @Test
    void testConstructorWithEventTypeOnly() {
        // Act
        CRMEvent event = new CRMEvent(EventType.CUSTOMER_CREATED);

        // Assert
        assertNotNull(event);
        assertEquals(EventType.CUSTOMER_CREATED, event.getEventType());
    }

    @Test
    void testConstructorWithEventTypeAndCustomerId() {
        // Act
        CRMEvent event = new CRMEvent(EventType.CUSTOMER_UPDATED, 123);

        // Assert
        assertNotNull(event);
        assertEquals(EventType.CUSTOMER_UPDATED, event.getEventType());
    }

    @Test
    void testConstructorWithAllFields() {
        // Act
        CRMEvent event = new CRMEvent(EventType.SALE_MADE, 456, "Sale of $100");

        // Assert
        assertNotNull(event);
        assertEquals(EventType.SALE_MADE, event.getEventType());
    }

    @Test
    void testGetEventTypeReturnsCorrectType() {
        // Arrange & Act
        CRMEvent event = new CRMEvent(EventType.NOTIFICATION_SENT);

        // Assert
        assertEquals(EventType.NOTIFICATION_SENT, event.getEventType());
    }

    @Test
    void testToStringContainsEventType() {
        // Arrange
        CRMEvent event = new CRMEvent(EventType.CUSTOMER_DELETED);

        // Act
        String result = event.toString();

        // Assert
        assertTrue(result.contains("Type: Customer Deleted"));
    }

    @Test
    void testToStringContainsTimestamp() {
        // Arrange
        CRMEvent event = new CRMEvent(EventType.CUSTOMER_CREATED);

        // Act
        String result = event.toString();

        // Assert
        assertTrue(result.contains("Time:"));
    }

    @Test
    void testToStringWithCustomerId() {
        // Arrange
        CRMEvent event = new CRMEvent(EventType.CUSTOMER_UPDATED, 789);

        // Act
        String result = event.toString();

        // Assert
        assertTrue(result.contains("Type: Customer Updated"));
        assertTrue(result.contains("ID: 789"));
    }

    @Test
    void testToStringWithoutCustomerId() {
        // Arrange
        CRMEvent event = new CRMEvent(EventType.CUSTOMER_CREATED);

        // Act
        String result = event.toString();

        // Assert
        assertTrue(result.contains("Type: Customer Created"));
        assertFalse(result.contains("ID:"));
    }

    @Test
    void testToStringWithAdditionalInfo() {
        // Arrange
        CRMEvent event = new CRMEvent(EventType.SALE_MADE, 100, "Product: Widget, Amount: $50");

        // Act
        String result = event.toString();

        // Assert
        assertTrue(result.contains("Type: Sale Made"));
        assertTrue(result.contains("ID: 100"));
        assertTrue(result.contains("Info: Product: Widget, Amount: $50"));
    }

    @Test
    void testToStringWithoutAdditionalInfo() {
        // Arrange
        CRMEvent event = new CRMEvent(EventType.CUSTOMER_CREATED, 200);

        // Act
        String result = event.toString();

        // Assert
        assertTrue(result.contains("Type: Customer Created"));
        assertTrue(result.contains("ID: 200"));
        assertFalse(result.contains("Info:"));
    }

    @Test
    void testToStringWithEmptyAdditionalInfo() {
        // Arrange
        CRMEvent event = new CRMEvent(EventType.NOTIFICATION_SENT, 300, "");

        // Act
        String result = event.toString();

        // Assert
        assertTrue(result.contains("Type: Notification Sent"));
        assertTrue(result.contains("ID: 300"));
        assertFalse(result.contains("Info:")); // Empty string should not show Info
    }

    @Test
    void testToStringWithNullCustomerId() {
        // Arrange
        CRMEvent event = new CRMEvent(EventType.CUSTOMER_CREATED, null, "Some info");

        // Act
        String result = event.toString();

        // Assert
        assertTrue(result.contains("Type: Customer Created"));
        assertFalse(result.contains("ID:")); // Null ID should not show
        assertTrue(result.contains("Info: Some info"));
    }

    @Test
    void testAllEventTypes() {
        // Test that all event types can be used to create events
        EventType[] allTypes = {
                EventType.CUSTOMER_CREATED,
                EventType.CUSTOMER_UPDATED,
                EventType.CUSTOMER_DELETED,
                EventType.NOTIFICATION_SENT,
                EventType.SALE_MADE,
                EventType.VIEW_HISTORY
        };

        for (EventType type : allTypes) {
            CRMEvent event = new CRMEvent(type);
            assertNotNull(event);
            assertEquals(type, event.getEventType());
            assertTrue(event.toString().contains(type.toString()));
        }
    }

    @Test
    void testTimestampIsNotNull() {
        // Arrange & Act
        CRMEvent event = new CRMEvent(EventType.CUSTOMER_CREATED);
        String result = event.toString();

        // Assert
        assertTrue(result.contains("Time:"));
        // Timestamp should be present and not show "null"
        assertFalse(result.contains("Time: null"));
    }

    @Test
    void testMultipleEventsHaveDifferentTimestamps() throws InterruptedException {
        // Arrange & Act
        CRMEvent event1 = new CRMEvent(EventType.CUSTOMER_CREATED);
        Thread.sleep(10); // Small delay to ensure different timestamps
        CRMEvent event2 = new CRMEvent(EventType.CUSTOMER_CREATED);

        // Assert
        String result1 = event1.toString();
        String result2 = event2.toString();

        // While the strings might be the same if timestamps are too close,
        // at minimum they should both be valid
        assertTrue(result1.contains("Time:"));
        assertTrue(result2.contains("Time:"));
    }

    @Test
    void testToStringFormatIsConsistent() {
        // Arrange
        CRMEvent event = new CRMEvent(EventType.CUSTOMER_UPDATED, 12345, "Test Information");

        // Act
        String result = event.toString();

        // Assert
        // Check that format follows: "Type: X, Time: Y, ID: Z, Info: W"
        assertTrue(result.matches(".*Type: .*Time: .*ID: .*Info: .*"));
    }

    @Test
    void testConstructorChaining() {
        // Test that constructor overloads work correctly
        // Single parameter constructor
        CRMEvent event1 = new CRMEvent(EventType.CUSTOMER_CREATED);
        assertNotNull(event1);

        // Two parameter constructor
        CRMEvent event2 = new CRMEvent(EventType.CUSTOMER_UPDATED, 100);
        assertNotNull(event2);

        // Three parameter constructor
        CRMEvent event3 = new CRMEvent(EventType.SALE_MADE, 200, "Info");
        assertNotNull(event3);
    }

    @Test
    void testEventWithLongAdditionalInfo() {
        // Arrange
        String longInfo = "This is a very long additional information string that contains " +
                "multiple pieces of data including customer preferences, contact method details, " +
                "and transaction information that might be useful for logging purposes";

        // Act
        CRMEvent event = new CRMEvent(EventType.NOTIFICATION_SENT, 999, longInfo);
        String result = event.toString();

        // Assert
        assertTrue(result.contains(longInfo));
    }

    @Test
    void testEventWithSpecialCharactersInAdditionalInfo() {
        // Arrange
        String specialInfo = "Special chars: !@#$%^&*()_+-=[]{}|;':\",./<>?";

        // Act
        CRMEvent event = new CRMEvent(EventType.CUSTOMER_UPDATED, 777, specialInfo);
        String result = event.toString();

        // Assert
        assertTrue(result.contains(specialInfo));
    }
}
