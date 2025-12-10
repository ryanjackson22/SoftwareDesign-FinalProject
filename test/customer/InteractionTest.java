package customer;

import crm.observer.event.EventType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InteractionTest {

    @Test
    void testInteractionCreation() {
        // Arrange & Act
        Interaction interaction = new Interaction(EventType.CUSTOMER_CREATED, "Customer record created");

        // Assert
        assertNotNull(interaction, "Interaction should not be null");
        assertEquals(EventType.CUSTOMER_CREATED, interaction.getEventType());
        assertEquals("Customer record created", interaction.getDetails());
        assertNotNull(interaction.getTimestamp(), "Timestamp should not be null");
    }

    @Test
    void testInteractionTimestampIsRecent() {
        // Arrange
        LocalDateTime before = LocalDateTime.now();

        // Act
        Interaction interaction = new Interaction(EventType.SALE_MADE, "Sale completed");
        LocalDateTime after = LocalDateTime.now();

        // Assert
        LocalDateTime timestamp = interaction.getTimestamp();
        assertTrue(timestamp.isAfter(before.minusSeconds(1)) && timestamp.isBefore(after.plusSeconds(1)),
                "Timestamp should be between before and after times");
    }

    @Test
    void testInteractionWithDifferentEventTypes() {
        // Arrange & Act
        Interaction created = new Interaction(EventType.CUSTOMER_CREATED, "Created");
        Interaction updated = new Interaction(EventType.CUSTOMER_UPDATED, "Updated");
        Interaction deleted = new Interaction(EventType.CUSTOMER_DELETED, "Deleted");
        Interaction sale = new Interaction(EventType.SALE_MADE, "Sale");
        Interaction notification = new Interaction(EventType.NOTIFICATION_SENT, "Notification");

        // Assert
        assertEquals(EventType.CUSTOMER_CREATED, created.getEventType());
        assertEquals(EventType.CUSTOMER_UPDATED, updated.getEventType());
        assertEquals(EventType.CUSTOMER_DELETED, deleted.getEventType());
        assertEquals(EventType.SALE_MADE, sale.getEventType());
        assertEquals(EventType.NOTIFICATION_SENT, notification.getEventType());
    }

    @Test
    void testInteractionToString() {
        // Arrange
        Interaction interaction = new Interaction(EventType.CUSTOMER_CREATED, "Test details");

        // Act
        String result = interaction.toString();

        // Assert
        assertTrue(result.contains("Customer Created"), "Should contain event type");
        assertTrue(result.contains("Test details"), "Should contain details");
        assertTrue(result.matches(".*\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}.*"),
                "Should contain formatted timestamp");
    }

    @Test
    void testInteractionWithEmptyDetails() {
        // Arrange & Act
        Interaction interaction = new Interaction(EventType.NOTIFICATION_SENT, "");

        // Assert
        assertEquals("", interaction.getDetails());
        assertNotNull(interaction.toString());
    }

    @Test
    void testInteractionWithLongDetails() {
        // Arrange
        String longDetails = "This is a very long detail string that contains lots of information " +
                "about what happened during this interaction with the customer. " +
                "It might include multiple pieces of information and specific details.";

        // Act
        Interaction interaction = new Interaction(EventType.CUSTOMER_UPDATED, longDetails);

        // Assert
        assertEquals(longDetails, interaction.getDetails());
        assertTrue(interaction.toString().contains(longDetails));
    }

    @Test
    void testMultipleInteractionsHaveDifferentTimestamps() throws InterruptedException {
        // Arrange & Act
        Interaction interaction1 = new Interaction(EventType.CUSTOMER_CREATED, "First");
        Thread.sleep(10); // Small delay to ensure different timestamps
        Interaction interaction2 = new Interaction(EventType.CUSTOMER_UPDATED, "Second");

        // Assert
        assertNotEquals(interaction1.getTimestamp(), interaction2.getTimestamp(),
                "Different interactions should have different timestamps");
        assertTrue(interaction2.getTimestamp().isAfter(interaction1.getTimestamp()),
                "Second interaction should have later timestamp");
    }

    @Test
    void testInteractionImmutability() {
        // Arrange
        Interaction interaction = new Interaction(EventType.SALE_MADE, "Original details");
        EventType originalType = interaction.getEventType();
        String originalDetails = interaction.getDetails();
        LocalDateTime originalTimestamp = interaction.getTimestamp();

        // Act - attempting to get values (no setters should exist)
        EventType retrievedType = interaction.getEventType();
        String retrievedDetails = interaction.getDetails();
        LocalDateTime retrievedTimestamp = interaction.getTimestamp();

        // Assert - values should be the same
        assertEquals(originalType, retrievedType);
        assertEquals(originalDetails, retrievedDetails);
        assertEquals(originalTimestamp, retrievedTimestamp);
    }
}
