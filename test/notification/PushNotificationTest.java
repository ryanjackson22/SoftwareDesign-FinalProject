package notification;

import customer.Customer;
import customer.RegularCustomer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class PushNotificationTest {

    private PushNotification pushNotification;
    private Customer testCustomer;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        pushNotification = new PushNotification();

        // Create a test customer using anonymous class
        testCustomer = new RegularCustomer("John Smith", "johnsmith@test.com", "(111)555-5555") {
            {
                setEmail("test@example.com");
                setPhone("555-1234");
            }
        };

        // Redirect System.out to capture console output
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        // Restore original System.out
        System.setOut(originalOut);
    }

    @Test
    void testSendPushNotification() {
        // Arrange
        String message = "Hello, this is a test push notification";

        // Act
        pushNotification.send(testCustomer, message);

        // Assert
        String expectedOutput = "Sent Push Notification: 'Hello, this is a test push notification' to 555-1234";
        assertTrue(outContent.toString().trim().contains(expectedOutput),
                "Push notification should print the correct message");
    }

    @Test
    void testSendPushWithEmptyMessage() {
        // Arrange
        String message = "";

        // Act
        pushNotification.send(testCustomer, message);

        // Assert
        String expectedOutput = "Sent Push Notification: '' to 555-1234";
        assertTrue(outContent.toString().trim().contains(expectedOutput),
                "Push notification should handle empty messages");
    }

    @Test
    void testSendPushToCustomerWithPhone() {
        // Arrange
        String message = "Important push notification";

        // Act
        pushNotification.send(testCustomer, message);
        String output = outContent.toString().trim();

        // Assert
        assertTrue(output.contains("555-1234"),
                "Push notification should include customer's phone number");
        assertTrue(output.contains(message),
                "Push notification should include the message content");
        assertTrue(output.contains("Sent Push Notification:"),
                "Push notification should include push notification prefix");
    }

    @Test
    void testNotificationStrategyInterface() {
        // Assert
        assertInstanceOf(NotificationStrategy.class, pushNotification,
                "PushNotification should implement NotificationStrategy interface");
    }

    @Test
    void testPushWithLongMessage() {
        // Arrange
        String message = "This is a very long push notification message that contains multiple words and should still be handled correctly";

        // Act
        pushNotification.send(testCustomer, message);

        // Assert
        String output = outContent.toString().trim();
        assertTrue(output.contains(message),
                "Push notification should handle long messages");
    }

    @Test
    void testPushNotificationFormat() {
        // Arrange
        String message = "Test message";

        // Act
        pushNotification.send(testCustomer, message);

        // Assert
        String output = outContent.toString().trim();
        assertTrue(output.startsWith("Sent Push Notification:"),
                "Push notification should start with 'Sent Push Notification:'");
    }
}
