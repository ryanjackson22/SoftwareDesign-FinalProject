package notification;

import customer.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class SMSNotificationTest {

    private SMSNotification smsNotification;
    private Customer testCustomer;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        smsNotification = new SMSNotification();

        // Create a test customer using anonymous class
        testCustomer = new Customer("John Smith", "johnsmith@test.com", "(111)555-5555") {
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
    void testSendSMSNotification() {
        // Arrange
        String message = "Hello, this is a test SMS";

        // Act
        smsNotification.send(testCustomer, message);

        // Assert
        String expectedOutput = "Sent: 'Hello, this is a test SMS' to 555-1234";
        assertTrue(outContent.toString().trim().contains(expectedOutput),
                "SMS notification should print the correct message");
    }

    @Test
    void testSendSMSWithEmptyMessage() {
        // Arrange
        String message = "";

        // Act
        smsNotification.send(testCustomer, message);

        // Assert
        String expectedOutput = "Sent: '' to 555-1234";
        assertTrue(outContent.toString().trim().contains(expectedOutput),
                "SMS notification should handle empty messages");
    }

    @Test
    void testSendSMSToCustomerWithPhone() {
        // Arrange
        String message = "Important SMS notification";

        // Act
        smsNotification.send(testCustomer, message);
        String output = outContent.toString().trim();

        // Assert
        assertTrue(output.contains("555-1234"),
                "SMS notification should include customer's phone number");
        assertTrue(output.contains(message),
                "SMS notification should include the message content");
    }

    @Test
    void testNotificationStrategyInterface() {
        // Assert
        assertInstanceOf(NotificationStrategy.class, smsNotification,
                "SMSNotification should implement NotificationStrategy interface");
    }

    @Test
    void testSMSWithSpecialCharacters() {
        // Arrange
        String message = "Special chars: @#$%^&*()";

        // Act
        smsNotification.send(testCustomer, message);

        // Assert
        String output = outContent.toString().trim();
        assertTrue(output.contains(message),
                "SMS notification should handle special characters");
    }
}
